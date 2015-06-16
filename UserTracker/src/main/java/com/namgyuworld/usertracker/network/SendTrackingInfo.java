package com.namgyuworld.usertracker.network;

import android.content.Context;

import com.namgyuworld.usertracker.database.SQLiteHelper;
import com.namgyuworld.usertracker.model.TrackingModel;
import com.namgyuworld.usertracker.preference.SharePref;
import com.namgyuworld.usertracker.util.Logger;
import com.namgyuworld.usertracker.util.StringUtil;
import com.namgyuworld.usertracker.util.cryptography.CryptoUtil;
import com.namgyuworld.usertracker.util.google.adId.GoogleAdvertisingIdClient;
import com.namgyuworld.usertracker.util.google.adId.OnAdvertisingIDcompleteListener;
import com.namgyuworld.usertracker.variables.TrackingMapKey;

import java.util.List;

/**
 * Created by danielpark on 6/16/15.
 */
public class SendTrackingInfo {

    private final String TAG = SendTrackingInfo.class.getSimpleName();
    private final Logger LOG = Logger.getInstance();

    private Context mContext;
    private SharePref mPref;

    private SQLiteHelper mDatabase;

    public SendTrackingInfo(Context context) {
        this.mContext = context;
        this.mPref = new SharePref(context);
        this.mDatabase = new SQLiteHelper(context);
    }

    /**
     * Collect all information and transmit to server
     *
     * @param trackingModel
     */
    public void transmit(final TrackingModel trackingModel) {

        // Get google advertising id
        GoogleAdvertisingIdClient.getGoogleAdID(mContext, new OnAdvertisingIDcompleteListener() {
            @Override
            public void onAdvertisingIdComplete(String adId) {
                String advertisementID = adId;
                if (StringUtil.isNullorEmpty(adId)) {
                    advertisementID = mPref.getGoogleAdId();
                } else {
                    mPref.setGoogleAdId(adId);
                }
                // Do some stuff
                trackingModel.putValuePair(TrackingMapKey.GOOGLE_AD_ID, advertisementID);
                LOG.i(TAG, trackingModel.toString());
            }
        });
    }

    private void handleTracking(final TrackingModel trackingModel) {
        try {
            if (trackingModel.getValuePair(TrackingMapKey.TRACKING_TYPE).equals(TrackingModel.TrackingType.TRACKING.toAcronymCode())) {
                // This is tracking option
            }
            else if (trackingModel.getValuePair(TrackingMapKey.TRACKING_TYPE).equals(TrackingModel.TrackingType.IMMEDIATELY.toAcronymCode())) {
                // This is Immediately option
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    // Save in temporary database
    private void saveTemporary(final TrackingModel trackingModel){
        mDatabase.putTemporary(trackingModel.getTrackingList());
    }

    // Send Tracking immediately
    private void sendTracking(final TrackingModel trackingModel){
        //..
    }
}
