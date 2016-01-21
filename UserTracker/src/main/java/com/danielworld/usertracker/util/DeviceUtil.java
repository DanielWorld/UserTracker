package com.danielworld.usertracker.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.danielworld.usertracker.util.log.Logger;
import com.danielworld.usertracker.model.info.ApplicationInfo;
import com.danielworld.usertracker.model.info.DeviceInfo;
import com.danielworld.usertracker.model.info.EnvironmentInfo;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();

    private static DeviceUtil sThis;
    private DeviceUtil(){}
    public synchronized static final DeviceUtil getInstance(){
        if(sThis == null)
            return sThis = new DeviceUtil();
        return sThis;
    }



    /**
     * Get device information
     */
    public final DeviceInfo getDeviceInfo(){
        DeviceInfo di = new DeviceInfo();
        di.setDeviceName(Build.MODEL);
        di.setDeviceManufacture(Build.MANUFACTURER);

        String osVersion = Build.VERSION.RELEASE;
        if(StringUtil.getInstance().isNullorEmpty(osVersion)){
            osVersion = String.valueOf(Build.VERSION.SDK_INT);
        }
        di.setOsVersion(osVersion);
        return di;
    }


    public final ApplicationInfo getAppInfo(Context context){
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;

        try{
            packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException("unmatched package name : " + e.getMessage());
        }

        final ApplicationInfo ai = new ApplicationInfo();
        ai.setPackageName(context.getPackageName());
        ai.setVersionCode(packageInfo.versionCode);
        ai.setVersionName(packageInfo.versionName);
        return ai;
    }

    public final EnvironmentInfo getEnvironmentInfo(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        DateFormat dateFormat = DateFormat.getInstance();

        final EnvironmentInfo enInfo = new EnvironmentInfo();
        enInfo.setDevicdId(tm.getDeviceId());
        enInfo.setCurrentTime(dateFormat.format(new Date()));
        enInfo.setNetworkProvider(getNetworkProvider(tm));
        enInfo.setCountry(getCountry(tm));
        enInfo.setLanguage(Locale.getDefault().getLanguage());
        return enInfo;
    }

    private String getCountry(TelephonyManager tm) {
        return (tm.getSimState() == TelephonyManager.SIM_STATE_READY) ? tm.getSimCountryIso() : Locale.getDefault().getCountry();
    }

    private String getNetworkProvider(TelephonyManager tm) {
        LOG.v(TAG, "Network Operator Name: " + tm.getNetworkOperatorName());
        LOG.v(TAG, "Sim Operator Name: " + tm.getSimOperatorName());
        String provider = null;

        if(tm.getSimState() == TelephonyManager.SIM_STATE_READY){
            provider = tm.getSimOperatorName();
        }
        if(StringUtil.getInstance().isNullorEmpty(provider)){
            provider = tm.getNetworkOperatorName();
        }
        return StringUtil.getInstance().isNullorEmpty(provider) ? "UNKNOWN" : provider;
    }

}
