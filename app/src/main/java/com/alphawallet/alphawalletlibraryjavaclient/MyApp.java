package com.alphawallet.alphawalletlibraryjavaclient;

import android.app.Application;

import com.alphawallet.app.App;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        new App(getApplicationContext()).onCreate();
    }
}
