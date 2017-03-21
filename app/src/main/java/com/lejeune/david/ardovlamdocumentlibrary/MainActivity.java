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
    private TextView tvTitleLogin ,txtResultMain ;

    private static String connectionType ;
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
        //retrieving the shared preferences
        myTools.retrieveSharedPref(cntx);
        //endregion

        //region UI elements
        txtResultMain = (TextView) findViewById(R.id.txtResultMain);
        txtResultMain.setVisibility(View.VISIBLE);
        txtResultMain.setText("");
//        txtResultMain.setBackgroundResource(android.R.color.darker_gray);
//        txtResultMain.setBackgroundResource(R.color.almostBlack);

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
        //refresh display variables will be used later for gridview image dimensions
        refreshDisplayVariables();
        System.out.println("Height:" + MyVars.screenHeight + " Width:" + MyVars.screenWidth);
        //endregion

    }

    //region Misc methods
    private void setTxtResultMain(String message){
        // all output in sequence
        txtResultMain.setText(txtResultMain.getText() + message + "\n");
        // only 1 output shown
        //txtResultMain.setText(message + "\n");
    }

    private void refreshDisplayVariables(){
        //display variables will be used later for gridview image dimensions
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

    /** Async tasks work as following :
     * If NOT on Wifi :
     *    simply continue to login screen
     * If on Wifi :
     *    - Download data folder from ftp server
     *    - Upload personal stat file to ftp server
     * If on Wifi AND admin then also :
     *    - Download stat files from ftp server
     *    - Merge all individual stat files into 1 big stat file
    */

    /**
     *  Downloading the complete data folder
     */
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

//            if (status == true) {
////                    handler.sendEmptyMessage(5);
//            }
//            else
//            {
////                      handler.sendEmptyMessage(-1);
//            }

            ftpclient.ftpDisconnect();

            return null;
        }

    }

    /**
     *  Uploading the users personal stat file
     */
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

            // if admin continue with stat download and big stat creation
            // else just continue to login
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

    /**
     *  Admins wil download all user stat files
     */
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

    /**
     *  Admin will combine all user stat files into 1 big stat file
     */
    public class AsyncBigStatsCreateDL extends AsyncTask<String, String, String> {


        int iCountStatFiles;
        MyTimer myTimer;
        File dir = null;
        File file = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myTimer = new MyTimer("AsyncBigStatsCreateDL");

            dir = Environment.getExternalStorageDirectory();
            file = new File(dir, MyVars.FOLDER_STATS);

            setTxtResultMain("Creating big stat file (all users)");

            MyStats.createBigLogFile();

            // this counter is used for the array of files
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

            // create the big stat file
            traverseMerge(file);
            return null;
        }

        public void traverseCount(File dir) {

            // a counter for the array length
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
