package com.lejeune.david.ardovlamdocumentlibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
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

    //region Declarations
    private TextView txtUserType, txtFullName, txtTitle;
    public static ImageButton btnStats , btnUpdates;

    public static Boolean newUpdatesDoc = false;
    public static Boolean newUpdatesCom = false;
    public static Boolean newUpdatesTec = false;
    public static Boolean updateValueFound = false;

    public static String filterID = "";
    public static String updateValue = "";
    public static Context cntx;

    public static int countUpdates;

    public static ArrayList<String> listUpdateFilesDoc;
    public static ArrayList<String> listUpdateFilesCom;
    public static ArrayList<String> listUpdateFilesTec;

    public static int iServerUpdate;

    public static String connectionType ;
    MyTools myTools= null;
    public FTPClient mFTPClient = null;
    public String FOLDER_UPDATE_DOCUMENTS = MyVars.FOLDER_DOCUMENTS;

    String userType ;
    static String strUserType ;
    static String updateDocFile  = "Update_documents.txt";
    static String updateComFile  = "Update_commercial.txt";
    static String updateTecFile  = "Update_technical.txt";

    ImageView imgProfile;

    GridView gridview;


    private GridView gridView;
    private GridViewAdapter gridAdapter;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        //region Initialisations
        init();
        //endregion

        //region Gridview

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                System.out.println("position : "+ position);
//                //Create intent
//                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//                intent.putExtra("title", item.getTitle());
//                intent.putExtra("image", item.getImage());
//
//                //Start details activity
//                startActivity(intent);


                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                switch (position) {

                    case 0:
                        filterID = "Inbraakdetectie";
                        break;
                    case 1:
                        filterID = "Branddetectie";
                        break;
                    case 2:
                        filterID = "Gasdetectie";
                        break;
                    case 3:
                        filterID = "Camerabewaking";
                        break;
                    case 4:
                        filterID = "Toegangscontrole";
                        break;
                    case 5:
                        filterID = "Geintegreerde bewaking";
                        break;
                }

                Toast.makeText(MenuActivity.this, "Please wait ...", Toast.LENGTH_SHORT).show();
                new AsyncShowMeDL().execute();


            }
        });

//        gridview = (GridView) findViewById(R.id.gridview);
//        gridview.setAdapter(new ImageAdapter(this));
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//
//                switch (position) {
//
//                    case 0:
//                        filterID = "Inbraakdetectie";
//                        break;
//                    case 1:
//                        filterID = "Branddetectie";
//                        break;
//                    case 2:
//                        filterID = "Gasdetectie";
//                        break;
//                    case 3:
//                        filterID = "Camerabewaking";
//                        break;
//                    case 4:
//                        filterID = "Toegangscontrole";
//                        break;
//                    case 5:
//                        filterID = "Geintegreerde bewaking";
//                        break;
//                }
//
//                Toast.makeText(MenuActivity.this, "Please wait ...", Toast.LENGTH_SHORT).show();
//                new AsyncShowMeDL().execute();
//
//            }
//        });



        //endregion

        //region Textfields
        txtUserType = (TextView) findViewById(R.id.txtUserType);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtFullName = (TextView) findViewById(R.id.txtFullName);
        //endregion

        //region Buttons
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

        //endregion

        //region ProfileImage
        myTools.retrieveSharedPref(cntx);
        txtFullName.setText(MyVars.fullname);

        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                final Intent loginIntent = new Intent(MenuActivity.this, LoginActivity.class);
                MenuActivity.this.startActivity(loginIntent);
                MenuActivity.this.finish();
            }
        });

        getProfileImg();
        //endregion

        //region Setting interface based on Usertype
        getUsertype();
        if (userType.equalsIgnoreCase("1")){
            strUserType = "Admin";
            checkUpdatesAvailable(updateComFile);
            checkUpdatesAvailable(updateDocFile);
            checkUpdatesAvailable(updateTecFile);

            txtUserType.setText(strUserType);
            btnStats.setVisibility(View.VISIBLE);
        }
        else if (userType.equalsIgnoreCase("2")){
            strUserType = "Project manager";
            checkUpdatesAvailable(updateComFile);

            txtUserType.setText(strUserType);
            btnStats.setVisibility(View.INVISIBLE);
        }
        else if (userType.equalsIgnoreCase("0")){
            strUserType = "Electrician";
            checkUpdatesAvailable(updateDocFile);

            txtUserType.setText(strUserType);
            btnStats.setVisibility(View.INVISIBLE);
        }
        else if (userType.equalsIgnoreCase("3")){
            strUserType = "Service Engineer";
            checkUpdatesAvailable(updateTecFile);

            txtUserType.setText(strUserType);
            btnStats.setVisibility(View.INVISIBLE);
        }
        //endregion

        //region Setting update(s)
        compareUpdateNrsDoc();
        compareUpdateNrsCom();
        compareUpdateNrsTec();
        myTools.retrieveSharedPref(cntx);
        //endregion

        MyVars.currentOrientation = getRotation(cntx);
