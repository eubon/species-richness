package sk.sav.ibot.speciesrichness.geo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sk.sav.ibot.speciesrichness.model.Coredata;

/**
 * Grid is a spatial container defined by its bottom-left corner and top-right
 * corner, containing cells in space and time.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public class Grid {

    private final LatLon bottomLeft;
    private final LatLon topRight;
    private List<Cell> cells = new ArrayList<>();

    public Grid(final LatLon bottomLeft, final LatLon topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    /**
     * Coordinates of the bottom-left corner.
     * @return 
     */
    public LatLon getBottomLeft() {
        return bottomLeft;
    }

    /**
     * Coordinates of the top-right corner.
     * @return 
     */
    public LatLon getTopRight() {
        return topRight;
    }

    /**
     * Cells of the grid.
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
     * Wrapper for the occurencesInGrid(int spatialResolution, List data, int taxonkey) method,
     * where taxonkey is not needed. Zero is passed as taxonkey.
     * @param spatialResolution Width and height of cells contained in the grid
     * @param data Data the cells are created for
     */
    public void occurencesInGrid(int spatialResolution, final List<Coredata> data) {
        this.occurencesInGrid(spatialResolution, data, 0);
    }
    
    /**
     * Creates list of all cells in this grid. Each cell contains total number
     * of occurences and species for all the data occuring within an area
     * covered by specific cell in specific year. Retrieve results by calling
     * getCells();
     *
     * @param spatialResolution Width and height of cells contained in the grid
     * @param data Data the cells are created for
     * @param taxonkey idetifies specific taxon to count occurences for
     * @return set of taxonkeys found in the data
     */
    public Set<Integer> occurencesInGrid(int spatialResolution, final List<Coredata> data, int taxonkey) {
        if (spatialResolution <= 0) {
            throw new IllegalArgumentException("spatial resolution must be a positive number");
        }
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }
        Set<Integer> keys = new HashSet<>();
        for (final Coredata d : data) {
            //check for outside the bounding box
            if (d.getLatitude() < this.bottomLeft.getLatitude()
                    || d.getLatitude() > this.topRight.getLatitude()
                    || d.getLongitude() < this.bottomLeft.getLongitude()
                    || d.getLongitude() > this.topRight.getLongitude()) {
                continue;
            }
            //add taxonkey to the set
            keys.add(d.getTaxonkey());
            
            LatLon point = new LatLon(d.getLatitude(), d.getLongitude());
            Cell c = occurenceInGrid(spatialResolution, point, d.getCyear()); //create cell for given point and year
            if (this.cells.contains(c)) { //if such cell exists, update its occurences and species
                Cell cellIn = this.cells.get(this.cells.indexOf(c));
                cellIn.addNumOccurences(d.getNumRecords());
                cellIn.addSpecies(d.getTaxonkey());
                if (d.getTaxonkey() == taxonkey) { //if current row is desired taxonkey, add row occurences to taxon occurences
                    cellIn.addTaxonOccurences(d.getNumRecords());
                }
            } else {
                c.addNumOccurences(d.getNumRecords());
                c.addSpecies(d.getTaxonkey());
                if (d.getTaxonkey() == taxonkey) {
                    c.addTaxonOccurences(d.getNumRecords());
                }
                this.cells.add(c);
            }
        }
        return keys;
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
    public Cell occurenceInGrid(int width, final LatLon point, int year) {
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

    @Override
    public String toString() {
        return "Grid{" + "bottomLeft=" + bottomLeft + ", topRight=" + topRight + ", cells=" + cells.size() + '}';
    }

}
