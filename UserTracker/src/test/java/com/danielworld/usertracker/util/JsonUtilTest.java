package com.danielworld.usertracker.util;

import com.danielworld.usertracker.model.TrackingModel;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 2015. 6. 29..
 */
public class JsonUtilTest {

    private String TAG = JsonUtilTest.class.getSimpleName();

    List<TrackingModel> list = new ArrayList<>();
    JsonUtil jsonUtil = new JsonUtil();

    @org.junit.Test
    public void testJsonUtil(){

        TrackingModel model = new TrackingModel();

        for(int i=0; i< 2; i++){
           model.putValuePair("key"+i, String.valueOf(i));
        }


        Assert.assertEquals(jsonUtil.toJson(model.getTrackingList()), "{\"key0\":\"0\",\"key1\":\"1\"}");
        Assert.assertEquals(jsonUtil.toJson(jsonUtil.fromJson(jsonUtil.toJson(model.getTrackingList())).getTrackingList()), "{\"key0\":\"0\",\"key1\":\"1\"}");

    }
}
