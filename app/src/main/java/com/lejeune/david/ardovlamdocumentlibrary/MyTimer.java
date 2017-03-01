package com.lejeune.david.ardovlamdocumentlibrary;

/**
 * Created by Lucian on 2/27/2017.
 */

public class MyTimer {
    long startTime ;
    long stopTime ;
    long elapsedTime ;

    public MyTimer() {
        startTime = System.currentTimeMillis();
    }

    public void getElapsedTime(){
        stopTime = System.currentTimeMillis();
        elapsedTime = (stopTime - startTime) /1000 ;
        System.out.println("Time elapsed : " + elapsedTime);
    }
}
