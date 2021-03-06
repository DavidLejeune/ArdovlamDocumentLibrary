package com.lejeune.david.ardovlamdocumentlibrary;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lucian on 2/23/2017.
 */

public class MyStats {

    //region Adding to user stat file
    /**
     *  Add an entry to the logfile , can be used for all types of lo entries
     */
    public static void logEntry(String entryType , String entryMessage) {

        if(entryMessage.length()<1)
        {
            entryMessage="x";
        }
        String textToWrite = MyVars.fullname + "," + MyTools.getDate() + "," + MyTools.getTime() + "," +
                entryType + "," + entryMessage + "\n";


        File dir = Environment.getExternalStorageDirectory();
        File file = null;


        if(MyVars.fullname!=null&&MyVars.registereduser.equalsIgnoreCase("1")){
            file = new File(dir, MyVars.FOLDER_DATA + MyVars.fullname + ".txt");
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream oFile = null;
        try {
            oFile = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            oFile.write(textToWrite.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //endregion

    //region Big stat file
    /**
     *  Combine all individual user stat files into 1 single big file
     */
    public static void createBigLogFile() {
        File dir = Environment.getExternalStorageDirectory();
        File file = null;
        file = new File(dir, MyVars.FOLDER_DATA + "all_users.txt");
        if (file.exists()) {
            FileOutputStream oFile = null;
            try {
                oFile = new FileOutputStream(file, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                String textToWrite = "";
                oFile.write(textToWrite.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                oFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logStatEntryToBigLogFile(String logMessage){

        File dir = Environment.getExternalStorageDirectory();
        File file = null;
        file = new File(dir, MyVars.FOLDER_DATA + "all_users.txt");
        String textToWrite = logMessage;

        FileOutputStream oFile = null;
        try {
            oFile = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            oFile.write(textToWrite.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //endregion

    //region Filtering the Big stat file
    /**
     *  Creating the arrays for both Menth and Hourly stats
     */
    public static void createMonthArrays(){


        MyTimer myTimer = new MyTimer("createMonthArrays");
        MyVars.arrJanuary = new int[31];
        for (int i = 0; i < MyVars.arrJanuary.length; i++) {
            MyVars.arrJanuary[i] = 0 ;
        }

        MyVars.arrFebruary = new int[29];
        for (int i = 0; i < MyVars.arrFebruary.length; i++) {
            MyVars.arrFebruary[i] = 0 ;
        }

        MyVars.arrMarch = new int[31];
        for (int i = 0; i < MyVars.arrMarch.length; i++) {
            MyVars.arrMarch[i] = 0 ;
        }

        MyVars.arrApril = new int[30];
        for (int i = 0; i < MyVars.arrApril.length; i++) {
            MyVars.arrApril[i] = 0 ;
        }

        MyVars.arrMay = new int[31];
        for (int i = 0; i < MyVars.arrMay.length; i++) {
            MyVars.arrMay[i] = 0 ;
        }

        MyVars.arrJune = new int[30];
        for (int i = 0; i < MyVars.arrJune.length; i++) {
            MyVars.arrJune[i] = 0 ;
        }

        MyVars.arrJuly = new int[31];
        for (int i = 0; i < MyVars.arrJuly.length; i++) {
            MyVars.arrJuly[i] = 0 ;
        }

        MyVars.arrAugustus = new int[31];
        for (int i = 0; i < MyVars.arrAugustus.length; i++) {
            MyVars.arrAugustus[i] = 0 ;
        }

        MyVars.arrSeptember = new int[30];
        for (int i = 0; i < MyVars.arrSeptember.length; i++) {
            MyVars.arrSeptember[i] = 0 ;
        }

        MyVars.arrOctober = new int[31];
        for (int i = 0; i < MyVars.arrOctober.length; i++) {
            MyVars.arrOctober[i] = 0 ;
        }

        MyVars.arrNovember = new int[30];
        for (int i = 0; i < MyVars.arrNovember.length; i++) {
            MyVars.arrNovember[i] = 0 ;
        }

        MyVars.arrDecember = new int[31];
        for (int i = 0; i < MyVars.arrDecember.length; i++) {
            MyVars.arrDecember[i] = 0 ;
        }
        myTimer.getElapsedTime();


    }
    public static void createMonthHourArrays(){


        MyTimer myTimer = new MyTimer("createMonthHourArrays");
        MyVars.arrJanuaryHour = new int[24];
        MyVars.arrFebruaryHour = new int[24];
        MyVars.arrMarchHour = new int[24];
        MyVars.arrAprilHour = new int[24];
        MyVars.arrMayHour = new int[24];
        MyVars.arrJuneHour = new int[24];
        MyVars.arrJulyHour = new int[24];
        MyVars.arrAugustusHour = new int[24];
        MyVars.arrSeptemberHour = new int[24];
        MyVars.arrOctoberHour = new int[24];
        MyVars.arrNovemberHour = new int[24];
        MyVars.arrDecemberHour = new int[24];
        for (int i = 0; i < MyVars.arrJanuaryHour.length; i++) {
            MyVars.arrJanuaryHour[i] = 0 ;
            MyVars.arrFebruaryHour[i] = 0 ;
            MyVars.arrMarchHour[i] = 0 ;
            MyVars.arrAprilHour[i] = 0 ;
            MyVars.arrMayHour[i] = 0 ;
            MyVars.arrJuneHour[i] = 0 ;
            MyVars.arrJulyHour[i] = 0 ;
            MyVars.arrAugustusHour[i] = 0 ;
            MyVars.arrSeptemberHour[i] = 0 ;
            MyVars.arrOctoberHour[i] = 0 ;
            MyVars.arrNovemberHour[i] = 0 ;
            MyVars.arrDecemberHour[i] = 0 ;
        }
        myTimer.getElapsedTime();


    }


    /**
     *  Search the big stat file and filter based on year and stat type
     */
    public static void filterBigStatFiles(String filterStatYear , String filterStatType){

        MyVars.totalStatRecords = 0;

//        MyVars.arrListUsers = new ArrayList<String>();
//        MyVars.arrListUsers.add("");


        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "all_users.txt");
        MyTimer myTimer = new MyTimer("filterBigStatFiles");

        String line = "";
        int iCount=0;
        if (file.exists()) {

            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        {
                            String[] str = line.split(",");
                            iCount+=1;
                            MyVars.totalStatRecords += 1;
                            String strID = str[0].toLowerCase();
                            String strUser = str[0].toLowerCase();

//                            if (MyVars.arrListUsers.contains(strUser)) {
//                                System.out.println("Account found");
//                            } else {
//                                System.out.println("Account not found");
//                                MyVars.arrListUsers.add(strUser);
//                            }


                            String strDate = str[1].toLowerCase();
                            String strTime = str[2].toLowerCase();
                            String strType = str[3].toLowerCase();
                            String strExtra = str[4].toLowerCase();

                            String strYear = strDate.substring(0,4);
                            String strMonth = strDate.substring(4,6);
                            String strDay = strDate.substring(6);

                            int iDay = Integer.parseInt(strDay);

                            String strHour = strTime.substring(0,2);
                            String strMinute = strTime.substring(3,5);

                            int iHour = Integer.parseInt(strHour);

                            //first check the year
                            if(filterStatYear.equalsIgnoreCase(strYear)){
                                //then check the stat type
                                if (filterStatType.length()>0){
                                    if(filterStatType.equalsIgnoreCase(strType))
                                    {
//                                        if(MyVars.filterStatUser==null){
//                                            MyVars.filterStatUser = "";
//                                        }
                                        if(MyVars.filterStatUser.length()>0){
                                            if(MyVars.filterStatUser.equalsIgnoreCase(strUser)){
                                                // logging the days
                                                switch (strMonth) {
                                                    case "01":
                                                        MyVars.arrJanuary[iDay - 1] = MyVars.arrJanuary[iDay - 1] + 1;
                                                        MyVars.arrJanuaryHour[iHour] = MyVars.arrJanuaryHour[iHour] + 1;
                                                        break;
                                                    case "02":
                                                        MyVars.arrFebruary[iDay - 1] = MyVars.arrFebruary[iDay - 1] + 1;
                                                        MyVars.arrFebruaryHour[iHour] = MyVars.arrFebruaryHour[iHour] + 1;
                                                        break;
                                                    case "03":
                                                        MyVars.arrMarch[iDay - 1] = MyVars.arrMarch[iDay - 1] + 1;
                                                        MyVars.arrMarchHour[iHour] = MyVars.arrMarchHour[iHour] + 1;
                                                        break;
                                                    case "04":
                                                        MyVars.arrApril[iDay - 1] = MyVars.arrApril[iDay - 1] + 1;
                                                        MyVars.arrAprilHour[iHour] = MyVars.arrAprilHour[iHour] + 1;
                                                        break;
                                                    case "05":
                                                        MyVars.arrMay[iDay - 1] = MyVars.arrMay[iDay - 1] + 1;
                                                        MyVars.arrMayHour[iHour] = MyVars.arrMayHour[iHour] + 1;
                                                        break;
                                                    case "06":
                                                        MyVars.arrJune[iDay - 1] = MyVars.arrJune[iDay - 1] + 1;
                                                        MyVars.arrJuneHour[iHour] = MyVars.arrJuneHour[iHour] + 1;
                                                        break;
                                                    case "07":
                                                        MyVars.arrJuly[iDay - 1] = MyVars.arrJuly[iDay - 1] + 1;
                                                        MyVars.arrJulyHour[iHour] = MyVars.arrJulyHour[iHour] + 1;
                                                        break;
                                                    case "08":
                                                        MyVars.arrAugustus[iDay - 1] = MyVars.arrAugustus[iDay - 1] + 1;
                                                        MyVars.arrAugustusHour[iHour] = MyVars.arrAugustusHour[iHour] + 1;
                                                        break;
                                                    case "09":
                                                        MyVars.arrSeptember[iDay - 1] = MyVars.arrSeptember[iDay - 1] + 1;
                                                        MyVars.arrSeptemberHour[iHour] = MyVars.arrSeptemberHour[iHour] + 1;
                                                        break;
                                                    case "10":
                                                        MyVars.arrOctober[iDay - 1] = MyVars.arrOctober[iDay - 1] + 1;
                                                        MyVars.arrOctoberHour[iHour] = MyVars.arrOctoberHour[iHour] + 1;
                                                        break;
                                                    case "11":
                                                        MyVars.arrNovember[iDay - 1] = MyVars.arrNovember[iDay - 1] + 1;
                                                        MyVars.arrNovemberHour[iHour] = MyVars.arrNovemberHour[iHour] + 1;
                                                        break;
                                                    case "12":
                                                        MyVars.arrDecember[iDay - 1] = MyVars.arrDecember[iDay - 1] + 1;
                                                        MyVars.arrDecemberHour[iHour] = MyVars.arrDecemberHour[iHour] + 1;
                                                        break;
                                                }

                                            }

                                        }
                                        else {
                                            // logging the days
                                            switch (strMonth) {
                                                case "01":
                                                    MyVars.arrJanuary[iDay - 1] = MyVars.arrJanuary[iDay - 1] + 1;
                                                    MyVars.arrJanuaryHour[iHour] = MyVars.arrJanuaryHour[iHour] + 1;
                                                    break;
                                                case "02":
                                                    MyVars.arrFebruary[iDay - 1] = MyVars.arrFebruary[iDay - 1] + 1;
                                                    MyVars.arrFebruaryHour[iHour] = MyVars.arrFebruaryHour[iHour] + 1;
                                                    break;
                                                case "03":
                                                    MyVars.arrMarch[iDay - 1] = MyVars.arrMarch[iDay - 1] + 1;
                                                    MyVars.arrMarchHour[iHour] = MyVars.arrMarchHour[iHour] + 1;
                                                    break;
                                                case "04":
                                                    MyVars.arrApril[iDay - 1] = MyVars.arrApril[iDay - 1] + 1;
                                                    MyVars.arrAprilHour[iHour] = MyVars.arrAprilHour[iHour] + 1;
                                                    break;
                                                case "05":
                                                    MyVars.arrMay[iDay - 1] = MyVars.arrMay[iDay - 1] + 1;
                                                    MyVars.arrMayHour[iHour] = MyVars.arrMayHour[iHour] + 1;
                                                    break;
                                                case "06":
                                                    MyVars.arrJune[iDay - 1] = MyVars.arrJune[iDay - 1] + 1;
                                                    MyVars.arrJuneHour[iHour] = MyVars.arrJuneHour[iHour] + 1;
                                                    break;
                                                case "07":
                                                    MyVars.arrJuly[iDay - 1] = MyVars.arrJuly[iDay - 1] + 1;
                                                    MyVars.arrJulyHour[iHour] = MyVars.arrJulyHour[iHour] + 1;
                                                    break;
                                                case "08":
                                                    MyVars.arrAugustus[iDay - 1] = MyVars.arrAugustus[iDay - 1] + 1;
                                                    MyVars.arrAugustusHour[iHour] = MyVars.arrAugustusHour[iHour] + 1;
                                                    break;
                                                case "09":
                                                    MyVars.arrSeptember[iDay - 1] = MyVars.arrSeptember[iDay - 1] + 1;
                                                    MyVars.arrSeptemberHour[iHour] = MyVars.arrSeptemberHour[iHour] + 1;
                                                    break;
                                                case "10":
                                                    MyVars.arrOctober[iDay - 1] = MyVars.arrOctober[iDay - 1] + 1;
                                                    MyVars.arrOctoberHour[iHour] = MyVars.arrOctoberHour[iHour] + 1;
                                                    break;
                                                case "11":
                                                    MyVars.arrNovember[iDay - 1] = MyVars.arrNovember[iDay - 1] + 1;
                                                    MyVars.arrNovemberHour[iHour] = MyVars.arrNovemberHour[iHour] + 1;
                                                    break;
                                                case "12":
                                                    MyVars.arrDecember[iDay - 1] = MyVars.arrDecember[iDay - 1] + 1;
                                                    MyVars.arrDecemberHour[iHour] = MyVars.arrDecemberHour[iHour] + 1;
                                                    break;
                                            }
                                        }




                                    }
                                }
                            }

                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            myTimer.getElapsedTime();
        }
        else
        {
        }

    }

    public static void buildArrayStatUser(){
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "all_users.txt");
        MyTimer myTimer = new MyTimer("buildArrayStatUser");
        MyVars.arrListUsers = new ArrayList<String>();
        MyVars.arrListUsers.add("");
        String line = "";
        int iCount=0;
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0)
                    {
                        String[] str = line.split(",");
                        iCount+=1;
                        //System.out.println(iCount + " " + line);
                        //String strID = str[0].toLowerCase();
                        String strUser = str[0];
                        if (MyVars.arrListUsers.contains(strUser)) {
                            //System.out.println("Account found");
                        } else {
                            System.out.println("Account not found , user " + strUser + " added to arraylist");
                            MyVars.arrListUsers.add(strUser);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("file export");
            myTimer.getElapsedTime();
            //viewAllDataSQLite();
        }
        else
        {
            System.out.println("File documents doc not found");
        }
    }

    public static void resetStatVars(){

        MyVars.filterStatType = "";
        MyVars.filterStatYear = "";
        MyVars.filterStatMonth = "";
    }
    //endregion

}





