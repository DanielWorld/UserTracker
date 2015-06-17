package com.namgyuworld.usertracker.util.google.adId;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;

import com.namgyuworld.usertracker.util.Logger;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Get Advertising ID <br>
 *     This shouldn't be running on Main Thread
 * <br><br>
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by Daniel Park on 2015-06-16.
 */
public class GoogleAdvertisingIdClient {

    private static final String TAG = GoogleAdvertisingIdClient.class.getSimpleName();
    static final Logger LOG = Logger.getInstance();

    /**
     * Get Google Advertising ID
     *
     * @param context
     */
    static public void getGoogleAdID(final Context context, final OnAdvertisingIDcompleteListener listener) {

        new AsyncTask<Void, Void, String>() {

            private String id;

            @Override
            protected String doInBackground(Void... params) {

                GoogleAdvertisingIdClient.AdInfo adInfo = null;

                // Sometimes, connection fails
                int tryCount = 0;
                while (adInfo == null && tryCount < 5) {
                    tryCount++;

                    try {
                        adInfo = GoogleAdvertisingIdClient.getAdvertisingIdInfo(context);
                    } catch (Exception e) {
//                        LOG.e(TAG, e.getMessage());
                        LOG.e(TAG, "advertisingIdInfo error");
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {

                    }
                }

                try {
                    id = adInfo.getId();
                }
                // If adInfo.getId() causes error by java.lang.NullPointException
                // then set "" to id
                catch (Exception e) {
                    LOG.i(TAG, "Failed to get id of AdvertisingIdClient.AdInfo");
                    return null;
                }

//                LOG.i(TAG, "Advertising ID: " + id);
                return id;
            }

            @Override
            protected void onPostExecute(String adId) {
                // Only one time you can get adId, otherwise it fails
                listener.onAdvertisingIdComplete(adId);
            }
        }.execute();
    }



    private static final class AdInfo {
        private final String advertisingId;
        private final boolean limitAdTrackingEnabled;

        AdInfo(String advertisingId, boolean limitAdTrackingEnabled) {
            this.advertisingId = advertisingId;
            this.limitAdTrackingEnabled = limitAdTrackingEnabled;
        }

        public String getId() {
            return this.advertisingId;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.limitAdTrackingEnabled;
        }
    }
    private static AdInfo getAdvertisingIdInfo(Context context) throws Exception {
        if(Looper.myLooper() == Looper.getMainLooper()) throw new IllegalStateException("Cannot be called from the main thread");

        try { PackageManager pm = context.getPackageManager(); pm.getPackageInfo("com.android.vending", 0); }
        catch (Exception e) { throw e; }

        AdvertisingConnection connection = new AdvertisingConnection();
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        if(context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            try {
                AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());
                AdInfo adInfo = new AdInfo(adInterface.getId(), adInterface.isLimitAdTrackingEnabled(true));
                return adInfo;
            } catch (Exception exception) {
                throw exception;
            } finally {
                context.unbindService(connection);
            }
        }
        throw new IOException("Google Play connection failed");
    }


    private static final class AdvertisingConnection implements ServiceConnection {
        boolean retrieved = false;
        private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<IBinder>(1);

        public void onServiceConnected(ComponentName name, IBinder service) {
            try { this.queue.put(service); }
            catch (InterruptedException localInterruptedException){}
        }

        public void onServiceDisconnected(ComponentName name){}

        public IBinder getBinder() throws InterruptedException {
            if (this.retrieved) throw new IllegalStateException();
            this.retrieved = true;
            return (IBinder)this.queue.take();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;

        public AdvertisingInterface(IBinder pBinder) {
            binder = pBinder;
        }

        public IBinder asBinder() {
            return binder;
        }

        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            String id;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                binder.transact(1, data, reply, 0);
                reply.readException();
                id = reply.readString();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return id;
        }

        public boolean isLimitAdTrackingEnabled(boolean paramBoolean) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            boolean limitAdTracking;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                data.writeInt(paramBoolean ? 1 : 0);
                binder.transact(2, data, reply, 0);
                reply.readException();
                limitAdTracking = 0 != reply.readInt();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return limitAdTracking;
        }
    }
}
