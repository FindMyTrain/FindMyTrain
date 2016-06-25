package iitb.mtp.aurobindo.findmytrain;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String dbName = "locationDB", locationMAP = "locationMAP", stationMap = "stationMap";

    InputStream is = null;
    String TAG = "DatabaseHandler";
    List<String> dataFile;
    Context cntxt;
    public DatabaseHandler(Context context) {
        super(context, dbName, null, 25);
        cntxt = context;
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
        dataFile = readCsvFile(cntxt.getResources().openRawResource(R.raw.data));
        insertLocationMap(dataFile, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + locationMAP;
        db.execSQL(sql);
        onCreate(db);
    }


    /********** Insert GSM to GPS map from csv file to LocationDB database **********/
    public void insertLocationMap(List<String> dataFile, SQLiteDatabase db)   {
        ContentValues cv;
        for (String line: dataFile) {
            String[] d = line.split(",");
            cv = new ContentValues();
            cv.put("routeID",Integer.parseInt(d[0]));
            cv.put("operator",d[1]);
            cv.put("cellID",Integer.parseInt(d[2]));
            cv.put("RSSI",Integer.parseInt(d[3]));
            cv.put("lat",Double.parseDouble(d[4]));
            cv.put("lon", Double.parseDouble(d[5]));
            db.insert(locationMAP, null, cv);
        }
        Log.d(TAG, "insertLocationMap: Added data to db!!!");
    }


    /*********** Read the LocationMap table and return an ArrayList of results ***********/
    public ArrayList<ArrayList<String>> readLocationMap(String sql)    {
        ArrayList<ArrayList<String>> locationMapData = new ArrayList<>();
        ArrayList<String> read;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if(c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                read = new ArrayList<>();
                read.add(c.getString(0));
                read.add(c.getString(1));
                read.add(c.getString(2));
                read.add(c.getString(3));
                read.add(c.getString(4));
                read.add(c.getString(5));
                locationMapData.add(read);
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return locationMapData;
    }


    /******************* Read a csv file in to a List of String ******************/
    public List<String> readCsvFile(InputStream is) {
        List<String> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                resultList.add(csvLine);
            }
        } catch (IOException ex) {
            Log.d(TAG, "readCsvFile: "+"Error in database file loading!!!");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.d(TAG, "readCsvFile: "+"Error in closing database file loading!!!");
            }
        }
        return resultList;
    }
}


