package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private View mLoginContent;//内容页面
    private View mLoginTitle;
    private ImageView mBack;//返回按钮
    private TextView mFP;//忘记密码
    private TextView mRegister;//注册

    @Override
    public View CreateTitle() {
        mLoginTitle = View.inflate(this, R.layout.base_back_title, null);
        return mLoginTitle;
    }

    @Override
    public View CreateSuccess() {
        mLoginContent = View.inflate(this, R.layout.activity_login_content, null);
        initView();
        initListener();
        return mLoginContent;
    }



    private void initView() {
        TextView mTitle = (TextView) mLoginTitle.findViewById(R.id.base_title);
        mTitle.setText("登录");
        mBack = (ImageView) mLoginTitle.findViewById(R.id.base_title_back);
        mFP = (TextView) mLoginContent.findViewById(R.id.login_ForgetPassword);
        mRegister = (TextView) mLoginContent.findViewById(R.id.login_register);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mFP.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.login_ForgetPassword://忘记密码
                openActivityWitchAnimation(ResettingPassWordActivity.class);
                break;
            case R.id.login_register://注册
                openActivityWitchAnimation(RegisterActivity.class);
                break;
        }
    }
}
