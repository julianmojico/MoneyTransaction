package com.agilisys.Models;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "Transactions", schemaVersion = "1.0")
//@XmlRootElement
public class Transaction {


    /* String is not the most performant choice for index type,
    this is to avoid XMLEncoder incompatibly with UUID since this type doesn't have default constructor */
    @Id
    private String id;
    @NotNull
    private int sourceAccount;
    @NotNull
    private int destAccount;
    private Status status;
    private Date submissionDate;
    private Date endDate;
    private double amount;
    private Currency currency;
    private String message;

    /* public default constructor (with no arguments) needed by XMLEncoder*/
    public Transaction(){}

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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
