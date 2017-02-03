/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.TransformerUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.sav.ibot.speciesrichness.geo.Cell;
import sk.sav.ibot.speciesrichness.geo.Grid;
import sk.sav.ibot.speciesrichness.geo.LatLon;
import sk.sav.ibot.speciesrichness.rest.results.ResultCell;
import sk.sav.ibot.speciesrichness.rest.results.ResultItems;
import sk.sav.ibot.speciesrichness.rest.results.SearchTerms;
import sk.sav.ibot.speciesrichness.model.Coredata;
import sk.sav.ibot.speciesrichness.rest.results.Layer;
import sk.sav.ibot.speciesrichness.rest.results.ResultCellBounds;
import sk.sav.ibot.speciesrichness.rest.results.ResultSpecies;
import sk.sav.ibot.speciesrichness.services.CoredataService;

/**
 * This class does the logic of obtaining the correct data
 *
 * @author Matus
 */
@Service(value = "coredataController")
public class CoredataController {

    @Autowired
    private CoredataService coredataService;

    public void setCoredataService(CoredataService coredataService) {
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
    public ResultItems retrieveResults(SearchTerms search, Collection<NameUsage> species) {
        if (search == null) {
            throw new IllegalArgumentException("search is null");
        }
        if (species == null) {
            throw new IllegalArgumentException("species is null");
        }
        //gbif keys are extracted from the objects
        List<Integer> taxonkeys = (List) CollectionUtils.collect(species, TransformerUtils.invokerTransformer("getKey"));
        //search for those keys in the database, restrict by years
        List<Coredata> data = this.coredataService.getCoredataByTaxonkeys(taxonkeys, search.getYearFrom(), search.getYearTo());
        //create a grid
        Grid grid = new Grid(new LatLon(search.getBoundsSouth(), search.getBoundsWest()), new LatLon(search.getBoundsNorth(), search.getBoundsEast()));
        //compute the cells in grid from result data
        //the set of keys found among the results is the sideproduct
        Set<Integer> taxaUsed = grid.occurencesInGrid(search.getSpatialResolution(), data, search.getTaxonGbifKey());
        //we use those taxa to create a map of used species - key, taxon
        Map<Integer, NameUsage> hashedNames = makeUsedSpecies(taxaUsed, species);

        List<Cell> cells = grid.getCells(); //get cells
        convergeCells(cells, search.getTemporalResolution(), search.getYearFrom(), search.getYearTo()); //converge them
        List<Cell> cleanedCells = tidyUpCells(cells); //clean them
        Map<Integer, List<Cell>> mappedCells = makeMap(cleanedCells); //arrange them in a map
        List<Layer> results = makeResultLayers(mappedCells, hashedNames);
        ResultItems items = new ResultItems(search, results);
        return items;
    }

    /**
     * Arranges layers of cells as result objects ready for output.
     *
     * @param cells All cells of the grid
     * @return Map of lists of result items hashed by year
     */
    /*
    public Map<String, List<ResultCell>> makeResultsMap(Map<Integer, List<Cell>> cells) {
        if (cells == null) {
            throw new IllegalArgumentException("cells is null");
        }
        Map<String, List<ResultCell>> results = new HashMap<>();
        for (Integer keyYear : cells.keySet()) {
            List<ResultCell> resultList = new LinkedList<>();
            for (Cell cell : cells.get(keyYear)) {
                ResultCell resultCell = new ResultCell(new ResultCellBounds(cell.getBottomLeft(), cell.getTopRight()),
                        cell.getYear(), cell.getNumOccurences(), cell.getTaxonOccurences(), cell.getNumSpecies(), SpeciesGbifClient.keysToSpecies(cell.getSpecies()));
                resultList.add(resultCell);
            }
            results.put(String.valueOf(keyYear), resultList);
        }
        return results;
    }
    */

    /**
     * Arranges layers of cells into JAXB friendly list of objects.
     * Each cell contains a set of species in form of taxa keys. These keys are
     * looked at the map of names and sets of ResultSpecies are created for each cell.
     * @param cells Cells of the grid mapped to the year they belong to
     * @param names map the names are fetched from for taxonkeys of each cell
     * @return list of Layer object where each layer contains list of cells belonging to specified year
     */
    public List<Layer> makeResultLayers(Map<Integer, List<Cell>> cells, Map<Integer, NameUsage> names) {
        if (cells == null) {
            throw new IllegalArgumentException("cells is null");
        }
        List<Layer> layers = new LinkedList<>();
        for (Integer year : cells.keySet()) {
            List<ResultCell> resultList = new LinkedList<>();
            for (Cell cell : cells.get(year)) {
                //convert set of keys to objects of ResultSpecies
                Set<ResultSpecies> resultSpcs = new HashSet<>(cell.getNumSpecies());
                for (Integer spec : cell.getSpecies()) {
                    NameUsage rs = names.get(spec);
                    if (rs == null) {
                        Logger.getLogger(CoredataController.class.getName()).debug("no name found in the map - is null - for key " + spec);
                    } else {
                        resultSpcs.add(new ResultSpecies(rs.getKey(), rs.getScientificName()));
                    }
                }
                //create jaxb cell object
                ResultCell resultCell = new ResultCell(new ResultCellBounds(cell.getBottomLeft(), cell.getTopRight()),
                        cell.getYear(), cell.getNumOccurences(), cell.getTaxonOccurences(), cell.getNumSpecies(), resultSpcs);
                //add cell to the results
                resultList.add(resultCell);
            }
            //add year layer to the results
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
     * Produces a list of cells where each cell is unique. Number
     * of records and species for duplicate cells cumulate into their respective cell they belong to
     * (according to Cell.equals method) in the dirty list.
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
                cellInClean.addTaxonOccurences(cell.getTaxonOccurences());
            } else {
                //or add the cell to the clean list if it is not present yet
                cleaned.add(cell);
            }
        }
        return cleaned;
    }
    
