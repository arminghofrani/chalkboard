package com.ghofrani.classapp.module;

import com.ghofrani.classapp.model.Event;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class DataSingleton {

    private static DataSingleton dataSingletonInstance = null;

    private StandardClass currentClass;

    private StandardClass nextClass;
    private ArrayList<StandardClass> nextClassesArrayList;

    private ArrayList<StandardClass> tomorrowClassesArrayList;

    private String minutesLeftText;
    private int progressBarProgress;
    private String progressBarText;

    private ArrayList<StandardClass> sundayClasses;
    private ArrayList<StandardClass> mondayClasses;
    private ArrayList<StandardClass> tuesdayClasses;
    private ArrayList<StandardClass> wednesdayClasses;
    private ArrayList<StandardClass> thursdayClasses;
    private ArrayList<StandardClass> fridayClasses;
    private ArrayList<StandardClass> saturdayClasses;

    private ArrayList<SlimClass> allClassesArrayList;
    private ArrayList<String> allClassNamesArrayList;

    private ArrayList<Event> todayEventArrayList;
    private ArrayList<Event> tomorrowEventArrayList;
    private ArrayList<Event> thisWeekEventArrayList;
    private ArrayList<Event> nextWeekEventArrayList;
    private ArrayList<Event> thisMonthEventArrayList;
    private ArrayList<Event> beyondThisMonthEventArrayList;
    private ArrayList<Event> pastEventArrayList;

    private ArrayList<Object> eventDataArrayList;

    private DateTime thisWeekEnd;
    private DateTime nextWeekEnd;

    private int selectedTabPosition;

    private boolean isExpandableListViewCollapsed;

    private boolean is24Hour;

    private boolean recreate;
    private boolean isChangedFirstDay;

    private boolean reactToBroadcastData;

    private DataSingleton() {

        nextClassesArrayList = new ArrayList<>();

        tomorrowClassesArrayList = new ArrayList<>();

        progressBarProgress = 0;
        progressBarText = "0%";

        sundayClasses = null;
        mondayClasses = null;
        tuesdayClasses = null;
        wednesdayClasses = null;
        thursdayClasses = null;
        fridayClasses = null;
        saturdayClasses = null;

        allClassesArrayList = new ArrayList<>();
        allClassNamesArrayList = new ArrayList<>();

        todayEventArrayList = new ArrayList<>();
        tomorrowEventArrayList = new ArrayList<>();
        thisWeekEventArrayList = new ArrayList<>();
        nextWeekEventArrayList = new ArrayList<>();
        thisMonthEventArrayList = new ArrayList<>();
        beyondThisMonthEventArrayList = new ArrayList<>();
        pastEventArrayList = new ArrayList<>();

        eventDataArrayList = new ArrayList<>();

        selectedTabPosition = 0;

        isExpandableListViewCollapsed = false;

        is24Hour = true;

        recreate = false;
        isChangedFirstDay = false;

        reactToBroadcastData = true;

    }

    public static void initInstance() {

        if (dataSingletonInstance == null)
            dataSingletonInstance = new DataSingleton();

    }

    public static DataSingleton getInstance() {

        return dataSingletonInstance;

    }

    public StandardClass getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(StandardClass currentClass) {
        this.currentClass = currentClass;
    }

    public StandardClass getNextClass() {
        return nextClass;
    }

    public void setNextClass(StandardClass nextClass) {
        this.nextClass = nextClass;
    }

    public ArrayList<StandardClass> getNextClassesArrayList() {
        return nextClassesArrayList;
    }

    public void setNextClassesArrayList(ArrayList<StandardClass> nextClassesArrayList) {
        this.nextClassesArrayList = nextClassesArrayList;
    }

    public ArrayList<StandardClass> getTomorrowClassesArrayList() {
        return tomorrowClassesArrayList;
    }

    public void setTomorrowClassesArrayList(ArrayList<StandardClass> tomorrowClassesArrayList) {
        this.tomorrowClassesArrayList = tomorrowClassesArrayList;
    }

    public String getMinutesLeftText() {
        return minutesLeftText;
    }

    public void setMinutesLeftText(String minutesLeftText) {
        this.minutesLeftText = minutesLeftText;
    }

    public int getProgressBarProgress() {
        return progressBarProgress;
    }

    public void setProgressBarProgress(int progressBarProgress) {
        this.progressBarProgress = progressBarProgress;
    }

    public String getProgressBarText() {
        return progressBarText;
    }

    public void setProgressBarText(String progressBarText) {
        this.progressBarText = progressBarText;
    }

    public ArrayList<StandardClass> getSundayClasses() {
        return sundayClasses;
    }

    public void setSundayClasses(ArrayList<StandardClass> sundayClasses) {
        this.sundayClasses = sundayClasses;
    }

    public ArrayList<StandardClass> getMondayClasses() {
        return mondayClasses;
    }

    public void setMondayClasses(ArrayList<StandardClass> mondayClasses) {
        this.mondayClasses = mondayClasses;
    }

    public ArrayList<StandardClass> getTuesdayClasses() {
        return tuesdayClasses;
    }

    public void setTuesdayClasses(ArrayList<StandardClass> tuesdayClasses) {
        this.tuesdayClasses = tuesdayClasses;
    }

    public ArrayList<StandardClass> getWednesdayClasses() {
        return wednesdayClasses;
    }

    public void setWednesdayClasses(ArrayList<StandardClass> wednesdayClasses) {
        this.wednesdayClasses = wednesdayClasses;
    }

    public ArrayList<StandardClass> getThursdayClasses() {
        return thursdayClasses;
    }

    public void setThursdayClasses(ArrayList<StandardClass> thursdayClasses) {
        this.thursdayClasses = thursdayClasses;
    }

    public ArrayList<StandardClass> getFridayClasses() {
        return fridayClasses;
    }

    public void setFridayClasses(ArrayList<StandardClass> fridayClasses) {
        this.fridayClasses = fridayClasses;
    }

    public ArrayList<StandardClass> getSaturdayClasses() {
        return saturdayClasses;
    }

    public void setSaturdayClasses(ArrayList<StandardClass> saturdayClasses) {
        this.saturdayClasses = saturdayClasses;
    }

    public ArrayList<SlimClass> getAllClassesArrayList() {
        return allClassesArrayList;
    }

    public void setAllClassesArrayList(ArrayList<SlimClass> allClassesArrayList) {
        this.allClassesArrayList = allClassesArrayList;
    }

    public ArrayList<String> getAllClassNamesArrayList() {
        return allClassNamesArrayList;
    }

    public void setAllClassNamesArrayList(ArrayList<String> allClassNamesArrayList) {
        this.allClassNamesArrayList = allClassNamesArrayList;
    }

    public ArrayList<Event> getTodayEventArrayList() {
        return todayEventArrayList;
    }

    public void setTodayEventArrayList(ArrayList<Event> todayEventArrayList) {
        this.todayEventArrayList = todayEventArrayList;
    }

    public ArrayList<Event> getTomorrowEventArrayList() {
        return tomorrowEventArrayList;
    }

    public void setTomorrowEventArrayList(ArrayList<Event> tomorrowEventArrayList) {
        this.tomorrowEventArrayList = tomorrowEventArrayList;
    }

    public ArrayList<Event> getThisWeekEventArrayList() {
        return thisWeekEventArrayList;
    }

    public void setThisWeekEventArrayList(ArrayList<Event> thisWeekEventArrayList) {
        this.thisWeekEventArrayList = thisWeekEventArrayList;
    }

    public ArrayList<Event> getNextWeekEventArrayList() {
        return nextWeekEventArrayList;
    }

    public void setNextWeekEventArrayList(ArrayList<Event> nextWeekEventArrayList) {
        this.nextWeekEventArrayList = nextWeekEventArrayList;
    }

    public ArrayList<Event> getThisMonthEventArrayList() {
        return thisMonthEventArrayList;
    }

    public void setThisMonthEventArrayList(ArrayList<Event> thisMonthEventArrayList) {
        this.thisMonthEventArrayList = thisMonthEventArrayList;
    }

    public ArrayList<Event> getBeyondThisMonthEventArrayList() {
        return beyondThisMonthEventArrayList;
    }

    public void setBeyondThisMonthEventArrayList(ArrayList<Event> beyondThisMonthEventArrayList) {
        this.beyondThisMonthEventArrayList = beyondThisMonthEventArrayList;
    }

    public ArrayList<Event> getPastEventArrayList() {
        return pastEventArrayList;
    }

    public void setPastEventArrayList(ArrayList<Event> pastEventArrayList) {
        this.pastEventArrayList = pastEventArrayList;
    }

    public ArrayList<Object> getEventDataArrayList() {
        return eventDataArrayList;
    }

    public void setEventDataArrayList(ArrayList<Object> dataArrayList) {
        this.eventDataArrayList = dataArrayList;
    }

    public DateTime getThisWeekEnd() {
        return thisWeekEnd;
    }

    public void setThisWeekEnd(DateTime thisWeekEnd) {
        this.thisWeekEnd = thisWeekEnd;
    }

    public DateTime getNextWeekEnd() {
        return nextWeekEnd;
    }

    public void setNextWeekEnd(DateTime nextWeekEnd) {
        this.nextWeekEnd = nextWeekEnd;
    }

    public int getSelectedTabPosition() {
        return selectedTabPosition;
    }

    public void setSelectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
    }

    public boolean isExpandableListViewCollapsed() {
        return isExpandableListViewCollapsed;
    }

    public void setIsExpandableListViewCollapsed(boolean isExpandableListViewCollapsed) {
        this.isExpandableListViewCollapsed = isExpandableListViewCollapsed;
    }

    public boolean is24Hour() {
        return is24Hour;
    }

    public void setIs24Hour(boolean is24Hour) {
        this.is24Hour = is24Hour;
    }

    public boolean isRecreate() {
        return recreate;
    }

    public void setRecreate(boolean recreate) {
        this.recreate = recreate;
    }

    public boolean isChangedFirstDay() {
        return isChangedFirstDay;
    }

    public void setChangedFirstDay(boolean changedFirstDay) {
        isChangedFirstDay = changedFirstDay;
    }

    public boolean isReactToBroadcastData() {
        return reactToBroadcastData;
    }

    public void setReactToBroadcastData(boolean reactToBroadcastData) {
        this.reactToBroadcastData = reactToBroadcastData;
    }

}