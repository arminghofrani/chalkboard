package com.ghofrani.classapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.format.DateTimeFormat;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Toolbar toolbar = findViewById(R.id.activity_settings_toolbar);
        toolbar.setTitle("Settings");
        toolbar.setElevation(getPixelFromDP(4));
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.activity_settings_frame_layout, new SettingsFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            super.onBackPressed();

            return true;

        } else {

            return onOptionsItemSelected(menuItem);

        }

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_settings);

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

            if (key.equals("detailed_notification") && DataSingleton.getInstance().getCurrentClass() != null
                    || key.equals("flip_colors") && DataSingleton.getInstance().getCurrentClass() != null
                    || key.equals("class_notification") && DataSingleton.getInstance().getCurrentClass() != null
                    || key.equals("next_class_notification") && !DataSingleton.getInstance().getNextClassesArrayList().isEmpty()
                    || key.equals("next_class_notification_minutes")
                    || key.equals("tomorrow_classes")) {

                EventBus.getDefault().post(new Update(true, false, false, false, false));

            } else if (key.equals("primary_color")
                    || key.equals("accent_color")) {

                DataSingleton.getInstance().setRecreate(true);

                getActivity().recreate();

            } else if (key.equals("24_hour_time")) {

                DataSingleton.getInstance().setIs24Hour(sharedPreferences.getBoolean("24_hour_time", true));

            } else if (key.equals("first_day_of_week")) {

                EventBus.getDefault().post(new Update(false, true, false, false, false));

                DataSingleton.getInstance().setChangedFirstDay(true);

            } else if (key.equals("expand_next_classes") || key.equals("expand_tomorrow_classes")) {

                DataSingleton.getInstance().setIsExpandableListViewCollapsed(false);

            } else if (key.equals("default_lesson_length")) {

                if (Integer.parseInt(sharedPreferences.getString(key, "60")) < 1 || Integer.parseInt(sharedPreferences.getString(key, "60")) > 1439) {

                    sharedPreferences.edit().putString(key, "60").commit();

                    getActivity().recreate();

                    Toast.makeText(getActivity(), "Please choose a value between 1 and 1439!", Toast.LENGTH_LONG).show();

                }

            } else if (key.equals("vibrate_only_during_classes")) {

                if (DataSingleton.getInstance().getCurrentClass() != null) {

                    final AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

                    if (sharedPreferences.getBoolean(key, false)) {

                        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                    } else {

                        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                    }

                }

            } else if (key.equals("first_lesson_time")) {

                try {

                    DateTimeFormat.forPattern("HH:mm").parseLocalTime(sharedPreferences.getString(key, "08:00"));
                    sharedPreferences.edit().putString(key, DateTimeFormat.forPattern("HH:mm").print(DateTimeFormat.forPattern("HH:mm").parseLocalTime(sharedPreferences.getString(key, "08:00")))).commit();

                } catch (Exception exception) {

                    sharedPreferences.edit().putString(key, "08:00").commit();

                    getActivity().recreate();

                    Toast.makeText(getActivity(), "Please enter the time in the format HH:mm!", Toast.LENGTH_LONG).show();

                }

            }

        }

    }

}