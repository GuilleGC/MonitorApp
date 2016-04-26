package com.udl.monitorizacion;

import android.location.LocationManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**********
 *
 **********/
public class SharedPreferenceActivity extends PreferenceActivity {

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    Preference GPS;
    Preference NET;

    LocationManager locManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sharedpreferences);
        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        GPS = findPreference("GPS");
        NET = findPreference("NET");
        GPS.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                checkPreferenceClick(preference);
                return true;
            }
        });

        NET.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                checkPreferenceClick(preference);
                return true;
            }
        });

    }

    public void checkPreferenceClick(Preference preference) {
        String key = preference.getKey();

        CheckBoxPreference checkBox = null;

        Boolean enabled = false;
        if (key.contains("GPS")) {
            enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            checkBox = (CheckBoxPreference) findPreference("GPS");

        } else if (key.contains("NET")) {
            enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            checkBox = (CheckBoxPreference) findPreference("NET");

        }

        if (!enabled) {
            Toast.makeText(getBaseContext(), "ACTIVA TU PROVEEDOR DE " + key, Toast.LENGTH_LONG).show();
            checkBox.setChecked(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
