package com.danielworld.usertracker.network;

import android.content.Context;
import android.os.AsyncTask;

import com.danielworld.usertracker.database.SQLiteHelper;
import com.danielworld.usertracker.model.TrackingModel;
import com.danielworld.usertracker.util.AppUtil;
import com.danielworld.usertracker.util.JsonUtil;
import com.danielworld.usertracker.util.Logger;
import com.danielworld.usertracker.util.cryptography.CryptoUtil;
import com.danielworld.usertracker.util.cryptography.model.CryptoModel;
import com.danielworld.usertracker.util.cryptography.type.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.crypto.SecretKey;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/17/15.
 */
public class TrackerHttpConnection {

    private final String TAG = TrackerHttpConnection.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    /**
     * Send Tracking to server immediately
     *
     * @param context
     * @param trackingInfo
     */
    public void sendTrackingToServer(final Context context, final List<TrackingModel> trackingInfo) {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                String trackingData = new JsonUtil().toJson(trackingInfo);
                /**
                 * Make sure to encrypt trackingData using symmetric key (AES 128) later
                 */
                String seedKey = "iidkh*37 *#( 20*#)@KKO";
                SecretKey skey = CryptoUtil.AESUtils.generateKey(seedKey);
                CryptoModel model;
                String iv = null;
                try {
                    model = CryptoUtil.AESUtils.encrypt(trackingData, skey);
                    trackingData = model.getCryptoText();
                    iv = model.getIv();
                } catch (UnsupportedEncodingException e) {
                    trackingData = e.getMessage();
                } catch (GeneralSecurityException e) {
                    trackingData = e.getMessage();
                }
                LOG.i(TAG, "toJson:\n" + trackingData);
//                LOG.i(TAG, "fromJson:\n" + new JsonUtil().fromJson(trackingData).toString());

                try {
                    URL url = new URL(URLs.getURL(URLs.ConnectionMethod.SEND_TRACKING));   // URL
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // Open connection

                    // Specify POST method
                    conn.setRequestMethod("POST");
                    // set connection timeout
                    conn.setConnectTimeout(5000);

                    // Set the headers
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", AppUtil.getAppKeyHash(context));
                    conn.setRequestProperty("Sector", Base64.encodeToString(iv.getBytes(), Base64.NO_WRAP));

                    conn.setDoOutput(true);

                    // Get connection output stream
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                    // Copy push contents "JSON" info
                    wr.writeBytes(trackingData);

                    // Send the request
                    wr.flush();

                    // close
                    wr.close();

                    // Get the response
                    int responseCode = conn.getResponseCode();
                    LOG.i(TAG, "response code : " + responseCode);

                    if (HttpURLConnection.HTTP_OK == responseCode) {

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                    } else {
                        LOG.e(TAG, HttpStatusCode.getMessage(responseCode));
                        // response code isn't 200
                        // then time to save database
                        SQLiteHelper dbHelper = new SQLiteHelper(context);
                        dbHelper.putTemporary(trackingInfo);
                    }

                } catch (MalformedURLException e) {
                    LOG.e(TAG, "Malformed URL exception \n" + e.getMessage());
                    // then time to save database
                    SQLiteHelper dbHelper = new SQLiteHelper(context);
                    dbHelper.putTemporary(trackingInfo);
                } catch (IOException e) {
                    LOG.e(TAG, "IOException \n" + e.getMessage());
                    // then time to save database
                    SQLiteHelper dbHelper = new SQLiteHelper(context);
                    dbHelper.putTemporary(trackingInfo);
                }
                return null;
            }
        }.execute();
    }

    /**
     * Try to transmit all trackings data in database
     *
     * @param context
     * @param allTrackingInfo
     */
    public void sendTrackingInDBToServer(final Context context, final List<List<TrackingModel>> allTrackingInfo) {

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                String trackingData = new JsonUtil().toJsonList(allTrackingInfo);
                /**
                 * Make sure to encrypt trackingData using symmetric key (AES 128) later
                 */
                String seedKey = "iidkh*37 *#( 20*#)@KKO";
                SecretKey skey = CryptoUtil.AESUtils.generateKey(seedKey);
                CryptoModel model;
                String iv = null;
                try {
                    model = CryptoUtil.AESUtils.encrypt(trackingData, skey);
                    trackingData = model.getCryptoText();
                    iv = model.getIv();
                } catch (UnsupportedEncodingException e) {
                    trackingData = e.getMessage();
                } catch (GeneralSecurityException e) {
                    trackingData = e.getMessage();
                }
                LOG.i(TAG, "toJson:\n" + trackingData);

                try {
                    URL url = new URL(URLs.getURL(URLs.ConnectionMethod.SEND_TRACKING));   // URL
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // Open connection

                    // Specify POST method
                    conn.setRequestMethod("POST");
                    // set connection timeout
                    conn.setConnectTimeout(5000);

                    // Set the headers
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", AppUtil.getAppKeyHash(context));
                    conn.setRequestProperty("Sector", Base64.encodeToString(iv.getBytes(), Base64.NO_WRAP));

                    conn.setDoOutput(true);

                    // Get connection output stream
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                    // Copy push contents "JSON" info
                    wr.writeBytes(trackingData);

                    // Send the request
                    wr.flush();

                    // close
                    wr.close();

                    // Get the response
                    int responseCode = conn.getResponseCode();
                    LOG.i(TAG, "response code : " + responseCode);

                    if (HttpURLConnection.HTTP_OK == responseCode) {

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // response code is 200
                        // then time to delete database
                        SQLiteHelper dbHelper = new SQLiteHelper(context);
                        // then delete sent trackings
                        dbHelper.clearTracking(allTrackingInfo);

                    } else {
                        LOG.e(TAG, HttpStatusCode.getMessage(responseCode));
                    }

                } catch (MalformedURLException e) {
                    LOG.e(TAG, "Malformed URL exception \n" + e.getMessage());
                } catch (IOException e) {
                    LOG.e(TAG, "IOException \n" + e.getMessage());
                }
                return null;
            }
        }.execute();
    }

}