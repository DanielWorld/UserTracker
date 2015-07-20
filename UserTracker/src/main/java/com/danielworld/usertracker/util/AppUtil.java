package com.danielworld.usertracker.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();
    private static final Logger LOG = Logger.getInstance();

    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    /**
     * Get application version code
     * @param context
     * @return current App versionCode <br>
     *     return 0 if you failed to get App versionCode
     */
    public static int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get application version name
     * @param context
     * @return current App versionName <br>
     *     return "" is you failed to get App versionCode
     */
    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Get String value from 'meta-data' in AndroidManifest.xml<br>
     *     MAKE SURE that 'meta-data' should be declared in Manifest xml file <br>
     *         <br> (example) <br>
     * &lt;meta-data <br>
     android:name="com.namgyuworld.utility.appid" <br>
     android:value="@string/app_id" /&gt;
     *
     * @param context
     * @return
     */
    public static String getMetaDataValue(Context context){
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("com.danielworld.utility.appId");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if the application is signed by Debug keystore.
     *
     * @param ctx
     * @return <code>true</code> : if app was signed by debug.keystore
     */
    public static boolean isDebuggable(Context ctx) {
        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (int i = 0; i < signatures.length; i++) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                if (cert.getSubjectX500Principal().equals(DEBUG_DN)) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            // debuggable variable will remain false
        } catch (CertificateException e) {
            // debuggable variable will remain false
        }
        return false;
    }

    public static final Account getFirstGoogleAccount(Context context){
        Account[] accounts = getGoogleAccounts(context);
        if(accounts == null || accounts.length == 0){
            return null;
        }
        return accounts[0];
    }
    /**
     * Get all google accounts in device
     * @param context
     * @return
     */
    private static final Account[] getGoogleAccounts(Context context){
        AccountManager am = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        return am.getAccountsByType("com.google");
    }
    /**
     * Get key hash like e.g) MoOnjObCBRe$nfa42kdoeMdie4=
     * @param context
     * @return return "" if you failed to get key-hash
     */
    public static String getAppKeyHash(Context context){
        PackageInfo packageInfo;
        String keyHash = null;
        try{
            // Getting application package name
            String packageName = context.getApplicationContext().getPackageName();

            // Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for(Signature signature: packageInfo.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = new String(Base64.encode(md.digest(), 0));

                return keyHash;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}