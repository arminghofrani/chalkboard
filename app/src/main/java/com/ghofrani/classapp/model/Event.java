package com.ghofrani.classapp.model;

import org.joda.time.DateTime;

public class Event {

    public static final int TYPE_HOMEWORK = 0;
    public static final int TYPE_TASK = 1;
    public static final int TYPE_EXAM = 2;

    private final String name;
    private final String description;
    private final int type;
    private final String className;
    private final DateTime dateTime;
    private final boolean attach;
    private final boolean remind;
    private final int color;

    public Event(String name, String description, int type, String className, DateTime dateTime, boolean attach, boolean remind, int color) {

        this.name = name;
        this.description = description;
        this.type = type;
        this.className = className;
        this.dateTime = dateTime;
        this.attach = attach;
        this.remind = remind;
        this.color = color;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public String getClassName() {
        return className;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public boolean isAttach() {
        return attach;
    }

    public boolean isRemind() {
        return remind;
    }

    public int getColor() {
        return color;
    }

}