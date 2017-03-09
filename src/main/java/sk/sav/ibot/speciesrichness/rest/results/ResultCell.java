package sk.sav.ibot.speciesrichness.rest.results;

import io.swagger.annotations.ApiModel;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * POJO class representing single cell of the result set.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ApiModel(value = "Cell")
public class ResultCell {

    private final ResultCellBounds bounds;
    private final int year;
    private final int numOccurrences;
    private final int taxonOccurrences;
    private final int numSpecies;
    private final SortedSet<ResultSpecies> species;

    public ResultCell(final ResultCellBounds bounds, int year, int numOccurrences, int taxonOccurrences, int numSpecies, final Set<ResultSpecies> species) {
        this.bounds = bounds;
        this.year = year;
        this.numOccurrences = numOccurrences;
        this.taxonOccurrences = taxonOccurrences;
        this.numSpecies = numSpecies;
        this.species = new TreeSet<>(species);
    }

    public ResultCellBounds getBounds() {
        return bounds;
    }

    public int getNumOccurrences() {
        return this.numOccurrences;
    }

    public int getTaxonOccurrences() {
        return taxonOccurrences;
    }

    /**
     * Ratio of species occurrences to the total number of occurrences of higher
     * taxon.
     *
     * @return value between 0.0 and 1.0
     */
    public double getTaxonRatio() {
        if (this.numOccurrences == 0) {
            return 0.0;
        }
        double result = (double) this.taxonOccurrences / this.numOccurrences;
        return result;
    }

    public int getNumSpecies() {
        return this.numSpecies;
    }

    public int getYear() {
        return this.year;
    }

    @XmlElementWrapper(name = "species")
    @XmlElement(name = "value")
    public SortedSet<ResultSpecies> getSpecies() {
        return Collections.unmodifiableSortedSet(species);
    }

    @Override
    public String toString() {
        return "ResultCell{" + "bounds=" + bounds + ", year=" + year + ", numOccurrences=" + numOccurrences + ", taxonOccurrences=" + taxonOccurrences + ", numSpecies=" + numSpecies + ", species=" + species.size() + ", taxonRatio=" + getTaxonRatio() + '}';
    }

}
