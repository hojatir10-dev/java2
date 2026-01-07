package com.example.smartstudent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Random;

public class MotivationService extends Service {

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        runnable = new Runnable() {
            @Override
            public void run() {

                String[] quotes = {
                        "هر روز یک قدم به جلو بردار",
                        "موفقیت نتیجه پشتکار است",
                        "به خودت ایمان داشته باش",
                        "شروع، مهم‌تر از کامل بودن است"
                };
                String text = quotes[new Random().nextInt(quotes.length)];
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                handler.postDelayed(this, 60000); 
            }
        };
        handler.post(runnable);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
