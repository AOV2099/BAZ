package com.aov2099.baz;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;



public class AddressService extends JobService {

    final  FirestoreCalls writeAddress = new  FirestoreCalls();

    public static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "JobStarted");

        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(JobParameters params){
        new Thread(new Runnable(){

            @Override
            public void run() {

                writeAddress.pushAddressData();

                Log.d(TAG, "Address Updated finished");
                jobFinished(params, false);
            }

        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }




}
