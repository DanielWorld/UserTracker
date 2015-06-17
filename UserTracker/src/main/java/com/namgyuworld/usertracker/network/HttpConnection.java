package com.namgyuworld.usertracker.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.namgyuworld.usertracker.database.SQLiteHelper;
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

    /**
     * Send Tracking to server immediately
     * @param context
     * @param trackingInfo
     */
    public void sendTrackingToServer(final Context context, final List<TrackingModel> trackingInfo) {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                String trackingData = new JsonUtil().toJson(trackingInfo);
                LOG.i(TAG, "toJson:\n" + trackingData);
//                LOG.i(TAG, "fromJson:\n" + new JsonUtil().fromJson(trackingData).toString());

                try{
                    URL url = new URL(URLs.getURL());   // URL
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // Open connection

                    // Specify POST method
                    conn.setRequestMethod("POST");
                    // set connection timeout
                    conn.setConnectTimeout(5000);

                    // Set the headers
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", AppUtil.getAppKeyHash(context));

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

                    if(HttpURLConnection.HTTP_OK == responseCode) {

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
                        dbHelper.putTemporary(trackingInfo);
                    }
                    else{
                        LOG.e(TAG, HttpStatusCode.getMessage(responseCode));
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    /**
     * Try to transmit all trackings data in database
     * @param context
     * @param allTrackingInfo
     */
    public void sendTrackingInDBToServer(final Context context, final List<List<TrackingModel>> allTrackingInfo) {



            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    String trackingData = new JsonUtil().toJsonList(allTrackingInfo);
                    LOG.i(TAG, "toJson:\n" + trackingData);

                    try {
                        URL url = new URL(URLs.getURL());   // URL
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // Open connection

                        // Specify POST method
                        conn.setRequestMethod("POST");
                        // set connection timeout
                        conn.setConnectTimeout(5000);

                        // Set the headers
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Authorization", AppUtil.getAppKeyHash(context));

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
                            dbHelper.clearTemporaryDB();

                        } else {
                            LOG.e(TAG, HttpStatusCode.getMessage(responseCode));
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

}