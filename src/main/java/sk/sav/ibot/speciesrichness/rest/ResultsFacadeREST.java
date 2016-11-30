/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest;

import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sk.sav.ibot.speciesrichness.json.gbif.GbifTaxon;
import sk.sav.ibot.speciesrichness.rest.results.ResultItems;
import sk.sav.ibot.speciesrichness.rest.results.SearchTerms;
import sk.sav.ibot.speciesrichness.logic.CoredataController;
import sk.sav.ibot.speciesrichness.logic.TaxonomyController;
import sk.sav.ibot.speciesrichness.model.Taxonomy;
import sk.sav.ibot.speciesrichness.services.CoredataService;
import sk.sav.ibot.speciesrichness.services.TaxonomyService;

/**
 * Provider of REST services /api/occurences/
 * @author Matus
 */
@Component
@Scope("request")
@Path("/occurences/{taxon}")
public class ResultsFacadeREST {

    //spring service
    @Autowired
    private CoredataService coredataService;
    @Autowired
    private TaxonomyService taxonomyService;
    
    public ResultsFacadeREST() {
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResultItems getResults(
            @PathParam("taxon") String taxon, //taxon can be either gbifkey
        @DefaultValue("1785") @QueryParam("year-from") int yearFrom,
        @DefaultValue("2015") @QueryParam("year-to") int yearTo,
        @DefaultValue("5") @QueryParam("spatial-res") int spatialResolution,
        @DefaultValue("1") @QueryParam("temporal-res") int temporalResolution,
        @DefaultValue("72") @QueryParam("north") double north,
        @DefaultValue("40") @QueryParam("east") double east,
        @DefaultValue("29") @QueryParam("south") double south,
        @DefaultValue("-10") @QueryParam("west") double west
    ) {
        TaxonomyController tc = new TaxonomyController();
        String gbifKey;
        String higherTaxonName;
        String higherTaxonRank;
        if (taxon.matches("[0-9]+")) { //taxon is directly gbifkey
            gbifKey = taxon;
            //get name from database
            Taxonomy higherTaxon = taxonomyService.getTaxonByGbifkey(Long.parseLong(taxon));
            if (higherTaxon != null) {
                higherTaxonName = higherTaxon.getName();
                higherTaxonRank = higherTaxon.getRank();
            } else {
                throw new NullPointerException("Gbif key " + taxon + " was not found in database");
            }
        } else if (taxon.matches("[A-Za-z]")) { //taxon is scientific name
            GbifTaxon gbifTaxon = tc.retrieveHigherTaxon(taxon); //match taxon in GBIF
            if (!gbifTaxon.getMatchType().equals("NONE")) { //there was a hit
                gbifKey = gbifTaxon.getUsageKey().toString();
                higherTaxonName = gbifTaxon.getScientificName();
                higherTaxonRank = gbifTaxon.getRank();
            } else {
                throw new NullPointerException("No taxon found for value " + taxon);
            }
        } else {
            throw new IllegalArgumentException("Invalid argument provided: " + taxon);
        }
        List<GbifTaxon> species = tc.retrieveSpecies(gbifKey);
        
        CoredataController cc = new CoredataController(coredataService);
        SearchTerms search = new SearchTerms(spatialResolution, yearFrom, 
                yearTo, temporalResolution, higherTaxonName, higherTaxonRank, gbifKey, 
                north, east, south, west);
        ResultItems ri = cc.retrieveResults(search, species);
        return ri;
    }
    
}
