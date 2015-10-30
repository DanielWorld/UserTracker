package com.danielworld.usertracker.network;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.danielpark.androidlibrary.StringUtil;
import com.danielpark.androidlibrary.google.adid.GoogleAdvertisingIdClient;
import com.danielpark.androidlibrary.google.adid.interfaces.OnAdvertisingIDcompleteListener;
import com.danielpark.androidlibrary.log.Logger;
import com.danielworld.usertracker.database.SQLiteHelper;
import com.danielworld.usertracker.model.TrackingModel;
import com.danielworld.usertracker.preference.SharePref;
import com.danielworld.usertracker.service.TrackingService;
import com.danielworld.usertracker.variables.TrackingMapKey;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class SendTrackingInfo {

    private final String TAG = SendTrackingInfo.class.getSimpleName();
    private final Logger LOG = Logger.getInstance();

    private Context mContext;
    private SharePref mPref;

    private SQLiteHelper mDatabase;

    public SendTrackingInfo(Context context) {
        this.mContext = context;
        this.mPref = new SharePref(context);
        this.mDatabase = new SQLiteHelper(context);
    }

    /**
     * Collect all information and transmit to server
     *
     * @param trackingModel
     */
    public void transmit(final TrackingModel trackingModel) {

        // Get google advertising id
        GoogleAdvertisingIdClient.getGoogleAdID(mContext, new OnAdvertisingIDcompleteListener() {
            @Override
            public void onAdvertisingIdComplete(String adId) {
                String advertisementID = adId;
                if (StringUtil.getInstance().isNullorEmpty(adId)) {
                    advertisementID = mPref.getGoogleAdId();
                } else {
                    mPref.setGoogleAdId(adId);
                }
                // Add google ad id to tracking model
//                trackingModel.putValuePair(TrackingMapKey.GOOGLE_AD_ID, advertisementID);
                LOG.i(TAG, trackingModel.toString());

                /**
                 * REFERRER MEASUREMENT
                 */
                // Check if temporary tracking data exists
                SQLiteHelper dbHelper = new SQLiteHelper(mContext);
                SQLiteDatabase db = null;
                Cursor c = null;
                try {
                    db = dbHelper.getReadableDatabase();
                    c = db.query(SQLiteHelper.TABLE_TEMPORARY, new String[]{SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_DATA}, null, null, null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Check install_referrer
                if (!StringUtil.getInstance().isNullorEmpty(mPref.getInstallReferrer())) {
                    // Referrer exists
                    LOG.i(TAG, "Install Referrer exists");

                    if (c.getCount() == 0) {
                        LOG.v(TAG, "No temporory tracking data!");
                        c.close();
                        db.close();
                        // Send tracking data to Server
                        sendTracking(trackingModel);
                    } else {
                        // Save current tracking data
                        saveTemporary(trackingModel);
                        c.close();
                        db.close();
                        // Start service
                        TrackingService.startService(mContext);
                    }
                } else {
                    // Referrer doesn't exist
                    // Save current tracking data
                    saveTemporary(trackingModel);
                    c.close();
                    db.close();
                    // Start service
                    TrackingService.startService(mContext);
                }


            }
        });
    }

    // Save in temporary database
    private void saveTemporary(final TrackingModel trackingModel){
        mDatabase.putTemporary(trackingModel.getTrackingList());
    }

    // Send Tracking immediately
    private void sendTracking(final TrackingModel trackingModel){

        // If Install referrer exists
        if(!StringUtil.getInstance().isNullorEmpty(mPref.getInstallReferrer())){
            trackingModel.putValuePair(TrackingMapKey.INSTALL_REFERRER, mPref.getInstallReferrer());
        }

        new TrackerHttpConnection().sendTrackingToServer(mContext, trackingModel.getTrackingList());
    }
}
