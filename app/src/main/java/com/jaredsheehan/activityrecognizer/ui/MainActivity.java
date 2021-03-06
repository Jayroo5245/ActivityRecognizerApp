package com.jaredsheehan.activityrecognizer.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.jaredsheehan.activityrecognizer.R;
import com.jaredsheehan.activityrecognizer.services.ActivityRecognitionIntentService;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int ACTIVITY_RECOGNITION_REQUEST_INTERVAL = 10000;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ActivityRecognitionClient mActivityRecognitionClient;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private boolean mIsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        GooglePlayServicesClient.ConnectionCallbacks connectionCallback = new GooglePlayServicesClient.ConnectionCallbacks(){
            @Override
            public void onConnected(Bundle bundle) {
                Log.d(LOG_TAG, "onConnected()");
                /*
                * Create the PendingIntent that Location Services uses
                * to send activity recognition updates back to this app.
                */
                Intent intent = new Intent(MainActivity.this, ActivityRecognitionIntentService.class);
                PendingIntent activityRecognitionPendingIntent =
                        PendingIntent.getService(MainActivity.this, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                mActivityRecognitionClient.requestActivityUpdates(ACTIVITY_RECOGNITION_REQUEST_INTERVAL, activityRecognitionPendingIntent);
            }

            @Override
            public void onDisconnected() {
                Log.d(LOG_TAG, "onDisconnected()");
            }

        };

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener(){

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                Log.d(LOG_TAG, "onConnectionFailed()");
            }
        };
        mActivityRecognitionClient =
                new ActivityRecognitionClient(this, connectionCallback, onConnectionFailedListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(mIsVisible == true && intent != null && intent.hasExtra(ActivityRecognitionIntentService.ACTIVITY_NAME_KEY)){
            onNewActivity(intent);
        }
    }

    private void onNewActivity(Intent intent){
        Log.d(LOG_TAG, "onNewActivity(Intent intent)");
        if(intent != null){
            String activityName = intent.getStringExtra(ActivityRecognitionIntentService.ACTIVITY_NAME_KEY);
            int confidence = intent.getIntExtra(ActivityRecognitionIntentService.ACTIVITY_CONFIDENCE_KEY, -1);
            final String log = "ActivityRecognitionResult has result: activityName: " + activityName + ": confidence: " + confidence;
            Log.d(LOG_TAG, "New Activity: " + log);
            Toast.makeText(this, log, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivityRecognitionClient.connect();
        mIsVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityRecognitionClient.disconnect();
        mIsVisible = false;
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
