package com.ghofrani.classapp.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ChangeEvent;
import com.ghofrani.classapp.activity.Main;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.event.UpdateClassesUI;
import com.ghofrani.classapp.event.UpdateEventsUI;
import com.ghofrani.classapp.event.UpdateProgressUI;
import com.ghofrani.classapp.model.Event;
import com.ghofrani.classapp.model.EventWithID;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.model.StringWithID;
import com.ghofrani.classapp.module.AlarmReceiver;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;

import static com.ghofrani.classapp.R.drawable.event;

public class Background extends Service {

    private static IntentFilter backgroundIntentFilter;
    private static BroadcastReceiver backgroundBroadcastReceiver;

    private final int NOTIFICATION_CURRENT_CLASS_ID = 0;
    private final int NOTIFICATION_NEXT_CLASS_ID = 1;
    private final int NOTIFICATION_SERVICE_ID = 999;
    private final int ID_EVENTS = 6;

    private int reminderNotificationID = 1;

    private int progressBarId;
    private int progressTextId;
    private int headerId;
    private int textId;
    private Handler handler;
    private Runnable notificationRunnable;
    private Runnable noNotificationRunnable;
    private NotificationCompat.Builder notificationCompatBuilderService;
    private NotificationCompat.Builder notificationCompatBuilderClass;
    private NotificationCompat.Builder notificationCompatBuilderReminder;
    private NotificationManager notificationManager;
    private NotificationChannel notificationChannelService;
    private NotificationChannel notificationChannelClass;
    private NotificationChannel notificationChannelReminder;
    private RemoteViews remoteViews;

    private int red;
    private int pink;
    private int purple;
    private int deepPurple;
    private int indigo;
    private int blue;
    private int lightBlue;
    private int cyan;
    private int teal;
    private int green;
    private int lightGreen;
    private int lime;
    private int yellow;
    private int amber;
    private int orange;
    private int deepOrange;
    private int black;
    private int blueGrey;

    private DateTimeFormatter dateTimeFormatterAMPM;

    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    private AudioManager audioManager;

    private boolean currentToNextTransition = false;
    private boolean nextToCurrentTransition = false;
    private boolean simpleToDetailedTransition = false;
    private boolean detailedToSimpleTransition = false;

    private DateTime lastEventNotifyDateTime;
    private ArrayList<String> reminderSwitches;

    private AlarmManager alarmManager;
    private PendingIntent alarmManagerPendingIntent;

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            final Intent homeActivityIntent = new Intent(this, Main.class);
            final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, NOTIFICATION_SERVICE_ID, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            notificationCompatBuilderService = new NotificationCompat.Builder(this, "service")
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setWhen(0)
                    .setContentIntent(addHomeActivityIntent)
                    .setContentTitle("Chalkboard is running idle...")
                    .setContentText("Long press on this notification to hide it.")
                    .setPriority(NotificationCompat.PRIORITY_MIN);

