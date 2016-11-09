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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.TransformerUtils;
import sk.sav.ibot.speciesrichness.geo.Cell;
import sk.sav.ibot.speciesrichness.geo.Grid;
import sk.sav.ibot.speciesrichness.geo.LatLon;
import sk.sav.ibot.speciesrichness.json.GbifTaxon;
import sk.sav.ibot.speciesrichness.model.Coredata;
import sk.sav.ibot.speciesrichness.services.CoredataService;

/**
 *
 * @author Matus
 */
@ManagedBean(name = "data")
@SessionScoped
public class DataBean implements Serializable {

    //@ManagedProperty(value = "#{taxonomyService}")
    //private TaxonomyService taxonomyService;
    @ManagedProperty(value = "#{coredataService}")
    private transient CoredataService coredataService;

    @ManagedProperty(value = "#{taxonomyBean}")
    private TaxonomyBean taxonomyBean;
    
    private String higherTaxonName;
    private String higherTaxonHierarchy;
    private double bbWest = -10.0;
    private double bbNorth = 72.0;
    private double bbEast = 40.0;
    private double bbSouth = 29.0;
    private int spatial = 5;
    private int temporalFrom = 1785;
    private int temporalTo = 2015;
    private int temporalRes = 1;
    
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

    public int getTemporalFrom() {
        return temporalFrom;
    }

    public void setTemporalFrom(int temporalFrom) {
        this.temporalFrom = temporalFrom;
    }

    public int getTemporalTo() {
        return temporalTo;
    }

    public void setTemporalTo(int temporalTo) {
        this.temporalTo = temporalTo;
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
     * Computes grid - layers of cells - from the user-defined input.
     * Retrieves all species from GBIF API by given higher taxon. Fetches ocurrences for
     * these species. Creates cells in a range of years in user-defined grid boundaries, 
     * and cell width.
     * @return webpage with results - "cells"
     */
    public String retrieveCells() {
        List<GbifTaxon> species = taxonomyBean.retrieveSpecies();
        this.higherTaxonName = taxonomyBean.getHigherTaxonName();
        this.higherTaxonHierarchy = taxonomyBean.getHigherTaxonHierarchy();
        
        //List<Integer> taxonkeys = this.coredataService.getAllTaxonkeys(this.temporalFrom, this.temporalTo); //testing only
        
        List<Integer> taxonkeys = (List) CollectionUtils.collect(species, TransformerUtils.invokerTransformer("getKey"));
        List<Coredata> data = this.coredataService.getCoredataByTaxonkeys(taxonkeys, this.temporalFrom, this.temporalTo);
        Grid grid = new Grid(new LatLon(this.bbSouth, this.bbWest), new LatLon(this.bbNorth, this.bbEast));
        grid.occurencesInGrid(this.spatial, this.temporalRes, this.temporalFrom, data);
        SortedMap<String, List<Cell>> occurences = new TreeMap<>(grid.getLayers()); //occurences moved from class attribute to local variable
        
        /*
        Map<String, List<Cell>> layers = new HashMap<>();
        List<Cell> c1s = new ArrayList<>();
        c1s.add(new Cell(new LatLon(29, -10), new LatLon(59, 20), 2002, 9050));
        c1s.add(new Cell(new LatLon(59, 20), new LatLon(72, 40), 2002, 29));
        c1s.add(new Cell(new LatLon(59, -10), new LatLon(72, 20), 2002, 122));
        layers.put("2002", c1s);
        List<Cell> c2s = new ArrayList<>();
        c2s.add(new Cell(new LatLon(29, -10), new LatLon(59, 20), 2003, 8599));
        c2s.add(new Cell(new LatLon(29, 20), new LatLon(59, 40), 2003, 4));
        c2s.add(new Cell(new LatLon(59, 20), new LatLon(72, 40), 2003, 29));
        layers.put("2003", c2s);
        List<Cell> c3s = new ArrayList<>();
        c3s.add(new Cell(new LatLon(29, -10), new LatLon(59, 20), 2004, 700));
        c3s.add(new Cell(new LatLon(29, 20), new LatLon(59, 40), 2004, 0));
        c3s.add(new Cell(new LatLon(59, 20), new LatLon(72, 40), 2004, 300));
        layers.put("2004", c3s);
        SortedMap<String, List<Cell>> occurences = new TreeMap<>(layers);
        */
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.occurencesJson = mapper.writeValueAsString(occurences);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DataBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "cells";
    }

}
