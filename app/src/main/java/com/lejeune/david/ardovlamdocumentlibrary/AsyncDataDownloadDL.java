//package com.lejeune.david.ardovlamdocumentlibrary;
//
//import android.os.AsyncTask;
//
///**
// * Created by Lucian on 3/14/2017.
// */
//
//public class AsyncDataDownloadDL  extends AsyncTask<String, String, String> {
//
//    private FTPfunctions ftpclient = null;
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//
//        new AsyncUploadStatDL().execute();
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
//
//        ftpclient = new FTPfunctions();
//        ftpclient.ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
//        boolean status = false;
//        status = ftpclient.downloadDataFiles(MyVars.FOLDER_DATA);
//        if (status == true) {
////                    handler.sendEmptyMessage(5);
//        } else {
////                    handler.sendEmptyMessage(-1);
//        }
//
//        ftpclient.ftpDisconnect();
//        return null;
//    }
//
//
//
//}
