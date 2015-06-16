package com.namgyuworld.usertracker.network;

import com.namgyuworld.usertracker.model.TrackingModel;
import com.namgyuworld.usertracker.util.Logger;

import java.util.List;

/**
 * Created by danielpark on 6/16/15.
 */
public class SendTrackingInfo {

    private final String TAG = SendTrackingInfo.class.getSimpleName();
    private final Logger LOG = Logger.getInstance();

    /**
     * Collect all information and transmit to server
     * @param trackingModel
     */
    public void transmit(final List<TrackingModel> trackingModel){
        LOG.i(TAG, trackingModel.toString());
    }
}
