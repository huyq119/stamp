package com.example.stamp.ui;

/**
 * Created by Administrator on 2016/6/24.
 */


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

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

        Log.e("什么时候执行-------------->>", "执行了");
        SharedPreferences sp = getSharedPreferences("stamp", MODE_PRIVATE);
        sp.edit().putBoolean("isSetup", true).commit();
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

    @Override
    public void onTerminate() {
        Log.e("什么时候执行---------->", "执行了");
        super.onTerminate();
        SharedPreferences sp = getSharedPreferences("stamp", MODE_PRIVATE);
        sp.edit().putBoolean("isSetup", true).commit();
    }
}