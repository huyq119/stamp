package com.example.stamp.activity;

import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.HomeViewPagerAdapter;
import com.example.stamp.adapter.StampDetailPagerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.view.CustomViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 商城详情页
 */
public class SelfMallDetailActivity extends BaseActivity implements View.OnClickListener{


    private View mSelfMallDetailTitle;
    private View mSelfMallDetailContent;
    private TextView mTitle;//市场价
    private ImageView mBack,mShared;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String[] arrImage = {"http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};

    @Override
    public View CreateTitle() {
        mSelfMallDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mSelfMallDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mSelfMallDetailContent = View.inflate(this, R.layout.activity_selfmalldetail_content, null);
        initView();
        initAdapter();
        initListener();
        return mSelfMallDetailContent;
    }

    private void initView() {
        mBack = (ImageView) mSelfMallDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mSelfMallDetailTitle.findViewById(R.id.base_title);
        mTitle.setText("庚申年");
        mShared = (ImageView) mSelfMallDetailTitle.findViewById(R.id.base_shared);

        //轮播条的View
        mTopVP = (ViewPager) mSelfMallDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mSelfMallDetailContent.findViewById(R.id.base_viewpagerIndicator);
    }

    private void initAdapter(){
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);

    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mShared.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_shared://分享按钮
                openActivity(SharedActivity.class);
                break;
        }
    }
}
