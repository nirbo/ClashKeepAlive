package org.nirbo.clashkeepalive.Utilities;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;
import org.nirbo.clashkeepalive.KeepAliveService;

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
}
