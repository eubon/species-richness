package sk.sav.ibot.speciesrichness.jsf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import sk.sav.ibot.speciesrichness.rest.results.ResultItems;
import sk.sav.ibot.speciesrichness.rest.results.SearchTerms;
import sk.sav.ibot.speciesrichness.logic.CoredataController;
import sk.sav.ibot.speciesrichness.logic.NameUsage;
import sk.sav.ibot.speciesrichness.values.Defaults;

/**
 * JSF bean, recieves input values and returns results obtained by logic.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ManagedBean(name = "data")
@SessionScoped
public class DataBean implements Serializable {

    @ManagedProperty(value = "#{coredataController}")
    private transient CoredataController coredataController;

    @ManagedProperty(value = "#{taxonomyBean}")
    private TaxonomyBean taxonomyBean;

    private double bbNorth = Defaults.NORTH;
    private double bbEast = Defaults.EAST;
    private double bbSouth = Defaults.SOUTH;
    private double bbWest = Defaults.WEST;
    private int spatial = Defaults.SPATIAL_RESOLUTION;
    private int yearFrom = Defaults.YEAR_FROM;
    private int yearTo = Defaults.YEAR_TO;
    private int temporalRes = Defaults.TEMPORAL_RESOLUTION;

    private String occurencesJson;

    /**
     * Class containing the logic of processing occurences data
     *
     * @return
     */
    public CoredataController getCoredataController() {
        return coredataController;
    }

    public void setCoredataController(CoredataController coredataController) {
        this.coredataController = coredataController;
    }

    public TaxonomyBean getTaxonomyBean() {
        return taxonomyBean;
    }

    public void setTaxonomyBean(TaxonomyBean taxonomyBean) {
        this.taxonomyBean = taxonomyBean;
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
    
    public String isValid() {
        FacesContext context = FacesContext.getCurrentInstance();
        UIInput current = (UIInput) UIComponent.getCurrentComponent(context);
        
        return current.isValid() ? "" : "input-invalid";
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
        if (taxonomyBean.getHigherTaxonGbifKey() == 0) { //not autocomplete value
            throw new NullPointerException("No result found for taxon " + taxonomyBean.getHigherTaxonName());
        }

        Set<NameUsage> species = this.taxonomyBean.speciesOfHigherTaxon();

        SearchTerms terms = new SearchTerms(this.spatial, this.yearFrom,
                this.yearTo, this.temporalRes, taxonomyBean.getHigherTaxonName(),
                this.taxonomyBean.getHigherTaxonRank(), this.taxonomyBean.getHigherTaxonGbifKey(),
                this.taxonomyBean.getSpeciesTaxonName(), this.taxonomyBean.getSpeciesTaxonGbifKey(),
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
