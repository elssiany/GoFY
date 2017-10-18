package com.dkbrothers.app.gofy.services;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


public class JackyGiftsJobService extends JobService {

    private static final String TAG = "JackyGiftsJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Realización de tareas de larga ejecución en tareas programadas");
        // TODO(developer): agrega tarea de ejecución larga aquí.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
