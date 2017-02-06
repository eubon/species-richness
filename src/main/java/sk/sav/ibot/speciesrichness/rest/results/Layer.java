package sk.sav.ibot.speciesrichness.rest.results;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * POJO class representing single layer of cells with certain year.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
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

    @Override
    public String toString() {
        return "Layer{" + "year=" + year + ", cells=" + cells.size() + '}';
    }

}
