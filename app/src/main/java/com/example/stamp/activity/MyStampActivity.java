package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 我的邮集页面
 */
public class MyStampActivity extends BaseActivity {

    private View mStampTitle;
    private View mStampContent;


    @Override
    public View CreateTitle() {
        mStampTitle = View.inflate(this, R.layout.activity_mystamp_title, null);
        return mStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mStampContent = View.inflate(this, R.layout.activity_mystamp_content, null);
        return mStampContent;
    }

    @Override
    public void AgainRequest() {

    }


}
