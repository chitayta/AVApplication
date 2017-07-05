package com.tma.tctay.utility;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by tctay on 7/4/2017.
 */

public class NetworkUtility {

    public static boolean isNetworkAvailable(Context appContext)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
