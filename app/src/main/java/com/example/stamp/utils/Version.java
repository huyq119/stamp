package com.example.stamp.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.example.stamp.ui.MyApplication;

/**
 * 获取版本号的方法
 *
 * @author Administrator
 */
public class Version {

    public static String getVersion() {
        PackageManager packageManager = MyApplication.getApplication().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(MyApplication.getApplication().getPackageName(), 0);
            String version = packageInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            return "1.0";
        }
    }
}
