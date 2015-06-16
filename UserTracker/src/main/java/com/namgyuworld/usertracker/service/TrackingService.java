package com.namgyuworld.usertracker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by danielpark on 6/16/15.
 */
public class TrackingService extends IntentService{

    public static void startService(Context context){

    }

    public TrackingService() {
        super("service name");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TrackingService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
