package com.danielworld.usertracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class UserTracker {
    private static UserTracker sThis;

    synchronized public static final UserTracker getInstance(Context context) {
        if (sThis == null)
            sThis = new UserTracker(context);
        return sThis;
    }

    private Context mContext;
    private Tracker mTracker;
    private TrackerFactory mTrackerFactory;

    private UserTracker(Context context) {
        this.mContext = context;
        this.mTracker = new Tracker(context);
        this.mTrackerFactory = new TrackerFactory(context);
    }

    /**
     * Called when application runs for the first time <br>
     * Once you called and deliver message to Server, it won't invoke even thou you call this again
     */
    public final void sendFirstRun() {
        if (Build.VERSION.SDK_INT >= 10) {
            mTracker.send(mTrackerFactory.newFirstRunTracking());
        }
    }

    public final void sendForeground(String tag){
        sendForeground(tag, null);
    }
    /**
     * Called when app is foreground
     *
     * @param tag
     */
    public final void sendForeground(String tag, String action) {
        if (Build.VERSION.SDK_INT >= 10) {
            mTracker.send(mTrackerFactory.newForegroundTracking(tag, action));
        }
    }

    public final void sendBackground(String tag){
        sendBackground(tag, null);
    }
    /**
     * Called when app is background
     *
     * @param tag
     */
    public final void sendBackground(String tag, String action) {
        if (Build.VERSION.SDK_INT >= 10) {
            mTracker.send(mTrackerFactory.newBackgroundTracking(tag, action));
        }
    }

    public final void sendAction(String tag){
        sendAction(tag, null);
    }
    /**
     * Called when app sends certain action
     *
     * @param tag
     */
    public final void sendAction(String tag, String action) {
        if (Build.VERSION.SDK_INT >= 10) {
            mTracker.send(mTrackerFactory.newActionTracking(tag, action));
        }
    }
}
