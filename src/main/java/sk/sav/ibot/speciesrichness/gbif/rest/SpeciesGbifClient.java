/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.gbif.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.sav.ibot.speciesrichness.gbif.json.GbifResults;
import sk.sav.ibot.speciesrichness.gbif.json.GbifTaxon;
import sk.sav.ibot.speciesrichness.values.Defaults;

/**
 * Provides methods for querying the GBIF species API
 *
 * @author Matus
 */
@Service(value = "speciesGbifClient")
@Scope(value = "singleton")
public class SpeciesGbifClient implements GbifClient {

    private final String gbifSpeciesUrl = Defaults.GBIF_BASE_URL + Defaults.GBIF_SPECIES_PATH;

    public String getGbifSpeciesUrl() {
        return gbifSpeciesUrl;
    }

    /**
     * The method queries gbif API to get species in the higher taxon identified
     * by given gbifKey.
     * http://api.gbif.org/v1/species/search?highertaxonKey={gbifKey}&rank=SPECIES&status=ACCEPTED
     *
     * @param higherTaxonGbifKey
     * @return List of taxa in GBIF contained in the higher taxon
     */
    @Override
    public Set<GbifTaxon> retrieveSpeciesOfHigherTaxon(int higherTaxonGbifKey) {
        Set<GbifTaxon> species = new HashSet<>();
        RestTemplate gbifRest = new RestTemplate(); //GBIF Rest service

        Map<String, String> parameters = new HashMap<>();
        parameters.put("highertaxonKey", String.valueOf(higherTaxonGbifKey));
        parameters.put("rank", GbifTaxon.SPECIES);
        parameters.put("status", GbifTaxon.ACCEPTED);
        parameters.put("limit", "1000");
        int offset = 0;
        parameters.put("offset", String.valueOf(offset));

        boolean endofdata = false;
        //repeatedly fetch 1000 results at a time until end of data is reached
        while (!endofdata) {
            GbifResults results = gbifRest.getForObject(this.gbifSpeciesUrl + "search?highertaxonKey={highertaxonKey}&rank={rank}&status={status}&limit={limit}&offset={offset}",
                    GbifResults.class, parameters);
            species.addAll(results.getResults());
            offset += 1000;
            parameters.put("offset", String.valueOf(offset));
            endofdata = results.isEndOfRecords();
            if (offset > 15000) {
                break;
            }
        }
        //System.out.println("Retrieved " + species.size() + " results for " + gbifKey);
        return species;
    }

    /**
     * The method queries GBIF api to resolve the provided name.
     * http://api.gbif.org/v1/species/match?name={name}&strict=true
     *
     * @param name scientific name to match against GBIF
     * @param strict
     * @param isSpecies
     * @return Object of GbifTaxon containing key, rank, higher hierarchy, taxon
     * name, or GbifTaxon with matchType = "NONE" if the GBIF service was not
     * able to find record
     */
    @Override
    public GbifTaxon retrieveTaxonByName(String name, boolean isSpecies, boolean strict) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        RestTemplate gbifRest = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("strict", String.valueOf(strict));
        String rank = "";
        if (isSpecies) {
            rank = "&rank={rank}";
            parameters.put("rank", GbifTaxon.SPECIES);
        }

        String query = this.gbifSpeciesUrl + "match?name={name}&strict={strict}" + rank;
        GbifTaxon result = gbifRest.getForObject(query,
                GbifTaxon.class, parameters);
        return result;
    }

    /**
     * Uses quick autocomplete function of GBIF api
     * http://api.gbif.org/v1/species/suggest?q={term}&rank=SPECIES
     *
     * @param term search term
     * @return List of gbif taxa containing term
     */
    @Override
    public List<GbifTaxon> suggestSpecies(String term) {
        if (term == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (term.isEmpty()) {
            return null;
        }
        RestTemplate gbifRest = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", term);
        parameters.put("rank", GbifTaxon.SPECIES);

        //FIXME fix data fetching - this url returns array of gbiftaxon
        GbifTaxon[] results = gbifRest.getForObject(this.gbifSpeciesUrl + "suggest?q={q}&rank={rank}",
                GbifTaxon[].class, parameters);
        return Arrays.asList(results);
    }

    /**
     * The method queries GBIF api to resolve the provided gbif key.
     * http://api.gbif.org/v1/species/{key}
     *
     * @param key GBIF key of the record
     * @return Object of GbifTaxon matching the key
     */
    @Override
    @Cacheable("gbifTaxon")
    public GbifTaxon retrieveTaxonByKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (key.isEmpty()) {
            throw new IllegalArgumentException("key is empty");
        }
        RestTemplate gbifRest = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", key);

        GbifTaxon result = null;
        try {
            result = gbifRest.getForObject(this.gbifSpeciesUrl + "{key}",
                GbifTaxon.class, parameters);
        } catch (RestClientException ex) {
            Logger.getLogger(SpeciesGbifClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Queries GBIF api to resolve the value to GbifTaxon.
     *
     * @param value value to be resolved, can be GBIF key or scientific name
     * @param isSpecies
     * @param strict if false, fuzzy matching is performed
     * @return reponse from the GBIF api as GbifTaxon object
     */
    @Override
    public GbifTaxon retrieveTaxon(String value, boolean isSpecies, boolean strict) {
        if (value == null) {
            throw new NullPointerException("Value is null");
        }
        if (value.matches("[1-9][0-9]*")) { //gbif key
            return retrieveTaxonByKey(value);
        }
        if (value.matches("[A-Za-z]+[A-Za-z \\.\\(\\)]*")) { //scientific name
            return retrieveTaxonByName(value, isSpecies, strict);
        }
        return null;
    }

}
