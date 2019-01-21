package com.example.graeme.beamitup;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.graeme.beamitup.notifications.NotificationBuilderProvider;
import com.example.graeme.beamitup.notifications.NotificationWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationsTest {
    private NotificationCompat.Builder notificationBuilder;
    private NotificationWrapper notificationWrapper;

    @Before
    public void setUp(){
        NotificationBuilderProvider notificationBuilderProvider = Mockito.mock(NotificationBuilderProvider.class);
        notificationBuilder = Mockito.mock(NotificationCompat.Builder.class, Mockito.RETURNS_SELF);

        when(notificationBuilderProvider.get()).thenReturn(notificationBuilder);

        NotificationManager notificationManager = Mockito.mock(NotificationManager.class);

        notificationWrapper = new NotificationWrapper(notificationBuilderProvider, notificationManager);
    }

    @Test
    public void createNotification_ValuesShouldMatch(){
        String title = "sometitle";
        String text = "sometext";
        int color = 500;
        long[] pattern = new long[]{0, 1, 2, 3, 100};
        String tag = "sometag";

        notificationWrapper.postNotification(
                title,
                text,
                color,
                pattern,
                tag
        );

        verify(notificationBuilder)
                .setContentTitle(title)
                .setContentText(text);
    }
}
