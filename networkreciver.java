package com.example.smartstudent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            Toast.makeText(context, "اینترنت وصل شد", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "اینترنت قطع شد", Toast.LENGTH_SHORT).show();
        }
    }
}


