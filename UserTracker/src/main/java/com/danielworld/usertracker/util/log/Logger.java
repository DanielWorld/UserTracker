package com.danielworld.usertracker.util.log;

import android.util.Log;

/**
 * Created by daniel on 15. 10. 29.
 */
public class Logger {

    private static Logger sThis;

    public static final Logger getInstance() {
        if (sThis == null)
            sThis = new Logger();

        return sThis;
    }

    private boolean mLogFlag = true;

    /**
     * Log 활성화 <br>
     * Enable Log function
     */
    public void enableLog() {
        mLogFlag = true;
    }

    /**
     * Log 비활성화 <br>
     * Set disable Log function
     */
    public void disableLog() {
        mLogFlag = false;
    }

    public void v(String tag, String msg) {
        if (mLogFlag) {
            Log.v("" + tag, "" + msg);
        }
    }

    public void d(String tag, String msg) {
        if (mLogFlag) {
            Log.d("" + tag, "" + msg);
        }
    }

    public void e(String tag, String msg) {
        if (mLogFlag) {
            Log.e("" + tag, "" + msg);
        }
    }

    public void i(String tag, String msg) {
        if (mLogFlag) {
            Log.i("" + tag, "" + msg);
        }
    }

    public void w(String tag, String msg) {
        if (mLogFlag) {
            Log.w("" + tag, "" + msg);
        }
    }
}
