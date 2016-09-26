package cn.com.chinau.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import cn.com.chinau.ui.MyApplication;

/**
 * 关于IMEI的
 * 
 * @author Administrator
 *
 */
public class IMEI {
	/**
	 * 获取手机IMEI码
	 */
	public static String getIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
}
