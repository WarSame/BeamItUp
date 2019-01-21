package com.example.graeme.beamitup.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationManagerCompat;

import com.example.graeme.beamitup.R;

public class NotificationWrapper {
    private final NotificationBuilderProvider notificationBuilderProvider;
    private final NotificationManager notificationManager;

    public NotificationWrapper(
            NotificationBuilderProvider notificationBuilderProvider,
            NotificationManager notificationManager
    ){
        this.notificationBuilderProvider = notificationBuilderProvider;
        this.notificationManager = notificationManager;
    }

    public void postNotification(
            String title,
            String text,
            int color,
            long[] pattern,
            String tag
    ){
        Notification notification = notificationBuilderProvider.get()
                .setContentTitle(title)
                .setContentText(text)
                .setColor(color)
                .setSmallIcon(R.drawable.ic_beamitup)
                .setVibrate(pattern)
                .build();

        notificationManager.notify(tag, 1, notification);
    }
}
