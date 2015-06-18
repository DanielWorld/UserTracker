package com.namgyuworld.usertracker;

import android.content.Context;
import android.content.res.Resources;

import com.namgyuworld.usertracker.model.TrackingModel;
import com.namgyuworld.usertracker.network.SendTrackingInfo;
import com.namgyuworld.usertracker.network.URLs;
import com.namgyuworld.usertracker.preference.SharePref;
import com.namgyuworld.usertracker.service.TrackingService;
import com.namgyuworld.usertracker.util.AppUtil;
import com.namgyuworld.usertracker.util.Logger;
import com.namgyuworld.usertracker.util.StringUtil;
import com.namgyuworld.usertracker.variables.TrackingMapKey;

import java.util.List;


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
        if(AppUtil.isDebuggable(context)){
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
        if(StringUtil.isNullorEmpty(AppUtil.getMetaDataValue(mContext))){
            LOG.e(TAG, "App id is null or empty");
            throw new Resources.NotFoundException("App id wasn't found!!!");
        }
    }

    protected void send(TrackingModel trackingModel){
        LOG.v(TAG, "send tracking: " + trackingModel.toString());
        checkAppId();

        if(trackingModel.getValuePair(TrackingMapKey.TRACKING_EVENT).equals(TrackingModel.TrackingEvent.FIRST_RUN)){
            if(!mPref.hasFirstRunStart()) {
                mPref.setFirstRunStart(true);
                mSendTrackingInfo.transmit(trackingModel);
            }
        }else {
            mSendTrackingInfo.transmit(trackingModel);
        }
    }
}
