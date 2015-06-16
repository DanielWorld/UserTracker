package com.namgyuworld.usertracker.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Daniel Park on 2015-06-16.
 */
public class SharePref {

    private static final String PREF_NAME = "tracker_pref";

    private static final String KEY_INSTALL_REFERRER = "install_referrer_key";
    private static final String KEY_GOOGLE_AD_ID = "google_ad_id_key";

    private SharedPreferences mPrefs;

    public SharePref(Context context){
        this.mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    // set google advertisement id
    public void setGoogleAdId(String adId){
        mPrefs.edit().putString(KEY_GOOGLE_AD_ID, adId).commit();
    }
    public String getGoogleAdId(){
        return mPrefs.getString(KEY_GOOGLE_AD_ID, null);
    }

    // set install referrer
    public void setInstallReferrer(String referrer){
        mPrefs.edit().putString(KEY_INSTALL_REFERRER, referrer).commit();
    }
    public String getInstallReferrer(){
        return mPrefs.getString(KEY_INSTALL_REFERRER, null);
    }




}
