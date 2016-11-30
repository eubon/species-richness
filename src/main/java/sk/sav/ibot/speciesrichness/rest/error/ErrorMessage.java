/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.error;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO class of the error message. Used for REST.
 * @author Matus
 */
@XmlRootElement(name = "error")
public class ErrorMessage {
 
    private int status;
    private String message;
//    private String developerMessage;

    public ErrorMessage() {
    }

    public ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public String getDeveloperMessage() {
//        return developerMessage;
//    }
//
//    public void setDeveloperMessage(String developerMessage) {
//        this.developerMessage = developerMessage;
//    }
    
}
