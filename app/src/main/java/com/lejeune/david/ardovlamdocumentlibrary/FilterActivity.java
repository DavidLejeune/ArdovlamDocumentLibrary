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

        //region Initialisations
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
        //endregion

        //region Misc actions
        setViewOfFilter();
        MyFilter.buildVariableTypeDocList();
        showArrayListDocType();
        countOccurencesOnDocType();
        //endregion


        //region chkDocuments
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
                                                        countOccurencesOnDocType();
                                                    }
                                                }
        );
        //endregion

        //region chkCommercial
        chkCommercial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                         if(isChecked){
                                                             if (chkDocuments.isChecked()){
                                                                 chkDocuments.setChecked(false);
                                                                 docTypeFilter = "" ;
                                                                 spinDocType.setVisibility(View.VISIBLE);
                                                                 lblDocType.setVisibility(View.VISIBLE);
                                                             }
                                                             if (chkTechnical.isChecked()){
                                                                 chkTechnical.setChecked(false);
                                                                 spinDocType.setVisibility(View.VISIBLE);
                                                                 lblDocType.setVisibility(View.VISIBLE);
                                                             }
                                                             resetTxtResult();
                                                             getFilteredDocuments();
                                                             countOccurencesOnDocType();
                                                         }
                                                     }
                                                 }
        );
        //endregion

        //region btnShowFilteredDocs
        btnShowFilteredDocs = (Button) findViewById(R.id.btnShowFilteredDocs);
        btnShowFilteredDocs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(docTypeFilter.length()<1){
                    //for(int i=0; i<1000 ;i++) {
                        MyStats.logEntry("SEARCH", filterID + "-x");
                    //}
                }
                else
                {
                    //for(int i=0; i<100 ;i++) {
                        MyStats.logEntry("SEARCH", filterID + "-" + docTypeFilter);
                    //}
                }
                resetTxtResult();
                showFilteredResult();
            }
        });
        //endregion





        //region Spinner Doc Type
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
                //setViewOfFilter();
                resetTxtResult();
                getFilteredDocuments();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        showArrayListTempDocType();
        pd.dismiss();

        //region chktechnical
        chkTechnical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                        if(isChecked){

                                                            if (chkDocuments.isChecked()){
                                                                chkDocuments.setChecked(false);
                                                                docTypeFilter = "" ;
                                                                spinDocType.setVisibility(View.VISIBLE);
                                                                lblDocType.setVisibility(View.VISIBLE);
                                                            }


                                                            if (chkCommercial.isChecked()){
                                                                chkCommercial.setChecked(false);
                                                                spinDocType.setVisibility(View.VISIBLE);
                                                                lblDocType.setVisibility(View.VISIBLE);
                                                            }

                                                            resetTxtResult();
                                                            getFilteredDocuments();
                                                            countOccurencesOnDocType();
                                                        }
                                                    }
                                                }
        );
        //endregion

    }

    //region Misc methods
    private void countOccurencesOnDocType(){
        System.out.println("listTypeDocNames.size() " + listTypeDocNames.size());
        tempDocType = new ArrayList<>();
        for (String cu : listTypeDocNames) {
            System.out.println("cu : " +cu);
            if (chkDocuments.isChecked()) {
                docTypeFilter = MyFilter.findVariableTypeDoc(cu);
                getDepartmentTag();
                getFilteredDocumentList();
                if (iCountOccurence>0){
                    tempDocType.add(cu);
                }
            }
            if (chkTechnical.isChecked()) {
                docTypeFilter = MyFilter.findVariableTypeDoc(cu);
                getDepartmentTag();
                getFilteredTechnicalList();
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
            result = "You have project manager rights";
        }
        if(userType.equalsIgnoreCase("0")){
            chkDocuments.setVisibility(View.VISIBLE);
            result = "You have electrician rights";
        }
        if(userType.equalsIgnoreCase("3")){
            chkTechnical.setVisibility(View.VISIBLE);
            result = "You have service engineer rights";
        }
        txtResultFilter.setText(result);
    }
    private void setViewOfFilter(){
        // Depending on usertype show certin UI elements
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
            result = "You have electrician rights";
        }
        if(userType.equalsIgnoreCase("3")){
            chkDocuments.setVisibility(View.INVISIBLE);
            chkCommercial.setVisibility(View.INVISIBLE);
            chkTechnical.setVisibility(View.VISIBLE);
            chkTechnical.setChecked(true);
            result = "You have service engineer rights";
        }
        txtResultFilter.setText(result);
    }

    private void showFilteredResult(){
        // a checkbox must be checked or no result will be found
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

    private void gotoListDocs(){
        final Intent listDocs = new Intent(FilterActivity.this, ListDocsActivity.class);
        FilterActivity.this.startActivity(listDocs);

        if(userType.equalsIgnoreCase("2")){
            FilterActivity.this.finish();
        }
    }
    //endregion

    //region Filtering
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

                System.out.println("strFile " + strFile);
                System.out.println("docTypeFilter" + docTypeFilter);
                if (departmentID.equalsIgnoreCase(departmentTag))
                {
                    if (!isImage){

                        if( docTypeFilter.length()>0)
                        {
                            Boolean docTypeFound = strFile.contains("-" + docTypeFilter + "-");
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
        if (iCountOccurence!=0){

            txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);
        }
        else
        {
            txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);

        }

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
                            Boolean docTypeFound = strFile.contains("-" + docTypeFilter + "-");
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

        if (iCountOccurence!=0){

            txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);
        }
        else
        {
            resetTxtResult();
            txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);

        }
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
                            Boolean docTypeFound = strFile.contains("-" + docTypeFilter + "-");
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

        if (iCountOccurence!=0){

            txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);
        }
        else
        {
            txtResultFilter.setText(txtResultFilter.getText() +"\nNumber of files matching filter(s) : " + iCountOccurence);

        }

    }

    public void showArrayListDocType(){

        System.out.println("looping through the arraylist var type");
        for (String cu : listTypeDocNames) {
            System.out.println("showArrayListDocType doc type entry : " + cu);
        }
    }
    public void showArrayListTempDocType(){

        System.out.println("looping through the temp arraylist var type");
        for (String cu : tempDocType) {
            System.out.println("showArrayListTempDocType doc type entry : " + cu);
        }
    }
    //endregion

}
