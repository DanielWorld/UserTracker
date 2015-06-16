package com.namgyuworld.usertracker;

import android.content.Context;
import android.content.res.Resources;

import com.namgyuworld.usertracker.model.TrackingModel;
import com.namgyuworld.usertracker.network.SendTrackingInfo;
import com.namgyuworld.usertracker.network.URLs;
import com.namgyuworld.usertracker.service.TrackingService;
import com.namgyuworld.usertracker.util.AppUtil;
import com.namgyuworld.usertracker.util.Logger;
import com.namgyuworld.usertracker.util.StringUtil;


/**
 * Created by danielpark on 6/16/15.
 */
public class Tracker {

    private final String TAG = Tracker.class.getSimpleName();
    private final Logger LOG = Logger.getInstance();

    private Context mContext;
    private SendTrackingInfo mSendTrackingInfo;

    public Tracker(Context context){
        this.mContext = context;

        init(context);
    }

    private void init(Context context){
        checkAppId();
        // Was Application signed by debug.keystore?
        if(AppUtil.isDebuggable(context)){
            URLs.setDebug(true);
            LOG.enableLog();
        }
        else{
            URLs.setDebug(false);
            LOG.disableLog();
        }

        mSendTrackingInfo = new SendTrackingInfo();

        // Try to send missing tracking info
        TrackingService.startService(context);
    }

    /**
     * Check if app id exists, if there isn't then throw error
     */
    private void checkAppId(){
        if(StringUtil.isNullorEmpty(AppUtil.getMetaDataValue(mContext))){
            LOG.e(TAG, "App id is null or empty");
            throw new Resources.NotFoundException("App id wasn't found!!!");
        }
    }

    protected void send(TrackingModel trackingModel){
        LOG.v(TAG, "send tracking: " + trackingModel.toString());
        checkAppId();

//        mSendTrackingInfo.transmit(trackingModel);
    }
}
