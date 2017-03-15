package com.lejeune.david.ardovlamdocumentlibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class StatsActivity extends Activity {

    //region Declaration
    SQLiteDatabase mydb;
    public static Context cntx;
    Button btnShowGrap ;
    ImageButton btnUpdatesStat;
    MyStats myStats;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //region Initialisations
        cntx = getApplicationContext();

        myStats = new MyStats();
        MyStats.resetStatVars();
        //endregion

        //region Stat initialisations
        // if no year selected, get current year
        if (MyVars.filterStatYear.equalsIgnoreCase("")){
            System.out.println("current year");
            MyTools.setStatCurrentYear();
        }
        // if no stat type selected, select ENTRY
        if (MyVars.filterStatType.equalsIgnoreCase("")){
            MyVars.filterStatType = "ENTRY";
        }
        if (MyVars.filterStatUser==null){
            MyVars.filterStatUser = "";
        }
        //endregion

        // old btnShowGraph
        btnShowGrap = (Button) findViewById(R.id.btnShowGraph);
        btnShowGrap.setVisibility(View.INVISIBLE);

        //region Create the stats for the selected year
        new AsyncCreateStatForYearDL().execute();
        //endregion

    }

    private void gotoGraph(){
        final Intent graphIntent = new Intent(StatsActivity.this, GraphActivity.class);
        StatsActivity.this.startActivity(graphIntent);
        StatsActivity.this.finish();
    }

    //region Internal Async tasks

    public class AsyncCreateStatForYearDL extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = ProgressDialog.show(StatsActivity.this, "", "Creating single stat for this year..",
                    true, false);

            myStats.createMonthArrays();
            myStats.createMonthHourArrays();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pd.dismiss();
            gotoGraph();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected String doInBackground(String... params) {
            MyStats.filterBigStatFiles(MyVars.filterStatYear, MyVars.filterStatType);

            return null;
        }

    }
    //endregion

}
