package com.example.smartstudent;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static void sendMotivation(Context context, String text) {

        PendingIntent pi = PendingIntent.getActivity(
                context,
                0,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "motivation_channel")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("پیام انگیزشی")
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setContentIntent(pi);

        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(100, builder.build());
    }
}
