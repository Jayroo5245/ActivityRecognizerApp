package activityrecognizer.jaredsheehan.com.activityrecognizerapp;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;

import activityrecognizer.jaredsheehan.com.activityrecognizerapp.services.ActivityRecognitionIntentService;

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

        ConnectionCallbacks connectionCallback = new ConnectionCallbacks(){
            @Override
            public void onConnected(Bundle bundle) {
                Log.d(LOG_TAG, "onConnected()");
                /*
                * Create the PendingIntent that Location Services uses
                * to send activity recognition updates back to this app.
                */
                Intent intent = new Intent(sInstance, ActivityRecognitionIntentService.class);
                PendingIntent activityRecognitionPendingIntent =
                        PendingIntent.getService(sInstance, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                mActivityRecognitionClient.requestActivityUpdates(ACTIVITY_RECOGNITION_REQUEST_INTERVAL, activityRecognitionPendingIntent);
            }

            @Override
            public void onDisconnected() {
                Log.d(LOG_TAG, "onDisconnected()");
            }

        };

        OnConnectionFailedListener onConnectionFailedListener = new OnConnectionFailedListener(){

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                Log.d(LOG_TAG, "onConnectionFailed()");
            }
        };
        mActivityRecognitionClient =
                new ActivityRecognitionClient(this, connectionCallback, onConnectionFailedListener);
        mActivityRecognitionClient.connect();

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
