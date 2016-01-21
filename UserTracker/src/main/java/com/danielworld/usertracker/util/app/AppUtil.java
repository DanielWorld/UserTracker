package com.danielworld.usertracker.util.app;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.danielworld.usertracker.util.log.Logger;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.security.auth.x500.X500Principal;

/**
 * Created by daniel on 15. 10. 29.
 */
public class AppUtil {

    private final Logger LOG = Logger.getInstance();
    private final String TAG = getClass().getSimpleName();

    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    // Apply singleton pattern (DCL : double checked locking)
    private static volatile AppUtil sThis;
    private AppUtil() {}

    public static final AppUtil getInstance(){
        if(sThis == null){
            synchronized (AppUtil.class){
                if(sThis == null){
                    sThis = new AppUtil();
                }
            }
        }
        return sThis;
    }

    /**
     * Check if the application is signed by debug.keystore.
     *
     * @param ctx
     * @return <code>true</code> : if app was signed by debug.keystore
     */
    public boolean isDebuggable(Context ctx) {
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

    /**
     * Get key hash like e.g) MoOnjObCBRe$nfa42kdoeMdie4=
     * @param context
     * @return return "" if you failed to get key-hash
     */
    public String getAppKeyHash(Context context){
        PackageInfo packageInfo;
        String keyHash = null;
        try{
            // Getting application package name
            String packageName = getAppPackageName(context);

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

    /**
     * Get application version code
     * @param context
     * @return current App versionCode <br>
     *     return 0 if you failed to get App versionCode
     */
    public int getAppVersionCode(Context context) {
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
    public String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0.0";
        }
    }

    /**
     * Get application package name
     * @param context
     * @return
     */
    public String getAppPackageName(Context context){
        return context.getApplicationContext().getPackageName();
    }

    /**
     * Get installed packages list
     * @param context
     * @param permission <code>true</code> Get Installed packages with permission granted <br>
     *                   <code>false</code> Get Installed packages with permission denied
     */
    public List<PackageInfo> getInstalledPackages(Context context, boolean permission){

        PackageManager pm = context.getPackageManager();

        if(permission) {
            List<PackageInfo> grantedPacks = pm.getInstalledPackages(PackageManager.PERMISSION_GRANTED);

            Collections.sort(grantedPacks, new Comparator<PackageInfo>() {
                @Override
                public int compare(PackageInfo lhs, PackageInfo rhs) {
                    return lhs.packageName.compareTo(rhs.packageName);
                }
            });
            for (PackageInfo pack : grantedPacks) {
                LOG.d(TAG, "Permission Granted : " + pack.packageName);
            }

            return grantedPacks;
        }
        else {

            List<PackageInfo> deniedPacks = pm.getInstalledPackages(PackageManager.PERMISSION_DENIED);
            Collections.sort(deniedPacks, new Comparator<PackageInfo>() {
                @Override
                public int compare(PackageInfo lhs, PackageInfo rhs) {
                    return lhs.packageName.compareTo(rhs.packageName);
                }
            });

            for (PackageInfo pack : deniedPacks) {
                LOG.d(TAG, "Permission Denied : " + pack.packageName);
            }

            return deniedPacks;
        }
    }

    /**
     * Get String value from 'meta-data' in AndroidManifest.xml <br>
     *     MAKE SURE that 'meta-data' should be declared in Manifest.xml file <br>
     *         <br> (example) <br>
     *             $lt;meta-data <br>
     *                 android:name="com.danielworld.utility.appID" <br>
     *                     android:value="@string/app_id" /&gt;
     * @param context
     * @return
     */
    public String getMetaDataValue(Context context){
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("com.danielworld.utility.appID");
        } catch (PackageManager.NameNotFoundException e) {
            LOG.e(TAG, "Make sure to set appID!!!");
        }
        return null;
    }

    /**
     * Get first google account
     * @param context
     * @return
     */
    public final Account getFirstGoogleAccount(Context context){
        Account[] accounts = getGoogleAccounts(context);
        if(accounts == null || accounts.length == 0)
            return null;
        return accounts[0];
    }

    /**
     * Get all google accounts in device
     * @param context
     * @return
     */
    private final Account[] getGoogleAccounts(Context context){
        AccountManager am = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        return am.getAccountsByType("com.google");
    }

}
