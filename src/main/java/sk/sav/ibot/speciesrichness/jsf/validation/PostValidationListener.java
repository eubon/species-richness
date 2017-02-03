/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.jsf.validation;

import javax.faces.component.UIInput;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 *
 * @author Matus
 */
public class PostValidationListener implements SystemEventListener {
 
    @Override
    public boolean isListenerForSource(Object source) {
        return true;
    }
 
    /**
     *
     * @param event
     * @throws AbortProcessingException
     */
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        UIInput source = (UIInput) event.getSource();
 
        if(!source.isValid()) {
            //source.getAttributes().put("styleClass", "input-invalid");
            String styleClass = (String) source.getAttributes().get("styleClass");
            source.getAttributes().put("styleClass", styleClass + " input-invalid");
        }
    }
}
