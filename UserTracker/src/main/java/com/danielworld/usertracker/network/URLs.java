package com.danielworld.usertracker.network;

import java.util.Locale;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p>
 * This file is part of Android (https://github.com/DanielWorld)
 * Created by danielpark on 2015. 7. 23..
 */
public class URLs {

    private static boolean isDebug;

    /**
     * Set debug mode or not
     * @param flag
     */
    public static void setDebug(boolean flag){
        isDebug = flag;
    }

    private static final String URL_HOST = "http://192.168.0.2:8080";
    private static final String URL_DEV = "http://192.168.0.2:8080";

    /**
     * Method
     */
    private static final String FORMAT_SEND_TRACKING = "%s/tracking/receive_data";
    private static final String FORMAT_CHECK_FLAG = "%s/check_flag";

    public enum ConnectionMethod{
        /* Send tracking to Server */
        SEND_TRACKING,
        /* Check flag in server to proceed tracking or not */
        CHECK_FLAG
    }
    /**
     * Get url to send tracking data to Server
     * @return
     */
    public static String getURL(ConnectionMethod method){
        if(method == null){
            throw new IllegalArgumentException("method can't be null.");
        }

        // Get Host url
        final String hostURL = isDebug ? URL_DEV : URL_HOST;

        switch(method){
            case SEND_TRACKING:
                return String.format(Locale.US, FORMAT_SEND_TRACKING, hostURL);
            case CHECK_FLAG:
                return String.format(Locale.US, FORMAT_CHECK_FLAG, hostURL);
            default:
                return null;
        }
    }
}
