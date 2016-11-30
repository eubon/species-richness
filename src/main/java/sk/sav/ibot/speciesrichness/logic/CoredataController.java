/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.TransformerUtils;
import sk.sav.ibot.speciesrichness.geo.Cell;
import sk.sav.ibot.speciesrichness.geo.Grid;
import sk.sav.ibot.speciesrichness.geo.LatLon;
import sk.sav.ibot.speciesrichness.json.gbif.GbifTaxon;
import sk.sav.ibot.speciesrichness.rest.results.ResultCell;
import sk.sav.ibot.speciesrichness.rest.results.ResultItems;
import sk.sav.ibot.speciesrichness.rest.results.SearchTerms;
import sk.sav.ibot.speciesrichness.model.Coredata;
import sk.sav.ibot.speciesrichness.rest.results.Layer;
import sk.sav.ibot.speciesrichness.rest.results.ResultCellBounds;
import sk.sav.ibot.speciesrichness.services.CoredataService;

/**
 * This class does the logic of obtaining the correct data
 *
 * @author Matus
 */
public class CoredataController {

    private final CoredataService coredataService;

    public CoredataController(CoredataService coredataService) {
        this.coredataService = coredataService;
    }

    /**
     * Computes grid - layers of cells - from the user-defined input and puts it
     * out as a ResultItems object. Retrieves all species from GBIF API by given
     * higher taxon. Fetches ocurrences for these species. Creates cells in a
     * range of years in user-defined grid boundaries, and cell width.
     *
     * @param search Search terms
     * @param species List of species to search
     * @return
     */
    public ResultItems retrieveResults(SearchTerms search, List<GbifTaxon> species) {
        if (search == null) {
            throw new IllegalArgumentException("search is null");
        }
        if (species == null) {
            throw new IllegalArgumentException("species is null");
        }
        List<Integer> taxonkeys = (List) CollectionUtils.collect(species, TransformerUtils.invokerTransformer("getKey"));
        List<Coredata> data = this.coredataService.getCoredataByTaxonkeys(taxonkeys, search.getYearFrom(), search.getYearTo());
        Grid grid = new Grid(new LatLon(search.getBoundsSouth(), search.getBoundsWest()), new LatLon(search.getBoundsNorth(), search.getBoundsEast()));
        grid.occurencesInGrid(search.getSpatialResolution(), search.getTemporalResolution(), search.getYearFrom(), data);

        List<Cell> cells = grid.getCells(); //get cells
        convergeCells(cells, search.getTemporalResolution(), search.getYearFrom()); //converge them
        List<Cell> cleanedCells = tidyUpCells(cells); //clean them
        Map<Integer, List<Cell>> mappedCells = makeMap(cleanedCells); //arrange them in a map
        List<Layer> results = makeResultLayers(mappedCells);
        ResultItems items = new ResultItems(search, results);
        return items;
    }

    /**
     * Arranges layers of cells as result objects ready for output.
     *
     * @param cells All cells of the grid
     * @return Map of lists of result items hashed by year
     */
    public Map<String, List<ResultCell>> makeResultsMap(Map<Integer, List<Cell>> cells) {
        if (cells == null) {
            throw new IllegalArgumentException("cells is null");
        }
        Map<String, List<ResultCell>> results = new HashMap<>();
        for (Integer keyYear : cells.keySet()) {
            List<ResultCell> resultList = new LinkedList<>();
            for (Cell cell : cells.get(keyYear)) {
                ResultCell resultCell = new ResultCell(new ResultCellBounds(cell.getBottomLeft(), cell.getTopRight()),
                        cell.getYear(), cell.getNumOccurences(), cell.getNumSpecies(), cell.getSpecies());
                resultList.add(resultCell);
            }
            results.put(String.valueOf(keyYear), resultList);
        }
        return results;
    }

