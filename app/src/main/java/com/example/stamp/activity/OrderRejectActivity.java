package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 回购订单订单驳回页面
 */
public class OrderRejectActivity extends BaseActivity {

    private View mOrderRejectTitle;
    private View mOrderRejectContent;
    private ImageView mBack;

    @Override
    public View CreateTitle() {
        mOrderRejectTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderRejectTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderRejectContent = View.inflate(this, R.layout.activity_orderreject, null);
        initView();
        return mOrderRejectContent;
    }

    @Override
    public void AgainRequest() {

    }
    private void initView() {
        mBack = (ImageView) mOrderRejectTitle.findViewById(R.id.base_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        TextView mTitle = (TextView) mOrderRejectTitle.findViewById(R.id.base_title);
        mTitle.setText("订单详情");
    }
}
