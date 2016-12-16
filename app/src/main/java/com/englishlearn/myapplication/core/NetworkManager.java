package com.englishlearn.myapplication.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by yanzl on 16-10-11.
 */
public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager INSTANCE;
    private Context context;


    public NetworkManager(Context context) {
        this.context = context;
    }

    public static synchronized NetworkManager newInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NetworkManager(context);
        }
        return INSTANCE;
    }

    public boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

}
