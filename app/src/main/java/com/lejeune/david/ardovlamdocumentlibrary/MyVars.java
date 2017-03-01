package com.lejeune.david.ardovlamdocumentlibrary;

/**
 * Created by Lucian on 2/23/2017.
 */

public class MyVars {

    public final static String ROOT_FOLDER = "/DocLib/";
    public final static String FOLDER_DATA = ROOT_FOLDER + "Data/";
    public final static String FOLDER_STATS = FOLDER_DATA + "Stats/";
    public final static String FOLDER_DOCUMENTS = ROOT_FOLDER + "Documents/";
    public final static String FOLDER_IMG = ROOT_FOLDER + "IMG/";
    public final static String FOLDER_COMMERCIAL = ROOT_FOLDER + "Commercial/";


    public static String firstname, lastname, birthdate, registereduser, usertype, fullname , updatedoclocal , updatedocserver , updatecomlocal, updatecomserver;


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


    public static String filterStatType;
    public static String filterUser;
    public static String filterStatYear;
    public static String filterStatMonth;


}
