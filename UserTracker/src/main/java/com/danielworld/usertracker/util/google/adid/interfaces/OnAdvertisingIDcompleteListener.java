package com.danielworld.usertracker.util.google.adid.interfaces;

/**
 * It invokes when Google Advertising ID has got received
 * <br><br>
 * Created by Daniel Park on 2015-04-17.
 */
public interface OnAdvertisingIDcompleteListener {

    /**
     * Getting Google advertising ID has completed
     * @param adId advertising id
     */
    void onAdvertisingIdComplete(String adId);
}
