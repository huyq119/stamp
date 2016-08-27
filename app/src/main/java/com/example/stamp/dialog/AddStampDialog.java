package com.example.stamp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.stamp.R;

/**
 * 添加邮集对话框
 * Created by Administrator on 2016/8/9.
 */
public class AddStampDialog extends Dialog {

    public AddStampDialog(Context context) {
        super(context, R.style.AddStampDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addstamp);
        // 设置为点击对话框之外的区域对话框不消失
        this.setCanceledOnTouchOutside(true);
    }
}
