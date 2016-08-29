package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * Created by Administrator on 2016/8/19.
 * 设计故事详情页面
 */
public class DesignerStoryDetailActivity extends BaseActivity implements View.OnClickListener{

    private View mStoryDetails;
    private View mStampTapDetailTitle;
    private ImageView mBack;
    private ImageView mShared;
    private TextView mTitle;

    @Override
    public View CreateTitle() {
        mStampTapDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mStampTapDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mStoryDetails = View.inflate(this, R.layout.activity_designerstorydetail_content, null);
        initView();
        return mStoryDetails;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mBack = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_title_back);
        mShared = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_shared);
        mTitle = (TextView) mStampTapDetailTitle.findViewById(R.id.base_title);
        mTitle.setText("设计故事");
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.base_shared://分享按钮
            openActivity(SharedActivity.class);
            break;
        }
    }
}
