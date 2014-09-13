package org.nirbo.clashkeepalive;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import org.nirbo.clashkeepalive.Utilities.Utils;

public class GetForegroundApp extends AsyncTask<Void, Void, Bundle> {
    Context mContext;

    public GetForegroundApp(Context context) {
        this.mContext = context;
    }

    @Override
    protected Bundle doInBackground(Void... params) {
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

    @Override
    protected void onPostExecute(Bundle appDetails) {
        super.onPostExecute(appDetails);

//        if (appDetails.getString("AppName") != null) {
//            Utils.toastMessage(mContext, appDetails.getString("AppName"));
//        } else {
//            Utils.toastMessage(mContext, "NULL");
//        }
    }


}
