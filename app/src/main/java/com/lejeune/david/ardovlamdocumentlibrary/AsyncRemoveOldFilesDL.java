package com.lejeune.david.ardovlamdocumentlibrary;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lucian on 3/8/2017.
 */



public class AsyncRemoveOldFilesDL extends AsyncTask{

    private static ArrayList<String> listFilesDoc;
    private static ArrayList<String> listFilesTec;
    private static ArrayList<String> listFilesCom;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        listFilesDoc = new ArrayList<>();
        listFilesCom = new ArrayList<>();
        listFilesTec = new ArrayList<>();


    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        showArrayListDoc();
        showArrayListCom();
        showArrayListTec();





    }

    @Override
    protected Object doInBackground(Object[] objects) {

        createListOfFiles(MyVars.FOLDER_DOCUMENTS , listFilesDoc);
        createListOfFiles(MyVars.FOLDER_COMMERCIAL , listFilesCom);
        createListOfFiles(MyVars.FOLDER_TECHNICAL , listFilesTec);
        return null;
    }


    private void createListOfFiles(String strFolder , ArrayList listFiles){

        String path = Environment.getExternalStorageDirectory().toString()+ strFolder;
        File directory = new File(path);
        File[] files = directory.listFiles();
        System.out.println("#files : "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            boolean isFile = files[i].isFile();

            if (isFile) {
                String strFile = files[i].getName();
                String[] separated = strFile.split("-");
                String departmentID = separated[0];
                Boolean isImage = strFile.contains(".PNG");
                if (!isImage){
                    listFiles.add(strFile);
                }
            }
        }


    }


    private void showArrayListDoc(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesDoc) {
            System.out.println("list DOC files : " + cu);
        }
    }
    private void showArrayListCom(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesCom) {
            System.out.println("list COM files : " + cu);
        }
    }
    private void showArrayListTec(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesTec) {
            System.out.println("list TEC files : " + cu);
        }
    }

    private void loopCSVfile(File file){


    }

    private void deleteFile(File file){
        
    }

}
