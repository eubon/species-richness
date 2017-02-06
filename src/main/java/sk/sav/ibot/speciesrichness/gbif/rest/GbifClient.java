
package sk.sav.ibot.speciesrichness.gbif.rest;

import java.util.List;
import java.util.Set;
import sk.sav.ibot.speciesrichness.gbif.json.GbifTaxon;

/**
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
public interface GbifClient {
    
    public Set<GbifTaxon> retrieveSpeciesOfHigherTaxon(int higherTaxonGbifKey);
    public GbifTaxon retrieveTaxonByName(String name, boolean isSpecies, boolean strict);
    public GbifTaxon retrieveTaxonByKey(String key);
    public GbifTaxon retrieveTaxon(String value, boolean isSpecies, boolean strict);
    public List<GbifTaxon> suggestSpecies(String term);
    
}
