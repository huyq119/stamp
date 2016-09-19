package com.example.stamp.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.activity.LoginActivity;
import com.example.stamp.activity.RegisterActivity;

/**
 * Created by Administrator on 2016/9/19.
 * 注册过的手机号对话框
 */
public class RegisterDialog extends Dialog{

    private TextView mCancel;// 取消按钮
    private TextView mEnsure;// 确定按钮
    private RegisterActivity context;
    private String phone;
    public RegisterDialog(RegisterActivity context, String phone) {
        super(context, R.style.exitdialog);
        this.context = context;
        this.phone = phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_register);
        this.setCanceledOnTouchOutside(false);

        // 设置为点击对话框之外的区域对话框不消失
        mEnsure = (TextView) findViewById(R.id.alertdialog_ensure);// 确定
        mCancel = (TextView) findViewById(R.id.alertdialog_cancel);// 取消
        // 设置事件
        mEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("WithDraw", "register");
                intent.putExtra("phone", phone);
                context.startActivity(intent);
                context.finish();
                dismiss();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
