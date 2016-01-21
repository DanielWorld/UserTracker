package com.danielworld.usertracker;

import android.content.Context;
import android.content.res.Resources;

import com.danielworld.usertracker.util.StringUtil;
import com.danielworld.usertracker.util.app.AppUtil;
import com.danielworld.usertracker.util.log.Logger;
import com.danielworld.usertracker.model.TrackingModel;
import com.danielworld.usertracker.network.SendTrackingInfo;
import com.danielworld.usertracker.network.URLs;
import com.danielworld.usertracker.preference.SharePref;
import com.danielworld.usertracker.service.TrackingService;
import com.danielworld.usertracker.variables.TrackingMapKey;


/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class Tracker {

    private final String TAG = Tracker.class.getSimpleName();
    private final Logger LOG = Logger.getInstance();

    private Context mContext;
    private SendTrackingInfo mSendTrackingInfo;
    private SharePref mPref;

    public Tracker(Context context){
        this.mContext = context;
        this.mPref = new SharePref(context);

        init(context);
    }

    private void init(Context context){
        checkAppId();
        // Was Application signed by debug.keystore?
        if(AppUtil.getInstance().isDebuggable(context)){
            URLs.setDebug(true);
            LOG.enableLog();
        }
        else{
            URLs.setDebug(false);
            LOG.disableLog();
        }

        mSendTrackingInfo = new SendTrackingInfo(context);

        // Try to send missing tracking info
        TrackingService.startService(context);
    }

    /**
     * Check if app id exists, if there isn't then throw error
     */
    private void checkAppId(){
        if(StringUtil.getInstance().isNullorEmpty(AppUtil.getInstance().getMetaDataValue(mContext))){
            LOG.e(TAG, "App id is null or empty");
            throw new Resources.NotFoundException("App id wasn't found!!!");
        }
    }

    protected void send(TrackingModel trackingModel){
        LOG.v(TAG, "send tracking: " + trackingModel.toString());
        checkAppId();

        if(trackingModel.getValuePair(TrackingMapKey.TRACKING_EVENT).equals(TrackingModel.TrackingEvent.FIRST_RUN.toAcronymCode())){
            if(!mPref.hasFirstRunStart()) {
                mPref.setFirstRunStart(true);
                mSendTrackingInfo.transmit(trackingModel);
            }
        }else {
            mSendTrackingInfo.transmit(trackingModel);
        }
    }
}
