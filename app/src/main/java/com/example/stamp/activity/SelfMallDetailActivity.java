package com.example.stamp.activity;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 自营商品详情页
 */
public class SelfMallDetailActivity extends BaseActivity {


    private View mSelfMallDetailTitle;
    private View mSelfMallDetailContent;
    private TextView mMarketPrice;//市场价

    @Override
    public View CreateTitle() {
        mSelfMallDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mSelfMallDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mSelfMallDetailContent = View.inflate(this, R.layout.activity_selfmalldetail_content, null);
        initView();
        return mSelfMallDetailContent;
    }

    private void initView() {
        mMarketPrice = (TextView) mSelfMallDetailContent.findViewById(R.id.market_price);
        mMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
    }

    @Override
    public void AgainRequest() {

    }
}
