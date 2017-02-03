/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Matus
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

    public NameUsage parseName(String name, boolean isSpecies, boolean strict) throws TaxonNameException {
        //default
        if (isSpecies && name.equals(Defaults.DEFAULT_SPECIES)) {
            return new NameUsageImpl(Integer.parseInt(Defaults.DEFAULT_SPECIES), "", "", "EXACT");
        }
        //accept gbif key or scientific name
        if (!name.matches("[1-9][0-9]*|[A-Za-z]+[A-Za-z \\.\\(\\)]*")) {
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
        //
        //int key = gbifTaxon.getKey() == null ? gbifTaxon.getUsageKey() : gbifTaxon.getKey();
        return new NameUsageImpl(gbifTaxon);
    }

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

    public Set<NameUsage> speciesOfHigherTaxon(int higherTaxonGbifKey) {
        if (higherTaxonGbifKey <= 0) {
            throw new IllegalArgumentException("higherTaxonGbifKey must be greater than 0");
        }
//        Set<GbifTaxon> gbifTaxa = this.speciesGbifClient.retrieveSpeciesOfHigherTaxon(higherTaxonGbifKey);
//        Set<NameUsage> names = new HashSet<>(gbifTaxa.size());
//        for (GbifTaxon gbifTaxon : gbifTaxa) {
//            names.add(new NameUsageImpl(gbifTaxon));
//        }
        Set<NameUsage> names = new HashSet<>(1);
        names.add(new NameUsageImpl(3044855, "Alyssum alyssoides (L.) L.", "SPECIES", "EXACT"));
        names.add(new NameUsageImpl(4928114, "A", "SPECIES", "EXACT"));
        names.add(new NameUsageImpl(5375020, "B", "SPECIES", "EXACT"));
        names.add(new NameUsageImpl(3052509, "C", "SPECIES", "EXACT"));
        names.add(new NameUsageImpl(5708780, "D", "SPECIES", "EXACT"));
        names.add(new NameUsageImpl(3046076, "E", "SPECIES", "EXACT"));
        names.add(new NameUsageImpl(3046890, "F", "SPECIES", "EXACT"));
        names.add(new NameUsageImpl(5373358, "G", "SPECIES", "EXACT"));
        return names;
    }

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
