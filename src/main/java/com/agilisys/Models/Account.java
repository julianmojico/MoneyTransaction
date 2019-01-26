package com.agilisys.Models;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.Date;

@Document(collection = "Accounts", schemaVersion = "1.0")
//@XmlRootElement
public class Account {

    @Id
    private int id;
    private String owner;
    private double currentFunds;
    private Date creationDate;

    public Account(int id, String owner){
        this.id = id;
        this.owner = owner;
        currentFunds = 0;
        this.creationDate = new Date();
    }

    /* public default constructor (with no arguments) needed by XMLEncoder*/
    public Account(){}

    public void fundAddition(double amount){
        currentFunds+=amount;
    }

    public void fundSubstract(double amount) {
        currentFunds-=amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getCurrentFunds() {
        return currentFunds;
    }

    public void setCurrentFunds(double currentFunds) {
        this.currentFunds = currentFunds;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
