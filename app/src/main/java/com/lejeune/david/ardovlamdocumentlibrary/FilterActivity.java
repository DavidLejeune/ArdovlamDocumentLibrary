package com.lejeune.david.ardovlamdocumentlibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.lejeune.david.ardovlamdocumentlibrary.MenuActivity.filterID;
import static com.lejeune.david.ardovlamdocumentlibrary.MyFilter.listTypeDocNames;


public class FilterActivity extends Activity {

    //region Declarations
    TextView txtResultFilter, txtDepartment;
    public static CheckBox chkDocuments , chkCommercial , chkTechnical;
    Button btnShowFilteredDocs;
    String departmentTag = "";
    public static ArrayList<String> listFilteredFiles;
    public static int iCountTotalDocs=0;
    String userType;
    String docTypeFilter = "";

    Spinner spinDocType;
    ArrayAdapter<String> adapterSpinner;
    TextView lblDocType;
    ArrayList<String> tempDocType;

    int iCountOccurence;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        ProgressDialog pd = ProgressDialog.show(FilterActivity.this, "", "Searching ...",
                true, false);


        txtResultFilter = (TextView) findViewById(R.id.txtResultFilter);
        txtDepartment = (TextView) findViewById(R.id.txtDepartment);

        chkDocuments = (CheckBox) findViewById(R.id.chkDocuments);
        chkCommercial = (CheckBox) findViewById(R.id.chkCommercial);
        chkDocuments.setVisibility(View.INVISIBLE);
        chkCommercial.setVisibility(View.INVISIBLE);
        chkDocuments.setChecked(false);
        chkCommercial.setChecked(false);
        chkTechnical = (CheckBox) findViewById(R.id.chkTechnical);
        chkTechnical.setVisibility(View.INVISIBLE);
        chkTechnical.setChecked(false);

