package sk.sav.ibot.speciesrichness.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import sk.sav.ibot.speciesrichness.exception.TaxonNameException;
import sk.sav.ibot.speciesrichness.gbif.json.GbifTaxon;
import sk.sav.ibot.speciesrichness.gbif.rest.GbifClient;
import sk.sav.ibot.speciesrichness.gbif.rest.SpeciesGbifClient;
import sk.sav.ibot.speciesrichness.values.Defaults;

/**
 * Contains logic for processing data returned by GBIF service
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@Service(value = "taxonomyController")
@Scope(value = "singleton")
public class TaxonomyController {

    @Autowired
    private GbifClient speciesGbifClient;

    public GbifClient getSpeciesGbifClient() {
        return speciesGbifClient;
    }

    public void setSpeciesGbifClient(SpeciesGbifClient speciesGbifClient) {
        this.speciesGbifClient = speciesGbifClient;
    }

    /**
     * By the name argument provided, this methid decides which method to call
     * to get the correct result from GBIF. GBIF taxon results differ in
     * structure and content for species and higher taxon (Genus and above).
     *
     * @param name argument to search in GBIF. Can be full scientific name.
     * Allowed characters are: A-Z, a-z, space, dot, parentheses, dash. Or can
     * be a gbif key. In that case it must be integer number.
     * @param isSpecies indicates what type of result we expect from GBIF
     * @param strict if true, strict search is performed, otherwise fuzzy
     * matching
     * @return NameUsage object found in GBIF
     * @throws TaxonNameException is thrown when: - name argument is neither
     * scientific name, nor gbif key - name argument looks legitimate but
     * nothing has been found by GBIF service - provided name argument results
     * in different type of taxon than we expected (e.g. found genus, but we
     * want species, and vice versa) - GBIF service return any other error
     */
    public NameUsage parseName(String name, boolean isSpecies, boolean strict) throws TaxonNameException {
        //default
        if (isSpecies && name.equals(Defaults.DEFAULT_SPECIES)) {
            return new NameUsageImpl(Integer.parseInt(Defaults.DEFAULT_SPECIES), "", "", "EXACT");
        }
        //accept gbif key or scientific name
        if (!name.matches("[1-9][0-9]*|[A-Za-z]+[A-Za-z \\.\\(\\)\\-]*")) {
            throw new TaxonNameException("Invalid argument provided: " + name, TaxonNameException.ExceptionSeverity.ERROR, Response.Status.NOT_ACCEPTABLE);
        }
        //check the name in gbif
        GbifTaxon gbifTaxon = this.speciesGbifClient.retrieveTaxon(name, isSpecies, strict);
        if (gbifTaxon == null) { //name was not gbif key neither scientific name
            throw new TaxonNameException("Invalid argument provided: " + name, TaxonNameException.ExceptionSeverity.ERROR, Response.Status.NOT_ACCEPTABLE);
        }
        //gbif could not resolve parameter properly
        if (gbifTaxon.getMatchType() != null && gbifTaxon.getMatchType().equals(GbifTaxon.NONE)) {
            if (gbifTaxon.getNote() != null) { //if gbif provides error response with meaningful note
                //throw new IllegalArgumentException(gbifTaxon.getNote());
                throw new TaxonNameException(gbifTaxon.getNote(), TaxonNameException.ExceptionSeverity.ERROR, Response.Status.NOT_FOUND);
            }
            //throw new IllegalArgumentException("No taxon found for " + name);
            throw new TaxonNameException("No taxon found for " + name, TaxonNameException.ExceptionSeverity.ERROR, Response.Status.NOT_FOUND);
        }
        if (isSpecies && !gbifTaxon.getRank().equals(GbifTaxon.SPECIES)) { //we wanted species but non-species provided as parameter
            //throw new IllegalArgumentException("Value " + name + " is not species, is in fact " + gbifTaxon.getRank().toLowerCase());
            throw new TaxonNameException("Value " + name + " is not species, is in fact " + gbifTaxon.getRank().toLowerCase(), TaxonNameException.ExceptionSeverity.WARNING, Response.Status.BAD_REQUEST);
        }
        if (!isSpecies && gbifTaxon.getRank().equals(GbifTaxon.SPECIES)) { //we wanted higher taxon but species provided as parameter
            //throw new IllegalArgumentException("Value " + name + " is not higher taxon, is in fact species");
            throw new TaxonNameException("Value " + name + " is not higher taxon, is in fact species", TaxonNameException.ExceptionSeverity.WARNING, Response.Status.BAD_REQUEST);
        }
        return new NameUsageImpl(gbifTaxon);
    }

    /**
     * Returns single species identified by given key
     *
     * @param key unique key of the species in GBIF
     * @return
     */
    public NameUsage speciesByKey(int key) {
        if (key <= 0) {
            throw new IllegalArgumentException("key must be greater than 0");
        }
        GbifTaxon gbifTaxon = this.speciesGbifClient.retrieveTaxonByKey(String.valueOf(key));
        if (gbifTaxon != null) {
            return new NameUsageImpl(gbifTaxon);
        }
        return null;
    }

    /**
     * Returns a set of species of the higher taxon.
     *
     * @param higherTaxonGbifKey gbif key of the higher taxon
     * @return
     */
    public Set<NameUsage> speciesOfHigherTaxon(int higherTaxonGbifKey) {
        if (higherTaxonGbifKey <= 0) {
            throw new IllegalArgumentException("higherTaxonGbifKey must be greater than 0");
        }
        Set<GbifTaxon> gbifTaxa = this.speciesGbifClient.retrieveSpeciesOfHigherTaxon(higherTaxonGbifKey);
        Set<NameUsage> names = new HashSet<>(gbifTaxa.size());
        for (GbifTaxon gbifTaxon : gbifTaxa) {
            names.add(new NameUsageImpl(gbifTaxon));
        }
//        Set<NameUsage> names = new HashSet<>(1);
//        names.add(new NameUsageImpl(3044855, "Alyssum alyssoides (L.) L.", "SPECIES", "EXACT"));
//        names.add(new NameUsageImpl(4928114, "A", "SPECIES", "EXACT"));
//        names.add(new NameUsageImpl(5375020, "B", "SPECIES", "EXACT"));
//        names.add(new NameUsageImpl(3052509, "C", "SPECIES", "EXACT"));
//        names.add(new NameUsageImpl(5708780, "D", "SPECIES", "EXACT"));
//        names.add(new NameUsageImpl(3046076, "E", "SPECIES", "EXACT"));
//        names.add(new NameUsageImpl(3046890, "F", "SPECIES", "EXACT"));
//        names.add(new NameUsageImpl(5373358, "G", "SPECIES", "EXACT"));
        return names;
    }

    /**
     * A convenience method that returns list of NameUsage objects which names
     * start with term string.
     *
     * @param term string to match the taxa by
     * @return
     */
    public List<NameUsage> suggestSpecies(String term) {
        List<GbifTaxon> gbifTaxa = this.speciesGbifClient.suggestSpecies(term);
        List<NameUsage> names = new ArrayList<>(gbifTaxa.size());
        for (GbifTaxon gbifTaxon : gbifTaxa) {
            names.add(new NameUsageImpl(gbifTaxon));
        }
        return names;
    }

    /**
     * Creates a list of NameUsageImpl objects from list of GbifTaxon objects.
     * Only such object are added to the result, whose higher hierarchy contains
     * higherTaxonKey
     *
     * @param source list to be checked
     * @param higherTaxonKey gbif key of higher taxon where gbif taxa from
     * source must belong to
     * @return list of NameUsageImpl object where each object is a member of the
     * higher taxon identified by higherTaxonKey
     */
    public static List<NameUsage> filterByHigherTaxon(List<NameUsage> source, int higherTaxonKey) {
        List<NameUsage> valid = new ArrayList<>();
        for (NameUsage taxon : source) {
            if (higherTaxonKey == 0 || taxon.isBelongsTo(higherTaxonKey)) {
                valid.add(taxon);
            }
        }
        return valid;
    }

    /**
     * Each key in the argument collection is used to fetch its corresponding
     * taxon from GBIF. speciesByKey(int key) is used.
     *
     * @param keys collection of gbif keys
     * @return set of NameUsage objects identified by their unique keys
     */
    public Set<NameUsage> keysToSpecies(Collection<Integer> keys) {
        Set<NameUsage> species = new HashSet<>(keys.size());
        for (Integer key : keys) {
            NameUsage name = this.speciesByKey(key);
            if (name != null) {
                species.add(name);
            }
        }
        return species;
    }

}
