package org.nirbo.clashkeepalive.Utilities;

import android.content.Context;
import android.widget.Toast;

public class Utils {

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)
                .show();
    }

}
