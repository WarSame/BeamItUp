package com.example.graeme.beamitup.utils;

import rx.Scheduler;
import rx.schedulers.Schedulers;

public class AndroidSchedulers {
    public static Scheduler mainThread(){
        return Schedulers.from(new UiThreadExecutor());
    }
}
