package com.example.yana.mybrowser.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.example.yana.mybrowser.MainActivity;

/**
 * Created by Yana on 13.07.2015.
 */
public class DownloadReciver extends ResultReceiver {

    MainActivity activity;

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        //this is UI thread
        if(resultCode == DwnloadService.NUM) {
            resultData.getInt("int", -1);
//            activity.showToast();
        }
    }

    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public DownloadReciver(Handler handler) {
        super(handler);
    }
}
