package com.ghofrani.classapp.model;

public class EventWithID {

    private final Event event;
    private final int ID;

    public EventWithID(Event event, int ID) {

        this.event = event;
        this.ID = ID;

    }

    public Event getEvent() {
        return event;
    }

    public int getID() {
        return ID;
    }

}