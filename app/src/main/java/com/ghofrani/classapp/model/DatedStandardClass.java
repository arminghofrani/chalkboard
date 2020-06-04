package com.ghofrani.classapp.model;

import org.joda.time.DateTime;

public class DatedStandardClass {

    private final StandardClass standardClass;
    private final DateTime dateTime;

    public DatedStandardClass(StandardClass standardClass, DateTime dateTime) {

        this.standardClass = standardClass;
        this.dateTime = dateTime;

    }

    public StandardClass getStandardClass() {
        return standardClass;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

}