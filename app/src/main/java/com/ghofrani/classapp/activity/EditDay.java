package com.ghofrani.classapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.EditClassList;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class EditDay extends AppCompatActivity {

    private String startTimeTextAdd;
    private String endTimeTextAdd;
    private String startTimeTextEdit;
    private String endTimeTextEdit;
    private ListView listView;
    private EditClassList listAdapter;
    private ArrayList<StandardClass> standardClassArrayList;
    private List<LocalTime> startTimeForPosition;
    private List<LocalTime> endTimeForPosition;
    private List<Integer> noClassIndexList;
    private DateTimeFormatter dateTimeFormatterAMPM;
    private DateTimeFormatter dateTimeFormatter24Hour;
    private boolean is24Hour;
    private int day;
    private ArrayList<String> days;
    private boolean skip = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_day);

        final Toolbar toolbar = findViewById(R.id.activity_edit_day_toolbar);

        switch (Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("first_day_of_week", "1"))) {

            case DateTimeConstants.MONDAY:

                switch (getIntent().getIntExtra("day", 0)) {

                    case 0:
                        toolbar.setTitle("Edit Monday");
                        break;
                    case 1:
                        toolbar.setTitle("Edit Tuesday");
                        break;
                    case 2:
                        toolbar.setTitle("Edit Wednesday");
                        break;
                    case 3:
                        toolbar.setTitle("Edit Thursday");
                        break;
                    case 4:
                        toolbar.setTitle("Edit Friday");
                        break;
                    case 5:
                        toolbar.setTitle("Edit Saturday");
                        break;
                    case 6:
                        toolbar.setTitle("Edit Sunday");
                        break;

                }

                break;

            case DateTimeConstants.TUESDAY:

                switch (getIntent().getIntExtra("day", 0)) {

                    case 0:
                        toolbar.setTitle("Edit Tuesday");
                        break;
                    case 1:
                        toolbar.setTitle("Edit Wednesday");
                        break;
                    case 2:
                        toolbar.setTitle("Edit Thursday");
                        break;
                    case 3:
                        toolbar.setTitle("Edit Friday");
                        break;
                    case 4:
                        toolbar.setTitle("Edit Saturday");
                        break;
                    case 5:
                        toolbar.setTitle("Edit Sunday");
                        break;
                    case 6:
                        toolbar.setTitle("Edit Monday");
                        break;

                }

                break;

            case DateTimeConstants.WEDNESDAY:

                switch (getIntent().getIntExtra("day", 0)) {

                    case 0:
                        toolbar.setTitle("Edit Wednesday");
                        break;
                    case 1:
                        toolbar.setTitle("Edit Thursday");
                        break;
                    case 2:
                        toolbar.setTitle("Edit Friday");
                        break;
                    case 3:
                        toolbar.setTitle("Edit Saturday");
                        break;
                    case 4:
                        toolbar.setTitle("Edit Sunday");
                        break;
                    case 5:
                        toolbar.setTitle("Edit Monday");
                        break;
                    case 6:
                        toolbar.setTitle("Edit Tuesday");
                        break;

                }

                break;

            case DateTimeConstants.THURSDAY:

                switch (getIntent().getIntExtra("day", 0)) {

                    case 0:
                        toolbar.setTitle("Edit Sunday");
                        break;
                    case 1:
                        toolbar.setTitle("Edit Monday");
                        break;
                    case 2:
                        toolbar.setTitle("Edit Tuesday");
                        break;
                    case 3:
                        toolbar.setTitle("Edit Wednesday");
                        break;
                    case 4:
                        toolbar.setTitle("Edit Thursday");
                        break;
                    case 5:
                        toolbar.setTitle("Edit Friday");
                        break;
                    case 6:
                        toolbar.setTitle("Edit Saturday");
                        break;

                }

                break;

            case DateTimeConstants.FRIDAY:

                switch (getIntent().getIntExtra("day", 0)) {

                    case 0:
                        toolbar.setTitle("Edit Sunday");
                        break;
                    case 1:
                        toolbar.setTitle("Edit Monday");
                        break;
                    case 2:
                        toolbar.setTitle("Edit Tuesday");
                        break;
                    case 3:
                        toolbar.setTitle("Edit Wednesday");
                        break;
                    case 4:
                        toolbar.setTitle("Edit Thursday");
                        break;
                    case 5:
                        toolbar.setTitle("Edit Friday");
                        break;
                    case 6:
                        toolbar.setTitle("Edit Saturday");
                        break;

                }

                break;

            case DateTimeConstants.SATURDAY:

                switch (getIntent().getIntExtra("day", 0)) {

                    case 0:
                        toolbar.setTitle("Edit Sunday");
                        break;
                    case 1:
                        toolbar.setTitle("Edit Monday");
                        break;
                    case 2:
                        toolbar.setTitle("Edit Tuesday");
                        break;
                    case 3:
                        toolbar.setTitle("Edit Wednesday");
                        break;
                    case 4:
                        toolbar.setTitle("Edit Thursday");
                        break;
                    case 5:
                        toolbar.setTitle("Edit Friday");
                        break;
                    case 6:
                        toolbar.setTitle("Edit Saturday");
                        break;

                }

                break;

            case DateTimeConstants.SUNDAY:

                switch (getIntent().getIntExtra("day", 0)) {

                    case 0:
                        toolbar.setTitle("Edit Sunday");
                        break;
                    case 1:
                        toolbar.setTitle("Edit Monday");
                        break;
                    case 2:
                        toolbar.setTitle("Edit Tuesday");
                        break;
                    case 3:
                        toolbar.setTitle("Edit Wednesday");
                        break;
                    case 4:
                        toolbar.setTitle("Edit Thursday");
                        break;
                    case 5:
                        toolbar.setTitle("Edit Friday");
                        break;
                    case 6:
                        toolbar.setTitle("Edit Saturday");
                        break;

                }

                break;

        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setElevation(getPixelFromDP(4));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        days = new ArrayList<>();

        switch (toolbar.getTitle().toString()) {

            case "Edit Monday":

                day = DateTimeConstants.MONDAY;

                if (DataSingleton.getInstance().getTuesdayClasses() != null)
                    days.add("Tuesday's timetable");

                if (DataSingleton.getInstance().getWednesdayClasses() != null)
                    days.add("Wednesday's timetable");

                if (DataSingleton.getInstance().getThursdayClasses() != null)
                    days.add("Thursday's timetable");

                if (DataSingleton.getInstance().getFridayClasses() != null)
                    days.add("Friday's timetable");

                if (DataSingleton.getInstance().getSaturdayClasses() != null)
                    days.add("Saturday's timetable");

                if (DataSingleton.getInstance().getSundayClasses() != null)
                    days.add("Sunday's timetable");

                break;

            case "Edit Tuesday":

                day = DateTimeConstants.TUESDAY;

                if (DataSingleton.getInstance().getWednesdayClasses() != null)
                    days.add("Wednesday's timetable");

                if (DataSingleton.getInstance().getThursdayClasses() != null)
                    days.add("Thursday's timetable");

                if (DataSingleton.getInstance().getFridayClasses() != null)
                    days.add("Friday's timetable");

                if (DataSingleton.getInstance().getSaturdayClasses() != null)
                    days.add("Saturday's timetable");

                if (DataSingleton.getInstance().getSundayClasses() != null)
                    days.add("Sunday's timetable");

                if (DataSingleton.getInstance().getMondayClasses() != null)
                    days.add("Monday's timetable");

                break;

            case "Edit Wednesday":

                day = DateTimeConstants.WEDNESDAY;

                if (DataSingleton.getInstance().getThursdayClasses() != null)
                    days.add("Thursday's timetable");

                if (DataSingleton.getInstance().getFridayClasses() != null)
                    days.add("Friday's timetable");

                if (DataSingleton.getInstance().getSaturdayClasses() != null)
                    days.add("Saturday's timetable");

                if (DataSingleton.getInstance().getSundayClasses() != null)
                    days.add("Sunday's timetable");

                if (DataSingleton.getInstance().getMondayClasses() != null)
                    days.add("Monday's timetable");

                if (DataSingleton.getInstance().getTuesdayClasses() != null)
                    days.add("Tuesday's timetable");

                break;

            case "Edit Thursday":

                day = DateTimeConstants.THURSDAY;

                if (DataSingleton.getInstance().getFridayClasses() != null)
                    days.add("Friday's timetable");

                if (DataSingleton.getInstance().getSaturdayClasses() != null)
                    days.add("Saturday's timetable");

                if (DataSingleton.getInstance().getSundayClasses() != null)
                    days.add("Sunday's timetable");

                if (DataSingleton.getInstance().getMondayClasses() != null)
                    days.add("Monday's timetable");

                if (DataSingleton.getInstance().getTuesdayClasses() != null)
                    days.add("Tuesday's timetable");

                if (DataSingleton.getInstance().getWednesdayClasses() != null)
                    days.add("Wednesday's timetable");

                break;

            case "Edit Friday":

                day = DateTimeConstants.FRIDAY;

                if (DataSingleton.getInstance().getSaturdayClasses() != null)
                    days.add("Saturday's timetable");

                if (DataSingleton.getInstance().getSundayClasses() != null)
                    days.add("Sunday's timetable");

                if (DataSingleton.getInstance().getMondayClasses() != null)
                    days.add("Monday's timetable");

                if (DataSingleton.getInstance().getTuesdayClasses() != null)
                    days.add("Tuesday's timetable");

                if (DataSingleton.getInstance().getWednesdayClasses() != null)
                    days.add("Wednesday's timetable");

                if (DataSingleton.getInstance().getThursdayClasses() != null)
                    days.add("Thursday's timetable");

                break;

            case "Edit Saturday":

                day = DateTimeConstants.SATURDAY;

                if (DataSingleton.getInstance().getSundayClasses() != null)
                    days.add("Sunday's timetable");

                if (DataSingleton.getInstance().getMondayClasses() != null)
                    days.add("Monday's timetable");

                if (DataSingleton.getInstance().getTuesdayClasses() != null)
                    days.add("Tuesday's timetable");

                if (DataSingleton.getInstance().getWednesdayClasses() != null)
                    days.add("Wednesday's timetable");

                if (DataSingleton.getInstance().getThursdayClasses() != null)
                    days.add("Thursday's timetable");

                if (DataSingleton.getInstance().getFridayClasses() != null)
                    days.add("Friday's timetable");

                break;

            case "Edit Sunday":

                day = DateTimeConstants.SUNDAY;

                if (DataSingleton.getInstance().getMondayClasses() != null)
                    days.add("Monday's timetable");

                if (DataSingleton.getInstance().getTuesdayClasses() != null)
                    days.add("Tuesday's timetable");

                if (DataSingleton.getInstance().getWednesdayClasses() != null)
                    days.add("Wednesday's timetable");

                if (DataSingleton.getInstance().getThursdayClasses() != null)
                    days.add("Thursday's timetable");

                if (DataSingleton.getInstance().getFridayClasses() != null)
                    days.add("Friday's timetable");

                if (DataSingleton.getInstance().getSaturdayClasses() != null)
                    days.add("Saturday's timetable");

                break;

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        DataSingleton.getInstance().setReactToBroadcastData(false);

        if (listView == null)
            listView = findViewById(R.id.activity_edit_day_list_view);

        if (standardClassArrayList == null)
            standardClassArrayList = new ArrayList<>();

        if (noClassIndexList == null)
            noClassIndexList = new ArrayList<>();

        if (startTimeForPosition == null)
            startTimeForPosition = new ArrayList<>();

        if (endTimeForPosition == null)
            endTimeForPosition = new ArrayList<>();

        if (dateTimeFormatterAMPM == null)
            dateTimeFormatterAMPM = DateTimeFormat.forPattern("h:mm a");

        if (dateTimeFormatter24Hour == null)
            dateTimeFormatter24Hour = DateTimeFormat.forPattern("HH:mm");

        updateUI();

        invalidateOptionsMenu();

    }

    @Override
    public void onTrimMemory(int level) {

        if (level == TRIM_MEMORY_UI_HIDDEN) {

            listView = null;
            listAdapter = null;
            standardClassArrayList = null;

            startTimeForPosition = null;
            endTimeForPosition = null;
            noClassIndexList = null;

            dateTimeFormatterAMPM = null;
            dateTimeFormatter24Hour = null;

        }

        super.onTrimMemory(level);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_copy, menu);

        menu.findItem(R.id.toolbar_copy_copy_item).setVisible(!days.isEmpty());

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            DataSingleton.getInstance().setReactToBroadcastData(true);

            super.onBackPressed();

            return true;

        } else if (menuItem.getItemId() == R.id.toolbar_copy_copy_item) {

            final String[] daysArray = new String[days.size()];

            for (int i = 0; i < days.size(); i++)
                daysArray[i] = days.get(i);

            new MaterialDialog.Builder(EditDay.this)
                    .title("Copy from...")
                    .items(daysArray)
                    .itemsCallback(new MaterialDialog.ListCallback() {

                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                            ArrayList<StandardClass> currentClasses = new ArrayList<>();

                            switch (text.toString()) {

                                case "Monday's timetable":
                                    currentClasses = Utils.getClassesArrayListOfDay(DateTimeConstants.MONDAY) == null ? null : (ArrayList<StandardClass>) Utils.getClassesArrayListOfDay(DateTimeConstants.MONDAY).clone();
                                    break;
                                case "Tuesday's timetable":
                                    currentClasses = Utils.getClassesArrayListOfDay(DateTimeConstants.TUESDAY) == null ? null : (ArrayList<StandardClass>) Utils.getClassesArrayListOfDay(DateTimeConstants.TUESDAY).clone();
                                    break;
                                case "Wednesday's timetable":
                                    currentClasses = Utils.getClassesArrayListOfDay(DateTimeConstants.WEDNESDAY) == null ? null : (ArrayList<StandardClass>) Utils.getClassesArrayListOfDay(DateTimeConstants.WEDNESDAY).clone();
                                    break;
                                case "Thursday's timetable":
                                    currentClasses = Utils.getClassesArrayListOfDay(DateTimeConstants.THURSDAY) == null ? null : (ArrayList<StandardClass>) Utils.getClassesArrayListOfDay(DateTimeConstants.THURSDAY).clone();
                                    break;
                                case "Friday's timetable":
                                    currentClasses = Utils.getClassesArrayListOfDay(DateTimeConstants.FRIDAY) == null ? null : (ArrayList<StandardClass>) Utils.getClassesArrayListOfDay(DateTimeConstants.FRIDAY).clone();
                                    break;
                                case "Saturday's timetable":
                                    currentClasses = Utils.getClassesArrayListOfDay(DateTimeConstants.SATURDAY) == null ? null : (ArrayList<StandardClass>) Utils.getClassesArrayListOfDay(DateTimeConstants.SATURDAY).clone();
                                    break;
                                case "Sunday's timetable":
                                    currentClasses = Utils.getClassesArrayListOfDay(DateTimeConstants.SUNDAY) == null ? null : (ArrayList<StandardClass>) Utils.getClassesArrayListOfDay(DateTimeConstants.SUNDAY).clone();
                                    break;

                            }

                            DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);

                            try {

                                databaseHelper.insertClassesIntoDay(currentClasses, day);

                            } finally {

                                databaseHelper.close();

                            }

                            Utils.setClassesArrayListOfDay(day, currentClasses);

                            updateUI();

                        }

                    })
                    .show();

            return true;

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    @Override
    public void onBackPressed() {

        DataSingleton.getInstance().setReactToBroadcastData(true);

        super.onBackPressed();

    }

    private void updateUI() {

        noClassIndexList.clear();
        startTimeForPosition.clear();
        endTimeForPosition.clear();

        is24Hour = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("24_hour_time", true);
        final int defaultLessonLength = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("default_lesson_length", "60"));

        if (Utils.getClassesArrayListOfDay(day) != null) {

            standardClassArrayList.clear();
            standardClassArrayList.addAll(Utils.getClassesArrayListOfDay(day));

            for (int i = 0; i <= standardClassArrayList.size(); i++) {

                if (i == 0) {

                    if (!standardClassArrayList.get(0).getStartTime().isEqual(new LocalTime().withMillisOfDay(0))) {

                        standardClassArrayList.add(0, null);
                        noClassIndexList.add(0);

                        if (standardClassArrayList.get(1).getStartTime().getMillisOfDay() <= defaultLessonLength * 60 * 1000)
                            startTimeForPosition.add(new LocalTime().withMillisOfDay(0));
                        else
                            startTimeForPosition.add(standardClassArrayList.get(1).getStartTime().minusMinutes(defaultLessonLength));

                        endTimeForPosition.add(standardClassArrayList.get(1).getStartTime());

                    } else {

                        startTimeForPosition.add(new LocalTime().withMillisOfDay(0));
                        endTimeForPosition.add(standardClassArrayList.get(0).getEndTime());

                    }

                } else if (i == standardClassArrayList.size()) {

                    if (!standardClassArrayList.get(i - 1).getEndTime().isEqual(new LocalTime().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(0).withMillisOfSecond(0))) {

                        standardClassArrayList.add(null);
                        noClassIndexList.add(i);

                        startTimeForPosition.add(standardClassArrayList.get(i - 1).getEndTime());

                        if (standardClassArrayList.get(i - 1).getEndTime().getMillisOfDay() >= (86399000 - defaultLessonLength * 60 * 1000))
                            endTimeForPosition.add(new LocalTime().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(0).withMillisOfSecond(0));
                        else
                            endTimeForPosition.add(standardClassArrayList.get(i - 1).getEndTime().plusMinutes(defaultLessonLength));

                    }

                    i++;

                } else if (i > 1 && !standardClassArrayList.get(i).getStartTimeString(false).equals(standardClassArrayList.get(i - 1).getEndTimeString(false))) {

                    startTimeForPosition.add(i, standardClassArrayList.get(i).getStartTime());
                    endTimeForPosition.add(i, standardClassArrayList.get(i).getEndTime());

                    standardClassArrayList.add(i, null);
                    noClassIndexList.add(i);

                    startTimeForPosition.add(i, standardClassArrayList.get(i - 1).getEndTime());
                    endTimeForPosition.add(i, standardClassArrayList.get(i + 1).getStartTime());

                    i++;

                } else {

                    startTimeForPosition.add(i, standardClassArrayList.get(i).getStartTime());
                    endTimeForPosition.add(i, standardClassArrayList.get(i).getEndTime());

                }

            }

        } else {

            standardClassArrayList = new ArrayList<>();
            standardClassArrayList.add(null);

            noClassIndexList.add(0);

            startTimeForPosition.add(DateTimeFormat.forPattern("HH:mm").parseLocalTime(PreferenceManager.getDefaultSharedPreferences(this).getString("first_lesson_time", "08:00")));
            endTimeForPosition.add(startTimeForPosition.get(0).plusMinutes(defaultLessonLength));

        }

        if (listAdapter == null) {

            listAdapter = new EditClassList(this, standardClassArrayList);
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    if (noClassIndexList.contains(position)) {

                        final LocalTime startTimeRestriction;
                        final LocalTime endTimeRestriction;

                        if (standardClassArrayList.size() == 1) {

                            startTimeRestriction = null;
                            endTimeRestriction = null;

                        } else if (position == 0) {

                            startTimeRestriction = null;
                            endTimeRestriction = endTimeForPosition.get(position);

                        } else if ((position + 1) == standardClassArrayList.size()) {

                            startTimeRestriction = startTimeForPosition.get(position);
                            endTimeRestriction = null;

                        } else {

                            startTimeRestriction = startTimeForPosition.get(position);
                            endTimeRestriction = endTimeForPosition.get(position);

                        }

                        final MaterialDialog.Builder materialDialogBuilderAddClass = new MaterialDialog.Builder(EditDay.this);

                        materialDialogBuilderAddClass.title("Add Class");
                        materialDialogBuilderAddClass.customView(R.layout.dialog_edit_day_add_class, false);
                        materialDialogBuilderAddClass.positiveText("Done");
                        materialDialogBuilderAddClass.positiveColorRes(R.color.black);
                        materialDialogBuilderAddClass.negativeText("Edit Times");
                        materialDialogBuilderAddClass.negativeColorRes(R.color.black);
                        materialDialogBuilderAddClass.autoDismiss(false);

                        final MaterialDialog materialDialogAddClass = materialDialogBuilderAddClass.show();

                        final EditText dialogAddClassCustomLocationEditText = materialDialogAddClass.getCustomView().findViewById(R.id.dialog_edit_day_add_class_location_edit_text);
                        final EditText dialogAddClassCustomTeacherEditText = materialDialogAddClass.getCustomView().findViewById(R.id.dialog_edit_day_add_class_teacher_edit_text);

                        final Spinner classNameSpinner = materialDialogAddClass.getCustomView().findViewById(R.id.dialog_edit_day_add_class_spinner);
                        final ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, R.layout.view_spinner_item, DataSingleton.getInstance().getAllClassNamesArrayList());

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classNameSpinner.setAdapter(classNameSpinnerAdapter);

                        classNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);

                                try {

                                    String[] locationTeacherColor = databaseHelper.getClassLocationTeacherColor(classNameSpinner.getSelectedItem().toString());

                                    dialogAddClassCustomLocationEditText.setText(locationTeacherColor[0].equals("no-location") ? "" : locationTeacherColor[0]);
                                    dialogAddClassCustomTeacherEditText.setText(locationTeacherColor[1].equals("no-teacher") ? "" : locationTeacherColor[1]);

                                } finally {

                                    databaseHelper.close();

                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }

                        });

                        final TextView dialogAddClassStartTimeTextView = materialDialogAddClass.getCustomView().findViewById(R.id.dialog_edit_day_add_class_start_time_text_view);
                        final TextView dialogAddClassEndTimeTextView = materialDialogAddClass.getCustomView().findViewById(R.id.dialog_edit_day_add_class_end_time_text_view);

                        dialogAddClassStartTimeTextView.setText(is24Hour ? dateTimeFormatter24Hour.print(startTimeForPosition.get(position)) : dateTimeFormatterAMPM.print(startTimeForPosition.get(position)));
                        dialogAddClassEndTimeTextView.setText(is24Hour ? dateTimeFormatter24Hour.print(endTimeForPosition.get(position)) : dateTimeFormatterAMPM.print(endTimeForPosition.get(position)));

                        materialDialogBuilderAddClass.onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                String className = classNameSpinner.getSelectedItem().toString();

                                final LocalTime classStartTime = is24Hour ? dateTimeFormatter24Hour.parseLocalTime(dialogAddClassStartTimeTextView.getText().toString()) : dateTimeFormatterAMPM.parseLocalTime(dialogAddClassStartTimeTextView.getText().toString());
                                final LocalTime classEndTime = is24Hour ? dateTimeFormatter24Hour.parseLocalTime(dialogAddClassEndTimeTextView.getText().toString()) : dateTimeFormatterAMPM.parseLocalTime(dialogAddClassEndTimeTextView.getText().toString());

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                ArrayList<StandardClass> currentClasses;

                                try {

                                    final String[] locationTeacherColor = databaseHelper.getClassLocationTeacherColor(className);

                                    currentClasses = (ArrayList<StandardClass>) standardClassArrayList.clone();

                                    currentClasses.add(position, new StandardClass(className,
                                            classStartTime,
                                            classEndTime,
                                            dateTimeFormatterAMPM.print(classStartTime),
                                            dateTimeFormatterAMPM.print(classEndTime),
                                            dialogAddClassCustomLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : dialogAddClassCustomLocationEditText.getText().toString().trim(),
                                            dialogAddClassCustomTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : dialogAddClassCustomTeacherEditText.getText().toString().trim(),
                                            Integer.parseInt(locationTeacherColor[2])));

                                    for (int i = 0; i < currentClasses.size(); i++) {

                                        if (currentClasses.get(i) == null) {

                                            currentClasses.remove(i);
                                            i--;

                                        }

                                    }

                                    standardClassArrayList.add(position, new StandardClass(className,
                                            classStartTime,
                                            classEndTime,
                                            dateTimeFormatterAMPM.print(classStartTime),
                                            dateTimeFormatterAMPM.print(classEndTime),
                                            dialogAddClassCustomLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : dialogAddClassCustomLocationEditText.getText().toString().trim(),
                                            dialogAddClassCustomTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : dialogAddClassCustomTeacherEditText.getText().toString().trim(),
                                            Integer.parseInt(locationTeacherColor[2])));

                                    databaseHelper.insertClassesIntoDay(currentClasses, day);

                                } finally {

                                    databaseHelper.close();

                                }

                                if (currentClasses.size() != 0)
                                    Utils.setClassesArrayListOfDay(day, currentClasses);
                                else
                                    Utils.setClassesArrayListOfDay(day, null);

                                materialDialog.dismiss();

                                updateUI();

                            }

                        });

                        materialDialogBuilderAddClass.onNegative(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                startTimeTextAdd = "";
                                endTimeTextAdd = "";

                                final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

                                        final LocalTime selectedStartTime = new LocalTime().withHourOfDay(hourOfDay).withMinuteOfHour(minute).withSecondOfMinute(0).withMillisOfSecond(0);
                                        final LocalTime selectedEndTime = new LocalTime().withHourOfDay(hourOfDayEnd).withMinuteOfHour(minuteEnd).withSecondOfMinute(0).withMillisOfSecond(0);

                                        if (selectedStartTime.isEqual(selectedEndTime)) {

                                            Toast.makeText(EditDay.this, "Your start time equals your end time!", Toast.LENGTH_LONG).show();

                                        } else if (selectedEndTime.isBefore(selectedStartTime)) {

                                            Toast.makeText(EditDay.this, "Your end time is before your start time!", Toast.LENGTH_LONG).show();

                                        } else {

                                            if (startTimeRestriction != null) {

                                                if (selectedStartTime.isBefore(startTimeRestriction)) {

                                                    Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                } else {

                                                    startTimeTextAdd = (is24Hour ? dateTimeFormatter24Hour.print(selectedStartTime) : dateTimeFormatterAMPM.print(selectedStartTime));

                                                }

                                            } else {

                                                startTimeTextAdd = (is24Hour ? dateTimeFormatter24Hour.print(selectedStartTime) : dateTimeFormatterAMPM.print(selectedStartTime));

                                            }

                                        }

                                        if (!startTimeTextAdd.isEmpty()) {

                                            if (endTimeRestriction != null) {

                                                if (selectedEndTime.isAfter(endTimeRestriction)) {

                                                    Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                } else {

                                                    endTimeTextAdd = (is24Hour ? dateTimeFormatter24Hour.print(selectedEndTime) : dateTimeFormatterAMPM.print(selectedEndTime));

                                                }

                                            } else {

                                                endTimeTextAdd = (is24Hour ? dateTimeFormatter24Hour.print(selectedEndTime) : dateTimeFormatterAMPM.print(selectedEndTime));

                                            }

                                            if (!endTimeTextAdd.isEmpty()) {

                                                dialogAddClassStartTimeTextView.setText(startTimeTextAdd);
                                                dialogAddClassEndTimeTextView.setText(endTimeTextAdd);

                                            }

                                        }


                                    }

                                };

                                final LocalTime startTime = (is24Hour ? dateTimeFormatter24Hour.parseLocalTime(dialogAddClassStartTimeTextView.getText().toString()) : dateTimeFormatterAMPM.parseLocalTime(dialogAddClassStartTimeTextView.getText().toString()));

                                final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                        onTimeSetListener,
                                        startTime.getHourOfDay(),
                                        startTime.getMinuteOfHour(),
                                        is24Hour

                                );

                                timePickerDialog.show(getFragmentManager(), "time_picker_dialog");

                            }

                        });

                    } else {

                        final LocalTime startTimeRestriction;
                        final LocalTime endTimeRestriction;

                        if (standardClassArrayList.size() == 1) {

                            startTimeRestriction = null;
                            endTimeRestriction = null;


                        } else if (position == 0) {

                            startTimeRestriction = null;

                            if (noClassIndexList.contains(position + 1)) {

                                if ((position + 2) == standardClassArrayList.size()) {

                                    endTimeRestriction = null;

                                } else {

                                    endTimeRestriction = endTimeForPosition.get(position + 1);

                                }

                            } else {

                                endTimeRestriction = endTimeForPosition.get(position);

                            }

                        } else if (position == 1 && noClassIndexList.contains(0)) {

                            startTimeRestriction = null;

                            if (noClassIndexList.contains(position + 1)) {

                                if (position + 2 == standardClassArrayList.size()) {

                                    endTimeRestriction = null;

                                } else {

                                    endTimeRestriction = endTimeForPosition.get(position + 1);

                                }

                            } else {

                                endTimeRestriction = endTimeForPosition.get(position);

                            }

                        } else if ((position + 1) == standardClassArrayList.size()) {

                            if (noClassIndexList.contains(position - 1)) {

                                if ((position - 1) == 0) {

                                    startTimeRestriction = null;

                                } else {

                                    startTimeRestriction = startTimeForPosition.get(position - 1);

                                }

                            } else {

                                startTimeRestriction = startTimeForPosition.get(position);

                            }

                            endTimeRestriction = null;

                        } else if ((position + 2) == standardClassArrayList.size() && noClassIndexList.contains(position + 1)) {

                            if (noClassIndexList.contains(position - 1)) {

                                if ((position - 1) == 0) {

                                    startTimeRestriction = null;

                                } else {

                                    startTimeRestriction = startTimeForPosition.get(position - 1);

                                }

                            } else {

                                startTimeRestriction = startTimeForPosition.get(position);

                            }

                            endTimeRestriction = null;

                        } else {

                            if (noClassIndexList.contains(position - 1))
                                startTimeRestriction = startTimeForPosition.get(position - 1);
                            else
                                startTimeRestriction = startTimeForPosition.get(position);

                            if (noClassIndexList.contains(position + 1))
                                endTimeRestriction = endTimeForPosition.get(position + 1);
                            else
                                endTimeRestriction = endTimeForPosition.get(position);

                        }

                        final LocalTime selectedClassStartTime = standardClassArrayList.get(position).getStartTime();
                        final String selectedClassName = standardClassArrayList.get(position).getName();

                        ArrayList<String> classNameList = DataSingleton.getInstance().getAllClassNamesArrayList();
                        classNameList.remove(selectedClassName);
                        classNameList.add(0, selectedClassName);

                        final MaterialDialog.Builder materialDialogBuilderEditClass = new MaterialDialog.Builder(EditDay.this);

                        materialDialogBuilderEditClass.title("Edit Class");
                        materialDialogBuilderEditClass.customView(R.layout.dialog_edit_day_edit_class, false);
                        materialDialogBuilderEditClass.positiveText("Done");
                        materialDialogBuilderEditClass.positiveColorRes(R.color.black);
                        materialDialogBuilderEditClass.negativeText("Edit Times");
                        materialDialogBuilderEditClass.negativeColorRes(R.color.black);
                        materialDialogBuilderEditClass.neutralText("Delete");
                        materialDialogBuilderEditClass.neutralColorRes(R.color.black);
                        materialDialogBuilderEditClass.autoDismiss(false);

                        final MaterialDialog materialDialogEditClass = materialDialogBuilderEditClass.show();

                        final TextView dialogEditClassStartTimeTextView = materialDialogEditClass.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_start_time_text_view);
                        final TextView dialogEditClassEndTimeTextView = materialDialogEditClass.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_end_time_text_view);

                        dialogEditClassStartTimeTextView.setText(is24Hour ? dateTimeFormatter24Hour.print(startTimeForPosition.get(position)) : dateTimeFormatterAMPM.print(startTimeForPosition.get(position)));
                        dialogEditClassEndTimeTextView.setText(is24Hour ? dateTimeFormatter24Hour.print(endTimeForPosition.get(position)) : dateTimeFormatterAMPM.print(endTimeForPosition.get(position)));

                        final EditText dialogEditClassCustomLocationEditText = materialDialogEditClass.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_location_edit_text);
                        final EditText dialogEditClassCustomTeacherEditText = materialDialogEditClass.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_teacher_edit_text);

                        final Spinner classNameSpinner = materialDialogEditClass.getCustomView().findViewById(R.id.dialog_edit_day_edit_class_spinner);
                        final ArrayAdapter<String> classNameSpinnerAdapter = new ArrayAdapter<>(EditDay.this, R.layout.view_spinner_item, classNameList);

                        classNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classNameSpinner.setAdapter(classNameSpinnerAdapter);

                        if (!skip)
                            skip = true;

                        classNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                if (!skip) {

                                    DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);

                                    try {

                                        String[] locationTeacherColor = databaseHelper.getClassLocationTeacherColor(classNameSpinner.getSelectedItem().toString());

                                        dialogEditClassCustomLocationEditText.setText(locationTeacherColor[0].equals("no-location") ? "" : locationTeacherColor[0]);
                                        dialogEditClassCustomTeacherEditText.setText(locationTeacherColor[1].equals("no-teacher") ? "" : locationTeacherColor[1]);

                                    } finally {

                                        databaseHelper.close();

                                    }

                                } else {

                                    dialogEditClassCustomLocationEditText.setText(standardClassArrayList.get(position).hasLocation() ? standardClassArrayList.get(position).getLocation() : "");
                                    dialogEditClassCustomTeacherEditText.setText(standardClassArrayList.get(position).hasTeacher() ? standardClassArrayList.get(position).getTeacher() : "");

                                    skip = false;

                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }

                        });

                        materialDialogBuilderEditClass.onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                String className = classNameSpinner.getSelectedItem().toString();

                                final LocalTime classStartTime = is24Hour ? dateTimeFormatter24Hour.parseLocalTime(dialogEditClassStartTimeTextView.getText().toString()) : dateTimeFormatterAMPM.parseLocalTime(dialogEditClassStartTimeTextView.getText().toString());
                                final LocalTime classEndTime = is24Hour ? dateTimeFormatter24Hour.parseLocalTime(dialogEditClassEndTimeTextView.getText().toString()) : dateTimeFormatterAMPM.parseLocalTime(dialogEditClassEndTimeTextView.getText().toString());

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);
                                ArrayList<StandardClass> currentClasses;

                                try {

                                    final String[] locationTeacherColor = databaseHelper.getClassLocationTeacherColor(className);

                                    currentClasses = (ArrayList<StandardClass>) standardClassArrayList.clone();

                                    currentClasses.remove(position);
                                    currentClasses.add(position, new StandardClass(className,
                                            classStartTime,
                                            classEndTime,
                                            dateTimeFormatterAMPM.print(classStartTime),
                                            dateTimeFormatterAMPM.print(classEndTime),
                                            dialogEditClassCustomLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : dialogEditClassCustomLocationEditText.getText().toString().trim(),
                                            dialogEditClassCustomTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : dialogEditClassCustomTeacherEditText.getText().toString().trim(),
                                            Integer.parseInt(locationTeacherColor[2])));

                                    for (int i = 0; i < currentClasses.size(); i++) {

                                        if (currentClasses.get(i) == null) {

                                            currentClasses.remove(i);
                                            i--;

                                        }

                                    }

                                    standardClassArrayList.remove(position);
                                    standardClassArrayList.add(position, new StandardClass(className,
                                            classStartTime,
                                            classEndTime,
                                            dateTimeFormatterAMPM.print(classStartTime),
                                            dateTimeFormatterAMPM.print(classEndTime),
                                            dialogEditClassCustomLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : dialogEditClassCustomLocationEditText.getText().toString().trim(),
                                            dialogEditClassCustomTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : dialogEditClassCustomTeacherEditText.getText().toString().trim(),
                                            Integer.parseInt(locationTeacherColor[2])));

                                    databaseHelper.insertClassesIntoDay(currentClasses, day);

                                } finally {

                                    databaseHelper.close();

                                }

                                if (currentClasses.size() != 0)
                                    Utils.setClassesArrayListOfDay(day, currentClasses);
                                else
                                    Utils.setClassesArrayListOfDay(day, null);

                                materialDialog.dismiss();

                                updateUI();

                            }

                        });

                        materialDialogBuilderEditClass.onNegative(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                startTimeTextEdit = "";
                                endTimeTextEdit = "";

                                final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

                                        final LocalTime selectedStartTime = new LocalTime().withHourOfDay(hourOfDay).withMinuteOfHour(minute).withSecondOfMinute(0).withMillisOfSecond(0);
                                        final LocalTime selectedEndTime = new LocalTime().withHourOfDay(hourOfDayEnd).withMinuteOfHour(minuteEnd).withSecondOfMinute(0).withMillisOfSecond(0);

                                        if (selectedStartTime.isEqual(selectedEndTime)) {

                                            Toast.makeText(EditDay.this, "Your start time equals your end time!", Toast.LENGTH_LONG).show();

                                        } else if (selectedEndTime.isBefore(selectedStartTime)) {

                                            Toast.makeText(EditDay.this, "Your end time is before your start time!", Toast.LENGTH_LONG).show();

                                        } else {

                                            if (startTimeRestriction != null) {

                                                if (selectedStartTime.isBefore(startTimeRestriction)) {

                                                    Toast.makeText(EditDay.this, "Choose a start time after the previous class!", Toast.LENGTH_LONG).show();

                                                } else {

                                                    startTimeTextEdit = (is24Hour ? dateTimeFormatter24Hour.print(selectedStartTime) : dateTimeFormatterAMPM.print(selectedStartTime));

                                                }

                                            } else {

                                                startTimeTextEdit = (is24Hour ? dateTimeFormatter24Hour.print(selectedStartTime) : dateTimeFormatterAMPM.print(selectedStartTime));

                                            }

                                        }

                                        if (!startTimeTextEdit.isEmpty()) {

                                            if (endTimeRestriction != null) {

                                                if (selectedEndTime.isAfter(endTimeRestriction)) {

                                                    Toast.makeText(EditDay.this, "Choose an end time before the next class!", Toast.LENGTH_LONG).show();

                                                } else {

                                                    endTimeTextEdit = (is24Hour ? dateTimeFormatter24Hour.print(selectedEndTime) : dateTimeFormatterAMPM.print(selectedEndTime));

                                                }

                                            } else {

                                                endTimeTextEdit = (is24Hour ? dateTimeFormatter24Hour.print(selectedEndTime) : dateTimeFormatterAMPM.print(selectedEndTime));

                                            }

                                            if (!endTimeTextEdit.isEmpty()) {

                                                dialogEditClassStartTimeTextView.setText(startTimeTextEdit);
                                                dialogEditClassEndTimeTextView.setText(endTimeTextEdit);

                                            }

                                        }

                                    }

                                };

                                final LocalTime startTime = (is24Hour ? dateTimeFormatter24Hour.parseLocalTime(dialogEditClassStartTimeTextView.getText().toString()) : dateTimeFormatterAMPM.parseLocalTime(dialogEditClassStartTimeTextView.getText().toString()));

                                final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(

                                        onTimeSetListener,
                                        startTime.getHourOfDay(),
                                        startTime.getMinuteOfHour(),
                                        is24Hour

                                );

                                timePickerDialog.show(getFragmentManager(), "time_picker_dialog");

                            }

                        });

                        materialDialogBuilderEditClass.onNeutral(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                                DatabaseHelper databaseHelper = new DatabaseHelper(EditDay.this);

                                try {

                                    databaseHelper.removeClassOutOfDay(day, selectedClassName, selectedClassStartTime);

                                } finally {

                                    databaseHelper.close();

                                }

                                ArrayList<StandardClass> currentClasses = (ArrayList<StandardClass>) standardClassArrayList.clone();

                                currentClasses.remove(position);

                                for (int i = 0; i < currentClasses.size(); i++) {

                                    if (currentClasses.get(i) == null) {

                                        currentClasses.remove(i);
                                        i--;

                                    }

                                }

                                if (!currentClasses.isEmpty())
                                    Utils.setClassesArrayListOfDay(day, currentClasses);
                                else
                                    Utils.setClassesArrayListOfDay(day, null);

                                materialDialog.dismiss();

                                updateUI();

                            }

                        });

                    }

                }

            });

        } else {

            listAdapter.setClassesArrayList((ArrayList<StandardClass>) standardClassArrayList.clone());

        }

        setListViewHeightBasedOnChildren();

    }

    @Override
    protected void onStop() {

        EventBus.getDefault().post(new Update(true, false, false, false, false));

        super.onStop();

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

    private void setListViewHeightBasedOnChildren() {

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            final View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = listView.getLayoutParams();
        listViewLayoutParams.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(listViewLayoutParams);

    }

}