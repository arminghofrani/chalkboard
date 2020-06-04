package com.ghofrani.classapp.event;

public class Update {

    private final boolean data;
    private final boolean events;
    private final boolean timetable;
    private final boolean classes;
    private final boolean notifyEvents;

    public Update(boolean data, boolean events, boolean timetable, boolean classes, boolean notifyEvents) {

        this.data = data;
        this.events = events;
        this.timetable = timetable;
        this.classes = classes;
        this.notifyEvents = notifyEvents;

    }

    public boolean isData() {
        return data;
    }

    public boolean isEvents() {
        return events;
    }

    public boolean isTimetable() {
        return timetable;
    }

    public boolean isClasses() {
        return classes;
    }

    public boolean isNotifyEvents() {
        return notifyEvents;
    }

}