    /**
     * Selects only those names from allnames whose keys are present int the taxaKeys
     * @param taxaKeys set of keys the names are identified by
     * @param allNames collection of all names we choose from
     * @return map where keys are unique gbif keys and values are objects identified by those keys
     */
    public Map<Integer, NameUsage> makeUsedSpecies(Set<Integer> taxaKeys, Collection<NameUsage> allNames) {
        Map<Integer, NameUsage> names = new HashMap<>(taxaKeys.size());
        for (NameUsage aName : allNames) {
            if (taxaKeys.contains(aName.getKey())) {
                names.put(aName.getKey(), aName);
            }
        }
        return names;
    }
    
    /**
     * Each cell is checked for its year, the cell is then assigned the closest
     * greater year according to the step and start. See
     * CoredataController.convergeTo(int, int, int)
     *
     * @param cells original list of cells
     * @param step step over years (every "n-th" year)
     * @param start year to start at, it is not modified
     * @param end year to finish at
     * @return list of cells with modified years
     */
    public List<Cell> convergeCells(List<Cell> cells, int step, int start, int end) {
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
            int convergedYear = CoredataController.convergeTo(year, step, start, end);
            cell.setYear(convergedYear);
        }
        return cells;
    }

    /**
     * Creates a closest greater or equal number A to subject such that 
     * A = x * step + start and A >= subject. The converged value does not exceed the end value.
     * E.g. start = 2, step = 5, end = 11. Subject 2 results in 2. 
     * For subject = {3, 4, 5, 6, 7} result = 7. 
     * For subject = {8, 9, 10, 11} result = 11 (reached end value)
     * etc.
     *
     * @param subject number to transform
     * @param step
     * @param start beginning to make convergence from, must be lower or equal
     * to subject
     * @param end value the subject, nor its converged value, cannot exceed
     * @return closest greater or equal number to subject
     */
    public static int convergeTo(final int subject, final int step, final int start, final int end) {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive number");
        }
        if (subject < start) {
            throw new IllegalArgumentException("start is bigger than what");
        }
        if (start > end) {
            throw new IllegalArgumentException("start is bigger than end");
        }
        if (subject >= end) {
            return end;
        }
        int whatShifted = subject - start;
        int c = (int) whatShifted / step;
        int mod = whatShifted % step;
        if (mod > 0) {
            mod = 1;
        }
        int r = (c + mod) * step + start;
        if (r > end) {
            return end;
        }
        return r;
    }

}
