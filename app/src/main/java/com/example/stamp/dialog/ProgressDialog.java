/*
 * @(#)ProgressDialog.java
 *
 * Copyright (C) 2016, ICHINASOFT INFORMATION TECHNOLOGY CO.,LTD. All right reserved.
 * see the site: http://www.ichinasoft.com
 */

package com.example.stamp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.stamp.R;


/**
 * @author  heqian
 * @mail    1564537102@qq.com
 * @since   2016年8月21日 9:38
 * @name    com.example.stamp.dialog.ProgressDialog.java
 * @version 1.0
 * 
 *
 */

public class ProgressDialog extends Dialog{



	public ProgressDialog(Context context) {
		super(context, R.style.exitdialog);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);

		this.setCanceledOnTouchOutside(false);
	}

}

