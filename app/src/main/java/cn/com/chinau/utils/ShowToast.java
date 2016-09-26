package cn.com.chinau.utils;

import android.widget.Toast;

import cn.com.chinau.ui.MyApplication;

/**
 * Created by Administrator on 2016/9/19.
 * 展示吐司
 */
public class ShowToast {
    /**
     * 展示吐司的方法
     *
     * @param text
     */
    public static void showshortToast(String text) {
        Toast.makeText(MyApplication.getApplication(), text, Toast.LENGTH_SHORT).show();
    }
}
