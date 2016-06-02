package iitb.mtp.aurobindo.findmytrain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import libsvm.svm;
import libsvm.svm_model;

public class MainActivity extends AppCompatActivity {

    ImageButton getResults;
    TextView tvResults;
    Spinner routeResults;
    String results="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream is;

        /***************** Initialize button, textView and Spinner ************/
        getResults = (ImageButton) findViewById(R.id.imgBtn_getResults);
        tvResults = (TextView) findViewById(R.id.tv_Result);
        routeResults = (Spinner) findViewById(R.id.spinner_Route);

        /******* Save SVM model for later Use ********/
        saveSvmModel();

        /******* Code for Query Button *********/
        getResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Fetching results from server...",Toast.LENGTH_SHORT).show();

                /***** Request current Train Locations as recently updated in server ****/
                results = new RequestHandler().sendPostRequest("http://10.129.28.209:808/FindYourTrain/Reporter","");
                tvResults.setText(results);
            }
        });

        /***** Initialise GSM 2 GPS database from csv file ******/

        /*************** Initializer **************/
        DatabaseHandler myDB = new DatabaseHandler(this);
        List<String> dataFile;
        /*** Insert to stationMap ***/
        is = getResources().openRawResource(R.raw.stn);
        insertToStationMap(is);

        /*** Insert to locationMAP ***/
        is = getResources().openRawResource(R.raw.data);
        dataFile = readCsvFile(is);

        myDB.insertLocationMap(dataFile);

    }

    @Override
    protected void onStart() {
        super.onStart();

        /************* Start Data Collection on Background service ***********/
        this.startService(new Intent(this, DataCollector.class));
        Toast.makeText(getApplicationContext(), "Data Collection Started on Background!!!", Toast.LENGTH_SHORT).show();
    }


    public void saveSvmModel()  {
        svm_model model;
        try {
            /************* Pass the Reader object of model file to svm_load_model() method of libsvm ************/
            InputStream is = getResources().getAssets().open("bustrain.model");
            BufferedReader modelReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            model = svm.svm_load_model(modelReader);
            /******** Save the svm model in a static object for further use ***********/
            SvmModel.model = model;
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error in SVM Model file loading!!!", Toast.LENGTH_SHORT).show();
            onDestroy();
        }
    }

    public List<String> readCsvFile(InputStream is) {
        List<String> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                resultList.add(csvLine);
            }
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(), "Error in database file loading!!!", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error in database file loading!!!", Toast.LENGTH_SHORT).show();
            }
        }
        return resultList;
    }

    public void insertToStationMap(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] stn = csvLine.split(",");
                StationMap.mapping.add(
                        new StationMap(
                                Integer.parseInt(stn[0]),
                                Integer.parseInt(stn[1]),
                                stn[2],
                                Double.parseDouble(stn[3]),
                                Double.parseDouble(stn[4]),
                                Double.parseDouble(stn[5]),
                                Double.parseDouble(stn[6])
                        )
                );
            }
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(), "Error in database file loading!!!", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error in database file loading!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
