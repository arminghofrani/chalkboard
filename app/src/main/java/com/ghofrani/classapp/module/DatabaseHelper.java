package com.ghofrani.classapp.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.DatabaseContract;
import com.ghofrani.classapp.model.Event;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_MONDAY = "create table "
            + DatabaseContract.Monday.TABLE_NAME + " ("
            + DatabaseContract.Monday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.Monday.COLUMN_CLASS + " TEXT, "
            + DatabaseContract.Monday.COLUMN_START_TIME + " TEXT, "
            + DatabaseContract.Monday.COLUMN_END_TIME + " TEXT, "
            + DatabaseContract.Monday.COLUMN_CUSTOM_LOCATION + " TEXT, "
            + DatabaseContract.Monday.COLUMN_CUSTOM_TEACHER + " TEXT)";

    private static final String CREATE_TUESDAY = "create table "
            + DatabaseContract.Tuesday.TABLE_NAME + " ("
            + DatabaseContract.Tuesday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.Tuesday.COLUMN_CLASS + " TEXT, "
            + DatabaseContract.Tuesday.COLUMN_START_TIME + " TEXT, "
            + DatabaseContract.Tuesday.COLUMN_END_TIME + " TEXT, "
            + DatabaseContract.Tuesday.COLUMN_CUSTOM_LOCATION + " TEXT, "
            + DatabaseContract.Tuesday.COLUMN_CUSTOM_TEACHER + " TEXT)";

    private static final String CREATE_WEDNESDAY = "create table "
            + DatabaseContract.Wednesday.TABLE_NAME + " ("
            + DatabaseContract.Wednesday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.Wednesday.COLUMN_CLASS + " TEXT, "
            + DatabaseContract.Wednesday.COLUMN_START_TIME + " TEXT, "
            + DatabaseContract.Wednesday.COLUMN_END_TIME + " TEXT, "
            + DatabaseContract.Wednesday.COLUMN_CUSTOM_LOCATION + " TEXT, "
            + DatabaseContract.Wednesday.COLUMN_CUSTOM_TEACHER + " TEXT)";

    private static final String CREATE_THURSDAY = "create table "
            + DatabaseContract.Thursday.TABLE_NAME + " ("
            + DatabaseContract.Thursday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.Thursday.COLUMN_CLASS + " TEXT, "
            + DatabaseContract.Thursday.COLUMN_START_TIME + " TEXT, "
            + DatabaseContract.Thursday.COLUMN_END_TIME + " TEXT, "
            + DatabaseContract.Thursday.COLUMN_CUSTOM_LOCATION + " TEXT, "
            + DatabaseContract.Thursday.COLUMN_CUSTOM_TEACHER + " TEXT)";

    private static final String CREATE_FRIDAY = "create table "
            + DatabaseContract.Friday.TABLE_NAME + " ("
            + DatabaseContract.Friday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.Friday.COLUMN_CLASS + " TEXT, "
            + DatabaseContract.Friday.COLUMN_START_TIME + " TEXT, "
            + DatabaseContract.Friday.COLUMN_END_TIME + " TEXT, "
            + DatabaseContract.Friday.COLUMN_CUSTOM_LOCATION + " TEXT, "
            + DatabaseContract.Friday.COLUMN_CUSTOM_TEACHER + " TEXT)";

    private static final String CREATE_SATURDAY = "create table "
            + DatabaseContract.Saturday.TABLE_NAME + " ("
            + DatabaseContract.Saturday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.Saturday.COLUMN_CLASS + " TEXT, "
            + DatabaseContract.Saturday.COLUMN_START_TIME + " TEXT, "
            + DatabaseContract.Saturday.COLUMN_END_TIME + " TEXT, "
            + DatabaseContract.Saturday.COLUMN_CUSTOM_LOCATION + " TEXT, "
            + DatabaseContract.Saturday.COLUMN_CUSTOM_TEACHER + " TEXT)";

    private static final String CREATE_SUNDAY = "create table "
            + DatabaseContract.Sunday.TABLE_NAME + " ("
            + DatabaseContract.Sunday.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.Sunday.COLUMN_CLASS + " TEXT, "
            + DatabaseContract.Sunday.COLUMN_START_TIME + " TEXT, "
            + DatabaseContract.Sunday.COLUMN_END_TIME + " TEXT, "
            + DatabaseContract.Sunday.COLUMN_CUSTOM_LOCATION + " TEXT, "
            + DatabaseContract.Sunday.COLUMN_CUSTOM_TEACHER + " TEXT)";

    private static final String CREATE_CLASSINFO = "create table "
            + DatabaseContract.ClassInfo.TABLE_NAME + " ("
            + DatabaseContract.ClassInfo.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.ClassInfo.COLUMN_NAME + " TEXT, "
            + DatabaseContract.ClassInfo.COLUMN_LOCATION + " TEXT, "
            + DatabaseContract.ClassInfo.COLUMN_TEACHER + " TEXT, "
            + DatabaseContract.ClassInfo.COLUMN_COLOR + " INTEGER)";

    private static final String CREATE_EVENTS = "create table "
            + DatabaseContract.Events.TABLE_NAME + " ("
            + DatabaseContract.Events.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.Events.COLUMN_NAME + " TEXT, "
            + DatabaseContract.Events.COLUMN_DESCRIPTION + " TEXT, "
            + DatabaseContract.Events.COLUMN_TYPE + " TEXT, "
            + DatabaseContract.Events.COLUMN_CLASS + " TEXT, "
            + DatabaseContract.Events.COLUMN_DATE_TIME + " TEXT, "
            + DatabaseContract.Events.COLUMN_ATTACH + " BOOLEAN, "
            + DatabaseContract.Events.COLUMN_REMIND + " BOOLEAN)";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_SUNDAY);
        db.execSQL(CREATE_MONDAY);
        db.execSQL(CREATE_TUESDAY);
        db.execSQL(CREATE_WEDNESDAY);
        db.execSQL(CREATE_THURSDAY);
        db.execSQL(CREATE_FRIDAY);
        db.execSQL(CREATE_SATURDAY);
        db.execSQL(CREATE_CLASSINFO);
        db.execSQL(CREATE_EVENTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertClassesIntoDay(ArrayList<StandardClass> classesToInsert, int day) {

        ContentValues contentValues = new ContentValues();

        final String columnClass;
        final String columnStartTime;
        final String columnEndTime;
        final String columnCustomLocation;
        final String columnCustomTeacher;
        final String tableName;

        switch (day) {

            case DateTimeConstants.MONDAY:

                columnClass = DatabaseContract.Monday.COLUMN_CLASS;
                columnStartTime = DatabaseContract.Monday.COLUMN_START_TIME;
                columnEndTime = DatabaseContract.Monday.COLUMN_END_TIME;
                columnCustomLocation = DatabaseContract.Monday.COLUMN_CUSTOM_LOCATION;
                columnCustomTeacher = DatabaseContract.Monday.COLUMN_CUSTOM_TEACHER;
                tableName = DatabaseContract.Monday.TABLE_NAME;

                break;

            case DateTimeConstants.TUESDAY:

                columnClass = DatabaseContract.Tuesday.COLUMN_CLASS;
                columnStartTime = DatabaseContract.Tuesday.COLUMN_START_TIME;
                columnEndTime = DatabaseContract.Tuesday.COLUMN_END_TIME;
                columnCustomLocation = DatabaseContract.Tuesday.COLUMN_CUSTOM_LOCATION;
                columnCustomTeacher = DatabaseContract.Tuesday.COLUMN_CUSTOM_TEACHER;
                tableName = DatabaseContract.Tuesday.TABLE_NAME;

                break;

            case DateTimeConstants.WEDNESDAY:

                columnClass = DatabaseContract.Wednesday.COLUMN_CLASS;
                columnStartTime = DatabaseContract.Wednesday.COLUMN_START_TIME;
                columnEndTime = DatabaseContract.Wednesday.COLUMN_END_TIME;
                columnCustomLocation = DatabaseContract.Wednesday.COLUMN_CUSTOM_LOCATION;
                columnCustomTeacher = DatabaseContract.Wednesday.COLUMN_CUSTOM_TEACHER;
                tableName = DatabaseContract.Wednesday.TABLE_NAME;

                break;

            case DateTimeConstants.THURSDAY:

                columnClass = DatabaseContract.Thursday.COLUMN_CLASS;
                columnStartTime = DatabaseContract.Thursday.COLUMN_START_TIME;
                columnEndTime = DatabaseContract.Thursday.COLUMN_END_TIME;
                columnCustomLocation = DatabaseContract.Thursday.COLUMN_CUSTOM_LOCATION;
                columnCustomTeacher = DatabaseContract.Thursday.COLUMN_CUSTOM_TEACHER;
                tableName = DatabaseContract.Thursday.TABLE_NAME;

                break;

            case DateTimeConstants.FRIDAY:

                columnClass = DatabaseContract.Friday.COLUMN_CLASS;
                columnStartTime = DatabaseContract.Friday.COLUMN_START_TIME;
                columnEndTime = DatabaseContract.Friday.COLUMN_END_TIME;
                columnCustomLocation = DatabaseContract.Friday.COLUMN_CUSTOM_LOCATION;
                columnCustomTeacher = DatabaseContract.Friday.COLUMN_CUSTOM_TEACHER;
                tableName = DatabaseContract.Friday.TABLE_NAME;

                break;

            case DateTimeConstants.SATURDAY:

                columnClass = DatabaseContract.Saturday.COLUMN_CLASS;
                columnStartTime = DatabaseContract.Saturday.COLUMN_START_TIME;
                columnEndTime = DatabaseContract.Saturday.COLUMN_END_TIME;
                columnCustomLocation = DatabaseContract.Saturday.COLUMN_CUSTOM_LOCATION;
                columnCustomTeacher = DatabaseContract.Saturday.COLUMN_CUSTOM_TEACHER;
                tableName = DatabaseContract.Saturday.TABLE_NAME;

                break;

            case DateTimeConstants.SUNDAY:

                columnClass = DatabaseContract.Sunday.COLUMN_CLASS;
                columnStartTime = DatabaseContract.Sunday.COLUMN_START_TIME;
                columnEndTime = DatabaseContract.Sunday.COLUMN_END_TIME;
                columnCustomLocation = DatabaseContract.Sunday.COLUMN_CUSTOM_LOCATION;
                columnCustomTeacher = DatabaseContract.Sunday.COLUMN_CUSTOM_TEACHER;
                tableName = DatabaseContract.Sunday.TABLE_NAME;

                break;

            default:

                columnClass = "";
                columnStartTime = "";
                columnEndTime = "";
                columnCustomLocation = "";
                columnCustomTeacher = "";
                tableName = "";

        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {

            ArrayList<String[]> classesLocationTeacher = new ArrayList<>();

            Cursor cursor = null;

            for (int i = 0; i < classesToInsert.size(); i++) {

                cursor = sqLiteDatabase.rawQuery("select * from "
                                + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                                + DatabaseContract.ClassInfo.COLUMN_NAME + "=?",
                        new String[]{classesToInsert.get(i).getName()});

                cursor.moveToFirst();

                final String[] locationTeacherColor = new String[3];

                locationTeacherColor[0] = cursor.getString(2);
                locationTeacherColor[1] = cursor.getString(3);

                classesLocationTeacher.add(locationTeacherColor);

            }

            cursor.close();

            sqLiteDatabase.execSQL("delete from " + tableName);

            int i = 0;

            for (final StandardClass standardClass : classesToInsert) {

                contentValues.clear();

                contentValues.put(columnClass, standardClass.getName());
                contentValues.put(columnStartTime, standardClass.getStartTime().toString());
                contentValues.put(columnEndTime, standardClass.getEndTime().toString());

                final String[] classLocationTeacher = classesLocationTeacher.get(i);

                contentValues.put(columnCustomLocation, standardClass.hasLocation() ? (standardClass.getLocation().equals(classLocationTeacher[0]) ? "default" : standardClass.getLocation()) : "no-location");
                contentValues.put(columnCustomTeacher, standardClass.hasTeacher() ? (standardClass.getTeacher().equals(classLocationTeacher[1]) ? "default" : standardClass.getTeacher()) : "no-teacher");

                sqLiteDatabase.insert(tableName, null, contentValues);

                i++;

            }


        } finally {

            sqLiteDatabase.close();

        }

    }

    public void removeClassOutOfDay(int day, String classToRemove, LocalTime startTime) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {

            switch (day) {

                case DateTimeConstants.MONDAY:

                    sqLiteDatabase.execSQL("delete from "
                                    + DatabaseContract.Monday.TABLE_NAME + " where "
                                    + DatabaseContract.Monday.COLUMN_CLASS + "=? and "
                                    + DatabaseContract.Monday.COLUMN_START_TIME + "=?",
                            new String[]{classToRemove, startTime.toString()});

                    break;

                case DateTimeConstants.TUESDAY:

                    sqLiteDatabase.execSQL("delete from "
                                    + DatabaseContract.Tuesday.TABLE_NAME + " where "
                                    + DatabaseContract.Tuesday.COLUMN_CLASS + "=? and "
                                    + DatabaseContract.Tuesday.COLUMN_START_TIME + "=?",
                            new String[]{classToRemove, startTime.toString()});

                    break;

                case DateTimeConstants.WEDNESDAY:

                    sqLiteDatabase.execSQL("delete from "
                                    + DatabaseContract.Wednesday.TABLE_NAME + " where "
                                    + DatabaseContract.Wednesday.COLUMN_CLASS + "=? and "
                                    + DatabaseContract.Wednesday.COLUMN_START_TIME + "=?",
                            new String[]{classToRemove, startTime.toString()});

                    break;

                case DateTimeConstants.THURSDAY:

                    sqLiteDatabase.execSQL("delete from "
                                    + DatabaseContract.Thursday.TABLE_NAME + " where "
                                    + DatabaseContract.Thursday.COLUMN_CLASS + "=? and "
                                    + DatabaseContract.Thursday.COLUMN_START_TIME + "=?",
                            new String[]{classToRemove, startTime.toString()});

                    break;

                case DateTimeConstants.FRIDAY:

                    sqLiteDatabase.execSQL("delete from "
                                    + DatabaseContract.Friday.TABLE_NAME + " where "
                                    + DatabaseContract.Friday.COLUMN_CLASS + "=? and "
                                    + DatabaseContract.Friday.COLUMN_START_TIME + "=?",
                            new String[]{classToRemove, startTime.toString()});

                    break;

                case DateTimeConstants.SATURDAY:

                    sqLiteDatabase.execSQL("delete from "
                                    + DatabaseContract.Saturday.TABLE_NAME + " where "
                                    + DatabaseContract.Saturday.COLUMN_CLASS + "=? and "
                                    + DatabaseContract.Saturday.COLUMN_START_TIME + "=?",
                            new String[]{classToRemove, startTime.toString()});

                    break;

                case DateTimeConstants.SUNDAY:

                    sqLiteDatabase.execSQL("delete from "
                                    + DatabaseContract.Sunday.TABLE_NAME + " where "
                                    + DatabaseContract.Sunday.COLUMN_CLASS + "=? and "
                                    + DatabaseContract.Sunday.COLUMN_START_TIME + "=?",
                            new String[]{classToRemove, startTime.toString()});

                    break;

            }

        } finally {

            sqLiteDatabase.close();

        }

    }

    public void deleteClass(String className) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Monday.TABLE_NAME + " where "
                            + DatabaseContract.Monday.COLUMN_CLASS + "=?",
                    new String[]{className});

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Tuesday.TABLE_NAME + " where "
                            + DatabaseContract.Tuesday.COLUMN_CLASS + "=?",
                    new String[]{className});

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Wednesday.TABLE_NAME + " where "
                            + DatabaseContract.Wednesday.COLUMN_CLASS + "=?",
                    new String[]{className});

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Thursday.TABLE_NAME + " where "
                            + DatabaseContract.Thursday.COLUMN_CLASS + "=?",
                    new String[]{className});

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Friday.TABLE_NAME + " where "
                            + DatabaseContract.Friday.COLUMN_CLASS + "=?",
                    new String[]{className});

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Saturday.TABLE_NAME + " where "
                            + DatabaseContract.Saturday.COLUMN_CLASS + "=?",
                    new String[]{className});

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Sunday.TABLE_NAME + " where "
                            + DatabaseContract.Sunday.COLUMN_CLASS + "=?",
                    new String[]{className});

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                            + DatabaseContract.ClassInfo.COLUMN_NAME + "=?",
                    new String[]{className});

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Events.TABLE_NAME + " where "
                            + DatabaseContract.Events.COLUMN_CLASS + "=?",
                    new String[]{className});

        } finally {

            sqLiteDatabase.close();

        }

    }

    public void addClass(SlimClass classToAdd) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        try {

            contentValues.put(DatabaseContract.ClassInfo.COLUMN_NAME, classToAdd.getName());
            contentValues.put(DatabaseContract.ClassInfo.COLUMN_LOCATION, classToAdd.getLocation());
            contentValues.put(DatabaseContract.ClassInfo.COLUMN_TEACHER, classToAdd.getTeacher());
            contentValues.put(DatabaseContract.ClassInfo.COLUMN_COLOR, classToAdd.getColor());

            sqLiteDatabase.insert(DatabaseContract.ClassInfo.TABLE_NAME, null, contentValues);

        } finally {

            sqLiteDatabase.close();

        }

    }

    public String[] getClassLocationTeacherColor(String className) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "
                        + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                        + DatabaseContract.ClassInfo.COLUMN_NAME + "=?",
                new String[]{className});

        final String[] locationTeacherColor = new String[3];

        if (cursor.moveToNext()) {

            locationTeacherColor[0] = cursor.getString(2);
            locationTeacherColor[1] = cursor.getString(3);
            locationTeacherColor[2] = cursor.getString(4);

            cursor.close();

            sqLiteDatabase.close();

            return locationTeacherColor;

        } else {

            cursor.close();

            sqLiteDatabase.close();

            return null;

        }

    }

    public int getClassColor(String className) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "
                        + DatabaseContract.ClassInfo.TABLE_NAME + " where "
                        + DatabaseContract.ClassInfo.COLUMN_NAME + "=?",
                new String[]{className});

        if (cursor.moveToNext()) {

            String resultString = cursor.getString(4);

            cursor.close();

            sqLiteDatabase.close();

            return Integer.parseInt(resultString);

        } else {

            cursor.close();

            sqLiteDatabase.close();

            return R.color.black;

        }

    }

    public Cursor getClassesCursor(int day) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor;

        switch (day) {

            case DateTimeConstants.MONDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Monday.TABLE_NAME + " order by rowid", null);

                break;

            case DateTimeConstants.TUESDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Tuesday.TABLE_NAME + " order by rowid", null);

                break;

            case DateTimeConstants.WEDNESDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Wednesday.TABLE_NAME + " order by rowid", null);

                break;

            case DateTimeConstants.THURSDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Thursday.TABLE_NAME + " order by rowid", null);

                break;

            case DateTimeConstants.FRIDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Friday.TABLE_NAME + " order by rowid", null);

                break;

            case DateTimeConstants.SATURDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Saturday.TABLE_NAME + " order by rowid", null);

                break;

            case DateTimeConstants.SUNDAY:

                cursor = sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Sunday.TABLE_NAME + " order by rowid", null);

                break;

            default:

                cursor = null;

                break;

        }

        return cursor;

    }

    public void deleteEvent(Event event) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Events.TABLE_NAME + " where "
                            + DatabaseContract.Events.COLUMN_DATE_TIME + "=? and "
                            + DatabaseContract.Events.COLUMN_NAME + "=? and "
                            + DatabaseContract.Events.COLUMN_CLASS + "=? and "
                            + DatabaseContract.Events.COLUMN_TYPE + "=?",
                    new String[]{event.getDateTime().toString(), event.getName(), event.getClassName(), String.valueOf(event.getType())});

        } finally {

            sqLiteDatabase.close();

        }

    }

    public void deleteEventByProperties(String name, String datetime) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {

            sqLiteDatabase.execSQL("delete from "
                            + DatabaseContract.Events.TABLE_NAME + " where "
                            + DatabaseContract.Events.COLUMN_DATE_TIME + "=? and "
                            + DatabaseContract.Events.COLUMN_NAME + "=?",
                    new String[]{datetime, name});

        } finally {

            sqLiteDatabase.close();

        }

    }

    public void flushEvents(ArrayList<Event> eventsToAdd) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        try {

            sqLiteDatabase.execSQL("delete from " + DatabaseContract.Events.TABLE_NAME);

            for (final Event event : eventsToAdd) {

                contentValues.clear();

                contentValues.put(DatabaseContract.Events.COLUMN_NAME, event.getName());
                contentValues.put(DatabaseContract.Events.COLUMN_DESCRIPTION, event.getDescription());
                contentValues.put(DatabaseContract.Events.COLUMN_TYPE, String.valueOf(event.getType()));
                contentValues.put(DatabaseContract.Events.COLUMN_CLASS, event.getClassName());
                contentValues.put(DatabaseContract.Events.COLUMN_DATE_TIME, event.getDateTime().toString());
                contentValues.put(DatabaseContract.Events.COLUMN_ATTACH, event.isAttach() ? 1 : 0);
                contentValues.put(DatabaseContract.Events.COLUMN_REMIND, event.isRemind() ? 1 : 0);

                sqLiteDatabase.insert(DatabaseContract.Events.TABLE_NAME, null, contentValues);

            }

        } finally {

            sqLiteDatabase.close();

        }

    }

    public void updateClass(String oldClassName, SlimClass classToUpdate) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValuesClassInfo = new ContentValues();

        contentValuesClassInfo.put(DatabaseContract.ClassInfo.COLUMN_NAME, classToUpdate.getName());
        contentValuesClassInfo.put(DatabaseContract.ClassInfo.COLUMN_LOCATION, classToUpdate.getLocation());
        contentValuesClassInfo.put(DatabaseContract.ClassInfo.COLUMN_TEACHER, classToUpdate.getTeacher());
        contentValuesClassInfo.put(DatabaseContract.ClassInfo.COLUMN_COLOR, String.valueOf(classToUpdate.getColor()));

        try {

            sqLiteDatabase.update(DatabaseContract.ClassInfo.TABLE_NAME, contentValuesClassInfo, DatabaseContract.ClassInfo.COLUMN_NAME + "=?", new String[]{oldClassName});

            if (!oldClassName.equals(classToUpdate.getName())) {

                ContentValues contentValuesOtherTables = new ContentValues();

                contentValuesOtherTables.put(DatabaseContract.Monday.COLUMN_CLASS, classToUpdate.getName());

                sqLiteDatabase.update(DatabaseContract.Monday.TABLE_NAME, contentValuesOtherTables, DatabaseContract.Monday.COLUMN_CLASS + "=?", new String[]{oldClassName});
                sqLiteDatabase.update(DatabaseContract.Tuesday.TABLE_NAME, contentValuesOtherTables, DatabaseContract.Tuesday.COLUMN_CLASS + "=?", new String[]{oldClassName});
                sqLiteDatabase.update(DatabaseContract.Wednesday.TABLE_NAME, contentValuesOtherTables, DatabaseContract.Wednesday.COLUMN_CLASS + "=?", new String[]{oldClassName});
                sqLiteDatabase.update(DatabaseContract.Thursday.TABLE_NAME, contentValuesOtherTables, DatabaseContract.Thursday.COLUMN_CLASS + "=?", new String[]{oldClassName});
                sqLiteDatabase.update(DatabaseContract.Friday.TABLE_NAME, contentValuesOtherTables, DatabaseContract.Friday.COLUMN_CLASS + "=?", new String[]{oldClassName});
                sqLiteDatabase.update(DatabaseContract.Saturday.TABLE_NAME, contentValuesOtherTables, DatabaseContract.Saturday.COLUMN_CLASS + "=?", new String[]{oldClassName});
                sqLiteDatabase.update(DatabaseContract.Sunday.TABLE_NAME, contentValuesOtherTables, DatabaseContract.Sunday.COLUMN_CLASS + "=?", new String[]{oldClassName});

                sqLiteDatabase.update(DatabaseContract.Events.TABLE_NAME, contentValuesOtherTables, DatabaseContract.Events.COLUMN_CLASS + "=?", new String[]{oldClassName});

            }

        } finally {

            sqLiteDatabase.close();

        }

    }

    public Cursor getEventsCursor() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        return sqLiteDatabase.rawQuery("select * from " + DatabaseContract.Events.TABLE_NAME + " order by rowid", null);

    }

    public Cursor getClassesCursor() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        return sqLiteDatabase.rawQuery("select * from " + DatabaseContract.ClassInfo.TABLE_NAME + " order by rowid", null);

    }

}