package com.lejeune.david.ardovlamdocumentlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

// We can create custom adapters
class MyAdapter extends ArrayAdapter<String> {

    ImageView imgThumbnail;
    String strFile;
    String strThumbnail;
    View theView;


    public MyAdapter(Context context, String[] values){

        super(context, R.layout.row_layout, values);

    }

    // Override getView which is responsible for creating the rows for our list
    // position represents the index we are in for the array.

    // convertView is a reference to the previous view that is available for reuse. As
    // the user scrolls the information is populated as needed to conserve memory.

    // A ViewGroup are invisible containers that hold a bunch of views and
    // define their layout properties.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // The LayoutInflator puts a layout into the right View
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        // inflate takes the resource to load, the parent that the resource may be
        // loaded into and true or false if we are loading into a parent view.
        theView = theInflater.inflate(R.layout.row_layout , parent, false);

        // We retrieve the text from the array
        strFile = getItem(position);
        strThumbnail = strFile.replace(".pdf", ".PNG");

        // Get the TextView we want to edit
        TextView txtRowItem =(TextView) theView.findViewById(R.id.txtRowItem);

        // Put the next TV Show into the TextView
        txtRowItem.setText(strFile);

        // Get the ImageView in the layout
        ImageView imgThumbnail = (ImageView) theView.findViewById(R.id.imgThumbnail);

        // We can set a ImageView like this
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,MyVars.FOLDER_DOCUMENTS + strThumbnail);
        if(FilterActivity.chkDocuments.isChecked() && !FilterActivity.chkCommercial.isChecked() ){
            file = new File(dir,MyVars.FOLDER_DOCUMENTS + strThumbnail);
        }

        if(FilterActivity.chkCommercial.isChecked() && !FilterActivity.chkDocuments.isChecked() ){
            file = new File(dir,MyVars.FOLDER_COMMERCIAL + strThumbnail);
        }

        System.out.println("thumbnail : " + file.toString());
        long length = file.length();
        System.out.println("length:"+length);

        if (length > 1){
            if(file.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imgThumbnail.setImageBitmap(myBitmap);
            }
        }
        else
        {
            imgThumbnail.setImageResource(R.drawable.dot);
        }

        return theView;

    }
}
