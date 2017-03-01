package com.lejeune.david.ardovlamdocumentlibrary;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Lucian on 2/23/2017.
 */

public class MyStats {

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

    public static void createMonthArrays(){


        MyTimer myTimer = new MyTimer();
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

    public static void filterBigStatFiles(String filterStatYear , String filterStatType){
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "all_users.txt");
        MyTimer myTimer = new MyTimer();
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
                            //System.out.println(iCount + " " + line);

                            String strID = str[0].toLowerCase();
                            String strUser = str[0].toLowerCase();
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

                            //first check the year
                            if(filterStatYear.equalsIgnoreCase(strYear)){
                                //then check the stat type
                                if (filterStatType.length()>0){
                                    if(filterStatType.equalsIgnoreCase(MyVars.filterStatType))
                                    {
                                        //System.out.println(strUser + " " + iDay);
                                        switch(strMonth) {
                                            case "01":
                                                MyVars.arrJanuary[iDay - 1] = MyVars.arrJanuary[iDay - 1] +1;
                                                break;
                                            case "02":
                                                //System.out.println(MyVars.arrFebruary[iDay - 1]);
                                                MyVars.arrFebruary[iDay - 1] = MyVars.arrFebruary[iDay - 1] +1;
                                                break;
                                            case "03":
                                                MyVars.arrMarch[iDay - 1] = MyVars.arrMarch[iDay - 1] +1;
                                                break;
                                            case "04":
                                                MyVars.arrApril[iDay - 1] = MyVars.arrApril[iDay - 1] +1;
                                                break;
                                            case "05":
                                                MyVars.arrMay[iDay - 1] = MyVars.arrMay[iDay - 1] +1;
                                                break;
                                            case "06":
                                                MyVars.arrJune[iDay - 1] = MyVars.arrJune[iDay - 1] +1;
                                                break;
                                            case "07":
                                                MyVars.arrJuly[iDay - 1] = MyVars.arrJuly[iDay - 1] +1;
                                                break;
                                            case "08":
                                                MyVars.arrAugustus[iDay - 1] = MyVars.arrAugustus[iDay - 1] +1;
                                                break;
                                            case "09":
                                                MyVars.arrSeptember[iDay - 1] = MyVars.arrSeptember[iDay - 1] +1;
                                                break;
                                            case "10":
                                                MyVars.arrOctober[iDay - 1] = MyVars.arrOctober[iDay - 1] +1;
                                                break;
                                            case "11":
                                                MyVars.arrNovember[iDay - 1] = MyVars.arrNovember[iDay - 1] +1;
                                                break;
                                            case "12":
                                                MyVars.arrDecember[iDay - 1] = MyVars.arrDecember[iDay - 1] +1;
                                                break;
                                        }
                                    }
                                }
                            }






//                            //insertDataSQLite(strUser , strDate, strTime, strType, strExtra);
//                            if (strUser.equalsIgnoreCase("David Lejeune")){
//                                //System.out.println("yeah its me");
//                                //System.out.println(strYear);
//                                //System.out.println(strMonth);
//                                //System.out.println(strDay);
//                                System.out.println(strHour);
//                                System.out.println(strMinute);
//                            }

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


}





