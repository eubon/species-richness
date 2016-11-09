/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.springframework.web.client.RestTemplate;
import sk.sav.ibot.speciesrichness.json.GbifResults;
import sk.sav.ibot.speciesrichness.json.GbifTaxon;
import sk.sav.ibot.speciesrichness.services.TaxonomyService;
import sk.sav.ibot.speciesrichness.model.Taxonomy;

/**
 * Handles autocomplete of the higher taxa and retrieval of species under selected higher taxon.
 * Higher taxa with their correspondig gbifKeys are stored in the database (entity Taxon).
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ManagedBean
@SessionScoped
public class TaxonomyBean implements Serializable {

    @ManagedProperty(value = "#{taxonomyService}")
    private transient TaxonomyService taxonomyService;

    //private final transient List<GbifTaxon> species = new ArrayList<>(); //stores species retrieved by the GBIF web service
    
    private String gbifkey; //gbifKey of the selected higher taxon
    private String higherTaxonName; //name of the selected higher taxon
    private String higherTaxonHierarchy; //hierarchy in which the higher taxon is located

    public void setTaxonomyService(TaxonomyService taxonomyFacade) {
        this.taxonomyService = taxonomyFacade;
    }

    public String getGbifkey() {
        return gbifkey;
    }

    public void setGbifkey(String gbifkey) {
        this.gbifkey = gbifkey;
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
    
    /*
    public List<GbifTaxon> getSpecies() {
        return species;
    }
    */
    /**
     * Populates autocomplete with Taxonomy results by term.
     * @param query Term to search taxons by
     * @return List of Taxonomy results beginning with query
     */
    public List<Taxonomy> complete(String query) {
        return this.taxonomyService.getHigherTaxonStartsWith(query);
    }

    /**
     * Handles autocomplete when user selects value.
     * @param e 
     */
    public void handleSelect(SelectEvent e) {
        Taxonomy p = (Taxonomy) e.getObject();
        this.gbifkey = p.getGbifkey().toString();
        this.higherTaxonName = p.getName() + " (" + p.getRank() + ")";
        this.higherTaxonHierarchy = p.getHigherHierarchy();
    }

    /**
     * The method queries gbif API to get species in the higher taxon identified by 
     * given gbifKey.
     * @return List of taxa in GBIF contained in the higher taxon
     */
    public List<GbifTaxon> retrieveSpecies() {
        //System.out.println("retrieving species from GBIF api");
        /*
        if (this.species != null && !this.species.isEmpty()) {
            this.species.clear();
        }*/
        List<GbifTaxon> species = new ArrayList<>();
        RestTemplate gbifRest = new RestTemplate(); //GBIF Rest service
        
        Map<String, String> parameters = new HashMap<>();
        parameters.put("highertaxonKey", this.gbifkey);
        parameters.put("rank", "SPECIES");
        parameters.put("status", "ACCEPTED");
        parameters.put("limit", "1000");
        int offset = 0;
        parameters.put("offset", String.valueOf(offset));
        
        boolean endofdata = false;
        while (!endofdata) {
            GbifResults results = gbifRest.getForObject("http://api.gbif.org/v1/species/search?highertaxonKey={highertaxonKey}&rank={rank}&status={status}&limit={limit}&offset={offset}",
                    GbifResults.class, parameters);
            species.addAll(results.getResults());
            offset += 1000;
            parameters.put("offset", String.valueOf(offset));
            endofdata = results.isEndOfRecords();
            if (offset > 15000) {
                break;
            }
        }
        return species;
    }

    /*
    public int getSpeciesSize() {
        return this.species.size();
    }
    */
}
