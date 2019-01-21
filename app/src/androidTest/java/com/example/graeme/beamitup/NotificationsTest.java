package com.example.graeme.beamitup;

import android.app.NotificationManager;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.graeme.beamitup.notifications.NotificationBuilderProvider;
import com.example.graeme.beamitup.notifications.NotificationWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationsTest {
    @Mock
    private NotificationCompat.Builder notificationBuilder;
    @Mock
    private NotificationWrapper notificationWrapper;
    @Mock
    private NotificationManager notificationManager;

    @Before
    public void setUp(){
        NotificationBuilderProvider notificationBuilderProvider = Mockito.mock(NotificationBuilderProvider.class);
        notificationBuilder = Mockito.mock(NotificationCompat.Builder.class, RETURNS_SELF);

        when(notificationBuilderProvider.get()).thenReturn(notificationBuilder);

        notificationManager = Mockito.mock(NotificationManager.class);

        notificationWrapper = new NotificationWrapper(notificationBuilderProvider, notificationManager);
    }

    @Test
    public void createNotification_ValuesShouldMatch(){
        String title = "sometitle";
        String text = "sometext";
        int color = Color.YELLOW;
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
                .setContentTitle(title);
        verify(notificationBuilder)
                .setContentText(text);
        verify(notificationBuilder)
                .setColor(color);
        verify(notificationBuilder)
                .setVibrate(pattern);

        //verify(notificationManager)
        //        .notify(tag, 1, null);
    }
}
