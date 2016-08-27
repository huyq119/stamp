package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 提现密码页面
 */
public class WithdrawPressWordActivity extends BaseActivity {


    private View mWithdrawPressWordTitle;
    private View mWithdrawPressWordContent;

    @Override
    public View CreateTitle() {
        mWithdrawPressWordTitle = View.inflate(this, R.layout.base_back_title, null);
        return mWithdrawPressWordTitle;
    }

    @Override
    public View CreateSuccess() {
        mWithdrawPressWordContent = View.inflate(this, R.layout.activity_withdrawpressword, null);
        return mWithdrawPressWordContent;
    }

    @Override
    public void AgainRequest() {

    }
}
