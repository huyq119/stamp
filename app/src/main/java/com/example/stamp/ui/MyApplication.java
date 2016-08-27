package com.example.stamp.ui;

/**
 * Created by Administrator on 2016/6/24.
 */


import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 全局上下文
 *
 * @author Administrator
 */
public class MyApplication extends Application {

    private static MyApplication application = new MyApplication();
    private static int MainID;// 主线程的id
    private static Handler handler;// 获取主线程的handler

    public MyApplication() {
    }

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MainID = android.os.Process.myTid();
        handler = new Handler();
    }

    public static Context getApplication() {
        return application;
    }

    public static int getMainID() {
        return MainID;
    }

    public static Handler getHandler() {
        return handler;
    }


}