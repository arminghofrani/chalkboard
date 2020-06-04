package com.ghofrani.classapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;

public class ChangeClass extends AppCompatActivity {

    private final int ID_TIMETABLE = 1;
    private final int RESULT_CHANGED = 0;
    private final int RESULT_NO_CHANGES = 1;
    private final int MODE_ADD = 0;
    private final int MODE_EDIT = 1;

    private int mode;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_class);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final Toolbar toolbar = findViewById(R.id.activity_change_class_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        mode = getIntent().getIntExtra("mode", MODE_ADD);

        if (mode == MODE_ADD) {

            toolbar.setTitle("Add New Class");

            if (!sharedPreferences.contains("show_toast_add_to_timetable"))
                sharedPreferences.edit().putBoolean("show_toast_add_to_timetable", true).commit();

        } else {

            className = getIntent().getStringExtra("class");

            toolbar.setTitle("Edit " + className);

        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mode == MODE_EDIT) {

            DatabaseHelper databaseHelper = new DatabaseHelper(this);

            String[] classLocationTeacherColor;

            try {

                classLocationTeacherColor = databaseHelper.getClassLocationTeacherColor(className);

            } finally {

                databaseHelper.close();

            }

            final EditText inputNameEditText = findViewById(R.id.activity_change_class_name_edit_text);
            final EditText inputLocationEditText = findViewById(R.id.activity_change_class_location_edit_text);
            final EditText inputTeacherEditText = findViewById(R.id.activity_change_class_input_teacher_edit_text);

            inputNameEditText.setText(className);
            inputLocationEditText.setText(classLocationTeacherColor[0].equals("no-location") ? "" : classLocationTeacherColor[0]);
            inputTeacherEditText.setText(classLocationTeacherColor[1].equals("no-teacher") ? "" : classLocationTeacherColor[1]);

            sharedPreferences.edit().putInt("add_class_color", Integer.parseInt(classLocationTeacherColor[2])).commit();

        }

        getFragmentManager().beginTransaction().replace(R.id.activity_change_class_color_frame_layout, new ColorFragment()).commit();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

            materialDialogBuilder.title("Discard changes?");

            if (mode == MODE_ADD)
                materialDialogBuilder.content("This class will be deleted.");
            else
                materialDialogBuilder.content("Your changes won't be saved.");

            materialDialogBuilder.positiveText("YES");
            materialDialogBuilder.positiveColorRes(R.color.black);
            materialDialogBuilder.negativeText("CANCEL");
            materialDialogBuilder.negativeColorRes(R.color.black);

            materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                    setResult(RESULT_NO_CHANGES, new Intent());

                    ChangeClass.super.onBackPressed();

                }

            });

            materialDialogBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                }

            });

            materialDialogBuilder.show();

            return true;

        } else if (menuItem.getItemId() == R.id.toolbar_change_event_check_item) {

            final EditText inputNameEditText = findViewById(R.id.activity_change_class_name_edit_text);

            if (!inputNameEditText.getText().toString().trim().isEmpty()) {

                DatabaseHelper databaseHelper = new DatabaseHelper(this);

                final EditText inputLocationEditText = findViewById(R.id.activity_change_class_location_edit_text);
                final EditText inputTeacherEditText = findViewById(R.id.activity_change_class_input_teacher_edit_text);

                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                if (mode == MODE_EDIT) {

                    if (!className.equals(inputNameEditText.getText().toString().trim())) {

                        if (DataSingleton.getInstance().getAllClassNamesArrayList() != null) {

                            if (DataSingleton.getInstance().getAllClassNamesArrayList().contains(inputNameEditText.getText().toString().trim())) {

                                databaseHelper.close();

                                Toast.makeText(this, "A class with this name exists already!", Toast.LENGTH_LONG).show();

                                return true;

                            }

                        }

                    }

                    try {

                        databaseHelper.updateClass(className, new SlimClass(inputNameEditText.getText().toString().trim(),
                                (inputLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : inputLocationEditText.getText().toString().trim()),
                                (inputTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : inputTeacherEditText.getText().toString().trim()),
                                sharedPreferences.getInt("add_class_color", ContextCompat.getColor(this, R.color.teal))));

                    } finally {

                        databaseHelper.close();

                    }

                    EventBus.getDefault().post(new Update(true, true, true, true, false));

                    final View currentFocus = this.getCurrentFocus();

                    if (currentFocus != null) {

                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                    }

                    setResult(RESULT_CHANGED, new Intent().putExtra("class_name", inputNameEditText.getText().toString().trim()));

                    finish();

                    return true;

                } else {

                    if (DataSingleton.getInstance().getAllClassNamesArrayList() != null) {

                        if (!DataSingleton.getInstance().getAllClassNamesArrayList().contains(inputNameEditText.getText().toString().trim())) {

                            try {

                                databaseHelper.addClass(new SlimClass(inputNameEditText.getText().toString().trim(),
                                        (inputLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : inputLocationEditText.getText().toString().trim()),
                                        (inputTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : inputTeacherEditText.getText().toString().trim()),
                                        sharedPreferences.getInt("add_class_color", ContextCompat.getColor(this, R.color.teal))));

                            } finally {

                                databaseHelper.close();

                            }

                            EventBus.getDefault().post(new Update(false, false, false, true, false));

                            final View currentFocus = this.getCurrentFocus();

                            if (currentFocus != null) {

                                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                            }

                            setResult(RESULT_CHANGED, new Intent().putExtra("switch_to_timetable", ID_TIMETABLE));

                            finish();

                            return true;

                        } else {

                            databaseHelper.close();

                            Toast.makeText(this, "A class with this name exists already!", Toast.LENGTH_LONG).show();

                            return true;

                        }

                    } else {

                        try {

                            databaseHelper.addClass(new SlimClass(inputNameEditText.getText().toString().trim(),
                                    (inputLocationEditText.getText().toString().trim().isEmpty() ? "no-location" : inputLocationEditText.getText().toString().trim()),
                                    (inputTeacherEditText.getText().toString().trim().isEmpty() ? "no-teacher" : inputTeacherEditText.getText().toString().trim()),
                                    sharedPreferences.getInt("add_class_color", ContextCompat.getColor(this, R.color.teal))));

                        } finally {

                            databaseHelper.close();

                        }

                        EventBus.getDefault().post(new Update(false, false, false, true, false));

                        final View currentFocus = this.getCurrentFocus();

                        if (currentFocus != null) {

                            final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);

                        }

                        setResult(RESULT_CHANGED, new Intent().putExtra("switch_to_timetable", ID_TIMETABLE));

                        finish();

                        return true;

                    }

                }

            } else {

                Toast.makeText(this, "Please add a name!", Toast.LENGTH_LONG).show();

                return true;

            }

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    @Override
    public void onBackPressed() {

        final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

        materialDialogBuilder.title("Discard changes?");

        if (mode == MODE_ADD)
            materialDialogBuilder.content("This class will be deleted.");
        else
            materialDialogBuilder.content("Your changes won't be saved.");

        materialDialogBuilder.positiveText("YES");
        materialDialogBuilder.positiveColorRes(R.color.black);
        materialDialogBuilder.negativeText("CANCEL");
        materialDialogBuilder.negativeColorRes(R.color.black);

        materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                materialDialog.dismiss();

                setResult(RESULT_NO_CHANGES, new Intent());

                ChangeClass.super.onBackPressed();

            }

        });

        materialDialogBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                materialDialog.dismiss();

            }

        });

        materialDialogBuilder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_change_class, menu);

        return true;

    }

    public static class ColorFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_add_class);

        }

        @Override
        public void onResume() {

            super.onResume();

            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {

            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);

            super.onPause();

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals("add_class_color")) {

                setPreferenceScreen(null);
                addPreferencesFromResource(R.xml.preferences_add_class);

            }

        }

    }

}