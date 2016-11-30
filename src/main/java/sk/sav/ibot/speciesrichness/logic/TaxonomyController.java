/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestTemplate;
import sk.sav.ibot.speciesrichness.json.gbif.GbifResults;
import sk.sav.ibot.speciesrichness.json.gbif.GbifTaxon;

/**
 * Logic requiring more than fetching data from database.
 * @author Matus
 */
public class TaxonomyController {

    /**
     * The method queries gbif API to get species in the higher taxon identified
     * by given gbifKey.
     *
     * @param gbifKey
     * @return List of taxa in GBIF contained in the higher taxon
     */
    public List<GbifTaxon> retrieveSpecies(String gbifKey) {
        List<GbifTaxon> species = new ArrayList<>();
        RestTemplate gbifRest = new RestTemplate(); //GBIF Rest service

        Map<String, String> parameters = new HashMap<>();
        parameters.put("highertaxonKey", gbifKey);
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

    /**
     * The method queries GBIF api /species/match with specified name to identify
     * taxon with GBIF record.
     * @param name scientific name to match against GBIF
     * @return Object of GbifTaxon containing key, rank, higher hierarchy, taxon name,
     * or GbifTaxon with matchType = "NONE" if the GBIF service was not able to find record
     */
    public GbifTaxon retrieveHigherTaxon(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        RestTemplate gbifRest = new RestTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);

        GbifTaxon result = gbifRest.getForObject("http://api.gbif.org/v1/species/match?name={name}",
                GbifTaxon.class, parameters);
        return result;
    }

}
