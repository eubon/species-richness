package sk.sav.ibot.speciesrichness.geo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Cell of a Grid is, just like Grid, defined by bottom-left corner and
 * top-right corner. The left and bottom borders belong to the cell, top and
 * right borders do not. The Cell has property year which adds time layer to the
 * grid. Holds values for occurences and species occuring in this cell.
 * Species is represented by unique taxonkey which is provided by GBIF.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public class Cell implements Comparable<Cell> {

    private final LatLon bottomLeft;
    private final LatLon topRight;
    private int year;
    private int numOccurrences;
    private Set<Integer> species = new HashSet<>(); //species in this cell
    private int taxonOccurrences; //given the specific taxon, hold occurences of it

    public Cell(final LatLon bottomLeft, final LatLon topRight, final int year) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.year = year;
    }

    public Cell(final LatLon bottomLeft, final LatLon topRight, int year, int numRecords, int taxonOccurences) {
        this(bottomLeft, topRight, year);
        this.numOccurrences = numRecords;
        this.taxonOccurrences = taxonOccurences;
    }

    /**
     * Coordinates of the bottom-left corner
     * @return 
     */
    public LatLon getBottomLeft() {
        return bottomLeft;
    }

    /**
     * Coordinates of the top-right corner
     * @return 
     */
    public LatLon getTopRight() {
        return topRight;
    }

    /**
     * Year the occurences in this cell belong to. A cell is associated with only one
     * year. If there are occurences belonging to the same area but to different year, 
     * another cell must be created.
     * @return 
     */
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Number of all occurrences in the area of this cell in the year associated with this cell.
     * @return 
     */
    public int getNumOccurrences() {
        return numOccurrences;
    }

    public void setNumOccurrences(int numOccurrences) {
        this.numOccurrences = numOccurrences;
    }

    /**
     * Increments number of all occurrences by value.
     * @param value can be positive, zero, or negative. The number of occurrences is
     * changed accordingly
     */
    public void addNumOccurences(int value) {
        //value can be negative
        this.numOccurrences += value;
    }

    /**
     * Occurrences of particular taxon in the area of this cell in the year associated with this cell.
     * @return 
     */
    public int getTaxonOccurrences() {
        return taxonOccurrences;
    }

    public void setTaxonOccurrences(int taxonOccurrences) {
        this.taxonOccurrences = taxonOccurrences;
    }
    
    /**
     * Increment number of taxon occurrences by value.
     * @param value 
     */
    public void addTaxonOccurences(int value) {
        //value can be negative
        this.taxonOccurrences += value;
    }
    
    /**
     * Number of species localized in the area of this cell.
     * @return 
     */
    public int getNumSpecies() {
        return this.species.size();
    }

    /**
     * Species localized in the area of this cell. Species in the set is represented by its
     * unique taxonkey.
     * @return Set of taxonkeys
     */
    public Set<Integer> getSpecies() {
        return species;
    }

    public void setSpecies(Set<Integer> taxonkey) {
        this.species = taxonkey;
    }

    /**
     * Adds taxonkey to the set of species.
     * @param taxonkey 
     */
    public void addSpecies(Integer taxonkey) {
        this.species.add(taxonkey);
    }
    
    /**
     * Adds a collection of taxonkeys to the set of species.
     * @param taxonkeys 
     */
    public void addSpecies(Collection<Integer> taxonkeys) {
        this.species.addAll(taxonkeys);
    }

    @Override
    public String toString() {
        return "Cell{" + "bottomLeft=" + bottomLeft + ", topRight=" + topRight + ", year=" + year + ", occurences=" + numOccurrences + ", species=" + species + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.bottomLeft);
        hash = 41 * hash + Objects.hashCode(this.topRight);
        hash = 41 * hash + this.year;
        return hash;
    }

    /**
     * Two cells are same when they have same bottom-left corners, same top-right corners, and same years.
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        if (!Objects.equals(this.bottomLeft, other.bottomLeft)) {
            return false;
        }
        if (!Objects.equals(this.topRight, other.topRight)) {
            return false;
        }
        return this.year == other.year;
    }

    /**
     * Two cells A, B are first compared by year, then by bottom-left corner, then by top-right corner.
     * 
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Cell o) {
        int yearCmp = this.year - o.getYear();
        if (yearCmp != 0) {
            return yearCmp;
        }
        int lonCmp = (int) (this.getBottomLeft().getLongitude() - o.getBottomLeft().getLongitude());
        return (int) (lonCmp != 0 ? lonCmp : (this.getBottomLeft().getLatitude() - o.getBottomLeft().getLatitude()));
    }

}
