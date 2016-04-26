/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.udl.monitorizacion.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

/**********
 *
 **********/

/** An endpoint class we are exposing */
@Api(
  name = "locApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.monitorizacion.udl.com",
    ownerName = "backend.monitorizacion.udl.com",
    packagePath=""
  )
)
public class MyEndpoint {

    @ApiMethod(name = "storeLoc")
    public void storeLoc(Location loc) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Key locParentKey = KeyFactory.createKey("LocParent", "todo.txt");
            Entity locEntity = new Entity("Location", loc.getId(), locParentKey);
            locEntity.setProperty("date", loc.getDate());
            locEntity.setProperty("time", loc.getTime());
            locEntity.setProperty("longitude", loc.getLongitude());
            locEntity.setProperty("latitude", loc.getLatitude());

            datastoreService.put(locEntity);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @ApiMethod(name = "getLocs")
    public List<Location> getLocs() {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key locParentKey = KeyFactory.createKey("LocParent", "todo.txt");
        Query query = new Query(locParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
        ArrayList<Location> locs = new ArrayList<Location>();
        for (Entity result : results) {
            Location loc = new Location();
            loc.setId(result.getKey().getId());
            loc.setDate((String) result.getProperty("date"));
            loc.setTime((String) result.getProperty("time"));
            loc.setLongitude((Double) result.getProperty("longitude"));
            loc.setLatitude((Double) result.getProperty("latitude"));
            locs.add(loc);
        }

        return locs;
    }

    @ApiMethod(name = "clearLocs")
    public void clearLocs() {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Key locParentKey = KeyFactory.createKey("LocParent", "todo.txt");
            Query query = new Query(locParentKey);
            List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
            for (Entity result : results) {
                datastoreService.delete(result.getKey());
            }
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }


}


