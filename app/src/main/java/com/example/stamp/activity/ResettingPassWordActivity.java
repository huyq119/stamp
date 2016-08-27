package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 忘记密码页面
 */
public class ResettingPassWordActivity extends BaseActivity implements View.OnClickListener {

    private View mResettingTitle;//标题页面
    private View mResettingContent;
    private ImageView mBack;


    @Override
    public View CreateTitle() {
        mResettingTitle = View.inflate(this, R.layout.base_back_title, null);
        return mResettingTitle;
    }

    @Override
    public View CreateSuccess() {
        mResettingContent = View.inflate(this, R.layout.activity_resettingpassword, null);
        initView();
        initListener();
        return mResettingContent;
    }



    private void initView() {
        TextView mTitle = (TextView) mResettingTitle.findViewById(R.id.base_title);
        mTitle.setText("忘记密码");
        mBack = (ImageView) mResettingTitle.findViewById(R.id.base_title_back);
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
