package com.namgyuworld.usertracker.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.namgyuworld.usertracker.TrackerFactory;
import com.namgyuworld.usertracker.database.SQLiteHelper;
import com.namgyuworld.usertracker.model.TrackingModel;
import com.namgyuworld.usertracker.network.TrackerHttpConnection;
import com.namgyuworld.usertracker.network.URLs;
import com.namgyuworld.usertracker.preference.SharePref;
import com.namgyuworld.usertracker.util.AppUtil;
import com.namgyuworld.usertracker.util.JsonUtil;
import com.namgyuworld.usertracker.util.Logger;
import com.namgyuworld.usertracker.util.StringUtil;
import com.namgyuworld.usertracker.variables.TrackingMapKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class TrackingService extends IntentService{

    private static final String TAG = TrackingService.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();

    // When does transmitting data function proceed? in DELAY_TIME (default delay time is 5 seconds)
    private static final int DELAY_TIME = 5000;

    private static long sTriggerTime;

    public TrackingService() {
        super(TAG);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TrackingService(String name) {
        super(TAG);
    }

    public static void startService(Context context){
        startService(context, DELAY_TIME); // Default delay value is 5000 seconds
    }

    public static void startService(Context context, long delayTime){

        // Suppose it invokes from Receiver
        if(AppUtil.isDebuggable(context)){
            URLs.setDebug(true);
            LOG.enableLog();
        }
        else{
            URLs.setDebug(false);
            LOG.disableLog();
        }

        // If there is no reserved task
        if(!existsAlarm()){
            synchronized (TrackingService.class){
                if(!existsAlarm()){
                    LOG.i(TAG, "Create new Transmit Schedule");
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    final long triggerAtTime = SystemClock.elapsedRealtime() + delayTime;

                    Intent service = new Intent(context, TrackingService.class);
                    PendingIntent operation = PendingIntent.getService(context, 0, service, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, operation);

                    // Save the last reserved time
                    sTriggerTime = triggerAtTime;
                    LOG.i(TAG, "Service has reserved and it will proceed in " + delayTime / 1000 + "seconds");
                }
            }
        }else{
            LOG.i(TAG, "Sorry, There is reserved task");
        }
    }


    private static boolean existsAlarm() {
        return sTriggerTime != 0 && sTriggerTime > SystemClock.elapsedRealtime();
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();

        SharePref mPrefs = new SharePref(context);

        // Prepare to send data in DB to Server
        synchronized (SQLiteHelper.class){
            SQLiteHelper dbHelper = new SQLiteHelper(context);

            if(!mPrefs.hasFirstRunStart() && !StringUtil.isNullorEmpty(mPrefs.getInstallReferrer())){
                // record that first run is in database
                mPrefs.setFirstRunStart(true);
                // Check runFirst in DB and other things..
                TrackingModel tracking = new TrackerFactory(context).newFirstRunTracking();
                tracking.putValuePair(TrackingMapKey.INSTALL_REFERRER, mPrefs.getInstallReferrer()); // put changed referrer
                tracking.putValuePair(TrackingMapKey.GOOGLE_AD_ID, mPrefs.getGoogleAdId()); // put google ad id
                dbHelper.putTemporary(tracking.getTrackingList());
            }

            // Check if there is other left data And update Install_referrer in all db?
            SQLiteDatabase db = null;
            Cursor c;
            try{
                db = dbHelper.getReadableDatabase();
                c = db.query(SQLiteHelper.TABLE_TEMPORARY, new String[]{SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_DATA}, null, null, null, null, null);

                int count = c.getCount();
                LOG.i(TAG, "tracking count in DB : " + count);

                if(count <= 0){
                    LOG.i(TAG, "No temporary tracking data");
                    return;
                }


                final int columnId = c.getColumnIndex(SQLiteHelper.COLUMN_ID);
                final int columnData = c.getColumnIndex(SQLiteHelper.COLUMN_DATA);

                List<List<TrackingModel>> trackingMultipleList = new ArrayList<>(); // Contain tracking lists

                TrackingModel trackingModel = null;

                if(count > 0){
                    while(c.moveToNext()){
                        LOG.i(TAG, "row id: " + c.getInt(columnId));
                        trackingModel = new JsonUtil().fromJson(c.getString(columnData));
                        trackingModel.putValuePair(TrackingMapKey.INSTALL_REFERRER, mPrefs.getInstallReferrer());

                        // If tracking is First Run and do not have Install referrer then, skip it.
                        // FIRST RUN type
                        if(trackingModel.getValuePair(TrackingMapKey.TRACKING_EVENT).equals(TrackingModel.TrackingEvent.FIRST_RUN.toAcronymCode())) {
                            // Install referrer exists
                            if(!StringUtil.isNullorEmpty(mPrefs.getInstallReferrer()))
                                trackingMultipleList.add(trackingModel.getTrackingList());
                        }else {
                            trackingMultipleList.add(trackingModel.getTrackingList());
                        }
                        LOG.i(TAG, trackingModel.toString());
                    }
                }

                c.close();

                // Send all trackings in DB to Server
                new TrackerHttpConnection().sendTrackingInDBToServer(context, trackingMultipleList);


            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }
}
