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
    private static final String KEY_GOOGLE_AD_ID = "google_ad_id_key";
    private static final String KEY_FIRST_RUN_IN_DB = "first_run_in_db";

    private SharedPreferences mPrefs;

    public SharePref(Context context){
        this.mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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

    public void setFirstRunInDB(boolean flag){
        mPrefs.edit().putBoolean(KEY_FIRST_RUN_IN_DB, flag).commit();
    }
    public boolean isFirstRunInDB(){
        return mPrefs.getBoolean(KEY_FIRST_RUN_IN_DB, false);
    }

}
