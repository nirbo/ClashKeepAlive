package org.nirbo.clashkeepalive.Utilities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import org.nirbo.clashkeepalive.KeepAliveService;

import java.util.Random;

public class Utils {

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)
                .show();
    }

    public static boolean isServiceRunning(Context mContext) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);

        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (KeepAliveService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Bundle getCocDetails(Context mContext) {
        Bundle mAppDetails = new Bundle();
        String mForegroundAppPackage = null;
        String mForegroundAppName = null;
        int pid = -1;

        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo mForegroundTask = activityManager.getRunningTasks(1).get(0);
        String mForegroundPackage = mForegroundTask.topActivity.getPackageName();
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo mForegroundInfo = packageManager.getPackageInfo(mForegroundPackage, 0);
            mForegroundAppName = mForegroundInfo.applicationInfo.loadLabel(packageManager).toString();
            mForegroundAppPackage = mForegroundInfo.packageName.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.processName.equalsIgnoreCase(mForegroundAppPackage)) {
                pid = processInfo.pid;

                if (pid != -1) {
                    break;
                }
            }
        }

        mAppDetails.putString("AppName", mForegroundAppName);
        mAppDetails.putString("AppPackage", mForegroundAppPackage);
        mAppDetails.putInt("AppPID", pid);

        return mAppDetails;
    }

    public static void launchCoc(Context mContext, String mCocPackageName) {
        Intent mStartCocIntent = mContext.getPackageManager().getLaunchIntentForPackage(mCocPackageName);
        mContext.startActivity(mStartCocIntent);
    }

    public static int randomNumber() {
        Random random = new Random();
        int mRandNumber = 0;

        mRandNumber = random.nextInt(180);

        while (mRandNumber < 50) {
            mRandNumber = random.nextInt(180);
        }

        return mRandNumber;
    }
}
