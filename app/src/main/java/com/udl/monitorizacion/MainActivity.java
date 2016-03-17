package com.udl.monitorizacion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**********
 *
 **********/
public class MainActivity extends Activity {

    private boolean gps;
    private boolean net;
    private boolean locationService;
    private SharedPreferences prefs;
    private EditText editTextGPS;
    private EditText editTextNET;
    private TextView textViewInfo;
    private ToggleButton toggleButtonService;

    private LocationManager locationManager;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gps = prefs.getBoolean("GPS", false);
        net = prefs.getBoolean("NET", false);
        setContentView(R.layout.drawer_layout);
        // setContentView(R.layout.main_activity);
        editTextGPS = (EditText) findViewById(R.id.editTextGPS);
        editTextNET = (EditText) findViewById(R.id.editTextNET);
        textViewInfo = (TextView) findViewById(R.id.textViewInfo);

        mDrawerList = (ListView) findViewById(R.id.navList);

        addDrawerItems();

        toggleButtonService = (ToggleButton) findViewById(R.id.toggleButtonService);

        toggleButtonService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButtonService.isChecked() && (
                        (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && gps) || (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && net)
                )) {
                    startService();
                    prefs.edit().putBoolean("LocationService", true).commit();
                } else {
                    Toast.makeText(getApplication(), "Porfavor active el localizador de su dispositivo", Toast.LENGTH_LONG).show();
                    toggleButtonService.setChecked(false);
                    stopService();
                    prefs.edit().putBoolean("LocationService", false).commit();

                }
            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, SharedPreferenceActivity.class);
                        break;

                    case 1:
                        intent = new Intent(MainActivity.this, MapsActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, MonitorActivity.class);
                        break;
                }
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, false,
                R.drawable.icon_settings, R.string.drawer_close, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (!gps && !net) { //Go To SharedPreferences
            Intent i = new Intent(this, SharedPreferenceActivity.class);
            startActivity(i);

        }

    }


    private void addDrawerItems() {
        String[] array = getResources().getStringArray(R.array.DrawerMenu);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent i = new Intent(this, SharedPreferenceActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gps = prefs.getBoolean("GPS", false);
        net = prefs.getBoolean("NET", false);
        locationService = prefs.getBoolean("LocationService", false);


        if (locationService) {
            toggleButtonService.setChecked(true);
        } else {
            toggleButtonService.setChecked(false);
        }

        Boolean gps_on = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean net_on = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps && gps_on) {
            editTextGPS.setBackgroundColor(Color.GREEN);
        } else {
            editTextGPS.setBackgroundColor(Color.RED);
        }

        if (net && net_on) {
            editTextNET.setBackgroundColor(Color.GREEN);
        } else {
            editTextNET.setBackgroundColor(Color.RED);
        }
        super.onResume();
    }

    public void startService() {
        startService(new Intent(this, LocationService.class));

    }

    public void stopService() {
        stopService(new Intent(this, LocationService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDrawerLayout.closeDrawers();
    }

}
