package com.danielworld.usertracker.util;

import com.danielworld.usertracker.util.log.Logger;
import com.danielworld.usertracker.model.TrackingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Convert List Object to Json or vice versa
 * <br><br.
 * Created by danielpark on 6/16/15.
 */
public class JsonUtil {

    private final String TAG = JsonUtil.class.getSimpleName();
    private Logger LOG = Logger.getInstance();

    /**
     * Convert only one List to Json
     * @param list
     * @return
     */
    public String toJson(List<TrackingModel> list){
        StringBuilder sb = new StringBuilder();
        return toJson(sb, list).toString();
    }

    private StringBuilder toJson(StringBuilder sb, List<TrackingModel> list){
        sb.append("{");

        for(int i=0; i < list.size(); i++){
            sb.append("\"" + list.get(i).getKey() + "\"");
            sb.append(":");

//            if(list.get(i).getValue() instanceof String){
            sb.append("\"" + list.get(i).getValue() + "\"");
//            }
            sb.append(",");
        }
        sb.delete(sb.lastIndexOf(","), sb.length() );
        sb.append("}");
        return sb;
    }

    /**
     * Convert String json data to Java Object
     * @param jsonData
     * @return
     */
    public TrackingModel fromJson(String jsonData){
        TrackingModel javaObject = new TrackingModel();

        String result1 = jsonData.replace("{", "").replace("}", "");

        List<String> trackingInfo = new ArrayList<>();

        for(String i : result1.split(",")){
            trackingInfo.add(i);
        }

        LOG.i(TAG, "tracking info count: " + trackingInfo.size());

        List<String> keyAndValue = new ArrayList<>();

        for(int i=0; i < trackingInfo.size(); i++){

            for(String j : trackingInfo.get(i).split(":")){
                keyAndValue.add(j);
            }
            try {
                javaObject.putValuePair(keyAndValue.get(0).replace("\"", ""), keyAndValue.get(1).replace("\"", ""));
            }catch (Exception e){
                e.printStackTrace();
            }
            keyAndValue.clear();
        }

        LOG.e(TAG, javaObject.toString());

        return javaObject;
    }


    /**
     * Convert multiple List to Json
     * @param list
     * @return
     */
    public String toJsonList(List<List<TrackingModel>> list){
        StringBuilder aa = new StringBuilder();
        aa.append("[");

        for(int i=0; i< list.size(); i++){
            StringBuilder sb = new StringBuilder();
            aa.append(toJson(sb, list.get(i)));
            aa.append(",");
        }
        aa.delete(aa.lastIndexOf(","), aa.length());

        aa.append("]");

        return aa.toString();
    }
}