//        switch (MyVars.currentOrientation) {
//            case "landscape":
//                gridview.setRowCount(1);
//            case "reverse landscape":
//                gridview.setRowCount(1);
//        }
    }


        private ArrayList<ImageItem> getData() {
            final ArrayList<ImageItem> imageItems = new ArrayList<>();
            TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
            for (int i = 0; i < imgs.length(); i++) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
                imageItems.add(new ImageItem(bitmap, ""));
            }
            return imageItems;
        }


    public String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                //gridView.setRowCount(1);
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                //gridView.setRowCount(1);
                return "reverse landscape";
        }
    }


    //region Misc
    private void init(){
        //checking displayvariables for gridview images sizes
        refreshDisplayVariables();

        listUpdateFilesDoc = new ArrayList<>();
        listUpdateFilesCom = new ArrayList<>();
        listUpdateFilesTec = new ArrayList<>();

        cntx = getApplicationContext();
        myTools = new MyTools();

    }

    private void refreshDisplayVariables(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        MyVars.screenHeight = displayMetrics.heightPixels;
        MyVars.screenWidth = displayMetrics.widthPixels;
        System.out.println("Height " + MyVars.screenHeight);
        System.out.println("Width " + MyVars.screenWidth);
    }

    private void gotoStats(){
        // only an admin will get access to stats
        if (userType.equalsIgnoreCase("1")) {

 //           for(int i=0; i<100 ;i++){
//            System.out.println(i);
                MyStats.logEntry("STAT" , "");
//            }

            final Intent statsIntent = new Intent(MenuActivity.this, StatsActivity.class);
            MenuActivity.this.startActivity(statsIntent);
            //LoginActivity.this.finish();
        }
    }
    private void showMe(){

        gotoFilterActivity();



    }
    private void gotoFilterActivity(){
        final Intent filterActivity = new Intent(MenuActivity.this, FilterActivity.class);
        MenuActivity.this.startActivity(filterActivity);

    }
    //endregion

    //region User
    private void getUsertype(){
        userType = MyVars.usertype;
    }
    private void getProfileImg(){
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,MyVars.FOLDER_IMG + "profile.jpg");
        long length = file.length();
        System.out.println("length:"+length);

        if (length > 1){
            if(file.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imgProfile.setImageBitmap(myBitmap);
            }
        }
        else
        {
            setDefaultProfileImg();
        }

    }
    private void setDefaultProfileImg(){
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.avatar_1));
    }
    //endregion

    //region Update
    public void updateProcedure(){
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        System.out.println("UpdateProcedure");
        if (userType.equalsIgnoreCase("1")){
            strUserType = "Admin";
            // this will search updates for all 3 categories
            updateDoc();
            if(!newUpdatesDoc) updateCom();
            if(!newUpdatesCom) updateTec();
        }
        else if (userType.equalsIgnoreCase("2")){
            strUserType = "Project manager";
            updateCom();
        }
        else if (userType.equalsIgnoreCase("0")){
            strUserType = "Electrician";
            updateDoc();
        }
        else if (userType.equalsIgnoreCase("3")){
            strUserType = "Service engineer";
            updateTec();
        }
    }

    private static void checkUpdatesAvailable(String updateDocument) {

        // this method allows to search for all categories if there are updates (by means of the updateDocument)
        System.out.println("checking server for update " + updateDocument);
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + updateDocument);
        String line = "";

        if (file.exists()) {
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

    /**
     *  For all 3 categories : seperate comparators of Update nrs
     */
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
            buildDownloadListDoc();
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
            System.out.println("local update doc lower (or equal) : need to update");
            newUpdatesCom = true;
            buildDownloadListCom();
        }
        else
        {
            newUpdatesCom = false;
            System.out.println("local update doc equal to server update : NO need to update");
        }
        displayUpdateButton();
    }
    private static void compareUpdateNrsTec(){
        System.out.println("updateteclocal : "+ MyVars.updateteclocal);
        System.out.println("updatetecserver : "+ MyVars.updatetecserver);
        int nrUpdateLocal = 0;
        int nrUpdateServer = 0;
        try {
            nrUpdateLocal = Integer.parseInt(MyVars.updateteclocal);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        try {
            nrUpdateServer = Integer.parseInt(MyVars.updatetecserver);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        if (nrUpdateLocal < nrUpdateServer){
            System.out.println("local update tec lower (or equal) : need to update");
            newUpdatesTec = true;
            buildDownloadListTec();
        }
        else
        {
            newUpdatesTec = false;
            System.out.println("local update tec equal to server update : NO need to update");
        }
        displayUpdateButton();
    }

    public static void displayUpdateButton(){
        // Depending on user type check the available updates
        // And depending on available updates display the update button (or not)
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

            if (newUpdatesDoc || newUpdatesCom || newUpdatesTec){
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

        if(MyVars.usertype.equalsIgnoreCase("3")){

            if (newUpdatesTec){
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

            MyVars.updatecomserver = sharedPref.getString ("updatecomserver", "0");
            System.out.println("updatecomserver in SharedPref : " + MyVars.updatecomserver);
        }
        else
        if (updateDocument.equalsIgnoreCase(updateTecFile)){
            System.out.println("updatetecserver : " + updateValue);
            System.out.println("setting the update nr ..");
            SharedPreferences sharedPref = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("updatetecserver", updateValue);
            editor.apply();

            MyVars.updatetecserver = sharedPref.getString ("updatetecserver", "0");
            System.out.println("updatetecserver in SharedPref : " + MyVars.updatetecserver);
        }



    }

    /**
     *  Actual updating of the 3 categories (with async tasks)
     */
    public void updateDoc(){

        if (newUpdatesDoc)
        {
            System.out.println("new updates found so action must be taken");
            connectionType = MyTools.checkNetworkStatus(getApplicationContext());
            if (connectionType.equalsIgnoreCase("wifi"))
            {
                System.out.println("newUpdate (true) + wifi connection >> executing updates");
                showArrayListUpdateDoc();
                importUpdateDoc();
            }
            else
            {
                System.out.println("no wifi connection so updates not executed");
            }
        }
        else
        {

            System.out.println("no new doc updates found");
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
                showArrayListUpdateCom();
                importUpdateCom();
            }
            else
            {
                System.out.println("no wifi connection so updates not executed");
            }
        }
        else
        {

            System.out.println("no new com updates found");
        }
    }
    public void updateTec(){

        if (newUpdatesTec)
        {
            System.out.println("new updates found so action must be taken");
            connectionType = MyTools.checkNetworkStatus(getApplicationContext());
            if (connectionType.equalsIgnoreCase("wifi"))
            {
                System.out.println("newUpdate (true) + wifi connection >> executing updates");
                showArrayListUpdateTec();
                importUpdateTec();
            }
            else
            {
                System.out.println("no wifi connection so updates not executed");
            }
        }
        else
        {

            System.out.println("no new com updates found");
        }
    }

    public void importUpdateCom(){
        System.out.println("getting ready to import updates com");
        AsyncUpdatesCommercialDownloadDL asyncCommercialDownload = new AsyncUpdatesCommercialDownloadDL();
        asyncCommercialDownload.execute();

    }
    public void importUpdateDoc(){
        System.out.println("getting ready to import updates doc");
        AsyncUpdatesDocumentsDownloadDL asyncUpdateDocuments = new AsyncUpdatesDocumentsDownloadDL();
        asyncUpdateDocuments.execute();

    }
    public void importUpdateTec(){
        System.out.println("getting ready to import updates tec");
        AsyncUpdatesTechnicalDownloadDL asyncUpdateTec = new AsyncUpdatesTechnicalDownloadDL();
        asyncUpdateTec.execute();

    }

    /**
     * Upon completion of update download set the local update nrs = to server update nrs
     */
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
    private static void setUpdatenrTec(Context context){
        System.out.println("setting the update nr ..");
        SharedPreferences sharedPref = context.getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String updateTemp = sharedPref.getString ("updatetecserver", "0");
        System.out.println("updateValue = " + updateTemp);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("updateteclocal", updateTemp);
        editor.apply();
        MyVars.updateteclocal = sharedPref.getString ("updateteclocal", "0");
        System.out.println("updateteclocal in SharedPref : " + MyVars.updateteclocal);
//        Toast.makeText(context , "Updates finished" , Toast.LENGTH_LONG).show();
    }

    /**
     *  Building and showing the list of files for each category
     */
    public static void buildDownloadListDoc(){
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
                            listUpdateFilesDoc.add(documentName);
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
    public static void buildDownloadListCom(){
        System.out.println("buildDownloadListCom");

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "Commercial.csv");
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
                            nrUpdate = Integer.parseInt(MyVars.updatecomlocal);
                        } catch(NumberFormatException nfe) {
                            System.out.println("Could not parse " + nfe);
                        }

                        if (nrValue > nrUpdate)
                        {
                            countUpdates +=1;
                            System.out.println("Take action on this file :");
                            System.out.println(line);
                            listUpdateFilesCom.add(documentName);
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
    public static void buildDownloadListTec(){
        System.out.println("buildDownloadListTec");

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "Technical.csv");
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
                            nrUpdate = Integer.parseInt(MyVars.updateteclocal);
                        } catch(NumberFormatException nfe) {
                            System.out.println("Could not parse " + nfe);
                        }

                        if (nrValue > nrUpdate)
                        {
                            countUpdates +=1;
                            System.out.println("Take action on this file :");
                            System.out.println(line);
                            listUpdateFilesTec.add(documentName);
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

    public void showArrayListUpdateDoc(){

        System.out.println("looping through the arraylist");
        for (String cu : listUpdateFilesDoc) {
            System.out.println("list Update DOC entry : " + cu);
        }
    }
    public void showArrayListUpdateCom(){

        System.out.println("looping through the arraylist");
        for (String cu : listUpdateFilesCom) {
            System.out.println("list Update COM entry : " + cu);
        }
    }
    public void showArrayListUpdateTec(){

        System.out.println("looping through the arraylist");
        for (String cu : listUpdateFilesTec) {
            System.out.println("list Update TEC entry : " + cu);
        }
    }
    //endregion

    //region Internal Async tasks

    /**
     *  Downloading the updates for all 3 categories using async tasks
     *  Note : many FTP functions were internalised for use with a progress display
     *  The progress display showing individual file names was later removed and replaced
     *  with an unpassable progressdialog to ensure all files were correctly downloaded before continuing
     *  But all internalised mechanism were retained (this can be made more DRY , but has been changed so code
     *  can be recycled if need be)
     */
    class AsyncUpdatesDocumentsDownloadDL extends AsyncTask<String, Integer, String> {


        boolean status = false;
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
            pd = ProgressDialog.show(MenuActivity.this, "", "Getting Document Files...",
                    true, false);

            // block the screen from changing orientation
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            stopTime = System.currentTimeMillis();
            elapsedTime = (stopTime - startTime) /1000  ;
            Toast.makeText(MenuActivity.this, "Updates FINISHED\n" + listUpdateFilesDoc.size() + " updates downloaded\nin " + elapsedTime + " seconds", Toast.LENGTH_LONG).show();

            // allowing screen rotation
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            pd.dismiss();

            //for(int i=0; i<1000 ;i++) {
                MyStats.logEntry("UPDATE", "DOC-" + MyVars.updatedocserver);

            if (userType.equalsIgnoreCase("1")){
                strUserType = "Admin";
                updateCom();
            }

            if (status == true) {
                // only if download was succes set the update nr (local)
                setUpdatenrDoc(cntx);
                compareUpdateNrsDoc();
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
            try {
                FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
                int length = ftpFiles.length;
                fileList = new String[length];
                for (int i = 0; i < length; i++) {
                    String name = ftpFiles[i].getName();
                    boolean isFile = ftpFiles[i].isFile();

                    if (isFile) {
                        fileList[i] = "File :: " + name;


                        System.out.println("looping through the arraylist");
                        for (String cu : listUpdateFilesDoc) {
                            if (cu.equalsIgnoreCase(name)){

                                downloadedFile = name;

                                // download both the pdf & png file
                                status = ftpDownload(MyVars.FOLDER_DOCUMENTS + name,
                                        Environment.getExternalStorageDirectory() + MyVars.FOLDER_DOCUMENTS + name);

                                String imgName = name.replace(".pdf", ".PNG");
                                downloadedFile = imgName;

                                ftpDownload(MyVars.FOLDER_DOCUMENTS + imgName,
                                        Environment.getExternalStorageDirectory() + MyVars.FOLDER_DOCUMENTS + imgName);

                                // old test to see how many fields were in the file name (used by Ardovlam to give structure to it's documents)
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

                    } else {
                        fileList[i] = "Directory :: " + name;
                    }

                }

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

                status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
                desFileStream.close();

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
    class AsyncUpdatesCommercialDownloadDL extends AsyncTask<String, String, String> {


        private FTPfunctions ftpclient = null;
        ProgressDialog pd;
        boolean status = false;

        int iCount =0;
        int progress = 0;
        String downloadedFile = "" ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pd = ProgressDialog.show(MenuActivity.this, "", "Getting Commercial Files...",
                    true, false);

            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            pd.dismiss();

            //for(int i=0; i<1000 ;i++) {
                MyStats.logEntry("UPDATE", "COM-" + MyVars.updatecomserver);
            //}

            if (userType.equalsIgnoreCase("1")){
                strUserType = "Admin";
                updateTec();
            }

            if (status == true) {
                setUpdatenrCom(cntx);
                compareUpdateNrsCom();
            }
        }


        @Override
        protected String doInBackground(String... params) {


            ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);

            status = downloadUpdateFilesCom(MyVars.FOLDER_COMMERCIAL);

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


        public boolean downloadUpdateFilesCom(String dir_path) {
            boolean status = false;
            String[] fileList = null;
            int iNot5 = 0;
            try {
                FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
                int length = ftpFiles.length;
                fileList = new String[length];
                for (int i = 0; i < length; i++) {
                    String name = ftpFiles[i].getName();
                    boolean isFile = ftpFiles[i].isFile();

                    if (isFile) {
                        fileList[i] = "File :: " + name;

                        for (String cu : listUpdateFilesCom) {
                            if (cu.equalsIgnoreCase(name)){

                                downloadedFile = name;

                                // download both the pdf and png file
                                status = ftpDownload(MyVars.FOLDER_COMMERCIAL + name,
                                        Environment.getExternalStorageDirectory() + MyVars.FOLDER_COMMERCIAL + name);

                                String imgName = name.replace(".pdf", ".PNG");
                                downloadedFile = imgName;

                                ftpDownload(MyVars.FOLDER_COMMERCIAL + imgName,
                                        Environment.getExternalStorageDirectory() + MyVars.FOLDER_COMMERCIAL + imgName);

                                // old test for ardovlm file structure
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

                    } else {
                        fileList[i] = "Directory :: " + name;
                    }

                }

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

                status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
                desFileStream.close();

                // this because there is no other check on the validity of the file
                // if file doesn't exist it creates an empty file that needs to be deleted
                if (!status)
                {
                    File file = new File(desFilePath);
                    boolean deleted = file.delete();
                }
                else
                {
                    Boolean isImage = srcFilePath.contains(".PNG");
                    if (!isImage){
                        iCount +=1;
                        progress = iCount;
                    }
                }

                return status;
            } catch (Exception e) {
                System.out.println("download failed");
            }

            return status;
        }



    }
    class AsyncUpdatesTechnicalDownloadDL extends AsyncTask<String, String, String> {

        boolean status = false;
        private FTPfunctions ftpclient = null;
        ProgressDialog pd;

        int iCount =0;
        int progress = 0;
        String downloadedFile = "" ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pd = ProgressDialog.show(MenuActivity.this, "", "Getting Technical Files...",
                    true, false);

            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            pd.dismiss();
            //for(int i=0; i<100 ;i++) {
                MyStats.logEntry("UPDATE", "TEC-" + MyVars.updatetecserver);
            //}
            if (status == true) {

                setUpdatenrTec(cntx);
                compareUpdateNrsTec();
            }

        }


        @Override
        protected String doInBackground(String... params) {


            ftpConnect(MyVars.HOST, MyVars.USERNAME, MyVars.PASSWORD, 21);


            status = downloadUpdateFilesTec(MyVars.FOLDER_TECHNICAL);
            System.out.println(MyVars.FOLDER_TECHNICAL + " status : " + status);


            if (status == true) {
                System.out.println("Download success");
//                    handler.sendEmptyMessage(5);

//                setUpdatenrTec(cntx);
//                compareUpdateNrsTec();
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


        public boolean downloadUpdateFilesTec(String dir_path) {
            boolean status = false;
            String[] fileList = null;
            int iNot5 = 0;
            System.out.println("downloadUpdateFileCom in FTPfunctions");
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
                        System.out.println("listUpdateFiles.length : " + listUpdateFilesTec.size());
                        for (String cu : listUpdateFilesTec) {
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

                                status = ftpDownload(MyVars.FOLDER_TECHNICAL + name,
                                        Environment.getExternalStorageDirectory() + MyVars.FOLDER_TECHNICAL + name);




                                String imgName = name.replace(".pdf", ".PNG");
                                downloadedFile = imgName;
                                //publishProgress(iCount);
                                ftpDownload(MyVars.FOLDER_TECHNICAL + imgName,
                                        Environment.getExternalStorageDirectory() + MyVars.FOLDER_TECHNICAL + imgName);





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
    //endregion

}
