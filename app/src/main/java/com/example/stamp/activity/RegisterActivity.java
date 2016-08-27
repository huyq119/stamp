package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private View mRegisterContent;
    private View mRegisterTitle;
    private ImageView mBack;


    @Override
    public View CreateTitle() {
        mRegisterTitle = View.inflate(this, R.layout.base_back_title, null);
        return mRegisterTitle;
    }

    @Override
    public View CreateSuccess() {
        mRegisterContent = View.inflate(this, R.layout.activity_register, null);
        initView();
        initListener();
        return mRegisterContent;
    }


    private void initView() {
        TextView mTitle = (TextView) mRegisterTitle.findViewById(R.id.base_title);
        mTitle.setText("注册");
        mBack = (ImageView) mRegisterTitle.findViewById(R.id.base_title_back);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
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
        }
    }
}
