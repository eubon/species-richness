/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.jsf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import sk.sav.ibot.speciesrichness.json.gbif.GbifTaxon;
import sk.sav.ibot.speciesrichness.rest.results.ResultItems;
import sk.sav.ibot.speciesrichness.rest.results.SearchTerms;
import sk.sav.ibot.speciesrichness.logic.CoredataController;
import sk.sav.ibot.speciesrichness.logic.TaxonomyController;
import sk.sav.ibot.speciesrichness.services.CoredataService;
import sk.sav.ibot.speciesrichness.values.Defaults;

/**
 * JSF bean, recieves input values and returns results obtained by logic.
 * @author Matus
 */
@ManagedBean(name = "data")
@SessionScoped
public class DataBean implements Serializable {

    @ManagedProperty(value = "#{coredataService}")
    private transient CoredataService coredataService;

    @ManagedProperty(value = "#{taxonomyBean}")
    private TaxonomyBean taxonomyBean;
    
    private String higherTaxonName;
    private String higherTaxonHierarchy;
    private double bbNorth = Defaults.NORTH;
    private double bbEast = Defaults.EAST;
    private double bbSouth = Defaults.SOUTH;
    private double bbWest = Defaults.WEST;
    private int spatial = Defaults.SPATIAL_RESOLUTION;
    private int yearFrom = Defaults.YEAR_FROM;
    private int yearTo = Defaults.YEAR_TO;
    private int temporalRes = Defaults.TEMPORAL_RESOLUTION;
    
    private String occurencesJson;

    public CoredataService getCoredataService() {
        return coredataService;
    }

    public void setCoredataService(CoredataService coredataService) {
        this.coredataService = coredataService;
    }

    public TaxonomyBean getTaxonomyBean() {
        return taxonomyBean;
    }

    public void setTaxonomyBean(TaxonomyBean taxonomyBean) {
        this.taxonomyBean = taxonomyBean;
    }

    public String getHigherTaxonName() {
        return higherTaxonName;
    }

    public void setHigherTaxonName(String higherTaxonName) {
        this.higherTaxonName = higherTaxonName;
    }

    public String getHigherTaxonHierarchy() {
        return higherTaxonHierarchy;
    }

    public void setHigherTaxonHierarchy(String higherTaxonHierarchy) {
        this.higherTaxonHierarchy = higherTaxonHierarchy;
    }

    public double getBbWest() {
        return bbWest;
    }

    public void setBbWest(double bbWest) {
        this.bbWest = bbWest;
    }

    public double getBbNorth() {
        return bbNorth;
    }

    public void setBbNorth(double bbNorth) {
        this.bbNorth = bbNorth;
    }

    public double getBbEast() {
        return bbEast;
    }

    public void setBbEast(double bbEast) {
        this.bbEast = bbEast;
    }

    public double getBbSouth() {
        return bbSouth;
    }

    public void setBbSouth(double bbSouth) {
        this.bbSouth = bbSouth;
    }

    public int getSpatial() {
        return spatial;
    }

    public void setSpatial(int spatial) {
        this.spatial = spatial;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(int yearFrom) {
        this.yearFrom = yearFrom;
    }

    public int getYearTo() {
        return yearTo;
    }

    public void setYearTo(int yearTo) {
        this.yearTo = yearTo;
    }

    public int getTemporalRes() {
        return temporalRes;
    }

    public void setTemporalRes(int temporalRes) {
        this.temporalRes = temporalRes;
    }

    /**
     * Serialized property, contains results in JSON form.
     * @return JSON string
     */
    public String getOccurencesJson() {
        return occurencesJson;
    }
    
    /**
     * Retrieves results by given search terms and serializes them into JSON object.
     * @return webpage with results - "cells"
     */
    public String retrieveCells() {
        //restrieve species by the higher taxon
        TaxonomyController tc = new TaxonomyController();
        if (taxonomyBean.getGbifkey() == null) { //not autocomplete value
            throw new NullPointerException("No result found for taxon " + taxonomyBean.getHigherTaxonName());
        }
        List<GbifTaxon> species = tc.retrieveSpecies(taxonomyBean.getGbifkey());
        //set values for serialization
        this.higherTaxonName = taxonomyBean.getHigherTaxonName();
        this.higherTaxonHierarchy = taxonomyBean.getHigherTaxonHierarchy();
        
        CoredataController cc = new CoredataController(coredataService);
        SearchTerms terms = new SearchTerms(this.spatial, this.yearFrom, 
                this.yearTo, this.temporalRes, this.higherTaxonName, 
                taxonomyBean.getHigherTaxonRank(), this.taxonomyBean.getGbifkey(), 
                this.bbNorth, this.bbEast, this.bbSouth, this.bbWest);
        
        ResultItems results = cc.retrieveResults(terms, species);       
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.occurencesJson = mapper.writeValueAsString(results);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DataBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "cells";
    }

}
