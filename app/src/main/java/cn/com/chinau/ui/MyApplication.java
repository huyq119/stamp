package cn.com.chinau.ui;

/**
 * Created by Administrator on 2016/6/24.
 */


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.google.gson.Gson;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;
import java.util.Set;

import cn.com.chinau.StaticField;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SPUtils;

/**
 * 全局上下文
 *
 * @author Administrator
 */
public class MyApplication extends Application {

    private static MyApplication application = new MyApplication();
    private static int MainID;// 主线程的id
    private static Handler handler;// 获取主线程的handler
    private static HashMap<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> groupSet;
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
        // app注册微信
        api = WXAPIFactory.createWXAPI(this, StaticField.APP_ID);
        api.registerApp(StaticField.APP_ID);

        UMShareAPI.get(this); // 初始化友盟sdk

        MyLog.LogShitou("什么时候执行-------------->>001", "执行了001");
        SharedPreferences sp = getSharedPreferences("stamp", MODE_PRIVATE);
        sp.edit().putBoolean("isSetup", true).commit();
        SPUtils.put(this, StaticField.SHOPJSON, new Gson().toJson(new ShopNameBean()));
    }

    // 注册友盟微信分享的AppId,AppSecret值
    {
        PlatformConfig.setWeixin("wxd37d120cce012c94", "b9bff0f06f0fedd86dc94de8a5a9e346");
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

    public static HashMap<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> getGroupSet() {
        return groupSet;
    }

    public static void setGroupSet(HashMap<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> groupSet) {
        MyApplication.groupSet = groupSet;
    }
}