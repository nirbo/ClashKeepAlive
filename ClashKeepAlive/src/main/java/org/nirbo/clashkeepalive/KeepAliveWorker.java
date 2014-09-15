package org.nirbo.clashkeepalive;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class KeepAliveWorker {
    Context mContext;
    private static final int COC_RELAUNCH_INTERVAL = 5000;
    Handler mHandler = new Handler();

    public KeepAliveWorker(Context context) {
        mContext = context;
    }

    public Bundle getForegroundApp() {
        Bundle mAppDetails = null;

        try {
            mAppDetails = new GetForegroundApp(mContext).execute().get();
        } catch (InterruptedException ignore) {
        } catch (ExecutionException ignore) {}

        return mAppDetails;
    }

    public void executeLogic(String appName, String appPackage, int appPID) {
        final Runtime runtime = Runtime.getRuntime();

        try {
            runtime.exec("su -c kill " + appPID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mHandler.postDelayed(new StartCoc(appPackage), COC_RELAUNCH_INTERVAL);
    }

    private class StartCoc implements Runnable {
        String mCocPackageName;

        public StartCoc(String packageName) {
            mCocPackageName = packageName;
        }

        @Override
        public void run() {
            try {
                Intent mStartCocIntent = mContext.getPackageManager().getLaunchIntentForPackage(mCocPackageName);
                mContext.startActivity(mStartCocIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
