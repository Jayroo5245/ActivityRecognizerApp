package com.jaredsheehan.activityrecognizer.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by jaredsheehan on 12/15/14.
 */
public class ActivityRecognitionIntentService extends IntentService {

    private static String LOG_TAG = ActivityRecognitionIntentService.class.getSimpleName();

    public ActivityRecognitionIntentService(){
        super(ActivityRecognitionIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // If the incoming intent contains an update
        if (ActivityRecognitionResult.hasResult(intent)) {
            // Get the update
            ActivityRecognitionResult result =
                    ActivityRecognitionResult.extractResult(intent);
            // Get the most probable activity
            DetectedActivity mostProbableActivity =
                    result.getMostProbableActivity();
            /*
             * Get the probability that this activity is the
             * the user's actual activity
             */
            int confidence = mostProbableActivity.getConfidence();
            /*
             * Get an integer describing the type of activity
             */
            int activityType = mostProbableActivity.getType();
            final String activityName = getNameFromType(activityType);

            /*
             * At this point, you have retrieved all the information
             * for the current update. You can display this
             * information to the user in a notification, or
             * send it to an Activity or Service in a broadcast
             * Intent.
             */
            final String log = "ActivityRecognitionResult has result: activityName: " + activityName + " confidence: " + confidence;
            Log.d(LOG_TAG, log);
//            if(Configuration.DEBUG_ENABLED) {
//                Utils.UIPost(new Runnable() {
//                    public void run() {
//                        Toast.makeText(ApioApplication.getInstance(), log, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
        } else {
            /*
             * This implementation ignores intents that don't contain
             * an activity update. If you wish, you can report them as
             * errors.
             */
            Log.d(LOG_TAG, "ActivityRecognitionResult has no result");
        }
    }
    /**
     * Map detected activity types to strings
     *@param activityType The detected activity type
     *@return A user-readable name for the type
     */
    private String getNameFromType(int activityType) {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                String inVehicle = "in_vehicle";
//                if(Configuration.DEBUG_ENABLED) {
//                    ApioApplication.getInstance().getTts().speak(inVehicle, TextToSpeech.QUEUE_ADD, null);
//                }
                return inVehicle;
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                String onFoot = "on foot";
//                if(Configuration.DEBUG_ENABLED) {
//                    ApioApplication.getInstance().getTts().speak(onFoot, TextToSpeech.QUEUE_ADD, null);
//                }
                return onFoot;
            case DetectedActivity.RUNNING:
                String running = "running";
//                if(Configuration.DEBUG_ENABLED) {
//                    ApioApplication.getInstance().getTts().speak(running, TextToSpeech.QUEUE_ADD, null);
//                }
                return "running";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
            case DetectedActivity.WALKING:
                String walking = "walking";
//                if(Configuration.DEBUG_ENABLED) {
//                    ApioApplication.getInstance().getTts().speak(walking, TextToSpeech.QUEUE_ADD, null);
//                }
                return walking;
        }
        return "unknown";
    }

}