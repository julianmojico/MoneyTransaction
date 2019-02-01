package com.agilisys.Models;

import com.agilisys.Util.GetterAndSetter;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Document(collection = "Accounts", schemaVersion = "1.0")
public class Account {

    @Id
    private int id;
    private String owner;
    private double currentFunds;
    private Date creationDate;

    public Account(String owner) {

        this.owner = owner;
        currentFunds = 0;
        this.creationDate = new Date();
    }

    /* public default constructor (with no arguments) needed by XMLEncoder during runtime*/
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

    public void updateAccount(Account newFields) {

        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(newFields.getClass().getDeclaredFields()));
        fields.forEach(
                field -> {
                    Object value = GetterAndSetter.callGetter(newFields, field.getName());
                    if (value != null) {
                        GetterAndSetter.callSetter(this, field.getName(), value);
                    }
                }
        );
    }
}
