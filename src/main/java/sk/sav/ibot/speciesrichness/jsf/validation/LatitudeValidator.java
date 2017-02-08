/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.jsf.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Matus
 */
@FacesValidator(value = "latitudeValidator")
public class LatitudeValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        if (o == null || o.toString().isEmpty()) {
            return;
        }
        UIInput input = (UIInput) uic;
        String label = (String) input.getAttributes().get("label");
        double val = (double) o;
        
        if (val < -90.0 || val > 90.0) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, label + ": Incorrect value!", "Value must be in range from -90 to 90 degrees."));
        }
    }
    
}
