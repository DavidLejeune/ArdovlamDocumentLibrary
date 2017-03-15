package com.lejeune.david.ardovlamdocumentlibrary;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lucian on 2/20/2017.
 */

public class MyFilter {

    public static ArrayList<String> listTypeDocNames;


    public static String findVariableDepartment(String department) {

        String departmentTag = "";

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "var_departement.txt");
        String line = "";

        if (file.exists()) {

            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        {
                            String[] str = line.split(",");

                            String filterLetter = str[0].toUpperCase();
                            String departmentName = str[1].toUpperCase();

                            if(department.equalsIgnoreCase(departmentName)){
                                departmentTag = filterLetter;
                            }
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
        }

        return departmentTag;
    }

    public static String findVariableTypeDoc(String doctype) {

        String typeDocFilter = "";

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "var_type_doc.txt");
        String line = "";

        if (file.exists()) {

            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        {
                            String[] str = line.split(",");

                            String typeDocID = str[0].toUpperCase();
                            String typeDocName = str[1].toUpperCase();

                            if(doctype.equalsIgnoreCase(typeDocName)){

                                typeDocFilter = typeDocID;
                            }
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
        }

        return typeDocFilter;
    }

    /**
     *  Builds an array list of available doctype for the department
     */
    public static void buildVariableTypeDocList() {
        listTypeDocNames = new ArrayList<>();
        listTypeDocNames.add("");

        String typeDoc = "";

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, MyVars.FOLDER_DATA + "var_type_doc.txt");
        String line = "";

        if (file.exists()) {

            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        {
                            String[] str = line.split(",");

                            String typeDocID = str[0].toUpperCase();
                            String typeDocName = str[1].toUpperCase();

                            typeDoc = typeDocName;
                            listTypeDocNames.add(typeDoc);
//                            }
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
        }

    }


}
