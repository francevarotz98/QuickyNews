package it.unipd.dei.esp1920.quickynews.connections;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;
import android.net.ConnectivityManager;
import android.widget.Toast;

import static android.net.NetworkInfo.State.CONNECTED;


public class NetConnectionReceiver extends BroadcastReceiver {

    private static final String TAG="NetConnectionReceiver";
    private NetworkInfo wifi;
    private NetworkInfo mobile;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Sono in onReceive",Toast.LENGTH_LONG).show();
        String action=intent.getAction();
        Log.i(TAG,"onReceive() method");

        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobile=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.getState()==CONNECTED){ //before I check wifi because faster than mobile network connection
            Log.d(TAG,"Connected to wifi ");
        }
        else if (mobile.getState()==CONNECTED){
            Log.d(TAG,"Connected to internet with mobile");
        }
        else{
            Log.d(TAG,"NOT connected to internet");
        }

    }
}
