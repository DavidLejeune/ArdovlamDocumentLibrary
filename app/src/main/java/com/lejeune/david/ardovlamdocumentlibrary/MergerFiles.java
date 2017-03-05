package com.lejeune.david.ardovlamdocumentlibrary;

/**
 * Created by Lucian on 3/6/2017.
 *
 * source : http://www.programcreek.com/2012/09/merge-files-in-java/
 */

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MergerFiles {
    public static File[] files = null;
    public static void main(String[] args) {

    }

    public static void addFileToList(String sourceFilePath, int iCountIndex) {


        files[iCountIndex] = new File(sourceFilePath);



    }
    public static void createBigLogFile() {

//        File dir = Environment.getExternalStorageDirectory();
//        File file = null;
//        file = new File(dir, MyVars.FOLDER_DATA + "all_users.txt");
//
//        String sourceFilePath = "/home/programcreek/Desktop/s1";
//        String sourceFile2Path = "/home/programcreek/Desktop/s2";
//
//        String mergedFilePath = "/home/programcreek/Desktop/m";
//
//        File[] files = new File[2];
//        files[0] = new File(sourceFile1Path);
//        files[1] = new File(sourceFile2Path);
//
//
        File dir = Environment.getExternalStorageDirectory();
        File mergedFile = new File(dir, MyVars.FOLDER_DATA + "all_users.txt");

        mergeFiles(files, mergedFile);


    }

    public static void mergeFiles(File[] files, File mergedFile) {

        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(mergedFile, true);
            out = new BufferedWriter(fstream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (File f : files) {
            System.out.println("merging: " + f.getName());
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                String aLine;
                while ((aLine = in.readLine()) != null) {
                    out.write(aLine);
                    out.newLine();
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File[] getAllLogFiles() {
        File folder = new File("path/toYour/dir");
        File[] files = folder.listFiles();
        return files;

    }
}


