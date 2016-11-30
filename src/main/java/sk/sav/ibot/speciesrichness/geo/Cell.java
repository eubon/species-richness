package sk.sav.ibot.speciesrichness.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private LatLon bottomLeft;
    private LatLon topRight;
    private int year;
    private int numOccurences;
    private Set<Integer> species = new HashSet<>(); //species in this cell
    //private int numSpecies;

    public Cell(LatLon bottomLeft, LatLon topRight, int year) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.year = year;
        this.numOccurences = 0;
    }

    public Cell(LatLon bottomLeft, LatLon topRight, int year, int numRecords/*, int numSpecies*/) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.year = year;
        this.numOccurences = numRecords;
        //this.numSpecies = numSpecies;
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

    @JsonIgnore
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @JsonIgnore
    public int getNumOccurences() {
        return numOccurences;
    }

    public void setNumOccurences(int numOccurences) {
        this.numOccurences = numOccurences;
    }

    public void addNumOccurences(int value) {
        this.numOccurences += value;
    }

    @JsonIgnore
    public int getNumSpecies() {
        return this.species.size();
    }

    /*
    public void setNumSpecies(int numSpecies) {
        this.numSpecies = numSpecies;
    }
     */
    /**
     * Getter for species in the cell. Species in the set is represented by its
     * unique taxonkey.
     * @return Set of taxonkeys
     */
    @JsonIgnore
    public Set<Integer> getSpecies() {
        return species;
    }

    /**
     * Setter for species in the cell. Species in the set is represented by its
     * unique taxonkey.
     * @param taxonkey Unique value representing a GBIF species
     */
    public void setSpecies(Set<Integer> taxonkey) {
        this.species = taxonkey;
    }

    public void addSpecies(Integer taxonkey) {
        this.species.add(taxonkey);
    }
    
    public void addSpecies(Collection<Integer> taxonkeys) {
        this.species.addAll(taxonkeys);
    }

    @Override
    public String toString() {
        return "Cell{" + "bottomLeft=" + bottomLeft + ", topRight=" + topRight + ", year=" + year + ", occurences=" + numOccurences + ", species=" + species + '}';
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
