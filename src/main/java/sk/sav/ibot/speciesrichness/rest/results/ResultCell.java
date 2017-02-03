/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.results;

import io.swagger.annotations.ApiModel;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * POJO class representing single cell of the result set.
 *
 * @author Matus
 */
@ApiModel(value = "Cell")
//@XmlRootElement(name = "cell")
public class ResultCell {

    private ResultCellBounds bounds;
    private int year;
    private int numOccurences;
    private int taxonOccurences;
    private int numSpecies;
    private SortedSet<ResultSpecies> species = new TreeSet<>();

    public ResultCell() {
    }

    public ResultCell(ResultCellBounds bounds, int year, int numOccurences, int taxonOccurences, int numSpecies,  Set<ResultSpecies> species) {
        this.bounds = bounds;
        this.year = year;
        this.numOccurences = numOccurences;
        this.taxonOccurences = taxonOccurences;
        this.numSpecies = numSpecies;
        this.species = new TreeSet<>(species);
    }

    public ResultCellBounds getBounds() {
        return bounds;
    }

    public void setBounds(ResultCellBounds bounds) {
        this.bounds = bounds;
    }

    public int getNumOccurences() {
        return this.numOccurences;
    }

    public void setNumOccurences(int numOccurences) {
        this.numOccurences = numOccurences;
    }

    public int getTaxonOccurences() {
        return taxonOccurences;
    }

    public void setTaxonOccurences(int taxonOccurences) {
        this.taxonOccurences = taxonOccurences;
    }

    /**
     * Ratio of species occurences to the total number of occurences of higher taxon.
     * @return value between 0.0 and 1.0
     */
    public double getTaxonRatio() {
        if (this.numOccurences == 0) {
            return 0.0;
        }
        double result = (double) this.taxonOccurences / this.numOccurences;
        return result;
    }
    
    public int getNumSpecies() {
        return this.numSpecies;
    }

    public void setNumSpecies(int numSpecies) {
        this.numSpecies = numSpecies;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @XmlElementWrapper(name = "species")
    @XmlElement(name = "value")
    public SortedSet<ResultSpecies> getSpecies() {
        return this.species;
    }

    public void setSpecies(SortedSet<ResultSpecies> species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return "ResultCell{" + "bounds=" + bounds + ", year=" + year + ", numOccurences=" + numOccurences + ", taxonOccurences=" + taxonOccurences + ", numSpecies=" + numSpecies + ", species=" + species.size() + ", taxonRatio=" + getTaxonRatio() + '}';
    }

}
