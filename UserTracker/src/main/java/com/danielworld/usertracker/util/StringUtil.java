package com.danielworld.usertracker.util;

/**
 * String Utility <br>
 *     - Is String null or empty? <br>
 *     - Set null to empty <br>
 *     - Get extension of file or whatever <br>
 *
 * <br><br>
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class StringUtil {

    /**
     * If input String is null, then set to empty
     * @param str
     * @return
     */
    public static String setNullToEmpty(String str){

        if(str == null)
            return "";

        return str;
    }

    /**
     * Check if input String is null or ""
     * @param str
     * @return <code>true</code> if String is null or ""
     */
    public static boolean isNullorEmpty(String str){

        if(str == null || str.equals("") || str.length() == 0){
            return true;
        }
        else{
            // Make sure to check "     "
            if(str.trim().length() == 0)
                return true;
        }
        return false;
    }

    /**
     * Get extension of input String (filename)
     * @param filename
     * @return
     */
    public static String getExtension(String filename){

        if(!filename.contains(".") || isNullorEmpty(filename)) {
            return filename;
        }

        String[] pairs = filename.split(".");
        int lastIndex = pairs.length;

        return pairs[lastIndex];
    }

    /**
     * Check if <b>str</b> is null
     * @param str
     * @return <code>true</code> : str is null
     */
    public static boolean isNull(String str){
        if(str == null){
            return true;
        }
        return false;
    }
}