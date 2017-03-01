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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StatsActivity extends Activity {

    SQLiteDatabase mydb;
    public static Context cntx;
    Button btnShowGrap , btnUpdatesStat;
    MyStats myStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        System.out.println("Stats activity");

        cntx = getApplicationContext();
        new AsyncStatsDownloadDL().execute();

        myStats = new MyStats();
        MyVars.filterStatType = "";
        System.out.println("this is the year : " + MyVars.filterStatType);
        if (MyVars.filterStatType.equalsIgnoreCase("")){
            System.out.println("current year");
            MyTools.setStatCurrentYear();
        }

        btnShowGrap = (Button) findViewById(R.id.btnShowGraph);
        btnShowGrap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Showing a graph");

                myStats.createMonthArrays();

                MyVars.filterStatType = "ENTRY";
                MyStats.filterBigStatFiles(MyVars.filterStatYear, MyVars.filterStatType);

                gotoGraph();
            }
        });

//        btnUpdatesStat = (ImageButton) findViewById(R.id.btnUpdatesStat);
//        btnUpdatesStat.setImageDrawable(getResources().getDrawable(R.drawable.update));
//        btnUpdatesStat.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                System.out.println("Starting stat update procedure");
//                updateProcedure();
//            }
//        });



    }

    private void gotoGraph(){
            final Intent graphIntent = new Intent(StatsActivity.this, GraphActivity.class);
            StatsActivity.this.startActivity(graphIntent);
    }


    public class AsyncStatsDownloadDL extends AsyncTask<String, String, String> {


        private FTPfunctions ftpclient = null;
        ProgressDialog pd;

        //DatabaseHelperStats helper;
        MyTimer myTimer;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            myTimer = new MyTimer();
            pd = ProgressDialog.show(StatsActivity.this, "", "Downloading stat files...",
                    true, false);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            helper = new DatabaseHelperStats(cntx);
//            helper.DATABASE_VERSION = helper.DATABASE_VERSION + 1;
//            saveStatFiles();
            pd.dismiss();
            myTimer.getElapsedTime();
            new AsyncBigStatsCreateDL().execute();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            ftpclient = new FTPfunctions();
            ftpclient.ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
            boolean status = false;
            status = ftpclient.downloadStatsFiles(MyVars.FOLDER_STATS);
            System.out.println(MyVars.FOLDER_STATS + " status : " + status);
            if (status == true) {
                System.out.println("Download success");
//                    handler.sendEmptyMessage(5);
            } else {
                System.out.println("Download failed");
//                    handler.sendEmptyMessage(-1);
            }

            ftpclient.ftpDisconnect();
            return null;
        }




//
//        private void insertDataSQLite(String user_name , String date, String time, String type, String extra){
//
//            boolean isInserted = helper.insertData(user_name , date, time, type, extra);
//            if (isInserted){
//                //System.out.println("data inserted");
//            }
//            else
//            {
//                //System.out.println("data NOT inserted");
//            }
//
//        }

//        private void viewAllDataSQLite(){
//            Cursor cursor = helper.getAllData();
//            if(cursor.getCount() == 0){
//                System.out.println("no results");
//                //showMessage("Error", "No data in SQLite");
//                return;
//            }
//            else
//            {
//                StringBuffer buffer = new StringBuffer();
//                while(cursor.moveToNext()){
//                    buffer.append("id : " + cursor.getString(0) + "\n");
//                    buffer.append("user : " + cursor.getString(1) + "\n");
//                    buffer.append("date : " + cursor.getString(2) + "\n");
//                    buffer.append("time : " + cursor.getString(3) + "\n\n");
//                    buffer.append("type : " + cursor.getString(4) + "\n\n");
//                    buffer.append("extra : " + cursor.getString(5) + "\n\n");
//                    System.out.println(buffer);
//                }
//
//                //showMessage("Data", buffer.toString());
//
//            }
//        }

//    public void showMessage(String title, String message){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        builder.show();
//
//    }


