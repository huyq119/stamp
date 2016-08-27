package com.example.stamp.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.example.stamp.ui.MyApplication;

/**
 * 关于分辨率
 * 
 * @author Administrator
 *
 */
public class Resolution {
	/**
	 * 获取分辨率
	 * 
	 * @return 长*宽 的字符串
	 */
	public static String getResolution() {
		WindowManager windowManager = (WindowManager) MyApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		return display.getHeight() + "*" + display.getWidth();
	}
}
