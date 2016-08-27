package com.example.stamp.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * MyHandler的基类
 * 
 * @author Administrator
 *
 */
public abstract class MyHandler extends Handler {
	public Activity activity;
	public WeakReference<Context> weakReference;

	public MyHandler(Activity activity) {
		this.activity = activity;
		weakReference = new WeakReference<Context>(activity);
	}

	@Override
	public abstract void handleMessage(Message msg);

}