//        private void saveStatFiles(){
//            File dir = Environment.getExternalStorageDirectory();
//            File file = new File(dir, MyVars.FOLDER_STATS + "David Lejeune.txt");
//            String line = "";
//            int iCount=0;
//            if (file.exists()) {
//
//                StringBuilder text = new StringBuilder();
//                try {
//                    BufferedReader br = new BufferedReader(new FileReader(file));
//                    while ((line = br.readLine()) != null) {
//                        if (line.length() > 0) {
//                            {
//                                String[] str = line.split(",");
//                                iCount+=1;
//                                System.out.println("count entry " + iCount);
//                                //String strID = str[0].toLowerCase();
//                                String strUser = str[0].toLowerCase();
//                                String strDate = str[1].toLowerCase();
//                                String strTime = str[2].toLowerCase();
//                                String strType = str[3].toLowerCase();
//                                String strExtra = str[4].toLowerCase();
//
//                                //insertDataSQLite(strUser , strDate, strTime, strType, strExtra);
//                                if (strUser.equalsIgnoreCase("David Lejeune")){
//                                    System.out.println("yeah its me");
//                                }
//
//                            }
//
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                //viewAllDataSQLite();
//            }
//            else
//            {
//                System.out.println("File documents doc not found");
//            }
//
//
//        }



    }

    public class AsyncBigStatsCreateDL extends AsyncTask<String, String, String> {


        ProgressDialog pd;


        //DatabaseHelperStats helper;
        MyTimer myTimer;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            myTimer = new MyTimer();
            pd = ProgressDialog.show(StatsActivity.this, "", "Creating single stat file..",
                    true, false);

            MyStats.createBigLogFile();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            pd.dismiss();
            myTimer.getElapsedTime();
            traverseBigStatFiles();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected String doInBackground(String... params) {


            File dir = Environment.getExternalStorageDirectory();
            File file = new File(dir, MyVars.FOLDER_STATS );
            traverse(file);
            return null;
        }

        public void traverse (File dir) {
            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; ++i) {
                    File file = files[i];
                    if (file.isDirectory()) {
                        //traverse(file);
                    } else {
                        // do something here with the file
                        System.out.println(file.toString());
                        addToBigStatFiles(file);
                    }
                }
            }
        }

        private void addToBigStatFiles(File file){
            //File dir = Environment.getExternalStorageDirectory();
            //File file = new File(dir, MyVars.FOLDER_STATS + "David Lejeune.txt");
            //MyTimer myTimer = new MyTimer();
            String line = "";
            int iCount=0;
            if (file.exists()) {

                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    while ((line = br.readLine()) != null) {
                        if (line.length() > 0) {
                            {
                                String[] str = line.split(",");
                                iCount+=1;
                                //System.out.println(iCount + " " + line);

                                MyStats.logStatEntryToBigLogFile(line + "\n");
//                                String strID = str[0].toLowerCase();
//                                String strUser = str[0].toLowerCase();
//                                String strDate = str[1].toLowerCase();
//                                String strTime = str[2].toLowerCase();
//                                String strType = str[3].toLowerCase();
//                                String strExtra = str[4].toLowerCase();
//
//                                String strYear = strDate.substring(0,4);
//                                String strMonth = strDate.substring(4,6);
//                                String strDay = strDate.substring(6);
//
//                                String strHour = strTime.substring(0,2);
//                                String strMinute = strTime.substring(3,5);

//                            //insertDataSQLite(strUser , strDate, strTime, strType, strExtra);
//                            if (strUser.equalsIgnoreCase("David Lejeune")){
//                                //System.out.println("yeah its me");
//                                //System.out.println(strYear);
//                                //System.out.println(strMonth);
//                                //System.out.println(strDay);
//                                System.out.println(strHour);
//                                System.out.println(strMinute);
//                            }

                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println("file export");
                //myTimer.getElapsedTime();
                //viewAllDataSQLite();
            }
            else
            {
                System.out.println("File documents doc not found");
            }


        }

        private void traverseBigStatFiles(){
            File dir = Environment.getExternalStorageDirectory();
            File file = new File(dir, MyVars.FOLDER_DATA + "all_users.txt");
            MyTimer myTimer = new MyTimer();
            String line = "";
            int iCount=0;
            if (file.exists()) {

                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    while ((line = br.readLine()) != null) {
                        if (line.length() > 0) {
                            {
                                String[] str = line.split(",");
                                iCount+=1;
                                //System.out.println(iCount + " " + line);

                                //String strID = str[0].toLowerCase();
//                            String strUser = str[0].toLowerCase();
//                            String strDate = str[1].toLowerCase();
//                            String strTime = str[2].toLowerCase();
//                            String strType = str[3].toLowerCase();
//                            String strExtra = str[4].toLowerCase();
//
//                            String strYear = strDate.substring(0,4);
//                            String strMonth = strDate.substring(4,6);
//                            String strDay = strDate.substring(6);
//
//                            String strHour = strTime.substring(0,2);
//                            String strMinute = strTime.substring(3,5);

//                            //insertDataSQLite(strUser , strDate, strTime, strType, strExtra);
//                            if (strUser.equalsIgnoreCase("David Lejeune")){
//                                //System.out.println("yeah its me");
//                                //System.out.println(strYear);
//                                //System.out.println(strMonth);
//                                //System.out.println(strDay);
//                                System.out.println(strHour);
//                                System.out.println(strMinute);
//                            }

                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println("file export");
                myTimer.getElapsedTime();
                //viewAllDataSQLite();
            }
            else
            {
                System.out.println("File documents doc not found");
            }


        }



    }


}
