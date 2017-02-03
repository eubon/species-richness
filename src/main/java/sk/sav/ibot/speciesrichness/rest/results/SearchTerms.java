/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.results;

import io.swagger.annotations.ApiModel;

/**
 * POJO class encapsulating search conditions. Is included in output.
 *
 * @author Matus
 */
@ApiModel(value = "Query")
public class SearchTerms {

    private int spatialResolution;
    private int yearFrom;
    private int yearTo;
    private int temporalResolution;
    private String supertaxonName;
    private String supertaxonRank;
    private int supertaxonGbifKey;

    private String taxonName; //species
    private int taxonGbifKey;

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
        this.supertaxonName = supertaxonName;
        this.supertaxonRank = supertaxonRank;
        this.supertaxonGbifKey = supertaxonGbifKey;
        this.taxonName = taxonName;
        this.taxonGbifKey = taxonGbifKey;
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

    public String getSupertaxonName() {
        return this.supertaxonName;
    }

    public String getSupertaxonRank() {
        return supertaxonRank;
    }

    public void setSupertaxonRank(String supertaxonRank) {
        this.supertaxonRank = supertaxonRank;
    }

    public int getSupertaxonGbifKey() {
        return this.supertaxonGbifKey;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public void setTaxonName(String taxonName) {
        this.taxonName = taxonName;
    }

    public int getTaxonGbifKey() {
        return taxonGbifKey;
    }

    public void setTaxonGbifKey(int taxonGbifKey) {
        this.taxonGbifKey = taxonGbifKey;
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

    public void setSupertaxonName(String supertaxonName) {
        this.supertaxonName = supertaxonName;
    }

    public void setSupertaxonGbifKey(int supertaxonGbifKey) {
        this.supertaxonGbifKey = supertaxonGbifKey;
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
        return "SearchTerms{" + "spatialResolution=" + spatialResolution + ", yearFrom=" + yearFrom + ", yearTo=" + yearTo + ", temporalResolution=" + temporalResolution + ", supertaxonName=" + supertaxonName + ", supertaxonRank=" + supertaxonRank + ", supertaxonGbifKey=" + supertaxonGbifKey + ", taxonName=" + taxonName + ", taxonGbifKey=" + taxonGbifKey + ", boundsNorth=" + boundsNorth + ", boundsEast=" + boundsEast + ", boundsSouth=" + boundsSouth + ", boundsWest=" + boundsWest + '}';
    }

}
