/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Set;
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
import sk.sav.ibot.speciesrichness.exception.TaxonNameException;
import sk.sav.ibot.speciesrichness.rest.results.ResultItems;
import sk.sav.ibot.speciesrichness.rest.results.SearchTerms;
import sk.sav.ibot.speciesrichness.logic.CoredataController;
import sk.sav.ibot.speciesrichness.logic.NameUsage;
import sk.sav.ibot.speciesrichness.logic.TaxonomyController;
import sk.sav.ibot.speciesrichness.rest.error.ErrorMessage;


/**
 * Provider of REST services /api/occurences/
 *
 * @author Matus
 */
@Component
@Scope("request")
@Api(value = "/occurences/{supertaxon}")
@Path("/occurences/{supertaxon}")
public class ResultsFacadeREST {

    //spring service to gbif api
    @Autowired
    private TaxonomyController taxonomyController;
    @Autowired
    private CoredataController coredataController;

    public TaxonomyController getTaxonomyController() {
        return taxonomyController;
    }

    public void setTaxonomyController(TaxonomyController taxonomyController) {
        this.taxonomyController = taxonomyController;
    }

    public CoredataController getCoredataController() {
        return coredataController;
    }

    public void setCoredataController(CoredataController coredataController) {
        this.coredataController = coredataController;
    }

    public ResultsFacadeREST() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(value = "Get occurences of the higher taxon", httpMethod = "GET", response = ResultItems.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK. Returns layers of cells with occurences of the higher taxon", response = ResultItems.class),
        @ApiResponse(code = 500, message = "Error occured", response = ErrorMessage.class)
    })
    public ResultItems getResults(
            @ApiParam(value = "higher taxon to get occurences for. Can be gbif key or scientific name", required = true) @PathParam("supertaxon") String supertaxon, //supertaxon can be either gbifkey or scientific name
            @ApiParam(value = "Taxon on species level to get percentage of occurences in the higher taxon. Can be gbif key or scientific name", required = false) @DefaultValue("0") @QueryParam("species") String taxon, //taxon can be gbifkey or scientific name
            @ApiParam(value = "Starting year", required = false) @DefaultValue("1758") @QueryParam("year-from") int yearFrom,
            @ApiParam(value = "Ending year", required = false) @DefaultValue("2015") @QueryParam("year-to") int yearTo,
            @ApiParam(value = "Size of the grid cell in degrees", required = false) @DefaultValue("5") @QueryParam("spatial-res") int spatialResolution,
            @ApiParam(value = "Group this many years (each n-th year)", required = false) @DefaultValue("1") @QueryParam("temporal-res") int temporalResolution,
            @ApiParam(value = "North bound of the grid in degrees", required = false) @DefaultValue("72") @QueryParam("north") double north,
            @ApiParam(value = "East bound of the grid in degrees", required = false) @DefaultValue("40") @QueryParam("east") double east,
            @ApiParam(value = "South bound of the grid in degrees", required = false) @DefaultValue("29") @QueryParam("south") double south,
            @ApiParam(value = "West bound of the grid in degrees", required = false) @DefaultValue("-10") @QueryParam("west") double west
    ) {
        
        try {
            //TaxonomyController tc = new SpeciesGbifClient();
            //supertaxon can be either gbif key or scientific name
            
            NameUsage higherTaxon = this.taxonomyController.parseName(supertaxon, false, true); //strict matching of the name
            int supertaxonGbifKey = higherTaxon.getKey();
            String supertaxonName = higherTaxon.getScientificName();
            String supertaxonRank = higherTaxon.getRank();
            
            //taxon can be either gbif key, scientific name, or  default ("0") when not checking for its occurences
            NameUsage speciesTaxon = this.taxonomyController.parseName(taxon, true, true); //strict matching of the name
            int taxonGbifKey = speciesTaxon.getKey();
            String taxonName = speciesTaxon.getScientificName();
            
            Set<NameUsage> species = this.taxonomyController.speciesOfHigherTaxon(supertaxonGbifKey);
            
            //CoredataController cc = new CoredataController(coredataService);
            SearchTerms search = new SearchTerms(spatialResolution, yearFrom,
                    yearTo, temporalResolution, supertaxonName, supertaxonRank, supertaxonGbifKey,
                    taxonName, taxonGbifKey,
                    north, east, south, west);
            ResultItems ri = this.coredataController.retrieveResults(search, species);
            return ri;
        } catch (TaxonNameException ex) {
            //Logger.getLogger(ResultsFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException(ex.getMessage());
        }
        
    }

}
