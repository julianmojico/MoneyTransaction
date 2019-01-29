package com.agilisys.Models;

import javax.ws.rs.ext.Provider;

@Provider
public class Error {

    private String statusDescription;
    private String errorMessage;

    public Error(String statusDescription, String errorMessage) {
        this.statusDescription = statusDescription;
        this.errorMessage = errorMessage;
    }

    public Error() {

    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
