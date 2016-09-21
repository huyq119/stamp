package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 回购订单订单关闭页面
 */
public class OrderCloseActivity extends BaseActivity {

    private View mOrderCloseTitle;
    private View mOrderCloseContent;
    private ImageView mBack;


    @Override
    public View CreateTitle() {
        mOrderCloseTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderCloseTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderCloseContent = View.inflate(this, R.layout.activity_orderclose, null);
        initView();
        return mOrderCloseContent;
    }

    @Override
    public void AgainRequest() {

    }
    private void initView() {
        mBack = (ImageView) mOrderCloseTitle.findViewById(R.id.base_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        TextView mTitle = (TextView) mOrderCloseTitle.findViewById(R.id.base_title);
        mTitle.setText("订单详情");


    }
}
