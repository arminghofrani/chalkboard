package com.ghofrani.classapp.model;

public class StringWithID {

    private final String string;
    private final int ID;

    public StringWithID(String string, int ID) {

        this.string = string;
        this.ID = ID;

    }

    public String getString() {
        return string;
    }

    public int getID() {
        return ID;
    }

}