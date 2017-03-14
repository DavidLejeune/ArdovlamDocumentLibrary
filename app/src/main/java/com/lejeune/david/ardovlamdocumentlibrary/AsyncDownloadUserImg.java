//package com.lejeune.david.ardovlamdocumentlibrary;
//
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.widget.Toast;
//
//import java.io.File;
//
///**
// * Created by Lucian on 3/14/2017.
// */
//
//public class AsyncDownloadUserImg extends AsyncTask<Void, Void, Void> {
//
//    private FTPfunctions ftpclient = null;
//    ProgressDialog pd;
//
//    @Override
//    protected Void doInBackground(Void... params) {
//
//        ftpclient = new FTPfunctions();
//        final String strFile = "/DocLib/IMG/" + MyVars.fullname + ".jpg";
//        boolean status = false;
//
//        ftpclient.ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
//
//        ftpclient.ftpDownload(strFile, Environment.getExternalStorageDirectory()
//                + "/DocLib/IMG/profile.jpg");
//
//        ftpclient.ftpDisconnect();
//        return null;
//    }
//
//    @Override
//    protected void onPreExecute() {
//
//        pd = ProgressDialog.show(StatsActivity.this, "", "Creating single stat for this year..",
//                true, false);
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        pd.setProgress(100);
//        pd.dismiss();
//
//        File dir = Environment.getExternalStorageDirectory();
//        File file = new File(dir,"/DocLib/IMG/profile.jpg");
//        long length = file.length();
//        LoginActivity.setProfileImg();
//        if (LoginActivity.loginOK)
//        {
//            LoginActivity.restartApp();
//        }
//        else
//        {
//            Toast.makeText(LoginActivity.this, "loginProcedure NOK", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    }
//
//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }
//
//}
