package com.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {
    public static ProgressDialog createProgress(Activity activity){
        ProgressDialog pd = ProgressDialog.show(activity, "","Please wait....", true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return pd;
    }
    public static boolean isNetworkConnected(Activity activity){
        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!= null;
    }
}
