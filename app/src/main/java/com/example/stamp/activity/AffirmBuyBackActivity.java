package com.example.stamp.activity;

import android.view.View;
import android.widget.Button;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 确认回购页面
 */
public class AffirmBuyBackActivity extends BaseActivity implements View.OnClickListener {

    private View mAffirmBuyBackTitle;
    private View mAffirmBuyBackContent;
    private Button mBuyBack;//确认回购


    @Override
    public View CreateTitle() {
        mAffirmBuyBackTitle = View.inflate(this, R.layout.base_back_title, null);
        return mAffirmBuyBackTitle;
    }

    @Override
    public View CreateSuccess() {
        mAffirmBuyBackContent = View.inflate(this, R.layout.activity_affirmbuyback, null);
        initView();
        initListener();
        return mAffirmBuyBackContent;
    }

    private void initView() {
        mBuyBack = (Button) mAffirmBuyBackContent.findViewById(R.id.Affirm_buyBack);
    }

    private void initListener() {
        mBuyBack.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Affirm_buyBack://确认回购按钮
                openActivityWitchAnimation(FastMailInfoActivity.class);
                break;
        }
    }
}
