package com.namgyuworld.usertracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.namgyuworld.usertracker.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 6/16/15.
 */
public class TrackingModel{

    private String key;
    private String value;

    // One tracking
    private List<TrackingModel> trackingList = new ArrayList<>();

    // Multiple tracking
    private List<List<TrackingModel>> allTrackingPackage = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get Tracking information
     * @return
     */
    public List<TrackingModel> getTrackingList(){
        return this.trackingList;
    }

    public TrackingModel() {
        if (trackingList == null)
            trackingList = new ArrayList<>();
        // Initialize list when TrackingModel is declared.
        trackingList.clear();
    }

    public TrackingModel(String key, String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return new JsonUtil().toJson(trackingList);
    }

    /**
     * Put info into Tracking List
     * @param name
     * @param value
     */
    public void putValuePair(String name, String value){
        if(trackingList == null)
            trackingList = new ArrayList<>();

        int foundIndex = -1;

        for(int i=0; i < trackingList.size(); i++){
            if(trackingList.get(i).getKey().equals(name)){
                // If input value pair exists in trackingList
                foundIndex = i;
            }
        }
        // If same key has found, remove old one and replace to new one
        if(foundIndex >= 0){
            trackingList.remove(foundIndex);
            this.trackingList.add(foundIndex, new TrackingModel(name, value));
        }
        else{
            // If same key wasn't found, then just add new one.
            this.trackingList.add(new TrackingModel(name, value));
        }
    }

    /**
     * Get value from trackingList using key
     * @param name
     * @return
     */
    public String getValuePair(String name){
        if(trackingList == null)
            throw new NoSuchElementException("Sorry, TrackingList is null");

        for(int i=0; i <trackingList.size(); i++){
            if(trackingList.get(i).getKey().equals(name)){
                return trackingList.get(i).getValue();
            }
        }
        return null;
    }

    /**
     * Tracking event type <br>
     *     FIRST_RUN : When application begins for the first time <br>
     *     FOREGROUND : When application is on the Foreground <br>
     *     BACKGROUND : When application is in the Background <br>
     *     ACTION : When application sends certain action
     */
    public enum TrackingEvent{
        FIRST_RUN, FOREGROUND, BACKGROUND, ACTION;

        public String toAcronymCode(){
            switch(this){
                case FIRST_RUN:
                    return "R";
                case FOREGROUND:
                    return "F";
                case BACKGROUND:
                    return "B";
                case ACTION:
                    return "A";
                default:
                    return null;
            }
        }

        public static TrackingEvent valueOfAcronym(String str){
            switch (str){
                case "R":
                    return TrackingEvent.FIRST_RUN;
                case "F":
                    return TrackingEvent.FOREGROUND;
                case "B":
                    return TrackingEvent.BACKGROUND;
                case "A":
                    return TrackingEvent.ACTION;
                default:
                    return null;
            }
        }
    }

}
