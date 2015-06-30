package com.danielworld.usertracker.util;

import android.util.Log;

/**
 * The package of Log.v / Log.d / Log.i / Log.w / Log.e
 * <br><br>
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class Logger {

    private static Logger sThis;

    public static Logger getInstance() {
        if (sThis == null)
            sThis = new Logger();

        return sThis;
    }

    private boolean mLogFlag;

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
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.v(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.v(tag, msg);
            }
        }
    }

    public void d(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.d(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.d(tag, msg);
            }
        }
    }

    public void e(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.e(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.e(tag, msg);
            }
        }
    }

    public void i(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.i(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.i(tag, msg);
            }
        }
    }

    public void w(String tag, String msg) {
        if (mLogFlag) {
            if(StringUtil.isNull(tag) || StringUtil.isNull(msg)){
                Log.w(StringUtil.setNullToEmpty(tag), StringUtil.setNullToEmpty(msg));
            }
            else {
                Log.w(tag, msg);
            }
        }
    }
}