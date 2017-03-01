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

        System.out.println("findVariableDepartment for " + department);
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
                                System.out.println("filter letter : " + departmentTag);
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
            System.out.println("File Var_departement not found");
        }

        return departmentTag;
    }


    public static String findVariableTypeDoc(String doctype) {

        String typeDocFilter = "";

        System.out.println("findVariableTypeDoc for " + doctype);
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

                            System.out.println("typeDocID : " + typeDocID + " typeDocName : " + typeDocName);
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
            System.out.println("File Var_departement not found");
        }

        return typeDocFilter;
    }


    public static void buildVariableTypeDocList() {
        listTypeDocNames = new ArrayList<>();
        listTypeDocNames.add("");

        String typeDoc = "";

        System.out.println("findVariableTypeDoc for " + typeDoc);
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

                            System.out.println("typeDocID : " + typeDocID + " typeDocName : " + typeDocName);
                            typeDoc = typeDocName;
                            listTypeDocNames.add(typeDoc);
//                            if(department.equalsIgnoreCase(departmentName)){
//                                departmentTag = filterLetter;
//                                System.out.println("filter letter : " + departmentTag);
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
            System.out.println("File Var_type doc not found");
        }

    }


}
