//package com.lejeune.david.ardovlamdocumentlibrary;
//
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//
///**
// * Created by Lucian on 3/14/2017.
// */
//
//public class AsyncCreateStatForYearDL  extends AsyncTask<String, String, String> {
//
//
//    ProgressDialog pd;
//
//
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//
//        pd = ProgressDialog.show(StatsActivity.this, "", "Creating single stat for this year..",
//                true, false);
//
//        myStats.createMonthArrays();
//        myStats.createMonthHourArrays();
//
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//
//        pd.dismiss();
//        gotoGraph();
//
//    }
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//
//
//        MyStats.filterBigStatFiles(MyVars.filterStatYear, MyVars.filterStatType);
//
//        return null;
//    }
//
//
//
//}
