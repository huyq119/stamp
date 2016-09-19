package com.example.stamp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.stamp.ui.MyApplication;

/**
 * Created by Administrator on 2016/9/19.
 * 三次的弹框
 */
public class ThreeCount {

    public static final String name = "stamp";
    private static SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);

    /**
     *
     * @param context
     *            上下文
     * @param current
     *            current对应的字段
     * @param currentTimeMillis
     *            时间对应的字段
     */
    public static void setThreeCount(Context context, String current, String currentTimeMillis) {
        Log.e("count", "%" + current);
        int count = sp.getInt(current, 0);
        Log.e("current", count + "");
        if (count == 0) {
            Log.e("设置时间", "是否执行");
            long currentTime = System.currentTimeMillis();// 第一次执行的时间,这个要保存在本地
            sp.edit().putLong(currentTimeMillis, currentTime).commit();
        }
        getCode(count, context, current);
    }

    /**
     * 判断次数
     *
     * @param count
     *            次数
     * @param current
     */
    private static void getCode(int count, Context context, String current) {
        count++;
        Log.e("count", count + "-" + current);
        sp.edit().putInt(current, count).commit();
        Log.e("canshu", sp.getInt(current, 0) + "");
    }

}
