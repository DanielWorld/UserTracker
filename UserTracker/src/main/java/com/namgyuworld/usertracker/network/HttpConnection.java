package com.namgyuworld.usertracker.network;

import android.content.Context;
import android.os.AsyncTask;

import com.namgyuworld.usertracker.model.TrackingModel;
import com.namgyuworld.usertracker.util.AppUtil;
import com.namgyuworld.usertracker.util.JsonUtil;
import com.namgyuworld.usertracker.util.Logger;
import com.namgyuworld.usertracker.util.StringUtil;
import com.namgyuworld.usertracker.util.cryptography.CryptoUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyPair;
import java.util.List;

import javax.crypto.SecretKey;

/**
 * Created by danielpark on 6/17/15.
 */
public class HttpConnection {

    private final String TAG = HttpConnection.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    public void sendTrackingToServer(final Context context, final List<TrackingModel> trackingInfo) {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                LOG.i(TAG, new JsonUtil().toJson(trackingInfo));
                return null;
            }
        }.execute();
    }

}
