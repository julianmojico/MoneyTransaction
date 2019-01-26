package com.agilisys.Models;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "error")
public class Error {

    private String statusDescription;
    private String errorMessage;

    public Error(String statusDescription, String errorMessage) {
        this.statusDescription = statusDescription;
        this.errorMessage = errorMessage;
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
