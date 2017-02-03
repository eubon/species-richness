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
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import sk.sav.ibot.speciesrichness.rest.results.ResultItems;
import sk.sav.ibot.speciesrichness.rest.results.SearchTerms;
import sk.sav.ibot.speciesrichness.logic.CoredataController;
import sk.sav.ibot.speciesrichness.logic.NameUsage;
import sk.sav.ibot.speciesrichness.logic.TaxonomyController;
import sk.sav.ibot.speciesrichness.model.Taxonomy;
import sk.sav.ibot.speciesrichness.values.Defaults;

/**
 * JSF bean, recieves input values and returns results obtained by logic.
 *
 * @author Matus
 */
@ManagedBean(name = "data")
@SessionScoped
public class DataBean implements Serializable {

    //@ManagedProperty(value = "#{coredataService}")
    //private transient CoredataService coredataService;
    @ManagedProperty(value = "#{coredataController}")
    private transient CoredataController coredataController;

    @ManagedProperty(value = "#{taxonomyController}")
    private transient TaxonomyController taxonomyController;

    @ManagedProperty(value = "#{taxonomyBean}")
    private TaxonomyBean taxonomyBean;

    private String higherTaxonName;
    private List<Taxonomy> higherTaxonHierarchy;
    private int taxonKey;
    private String taxonName;
    private double bbNorth = Defaults.NORTH;
    private double bbEast = Defaults.EAST;
    private double bbSouth = Defaults.SOUTH;
    private double bbWest = Defaults.WEST;
    private int spatial = Defaults.SPATIAL_RESOLUTION;
    private int yearFrom = Defaults.YEAR_FROM;
    private int yearTo = Defaults.YEAR_TO;
    private int temporalRes = Defaults.TEMPORAL_RESOLUTION;

    private String taxonStatus = "glyphicon-info-sign"; //indicates if the species name is valid - spelling, being in supertaxon, 
    private String taxonMessage;

    private String occurencesJson;

    public CoredataController getCoredataController() {
        return coredataController;
    }

    public void setCoredataController(CoredataController coredataController) {
        this.coredataController = coredataController;
    }

    public TaxonomyController getTaxonomyController() {
        return taxonomyController;
    }

    public void setTaxonomyController(TaxonomyController taxonomyController) {
        this.taxonomyController = taxonomyController;
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

    public List<Taxonomy> getHigherTaxonHierarchy() {
        return higherTaxonHierarchy;
    }

    public void setHigherTaxonHierarchy(List<Taxonomy> higherTaxonHierarchy) {
        this.higherTaxonHierarchy = higherTaxonHierarchy;
    }

    public int getTaxonKey() {
        return taxonKey;
    }

    public void setTaxonKey(int taxonKey) {
        this.taxonKey = taxonKey;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public void setTaxonName(String taxonName) {
        this.taxonName = taxonName;
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
     *
     * @return JSON string
     */
    public String getOccurencesJson() {
        return occurencesJson;
    }

    public String getTaxonStatus() {
        return taxonStatus;
    }

    public void setTaxonStatus(String taxonStatus) {
        this.taxonStatus = taxonStatus;
    }

    public String getTaxonMessage() {
        return taxonMessage;
    }

    public void setTaxonMessage(String taxonMessage) {
        this.taxonMessage = taxonMessage;
    }

    public String reset() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        return "index?faces-redirect=true";
    }

    /**
     * Retrieves results by given search terms and serializes them into JSON
     * object.
     *
     * @return webpage with results - "cells"
     */
    public String retrieveCells() {
        //restrieve species by the higher taxon
        //TaxonomyController tc = new SpeciesGbifClient(this.taxonomyBean.getTaxonomyService());
        if (taxonomyBean.getSupertaxonGbifKey() == 0) { //not autocomplete value
            throw new NullPointerException("No result found for taxon " + taxonomyBean.getSupertaxonName());
        }
        //Set<GbifTaxon> species = SpeciesGbifClient.retrieveSpeciesOfHigherTaxon(taxonomyBean.getSupertaxonGbifKey());
        //TaxonomyController tc = new TaxonomyController(new SpeciesGbifClient());
        Set<NameUsage> species = this.taxonomyController.speciesOfHigherTaxon(taxonomyBean.getSupertaxonGbifKey());
        //set values for serialization
        this.higherTaxonName = taxonomyBean.getSupertaxonName();
        this.higherTaxonHierarchy = taxonomyBean.getSupertaxonHierarchy();
        this.taxonKey = taxonomyBean.getTaxonGbifKey();
        this.taxonName = taxonomyBean.getTaxonName();

        SearchTerms terms = new SearchTerms(this.spatial, this.yearFrom,
                this.yearTo, this.temporalRes, this.higherTaxonName,
                this.taxonomyBean.getSupertaxonRank(), this.taxonomyBean.getSupertaxonGbifKey(),
                this.taxonomyBean.getTaxonName(), this.taxonomyBean.getTaxonGbifKey(),
                this.bbNorth, this.bbEast, this.bbSouth, this.bbWest);

        ResultItems results = this.coredataController.retrieveResults(terms, species);
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.occurencesJson = mapper.writeValueAsString(results);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DataBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "cells";
    }

}
