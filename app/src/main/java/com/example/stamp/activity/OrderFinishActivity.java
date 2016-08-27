package com.example.stamp.activity;

import android.view.View;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 回购订单订单已完成
 */
public class OrderFinishActivity extends BaseActivity {

    private View mOrderFinishContent;
    private View mOrderFinishTitle;


    @Override
    public View CreateTitle() {
        mOrderFinishTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderFinishTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderFinishContent = View.inflate(this, R.layout.activity_orderfinish, null);
        return mOrderFinishContent;
    }

    @Override
    public void AgainRequest() {

    }
}
