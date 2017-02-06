package sk.sav.ibot.speciesrichness.rest.results;

import io.swagger.annotations.ApiModel;

/**
 * POJO class encapsulating search conditions. Is included in output.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ApiModel(value = "Query")
public class SearchTerms {

    private int spatialResolution;
    private int yearFrom;
    private int yearTo;
    private int temporalResolution;
    private String higherTaxonName;
    private String higherTaxonRank;
    private int higherTaxonGbifKey;

    private String speciesTaxonName; //species
    private int speciesTaxonGbifKey;

    private double boundsNorth;
    private double boundsEast;
    private double boundsSouth;
    private double boundsWest;

    public SearchTerms() {
    }

    public SearchTerms(int spatialResolution, int yearFrom, int yearTo, int temporalResolution,
            String supertaxonName, String supertaxonRank, int supertaxonGbifKey,
            String taxonName, int taxonGbifKey,
            double north, double east, double south, double west) {
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

    public void setHigherTaxonRank(String higherTaxonRank) {
        this.higherTaxonRank = higherTaxonRank;
    }

    public int getHigherTaxonGbifKey() {
        return this.higherTaxonGbifKey;
    }

    public String getSpeciesTaxonName() {
        return speciesTaxonName;
    }

    public void setSpeciesTaxonName(String speciesTaxonName) {
        this.speciesTaxonName = speciesTaxonName;
    }

    public int getSpeciesTaxonGbifKey() {
        return speciesTaxonGbifKey;
    }

    public void setSpeciesTaxonGbifKey(int speciesTaxonGbifKey) {
        this.speciesTaxonGbifKey = speciesTaxonGbifKey;
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

    public void setSpatialResolution(int spatialResolution) {
        this.spatialResolution = spatialResolution;
    }

    public void setYearFrom(int yearFrom) {
        this.yearFrom = yearFrom;
    }

    public void setYearTo(int yearTo) {
        this.yearTo = yearTo;
    }

    public void setTemporalResolution(int temporalResolution) {
        this.temporalResolution = temporalResolution;
    }

    public void setHigherTaxonName(String higherTaxonName) {
        this.higherTaxonName = higherTaxonName;
    }

    public void setHigherTaxonGbifKey(int higherTaxonGbifKey) {
        this.higherTaxonGbifKey = higherTaxonGbifKey;
    }

    public void setBoundsNorth(double boundsNorth) {
        this.boundsNorth = boundsNorth;
    }

    public void setBoundsEast(double boundsEast) {
        this.boundsEast = boundsEast;
    }

    public void setBoundsSouth(double boundsSouth) {
        this.boundsSouth = boundsSouth;
    }

    public void setBoundsWest(double boundsWest) {
        this.boundsWest = boundsWest;
    }

    @Override
    public String toString() {
        return "SearchTerms{" + "spatialResolution=" + spatialResolution + ", yearFrom=" + yearFrom + ", yearTo=" + yearTo + ", temporalResolution=" + temporalResolution + ", supertaxonName=" + higherTaxonName + ", supertaxonRank=" + higherTaxonRank + ", supertaxonGbifKey=" + higherTaxonGbifKey + ", taxonName=" + speciesTaxonName + ", taxonGbifKey=" + speciesTaxonGbifKey + ", boundsNorth=" + boundsNorth + ", boundsEast=" + boundsEast + ", boundsSouth=" + boundsSouth + ", boundsWest=" + boundsWest + '}';
    }

}
