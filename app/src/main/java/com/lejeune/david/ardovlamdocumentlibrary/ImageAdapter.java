package com.lejeune.david.ardovlamdocumentlibrary;

import android.content.Context;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by David on 10/22/2016.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        System.out.println("orientation of activity: " + MyVars.currentOrientation);

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);



            switch (MyVars.currentOrientation) {
                case "portrait":
                    imageView.setLayoutParams(new GridView.LayoutParams(Math.round(MyVars.screenHeight / 4) , Math.round(MyVars.screenWidth / 2)));
                case "landscape":
                    imageView.setLayoutParams(new GridView.LayoutParams(Math.round(MyVars.screenWidth / 4) , Math.round(MyVars.screenHeight / 4)));
                case "reverse portrait":
                    imageView.setLayoutParams(new GridView.LayoutParams(Math.round(MyVars.screenHeight / 4) , Math.round(MyVars.screenWidth / 2)));
                case "reverse landscape":
                    imageView.setLayoutParams(new GridView.LayoutParams(Math.round(MyVars.screenWidth / 4) , Math.round(MyVars.screenHeight / 4)));
                //default:
                    //
            }



            //imageView.setLayoutParams(new GridView.LayoutParams(Math.round(MyVars.screenWidth / 4) , Math.round(MyVars.screenHeight / 4)));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.inbraak, R.drawable.branddetectie,
            R.drawable.gasdetectie, R.drawable.camera,
            R.drawable.toegang, R.drawable.geintegreerd
            //test to see if extra sections still shows = yes with gridview scrollable
            //,
            //R.drawable.a, R.drawable.b,
            //R.drawable.c, R.drawable.d

    };
}