package org.nirbo.clashkeepalive;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.*;
import android.util.Log;
import org.nirbo.clashkeepalive.Utilities.Utils;

import java.io.IOException;

public class KeepAliveWorker {
    Context mContext;
    private static final int COC_RELAUNCH_INTERVAL = Utils.randomNumber(10000, 4500);
    private static final int COC_BREAK_INTERVAL = Utils.randomNumber(660000, 480000);
    private long mBreakCounter = 0;
    private int mRandomBreakInterval = 0;
    Handler mHandler = new Handler();

    public KeepAliveWorker(Context context) {
        mContext = context;
    }

    public Bundle getForegroundApp() {
        Bundle mAppDetails = null;

        try {
            mAppDetails = new GetForegroundApp(mContext).execute().get();
        } catch (Exception ignore) {}

        return mAppDetails;
    }

    public void executeLogic(String appName, String appPackage, int appPID) {
        final Runtime runtime = Runtime.getRuntime();
        long mCurrentTime = Utils.getCurrentTimeSeconds();

        if (mBreakCounter == 0) {
            mBreakCounter = Utils.getCurrentTimeSeconds();
            mRandomBreakInterval = Utils.randomNumber(25200, 7200);
        }

        try {
            runtime.exec("su -c kill " + appPID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long mBreakTime = (mBreakCounter + mRandomBreakInterval);
        if (mCurrentTime > mBreakTime) {
            mHandler.postDelayed(new StartCoc(appPackage), COC_BREAK_INTERVAL);
            mBreakCounter = 0;
        } else {
            mHandler.postDelayed(new StartCoc(appPackage), COC_RELAUNCH_INTERVAL);
        }
    }

    private class StartCoc implements Runnable {
        String mCocPackageName;

        public StartCoc(String packageName) {
            mCocPackageName = packageName;
        }

        @Override
        public void run() {
            try {
                Utils.launchCoc(mContext, mCocPackageName);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
