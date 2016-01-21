package com.danielworld.usertracker.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * String Utility <br>
 * <br><br>
 * Created by daniel on 15. 10. 29.
 */
public class StringUtil {

    private StringUtil(){}

    private static StringUtil sThis;

    public static final StringUtil getInstance(){  // Singleton pattern default lazy-loading (Not Thread-safe)

        if(sThis == null)
            sThis = new StringUtil();

        return sThis;
    }

    /**
     * Check if input value(String) is null or emtpy
     * @param str
     * @return <code>true</code> if input value is null or ""
     */
    public boolean isNullorEmpty(String str){

        // Checking null should be first, or you will get throw exception
        if(str == null || str.trim().equals("") || str.trim().length() == 0){
            return true;
        }
        return false;
    }

    /**
     * If input is NUll then convert it to empty String
     * @param str
     * @return
     */
    public String setNullToEmpty(String str){
        // Checking null should be first, or you will get throw exception
        if(str == null || str.trim().equals("") || str.trim().length() == 0){
            return "";
        }
        return str;
    }

    /**
     * Return String which only consists of alphabets
     * <br>
     * e.g) "03de8283ffe&*&" -> "deffe"
     * @param word
     * @return
     */
    public String getOnlyAlphabets(String word){

        // Check if input is null or empty
        if(StringUtil.getInstance().isNullorEmpty(word))
            return word;

        String changedWord = "";

        String[] a = word.split("");
        List<String> newVoca = new ArrayList<String>();
        for(String str : a){
            if(Pattern.matches("[a-zA-z]+", str)){
                newVoca.add(str);
            }
        }

        // List to String
        for(String s : newVoca)
            changedWord += 2;

        return changedWord;
    }
}
