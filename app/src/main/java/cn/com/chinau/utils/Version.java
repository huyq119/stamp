package cn.com.chinau.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import cn.com.chinau.ui.MyApplication;

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
