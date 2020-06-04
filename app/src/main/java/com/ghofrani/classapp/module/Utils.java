package com.ghofrani.classapp.module;

import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.StandardClass;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;

public class Utils {

    public static boolean setThemeGetContrastFlag(AppCompatActivity appCompatActivity) {

        int colorPrimary = PreferenceManager.getDefaultSharedPreferences(appCompatActivity).getInt("primary_color", ContextCompat.getColor(appCompatActivity, R.color.teal));

        if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.red))
            appCompatActivity.getTheme().applyStyle(R.style.primary_red, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.pink))
            appCompatActivity.getTheme().applyStyle(R.style.primary_pink, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.purple))
            appCompatActivity.getTheme().applyStyle(R.style.primary_purple, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.deep_purple))
            appCompatActivity.getTheme().applyStyle(R.style.primary_deep_purple, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.indigo))
            appCompatActivity.getTheme().applyStyle(R.style.primary_indigo, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.blue))
            appCompatActivity.getTheme().applyStyle(R.style.primary_blue, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.light_blue))
            appCompatActivity.getTheme().applyStyle(R.style.primary_light_blue, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.cyan))
            appCompatActivity.getTheme().applyStyle(R.style.primary_cyan, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.teal))
            appCompatActivity.getTheme().applyStyle(R.style.primary_teal, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.green))
            appCompatActivity.getTheme().applyStyle(R.style.primary_green, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.light_green))
            appCompatActivity.getTheme().applyStyle(R.style.primary_light_green, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.lime))
            appCompatActivity.getTheme().applyStyle(R.style.primary_lime, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.orange))
            appCompatActivity.getTheme().applyStyle(R.style.primary_orange, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.deep_orange))
            appCompatActivity.getTheme().applyStyle(R.style.primary_deep_orange, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.blue_grey))
            appCompatActivity.getTheme().applyStyle(R.style.primary_blue_grey, true);

        int colorAccent = PreferenceManager.getDefaultSharedPreferences(appCompatActivity).getInt("accent_color", ContextCompat.getColor(appCompatActivity, R.color.deep_orange_accent));
        boolean contrastFlag = false;

        if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.red_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_red, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.pink_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_pink, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.purple_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_purple, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.deep_purple_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_deep_purple, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.indigo_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_indigo, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.blue_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_blue, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.light_blue_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_light_blue, true);
            contrastFlag = true;
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.cyan_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_cyan, true);
            contrastFlag = true;
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.teal_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_teal, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.green_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_green, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.lime_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_lime, true);
            contrastFlag = true;
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.yellow_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_yellow, true);
            contrastFlag = true;
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.amber_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_amber, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.orange_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_orange, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.deep_orange_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_deep_orange, true);
        }

        return contrastFlag;

    }

    public static void setTheme(AppCompatActivity appCompatActivity) {

        int colorPrimary = PreferenceManager.getDefaultSharedPreferences(appCompatActivity).getInt("primary_color", ContextCompat.getColor(appCompatActivity, R.color.teal));

        if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.red))
            appCompatActivity.getTheme().applyStyle(R.style.primary_red, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.pink))
            appCompatActivity.getTheme().applyStyle(R.style.primary_pink, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.purple))
            appCompatActivity.getTheme().applyStyle(R.style.primary_purple, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.deep_purple))
            appCompatActivity.getTheme().applyStyle(R.style.primary_deep_purple, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.indigo))
            appCompatActivity.getTheme().applyStyle(R.style.primary_indigo, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.blue))
            appCompatActivity.getTheme().applyStyle(R.style.primary_blue, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.light_blue))
            appCompatActivity.getTheme().applyStyle(R.style.primary_light_blue, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.cyan))
            appCompatActivity.getTheme().applyStyle(R.style.primary_cyan, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.teal))
            appCompatActivity.getTheme().applyStyle(R.style.primary_teal, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.green))
            appCompatActivity.getTheme().applyStyle(R.style.primary_green, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.light_green))
            appCompatActivity.getTheme().applyStyle(R.style.primary_light_green, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.lime))
            appCompatActivity.getTheme().applyStyle(R.style.primary_lime, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.orange))
            appCompatActivity.getTheme().applyStyle(R.style.primary_orange, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.deep_orange))
            appCompatActivity.getTheme().applyStyle(R.style.primary_deep_orange, true);
        else if (colorPrimary == ContextCompat.getColor(appCompatActivity, R.color.blue_grey))
            appCompatActivity.getTheme().applyStyle(R.style.primary_blue_grey, true);

        int colorAccent = PreferenceManager.getDefaultSharedPreferences(appCompatActivity).getInt("accent_color", ContextCompat.getColor(appCompatActivity, R.color.deep_orange_accent));

        if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.red_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_red, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.pink_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_pink, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.purple_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_purple, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.deep_purple_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_deep_purple, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.indigo_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_indigo, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.blue_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_blue, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.light_blue_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_light_blue, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.cyan_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_cyan, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.teal_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_teal, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.green_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_green, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.lime_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_lime, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.yellow_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_yellow, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.amber_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_amber, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.orange_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_orange, true);
        } else if (colorAccent == ContextCompat.getColor(appCompatActivity, R.color.deep_orange_accent)) {
            appCompatActivity.getTheme().applyStyle(R.style.accent_deep_orange, true);
        }

    }

    public static void setClassesArrayListOfDay(int day, ArrayList<StandardClass> standardClassArrayList) {

        switch (day) {

            case DateTimeConstants.MONDAY:

                DataSingleton.getInstance().setMondayClasses(standardClassArrayList);

                break;

            case DateTimeConstants.TUESDAY:

                DataSingleton.getInstance().setTuesdayClasses(standardClassArrayList);

                break;

            case DateTimeConstants.WEDNESDAY:

                DataSingleton.getInstance().setWednesdayClasses(standardClassArrayList);

                break;

            case DateTimeConstants.THURSDAY:

                DataSingleton.getInstance().setThursdayClasses(standardClassArrayList);

                break;

            case DateTimeConstants.FRIDAY:

                DataSingleton.getInstance().setFridayClasses(standardClassArrayList);

                break;

            case DateTimeConstants.SATURDAY:

                DataSingleton.getInstance().setSaturdayClasses(standardClassArrayList);

                break;

            case DateTimeConstants.SUNDAY:

                DataSingleton.getInstance().setSundayClasses(standardClassArrayList);

                break;

        }

    }

    public static ArrayList<StandardClass> getClassesArrayListOfDay(int day) {

        switch (day) {

            case DateTimeConstants.MONDAY:

                return DataSingleton.getInstance().getMondayClasses();

            case DateTimeConstants.TUESDAY:

                return DataSingleton.getInstance().getTuesdayClasses();

            case DateTimeConstants.WEDNESDAY:

                return DataSingleton.getInstance().getWednesdayClasses();

            case DateTimeConstants.THURSDAY:

                return DataSingleton.getInstance().getThursdayClasses();

            case DateTimeConstants.FRIDAY:

                return DataSingleton.getInstance().getFridayClasses();

            case DateTimeConstants.SATURDAY:

                return DataSingleton.getInstance().getSaturdayClasses();

            case DateTimeConstants.SUNDAY:

                return DataSingleton.getInstance().getSundayClasses();

            default:

                return new ArrayList<>();

        }

    }

    public static int getPlusDaysTill(int day) {

        final DateTime now = DateTime.now();

        switch (day) {

            case DateTimeConstants.MONDAY:

                return 8 - now.getDayOfWeek();

            case DateTimeConstants.TUESDAY:

                if (now.getDayOfWeek() == DateTimeConstants.TUESDAY) {

                    return 7;

                } else if (now.getDayOfWeek() < DateTimeConstants.TUESDAY) {

                    return 2 - now.getDayOfWeek();

                } else {

                    return 9 - now.getDayOfWeek();

                }

            case DateTimeConstants.WEDNESDAY:

                if (now.getDayOfWeek() == DateTimeConstants.WEDNESDAY) {

                    return 7;

                } else if (now.getDayOfWeek() < DateTimeConstants.WEDNESDAY) {

                    return 3 - now.getDayOfWeek();

                } else {

                    return 10 - now.getDayOfWeek();

                }

            case DateTimeConstants.THURSDAY:

                if (now.getDayOfWeek() == DateTimeConstants.THURSDAY) {

                    return 7;

                } else if (now.getDayOfWeek() < DateTimeConstants.THURSDAY) {

                    return 4 - now.getDayOfWeek();

                } else {

                    return 11 - now.getDayOfWeek();

                }

            case DateTimeConstants.FRIDAY:

                if (now.getDayOfWeek() == DateTimeConstants.FRIDAY) {

                    return 7;

                } else if (now.getDayOfWeek() < DateTimeConstants.FRIDAY) {

                    return 5 - now.getDayOfWeek();

                } else {

                    return 12 - now.getDayOfWeek();

                }

            case DateTimeConstants.SATURDAY:

                if (now.getDayOfWeek() == DateTimeConstants.SATURDAY) {

                    return 7;

                } else if (now.getDayOfWeek() < DateTimeConstants.SATURDAY) {

                    return 6 - now.getDayOfWeek();

                } else {

                    return 12 - now.getDayOfWeek();

                }

            case DateTimeConstants.SUNDAY:

                if (now.getDayOfWeek() == DateTimeConstants.SUNDAY) {

                    return 7;

                } else {

                    return 7 - now.getDayOfWeek();

                }

            default:

                return 0;

        }

    }

}