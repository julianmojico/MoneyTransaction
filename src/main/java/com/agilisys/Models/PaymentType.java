package com.agilisys.Models;

public enum PaymentType {
    DEBIT("DEBIT"),
    CREDIT("CREDIT"),
    OTHER("OTHER");

    private String name;

    PaymentType(String name) {
        this.name = name;
    }
}
