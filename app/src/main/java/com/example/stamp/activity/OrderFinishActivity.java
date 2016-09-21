package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 回购订单订单已完成
 */
public class OrderFinishActivity extends BaseActivity {

    private View mOrderFinishContent;
    private View mOrderFinishTitle;
    private ImageView mBack;


    @Override
    public View CreateTitle() {
        mOrderFinishTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderFinishTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderFinishContent = View.inflate(this, R.layout.activity_orderfinish, null);
        initView();
        return mOrderFinishContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {

        mBack = (ImageView) mOrderFinishTitle.findViewById(R.id.base_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        TextView mTitle = (TextView) mOrderFinishTitle.findViewById(R.id.base_title);
        mTitle.setText("订单详情");

    }
}
