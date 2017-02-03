/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import sk.sav.ibot.speciesrichness.logic.NameUsage;
import sk.sav.ibot.speciesrichness.logic.NameUsageImpl;
import sk.sav.ibot.speciesrichness.logic.TaxonomyController;
import sk.sav.ibot.speciesrichness.services.TaxonomyService;
import sk.sav.ibot.speciesrichness.model.Taxonomy;

/**
 * Handles autocomplete of the higher taxa and retrieval of species under
 * selected higher taxon. Higher taxa with their correspondig gbifKeys are
 * stored in the database (entity Taxon).
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ManagedBean
@SessionScoped
public class TaxonomyBean implements Serializable {

    @ManagedProperty(value = "#{taxonomyService}")
    private transient TaxonomyService taxonomyService;

    @ManagedProperty(value = "#{taxonomyController}")
    private transient TaxonomyController taxonomyController;

    private int supertaxonGbifKey; //gbifKey of the selected higher taxon
    private String supertaxonName; //name of the selected higher taxon
    private List<Taxonomy> supertaxonHierarchy; //hierarchy in which the higher taxon is located
    private String supertaxonRank;

    private int taxonGbifKey;
    private String taxonName;

    public void setTaxonomyService(TaxonomyService taxonomyFacade) {
        this.taxonomyService = taxonomyFacade;
    }

    public TaxonomyService getTaxonomyService() {
        return taxonomyService;
    }

    public TaxonomyController getTaxonomyController() {
        return taxonomyController;
    }

    public void setTaxonomyController(TaxonomyController taxonomyController) {
        this.taxonomyController = taxonomyController;
    }

    public int getSupertaxonGbifKey() {
        return supertaxonGbifKey;
    }

    public void setSupertaxonGbifKey(int supertaxonGbifKey) {
        this.supertaxonGbifKey = supertaxonGbifKey;
    }

    public String getSupertaxonName() {
        return supertaxonName;
    }

    public void setSupertaxonName(String supertaxonName) {
        this.supertaxonName = supertaxonName;
    }

    public List<Taxonomy> getSupertaxonHierarchy() {
        return supertaxonHierarchy;
    }

    public void setSupertaxonHierarchy(List<Taxonomy> supertaxonHierarchy) {
        this.supertaxonHierarchy = supertaxonHierarchy;
    }

    public String getSupertaxonRank() {
        return supertaxonRank;
    }

    public void setSupertaxonRank(String supertaxonRank) {
        this.supertaxonRank = supertaxonRank;
    }

    public int getTaxonGbifKey() {
        return taxonGbifKey;
    }

    public void setTaxonGbifKey(int taxonGbifKey) {
        this.taxonGbifKey = taxonGbifKey;
    }

    public String getTaxonName() {
        return taxonName;
    }

    public void setTaxonName(String taxonName) {
        this.taxonName = taxonName;
    }

    /**
     * Populates higher taxon autocomplete with Taxonomy results by term.
     *
     * @param query Term to search taxons by
     * @return List of Taxonomy results beginning with query
     */
    public List<Taxonomy> completeHigherTaxon(String query) {
        return this.taxonomyService.getHigherTaxonStartsWith(query);
    }

    /**
     * Handles higher taxon autocomplete when user selects value.
     *
     * @param e
     */
    public void handleSelectHigherTaxon(SelectEvent e) {
        Taxonomy p = (Taxonomy) e.getObject();
        this.supertaxonGbifKey = p.getGbifkey().intValue();
        this.supertaxonName = p.getName() + " (" + p.getRank() + ")";
        this.supertaxonHierarchy = p.getHigherHierarchy();
        this.supertaxonRank = p.getRank();
    }

    /**
     * Populates species autocomplete with NameUsageImpl results. The
     * autocomplete is triggered after the first space character in the query.
     *
     * @param query term to search species by. Two words separated by space are
     * required, where second word can be incomplete, e.g. "Alyssum mont"
     * @return list of NameUsageImpl objects, if the query is valid
     */
    public List<NameUsage> completeSpecies(String query) {
        if (query.matches("\\w+ \\w+")) {
            List<NameUsage> gbifTaxa = this.taxonomyController.suggestSpecies(query);
            return TaxonomyController.filterByHigherTaxon(gbifTaxa, supertaxonGbifKey);
        }
        return new ArrayList<>();
    }

    /**
     * Handles species autocomplete when user selects value
     *
     * @param e
     */
    public void handleSelectSpecies(SelectEvent e) {
        if (e.getObject() instanceof NameUsageImpl) {
            NameUsage tw = (NameUsageImpl) e.getObject();
            this.taxonGbifKey = tw.getKey();
            this.taxonName = tw.getScientificName();
        }
    }

}
