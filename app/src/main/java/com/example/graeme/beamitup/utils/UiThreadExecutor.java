package com.example.graeme.beamitup.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class UiThreadExecutor implements Executor {
    @Override
    public void execute(@NonNull Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
