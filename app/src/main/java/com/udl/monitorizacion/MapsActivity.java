package com.udl.monitorizacion;


import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng currentPos;
    private Cursor c;
    private TextView textviewdate;
    private String date;
    private SQLiteDatabase db;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        textviewdate = (TextView) findViewById(R.id.displayDate);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(calendar.getTime());
        textviewdate.setText(date);


        LocationsSQLiteHelper locdbh =
                new LocationsSQLiteHelper(this, "DBLocations", null, 2);
        db = locdbh.getWritableDatabase();
        c = db.rawQuery("SELECT * FROM Locations WHERE date LIKE '%" + date + "%'", null);
        mapFragment.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMap();
    }

    private void updateMap() {
        if (c.getCount() == 0) {
            Toast.makeText(this, "No hay datos para esta fecha", Toast.LENGTH_SHORT).show();
        } else {
            if (c.moveToFirst()) {
                currentPos = new LatLng(c.getDouble(3), c.getDouble(4));
                mMap.addCircle(new CircleOptions().center(currentPos).fillColor(Color.RED).strokeColor(Color.RED).radius(2));
                do {
                    addPosition(c);
                } while (c.moveToNext());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15));
                Toast.makeText(this, "Ruta actualizada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addPosition(Cursor c) {
        LatLng oldPos = currentPos;
        currentPos = new LatLng(c.getDouble(3), c.getDouble(4));
        mMap.addCircle(new CircleOptions().center(currentPos).fillColor(Color.RED).strokeColor(Color.RED).radius(2));
        mMap.addPolyline(new PolylineOptions().width(5).add(oldPos, currentPos));
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void getdb() {
        c = db.rawQuery("SELECT * FROM Locations WHERE date LIKE '%" + date + "%'", null);
        onMapReady(mMap);
    }

    public void RefreshRoute(View v) {
        date = textviewdate.getText().toString();
        mMap.clear();
        getdb();
    }

}

