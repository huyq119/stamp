package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 重置登录密码页面
 */
public class LoginPressWordActivity extends BaseActivity {

    private View mLoginPressWordTitle;
    private View mLoginPressWordContent;


    @Override
    public View CreateTitle() {
        mLoginPressWordTitle = View.inflate(this, R.layout.base_back_title, null);
        return mLoginPressWordTitle;
    }

    @Override
    public View CreateSuccess() {
        mLoginPressWordContent = View.inflate(this, R.layout.activity_loginpressword, null);
        initView();
        return mLoginPressWordContent;
    }

    private void initView() {

    }

    @Override
    public void AgainRequest() {

    }
}