            startForeground(NOTIFICATION_SERVICE_ID, notificationCompatBuilderService.build());

        }

        if (intent != null) {

            if (intent.hasExtra("name")) {

                databaseHelper.deleteEventByProperties(intent.getStringExtra("name"), intent.getStringExtra("date_time"));

                notificationManager.cancel(Integer.parseInt(intent.getStringExtra("notification_id")));

                getEvents(false);

            }

        }

        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    @Override
    public void onCreate() {

        super.onCreate();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        databaseHelper = new DatabaseHelper(this);

        DataSingleton.getInstance().setIs24Hour(sharedPreferences.getBoolean("24_hour_time", true));

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.view_notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannels();

        progressBarId = R.id.view_notification_progress_bar_red;
        textId = R.id.view_notification_subtitle_text_view;
        headerId = R.id.view_notification_title_text_view;
        progressTextId = R.id.view_notification_progress_text_text_view;

        dateTimeFormatterAMPM = DateTimeFormat.forPattern("h:mm a");

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        red = ContextCompat.getColor(this, R.color.red);
        pink = ContextCompat.getColor(this, R.color.pink);
        purple = ContextCompat.getColor(this, R.color.purple);
        deepPurple = ContextCompat.getColor(this, R.color.deep_purple);
        indigo = ContextCompat.getColor(this, R.color.indigo);
        blue = ContextCompat.getColor(this, R.color.blue);
        lightBlue = ContextCompat.getColor(this, R.color.light_blue);
        cyan = ContextCompat.getColor(this, R.color.cyan);
        teal = ContextCompat.getColor(this, R.color.teal);
        green = ContextCompat.getColor(this, R.color.green);
        lightGreen = ContextCompat.getColor(this, R.color.light_green);
        lime = ContextCompat.getColor(this, R.color.lime);
        yellow = ContextCompat.getColor(this, R.color.yellow);
        amber = ContextCompat.getColor(this, R.color.amber);
        orange = ContextCompat.getColor(this, R.color.orange);
        deepOrange = ContextCompat.getColor(this, R.color.deep_orange);
        black = ContextCompat.getColor(this, R.color.black);
        blueGrey = ContextCompat.getColor(this, R.color.blue_grey);

        lastEventNotifyDateTime = new DateTime().withTimeAtStartOfDay();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Intent alarmManagerIntent = new Intent(this, AlarmReceiver.class);
        alarmManagerPendingIntent = PendingIntent.getBroadcast(this, 1, alarmManagerIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        getData();
        getEvents(false);
        getTimetable();
        getClasses();

        backgroundIntentFilter = new IntentFilter();
        backgroundIntentFilter.addAction(Intent.ACTION_TIME_TICK);
        backgroundIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        backgroundIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        backgroundIntentFilter.addAction(Intent.ACTION_DATE_CHANGED);

        registerBroadcastReceiver();

        EventBus.getDefault().register(this);

    }

    @Subscribe
    public void onEvent(Update updateEvent) {

        if (updateEvent.isData())
            getData();

        if (updateEvent.isEvents())
            getEvents(updateEvent.isNotifyEvents());

        if (updateEvent.isTimetable())
            getTimetable();

        if (updateEvent.isClasses())
            getClasses();

    }

    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);

        alarmManager.cancel(alarmManagerPendingIntent);

        unregisterReceiver(backgroundBroadcastReceiver);

        notificationManager.cancelAll();

        if (handler != null) {

            handler.removeCallbacksAndMessages(null);
            handler = null;

        }

        sharedPreferences = null;
        databaseHelper.close();
        databaseHelper = null;

        backgroundIntentFilter = null;
        backgroundBroadcastReceiver = null;
        notificationRunnable = null;
        noNotificationRunnable = null;
        notificationCompatBuilderService = null;
        notificationCompatBuilderClass = null;
        notificationCompatBuilderReminder = null;
        notificationManager = null;
        notificationChannelService = null;
        notificationChannelClass = null;
        notificationChannelReminder = null;
        remoteViews = null;
        dateTimeFormatterAMPM = null;
        audioManager = null;
        lastEventNotifyDateTime = null;
        reminderSwitches = null;
        alarmManager = null;
        alarmManagerPendingIntent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            stopForeground(true);

        super.onDestroy();

    }

    @TargetApi(26)
    private void createNotificationChannels() {

        notificationChannelService = new NotificationChannel("service", "Services", NotificationManager.IMPORTANCE_NONE);
        notificationChannelService.setDescription("Notifications about services.");
        notificationChannelService.enableLights(false);
        notificationChannelService.enableVibration(false);
        notificationChannelService.setBypassDnd(false);
        notificationChannelService.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        notificationChannelService.setShowBadge(false);

        notificationManager.createNotificationChannel(notificationChannelService);

        notificationChannelClass = new NotificationChannel("class", "Class Notifications", NotificationManager.IMPORTANCE_LOW);
        notificationChannelClass.setDescription("Notifications about classes.");
        notificationChannelClass.enableLights(false);
        notificationChannelClass.enableVibration(false);
        notificationChannelClass.setBypassDnd(false);
        notificationChannelClass.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationChannelClass.setShowBadge(false);

        notificationManager.createNotificationChannel(notificationChannelClass);

        notificationChannelReminder = new NotificationChannel("reminder", "Reminders", NotificationManager.IMPORTANCE_HIGH);
        notificationChannelReminder.setDescription("Notifications about events.");
        notificationChannelReminder.enableLights(true);
        notificationChannelReminder.setLightColor(sharedPreferences.getInt("primary_color", ContextCompat.getColor(this, R.color.teal)));
        notificationChannelReminder.enableVibration(true);
        notificationChannelReminder.setBypassDnd(true);
        notificationChannelReminder.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationChannelReminder.setShowBadge(false);

        notificationManager.createNotificationChannel(notificationChannelReminder);

    }

    private void registerBroadcastReceiver() {

        backgroundBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                if (DataSingleton.getInstance().isReactToBroadcastData()) {

                    getData();
                    getEvents(true);

                    if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED) || intent.getAction().equals(Intent.ACTION_DATE_CHANGED) || intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {

                        getTimetable();

                    }

                }

            }

        };

        registerReceiver(backgroundBroadcastReceiver, backgroundIntentFilter);

    }

    private void getData() {

        if (handler != null) {

            handler.removeCallbacksAndMessages(null);
            handler = null;

        }

        final Cursor todayCursor = databaseHelper.getClassesCursor(new DateTime().getDayOfWeek());

        StandardClass currentClass = null;
        StandardClass nextClass = null;
        ArrayList<StandardClass> nextClassesArrayList = new ArrayList<>();

        final LocalTime currentTime = new LocalTime().withHourOfDay(LocalTime.now().getHourOfDay()).withMinuteOfHour(LocalTime.now().getMinuteOfHour()).withSecondOfMinute(0).withMillisOfSecond(0);

        try {

            while (todayCursor.moveToNext()) {

                final String[] locationTeacherColor = databaseHelper.getClassLocationTeacherColor(todayCursor.getString(1));

                final StandardClass standardClass = new StandardClass(todayCursor.getString(1),
                        LocalTime.parse(todayCursor.getString(2)),
                        LocalTime.parse(todayCursor.getString(3)),
                        dateTimeFormatterAMPM.print(LocalTime.parse(todayCursor.getString(2))),
                        dateTimeFormatterAMPM.print(LocalTime.parse(todayCursor.getString(3))),
                        todayCursor.getString(4).equals("no-location") ? "no-location" : (todayCursor.getString(4).equals("default") ? locationTeacherColor[0] : todayCursor.getString(4)),
                        todayCursor.getString(5).equals("no-teacher") ? "no-teacher" : (todayCursor.getString(5).equals("default") ? locationTeacherColor[1] : todayCursor.getString(5)),
                        Integer.parseInt(locationTeacherColor[2]));

                if (standardClass.getStartTime().isAfter(currentTime)) {

                    nextClassesArrayList.add(standardClass);

                    if (nextClass == null)
                        nextClass = standardClass;

                } else if ((standardClass.getStartTime().isBefore(currentTime) || standardClass.getStartTime().isEqual(currentTime)) && standardClass.getEndTime().isAfter(currentTime)) {

                    currentClass = standardClass;

                }

            }

        } finally {

            todayCursor.close();

        }

        if (currentClass != null) {

            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT)
                if (sharedPreferences.getBoolean("vibrate_only_during_classes", false))
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            final StandardClass finalCurrentClass = currentClass;

            if (sharedPreferences.getBoolean("class_notification", true)) {

                if (sharedPreferences.getBoolean("detailed_notification", true)) {

                    currentToNextTransition = true;

                    if (nextToCurrentTransition) {

                        notificationManager.cancel(NOTIFICATION_CURRENT_CLASS_ID);
                        notificationManager.cancel(NOTIFICATION_NEXT_CLASS_ID);

                        nextToCurrentTransition = false;

                    }

                    detailedToSimpleTransition = true;

                    if (simpleToDetailedTransition) {

                        notificationManager.cancel(NOTIFICATION_CURRENT_CLASS_ID);
                        notificationManager.cancel(NOTIFICATION_NEXT_CLASS_ID);

                        simpleToDetailedTransition = false;

                    }

                    final Intent homeActivityIntent = new Intent(this, Main.class);
                    final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, NOTIFICATION_CURRENT_CLASS_ID, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    final Intent eventActivityIntent = new Intent(this, ChangeEvent.class).putExtra("origin_notification", true);
                    final PendingIntent addEventActivityEvent = PendingIntent.getActivity(this, NOTIFICATION_CURRENT_CLASS_ID, eventActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    remoteViews.setOnClickPendingIntent(R.id.view_notification_change_event_image_button, addEventActivityEvent);

                    remoteViews.setInt(progressBarId, "setVisibility", View.GONE);
                    remoteViews.setInt(textId, "setVisibility", View.GONE);
                    remoteViews.setInt(progressTextId, "setVisibility", View.GONE);
                    remoteViews.setInt(headerId, "setVisibility", View.GONE);

                    if (sharedPreferences.getBoolean("flip_colors", false)) {

                        remoteViews.setInt(R.id.view_notification_relative_layout, "setBackgroundColor", finalCurrentClass.getColor());

                        if (finalCurrentClass.getColor() == lime || finalCurrentClass.getColor() == yellow || finalCurrentClass.getColor() == amber) {

                            remoteViews.setInt(R.id.view_notification_change_event_image_button, "setColorFilter", Color.BLACK);

                            progressBarId = R.id.view_notification_progress_bar_black_contrast;
                            textId = R.id.view_notification_subtitle_black_text_view;
                            progressTextId = R.id.view_notification_progress_text_text_view;
                            headerId = R.id.view_notification_title_black_text_view;

                        } else {

                            remoteViews.setInt(R.id.view_notification_change_event_image_button, "setColorFilter", Color.WHITE);

                            progressBarId = R.id.view_notification_progress_bar_white;
                            textId = R.id.view_notification_subtitle_white_text_view;
                            progressTextId = R.id.view_notification_progress_text_white_text_view;
                            headerId = R.id.view_notification_title_white_text_view;

                        }


                    } else {

                        remoteViews.setInt(R.id.view_notification_change_event_image_button, "setColorFilter", finalCurrentClass.getColor());
                        remoteViews.setInt(R.id.view_notification_relative_layout, "setBackgroundColor", Color.TRANSPARENT);

                        if (finalCurrentClass.getColor() == red) {

                            progressBarId = R.id.view_notification_progress_bar_red;

                        } else if (finalCurrentClass.getColor() == pink) {

                            progressBarId = R.id.view_notification_progress_bar_pink;

                        } else if (finalCurrentClass.getColor() == purple) {

                            progressBarId = R.id.view_notification_progress_bar_purple;

                        } else if (finalCurrentClass.getColor() == deepPurple) {

                            progressBarId = R.id.view_notification_progress_bar_deep_purple;

                        } else if (finalCurrentClass.getColor() == indigo) {

                            progressBarId = R.id.view_notification_progress_bar_indigo;

                        } else if (finalCurrentClass.getColor() == blue) {

                            progressBarId = R.id.view_notification_progress_bar_blue;

                        } else if (finalCurrentClass.getColor() == lightBlue) {

                            progressBarId = R.id.view_notification_progress_bar_light_blue;

                        } else if (finalCurrentClass.getColor() == cyan) {

                            progressBarId = R.id.view_notification_progress_bar_cyan;

                        } else if (finalCurrentClass.getColor() == teal) {

                            progressBarId = R.id.view_notification_progress_bar_teal;

                        } else if (finalCurrentClass.getColor() == green) {

                            progressBarId = R.id.view_notification_progress_bar_green;

                        } else if (finalCurrentClass.getColor() == lightGreen) {

                            progressBarId = R.id.view_notification_progress_bar_light_green;

                        } else if (finalCurrentClass.getColor() == lime) {

                            progressBarId = R.id.view_notification_progress_bar_lime;

                        } else if (finalCurrentClass.getColor() == yellow) {

                            progressBarId = R.id.view_notification_progress_bar_yellow;

                        } else if (finalCurrentClass.getColor() == amber) {

                            progressBarId = R.id.view_notification_progress_bar_amber;

                        } else if (finalCurrentClass.getColor() == orange) {

                            progressBarId = R.id.view_notification_progress_bar_orange;

                        } else if (finalCurrentClass.getColor() == deepOrange) {

                            progressBarId = R.id.view_notification_progress_bar_deep_orange;

                        } else if (finalCurrentClass.getColor() == black) {

                            progressBarId = R.id.view_notification_progress_bar_black;

                        } else if (finalCurrentClass.getColor() == blueGrey) {

                            progressBarId = R.id.view_notification_progress_bar_blue_grey;

                        }

                        textId = R.id.view_notification_subtitle_text_view;
                        progressTextId = R.id.view_notification_progress_text_text_view;
                        headerId = R.id.view_notification_title_text_view;

                    }

                    remoteViews.setInt(progressBarId, "setVisibility", View.VISIBLE);
                    remoteViews.setInt(textId, "setVisibility", View.VISIBLE);
                    remoteViews.setInt(progressTextId, "setVisibility", View.VISIBLE);
                    remoteViews.setInt(headerId, "setVisibility", View.VISIBLE);

                    notificationCompatBuilderClass = new NotificationCompat.Builder(this, "class")
                            .setSmallIcon(R.drawable.ic_notification_icon)
                            .setOngoing(true)
                            .setContentIntent(addHomeActivityIntent)
                            .setColor(finalCurrentClass.getColor())
                            .setContent(remoteViews)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setWhen(0);

                    notificationRunnable = new Runnable() {

                        public void run() {

                            final DateTime currentTime = new DateTime();

                            final long currentClassTotal = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), finalCurrentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            final long currentClassProgress = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            final int minutesRemaining = finalCurrentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingTitleText;

                            if (minutesRemaining == 1)
                                remainingTitleText = "1 min. left";
                            else
                                remainingTitleText = minutesRemaining + " mins. left";

                            remoteViews.setTextViewText(headerId, finalCurrentClass.getName() + " • " + remainingTitleText);

                            if (DataSingleton.getInstance().getNextClass() != null)
                                remoteViews.setTextViewText(textId, "Next: " + DataSingleton.getInstance().getNextClass().getName() + (DataSingleton.getInstance().getNextClass().hasLocation() ? " at " + DataSingleton.getInstance().getNextClass().getLocation() : ""));
                            else
                                remoteViews.setTextViewText(textId, "No further classes");

                            String progressBarText;
                            int progressBarProgress;

                            if (percentageValueInt >= 0 && percentageValueInt <= 100) {

                                progressBarText = String.valueOf(percentageValueInt) + "%";
                                progressBarProgress = percentageValueInt;

                                remoteViews.setTextViewText(progressTextId, String.valueOf(percentageValueInt));
                                remoteViews.setProgressBar(progressBarId, 100, percentageValueInt, false);

                            } else if (percentageValueInt < 0) {

                                progressBarText = "0%";
                                progressBarProgress = 0;

                                remoteViews.setTextViewText(progressTextId, "0");
                                remoteViews.setProgressBar(progressBarId, 100, 0, false);

                            } else {

                                progressBarText = "100%";
                                progressBarProgress = 100;

                                remoteViews.setTextViewText(progressTextId, "100");
                                remoteViews.setProgressBar(progressBarId, 100, 100, false);

                            }

                            DataSingleton.getInstance().setMinutesLeftText(remainingTitleText);
                            DataSingleton.getInstance().setProgressBarProgress(progressBarProgress);
                            DataSingleton.getInstance().setProgressBarText(progressBarText);

                            EventBus.getDefault().post(new UpdateProgressUI());

                            notificationManager.notify(NOTIFICATION_CURRENT_CLASS_ID, notificationCompatBuilderClass.build());

                            handler.postDelayed(this, 15000);

                        }

                    };

                    handler = new Handler();

                    handler.post(notificationRunnable);

                } else {

                    currentToNextTransition = true;

                    if (nextToCurrentTransition) {

                        notificationManager.cancel(NOTIFICATION_CURRENT_CLASS_ID);
                        notificationManager.cancel(NOTIFICATION_NEXT_CLASS_ID);

                        nextToCurrentTransition = false;

                    }

                    simpleToDetailedTransition = true;

                    if (detailedToSimpleTransition) {

                        notificationManager.cancel(NOTIFICATION_CURRENT_CLASS_ID);
                        notificationManager.cancel(NOTIFICATION_NEXT_CLASS_ID);

                        detailedToSimpleTransition = false;

                    }

                    final Intent homeActivityIntent = new Intent(this, Main.class);
                    final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, NOTIFICATION_CURRENT_CLASS_ID, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    final Intent eventActivityIntent = new Intent(this, ChangeEvent.class).putExtra("origin_notification", true);
                    final PendingIntent addEventActivityIntent = PendingIntent.getActivity(this, NOTIFICATION_CURRENT_CLASS_ID, eventActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    notificationCompatBuilderClass = new NotificationCompat.Builder(this, "class")
                            .setSmallIcon(R.drawable.ic_notification_icon)
                            .setOngoing(true)
                            .setColor(finalCurrentClass.getColor())
                            .setContentIntent(addHomeActivityIntent)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .addAction(event, "ADD EVENT", addEventActivityIntent)
                            .setContentTitle(finalCurrentClass.getName())
                            .setWhen(0);

                    notificationRunnable = new Runnable() {

                        public void run() {

                            final DateTime currentTime = new DateTime();

                            final long currentClassTotal = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), finalCurrentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            final long currentClassProgress = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            final int minutesRemaining = finalCurrentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingText;

                            if (minutesRemaining == 1)
                                remainingText = "1 min. left";
                            else
                                remainingText = minutesRemaining + " mins. left";

                            String remainingNotificationText = remainingText;

                            if (DataSingleton.getInstance().getNextClass() != null)
                                remainingNotificationText += " • Next: " + DataSingleton.getInstance().getNextClass().getName() + (DataSingleton.getInstance().getNextClass().hasLocation() ? " at " + DataSingleton.getInstance().getNextClass().getLocation() : "");
                            else
                                remainingNotificationText += " • No further classes";

                            notificationCompatBuilderClass.setContentText(remainingNotificationText);

                            String progressBarText;
                            int progressBarProgress;

                            if (percentageValueInt >= 0 && percentageValueInt <= 100) {

                                progressBarText = String.valueOf(percentageValueInt) + "%";
                                progressBarProgress = percentageValueInt;

                            } else if (percentageValueInt < 0) {

                                progressBarText = "0%";
                                progressBarProgress = 0;

                            } else {

                                progressBarText = "100%";
                                progressBarProgress = 100;

                            }

                            DataSingleton.getInstance().setMinutesLeftText(remainingText);
                            DataSingleton.getInstance().setProgressBarProgress(progressBarProgress);
                            DataSingleton.getInstance().setProgressBarText(progressBarText);

                            EventBus.getDefault().post(new UpdateProgressUI());

                            notificationManager.notify(NOTIFICATION_CURRENT_CLASS_ID, notificationCompatBuilderClass.build());

                            handler.postDelayed(this, 15000);

                        }

                    };

                    handler = new Handler();

                    handler.post(notificationRunnable);

                }

            } else {

                detailedToSimpleTransition = false;
                simpleToDetailedTransition = false;

                currentToNextTransition = true;
                nextToCurrentTransition = false;

                notificationManager.cancel(NOTIFICATION_CURRENT_CLASS_ID);
                notificationManager.cancel(NOTIFICATION_NEXT_CLASS_ID);

                if (noNotificationRunnable == null) {

                    noNotificationRunnable = new Runnable() {

                        @Override
                        public void run() {

                            final DateTime currentTime = new DateTime();

                            final long currentClassTotal = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), finalCurrentClass.getEndTime().toDateTimeToday()).toDurationMillis();
                            final long currentClassProgress = new Interval(finalCurrentClass.getStartTime().toDateTimeToday(), currentTime).toDurationMillis();

                            final int percentageValueInt = (int) (currentClassProgress * 100 / currentClassTotal);

                            final int minutesRemaining = finalCurrentClass.getEndTime().toDateTimeToday().getMinuteOfDay() - currentTime.getMinuteOfDay() - 1;
                            String remainingText;

                            String progressBarText;
                            int progressBarProgress;

                            if (percentageValueInt >= 0 && percentageValueInt <= 100) {

                                progressBarText = String.valueOf(percentageValueInt) + "%";
                                progressBarProgress = percentageValueInt;

                            } else if (percentageValueInt < 0) {

                                progressBarText = "0%";
                                progressBarProgress = 0;

                            } else {

                                progressBarText = "100%";
                                progressBarProgress = 100;

                            }

                            if (minutesRemaining == 1)
                                remainingText = "1 min. left";
                            else
                                remainingText = minutesRemaining + " mins. left";

                            DataSingleton.getInstance().setMinutesLeftText(remainingText);
                            DataSingleton.getInstance().setProgressBarProgress(progressBarProgress);
                            DataSingleton.getInstance().setProgressBarText(progressBarText);

                            EventBus.getDefault().post(new UpdateProgressUI());

                            handler.postDelayed(this, 15000);

                        }

                    };

                }

                handler = new Handler();

                handler.post(noNotificationRunnable);

            }

        } else if (nextClass != null && sharedPreferences.getBoolean("next_class_notification", true)) {

            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT)
                if (sharedPreferences.getBoolean("vibrate_only_during_classes", false))
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            nextToCurrentTransition = true;

            if (currentToNextTransition) {

                notificationManager.cancel(NOTIFICATION_CURRENT_CLASS_ID);
                notificationManager.cancel(NOTIFICATION_NEXT_CLASS_ID);

                currentToNextTransition = false;

            }

            final DateTime currentTimeNow = new DateTime();

            int minutesLeft = Minutes.minutesBetween(currentTimeNow, nextClass.getStartTime().toDateTimeToday()).getMinutes();

            if (minutesLeft <= Integer.parseInt(sharedPreferences.getString("next_class_notification_minutes", "30"))) {

                final Intent homeActivityIntent = new Intent(this, Main.class);
                final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, NOTIFICATION_NEXT_CLASS_ID, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                notificationCompatBuilderClass = new NotificationCompat.Builder(this, "class")
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setContentIntent(addHomeActivityIntent)
                        .setWhen(0)
                        .setColor(nextClass.getColor())
                        .setPriority(NotificationCompat.PRIORITY_MAX);

                notificationCompatBuilderClass.setContentTitle("Next: " + nextClass.getName());

                if (minutesLeft > 60) {

                    if (minutesLeft < 120) {

                        if (minutesLeft == 61)
                            notificationCompatBuilderClass.setContentText("In 1 hour, 1 minute" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                        else
                            notificationCompatBuilderClass.setContentText("In 1 hour, " + (minutesLeft % 60) + " minutes" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));

                    } else {

                        if ((minutesLeft % 60) == 0)
                            notificationCompatBuilderClass.setContentText("In " + (minutesLeft / 60) + " hours" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                        else if ((minutesLeft % 60) == 1)
                            notificationCompatBuilderClass.setContentText("In " + ((minutesLeft - (minutesLeft % 60)) / 60) + " hours, 1 minute" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                        else
                            notificationCompatBuilderClass.setContentText("In " + ((minutesLeft - (minutesLeft % 60)) / 60) + " hours, " + (minutesLeft % 60) + " minutes" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));

                    }

                } else if (minutesLeft == 60)
                    notificationCompatBuilderClass.setContentText("In 1 hour" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                else if (minutesLeft <= 0)
                    notificationCompatBuilderClass.setContentText("In 0 minutes" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                else if (minutesLeft == 1)
                    notificationCompatBuilderClass.setContentText("In 1 minute" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));
                else
                    notificationCompatBuilderClass.setContentText("In " + minutesLeft + " minutes" + (nextClass.hasLocation() ? " at " + nextClass.getLocation() : ""));

                notificationManager.notify(NOTIFICATION_NEXT_CLASS_ID, notificationCompatBuilderClass.build());

            } else {

                notificationManager.cancel(NOTIFICATION_CURRENT_CLASS_ID);
                notificationManager.cancel(NOTIFICATION_NEXT_CLASS_ID);

            }

        } else {

            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT)
                if (sharedPreferences.getBoolean("vibrate_only_during_classes", false))
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            currentToNextTransition = false;
            nextToCurrentTransition = false;
            detailedToSimpleTransition = false;
            simpleToDetailedTransition = false;

            notificationManager.cancel(NOTIFICATION_CURRENT_CLASS_ID);
            notificationManager.cancel(NOTIFICATION_NEXT_CLASS_ID);

        }

        ArrayList<StandardClass> tomorrowClassesArrayList = new ArrayList<>();

        if (sharedPreferences.getBoolean("tomorrow_classes", true)) {

            int day = new DateTime().getDayOfWeek();

            if (day == DateTimeConstants.SUNDAY)
                day = DateTimeConstants.MONDAY;
            else
                day++;

            Cursor tomorrowCursor = databaseHelper.getClassesCursor(day);

            try {

                while (tomorrowCursor.moveToNext()) {

                    final String[] locationTeacherColor = databaseHelper.getClassLocationTeacherColor(tomorrowCursor.getString(1));

                    tomorrowClassesArrayList.add(new StandardClass(tomorrowCursor.getString(1),
                            LocalTime.parse(tomorrowCursor.getString(2)),
                            LocalTime.parse(tomorrowCursor.getString(3)),
                            dateTimeFormatterAMPM.print(LocalTime.parse(tomorrowCursor.getString(2))),
                            dateTimeFormatterAMPM.print(LocalTime.parse(tomorrowCursor.getString(3))),
                            tomorrowCursor.getString(4).equals("no-location") ? "no-location" : (tomorrowCursor.getString(4).equals("default") ? locationTeacherColor[0] : tomorrowCursor.getString(4)),
                            tomorrowCursor.getString(5).equals("no-teacher") ? "no-teacher" : (tomorrowCursor.getString(5).equals("default") ? locationTeacherColor[1] : tomorrowCursor.getString(5)),
                            Integer.parseInt(locationTeacherColor[2])));

                }

            } finally {

                tomorrowCursor.close();

            }

        }

        DataSingleton.getInstance().setCurrentClass(currentClass);
        DataSingleton.getInstance().setNextClass(nextClass);
        DataSingleton.getInstance().setNextClassesArrayList(nextClassesArrayList);
        DataSingleton.getInstance().setTomorrowClassesArrayList(tomorrowClassesArrayList);

        EventBus.getDefault().post(new UpdateClassesUI());

    }

    private void getEvents(boolean notify) {

        if (notify) {

            if (lastEventNotifyDateTime.equals(new DateTime().withTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), 0, 0)))
                notify = false;

            lastEventNotifyDateTime = new DateTime().withTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), 0, 0);

        }

        Cursor eventsCursor = databaseHelper.getEventsCursor();

        final ArrayList<Event> todayEventArrayList = new ArrayList<>();
        final ArrayList<Event> tomorrowEventArrayList = new ArrayList<>();
        final ArrayList<Event> thisWeekEventArrayList = new ArrayList<>();
        final ArrayList<Event> nextWeekEventArrayList = new ArrayList<>();
        final ArrayList<Event> thisMonthEventArrayList = new ArrayList<>();
        final ArrayList<Event> beyondThisMonthEventArrayList = new ArrayList<>();
        final ArrayList<Event> pastEventArrayList = new ArrayList<>();

        final DateTime today = new DateTime().withTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), 0, 0);
        final DateTime tomorrow = today.plusDays(1);

        boolean thisWeekEnabled = true;
        Interval thisWeek;
        Interval nextWeek;

        int secondLastDayOfWeek = 0;
        int lastDayOfWeek = 0;

        switch (Integer.parseInt(sharedPreferences.getString("first_day_of_week", "1"))) {

            case DateTimeConstants.MONDAY:

                secondLastDayOfWeek = DateTimeConstants.SATURDAY;
                lastDayOfWeek = DateTimeConstants.SUNDAY;

                break;

            case DateTimeConstants.TUESDAY:

                secondLastDayOfWeek = DateTimeConstants.SUNDAY;
                lastDayOfWeek = DateTimeConstants.MONDAY;

                break;

            case DateTimeConstants.WEDNESDAY:

                secondLastDayOfWeek = DateTimeConstants.MONDAY;
                lastDayOfWeek = DateTimeConstants.TUESDAY;

                break;

            case DateTimeConstants.THURSDAY:

                secondLastDayOfWeek = DateTimeConstants.TUESDAY;
                lastDayOfWeek = DateTimeConstants.WEDNESDAY;

                break;

            case DateTimeConstants.FRIDAY:

                secondLastDayOfWeek = DateTimeConstants.WEDNESDAY;
                lastDayOfWeek = DateTimeConstants.THURSDAY;

                break;

            case DateTimeConstants.SATURDAY:

                secondLastDayOfWeek = DateTimeConstants.THURSDAY;
                lastDayOfWeek = DateTimeConstants.FRIDAY;

                break;

            case DateTimeConstants.SUNDAY:

                secondLastDayOfWeek = DateTimeConstants.FRIDAY;
                lastDayOfWeek = DateTimeConstants.SATURDAY;

                break;

        }

        final DateTime thisWeekEndDateTime = new DateTime().plusDays(Utils.getPlusDaysTill(Integer.parseInt(sharedPreferences.getString("first_day_of_week", "1")))).withTimeAtStartOfDay();

        if (today.getDayOfWeek() == secondLastDayOfWeek) {

            thisWeekEnabled = false;
            thisWeek = new Interval(today, today);
            nextWeek = new Interval(today.plusDays(2).withTimeAtStartOfDay(), today.plusDays(9).withTimeAtStartOfDay());

        } else if (today.getDayOfWeek() == lastDayOfWeek) {

            thisWeekEnabled = false;
            thisWeek = new Interval(today, today);
            nextWeek = new Interval(today.plusDays(1).withTimeAtStartOfDay(), today.plusDays(8).withTimeAtStartOfDay());

        } else {

            thisWeek = new Interval(tomorrow.plusDays(1).withTimeAtStartOfDay(), thisWeekEndDateTime);
            nextWeek = new Interval(thisWeek.getEnd(), thisWeek.getEnd().plusDays(7));

        }

        boolean thisMonthEnabled = false;
        Interval thisMonth = new Interval(today, today);

        if (nextWeek.getEnd().getMonthOfYear() == today.getMonthOfYear()) {

            if (today.dayOfMonth().withMaximumValue().getDayOfMonth() > nextWeek.getEnd().minusDays(1).getDayOfMonth()) {

                thisMonthEnabled = true;
                thisMonth = new Interval(nextWeek.getEnd(), today.dayOfMonth().withMaximumValue().plusDays(1).withTimeAtStartOfDay());

            }

        }

        if (reminderSwitches == null)
            reminderSwitches = new ArrayList<>();
        else
            reminderSwitches.clear();

        String[] reminderTimes;
        ArrayList<Event> reminderEvents = new ArrayList<>();

        if (notify) {

            reminderTimes = sharedPreferences.getStringSet("reminder_times", null) == null ? new String[]{"no-reminders"} : (sharedPreferences.getStringSet("reminder_times", null).isEmpty() ? new String[]{"no-reminders"} : sharedPreferences.getStringSet("reminder_times", null).toArray(new String[]{}));

            if (!reminderTimes[0].equals("no-reminders"))
                Collections.addAll(reminderSwitches, reminderTimes);

        }

        final DateTimeZone dateTimeZone = DateTimeZone.getDefault();

        try {

            while (eventsCursor.moveToNext()) {

                final Event event = new Event(eventsCursor.getString(1), eventsCursor.getString(2), Integer.parseInt(eventsCursor.getString(3)), eventsCursor.getString(4), DateTime.parse(eventsCursor.getString(5)).withZone(dateTimeZone), eventsCursor.getInt(6) == 1, eventsCursor.getInt(7) == 1, databaseHelper.getClassColor(eventsCursor.getString(4)));

                if (event.getDateTime().isBefore(today) || event.getDateTime().isEqual(today)) {

                    pastEventArrayList.add(event);

                    if (event.getDateTime().isEqual(today))
                        reminderEvents.add(event);

                } else if (today.withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    todayEventArrayList.add(event);

                    if (notify) {

                        if (event.isRemind()) {

                            if (!reminderSwitches.isEmpty()) {

                                if (reminderSwitches.contains("0")) {

                                    if (reminderSwitches.contains("1")) {

                                        if (!event.getDateTime().isAfter(today.withTime(6, 0, 0, 0))) {

                                            if (event.getDateTime().minusHours(1).isEqual(today))
                                                reminderEvents.add(event);

                                        } else {

                                            if (event.getDateTime().minusHours(1).isEqual(today))
                                                reminderEvents.add(event);
                                            else if (today.isEqual(today.withTime(6, 0, 0, 0)))
                                                reminderEvents.add(event);

                                        }

                                    } else {

                                        if (event.getDateTime().minusHours(1).isEqual(today))
                                            reminderEvents.add(event);

                                    }

                                } else if (reminderSwitches.contains("1")) {

                                    if (today.isEqual(today.withTime(6, 0, 0, 0)))
                                        reminderEvents.add(event);

                                }

                            }

                        }

                    }

                } else if (tomorrow.withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    tomorrowEventArrayList.add(event);

                    if (notify) {

                        if (event.isRemind()) {

                            if (!reminderSwitches.isEmpty()) {

                                if (reminderSwitches.contains("0")) {

                                    if (event.getDateTime().minusHours(1).isEqual(today))
                                        reminderEvents.add(event);

                                }

                                if (reminderSwitches.contains("2")) {

                                    if (today.isEqual(event.getDateTime().minusDays(1).withTime(21, 0, 0, 0)))
                                        reminderEvents.add(event);

                                }

                                if (reminderSwitches.contains("3")) {

                                    if (today.isEqual(event.getDateTime().minusDays(1).withTime(18, 0, 0, 0)))
                                        reminderEvents.add(event);

                                }

                                if (reminderSwitches.contains("4")) {

                                    if (Utils.getClassesArrayListOfDay(today.getDayOfWeek()) != null)
                                        if (today.isEqual(Utils.getClassesArrayListOfDay(today.getDayOfWeek()).get(Utils.getClassesArrayListOfDay(today.getDayOfWeek()).size() - 1).getEndTime().toDateTimeToday()))
                                            reminderEvents.add(event);

                                }

                            }

                        }

                    }

                } else if (thisWeek.contains(event.getDateTime()) && thisWeekEnabled) {

                    thisWeekEventArrayList.add(event);

                } else if (nextWeek.contains(event.getDateTime())) {

                    nextWeekEventArrayList.add(event);

                    if (notify)
                        if (event.isRemind())
                            if (!reminderSwitches.isEmpty())
                                if (reminderSwitches.contains("5"))
                                    if (event.getDateTime().minusDays(7).withTimeAtStartOfDay().equals(today.withTimeAtStartOfDay()))
                                        if (today.equals(today.withTime(18, 0, 0, 0)))
                                            reminderEvents.add(event);

                } else if (thisMonth.contains(event.getDateTime()) && thisMonthEnabled) {

                    thisMonthEventArrayList.add(event);

                } else {

                    beyondThisMonthEventArrayList.add(event);

                }

            }

        } finally {

            eventsCursor.close();

        }

        DataSingleton.getInstance().setTodayEventArrayList(todayEventArrayList);
        DataSingleton.getInstance().setTomorrowEventArrayList(tomorrowEventArrayList);
        DataSingleton.getInstance().setThisWeekEventArrayList(thisWeekEventArrayList);
        DataSingleton.getInstance().setNextWeekEventArrayList(nextWeekEventArrayList);
        DataSingleton.getInstance().setThisMonthEventArrayList(thisMonthEventArrayList);
        DataSingleton.getInstance().setBeyondThisMonthEventArrayList(beyondThisMonthEventArrayList);
        DataSingleton.getInstance().setPastEventArrayList(pastEventArrayList);

        ArrayList<Object> eventDataArrayList = new ArrayList<>();

        if (!DataSingleton.getInstance().getPastEventArrayList().isEmpty())
            eventDataArrayList.add(new StringWithID("Late", eventDataArrayList.size()));

        for (final Event event : DataSingleton.getInstance().getPastEventArrayList())
            eventDataArrayList.add(new EventWithID(event, eventDataArrayList.size()));

        if (!DataSingleton.getInstance().getTodayEventArrayList().isEmpty())
            eventDataArrayList.add(new StringWithID("Today", eventDataArrayList.size()));

        for (final Event event : DataSingleton.getInstance().getTodayEventArrayList())
            eventDataArrayList.add(new EventWithID(event, eventDataArrayList.size()));

        if (!DataSingleton.getInstance().getTomorrowEventArrayList().isEmpty())
            eventDataArrayList.add(new StringWithID("Tomorrow", eventDataArrayList.size()));

        for (final Event event : DataSingleton.getInstance().getTomorrowEventArrayList())
            eventDataArrayList.add(new EventWithID(event, eventDataArrayList.size()));

        if (!DataSingleton.getInstance().getThisWeekEventArrayList().isEmpty())
            eventDataArrayList.add(new StringWithID("This week", eventDataArrayList.size()));

        for (final Event event : DataSingleton.getInstance().getThisWeekEventArrayList())
            eventDataArrayList.add(new EventWithID(event, eventDataArrayList.size()));

        if (!DataSingleton.getInstance().getNextWeekEventArrayList().isEmpty())
            eventDataArrayList.add(new StringWithID("Next week", eventDataArrayList.size()));

        for (final Event event : DataSingleton.getInstance().getNextWeekEventArrayList())
            eventDataArrayList.add(new EventWithID(event, eventDataArrayList.size()));

        if (!DataSingleton.getInstance().getThisMonthEventArrayList().isEmpty())
            eventDataArrayList.add(new StringWithID("This month", eventDataArrayList.size()));

        for (final Event event : DataSingleton.getInstance().getThisMonthEventArrayList())
            eventDataArrayList.add(new EventWithID(event, eventDataArrayList.size()));

        if (!DataSingleton.getInstance().getBeyondThisMonthEventArrayList().isEmpty())
            eventDataArrayList.add(new StringWithID("After this month", eventDataArrayList.size()));

        for (final Event event : DataSingleton.getInstance().getBeyondThisMonthEventArrayList())
            eventDataArrayList.add(new EventWithID(event, eventDataArrayList.size()));

        DataSingleton.getInstance().setEventDataArrayList(eventDataArrayList);

        DataSingleton.getInstance().setThisWeekEnd(thisWeekEndDateTime);
        DataSingleton.getInstance().setNextWeekEnd(nextWeek.getEnd());

        if (notify) {

            if (!reminderEvents.isEmpty()) {

                if (reminderNotificationID == 998)
                    reminderNotificationID = 2;
                else
                    reminderNotificationID++;

                final DateTimeFormatter dateTimeFormatterDayOfWeekString = DateTimeFormat.forPattern("EEEE");
                final DateTimeFormatter dateTimeFormatterTime24Hour = DateTimeFormat.forPattern("HH:mm");

                final boolean is24Hour = sharedPreferences.getBoolean("24_hour_time", true);

                if (reminderEvents.size() == 1) {

                    final Event event = reminderEvents.get(0);

                    final Intent homeActivityIntent = new Intent(this, Main.class).putExtra("fragment", ID_EVENTS);
                    final PendingIntent homeActivityPendingIntent = PendingIntent.getActivity(this, reminderNotificationID, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    final Intent doneIntent = new Intent(this, Background.class);

                    doneIntent.putExtra("name", event.getName());
                    doneIntent.putExtra("date_time", event.getDateTime().toString());
                    doneIntent.putExtra("notification_id", String.valueOf(reminderNotificationID));

                    final PendingIntent donePendingIntent = PendingIntent.getService(this, reminderNotificationID, doneIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    notificationCompatBuilderReminder = new NotificationCompat.Builder(this, "reminder")
                            .setSmallIcon(R.drawable.ic_notification_icon)
                            .setColor(event.getColor())
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setContentIntent(homeActivityPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .addAction(R.drawable.event, "DONE", donePendingIntent)
                            .setContentTitle(event.getClassName() + " • " + event.getName());

                    String contentString = "";

                    if (!pastEventArrayList.contains(event)) {

                        if (event.getType() == Event.TYPE_HOMEWORK)
                            contentString += "Homework due ";
                        else if (event.getType() == Event.TYPE_TASK)
                            contentString += "Task due ";
                        else if (event.getType() == Event.TYPE_EXAM)
                            contentString += "Exam ";

                        if (event.isAttach()) {

                            ArrayList<StandardClass> classesList = Utils.getClassesArrayListOfDay(event.getDateTime().getDayOfWeek());
                            boolean completed = false;

                            if (classesList != null) {

                                int index = 0;

                                while (index < classesList.size() && !completed) {

                                    if (classesList.get(index).getName().equals(event.getClassName())) {

                                        if (classesList.get(index).getStartTime().equals(event.getDateTime().toLocalTime())) {

                                            if (todayEventArrayList.contains(event)) {

                                                if (event.getType() == Event.TYPE_EXAM)
                                                    contentString += "in today's class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                                else
                                                    contentString += "today's class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                            } else if (tomorrowEventArrayList.contains(event)) {

                                                if (event.getType() == Event.TYPE_EXAM)
                                                    contentString += "in tomorrow's class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                                else
                                                    contentString += "tomorrow's class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                            } else if (nextWeekEventArrayList.contains(event)) {

                                                if (event.getType() == Event.TYPE_EXAM)
                                                    contentString += "in " + dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + "'s class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                                else
                                                    contentString += dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + "'s class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                            }

                                            completed = true;

                                        }

                                    }

                                    index++;

                                }

                            }

                            if (!completed) {

                                if (todayEventArrayList.contains(event)) {

                                    contentString += "today at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                } else if (tomorrowEventArrayList.contains(event)) {

                                    contentString += "tomorrow at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                } else if (nextWeekEventArrayList.contains(event)) {

                                    if (event.getType() == Event.TYPE_EXAM)
                                        contentString += "on " + dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + " at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                    else
                                        contentString += dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + " at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                }

                            }

                        } else {

                            if (todayEventArrayList.contains(event)) {

                                contentString += "today at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                            } else if (tomorrowEventArrayList.contains(event)) {

                                contentString += "tomorrow at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                            } else if (nextWeekEventArrayList.contains(event)) {

                                if (event.getType() == Event.TYPE_EXAM)
                                    contentString += "on " + dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + " at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                else
                                    contentString += dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + " at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                            }

                        }

                    } else {

                        if (event.getType() == Event.TYPE_HOMEWORK)
                            contentString += "Homework late!";
                        else if (event.getType() == Event.TYPE_TASK)
                            contentString += "Task late!";
                        else if (event.getType() == Event.TYPE_EXAM)
                            contentString += "Exam starting now!";

                    }

                    notificationCompatBuilderReminder.setContentText(contentString);

                    notificationManager.notify(reminderNotificationID, notificationCompatBuilderReminder.build());

                } else if (reminderEvents.size() > 1) {

                    String reminderGroup = "reminder_group_" + String.valueOf(reminderNotificationID + reminderEvents.size());

                    for (final Event event : reminderEvents) {

                        final Intent homeActivityIntent = new Intent(this, Main.class).putExtra("fragment", ID_EVENTS);
                        final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, reminderNotificationID, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                        final Intent doneIntent = new Intent(this, Background.class);

                        doneIntent.putExtra("name", event.getName());
                        doneIntent.putExtra("date_time", event.getDateTime().toString());
                        doneIntent.putExtra("notification_id", String.valueOf(reminderNotificationID));

                        final PendingIntent donePendingIntent = PendingIntent.getService(this, reminderNotificationID, doneIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                        notificationCompatBuilderReminder = new NotificationCompat.Builder(this, "reminder")
                                .setSmallIcon(R.drawable.ic_notification_icon)
                                .setAutoCancel(true)
                                .setContentIntent(addHomeActivityIntent)
                                .setGroup(reminderGroup)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .addAction(R.drawable.event, "DONE", donePendingIntent)
                                .setContentTitle(event.getClassName() + " • " + event.getName());

                        String contentString = "";

                        if (!pastEventArrayList.contains(event)) {

                            if (event.getType() == Event.TYPE_HOMEWORK)
                                contentString += "Homework due ";
                            else if (event.getType() == Event.TYPE_TASK)
                                contentString += "Task due ";
                            else if (event.getType() == Event.TYPE_EXAM)
                                contentString += "Exam ";

                            if (event.isAttach()) {

                                ArrayList<StandardClass> classesList = Utils.getClassesArrayListOfDay(event.getDateTime().getDayOfWeek());
                                boolean completed = false;

                                if (classesList != null) {

                                    int index = 0;

                                    while (index < classesList.size() && !completed) {

                                        if (classesList.get(index).getName().equals(event.getClassName())) {

                                            if (classesList.get(index).getStartTime().equals(event.getDateTime().toLocalTime())) {

                                                if (todayEventArrayList.contains(event)) {

                                                    if (event.getType() == Event.TYPE_EXAM)
                                                        contentString += "in today's class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                                    else
                                                        contentString += "today's class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                                } else if (tomorrowEventArrayList.contains(event)) {

                                                    if (event.getType() == Event.TYPE_EXAM)
                                                        contentString += "in tomorrow's class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                                    else
                                                        contentString += "tomorrow's class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                                } else if (nextWeekEventArrayList.contains(event)) {

                                                    if (event.getType() == Event.TYPE_EXAM)
                                                        contentString += "in " + dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + "'s class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                                    else
                                                        contentString += dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + "'s class at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                                }

                                                completed = true;

                                            }

                                        }

                                        index++;

                                    }

                                }

                                if (!completed) {

                                    if (todayEventArrayList.contains(event)) {

                                        contentString += "today at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                    } else if (tomorrowEventArrayList.contains(event)) {

                                        contentString += "tomorrow at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                    } else if (nextWeekEventArrayList.contains(event)) {

                                        if (event.getType() == Event.TYPE_EXAM)
                                            contentString += "on " + dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + " at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                        else
                                            contentString += dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + " at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                    }

                                }

                            } else {

                                if (todayEventArrayList.contains(event)) {

                                    contentString += "today at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                } else if (tomorrowEventArrayList.contains(event)) {

                                    contentString += "tomorrow at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                } else if (nextWeekEventArrayList.contains(event)) {

                                    if (event.getType() == Event.TYPE_EXAM)
                                        contentString += "on " + dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + " at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));
                                    else
                                        contentString += dateTimeFormatterDayOfWeekString.print(event.getDateTime()) + " at " + (is24Hour ? dateTimeFormatterTime24Hour.print(event.getDateTime()) : dateTimeFormatterAMPM.print(event.getDateTime()));

                                }

                            }

                        } else {

                            if (event.getType() == Event.TYPE_HOMEWORK)
                                contentString += "Homework late!";
                            else if (event.getType() == Event.TYPE_TASK)
                                contentString += "Task late!";
                            else if (event.getType() == Event.TYPE_EXAM)
                                contentString += "Exam starting now!";

                        }

                        notificationCompatBuilderReminder.setContentText(contentString);

                        notificationManager.notify(reminderNotificationID, notificationCompatBuilderReminder.build());

                        reminderNotificationID++;

                    }

                    final Intent homeActivityIntent = new Intent(this, Main.class).putExtra("fragment", ID_EVENTS);
                    final PendingIntent addHomeActivityIntent = PendingIntent.getActivity(this, reminderNotificationID, homeActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    notificationCompatBuilderReminder = new NotificationCompat.Builder(this, "reminder")
                            .setSmallIcon(R.drawable.ic_notification_icon)
                            .setColor(sharedPreferences.getInt("primary_color", ContextCompat.getColor(this, R.color.teal)))
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setContentTitle("Chalkboard Reminders")
                            .setContentIntent(addHomeActivityIntent)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentText("You have " + reminderEvents.size() + " reminders")
                            .setGroup(reminderGroup)
                            .setGroupSummary(true);

                    notificationManager.notify(reminderNotificationID, notificationCompatBuilderReminder.build());

                }

            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (!eventDataArrayList.isEmpty())
                updateAlarm();

        EventBus.getDefault().post(new UpdateEventsUI());

    }

    private void getTimetable() {

        Cursor timetableCursor = null;

        try {

            for (int i = DateTimeConstants.MONDAY; i <= DateTimeConstants.SUNDAY; i++) {

                final ArrayList<StandardClass> classesArrayList = new ArrayList<>();

                timetableCursor = databaseHelper.getClassesCursor(i);

                while (timetableCursor.moveToNext()) {

                    final String[] locationTeacherColor = databaseHelper.getClassLocationTeacherColor(timetableCursor.getString(1));

                    classesArrayList.add(new StandardClass(timetableCursor.getString(1),
                            LocalTime.parse(timetableCursor.getString(2)),
                            LocalTime.parse(timetableCursor.getString(3)),
                            dateTimeFormatterAMPM.print(LocalTime.parse(timetableCursor.getString(2))),
                            dateTimeFormatterAMPM.print(LocalTime.parse(timetableCursor.getString(3))),
                            timetableCursor.getString(4).equals("no-location") ? "no-location" : (timetableCursor.getString(4).equals("default") ? locationTeacherColor[0] : timetableCursor.getString(4)),
                            timetableCursor.getString(5).equals("no-teacher") ? "no-teacher" : (timetableCursor.getString(5).equals("default") ? locationTeacherColor[1] : timetableCursor.getString(5)),
                            Integer.parseInt(locationTeacherColor[2])));

                }

                if (!classesArrayList.isEmpty())
                    Utils.setClassesArrayListOfDay(i, classesArrayList);
                else
                    Utils.setClassesArrayListOfDay(i, null);

            }

        } finally {

            timetableCursor.close();

        }

    }

    private void getClasses() {

        Cursor classesCursor = databaseHelper.getClassesCursor();

        final ArrayList<SlimClass> slimClassArrayList = new ArrayList<>();
        final ArrayList<String> classNamesArrayList = new ArrayList<>();

        try {

            while (classesCursor.moveToNext()) {

                slimClassArrayList.add(new SlimClass(classesCursor.getString(1), classesCursor.getString(2), classesCursor.getString(3), databaseHelper.getClassColor(classesCursor.getString(1))));
                classNamesArrayList.add(classesCursor.getString(1));

            }

        } finally {

            classesCursor.close();

        }

        Collections.reverse(classNamesArrayList);

        DataSingleton.getInstance().setAllClassesArrayList(slimClassArrayList);
        DataSingleton.getInstance().setAllClassNamesArrayList(classNamesArrayList);

    }

    @TargetApi(23)
    private void updateAlarm() {

        alarmManager.cancel(alarmManagerPendingIntent);

        final ArrayList<Long> alarmTimeArrayList = new ArrayList<>();

        for (final Object object : DataSingleton.getInstance().getEventDataArrayList()) {

            if (object instanceof EventWithID) {

                final Event event = ((EventWithID) object).getEvent();

                if (event.isRemind()) {

                    if (reminderSwitches.contains("0"))
                        alarmTimeArrayList.add(event.getDateTime().minusHours(1).getMillis());

                    if (reminderSwitches.contains("1"))
                        alarmTimeArrayList.add(event.getDateTime().withTime(6, 0, 0, 0).getMillis());

                    if (reminderSwitches.contains("2"))
                        alarmTimeArrayList.add(event.getDateTime().minusDays(1).withTime(21, 0, 0, 0).getMillis());

                    if (reminderSwitches.contains("3"))
                        alarmTimeArrayList.add(event.getDateTime().minusDays(1).withTime(18, 0, 0, 0).getMillis());

                    if (reminderSwitches.contains("4"))
                        if (Utils.getClassesArrayListOfDay(event.getDateTime().minusDays(1).getDayOfWeek()) != null)
                            alarmTimeArrayList.add(event.getDateTime().minusDays(1).withTime(Utils.getClassesArrayListOfDay(event.getDateTime().minusDays(1).getDayOfWeek()).get(Utils.getClassesArrayListOfDay(event.getDateTime().minusDays(1).getDayOfWeek()).size() - 1).getEndTime()).getMillis());

                    if (reminderSwitches.contains("5"))
                        alarmTimeArrayList.add(event.getDateTime().minusDays(7).withTime(18, 0, 0, 0).getMillis());

                    alarmTimeArrayList.add(event.getDateTime().getMillis());

                }

            }

        }

        Collections.sort(alarmTimeArrayList);

        for (final long time : alarmTimeArrayList) {

            if (DateTime.now().getMillis() < time) {

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time + 5000, alarmManagerPendingIntent);
                break;

            }

        }

    }

}