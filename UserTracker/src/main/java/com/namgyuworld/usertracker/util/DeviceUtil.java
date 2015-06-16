package com.namgyuworld.usertracker.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.namgyuworld.usertracker.model.info.ApplicationInfo;
import com.namgyuworld.usertracker.model.info.DeviceInfo;
import com.namgyuworld.usertracker.model.info.EnvironmentInfo;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by danielpark on 6/16/15.
 */
public class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getSimpleName();
    private static Logger LOG = Logger.getInstance();

    /**
     * Get device information
     */
    public static final DeviceInfo getDeviceInfo(){
        DeviceInfo di = new DeviceInfo();
        di.setDeviceName(Build.MODEL);
        di.setDeviceManufacture(Build.MANUFACTURER);

        String osVersion = Build.VERSION.RELEASE;
        if(StringUtil.isNullorEmpty(osVersion)){
            osVersion = String.valueOf(Build.VERSION.SDK_INT);
        }
        di.setOsVersion(osVersion);
        return di;
    }


    public static final ApplicationInfo getAppInfo(Context context){
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

    public static final EnvironmentInfo getEnvironmentInfo(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        DateFormat dateFormat = DateFormat.getInstance();

        final EnvironmentInfo enInfo = new EnvironmentInfo();
        enInfo.setCurrentTime(dateFormat.format(new Date()));
        enInfo.setNetworkProvider(getNetworkProvider(tm));
        enInfo.setCountry(getCountry(tm));
        enInfo.setLanguage(Locale.getDefault().getLanguage());
        return enInfo;
    }

    private static String getCountry(TelephonyManager tm) {
        return (tm.getSimState() == TelephonyManager.SIM_STATE_READY) ? tm.getSimCountryIso() : Locale.getDefault().getCountry();
    }

    private static String getNetworkProvider(TelephonyManager tm) {
        LOG.v(TAG, "Network Operator Name: " + tm.getNetworkOperatorName());
        LOG.v(TAG, "Sim Operator Name: " + tm.getSimOperatorName());
        return (tm.getSimState() == TelephonyManager.SIM_STATE_READY) ? tm.getSimOperatorName() : "UNKNOWN";
    }

}
