package com.freelancing.x;

import android.app.Application;

import com.cloudinary.android.MediaManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MediaManager.init(this);

    }
}
