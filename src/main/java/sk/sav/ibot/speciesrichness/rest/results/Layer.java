/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.results;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Matus
 */
public class Layer implements Comparable<Layer> {
    
    private int year;
    private List<ResultCell> cells;

    public Layer() {
    }

    public Layer(int year, List<ResultCell> cells) {
        this.year = year;
        this.cells = cells;
    }

    @XmlAttribute
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @XmlElement(name = "cell")
    public List<ResultCell> getCells() {
        return cells;
    }

    public void setCells(List<ResultCell> cells) {
        this.cells = cells;
    }

    @Override
    public int compareTo(Layer o) {
        return this.year - o.getYear();
    }
    
}
