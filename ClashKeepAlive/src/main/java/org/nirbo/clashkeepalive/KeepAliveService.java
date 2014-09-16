package org.nirbo.clashkeepalive;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import org.nirbo.clashkeepalive.Utilities.Utils;

public class KeepAliveService extends Service {
    private KeepAliveWorker mWorkerService;
    private Bundle mAppDetails;
    private String cocName = "Clash of Clans";
    private Handler mHandler;
    private WorkerRunnable mWorkerRunnable;
    private long mRandomInterval;

    @Override
    public void onCreate() {
        super.onCreate();

        mWorkerService = new KeepAliveWorker(this);
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWorkerRunnable = new WorkerRunnable(this);
        mRandomInterval = Utils.randomNumber();

        mHandler.post(mWorkerRunnable);

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mWorkerRunnable);
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

            mRandomInterval = Utils.randomNumber();
            Log.i("NIR", "New Random Interval: " + mRandomInterval);
            mHandler.postDelayed(mWorkerRunnable, mRandomInterval);
        }
    }


}
