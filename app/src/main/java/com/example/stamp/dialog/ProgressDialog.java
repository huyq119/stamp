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

