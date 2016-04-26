package com.udl.monitorizacion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udl.monitorizacion.backend.locApi.LocApi;
import com.udl.monitorizacion.backend.locApi.model.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.udl.monitorizacion.backendgcm.messaging.Messaging;


/**********
 *
 **********/
public class EndpointsLocationImpl {
    final LocApi locApiService;
    final Messaging msgService;


    public EndpointsLocationImpl() {
        LocApi.Builder builder = new LocApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);
        Messaging.Builder msgbuilder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl("https://monitorapp-3d62a.appspot.com/_ah/api/");

        this.locApiService = builder.build();
        this.msgService = msgbuilder.build();


    }


    public synchronized void pushToRemote(Context context) throws IOException {
        try {
            locApiService.clearLocs().execute();

            LocationsSQLiteHelper locdbh =
                    new LocationsSQLiteHelper(context, "DBLocations", null, 2);
            SQLiteDatabase db = locdbh.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM Locations", null);

            long id = 1;
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        Location loc = new Location();
                        loc.setId(id++);
                        loc.setDate(c.getString(1));
                        loc.setTime(c.getString(2));
                        loc.setLatitude(c.getDouble(3));
                        loc.setLongitude(c.getDouble(4));
                        locApiService.storeLoc(loc).execute();
                    } while (c.moveToNext());
                }
            }
            msgService.sendMessage("He compartido mi localizacion").execute();

        } catch (IOException e) {
            //Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
            Log.i("EndpointsLocationIMPL", "Error when storing locs", e);
            throw e;

        }
    }

    public synchronized void pullFromRemote(Context context) throws IOException {

        try {
            // Remote Call
            List<Location> remoteLocs = locApiService.getLocs().execute().getItems();

            if (remoteLocs != null) {
                ArrayList<Location> locList = new ArrayList<Location>();
                for (Location loc : remoteLocs) {
                    Location currLoc = new Location();
                    currLoc.setId(loc.getId());
                    currLoc.setDate(loc.getDate());
                    currLoc.setTime(loc.getTime());
                    currLoc.setLatitude(loc.getLatitude());
                    currLoc.setLongitude(loc.getLongitude());
                    locList.add(currLoc);
                }
                //REMPLAZAR BBDD LOCAL CON la REMOTA OBTENIDA
                LocationsSQLiteHelper locdbh =
                        new LocationsSQLiteHelper(context, "DBLocations", null, 2);
                SQLiteDatabase db = locdbh.getWritableDatabase();
                db.execSQL("DELETE FROM Locations");
                for(Location loc: locList){
                    db.execSQL("INSERT INTO Locations (date, time, latitude, longitude) VALUES ('"+loc.getDate()+"', '" +loc.getTime()+"', "+ loc.getLatitude() + ", " + loc.getLongitude() + ")");
                }

                //store(taskList);
                //reload();
                //lastSync = new Date();
            }

        } catch (IOException e) {
            //Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();

            Log.i("EndpointsLocationIMPL", "Error when loading locs", e);
            throw e;
        }
    }
}
