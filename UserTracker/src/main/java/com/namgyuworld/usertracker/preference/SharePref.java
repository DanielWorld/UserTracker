package com.namgyuworld.usertracker.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by Daniel Park on 2015-06-16.
 */
public class SharePref {

    private static final String PREF_NAME = "tracker_pref";

    private static final String KEY_INSTALL_REFERRER = "install_referrer_key";
    private static final String KEY_GOOGLE_HASHED_ID = "google_hashed_id";
    private static final String KEY_GOOGLE_AD_ID = "google_ad_id_key";
    private static final String KEY_FIRST_RUN_START = "first_run_start";
    private static final String KEY_RECEIVER_ACTIVATED = "receiver_activivated";

    private SharedPreferences mPrefs;

    public SharePref(Context context){
        this.mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    // set google hashed id
    public void setGoogleHashedID(String hashedId){
        mPrefs.edit().putString(KEY_GOOGLE_HASHED_ID, hashedId).commit();
    }
    public String getGoogleHashedID(){
        return mPrefs.getString(KEY_GOOGLE_HASHED_ID, "");
    }

    // set google advertisement id
    public void setGoogleAdId(String adId){
        mPrefs.edit().putString(KEY_GOOGLE_AD_ID, adId).commit();
    }
    public String getGoogleAdId(){
        return mPrefs.getString(KEY_GOOGLE_AD_ID, "");
    }

    // set install referrer
    public void setInstallReferrer(String referrer){
        mPrefs.edit().putString(KEY_INSTALL_REFERRER, referrer).commit();
    }
    public String getInstallReferrer(){
        return mPrefs.getString(KEY_INSTALL_REFERRER, "");
    }

    public void setFirstRunStart(boolean flag){
        mPrefs.edit().putBoolean(KEY_FIRST_RUN_START, flag).commit();
    }
    public boolean hasFirstRunStart(){
        return mPrefs.getBoolean(KEY_FIRST_RUN_START, false);
    }

    public void setReceiverActivated(int mode){
        mPrefs.edit().putInt(KEY_RECEIVER_ACTIVATED, mode).commit();
    }

    /**
     * 0 : nothing happened
     * 1 : got install_referrer from receiver
     * 2 : created Run first mode
     * @return
     */
    public int getReceiverActivated(){
        return mPrefs.getInt(KEY_RECEIVER_ACTIVATED, 0);
    }

}
