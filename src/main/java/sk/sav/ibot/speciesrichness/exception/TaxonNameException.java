/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.exception;

import javax.ws.rs.core.Response;

/**
 *
 * @author Matus
 */
public class TaxonNameException extends Exception {

    public enum ExceptionSeverity { ERROR, WARNING };
    
    private final ExceptionSeverity severity;
    private final Response.Status status;
    
    public TaxonNameException() {
        this.severity = ExceptionSeverity.ERROR;
        this.status = Response.Status.INTERNAL_SERVER_ERROR;
    }

    public TaxonNameException(String message) {
        super(message);
        this.severity = ExceptionSeverity.ERROR;
        this.status = Response.Status.INTERNAL_SERVER_ERROR;
    }

    public TaxonNameException(String message, Throwable cause) {
        super(message, cause);
        this.severity = ExceptionSeverity.ERROR;
        this.status = Response.Status.INTERNAL_SERVER_ERROR;
    }

    public TaxonNameException(Throwable cause) {
        super(cause);
        this.severity = ExceptionSeverity.ERROR;
        this.status = Response.Status.INTERNAL_SERVER_ERROR;
    }
    
    public TaxonNameException(String message, ExceptionSeverity severity, Response.Status status) {
        super(message);
        this.severity = severity;
        this.status = status;
    }

    public ExceptionSeverity getSeverity() {
        return severity;
    }

    public Response.Status getStatus() {
        return status;
    }
    
}
