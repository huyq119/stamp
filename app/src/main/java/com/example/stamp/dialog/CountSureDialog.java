package com.example.stamp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.ui.MyApplication;
import com.example.stamp.utils.FourCode;

/**
 * Created by Administrator on 2016/9/19.
 *
 * 获取验证码次数的弹框
 */
public class CountSureDialog extends Dialog implements android.view.View.OnClickListener{



    private EditText mCode;// 输入验证码
    private TextView mSure, mQuite, mFourCode, mTitle;// 确定,取消,4位验证码
    private ImageButton mRefurbish;// 刷新
    public final String name = "stamp";
    private SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
    private String current;
    private SendProgressDialog pd;

    public CountSureDialog(Context context, String current, SendProgressDialog pd) {
        super(context, R.style.exitdialog);
        this.current = current;
        this.pd = pd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_counts_ure);
        // 设置为我们的布局
        this.setCanceledOnTouchOutside(false);
        // 设置为点击对话框之外的区域对话框不消失
        initView();
        initListener();
    }

    private void initView() {
        mCode = (EditText) findViewById(R.id.dialog_code);
        mTitle = (TextView) findViewById(R.id.dialog_title);
        mSure = (TextView) findViewById(R.id.dialog_sure);
        mQuite = (TextView) findViewById(R.id.dialog_quite);
        mFourCode = (TextView) findViewById(R.id.dialog_fourcode);
        mRefurbish = (ImageButton) findViewById(R.id.dialog_refurbish);
    }

    private void initListener() {
        mFourCode.setText(FourCode.getCode());// 设置4位随机验证码
        mSure.setOnClickListener(this);
        mQuite.setOnClickListener(this);
        mRefurbish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_sure :// 确定
                String code = mCode.getText().toString();
                String fourCode = mFourCode.getText().toString();
                if (code.equalsIgnoreCase(fourCode)) {// 如果相同说明输入验证码正确
                    // 重新设置次数
                    sp.edit().putInt(current, 0).commit();
                    dismiss();
                    // 回调发送验证码的方法
                    mCount.SendCode();
                } else {
                    mTitle.setTextColor(Color.parseColor("#e20000"));
                    mTitle.setText("验证码错误");
                }
                break;
            case R.id.dialog_quite :// 取消
                if (pd != null)
                    pd.dismiss();
                dismiss();// 取消后直接关闭
                break;
            case R.id.dialog_refurbish :// 刷新
                mFourCode.setText(FourCode.getCode());// 设置4位随机验证码
                break;
            default :
                break;
        }

    }
    private Count mCount;

    public void setmCount(Count mCount) {
        this.mCount = mCount;
    }

    public interface Count {
        /**
         * 发送验证码
         */
        void SendCode();
    }




}
