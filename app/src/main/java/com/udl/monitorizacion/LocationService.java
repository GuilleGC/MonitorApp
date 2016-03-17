package com.udl.monitorizacion;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**********
 *
 **********/
public class LocationService extends Service {
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    private SharedPreferences prefs;
    private boolean gps_status;
    private boolean net_status;

    private SQLiteDatabase db;
    //Intent intent;
    //int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        LocationsSQLiteHelper locdbh = new LocationsSQLiteHelper(this, "DBLocations", null, 2);
        db = locdbh.getWritableDatabase();

        //intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gps_status = prefs.getBoolean("GPS", false);
        net_status = prefs.getBoolean("NET", false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && gps_status) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 25, listener);
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && net_status) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 25, listener);
        }
    }


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            //Log.i("**************************************", "Location changed");
            if (isBetterLocation(loc, previousBestLocation)) {
                Double latitude = loc.getLatitude();
                Double longitude = loc.getLongitude();

                Log.i("******************", "Location changed" + String.valueOf(latitude) + " " + String.valueOf(longitude) + " PROVEEDOR: " + loc.getProvider());

                db.execSQL("INSERT INTO Locations (latitude, longitude) VALUES (" + latitude + ", " + longitude + ")");


            }
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
            prefs.edit().putBoolean("LocationService", false).commit();
            locationManager.removeUpdates(listener);
            stopSelf();
        }


        public void onProviderEnabled(String provider) {
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}