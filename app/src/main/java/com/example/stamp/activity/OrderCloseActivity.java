package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 回购订单订单关闭页面
 */
public class OrderCloseActivity extends BaseActivity {

    private View mOrderCloseTitle;
    private View mOrderCloseContent;


    @Override
    public View CreateTitle() {
        mOrderCloseTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderCloseTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderCloseContent = View.inflate(this, R.layout.activity_orderclose, null);
        return mOrderCloseContent;
    }

    @Override
    public void AgainRequest() {

    }
}
