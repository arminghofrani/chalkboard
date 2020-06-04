package com.ghofrani.classapp.module;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.ghofrani.classapp.service.Background;

import net.danlew.android.joda.JodaTimeAndroid;

public class Chalkboard extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        JodaTimeAndroid.init(this);

        DataSingleton.initInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(new Intent(this, Background.class));
        else
            startService(new Intent(this, Background.class));

    }

}