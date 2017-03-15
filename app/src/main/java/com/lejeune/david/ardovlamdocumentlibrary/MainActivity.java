package com.lejeune.david.ardovlamdocumentlibrary;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends Activity {

    //region Declarations
    public TextView tvTitleLogin ,txtResultMain ;

    public static String connectionType ;
    private MyTools myTools= null;
    private Context cntx;
    private MyTimer timeActivity;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //region Initialisations
        cntx = getApplicationContext();
        timeActivity = new MyTimer("MainActivity");
        myTools= new MyTools();

        //making sure the correct folder structure exists
        myTools.createFolders();
        myTools.retrieveSharedPref(cntx);
        //endregion

        //region UI elements
        txtResultMain = (TextView) findViewById(R.id.txtResultMain);
        txtResultMain.setVisibility(View.VISIBLE);
        txtResultMain.setText("");

        setTxtResultMain("Checking folders");

        // Central screen advertisement
        ImageView imgLogo = (ImageView) findViewById(R.id.imgLogoTitle);
        imgLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        tvTitleLogin = (TextView) findViewById(R.id.tvTitleLogin);
        tvTitleLogin.setVisibility(View.VISIBLE);
        //endregion




        //region Download actions based on connection status (and type)
        // Checknetworkstatus , if on wifi this will allow data folder download
        connectionType = MyTools.checkNetworkStatus(cntx);
        System.out.println("connectionType : " + connectionType);
        if (connectionType.equalsIgnoreCase("wifi")){
            setTxtResultMain("WiFi detected");
            setTxtResultMain("Downloading data folder");
            new AsyncDataDownloadDL().execute();
        }
        else
        {

            setTxtResultMain("NO WiFi detected");
            gotoLogin();
        }
        //endregion

        //region Misc actions
        refreshDisplayVariables();
        System.out.println("Height:" + MyVars.screenHeight + " Width:" + MyVars.screenWidth);
        //endregion

    }

    //region Misc methods
    private void setTxtResultMain(String message){
        txtResultMain.setText(txtResultMain.getText() + message + "\n");
    }

    private void refreshDisplayVariables(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        MyVars.screenHeight = displayMetrics.heightPixels;
        MyVars.screenWidth = displayMetrics.widthPixels;
    }

    private void gotoLogin(){

        setTxtResultMain("Launching login");
                final Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(loginIntent);
                    MainActivity.this.finish();
    }
    //endregion



    //region Internal Async Tasks
    public class AsyncDataDownloadDL extends AsyncTask<String, String, String> {


        private FTPfunctions ftpclient = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new AsyncUploadStatDL().execute();
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
            status = ftpclient.downloadDataFiles(MyVars.FOLDER_DATA);
            if (status == true) {
//                    handler.sendEmptyMessage(5);
            } else {
//                    handler.sendEmptyMessage(-1);
            }

            ftpclient.ftpDisconnect();
            return null;
        }

    }

    public class AsyncUploadStatDL extends AsyncTask<String, String, String> {


        private FTPfunctions ftpclient = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setTxtResultMain("Uploading user stat file");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(MyVars.usertype.equalsIgnoreCase("1")){
                new AsyncStatsDownloadDL().execute();
            }
            else
            {
                gotoLogin();
                timeActivity.getElapsedTime();
            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            ftpclient = new FTPfunctions();
            ftpclient.ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
            System.out.println(MyVars.FOLDER_DATA + MyVars.fullname + ".txt");
            boolean status = false;

            status = ftpclient.ftpUpload(Environment.getExternalStorageDirectory() + MyVars.FOLDER_DATA + MyVars.fullname + ".txt" ,
                    MyVars.FOLDER_DATA + "Stats/" + MyVars.fullname + ".txt" ,
                    MyVars.FOLDER_DATA + "Stats/" , cntx);
            if (status == true) {
                System.out.println("upload success");
            } else {
                System.out.println("upload failed");
            }

            ftpclient.ftpDisconnect();
            return null;
        }
    }

    public class AsyncStatsDownloadDL extends AsyncTask<String, String, String> {


        private FTPfunctions ftpclient = null;
        ProgressDialog pd;

        MyTimer myTimer;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setTxtResultMain("Downloading all user stat files");
            myTimer = new MyTimer("AsyncStatsDownloadDL");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

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
            if (status == true) {
                System.out.println("Download success");
            } else {
                System.out.println("Download failed");
            }

            ftpclient.ftpDisconnect();
            return null;
        }



    }

    public class AsyncBigStatsCreateDL extends AsyncTask<String, String, String> {


        int iCountStatFiles;


        MyTimer myTimer;

        File dir = null;
        File file = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dir = Environment.getExternalStorageDirectory();
            file = new File(dir, MyVars.FOLDER_STATS);

            setTxtResultMain("Creating big stat file (all users)");

            myTimer = new MyTimer("AsyncBigStatsCreateDL");
            MyStats.createBigLogFile();
            iCountStatFiles = 0;
            traverseCount(file);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            myTimer.getElapsedTime();

            gotoLogin();
            timeActivity.getElapsedTime();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected String doInBackground(String... params) {


            traverseMerge(file);
            return null;
        }

        public void traverseCount(File dir) {
            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; ++i) {
                    File file = files[i];
                    if (file.isDirectory()) {
                    } else {
                        iCountStatFiles += 1;

                    }
                }
            }
        }


        public void traverseMerge(File dir) {

            int iCountIndex = 0;
            MergerFiles.files = new File[iCountStatFiles];

            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; ++i) {
                    File file = files[i];
                    if (file.isDirectory()) {
                    } else {
                        MergerFiles.addFileToList(file.toString(), iCountIndex);
                        iCountIndex += 1;


                    }
                }
                MergerFiles.createBigLogFile();
            }

        }
    }
    //endregion


}













