package sk.sav.ibot.speciesrichness.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sk.sav.ibot.speciesrichness.model.Coredata;

/**
 * Grid is a spatial container defined by its bottom-left corner and top-right
 * corner, containing cells in space and time.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public class Grid {

    private LatLon bottomLeft;
    private LatLon topRight;
    //private Map<Cell, Integer> occurences = new HashMap<>(1);
    //private List<Cell> occurencess = new ArrayList<>();
    //private Map<String, List<Cell>> layers = new HashMap<>();
    private List<Cell> cells = new ArrayList<>();

    public Grid(LatLon bottomLeft, LatLon topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    public LatLon getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(LatLon bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public LatLon getTopRight() {
        return topRight;
    }

    public void setTopRight(LatLon topRight) {
        this.topRight = topRight;
    }

    /**
     * Layers of cells. Filled by occurencesInGrid().
     *
     * @return Layers of cells
     */
//    public Map<String, List<Cell>> getLayers() {
//        return layers;
//    }
//
//    public void setLayers(Map<String, List<Cell>> layers) {
//        this.layers = layers;
//    }
    /**
     * Getter for the cells of the grid
     *
     * @return
     */
    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    /**
     * Creates list of all cells in this grid. Each cell contains total number
     * of occurences and species for all the data occuring within an area
     * covered by specific cell in specific year. Retrieve results by calling
     * getCells();
     *
     * @param spatialResolution Width and height of cells contained in the grid
     * @param temporalResolution Which years include to the result, e.g. if
     * years = [2001, 2002, 2003, 2004, 2005, 2006] and temporalResolution = 3
     * then result = [2001, 2004]
     * @param startYear Starting year
     * @param data Data the cells are created for
     */
    public void occurencesInGrid(int spatialResolution, int temporalResolution, int startYear, List<Coredata> data) {
        if (spatialResolution <= 0) {
            throw new IllegalArgumentException("spatial resolution must be a positive number");
        }
        if (temporalResolution <= 0) {
            throw new IllegalArgumentException("temporal resolution must be a positive number");
        }
        if (startYear <= 0) {
            throw new IllegalArgumentException("start year must be a positive number");
        }
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }
        for (Coredata d : data) {
            //check for outside the bounding box
            if (d.getLatitude() < this.bottomLeft.getLatitude()
                    || d.getLatitude() > this.topRight.getLatitude()
                    || d.getLongitude() < this.bottomLeft.getLongitude()
                    || d.getLongitude() > this.topRight.getLongitude()) {
                continue;
            }
            //do the work only for chosen years
            //if ((d.getCyear() - startYear) % temporalResolution == 0) {
            LatLon point = new LatLon(d.getLatitude(), d.getLongitude());
            Cell c = occurenceInGrid(spatialResolution, point, d.getCyear()); //create cell for given point and year
            if (this.cells.contains(c)) { //if such cell exists, update its occurences and species
                Cell cellIn = this.cells.get(this.cells.indexOf(c));
                cellIn.addNumOccurences(d.getNumRecords());
                cellIn.addSpecies(d.getTaxonkey());
            } else {
                c.addNumOccurences(d.getNumRecords());
                c.addSpecies(d.getTaxonkey());
                this.cells.add(c);
            }
            
            //make cells into layers by years - not in use
//                String layerId = d.getCyear().toString(); //year as key in map
//                List<Cell> cells;
//                if (this.layers.containsKey(layerId)) { //year is in the map
//                    cells = this.layers.get(layerId); //list of cells under specific year
//                    int oi = cells.indexOf(c); //find a cell in the list
//                    if (oi < 0) { //cell is not in the list
//                        c.addNumOccurences(d.getNumRecords());
//                        c.addSpecies(d.getTaxonkey());
//                        allCells.add(c);
//                        cells.add(c);
//                    } else { //cell is in the list
//                        //int count = cells.get(oi).getNumOccurences() + d.getNumRecords();
//                        cells.get(oi).addNumOccurences(d.getNumRecords());
//                        cells.get(oi).addSpecies(d.getTaxonkey());
//                    }
//                } else { //year is not in the map
//                    cells = new ArrayList<>(); //create empty list of cells
//                    c.addNumOccurences(d.getNumRecords());
//                    c.addSpecies(d.getTaxonkey());
//                    cells.add(c); //add first cell in the newly created list
//                    allCells.add(c);
//                }
//                this.layers.put(layerId, cells); //update map
            //}
        }
    }

    /**
     * Creates a new cell based on a point, and width and height of a cell in
     * degrees. The point belongs to the area covered by the cell. Borders of a
     * cell are positioned in a way that all previous cells, starting from the
     * bottom-left corner of the grid have the same size specified by
     * resolution.
     *
     * @param width width of the cell in degrees
     * @param point Spatial point within a cell
     * @param year A year the cell is created for
     * @return Cell with given width, containing the point, and existing in a
     * year
     */
    public Cell occurenceInGrid(int width, LatLon point, int year) {
        if (width <= 0) {
            throw new IllegalArgumentException("cell width must be positive number");
        }
        if (year <= 0) {
            throw new IllegalArgumentException("year must be a positive number");
        }
        if (point == null) {
            throw new IllegalArgumentException("point is null");
        }
        //corners of whole grid
        double btmLftLat = this.getBottomLeft().getLatitude();
        double btmLftLon = this.getBottomLeft().getLongitude();
        double topRgtLat = this.getTopRight().getLatitude();
        double topRgtLon = this.getTopRight().getLongitude();

        double latDiff = point.getLatitude() - btmLftLat;
        int timesRLat = (int) latDiff / width; //how many cells of width "resolution" fit before the point
        double lonDiff = point.getLongitude() - btmLftLon;
        int timesRLon = (int) lonDiff / width;
        //calculate cell borders
        double cellBtmLftLat = btmLftLat + timesRLat * width;
        double cellBtmLftLon = btmLftLon + timesRLon * width;
        double cellTopRgtLat = Math.min(btmLftLat + (timesRLat + 1) * width, topRgtLat);
        double cellTopRgtLon = Math.min(btmLftLon + (timesRLon + 1) * width, topRgtLon);
        Cell c = new Cell(new LatLon(cellBtmLftLat, cellBtmLftLon), new LatLon(cellTopRgtLat, cellTopRgtLon), year);
        return c;
    }

}
