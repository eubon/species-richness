/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.results;

import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO class representing single cell of the result set.
 *
 * @author Matus
 */
@XmlRootElement(name = "cell")
public class ResultCell {

    private ResultCellBounds bounds;
    private int year;
    private int numOccurences;
    private int numSpecies;
    private Set<Integer> species;

    public ResultCell() {
    }

    public ResultCell(ResultCellBounds bounds, int year, int numOccurences, int numSpecies, Set<Integer> species) {
        this.bounds = bounds;
        this.year = year;
        this.numOccurences = numOccurences;
        this.numSpecies = numSpecies;
        this.species = species;
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
    public Set<Integer> getSpecies() {
        return this.species;
    }

    public void setSpecies(Set<Integer> species) {
        this.species = species;
    }

}
