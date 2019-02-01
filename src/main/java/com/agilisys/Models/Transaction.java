package com.agilisys.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "Transactions", schemaVersion = "1.0")
public class Transaction {

    /*Model heavily annotated to allow Swagger definition and request examples*/

    @Id
    @ApiModelProperty(hidden = true)
    private String id;
    @NotNull
    @ApiModelProperty(required = true, example = "1")
    private int sourceAccount;
    @NotNull
    @ApiModelProperty(required = true, example = "2")
    private int destAccount;
    @ApiModelProperty(hidden = true)
    private Status status;
    @ApiModelProperty(hidden = true)
    private Date submissionDate;
    @ApiModelProperty(hidden = true)
    private Date endDate;
    @JsonProperty
    @ApiModelProperty(required = true, example = "300")
    private double amount;
    @ApiModelProperty(hidden = true)
    private String message;

    /* public default constructor (with no arguments) needed by XMLEncoder*/
    public Transaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(int sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public int getDestAccount() {
        return destAccount;
    }

    public void setDestAccount(int destAccount) {
        this.destAccount = destAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
