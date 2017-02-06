package sk.sav.ibot.speciesrichness.rest.error;

import io.swagger.annotations.ApiModel;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO class of the error message. Used for REST.
 *
 * @author
 * (http://www.codingpedia.org/ama/error-handling-in-rest-api-with-jersey/)
 */
@ApiModel(value = "Error")
@XmlRootElement(name = "error")
public class ErrorMessage {

    private int status;
    private String message;
    private String developerMessage;

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

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

}
