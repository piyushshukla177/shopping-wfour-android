package com.wfour.onlinestoreapp.utils.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

import com.wfour.onlinestoreapp.globals.Constants;

/**
 * Created by Suusoft on 05/13/2016.
 */
@SuppressLint("ParcelCreator")
public class AddressResultReceiver extends ResultReceiver {

    private IMaps iMaps;

    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public AddressResultReceiver(Handler handler, IMaps iMaps) {
        super(handler);
        this.iMaps = iMaps;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        // Display the address string
        // or an error message sent from the intent service.
        String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
        iMaps.processFinished(mAddressOutput);

        // Show a toast message if an address was found.
        if (resultCode == Constants.SUCCESS_RESULT) {
//                Toast.makeText(self, mAddressOutput, Toast.LENGTH_SHORT).show();
        }

    }
}
