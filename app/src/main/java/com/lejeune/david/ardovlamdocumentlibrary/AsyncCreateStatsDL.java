//package com.lejeune.david.ardovlamdocumentlibrary;
//
//import android.os.AsyncTask;
//
///**
// * Created by Lucian on 3/14/2017.
// */
//
//public class AsyncCreateStatsDL  extends AsyncTask<String, String, String> {
//
//    GraphActivity graphActivity = null;
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//        MyStats.createMonthArrays();
//        MyStats.createMonthHourArrays();
//        graphActivity = new GraphActivity();
//
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        graphActivity.restartGraph();
//    }
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//
//        MyStats.filterBigStatFiles(MyVars.filterStatYear, MyVars.filterStatType);
//
//        return null;
//    }
//
//
//}
