package org.nirbo.clashkeepalive;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import org.nirbo.clashkeepalive.Utilities.Utils;

public class MainFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        mContext = getActivity().getApplicationContext();

        Switch mServiceSwitch = (Switch) rootView.findViewById(R.id.serviceSwitch);
        mServiceSwitch.setOnCheckedChangeListener(this);

        if (isServiceRunning()) {
            mServiceSwitch.setChecked(true);
            Log.i("NIR", "Service is Running");
        } else {
            Log.i("NIR", "Service is Stopped");
        }

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Intent mServiceIntent = new Intent(mContext, KeepAliveService.class);

        if (isChecked) {
            Utils.toastMessage(mContext, "Service Started");
            mContext.startService(mServiceIntent);
        } else {
            Utils.toastMessage(mContext, "Service Stopped");
            mContext.stopService(mServiceIntent);
        }
    }

    private boolean isServiceRunning() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (KeepAliveService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
