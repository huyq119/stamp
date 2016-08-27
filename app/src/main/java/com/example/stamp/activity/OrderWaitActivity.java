package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 回购订单待寄送页面
 */
public class OrderWaitActivity extends BaseActivity {

    private View mOrderWaitTitle;
    private View mOrderWaitContent;


    @Override
    public View CreateTitle() {
        mOrderWaitTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderWaitTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderWaitContent = View.inflate(this, R.layout.activity_orderwait, null);
        //TODO 是快递信息那个页面 FastMailInfoActivity这个页面
        return mOrderWaitContent;
    }

    @Override
    public void AgainRequest() {

    }
}
