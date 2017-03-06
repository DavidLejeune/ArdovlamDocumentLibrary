package com.lejeune.david.ardovlamdocumentlibrary;

/**
 * Created by Lucian on 2/27/2017.
 */

public class MyTimer {
    long startTime ;
    long stopTime ;
    long elapsedTime ;
    String strTask;

    public MyTimer(String strTask) {
        this.strTask = strTask;
        startTime = System.currentTimeMillis();
    }

    public void getElapsedTime(){
        stopTime = System.currentTimeMillis();
        elapsedTime = (stopTime - startTime)  ;
        System.out.println("Time elapsed (ms) for " + strTask + " : " + elapsedTime);
    }
}
