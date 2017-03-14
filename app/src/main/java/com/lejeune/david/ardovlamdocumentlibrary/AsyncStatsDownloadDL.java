//package com.lejeune.david.ardovlamdocumentlibrary;
//
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//
///**
// * Created by Lucian on 3/14/2017.
// */
//
//public class AsyncStatsDownloadDL extends AsyncTask<String, String, String> {
//
//
//    private FTPfunctions ftpclient = null;
//    ProgressDialog pd;
//
//    MyTimer myTimer;
//
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//        txtResultMain.setText(txtResultMain.getText() + "Downloading all user stat files \n");
//        myTimer = new MyTimer("AsyncStatsDownloadDL");
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//
//        myTimer.getElapsedTime();
//        new MainActivity.AsyncBigStatsCreateDL().execute();
//
//    }
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//
//        ftpclient = new FTPfunctions();
//        ftpclient.ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
//        boolean status = false;
//        status = ftpclient.downloadStatsFiles(MyVars.FOLDER_STATS);
//        if (status == true) {
//            System.out.println("Download success");
//        } else {
//            System.out.println("Download failed");
//        }
//
//        ftpclient.ftpDisconnect();
//        return null;
//    }
//
//
//
//}
