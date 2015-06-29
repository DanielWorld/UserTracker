package com.namgyuworld.usertracker;

import android.content.Context;
import android.text.format.DateFormat;

import com.namgyuworld.usertracker.model.TrackingModel;
import com.namgyuworld.usertracker.model.info.ApplicationInfo;
import com.namgyuworld.usertracker.model.info.DeviceInfo;
import com.namgyuworld.usertracker.model.info.EnvironmentInfo;
import com.namgyuworld.usertracker.preference.SharePref;
import com.namgyuworld.usertracker.util.AppUtil;
import com.namgyuworld.usertracker.util.DeviceUtil;
import com.namgyuworld.usertracker.util.Logger;
import com.namgyuworld.usertracker.util.cryptography.CryptoUtil;
import com.namgyuworld.usertracker.variables.TrackingMapKey;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class TrackerFactory {

    private static final String TAG = TrackerFactory.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();

    private Context mContext;
    private DeviceInfo mDeviceInfo;
    private ApplicationInfo mApplicationInfo;
    private EnvironmentInfo mEnvironmentInfo;

    private String mHashedGoogleAccount;

    public TrackerFactory(Context context){
        this.mContext = context;

        mApplicationInfo = DeviceUtil.getAppInfo(context);
        mDeviceInfo = DeviceUtil.getDeviceInfo();
        mEnvironmentInfo = DeviceUtil.getEnvironmentInfo(context);

        // Get Hashed Google account
        try{
            mHashedGoogleAccount = CryptoUtil.CreateHash.SHA256(AppUtil.getFirstGoogleAccount(context).name);
            // Save hashed google account in SharedPreferences
            SharePref mPrefs = new SharePref(context);
            mPrefs.setGoogleHashedID(mHashedGoogleAccount);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public TrackingModel newFirstRunTracking(){
        LOG.i(TAG, "New First Run tracking.");

        TrackingModel tracking = new TrackingModel();
        tracking.putValuePair(TrackingMapKey.TRACKING_APP_ID, AppUtil.getMetaDataValue(mContext));
        tracking.putValuePair(TrackingMapKey.TRACKING_EVENT, TrackingModel.TrackingEvent.FIRST_RUN.toAcronymCode());
        tracking.putValuePair(TrackingMapKey.TRACKING_OCCUR_TIME, String.valueOf(new Date(System.currentTimeMillis())));

        addCommonInfo(tracking);
        return tracking;
    }
    public TrackingModel newForegroundTracking(String tag){
        LOG.i(TAG, "New Foreground tracking.");

        TrackingModel tracking = new TrackingModel();
        tracking.putValuePair(TrackingMapKey.TRACKING_APP_ID, AppUtil.getMetaDataValue(mContext));
        tracking.putValuePair(TrackingMapKey.TRACKING_EVENT, TrackingModel.TrackingEvent.FOREGROUND.toAcronymCode());
        tracking.putValuePair(TrackingMapKey.TRACKING_OCCUR_TIME, String.valueOf(new Date(System.currentTimeMillis())));
        tracking.putValuePair(TrackingMapKey.TRACKING_TAG, tag);

        addCommonInfo(tracking);
        return tracking;
    }
    public TrackingModel newBackgroundTracking(String tag){
        LOG.i(TAG, "New Background tracking.");

        TrackingModel tracking = new TrackingModel();
        tracking.putValuePair(TrackingMapKey.TRACKING_APP_ID, AppUtil.getMetaDataValue(mContext));
        tracking.putValuePair(TrackingMapKey.TRACKING_EVENT, TrackingModel.TrackingEvent.BACKGROUND.toAcronymCode());
        tracking.putValuePair(TrackingMapKey.TRACKING_OCCUR_TIME, String.valueOf(new Date(System.currentTimeMillis())));
        tracking.putValuePair(TrackingMapKey.TRACKING_TAG, tag);

        addCommonInfo(tracking);
        return tracking;
    }
    public TrackingModel newActionTracking(String tag){
        LOG.i(TAG, "New Action tracking.");

        TrackingModel tracking = new TrackingModel();
        tracking.putValuePair(TrackingMapKey.TRACKING_APP_ID, AppUtil.getMetaDataValue(mContext));
        tracking.putValuePair(TrackingMapKey.TRACKING_EVENT, TrackingModel.TrackingEvent.ACTION.toAcronymCode());
        tracking.putValuePair(TrackingMapKey.TRACKING_OCCUR_TIME, String.valueOf(new Date(System.currentTimeMillis())));
        tracking.putValuePair(TrackingMapKey.TRACKING_TAG, tag);

        addCommonInfo(tracking);
        return tracking;
    }

    private void addCommonInfo(TrackingModel tracking){
        tracking.putValuePair(TrackingMapKey.INSTALL_REFERRER, "");
        tracking.putValuePair(TrackingMapKey.GOOGLE_ACCOUNT, mHashedGoogleAccount);
        tracking.putValuePair(TrackingMapKey.OS_VERSION, mDeviceInfo.getOsVersion());
        tracking.putValuePair(TrackingMapKey.APP_PACKAGE_NAME, mApplicationInfo.getPackageName());
        tracking.putValuePair(TrackingMapKey.APP_VERSION_CODE, String.valueOf(mApplicationInfo.getVersionCode()));
        tracking.putValuePair(TrackingMapKey.APP_VERSION_NAME, String.valueOf(mApplicationInfo.getVersionName()));
        tracking.putValuePair(TrackingMapKey.DEVICE_NAME, mDeviceInfo.getDeviceName());
        tracking.putValuePair(TrackingMapKey.DEVICE_MANUFACTURE, mDeviceInfo.getDeviceManufacture());
        tracking.putValuePair(TrackingMapKey.COUNTRY, mEnvironmentInfo.getCountry());
        tracking.putValuePair(TrackingMapKey.LANGUAGE, mEnvironmentInfo.getLanguage());
        tracking.putValuePair(TrackingMapKey.NETWORK_PROVIDER, mEnvironmentInfo.getNetworkProvider());
//        tracking.putValuePair(TrackingMapKey.CURRENT_TIME, mEnvironmentInfo.getCurrentTime());
    }
}
