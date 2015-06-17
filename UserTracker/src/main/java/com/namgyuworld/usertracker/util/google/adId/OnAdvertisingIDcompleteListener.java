package com.namgyuworld.usertracker.util.google.adId;

/**
 * It invokes when Google Advertising ID has got received
 * <br><br>
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p/>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by Daniel Park on 2015-06-16.
 */
public interface OnAdvertisingIDcompleteListener {

    /**
     * Getting Google advertising ID has completed
     * @param adId advertising id
     */
    void onAdvertisingIdComplete(String adId);
}

