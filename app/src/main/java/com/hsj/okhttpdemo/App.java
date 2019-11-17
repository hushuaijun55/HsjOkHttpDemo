package com.hsj.okhttpdemo;

import android.app.Application;
import android.content.Context;

/**
 * Create by hsj55
 * 2019/11/17
 */
public class App extends Application {
    private static App mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }
}
