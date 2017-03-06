package sk.sav.ibot.speciesrichness.rest.results;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * POJO class representing single layer of cells with certain year.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public class Layer implements Comparable<Layer> {

    private final int year;
    private final List<ResultCell> cells;

    public Layer(final int year, final List<ResultCell> cells) {
        this.year = year;
        this.cells = cells;
    }

    @XmlAttribute
    public int getYear() {
        return year;
    }

    @XmlElement(name = "cell")
    public List<ResultCell> getCells() {
        return Collections.unmodifiableList(cells);
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
