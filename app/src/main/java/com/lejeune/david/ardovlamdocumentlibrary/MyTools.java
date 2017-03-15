package com.lejeune.david.ardovlamdocumentlibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lucian on 2/19/2017.
 */

public class MyTools {

    // Method to check networstatus (wifi / mobile /no network)
    public static String checkNetworkStatus(final Context context) {

        String networkStatus = "";

        // Get connect mangaer
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // check for wifi
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // check for mobile data
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if( wifi.isConnectedOrConnecting () ) {
            networkStatus = "wifi";
        } else if( mobile.isConnectedOrConnecting () ) {
            networkStatus = "mobileData";
        } else {
            networkStatus = "noNetwork";
        }

        return networkStatus;

    }  // end checkNetworkStatus

    // creating the folders required on the internal storage
    public void createFolders() {

        MyTimer myTimer = new MyTimer("createFolders");
        String[] foldernames = {MyVars.ROOT_FOLDER,MyVars.FOLDER_DATA , MyVars.FOLDER_DOCUMENTS,MyVars.FOLDER_IMG , MyVars.FOLDER_COMMERCIAL,
                                MyVars.FOLDER_STATS , MyVars.FOLDER_TECHNICAL};

        for (String s: foldernames)
        {
            File root = new File(Environment.getExternalStorageDirectory(),s);
            if (!root.exists()) {
                root.mkdirs();
                if (root.exists()) {
                }
                else
                {
                }
            }
            else
            {
            }
        }
        myTimer.getElapsedTime();

    }

    public void retrieveSharedPref(Context cntx){


        MyVars.firstname = SharedPrefHelper.getInstance().getValue(cntx, "firstname");
        MyVars.lastname = SharedPrefHelper.getInstance().getValue(cntx, "lastname");
        MyVars.birthdate = SharedPrefHelper.getInstance().getValue(cntx, "date");
        MyVars.registereduser = SharedPrefHelper.getInstance().getValue(cntx, "registered");
        MyVars.usertype = SharedPrefHelper.getInstance().getValue(cntx, "usertype");
        MyVars.fullname = (MyVars.firstname + " " + MyVars.lastname);
        MyVars.updatedoclocal = SharedPrefHelper.getInstance().getValue(cntx, "updatedoclocal");
        MyVars.updatedocserver = SharedPrefHelper.getInstance().getValue(cntx, "updatedocserver");
        MyVars.updatecomlocal = SharedPrefHelper.getInstance().getValue(cntx, "updatecomlocal");
        MyVars.updatecomserver = SharedPrefHelper.getInstance().getValue(cntx, "updatecomserver");
        MyVars.updateteclocal = SharedPrefHelper.getInstance().getValue(cntx, "updateteclocal");
        MyVars.updatetecserver = SharedPrefHelper.getInstance().getValue(cntx, "updatetecserver");


    }

    public static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new Date());
        return currentTime;

    }

    public static String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        return currentDate;

    }

    public static void setStatCurrentYear(){
        // When stats are initialised there is no year, so we log for the current year first
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String strYear = "" + year;
        MyVars.filterStatYear = strYear;

    }

}