        chkDocuments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                        if(isChecked){
                                                            if (chkCommercial.isChecked()){
                                                                chkCommercial.setChecked(false);
                                                                spinDocType.setVisibility(View.VISIBLE);
                                                                lblDocType.setVisibility(View.VISIBLE);
                                                            }
                                                            if (chkTechnical.isChecked()){
                                                                chkTechnical.setChecked(false);
                                                                spinDocType.setVisibility(View.VISIBLE);
                                                                lblDocType.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                        resetTxtResult();
                                                        getFilteredDocuments();
                                                    }
                                                }
        );


        chkCommercial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                         if(isChecked){
                                                             if (chkDocuments.isChecked()){
                                                                 chkDocuments.setChecked(false);
                                                                 docTypeFilter = "" ;
                                                                 spinDocType.setVisibility(View.INVISIBLE);
                                                                 lblDocType.setVisibility(View.INVISIBLE);
                                                             }
                                                             if (chkTechnical.isChecked()){
                                                                 chkTechnical.setChecked(false);
                                                                 spinDocType.setVisibility(View.INVISIBLE);
                                                                 lblDocType.setVisibility(View.INVISIBLE);
                                                             }
                                                             resetTxtResult();
                                                             getFilteredDocuments();
                                                         }
                                                     }
                                                 }
        );

        btnShowFilteredDocs = (Button) findViewById(R.id.btnShowFilteredDocs);
        btnShowFilteredDocs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(docTypeFilter.length()<1){
                    for(int i=0; i<100 ;i++) {
                        MyStats.logEntry("SEARCH", filterID + "-x");
                    }
                }
                else
                {
                    for(int i=0; i<100 ;i++) {
                        MyStats.logEntry("SEARCH", filterID + "-" + docTypeFilter);
                    }
                }
                resetTxtResult();
                showFilteredResult();
            }
        });

        setViewOfFilter();


        MyFilter.buildVariableTypeDocList();
        showArrayListDocType();
        countOccurencesOnDocType();



        lblDocType = (TextView) findViewById(R.id.lblDocType);
        spinDocType = (Spinner) findViewById(R.id.spinDocType);
        adapterSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item,listTypeDocNames);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDocType.setAdapter(adapterSpinner);
        spinDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDocType = parent.getItemAtPosition(position).toString();
                docTypeFilter = MyFilter.findVariableTypeDoc(selectedDocType);
                setViewOfFilter();
                getFilteredDocuments();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        showArrayListTempDocType();

        pd.dismiss();



        chkTechnical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                        if(isChecked){

                                                            if (chkDocuments.isChecked()){
                                                                chkDocuments.setChecked(false);
                                                                docTypeFilter = "" ;
                                                                spinDocType.setVisibility(View.INVISIBLE);
                                                                lblDocType.setVisibility(View.INVISIBLE);
                                                            }


                                                            if (chkCommercial.isChecked()){
                                                                chkCommercial.setChecked(false);
                                                                spinDocType.setVisibility(View.INVISIBLE);
                                                                lblDocType.setVisibility(View.INVISIBLE);
                                                            }


                                                            resetTxtResult();
                                                            getFilteredDocuments();
                                                        }
                                                    }
                                                }
        );

    }


    private void countOccurencesOnDocType(){



        tempDocType = new ArrayList<>();
        for (String cu : listTypeDocNames) {
            if (chkDocuments.isChecked()) {
                docTypeFilter = MyFilter.findVariableTypeDoc(cu);
                getDepartmentTag();
                getFilteredDocumentList();
                if (iCountOccurence>0){
                    tempDocType.add(cu);
                }
            }
        }
        listTypeDocNames = tempDocType;
        docTypeFilter = "";




    }

    private void resetTxtResult(){
        txtDepartment.setText(filterID);
        userType = MyVars.usertype;
        String result = "";
        if(userType.equalsIgnoreCase("1")){
            chkDocuments.setVisibility(View.VISIBLE);
            chkCommercial.setVisibility(View.VISIBLE);
            result = "You have admin rights";
        }
        if(userType.equalsIgnoreCase("2")){
            chkCommercial.setVisibility(View.VISIBLE);
            getFilteredDocuments();
            result = "You have projectleader rights";
        }
        if(userType.equalsIgnoreCase("0")){
            chkDocuments.setVisibility(View.VISIBLE);
            result = "You have installer rights";
        }
        txtResultFilter.setText(result);
    }
    private void setViewOfFilter(){
        txtDepartment.setText(filterID);
        userType = MyVars.usertype;
        String result = "";
        if(userType.equalsIgnoreCase("1")){
            chkDocuments.setVisibility(View.VISIBLE);
            chkCommercial.setVisibility(View.VISIBLE);
            chkTechnical.setVisibility(View.VISIBLE);
            chkDocuments.setChecked(true);
            result = "You have admin rights";
        }
        if(userType.equalsIgnoreCase("2")){
            chkDocuments.setVisibility(View.INVISIBLE);
            chkTechnical.setVisibility(View.INVISIBLE);
            chkCommercial.setVisibility(View.VISIBLE);
            chkCommercial.setChecked(true);
            getFilteredDocuments();
            result = "You have projectleader rights";
        }
        if(userType.equalsIgnoreCase("0")){
            chkDocuments.setVisibility(View.VISIBLE);
            chkTechnical.setVisibility(View.INVISIBLE);
            chkCommercial.setVisibility(View.INVISIBLE);
            chkDocuments.setChecked(true);
            result = "You have installer rights";
        }
        if(userType.equalsIgnoreCase("3")){
            chkTechnical.setVisibility(View.VISIBLE);
            chkTechnical.setChecked(true);
            result = "You have technician rights";
        }
        txtResultFilter.setText(result);
    }

    private void showFilteredResult(){
        if(!chkDocuments.isChecked() && !chkCommercial.isChecked() && !chkTechnical.isChecked())
        {
            Toast.makeText(FilterActivity.this , "you need to check which type",Toast.LENGTH_LONG).show();
        }
        if(chkDocuments.isChecked() || chkCommercial.isChecked() || chkTechnical.isChecked() )
        {
            getFilteredDocuments();
            gotoListDocs();
        }
    }

    private void getFilteredDocuments(){
        getDepartmentTag();
        if(chkDocuments.isChecked()) getFilteredDocumentList();
        if(chkCommercial.isChecked())  getFilteredCommercialList();
        if(chkTechnical.isChecked())  getFilteredTechnicalList();
    }
    private void getDepartmentTag(){
        departmentTag = MyFilter.findVariableDepartment(filterID);
        System.out.println("departmentTag : " +departmentTag );
    }


    private void getFilteredTechnicalList(){

        iCountTotalDocs=0;
        iCountOccurence = 0;
        int iNot5 = 0;

        listFilteredFiles = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString()+ MyVars.FOLDER_TECHNICAL;
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
                    iCountTotalDocs += 1 ;
                }


                if (departmentID.equalsIgnoreCase(departmentTag))
                {
                    if (!isImage){

                        if( docTypeFilter.length()>0)
                        {
                            Boolean docTypeFound = strFile.contains("-" + docTypeFilter);
                            if(docTypeFound)
                            {
                                iCountOccurence += 1 ;
                                listFilteredFiles.add(strFile);
                            }
                        }
                        else
                        {
                            iCountOccurence += 1 ;
                            listFilteredFiles.add(strFile);
                        }

                    }
                }


            }
        }

        txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);

    }
    private void getFilteredDocumentList(){
        iCountTotalDocs=0;
        iCountOccurence = 0;
        int iNot5 = 0;

        listFilteredFiles = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString()+ MyVars.FOLDER_DOCUMENTS;
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
                    iCountTotalDocs += 1 ;
                }


                if (departmentID.equalsIgnoreCase(departmentTag))
                {
                    if (!isImage){

                        if( docTypeFilter.length()>0)
                        {
                            Boolean docTypeFound = strFile.contains("-" + docTypeFilter);
                            if(docTypeFound)
                            {
                                iCountOccurence += 1 ;
                                listFilteredFiles.add(strFile);
                            }
                        }
                        else
                        {
                            iCountOccurence += 1 ;
                            listFilteredFiles.add(strFile);
                        }

                    }
                }


            }
        }

        txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);
        System.out.println("Total files in folder : " + iCountTotalDocs);
        System.out.println("# files with same filter : " + iCountOccurence);
    }
    private void getFilteredCommercialList(){
        iCountTotalDocs=0;
        iCountOccurence = 0;
        int iNot5 = 0;

        listFilteredFiles = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString()+ MyVars.FOLDER_COMMERCIAL;
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
                    iCountTotalDocs += 1 ;
                }


                if (departmentID.equalsIgnoreCase(departmentTag))
                {
                    if (!isImage){

                        if( docTypeFilter.length()>0)
                        {
                            Boolean docTypeFound = strFile.contains("-" + docTypeFilter);
                            if(docTypeFound)
                            {
                                iCountOccurence += 1 ;
                                listFilteredFiles.add(strFile);
                            }
                        }
                        else
                        {
                            iCountOccurence += 1 ;
                            listFilteredFiles.add(strFile);
                        }

                    }
                }


            }
        }

        txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);

    }


    private void gotoListDocs(){
        final Intent listDocs = new Intent(FilterActivity.this, ListDocsActivity.class);
        FilterActivity.this.startActivity(listDocs);

        if(userType.equalsIgnoreCase("2")){
            FilterActivity.this.finish();
        }
    }


    public void showArrayListDocType(){

        System.out.println("looping through the arraylist var type");
        for (String cu : listTypeDocNames) {
            System.out.println("list doc type entry : " + cu);
        }
    }

    public void showArrayListTempDocType(){

        System.out.println("looping through the temp arraylist var type");
        for (String cu : tempDocType) {
            System.out.println("list doc type entry : " + cu);
        }
    }
}
