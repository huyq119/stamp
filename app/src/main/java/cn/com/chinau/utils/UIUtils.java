package cn.com.chinau.utils;

import android.content.Context;

import cn.com.chinau.ui.MyApplication;


/**
 * 作者：jinlonghe on 2016/6/24 11:25
 *
 * 邮箱：753355530@qq.com
 */
public class UIUtils {
    /**
     * 获取数组
     *
     * @param tapName
     * @return
     */
    public static String[] getStringArray(int tapName) {
        return MyApplication.getApplication().getResources().getStringArray(tapName);
    }

    /**
     * 获取上下文的方法
     *
     * @return
     */
    public static Context getContext() {
        return MyApplication.getApplication();
    }

    /**
     * 把Runnable线程在主线程运行
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        MyLog.e(android.os.Process.myTid()+"-"+ MyApplication.getMainID());
        if (android.os.Process.myTid() == MyApplication.getMainID()) {// 如果当前线程和主线程相等的话
            runnable.run();
        } else {
            // 获取handler发送消息
            MyApplication.getHandler().post(runnable);
        }
    }


    public static int getdimens(int photoHeight) {
        return (int) getContext().getResources().getDimension(photoHeight);
    }

    /**
     * 执行自动轮询,会调用自己的run方法
     *
     * @param runnable
     *            要执行的任务
     * @param time
     *            延迟执行的时间
     */
    public static void postDelayed(Runnable runnable, int time) {
        MyApplication.getHandler().postDelayed(runnable, time);
    }

    /**
     * 移除一个异步任务
     *
     * @param runnable
     *            要移除的任务
     */
    public static void delete(Runnable runnable) {
        MyApplication.getHandler().removeCallbacks(runnable);
    }
}
