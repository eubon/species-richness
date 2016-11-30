/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.jsf;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
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

    private String gbifkey; //gbifKey of the selected higher taxon
    private String higherTaxonName; //name of the selected higher taxon
    private String higherTaxonHierarchy; //hierarchy in which the higher taxon is located
    private String higherTaxonRank;

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

    public String getHigherTaxonRank() {
        return higherTaxonRank;
    }

    public void setHigherTaxonRank(String higherTaxonRank) {
        this.higherTaxonRank = higherTaxonRank;
    }
    
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
        this.higherTaxonRank = p.getRank();
    }

}
