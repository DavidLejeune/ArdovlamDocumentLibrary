package com.lejeune.david.ardovlamdocumentlibrary;

import android.Manifest;
import android.app.Activity;
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
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends Activity {

    public TextView tvTitleLogin ;

    public static String connectionType ;
    MyTools myTools= null;
    Context cntx;

//    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Start declarations
        cntx = getApplicationContext();
        myTools= new MyTools();
        myTools.retrieveSharedPref(cntx);
//        String currentTime = myTools.getTime();
//        String currentDate = myTools.getDate();
//        System.out.println("Time :" + currentTime);
//        System.out.println("Date :" + currentDate);
        if(MyVars.registereduser.equalsIgnoreCase("1")){

            for(int i=0; i<100 ;i++){
//            System.out.println(i);
                MyStats.logEntry("ENTRY" , "");
            }
        }




//        showTable();

        //CHANGE UPDATENR HERE TO TEST IMPORTS OF UPDATES
        //SharedPrefHelper.getInstance().save(MainActivity.this,"1","updatenr");

        // Central screen advertisement
        ImageView imgProfile = (ImageView) findViewById(R.id.imgLogoTitle);
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        tvTitleLogin = (TextView) findViewById(R.id.tvTitleLogin);
        tvTitleLogin.setVisibility(View.VISIBLE);

        // Making sure the correct folder structure exists
        myTools.createFolders();

        // automatic redirect to loginactivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(loginIntent);
                MainActivity.this.finish();
            }
        }, 1977);

        // Checknetworkstatus , if on wifi this will allow data folder download
        connectionType = MyTools.checkNetworkStatus(cntx);
        System.out.println("connectionType : " + connectionType);
        if (connectionType.equalsIgnoreCase("wifi")){
            System.out.println("on wifi , so downloading data folder");
            new AsyncDataDownloadDL().execute();
        }


        refreshDisplayVariables();
        System.out.println("Height:" + MyVars.screenHeight + " Width:" + MyVars.screenWidth);


    }

    private void refreshDisplayVariables(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        MyVars.screenHeight = displayMetrics.heightPixels;
        MyVars.screenWidth = displayMetrics.widthPixels;
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



    public class AsyncDataDownloadDL extends AsyncTask<String, String, String> {


        //DatabaseHelper helper;
        private FTPfunctions ftpclient = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//
//                        MyTimer myTimer = new MyTimer();
//                        System.out.println("testing helper");
//                        helper.DATABASE_VERSION = helper.DATABASE_VERSION + 1;
//                        helper = new DatabaseHelper(cntx);
//                        saveDocumentsFile();
//                        myTimer.getElapsedTime();
//                }
//            };
//
//            thread.start();



            new AsyncUploadStatDL().execute();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
//        Toast.makeText(this, "Trying to download stuff", Toast.LENGTH_LONG).show();
//


            ftpclient = new FTPfunctions();
            ftpclient.ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
            boolean status = false;
            status = ftpclient.downloadDataFiles(MyVars.FOLDER_DATA);
            System.out.println(MyVars.FOLDER_DATA + " status : " + status);
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

//        private void insertDataSQLite(String folder_name , String document_name, String update_nr){
//
//            boolean isInserted = helper.insertData(folder_name , document_name , update_nr);
//            if (isInserted){
//                System.out.println("data inserted");
//            }
//            else
//            {
//                System.out.println("data NOT inserted");
//            }
//
//        }
//
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
//                    buffer.append("folder : " + cursor.getString(1) + "\n");
//                    buffer.append("document : " + cursor.getString(2) + "\n");
//                    buffer.append("update : " + cursor.getString(3) + "\n\n");
//                    System.out.println(buffer);
//                }
//
//                //showMessage("Data", buffer.toString());
//
//            }
//        }
//
////    public void showMessage(String title, String message){
////        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        builder.setCancelable(true);
////        builder.setTitle(title);
////        builder.setMessage(message);
////        builder.show();
////
////    }
//
//
//        private void saveDocumentsFile(){
//            File dir = Environment.getExternalStorageDirectory();
//            File file = new File(dir, MyVars.FOLDER_DATA + "documents.csv");
//            String line = "";
//
//            if (file.exists()) {
//
//                StringBuilder text = new StringBuilder();
//                try {
//                    BufferedReader br = new BufferedReader(new FileReader(file));
//                    while ((line = br.readLine()) != null) {
//                        if (line.length() > 0) {
//                            {
//                                String[] str = line.split(",");
//
//                                String strID = str[0].toLowerCase();
//                                String strFolder = str[1].toLowerCase();
//                                String strDoc = str[2].toLowerCase();
//                                String strUpdate = str[3].toLowerCase();
//
//                                insertDataSQLite(strFolder , strDoc, strUpdate);
//
//
//                            }
//
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                viewAllDataSQLite();
//            }
//            else
//            {
//                System.out.println("File documents doc not found");
//            }
//
//
//        }



    }


    public class AsyncUploadStatDL extends AsyncTask<String, String, String> {


        private FTPfunctions ftpclient = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
//        Toast.makeText(this, "Trying to download stuff", Toast.LENGTH_LONG).show();
//


            ftpclient = new FTPfunctions();
            ftpclient.ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
            System.out.println(MyVars.FOLDER_DATA + MyVars.fullname + ".txt");
            boolean status = false;

            status = ftpclient.ftpUpload(Environment.getExternalStorageDirectory() + MyVars.FOLDER_DATA + MyVars.fullname + ".txt" ,
                    MyVars.FOLDER_DATA + "Stats/" + MyVars.fullname + ".txt" ,
                    MyVars.FOLDER_DATA + "Stats/" , cntx);
            System.out.println("upload stats status : " + status);
            if (status == true) {
                System.out.println("upload success");
//                    handler.sendEmptyMessage(5);
            } else {
                System.out.println("upload failed");
//                    handler.sendEmptyMessage(-1);
            }

            ftpclient.ftpDisconnect();
            return null;
        }
    }


}