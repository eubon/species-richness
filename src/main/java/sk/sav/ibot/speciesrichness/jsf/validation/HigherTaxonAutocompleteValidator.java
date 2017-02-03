/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.jsf.validation;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author Matus
 */
//@ManagedBean(name = "higherTaxonValidator")
//@RequestScoped
@FacesValidator(value = "higherTaxonValidatorduo")
public class HigherTaxonAutocompleteValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String value = o.toString();
        if (!NumberUtils.isNumber(value)) {
            throw new ValidatorException(new FacesMessage("Invalid input value. You have to select item from the list."));
        }
    }
    
}
