package com.lejeune.david.ardovlamdocumentlibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MenuActivity extends Activity {

    private TextView txtUserType, txtFullName, txtTitle;
    public static ImageButton btnStats , btnUpdates;

    public static Boolean newUpdatesDoc = false;
    public static Boolean newUpdatesCom = false;
    public static Boolean updateValueFound = false;

    public static String filterID = "";
    public static String updateValue = "";
    public static Context cntx;

    public static int countUpdates;


    public static ArrayList<String> listUpdateFiles;


    public static int iServerUpdate;

    public static String connectionType ;
    MyTools myTools= null;
    public FTPClient mFTPClient = null;
    public String FOLDER_UPDATE_DOCUMENTS = "/DocLib/Documents/";

    String userType ;
    static String strUserType ;
    static String updateDocFile  = "Update_documents.txt";
    static String updateComFile  = "Update_commercial.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        refreshDisplayVariables();
        listUpdateFiles = new ArrayList<>();
        cntx = getApplicationContext();
        myTools = new MyTools();


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                //Toast.makeText(MenuActivity.this, "Searching ... please wait",
                     //   Toast.LENGTH_SHORT).show();

                switch (position) {

                    case 0:
                        filterID = "Inbraakdetectie";
//                        Toast.makeText(MenuActivity.this, filterID + " (pos " + position + ")",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        filterID = "Branddetectie";
//                        Toast.makeText(MenuActivity.this, filterID + " (pos " + position + ")",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        filterID = "Gasdetectie";
//                        Toast.makeText(MenuActivity.this, filterID + " (pos " + position + ")",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        filterID = "Camerabewaking";
//                        Toast.makeText(MenuActivity.this, filterID + " (pos " + position + ")",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        filterID = "Toegangscontrole";
//                        Toast.makeText(MenuActivity.this, filterID + " (pos " + position + ")",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        filterID = "Geintegreerde bewaking";
//                        Toast.makeText(MenuActivity.this, filterID + " (pos " + position + ")",
//                                Toast.LENGTH_SHORT).show();

                        break;
                }
                Toast.makeText(MenuActivity.this, "Please wait ...", Toast.LENGTH_SHORT).show();
                new AsyncShowMeDL().execute();
                //showMe();
                //getFilteredDocuments();
            }
        });


        txtUserType = (TextView) findViewById(R.id.txtUserType);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtFullName = (TextView) findViewById(R.id.txtFullName);


        btnStats = (ImageButton) findViewById(R.id.btnStats);
        btnStats.setImageDrawable(getResources().getDrawable(R.drawable.stats));
        btnStats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoStats();
            }
        });


        btnUpdates = (ImageButton) findViewById(R.id.btnUpdates);
        btnUpdates.setImageDrawable(getResources().getDrawable(R.drawable.update));
        btnUpdates.setVisibility(View.INVISIBLE);
        btnUpdates.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Starting update procedure");
                updateProcedure();
            }
        });

        myTools.retrieveSharedPref(cntx);
        txtFullName.setText(MyVars.fullname);


        getUsertype();
        getProfileImg();


        if (userType.equalsIgnoreCase("1")){
            strUserType = "Admin";
            checkUpdatesAvailable(updateComFile);
            checkUpdatesAvailable(updateDocFile);

            txtUserType.setText(strUserType);
            btnStats.setVisibility(View.VISIBLE);
        }
        else if (userType.equalsIgnoreCase("2")){
            strUserType = "Projectleider";
            checkUpdatesAvailable(updateComFile);
            txtUserType.setText(strUserType);
            btnStats.setVisibility(View.INVISIBLE);
        }
        else if (userType.equalsIgnoreCase("0")){
            strUserType = "Installateur";
            txtUserType.setText(strUserType);
            btnStats.setVisibility(View.INVISIBLE);
        }
        compareUpdateNrsDoc();
        compareUpdateNrsCom();
        myTools.retrieveSharedPref(cntx);


    }


    private void refreshDisplayVariables(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        MyVars.screenHeight = displayMetrics.heightPixels;
        MyVars.screenWidth = displayMetrics.widthPixels;
    }

    private void gotoStats(){
        if (userType.equalsIgnoreCase("1")) {

            for(int i=0; i<100 ;i++){
//            System.out.println(i);
                MyStats.logEntry("STAT" , "");
            }

            final Intent statsIntent = new Intent(MenuActivity.this, StatsActivity.class);
            MenuActivity.this.startActivity(statsIntent);
            //LoginActivity.this.finish();
        }
    }



    private void getUsertype(){
        userType = MyVars.usertype;

    }
    private void getProfileImg(){
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,"/DocLib/IMG/profile.jpg");
        long length = file.length();
        System.out.println("length:"+length);

        if (length > 1){
            if(file.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ImageView imgProfile = (ImageView) findViewById(R.id.imgProfile);
                imgProfile.setImageBitmap(myBitmap);
            }
        }
        else
        {
            setDefaultProfileImg();
        }

    }
    private void setDefaultProfileImg(){
        ImageView imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.avatar_1));
    }

    private static void checkUpdatesAvailable(String updateDocument) {
        System.out.println("checking server for update " + updateDocument);
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + updateDocument);
        String line = "";

        if (file.exists()) {
            //countUpdates = 0;
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        {
                            iServerUpdate = 0;
                            try {
                                iServerUpdate = Integer.parseInt(line);
                            } catch (NumberFormatException nfe) {
                                System.out.println("Could not parse " + nfe);
                            }
                            System.out.println("Update found in "+ updateDocument +" : " + iServerUpdate);
                            updateValueFound = true;
                            updateValue = iServerUpdate + "";
                            saveServerUpdateValue(updateDocument , cntx);
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("File Update not found");
        }
    }
    private static void compareUpdateNrsDoc(){
        System.out.println("updatedoclocal : "+ MyVars.updatedoclocal);
        System.out.println("updatedocserver : "+ MyVars.updatedocserver);
        int nrUpdateLocal = 0;
        int nrUpdateServer = 0;
        try {
            nrUpdateLocal = Integer.parseInt(MyVars.updatedoclocal);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        try {
            nrUpdateServer = Integer.parseInt(MyVars.updatedocserver);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        if (nrUpdateLocal < nrUpdateServer){
            System.out.println("local update doc lower (or equal) : need to update");
            newUpdatesDoc = true;
            buildDownloadList();
        }
        else
        {
            newUpdatesDoc = false;
            System.out.println("local update doc equal to server update : NO need to update");
        }
        displayUpdateButton();
    }


    private static void compareUpdateNrsCom(){
        System.out.println("updatecomlocal : "+ MyVars.updatecomlocal);
        System.out.println("updatecomserver : "+ MyVars.updatecomserver);
        int nrUpdateLocal = 0;
        int nrUpdateServer = 0;
        try {
            nrUpdateLocal = Integer.parseInt(MyVars.updatecomlocal);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        try {
            nrUpdateServer = Integer.parseInt(MyVars.updatecomserver);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        if (nrUpdateLocal < nrUpdateServer){
            System.out.println("local update com lower (or equal) : need to update");
            newUpdatesCom = true;
        }
        else
        {
            newUpdatesCom = false;
            System.out.println("local update com equal to server update : NO need to update");
        }
        displayUpdateButton();
    }

    public static void displayUpdateButton(){
        if(MyVars.usertype.equalsIgnoreCase("0")){
            if (newUpdatesDoc){
                System.out.println(newUpdatesDoc + " " + newUpdatesCom);
                btnUpdates.setVisibility(View.VISIBLE);
            }
            else
            {
                btnUpdates.setVisibility(View.INVISIBLE);
            }
        }
        if(MyVars.usertype.equalsIgnoreCase("1")){

            if (newUpdatesDoc || newUpdatesCom){
                System.out.println(newUpdatesDoc + " " + newUpdatesCom);
                btnUpdates.setVisibility(View.VISIBLE);
            }
            else
            {
                btnUpdates.setVisibility(View.INVISIBLE);
            }
        }
        if(MyVars.usertype.equalsIgnoreCase("2")){

            if (newUpdatesCom){
                System.out.println(newUpdatesDoc + " " + newUpdatesCom);
                btnUpdates.setVisibility(View.VISIBLE);
            }
            else
            {
                btnUpdates.setVisibility(View.INVISIBLE);
            }
        }


    }

    public static void saveServerUpdateValue(String updateDocument, Context context){
        System.out.println("updateDocuemt : " + updateDocument);
        if (updateDocument.equalsIgnoreCase(updateDocFile)){
            System.out.println("updatedocserver : " + updateValue);
            System.out.println("setting the update nr ..");
            SharedPreferences sharedPref = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("updatedocserver", updateValue);
            editor.apply();

            MyVars.updatedocserver = sharedPref.getString ("updatedocserver", "0");
            System.out.println("updatedocserver in SharedPref : " + MyVars.updatedocserver);
        }
        else
        if (updateDocument.equalsIgnoreCase(updateComFile)){
            System.out.println("updatecomserver : " + updateValue);
            System.out.println("setting the update nr ..");
            SharedPreferences sharedPref = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("updatecomserver", updateValue);
            editor.apply();

            MyVars.updatedocserver = sharedPref.getString ("updatecomserver", "0");
            System.out.println("updatecomserver in SharedPref : " + MyVars.updatecomserver);
        }



    }


    public void updateProcedure(){
        System.out.println("UpdateProcedure");
        if (userType.equalsIgnoreCase("1")){
            strUserType = "Admin";
            updateDoc();
            if(!newUpdatesDoc) updateCom();
        }
        else if (userType.equalsIgnoreCase("2")){
            strUserType = "Projectleider";
            updateCom();
        }
        else if (userType.equalsIgnoreCase("0")){
            strUserType = "Installateur";
            updateDoc();

        }
    }

    public void updateDoc(){

        if (newUpdatesDoc)
        {
            System.out.println("new updates found so action must be taken");
            connectionType = MyTools.checkNetworkStatus(getApplicationContext());
            if (connectionType.equalsIgnoreCase("wifi"))
            {
                System.out.println("newUpdate (true) + wifi connection >> executing updates");
                showArrayListUpdate();
                importUpdateDoc();
            }
            else
            {
                System.out.println("no wifi connection so updates not executed");
            }
        }
        else
        {

            System.out.println("no new updates found");
        }
    }

    public void updateCom(){

        if (newUpdatesCom)
        {
            System.out.println("new updates found so action must be taken");
            connectionType = MyTools.checkNetworkStatus(getApplicationContext());
            if (connectionType.equalsIgnoreCase("wifi"))
            {
                System.out.println("newUpdate (true) + wifi connection >> executing updates");
                // Create an async here for downloading folder commercial
                importUpdateCom();
            }
            else
            {
                System.out.println("no wifi connection so updates not executed");
            }
        }
        else
        {

            System.out.println("no new updates found");
        }
    }


    public void importUpdateCom(){
        System.out.println("getting ready to import updates com");
        AsyncCommercialDownloadDL asyncCommercialDownload = new AsyncCommercialDownloadDL();
        asyncCommercialDownload.execute();

    }

    public void importUpdateDoc(){
        System.out.println("getting ready to import updates doc");
        AsyncUpdatesDownloadDL asyncUpdateDocuments = new AsyncUpdatesDownloadDL();
        asyncUpdateDocuments.execute();

    }


    private static void setUpdatenrDoc(Context context){
        System.out.println("setting the update nr ..");
        SharedPreferences sharedPref = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String updateTemp = sharedPref.getString ("updatedocserver", "0");
        System.out.println("updateValue = " + updateTemp);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("updatedoclocal", updateTemp);
        editor.apply();
        MyVars.updatedoclocal = sharedPref.getString ("updatedoclocal", "0");
        System.out.println("updatedoclocal in SharedPref : " + MyVars.updatedoclocal);
//        Toast.makeText(context , "Updates finished" , Toast.LENGTH_LONG).show();
    }

    private static void setUpdatenrCom(Context context){
        System.out.println("setting the update nr ..");
        SharedPreferences sharedPref = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String updateTemp = sharedPref.getString ("updatecomserver", "0");
        System.out.println("updateValue = " + updateTemp);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("updatecomlocal", updateTemp);
        editor.apply();
        MyVars.updatecomlocal = sharedPref.getString ("updatecomlocal", "0");
        System.out.println("updatecomlocal in SharedPref : " + MyVars.updatecomlocal);
//        Toast.makeText(context , "Updates finished" , Toast.LENGTH_LONG).show();
    }

    public static void buildDownloadList(){
        System.out.println("buildDownloadList");

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "Documents.csv");
        String line = "" ;

        if (file.exists()) {
            countUpdates = 0;

            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    if (line.length()>0)
                    {
                        String[] str = line.split(",");
                        String id = str[0].toUpperCase();
                        String folderName = str[1].toUpperCase();
                        String documentName = str[2];
                        String updateNr = str[3].toUpperCase();

                        text.append("ID : " + id);
                        text.append('\n');
                        text.append("folderName : " + folderName);
                        text.append('\n');
                        text.append("documentName : " + documentName);
                        text.append('\n');
                        text.append("updateNr : " + updateNr + "update value : " + updateValue);
                        text.append('\n');


                        int nrValue = 0;
                        int nrUpdate = 0;

                        try {
                            nrValue = Integer.parseInt(updateNr);
                        } catch(NumberFormatException nfe) {
                            System.out.println("Could not parse " + nfe);
                        }
                        try {
                            nrUpdate = Integer.parseInt(MyVars.updatedoclocal);
                        } catch(NumberFormatException nfe) {
                            System.out.println("Could not parse " + nfe);
                        }

                        if (nrValue > nrUpdate)
                        {
                            countUpdates +=1;
                            System.out.println("Take action on this file :");
                            System.out.println(line);
                            listUpdateFiles.add(documentName);
                            System.out.println("countUpdates = " + countUpdates + " file = " + documentName);

                        }

                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }


    public void showArrayListUpdate(){

        System.out.println("looping through the arraylist");
        for (String cu : listUpdateFiles) {
            System.out.println("list Update entry : " + cu);
        }
    }

    private void showMe(){

        gotoFilterActivity();



    }



    private void gotoFilterActivity(){
        final Intent filterActivity = new Intent(MenuActivity.this, FilterActivity.class);
        MenuActivity.this.startActivity(filterActivity);

    }

    public class AsyncUpdatesDownloadDL extends AsyncTask<String, Integer, String> {



        long startTime ;
        long stopTime ;
        long elapsedTime ;
        private FTPfunctions ftpclient = null;
        ProgressDialog pd;
        int iCount =0;
        int progress = 0;
        String downloadedFile = "" ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            startTime = System.currentTimeMillis();


            pd = new ProgressDialog(MenuActivity.this);
//            pd.setTitle("Downloading update documents");
//            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pd.setMax(listUpdateFiles.size());
//            pd.setProgress(0);
//            pd.show();
            pd = ProgressDialog.show(MenuActivity.this, "", "Getting Document Files...",
                    true, false);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            stopTime = System.currentTimeMillis();
            elapsedTime = (stopTime - startTime) /1000  ;
            Toast.makeText(MenuActivity.this, "Updates FINISHED\n" + listUpdateFiles.size() + " updates downloaded\nin " + elapsedTime + " seconds", Toast.LENGTH_LONG).show();
            System.out.println("Time downloading " + listUpdateFiles.size() + " updates : " + elapsedTime + " seconds.");

            //pd.setProgress(listUpdateFiles.size());
            pd.dismiss();

            System.out.println("finished AsyncUpdates download");
            setUpdatenrDoc(cntx);
            compareUpdateNrsDoc();

            for(int i=0; i<100 ;i++) {
                MyStats.logEntry("UPDATE", "DOC-" + MyVars.updatedocserver);
            }

            if (userType.equalsIgnoreCase("1")){
                strUserType = "Admin";
                updateCom();
            }


        }



        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            pd.setTitle("Downloading : \n" + downloadedFile);
            pd.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {


            ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);
            boolean status = false;

            status = downloadUpdateFiles(FOLDER_UPDATE_DOCUMENTS);
            System.out.println(FOLDER_UPDATE_DOCUMENTS + " status : " + status);


            if (status == true) {
                System.out.println("Download success");
//                    handler.sendEmptyMessage(5);
            } else {
                System.out.println("Download failed");
//                    handler.sendEmptyMessage(-1);
            }

            ftpDisconnect();
            return null;
        }

        public boolean ftpDisconnect() {
            try {
                mFTPClient.logout();
                mFTPClient.disconnect();
                return true;
            } catch (Exception e) {
                System.out.println("Error occurred while disconnecting from ftp server.");
            }

            return false;
        }


        public boolean downloadUpdateFiles(String dir_path) {
            boolean status = false;
            String[] fileList = null;
            int iNot5 = 0;
            System.out.println("downloadUpdateFile in FTPfunctions");
            System.out.println("dir_path : " + dir_path);
            try {
                FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
                int length = ftpFiles.length;
                System.out.println("length : " + length);
                fileList = new String[length];
                for (int i = 0; i < length; i++) {
                    String name = ftpFiles[i].getName();
                    boolean isFile = ftpFiles[i].isFile();
                    //Log.d("DD", "/David/Documents/" + name);

                    if (isFile) {
                        fileList[i] = "File :: " + name;


                        System.out.println("looping through the arraylist");
                        //MenuActivity.AsyncUpdatesDownloadDL.publishProgress();
                        System.out.println("listUpdateFiles.length : " + listUpdateFiles.size());
                        for (String cu : listUpdateFiles) {
                            //System.out.println("list Update entry : " + cu);
                            if (cu.equalsIgnoreCase(name)){
                                //iCount +=1;
                                System.out.println("iCount = " +iCount);
                                System.out.println("found this file in update list");
                                System.out.println(">file : " + name);
                                System.out.println(">cu   : " + cu);
                                System.out.println("function bulk ftpDownload updates :");
                                System.out.println(">file : " + name);

                                downloadedFile = name;
                                //publishProgress(iCount);

                                status = ftpDownload(MyVars.FOLDER_DOCUMENTS + name,
                                        Environment.getExternalStorageDirectory() + MyVars.FOLDER_DOCUMENTS + name);




                                String imgName = name.replace(".pdf", ".PNG");
                                downloadedFile = imgName;
                                //publishProgress(iCount);
                                ftpDownload(MyVars.FOLDER_DOCUMENTS + imgName,
                                        Environment.getExternalStorageDirectory() + MyVars.FOLDER_DOCUMENTS + imgName);





                                String CurrentString = cu;
                                String[] separated = CurrentString.split("-");
                                int iSplit = 0;
                                for (String split : separated){
                                    iSplit += 1;
                                    System.out.println("split " + iSplit + " : " + split);
                                }
                                if(iSplit != 5){
                                    iNot5 +=1;
                                    System.out.println("WARNING iNot5 !!!!!!!!!!!!!!!!!!!!");
                                }


                            }
                        }
//                    System.out.println("function bulk ftpDownload updates :");
//                    System.out.println(">file : " + name);
//                    status = ftpDownload(FOLDER_DATA_DOCUMENTS_ALL + name,
//                            Environment.getExternalStorageDirectory() + FOLDER_DATA_DOCUMENTS_ALL + name);


                        System.out.println(">Status bulk ftpDownload: " + status);

                    } else {
                        fileList[i] = "Directory :: " + name;
                    }

                }

                System.out.println("iNot5 : " + iNot5);


                return status;
            } catch (Exception e) {
                e.printStackTrace();
                return status;
            }
        }


        // Method to connect to FTP server:
        public boolean ftpConnect(String host, String username, String password,
                                  int port) {
            try {
                mFTPClient = new FTPClient();
                // connecting to the host
                mFTPClient.connect(host, port);

                // now check the reply code, if positive mean connection success
                if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                    // login using username & password
                    boolean status = mFTPClient.login(username, password);

				/*
				 * Set File Transfer Mode
				 *
				 * To avoid corruption issue you must specified a correct
				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
				 * transferring text, image, and compressed files.
				 */
                    mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                    mFTPClient.enterLocalPassiveMode();

                    System.out.println("function FTP connect :");
                    System.out.println(">Status MyFTPFunctions: " + status);
                    return status;
                }
            } catch (Exception e) {
                System.out.println("Error: could not connect to host " + host);
            }

            return false;
        }


        public boolean ftpDownload(String srcFilePath, String desFilePath) {
            boolean status = false;
            try {
                FileOutputStream desFileStream = new FileOutputStream(desFilePath);

                System.out.println("function ftpDownload :");
                System.out.println(">srcFilePath : " + srcFilePath);
                System.out.println(">desFilePath : " + desFilePath);
                status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
                desFileStream.close();
                System.out.println(">Status ftpDownload: " + status);



                // this because there is no other check on the validity of the file
                // if file doesn't exist it creates an empty file that needs to be deleted
                if (!status)
                {
                    //File dir = Environment.getExternalStorageDirectory();
                    File file = new File(desFilePath);
                    boolean deleted = file.delete();
                }
                else
                {
                    Boolean isImage = srcFilePath.contains(".PNG");
                    if (!isImage){
                        iCount +=1;
                        progress = iCount;
                        //publishProgress(progress);
                    }
                }


                return status;
            } catch (Exception e) {
                System.out.println("download failed");
            }

            return status;
        }






    }

    public class AsyncCommercialDownloadDL extends AsyncTask<String, String, String> {


        private FTPfunctions ftpclient = null;

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = ProgressDialog.show(MenuActivity.this, "", "Getting Commercial Files...",
                    true, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pd.dismiss();
            for(int i=0; i<100 ;i++) {
                MyStats.logEntry("UPDATE", "COM-" + MyVars.updatecomserver);
            }
            setUpdatenrCom(cntx);
            compareUpdateNrsCom();

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
            status = ftpclient.downloadCommercialFiles(MyVars.FOLDER_COMMERCIAL);
            System.out.println(MyVars.FOLDER_COMMERCIAL + " status : " + status);
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
    }

    class AsyncShowMeDL extends AsyncTask<String, String, String> {


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
            showMe();
            return null;
        }






    }


}
