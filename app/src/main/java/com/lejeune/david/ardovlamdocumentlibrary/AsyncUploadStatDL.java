//package com.lejeune.david.ardovlamdocumentlibrary;
//
//import android.os.AsyncTask;
//import android.os.Environment;
//
///**
// * Created by Lucian on 3/14/2017.
// */
//
//public class AsyncUploadStatDL extends AsyncTask<String, String, String> {
//
//    private FTPfunctions ftpclient = null;
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//        txtResultMain.setText(txtResultMain.getText() + "Uploading user stat file \n");
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        if(MyVars.usertype.equalsIgnoreCase("1")){
//            new MainActivity.AsyncStatsDownloadDL().execute();
//        }
//        else
//        {
//            gotoLogin();
//            timeActivity.getElapsedTime();
//        }
//
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
//        System.out.println(MyVars.FOLDER_DATA + MyVars.fullname + ".txt");
//        boolean status = false;
//
//        status = ftpclient.ftpUpload(Environment.getExternalStorageDirectory() + MyVars.FOLDER_DATA + MyVars.fullname + ".txt" ,
//                MyVars.FOLDER_DATA + "Stats/" + MyVars.fullname + ".txt" ,
//                MyVars.FOLDER_DATA + "Stats/" , cntx);
//        if (status == true) {
//            System.out.println("upload success");
//        } else {
//            System.out.println("upload failed");
//        }
//
//        ftpclient.ftpDisconnect();
//        return null;
//    }
//}
