package it.unipd.dei.esp1920.quickynews.connections;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.util.Log;
import android.net.ConnectivityManager;
import android.widget.Toast;


public class NetConnectionReceiver extends BroadcastReceiver {

    private static final String TAG="NetConnectionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Sono in onReceive",Toast.LENGTH_LONG).show();
        String action=intent.getAction();
        Log.i(TAG,"onReceive() method ");
        System.out.println("action= "+action);
        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isAvailable()==true){ //before check wifi because faster than mobile network connection
            Log.d(TAG,"Connected to wifi");
        }
        else if (mobile.isAvailable()==true){
            Log.d(TAG,"Connected to internet with mobile");
        }
        else{
            Log.d(TAG,"NOT connected to internet");
        }

    }
}
