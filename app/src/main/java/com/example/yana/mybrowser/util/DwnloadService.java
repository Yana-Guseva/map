package com.example.yana.mybrowser.util;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.TimeUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Yana on 13.07.2015.
 */
public class DwnloadService extends IntentService {

    public final static int NUM = 111;
    public final static String RECIVER = "Reciver";

    public DwnloadService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // this is background thread
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra(RECIVER);
//this is REST place
        Bundle resultData = new Bundle();
        resultData.putInt("int", 10);
        receiver.send(NUM, resultData);
    }
}
