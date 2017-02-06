
package sk.sav.ibot.speciesrichness.jsf.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.commons.lang3.math.NumberUtils;
import sk.sav.ibot.speciesrichness.logic.NameUsageImpl;
import sk.sav.ibot.speciesrichness.logic.TaxonomyController;

/**
 * Converter for NameUsageImpl.
 *
 * @author Matus Kempa, Institute of Botany, SAS, Bratislava, Slovakia
 */
@ManagedBean(name = "nameUsageConverter")
@RequestScoped
public class NameUsageConverter implements Converter {

    @ManagedProperty(value = "#{taxonomyController}")
    private transient TaxonomyController taxonomyController;

    public TaxonomyController getTaxonomyController() {
        return taxonomyController;
    }

    public void setTaxonomyController(TaxonomyController taxonomyController) {
        this.taxonomyController = taxonomyController;
    }

    /**
     * If string is gbifkey, the taxon is fetched from GBIF and returned as an
     * instance of NameUsageImpl.
     *
     * @param fc
     * @param uic
     * @param string
     * @return Instance of NameUsageImpl if string was found in GBIF. Null
     * otherwise.
     */
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (!string.isEmpty() && NumberUtils.isNumber(string)) {
            return this.taxonomyController.speciesByKey(Integer.parseInt(string));
        }
        return null;
    }

    /**
     * Object instance of NameUsageImpl is uniquely identified by its gbifKey
     * property.
     *
     * @param fc
     * @param uic
     * @param o
     * @return gbifKey as string, if object is instance of NameUsageImpl. Empty
     * string otherwise
     */
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o instanceof NameUsageImpl) {
            NameUsageImpl tw = (NameUsageImpl) o;
            return String.valueOf(tw.getKey());
        }
        return "";
    }

}
