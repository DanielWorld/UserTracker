package com.namgyuworld.usertracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namgyuworld.usertracker.preference.SharePref;
import com.namgyuworld.usertracker.service.TrackingService;
import com.namgyuworld.usertracker.util.Logger;
import com.namgyuworld.usertracker.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by danielpark on 6/16/15.
 */
public class InstallReferrerReceiver extends BroadcastReceiver{

    private static final String TAG = InstallReferrerReceiver.class.getSimpleName();
    private static final Logger LOG = Logger.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data = intent.getExtras();

        // check: extra data.
        if (data == null) {
            LOG.w(TAG, "Receive install referrer. But extra data is null.(no empty)");
            return;
        }

        // check: refferrer empty.
        String referrer = data.getString("referrer");
        if (StringUtil.isNullorEmpty(referrer)) {
            LOG.w(TAG, "Receive install referrer. But refferrer is empty(or null).");
            return;
        }

        // url encode refferrer
        try {
            referrer = URLEncoder.encode(referrer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SharePref prefs = new SharePref(context);

        if(StringUtil.isNullorEmpty(prefs.getInstallReferrer()) || !referrer.equals(prefs.getInstallReferrer())){
//            LOG.i(TAG, "install referrer got received. save " + referrer);
            prefs.setInstallReferrer(referrer);
            // Start Service
            TrackingService.startService(context);
        }

    }
}
