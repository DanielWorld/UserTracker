package com.danielworld.usertracker.network;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
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

    private static final String URL_HOST = "http://114.205.150.83:8080/spring/userTracker/trackingInfo";
    private static final String URL_DEV = "http://114.205.150.83:8080/spring/userTracker/trackingInfo";

    /**
     * Get url to send tracking data to Server
     * @return
     */
    public static String getURL(){
        if(isDebug)
            return URL_DEV;
        else
            return URL_HOST;
    }

}


