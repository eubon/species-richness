
package sk.sav.ibot.speciesrichness.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
@ManagedBean(name = "taxonomyBean")
@SessionScoped
public class TaxonomyBean implements Serializable {

    @ManagedProperty(value = "#{taxonomyService}")
    private transient TaxonomyService taxonomyService;

    @ManagedProperty(value = "#{taxonomyController}")
    private transient TaxonomyController taxonomyController;

    private int higherTaxonGbifKey; //gbifKey of the selected higher taxon
    private String higherTaxonName; //name of the selected higher taxon
    private List<Taxonomy> higherTaxonHierarchy; //hierarchy in which the higher taxon is located
    private String higherTaxonRank;

    private int speciesTaxonGbifKey;
    private String speciesTaxonName;

    /**
     * Database service for taxonomy data.
     *
     * @return
     */
    public TaxonomyService getTaxonomyService() {
        return taxonomyService;
    }

    public void setTaxonomyService(TaxonomyService taxonomyFacade) {
        this.taxonomyService = taxonomyFacade;
    }

    /**
     * GBIF service for taxonomy data
     *
     * @return
     */
    public TaxonomyController getTaxonomyController() {
        return taxonomyController;
    }

    public void setTaxonomyController(TaxonomyController taxonomyController) {
        this.taxonomyController = taxonomyController;
    }

    /**
     * Gbif key of the selected higher taxon.
     * @return 
     */
    public int getHigherTaxonGbifKey() {
        return this.higherTaxonGbifKey;
    }

    public void setHigherTaxonGbifKey(int higherTaxonGbifKey) {
        this.higherTaxonGbifKey = higherTaxonGbifKey;
    }

    /**
     * Scientific name of the selected higher taxon.
     * @return 
     */
    public String getHigherTaxonName() {
        return higherTaxonName;
    }

    public void setHigherTaxonName(String higherTaxonName) {
        this.higherTaxonName = higherTaxonName;
    }

    /**
     * Hierarchy of the selected higher taxon, starting with that taxon, edning with the Kingdom.
     * @return 
     */
    public List<Taxonomy> getHigherTaxonHierarchy() {
        return higherTaxonHierarchy;
    }

    public void setHigherTaxonHierarchy(List<Taxonomy> higherTaxonHierarchy) {
        this.higherTaxonHierarchy = higherTaxonHierarchy;
    }

    /**
     * Rank of the selected higher taxon. 
     * @return 
     */
    public String getHigherTaxonRank() {
        return higherTaxonRank;
    }

    public void setHigherTaxonRank(String higherTaxonRank) {
        this.higherTaxonRank = higherTaxonRank;
    }

    /**
     * Gbif key of the selected species.
     * @return species key, or 0 if no species has been selected
     */
    public int getSpeciesTaxonGbifKey() {
        return speciesTaxonGbifKey;
    }

    public void setSpeciesTaxonGbifKey(int speciesTaxonGbifKey) {
        this.speciesTaxonGbifKey = speciesTaxonGbifKey;
    }

    /**
     * Name of the selected species.
     * @return 
     */
    public String getSpeciesTaxonName() {
        return speciesTaxonName;
    }

    public void setSpeciesTaxonName(String speciesTaxonName) {
        this.speciesTaxonName = speciesTaxonName;
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
        this.higherTaxonGbifKey = p.getGbifkey().intValue();
        this.higherTaxonName = p.getName() + " (" + p.getRank() + ")";
        this.higherTaxonHierarchy = p.getHigherHierarchy();
        this.higherTaxonRank = p.getRank();
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
            return TaxonomyController.filterByHigherTaxon(gbifTaxa, higherTaxonGbifKey);
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
            this.speciesTaxonGbifKey = tw.getKey();
            this.speciesTaxonName = tw.getScientificName();
        }
    }

    /**
     * Returns a set of species belonging to the selected higherTaxonGbifKey.
     *
     * @return
     */
    public Set<NameUsage> speciesOfHigherTaxon() {
        if (this.higherTaxonGbifKey == 0) {
            throw new IllegalArgumentException("Higher taxon not selected");
        }
        return this.taxonomyController.speciesOfHigherTaxon(higherTaxonGbifKey);
    }

}
