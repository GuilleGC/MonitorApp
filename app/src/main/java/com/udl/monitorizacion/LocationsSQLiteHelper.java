package com.udl.monitorizacion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**********
 *
 **********/
public class LocationsSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Locations " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " date DATE DEFAULT CURRENT_DATE, " +
            " time TIME DEFAULT CURRENT_TIME, " +
            " latitude DOUBLE NOT NULL, " +
            " longitude DOUBLE NOT NULL)";

    public LocationsSQLiteHelper(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory,
                                 int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(("DROP TABLE IF EXISTS Locations"));
        db.execSQL(sqlCreate);
    }
}
