package iitb.mtp.aurobindo.findmytrain;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String dbName = "locationDB", locationMAP = "locationMAP";

    public DatabaseHandler(Context context) {
        super(context, dbName, null, 1);
    }

    /************* Create Table in Database **************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + locationMAP + " " +
                "( " +
                "routeID INTEGER NOT NULL, " +
                "operator TEXT NOT NULL, " +
                "cellID INTEGER NOT NULL, " +
                "RSSI INTEGER NOT NULL, " +
                "lat DOUBLE NOT NULL, " +
                "lon DOUBLE NOT NULL" +
                " )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + locationMAP;
        db.execSQL(sql);
        onCreate(db);
    }


    /********** Insert GSM to GPS map from csv file to LocationDB database **********/
    public void insertLocationMap(List<String> dataFile)   {
        ContentValues cv;
        SQLiteDatabase db = this.getWritableDatabase();
        for (String line: dataFile) {
            String[] d = line.split(",");
            cv = new ContentValues();
            cv.put("routeID",d[0]);
            cv.put("operator",d[1]);
            cv.put("cellID",d[2]);
            cv.put("RSSI",d[3]);
            cv.put("lat",d[4]);
            cv.put("lon",d[5]);
            db.insert(locationMAP, null, cv);
        }
    }


    /*********** Read the LocationMap table and return an ArrayList of results ***********/
    public ArrayList<ArrayList<String>> readLocationMap(String sql)    {
        ArrayList<ArrayList<String>> locationMapData = new ArrayList<>();
        ArrayList<String> read;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        if(c.isAfterLast() == false) {
            read = new ArrayList<>();
            read.add(c.getString(0));
            read.add(c.getString(1));
            read.add(c.getString(2));
            read.add(c.getString(3));
            read.add(c.getString(4));
            read.add(c.getString(5));
            locationMapData.add(read);
        }
        c.close();
        return locationMapData;
    }
}
