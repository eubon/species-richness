/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.gbif.rest;

import java.util.List;
import java.util.Set;
import sk.sav.ibot.speciesrichness.exception.TaxonNameException;
import sk.sav.ibot.speciesrichness.gbif.json.GbifTaxon;

/**
 *
 * @author Matus
 */
public interface GbifClient {
    
    public Set<GbifTaxon> retrieveSpeciesOfHigherTaxon(int higherTaxonGbifKey);
    public GbifTaxon retrieveTaxonByName(String name, boolean isSpecies, boolean strict);
    public GbifTaxon retrieveTaxonByKey(String key);
    public GbifTaxon retrieveTaxon(String value, boolean isSpecies, boolean strict);
    public List<GbifTaxon> suggestSpecies(String term);
    
}
