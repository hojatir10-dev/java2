package com.example.smartstudent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    @Override
    public int onStartCommand(Intent i, int f, int id) {
        new Thread(() -> {
            while (true) {
                Log.d("SERVICE", "Service is running...");
                try { Thread.sleep(10000); }
                catch (Exception ignored) {}
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
