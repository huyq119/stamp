package com.example.stamp.activity;

import android.view.View;
import android.widget.LinearLayout;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 密码管理页面
 */
public class PressWordManagerActivity extends BaseActivity implements View.OnClickListener {

    private View mPressWordManagerContent;
    private View mPressWordManagerTitle;
    private LinearLayout mWithdrawPW, mLoginPW;//提现密码,登录密码


    @Override
    public View CreateTitle() {
        mPressWordManagerTitle = View.inflate(this, R.layout.base_back_title, null);
        return mPressWordManagerTitle;
    }

    @Override
    public View CreateSuccess() {
        mPressWordManagerContent = View.inflate(this, R.layout.activity_presswordmanager, null);
        initView();
        initListener();
        return mPressWordManagerContent;
    }


    private void initView() {
        mLoginPW = (LinearLayout) mPressWordManagerContent.findViewById(R.id.pressWord_login);
        mWithdrawPW = (LinearLayout) mPressWordManagerContent.findViewById(R.id.pressWord_withdraw);
    }

    private void initListener() {
        mLoginPW.setOnClickListener(this);
        mWithdrawPW.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pressWord_login://登录密码
                openActivityWitchAnimation(LoginPressWordActivity.class);
                break;
            case R.id.pressWord_withdraw://提现密码
                openActivityWitchAnimation(WithdrawPressWordActivity.class);
                break;
        }
    }
}
