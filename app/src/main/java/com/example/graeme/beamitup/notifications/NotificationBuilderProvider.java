package com.example.graeme.beamitup.notifications;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class NotificationBuilderProvider {
    private Context context;

    NotificationBuilderProvider(Context context){
        this.context = context;
    }

    public NotificationCompat.Builder get(){
        return new NotificationCompat.Builder(context, "BeamItUp");
    }
}
