package cn.com.chinau.ui;

/**
 * Created by Administrator on 2016/6/24.
 */


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Set;

import cn.com.chinau.StaticField;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.utils.MyLog;

/**
 * 全局上下文
 *
 * @author Administrator
 */
public class MyApplication extends Application {

    private static MyApplication application = new MyApplication();
    private static int MainID;// 主线程的id
    private static Handler handler;// 获取主线程的handler
    private static HashMap<Integer, Set<ShopNameBean.GoodsBean>> groupSet;
    private IWXAPI api;

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
        api = WXAPIFactory.createWXAPI(this, StaticField.APP_ID);
        api.registerApp(StaticField.APP_ID);

        MyLog.LogShitou("什么时候执行-------------->>001", "执行了001");
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
       MyLog.LogShitou("什么时候执行---------->002", "执行了002");
        super.onTerminate();
        SharedPreferences sp = getSharedPreferences("stamp", MODE_PRIVATE);
        sp.edit().putBoolean("isSetup", true).commit();
    }

    public static HashMap<Integer, Set<ShopNameBean.GoodsBean>> getGroupSet() {
        return groupSet;
    }

    public static void setGroupSet(HashMap<Integer, Set<ShopNameBean.GoodsBean>> groupSet) {
        MyApplication.groupSet = groupSet;
    }
}