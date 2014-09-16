package org.nirbo.clashkeepalive;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import org.nirbo.clashkeepalive.Utilities.Utils;

public class GetForegroundApp extends AsyncTask<Void, Void, Bundle> {
    Context mContext;

    public GetForegroundApp(Context context) {
        this.mContext = context;
    }

    @Override
    protected Bundle doInBackground(Void... params) {
        Bundle mAppDetails;
        return mAppDetails = Utils.getCocDetails(mContext);
    }

    @Override
    protected void onPostExecute(Bundle appDetails) {
        super.onPostExecute(appDetails);
    }


}
