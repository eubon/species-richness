package sk.sav.ibot.speciesrichness.rest.results;

import io.swagger.annotations.ApiModel;

/**
 * POJO class encapsulating search conditions. Is included in output.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ApiModel(value = "Query")
public class SearchTerms {

    private final int spatialResolution;
    private final int yearFrom;
    private final int yearTo;
    private final int temporalResolution;
    private final String higherTaxonName;
    private final String higherTaxonRank;
    private final int higherTaxonGbifKey;

    private final String speciesTaxonName; //species
    private final int speciesTaxonGbifKey;

    private final double boundsNorth;
    private final double boundsEast;
    private final double boundsSouth;
    private final double boundsWest;

    public SearchTerms(final int spatialResolution, final int yearFrom, final int yearTo, 
            final int temporalResolution, final String supertaxonName, final String supertaxonRank, 
            final int supertaxonGbifKey, final String taxonName, final int taxonGbifKey,
            final double north, final double east, final double south, final double west) {
        this.spatialResolution = spatialResolution;
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
        this.temporalResolution = temporalResolution;
        this.higherTaxonName = supertaxonName;
        this.higherTaxonRank = supertaxonRank;
        this.higherTaxonGbifKey = supertaxonGbifKey;
        this.speciesTaxonName = taxonName;
        this.speciesTaxonGbifKey = taxonGbifKey;
        this.boundsNorth = north;
        this.boundsEast = east;
        this.boundsSouth = south;
        this.boundsWest = west;
    }

    public int getSpatialResolution() {
        return this.spatialResolution;
    }

    public int getYearFrom() {
        return this.yearFrom;
    }

    public int getYearTo() {
        return this.yearTo;
    }

    public int getTemporalResolution() {
        return this.temporalResolution;
    }

    public String getHigherTaxonName() {
        return this.higherTaxonName;
    }

    public String getHigherTaxonRank() {
        return higherTaxonRank;
    }

    public int getHigherTaxonGbifKey() {
        return this.higherTaxonGbifKey;
    }

    public String getSpeciesTaxonName() {
        return speciesTaxonName;
    }

    public int getSpeciesTaxonGbifKey() {
        return speciesTaxonGbifKey;
    }

    public double getBoundsNorth() {
        return this.boundsNorth;
    }

    public double getBoundsEast() {
        return this.boundsEast;
    }

    public double getBoundsSouth() {
        return this.boundsSouth;
    }

    public double getBoundsWest() {
        return this.boundsWest;
    }

    @Override
    public String toString() {
        return "SearchTerms{" + "spatialResolution=" + spatialResolution + ", yearFrom=" + yearFrom + ", yearTo=" + yearTo + ", temporalResolution=" + temporalResolution + ", supertaxonName=" + higherTaxonName + ", supertaxonRank=" + higherTaxonRank + ", supertaxonGbifKey=" + higherTaxonGbifKey + ", taxonName=" + speciesTaxonName + ", taxonGbifKey=" + speciesTaxonGbifKey + ", boundsNorth=" + boundsNorth + ", boundsEast=" + boundsEast + ", boundsSouth=" + boundsSouth + ", boundsWest=" + boundsWest + '}';
    }

}
