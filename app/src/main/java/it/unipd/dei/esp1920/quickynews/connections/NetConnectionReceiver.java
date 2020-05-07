package it.unipd.dei.esp1920.quickynews.connections;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;
import android.net.ConnectivityManager;


public class NetConnectionReceiver extends BroadcastReceiver {

    private final String TAG="NetConnectionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        System.out.println("action= "+action);
        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifi.isAvailable()){ //before check wifi because faster than mobile network connection
            Log.d(TAG,"Connected to wifi");
        }
        else if (mobile.isAvailable()){
            Log.d(TAG,"Connected to internet with mobile");
        }
        else{
            Log.d(TAG,"NOT connected to internet");
        }

    }
}
