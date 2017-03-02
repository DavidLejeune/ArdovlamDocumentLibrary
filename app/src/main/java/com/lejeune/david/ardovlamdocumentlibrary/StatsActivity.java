package com.lejeune.david.ardovlamdocumentlibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class StatsActivity extends Activity {

    SQLiteDatabase mydb;
    public static Context cntx;
    Button btnShowGrap ;
    ImageButton btnUpdatesStat;
    MyStats myStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        System.out.println("Stats activity");

        cntx = getApplicationContext();



        myStats = new MyStats();
        MyStats.resetStatVars();

        System.out.println("this is the year : " + MyVars.filterStatYear);
        if (MyVars.filterStatYear.equalsIgnoreCase("")){
            System.out.println("current year");
            MyTools.setStatCurrentYear();
        }


        if (MyVars.filterStatType.equalsIgnoreCase("")){
            MyVars.filterStatType = "ENTRY";
        }
//        if (MyVars.filterStatType.equalsIgnoreCase("")){
//            System.out.println("current year");
//            MyTools.setStatCurrentYear();
//        }



        btnShowGrap = (Button) findViewById(R.id.btnShowGraph);
        btnShowGrap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Showing a graph");

                myStats.createMonthArrays();
                myStats.createMonthHourArrays();
                //MyVars.filterStatType = "ENTRY";
                MyStats.filterBigStatFiles(MyVars.filterStatYear, MyVars.filterStatType);
                gotoGraph();

            }
        });

//        btnUpdatesStat = (ImageButton) findViewById(R.id.btnUpdatesStat);
//        btnUpdatesStat.setImageDrawable(getResources().getDrawable(R.drawable.update));
//        btnUpdatesStat.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                System.out.println("Starting stat update procedure");
//
//            }
//        });



    }

    private void gotoGraph(){
        final Intent graphIntent = new Intent(StatsActivity.this, GraphActivity.class);
        StatsActivity.this.startActivity(graphIntent);
    }




}
