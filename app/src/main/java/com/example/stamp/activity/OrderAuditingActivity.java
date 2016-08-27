package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 订单详情审核中页面
 */
public class OrderAuditingActivity extends BaseActivity {

    private View mOrderAuditingTitle;
    private View mOrderAuditingContent;


    @Override
    public View CreateTitle() {
        mOrderAuditingTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderAuditingTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderAuditingContent = View.inflate(this, R.layout.activity_orderauditing, null);
        initView();
        return mOrderAuditingContent;
    }

    private void initView() {

    }

    @Override
    public void AgainRequest() {

    }
}
