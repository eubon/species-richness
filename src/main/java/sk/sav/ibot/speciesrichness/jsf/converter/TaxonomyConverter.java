package sk.sav.ibot.speciesrichness.jsf.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.commons.lang3.math.NumberUtils;
import sk.sav.ibot.speciesrichness.model.Taxonomy;
import sk.sav.ibot.speciesrichness.services.TaxonomyService;

/**
 * Converter for Taxonomy. Taxonomy object is defined by its unique gbifkey.
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ManagedBean(name = "taxonomyConverter") //this is required because of the managed property injection
@RequestScoped
//@FacesConverter(value = "taxonomyConverter", forClass = Taxonomy.class) //not required because of the managedbean annot
public class TaxonomyConverter implements Converter {

    @ManagedProperty(value = "#{taxonomyService}")
    private TaxonomyService taxonomyService;

    public TaxonomyService getTaxonomyService() {
        return taxonomyService;
    }

    public void setTaxonomyService(TaxonomyService taxonomyService) {
        this.taxonomyService = taxonomyService;
    }

    /**
     * Taxonomy object is searched for in the database by given unique gbifkey.
     * @param fc
     * @param uic
     * @param string unique gbifkey
     * @return Taxonomy object with given gbifkey
     */
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (!string.isEmpty() && NumberUtils.isNumber(string)) {
            return this.taxonomyService.getTaxonByGbifkey(Long.decode(string));
        }
        return null;
    }

    /**
     * Object is converted into its representation as gbifkey.
     * @param fc
     * @param uic
     * @param o
     * @return gbifkey as string the object is identified by
     */
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o instanceof Taxonomy) {
            Taxonomy tax = (Taxonomy) o;
            return tax.getGbifkey().toString();
        }
        return "";
    }

}
