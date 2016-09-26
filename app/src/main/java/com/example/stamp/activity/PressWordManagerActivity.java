package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 密码管理页面
 */
public class PressWordManagerActivity extends BaseActivity implements View.OnClickListener {

    private View mPressWordManagerContent;
    private View mPressWordManagerTitle;
    private LinearLayout mWithdrawPW, mLoginPW;//提现密码,登录密码
    private ImageView mBack;
    private TextView mTitle;


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
    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mBack = (ImageView) mPressWordManagerTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mPressWordManagerTitle.findViewById(R.id.base_title);
        mTitle.setText("密码管理");
        mLoginPW = (LinearLayout) mPressWordManagerContent.findViewById(R.id.pressWord_login);
        mWithdrawPW = (LinearLayout) mPressWordManagerContent.findViewById(R.id.pressWord_withdraw);
    }


    private void initListener() {

        mBack.setOnClickListener(this);
        mLoginPW.setOnClickListener(this);
        mWithdrawPW.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.pressWord_login://登录密码
                openActivityWitchAnimation(LoginPressWordActivity.class);
                break;
            case R.id.pressWord_withdraw://提现密码
                openActivityWitchAnimation(WithdrawPressWordActivity.class);
                break;
        }
    }
}