//    public void showTable(){
//        try{
//            mydb = openOrCreateDatabase("Documents.db", Context.MODE_PRIVATE,null);
//            Cursor allrows  = mydb.rawQuery("SELECT * FROM "+  "documents_table", null);
//            System.out.println("COUNT : " + allrows.getCount());
//            Integer cindex = allrows.getColumnIndex("folder");
//            Integer cindex1 = allrows.getColumnIndex("document");
//            Integer cindex2 = allrows.getColumnIndex("update");
//
//
//            if(allrows.moveToFirst()){
//                do{
//
//                    final String ID = allrows.getString(0);
//                    String FOLDER= allrows.getString(1);
//                    String DOCUMENT= allrows.getString(2);
//                    String UPDATE= allrows.getString(3);
//
//
//                    System.out.println(allrows.getString(cindex) + " "+ allrows.getString(cindex1)+ " "+ allrows.getString(cindex2));
//
//                }
//                while(allrows.moveToNext());
//            }
//            mydb.close();
//        }catch(Exception e){
//            Toast.makeText(getApplicationContext(), "Error encountered."+e.toString(), Toast.LENGTH_LONG).show();
//        }
//
////        InputStreamReader is = ;
////        StringBuilder sb = new StringBuilder();
////        try {
////            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
////            String line = null;
////            while ((line = reader.readLine()) != null) {
////                sb.append(line).append("\n");
////            }
////            is.close();
////        } catch(OutOfMemoryError om) {
////            om.printStackTrace();
////        } catch(Exception ex) {
////            ex.printStackTrace();
////        }
////        String result = sb.toString();
//
////        BufferedReader reader = null;
////        try {
////            Context context = getApplicationContext();
////            reader = new BufferedReader( new InputStreamReader(context.openFileInput(Environment.getExternalStorageDirectory() + "/Users.csv"))) ;
////            reader.readLine(); // Ignores the first line
////            String data;
////            while ((data = reader.readLine()) != null) { // Gets a whole line
////                String[] line = data.split(","); // Splits the line up into a string array
////                if (line.length > 1) {
////                    // Do stuff, e.g:
////                    String value = line[1];
////                    System.out.println(value);
////                }
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        } finally {
////            if (reader != null) {
////                try {
////                    reader.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
//
////        CSVReader reader = new CSVReader(new FileReader(Environment.getExternalStorageDirectory() + "/Users.csv"));
////        String [] nextLine;
////        while ((nextLine = reader.readNext()) != null) {
////            // nextLine[] is an array of values from the line
////            System.out.println(nextLine[0] + nextLine[1] + "etc...");
////        }
//
////https://www.youtube.com/watch?v=BALgSGrsXH8
//
//
//
//
//    }

//    public void autoLaunchActivity(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                final Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
//
//                if(MyVars.registereduser.equalsIgnoreCase("1")){
//
//                    MainActivity.this.startActivity(menuIntent);
//                    MainActivity.this.finish();
//                }
//              else
//                {
//                    MainActivity.this.startActivity(loginIntent);
//                    MainActivity.this.finish();
//                }
//            }
//        }, 1977);
//    }