package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 修改银行卡页面
 */
public class ReviseCardActivity extends BaseActivity {

    private View mReviseCardTitle;
    private View mReviseCardContent;


    @Override
    public View CreateTitle() {
        mReviseCardTitle = View.inflate(this, R.layout.base_back_title, null);
        return mReviseCardTitle;
    }

    @Override
    public View CreateSuccess() {
        mReviseCardContent = View.inflate(this, R.layout.activity_revisecard, null);
        initView();
        return mReviseCardContent;
    }

    private void initView() {
        //这里判断一下标题的名称
        //TODO 之后判断一下标题名称
    }

    @Override
    public void AgainRequest() {

    }

}
