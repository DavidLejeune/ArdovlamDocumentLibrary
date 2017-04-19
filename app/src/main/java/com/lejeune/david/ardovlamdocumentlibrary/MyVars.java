package com.lejeune.david.ardovlamdocumentlibrary;

import java.util.ArrayList;

/**
 * Created by Lucian on 2/23/2017.
 */


public class MyVars {

    public final static String ROOT_FOLDER = "/DocLib/";
    public final static String FOLDER_DATA = ROOT_FOLDER + ".Data/";
    public final static String FOLDER_STATS = FOLDER_DATA + ".Stats/";
    public final static String FOLDER_DOCUMENTS = ROOT_FOLDER + ".Documents/";
    public final static String FOLDER_IMG = ROOT_FOLDER + ".IMG/";
    public final static String FOLDER_COMMERCIAL = ROOT_FOLDER + ".Commercial/";
    public final static String FOLDER_TECHNICAL = ROOT_FOLDER + ".Technical/";


    public static String firstname, lastname, birthdate, registereduser, usertype, fullname , updatedoclocal , updatedocserver , updatecomlocal, updatecomserver , updateteclocal, updatetecserver;


    public final static String HOST = "ftp.ardovlam.be";
    public final static String USERNAME = "ArdoVlam";
    public final static String PASSWORD = "ftptdav09";


    public static int screenWidth = 0;
    public static int screenHeight=0;

    public static int [] arrJanuary;
    public static int [] arrFebruary;
    public static int [] arrMarch;
    public static int [] arrApril;
    public static int [] arrMay;
    public static int [] arrJune;
    public static int [] arrJuly;
    public static int [] arrAugustus;
    public static int [] arrSeptember;
    public static int [] arrOctober;
    public static int [] arrNovember;
    public static int [] arrDecember;

    public static int [] arrJanuaryHour;
    public static int [] arrFebruaryHour;
    public static int [] arrMarchHour;
    public static int [] arrAprilHour;
    public static int [] arrMayHour;
    public static int [] arrJuneHour;
    public static int [] arrJulyHour;
    public static int [] arrAugustusHour;
    public static int [] arrSeptemberHour;
    public static int [] arrOctoberHour;
    public static int [] arrNovemberHour;
    public static int [] arrDecemberHour;


    public static String filterStatType;
    public static String filterStatUser;
    public static String filterStatYear;
    public static String filterStatMonth;

    public static int totalStatRecords;

    public static String [] arrUsers;
    public static ArrayList<String> arrListUsers;

    public static String currentOrientation;
}
