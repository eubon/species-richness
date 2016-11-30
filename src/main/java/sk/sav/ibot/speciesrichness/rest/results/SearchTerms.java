/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.results;

/**
 * POJO class encapsulating search conditions. Is included in output.
 * @author Matus
 */
public class SearchTerms {

    private int spatialResolution;
    private int yearFrom;
    private int yearTo;
    private int temporalResolution;
    private String taxonName;
    private String taxonRank;
    private String gbifKey;

    private double boundsNorth;
    private double boundsEast;
    private double boundsSouth;
    private double boundsWest;

    public SearchTerms() {
    }

    public SearchTerms(int spatialResolution, int yearFrom, int yearTo, int temporalResolution, String taxonName, String taxonRank, String gbifKey, 
            double north, double east, double south, double west) {
        this.spatialResolution = spatialResolution;
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
        this.temporalResolution = temporalResolution;
        this.taxonName = taxonName;
        this.taxonRank = taxonRank;
        this.gbifKey = gbifKey;
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

    public String getTaxonName() {
        return this.taxonName;
    }

    public String getTaxonRank() {
        return taxonRank;
    }

    public void setTaxonRank(String taxonRank) {
        this.taxonRank = taxonRank;
    }

    public String getGbifKey() {
        return this.gbifKey;
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

    public void setTaxonName(String taxonName) {
        this.taxonName = taxonName;
    }

    public void setGbifKey(String gbifKey) {
        this.gbifKey = gbifKey;
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

}
