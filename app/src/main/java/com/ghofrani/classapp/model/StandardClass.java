package com.ghofrani.classapp.model;

import com.ghofrani.classapp.module.DataSingleton;

import org.joda.time.LocalTime;

public class StandardClass {

    private final String name;
    private final LocalTime startTime;
    private final String startTimeString;
    private final String startTimeStringAMPM;
    private final LocalTime endTime;
    private final String endTimeString;
    private final String endTimeStringAMPM;
    private final String location;
    private final String teacher;
    private final boolean hasLocation;
    private final boolean hasTeacher;
    private final int color;

    public StandardClass(String name, LocalTime startTime, LocalTime endTime, String startTimeStringAMPM, String endTimeStringAMPM, String location, String teacher, int color) {

        this.name = name;

        this.startTime = startTime;
        this.endTime = endTime;

        this.startTimeString = this.startTime.toString().substring(0, 5);
        this.endTimeString = this.endTime.toString().substring(0, 5);

        this.startTimeStringAMPM = startTimeStringAMPM;
        this.endTimeStringAMPM = endTimeStringAMPM;

        this.location = location;
        this.teacher = teacher;
        this.color = color;

        hasLocation = !location.equals("no-location");
        hasTeacher = !teacher.equals("no-teacher");

    }

    public boolean hasLocation() {
        return hasLocation;
    }

    public boolean hasTeacher() {
        return hasTeacher;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getStartTimeString(boolean respectSettings) {

        if (respectSettings) {

            if (DataSingleton.getInstance().is24Hour())
                return startTimeString;
            else
                return startTimeStringAMPM;

        }

        return startTimeString;

    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getEndTimeString(boolean respectSettings) {

        if (respectSettings) {

            if (DataSingleton.getInstance().is24Hour())
                return endTimeString;
            else
                return endTimeStringAMPM;

        }

        return endTimeString;

    }

    public String getLocation() {
        return location;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getColor() {
        return color;
    }

}