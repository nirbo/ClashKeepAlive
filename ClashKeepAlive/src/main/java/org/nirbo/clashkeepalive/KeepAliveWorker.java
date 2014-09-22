package org.nirbo.clashkeepalive;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.*;
import org.joda.time.DateTime;
import org.nirbo.clashkeepalive.Utilities.Utils;

import java.io.IOException;

public class KeepAliveWorker {
    Context mContext;
    private static final int COC_RELAUNCH_INTERVAL = 5000;
    private static final int COC_BREAK_INTERVAL = 600000;
    Handler mHandler = new Handler();
    private DateTime mBreakCounter = null;

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
        DateTime mCurrentTime = new DateTime();
        int mRandomBreakInterval = Utils.randomNumber(7, 2);

        if (mBreakCounter == null) {
            mBreakCounter = new DateTime();
        }

        try {
            runtime.exec("su -c kill " + appPID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mBreakCounter.plusHours(mRandomBreakInterval).isAfter(mCurrentTime)) {
            mHandler.postDelayed(new StartCoc(appPackage), COC_BREAK_INTERVAL);
            mBreakCounter = null;
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
