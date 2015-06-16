package com.namgyuworld.usertracker;

import android.content.Context;
import android.os.Build;

/**
 * Created by danielpark on 6/16/15.
 */
public class UserTracker {
    private static UserTracker sThis;

    synchronized public static final UserTracker getInstance(Context context){
        if(sThis == null)
            sThis = new UserTracker(context);

        return sThis;
    }
    private Tracker mTracker;
    private TrackerFactory mTrackerFactory;

    public UserTracker(Context context){
        this.mTracker = new Tracker(context);
        this.mTrackerFactory = new TrackerFactory(context);
    }

    /**
     * Called when application runs for the first time <br>
     *     Once you called and deliver message to Server, it won't invoke even thou you call this again
     * @param tag
     */
    public final void sendFirstRun(String tag){
        if(Build.VERSION.SDK_INT >= 10){
            mTracker.send(mTrackerFactory.newFirstRunTracking(tag));
        }
    }

    /**
     * Called when app is foreground
     * @param tag
     */
    public final void sendForeground(String tag) {
        if(Build.VERSION.SDK_INT >= 10) {
            mTracker.send(mTrackerFactory.newForegroundTracking(tag));
        }
    }

    /**
     * Called when app is background
     * @param tag
     */
    public final void sendBackground(String tag) {
        if(Build.VERSION.SDK_INT >= 10) {
            mTracker.send(mTrackerFactory.newBackgroundTracking(tag));
        }
    }

    /**
     * Called when app sends certain action
     * @param action
     * @param param
     */
    public final void sendAction(String action, String param) {
        if(Build.VERSION.SDK_INT >= 10) {
            mTracker.send(mTrackerFactory.newActionTracking(action, param));
        }
    }
}
