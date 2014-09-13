package org.nirbo.clashkeepalive;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class KeepAliveWorker {
    Context mContext;

    public KeepAliveWorker(Context context) {
        mContext = context;
    }

    public Bundle getForegroundApp() {
        Bundle mAppDetails = null;

        try {
            mAppDetails = new GetForegroundApp(mContext).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return mAppDetails;
    }

    public void executeLogic(String appName, int appPID) {
        Log.i("NIR", appName + " " + appPID);
    }



}
