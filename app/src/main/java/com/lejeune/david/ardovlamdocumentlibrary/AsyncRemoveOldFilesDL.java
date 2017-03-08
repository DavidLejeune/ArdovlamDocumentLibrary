package com.lejeune.david.ardovlamdocumentlibrary;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lucian on 3/8/2017.
 */



public class AsyncRemoveOldFilesDL extends AsyncTask{

    private static ArrayList<String> listFilesDocLocal;
    private static ArrayList<String> listFilesTecLocal;
    private static ArrayList<String> listFilesComLocal;

    private static ArrayList<String> listFilesDocCSV;
    private static ArrayList<String> listFilesTecCSV;
    private static ArrayList<String> listFilesComCSV;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        listFilesDocLocal = new ArrayList<>();
        listFilesComLocal = new ArrayList<>();
        listFilesTecLocal = new ArrayList<>();

        listFilesDocCSV = new ArrayList<>();
        listFilesComCSV = new ArrayList<>();
        listFilesTecCSV = new ArrayList<>();


    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        showArrayListDocLocal();
        showArrayListComLocal();
        showArrayListTecLocal();
        showArrayListDocCSV();
        showArrayListComCSV();
        showArrayListTecCSV();

        compareFileList(listFilesDocLocal , listFilesDocCSV , "Documents");
        compareFileList(listFilesComLocal , listFilesComCSV , "Commercial");
        compareFileList(listFilesTecLocal , listFilesTecCSV , "Technical");

//        loopCSVfile("Documents.csv" , listFilesDocLocal);
//        loopCSVfile("Commercial.csv" , listFilesComLocal);
//        loopCSVfile("Technical.csv" , listFilesTecLocal);




    }

    @Override
    protected Object doInBackground(Object[] objects) {

        createListOfFilesLocal(MyVars.FOLDER_DOCUMENTS , listFilesDocLocal);
        createListOfFilesLocal(MyVars.FOLDER_COMMERCIAL , listFilesComLocal);
        createListOfFilesLocal(MyVars.FOLDER_TECHNICAL , listFilesTecLocal);

        createListOfFilesCSV("Documents.csv" , listFilesDocCSV);
        createListOfFilesCSV("Commercial.csv" , listFilesComCSV);
        createListOfFilesCSV("Technical.csv" , listFilesTecCSV);
        return null;
    }


    private void createListOfFilesLocal(String strFolder , ArrayList listFiles){

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
    private void createListOfFilesCSV(String fileName , ArrayList listFiles){
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + fileName);

        MyTimer myTimer = new MyTimer("loopCSVfile " + fileName);
        String strFile = "";
        String line = "";
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        {
                            String[] str = line.split(",");
                            //System.out.println(iCount + " " + line);

                            strFile = str[2].toLowerCase();
                            listFiles.add(strFile);
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
            System.out.println("File documents doc not found");
        }

    }


    private void showArrayListDocCSV(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesDocCSV) {
            System.out.println("list DOC files csv: " + cu);
        }
    }
    private void showArrayListComCSV(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesComCSV) {
            System.out.println("list COM files csv: " + cu);
        }
    }
    private void showArrayListTecCSV(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesTecCSV) {
            System.out.println("list TEC files csv: " + cu);
        }
    }

    private void showArrayListDocLocal(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesDocLocal) {
            System.out.println("list DOC files : " + cu);
        }
    }
    private void showArrayListComLocal(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesComLocal) {
            System.out.println("list COM files : " + cu);
        }
    }
    private void showArrayListTecLocal(){
        System.out.println("looping through the arraylist");
        for (String cu : listFilesTecLocal) {
            System.out.println("list TEC files : " + cu);
        }
    }

    private void compareFileList(ArrayList local , ArrayList csv , String strDepartment){
        MyTimer myTimer = new MyTimer("compareFileList ");
        String fileNameLocal = "";
        String fileNameCSV = "";
        File dir = Environment.getExternalStorageDirectory();


        for (Object localFile : local)
        {
            Boolean foundInCSV = false;

            fileNameLocal = localFile.toString().toLowerCase();
            for (Object csvFile : csv) {
                fileNameCSV = csvFile.toString().toLowerCase();
                if(fileNameCSV.equalsIgnoreCase(fileNameLocal)){
                    foundInCSV = true;
                }
            }


            if(!foundInCSV)
            {
                System.out.println("This file needs to be deleted : " + fileNameLocal);


                File fileToDelete = null;
                File imgToDelete = null;
                String strImg = fileNameLocal.replace(".pdf", ".png");
                if (strDepartment.equalsIgnoreCase("Documents")){
                    fileToDelete = new File(dir, MyVars.FOLDER_DOCUMENTS + fileNameLocal);
                    imgToDelete = new File(dir, MyVars.FOLDER_DOCUMENTS + strImg);
                }
                if (strDepartment.equalsIgnoreCase("Commercial")){
                    fileToDelete = new File(dir, MyVars.FOLDER_COMMERCIAL + fileNameLocal);
                    imgToDelete = new File(dir, MyVars.FOLDER_COMMERCIAL + strImg);
                }
                if (strDepartment.equalsIgnoreCase("Technical")){
                    fileToDelete = new File(dir, MyVars.FOLDER_TECHNICAL + fileNameLocal);
                    imgToDelete = new File(dir, MyVars.FOLDER_TECHNICAL + strImg);
                }
                deleteFile(fileToDelete,imgToDelete);
            }

        }
        myTimer.getElapsedTime();

    }


    private void deleteFile(File file,File img){
        boolean deleted = file.delete();
        System.out.println(file.toString() + " delete status  = " + deleted);
        if (!file.exists()) {
            System.out.println("succesfully deleted file");
        }
        boolean deletedImg = img.delete();
        System.out.println(img.toString() + " delete status img = " + deletedImg);
        if (!img.exists()) {
            System.out.println("succesfully deleted file");
        }
    }

}
