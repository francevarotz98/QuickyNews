package it.unipd.dei.esp1920.quickynews.connections;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.net.ConnectivityManager;


import static android.net.NetworkInfo.State.CONNECTED;


public class NetConnectionReceiver extends BroadcastReceiver {

    private static final String TAG = "NetConnectionReceiver";
    private NetworkInfo wifi;
    private NetworkInfo mobile;
    private static boolean isConnected = false; //default

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        Log.d(TAG,"onReceive()");

        checkConnection(context);
    }

    public static void checkConnection(Context context) {
        Log.d(TAG,"checkConnection()");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi= cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.getState()==CONNECTED) { // before I check wifi because faster than mobile network connection
            Log.d(TAG,"Connected to wifi ");
            isConnected=true;
        }
        else if (mobile.getState()==CONNECTED) {
            Log.d(TAG,"Connected to internet with mobile");
            isConnected=true;
        }
        else {
            Log.d(TAG, "NOT connected to internet");
            isConnected = false;
        }
    }

    public static boolean isMetered(Context context) {
        Log.d(TAG,"isMetered()");
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.isActiveNetworkMetered();
    }

    public static boolean isConnected(Context context) {
        Log.d(TAG,"isConnected()");
        checkConnection(context);
        return isConnected;
    }
}