    /**
     * Arranges layers of cells into JAXB friendly list of objects.
     * @param cells Cells of the grid mapped to the year they belong to
     * @return list of Layer object where each layer contains list of cells belonging to specified year
     */
    public List<Layer> makeResultLayers(Map<Integer, List<Cell>> cells) {
        if (cells == null) {
            throw new IllegalArgumentException("cells is null");
        }
        List<Layer> layers = new LinkedList<>();
        for (Integer year : cells.keySet()) {
            List<ResultCell> resultList = new LinkedList<>();
            for (Cell cell : cells.get(year)) {
                ResultCell resultCell = new ResultCell(new ResultCellBounds(cell.getBottomLeft(), cell.getTopRight()),
                        cell.getYear(), cell.getNumOccurences(), cell.getNumSpecies(), cell.getSpecies());
                resultList.add(resultCell);
            }
            Layer layer = new Layer(year, resultList);
            layers.add(layer);
        }
        return layers;
    }

    /**
     * This method arranges the list of cells of the grid into lists of cells
     * grouped by the respective years. The lists are arranged in a map hashed
     * by the corresponding year.
     *
     * @param cells List of cells
     * @return Map hashed by cell year as string
     */
    public Map<Integer, List<Cell>> makeMap(List<Cell> cells) {
        if (cells == null) {
            throw new IllegalArgumentException("cells is null");
        }
        Map<Integer, List<Cell>> cellMap = new HashMap<>();
        for (Cell cell : cells) {
            int key = cell.getYear();
            List<Cell> items;
            if (cellMap.containsKey(key)) {
                items = cellMap.get(key);
            } else {
                items = new LinkedList<>();
            }
            items.add(cell);
            cellMap.put(key, items);
        }
        return cellMap;
    }

    /**
     * Produces a list of cells where each cell is unique with cumulative number
     * of records and species for duplicate cells (according to Cell.equals
     * method) in the disrty list.
     *
     * @param cells The dirty list with cell duplicates
     * @return ArrayList of unique cells
     */
    public List<Cell> tidyUpCells(List<Cell> cells) {
        if (cells == null) {
            throw new IllegalArgumentException("cells is null");
        }
        List<Cell> cleaned = new ArrayList<>();
        for (Cell cell : cells) {
            if (cleaned.contains(cell)) { //cell exists in the clean list
                //get the cell in clean and update its occurences
                Cell cellInClean = cleaned.get(cleaned.indexOf(cell));
                cellInClean.addNumOccurences(cell.getNumOccurences());
                cellInClean.addSpecies(cell.getSpecies());
            } else {
                //or add the cell to the clean list if it is not present yet
                cleaned.add(cell);
            }
        }
        return cleaned;
    }

    /**
     * Each cell is checked for its year, the cell is then assigned the closest
     * greater year according to the step and start. See
     * CoredataController.convergeTo(int, int, int)
     *
     * @param cells original list of cells
     * @param step step over years (every "n-th" year)
     * @param start year to start at, it is not modified
     * @return list of cells with modified years
     */
    public List<Cell> convergeCells(List<Cell> cells, int step, int start) {
        if (cells == null) {
            throw new IllegalArgumentException("cells is null");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive number");
        }
        if (start <= 0) {
            throw new IllegalArgumentException("start must be positive number");
        }
        for (Cell cell : cells) {
            int year = cell.getYear();
            int convergedYear = convergeTo(year, step, start);
            cell.setYear(convergedYear);
        }
        return cells;
    }

    /**
     * Creates a closest greater or equal number A to subject such that A = x *
     * step + start and A >= what. E.g. start = 2, step = 5. For subject = {3,
     * 4, 5, 6, 7} result = 7. For subject = {8, 9, 10, 11, 12} result = 12,
     * etc.
     *
     * @param subject number to transform
     * @param step
     * @param start beginning to make convergence from, must be lower or equal
     * to subject
     * @return closest greater or equal number to subject
     */
    public int convergeTo(final int subject, final int step, final int start) {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive number");
        }
        if (subject < start) {
            throw new IllegalArgumentException("start is bigger than what");
        }
        int whatShifted = subject - start;
        int c = (int) whatShifted / step;
        int mod = whatShifted % step;
        if (mod > 0) {
            mod = 1;
        }
        int r = (c + mod) * step + start;
        return r;
    }

}
