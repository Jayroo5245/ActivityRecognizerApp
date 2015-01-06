package com.jaredsheehan.activityrecognizer;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionClient;

/**
 * Created by jaredsheehan on 1/6/15.
 */
public class RecognitionApplication extends Application {
    private static String LOG_TAG = RecognitionApplication.class.getSimpleName();
    private static final int ACTIVITY_RECOGNITION_REQUEST_INTERVAL = 20000;
    private static RecognitionApplication sInstance = null;
    private ActivityRecognitionClient mActivityRecognitionClient;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(LOG_TAG, "OnCreate()");
        sInstance = this;
    }

    public static RecognitionApplication getInstance(){
        return sInstance;
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        Log.d(LOG_TAG,"onTerminate()");
    }


}
