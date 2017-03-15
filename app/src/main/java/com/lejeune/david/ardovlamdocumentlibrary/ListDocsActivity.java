package com.lejeune.david.ardovlamdocumentlibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import static com.lejeune.david.ardovlamdocumentlibrary.FilterActivity.listFilteredFiles;


public class ListDocsActivity extends Activity {

    ListAdapter myAdapter;
    TextView txtResultListDocs;
    String docSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_docs);


        txtResultListDocs = (TextView) findViewById(R.id.txtResultListDocs);
        setResult();
        //getActionBar().setTitle(MenuActivity.filterID + " documenten :");

        String[] mStringArray = new String[listFilteredFiles.size()];
        mStringArray = listFilteredFiles.toArray(mStringArray);

        myAdapter = new MyAdapter(this, mStringArray);
        final ListView lvDocs = (ListView) findViewById(R.id.lvDocs);
        lvDocs.setAdapter(myAdapter);
        lvDocs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = lvDocs.getAdapter().getItem(position);
                docSelected = obj.toString();

                openPDFFile();

                //String docSelected = String.valueOf(myAdapter.getItem(position));
                Toast.makeText(ListDocsActivity.this , docSelected , Toast.LENGTH_SHORT).show();
                System.out.println("item selected : " + docSelected);
            }
        });


    }

    private void setResult(){
        txtResultListDocs.setText("Total documents on storage : " + FilterActivity.iCountTotalDocs + "\nFound " + listFilteredFiles.size() + " files with filter(s) :\n" +
                    "    Department : " + MenuActivity.filterID);
    }

    public void openPDFFile() {
        File pdfFile = null;

        // folder choice is based on category (chbx checked)
        if(!FilterActivity.chkCommercial.isChecked() && FilterActivity.chkDocuments.isChecked() && !FilterActivity.chkTechnical.isChecked() ){
            pdfFile = new File(Environment.getExternalStorageDirectory(),MyVars.FOLDER_DOCUMENTS + docSelected);//File path
        }

        if(FilterActivity.chkCommercial.isChecked() && !FilterActivity.chkDocuments.isChecked() && !FilterActivity.chkTechnical.isChecked() ){
            pdfFile = new File(Environment.getExternalStorageDirectory(),MyVars.FOLDER_COMMERCIAL + docSelected);//File path
        }

        if(!FilterActivity.chkCommercial.isChecked() && !FilterActivity.chkDocuments.isChecked() && FilterActivity.chkTechnical.isChecked() ){
            pdfFile = new File(Environment.getExternalStorageDirectory(),MyVars.FOLDER_TECHNICAL + docSelected);//File path
        }


        if (pdfFile.exists()) //Checking for the file is exist or not
        {
            for(int i=0; i<1000 ;i++) {
                MyStats.logEntry("READ", docSelected);
            }
            Uri path = Uri.fromFile(pdfFile);
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(objIntent);//Staring the pdf viewer
        }
    }


}
