/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.ibot.speciesrichness.rest.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps throwable exceptions to the readable representation. Format is specified in
 * http header Accept; e.g application/json, application/xml
 *
 * @author Matus
 * (http://www.codingpedia.org/ama/error-handling-in-rest-api-with-jersey/)
 */
@Provider
public class SpeciesRichnessThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private HttpHeaders headers;

    /**
     * Override of the method toResponse.
     * @param e
     * @return response containing error message
     */
    @Override
    public Response toResponse(Throwable e) {
        ErrorMessage errorMessage = new ErrorMessage();
        this.setHttpStatus(e, errorMessage);
        errorMessage.setMessage(e.getMessage());
        StringWriter errorStackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(errorStackTrace));
        errorMessage.setDeveloperMessage(errorStackTrace.toString());
        return Response.status(errorMessage.getStatus()).entity(errorMessage).type(headers.getMediaType()).build();
    }

    private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
        if (ex instanceof WebApplicationException) {
            errorMessage.setStatus(((WebApplicationException) ex).getResponse().getStatus());
        } else {
            errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); //defaults to internal server error 500
        }
    }

}
