package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 回购订单订单驳回页面
 */
public class OrderRejectActivity extends BaseActivity {


    private View mOrderRejectTitle;
    private View mOrderRejectContent;

    @Override
    public View CreateTitle() {
        mOrderRejectTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderRejectTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderRejectContent = View.inflate(this, R.layout.activity_orderreject, null);
        return mOrderRejectContent;
    }

    @Override
    public void AgainRequest() {

    }
}
