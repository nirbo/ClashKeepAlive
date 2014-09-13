package org.nirbo.clashkeepalive;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeepAliveService extends Service {
    private KeepAliveWorker mWorkerService;
    protected ScheduledExecutorService mScheduledExecutor;
    private Bundle mAppDetails;
    private String cocName = "Clash of Clans";

    @Override
    public void onCreate() {
        super.onCreate();

        mWorkerService = new KeepAliveWorker(this);
        mScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WorkerRunnable mWorkerRunnable = new WorkerRunnable(this);

        this.mScheduledExecutor.scheduleAtFixedRate(mWorkerRunnable, 0, 5, TimeUnit.SECONDS);

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mScheduledExecutor.shutdownNow();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startServiceTask() {
        mAppDetails = mWorkerService.getForegroundApp();
        String mAppName = mAppDetails.getString("AppName");
        String mAppPackage = mAppDetails.getString("AppPackage");
        int mAppPID = mAppDetails.getInt("AppPID");

        if (mAppName.equals(cocName)) {
            mWorkerService.executeLogic(mAppName, mAppPackage, mAppPID);
        }
    }

    private class WorkerRunnable implements Runnable {
        KeepAliveService mService;

        public WorkerRunnable(KeepAliveService service) {
            mService = service;
        }

        @Override
        public void run() {
            startServiceTask();
        }
    }


}
