package com.example.stamp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.DesignerDetailTapViewPagerAdapter;
import com.example.stamp.adapter.HomeViewPagerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.fragment.designerdetailfragment.DesignerResumeFragment;
import com.example.stamp.fragment.designerdetailfragment.DesignerStoryFragment;
import com.example.stamp.fragment.designerdetailfragment.DesignerWorksFragment;
import com.example.stamp.fragment.designerdetailfragment.DesigneriViewFragment;
import com.example.stamp.view.CustomViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 设计家详情页面
 */
public class DesignerDetailActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View mDesignerDetailTitle;
    private View mDesignerDetailContent;
    private ImageView mBack;//返回按钮
    private ImageView mShared;//分享按钮
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private TabPageIndicator mIndicator;
    private CustomViewPager mViewPager;

    private List<Fragment> mList;
    private String[] arr = {"个人简历", "设计故事", "艺术作品", "名家访谈"};

    private String[] arrImage = {"http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://img1.3lian.com/2015/a1/114/d/58.jpg", "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};

    @Override
    public View CreateTitle() {
        mDesignerDetailTitle = View.inflate(this, R.layout.base_search_title, null);
        return mDesignerDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mDesignerDetailContent = View.inflate(this, R.layout.activity_designerdetail, null);
        initView();
        initData();
        initAdapter();
        initListener();
        return mDesignerDetailContent;
    }


    private void initView() {
        //获取设计家传过来的内容(中英文名)
        Bundle bundle = getIntent().getExtras();
        String mDesignerChinese_name = bundle.getString(StaticField.DESIGNERDETAIL_CHINESE);
        String mDesignerEnglish_name = bundle.getString(StaticField.DESIGNERDETAIL_ENGLISH);

        mBack = (ImageView) mDesignerDetailTitle.findViewById(R.id.base_title_back);
        mShared = (ImageView) mDesignerDetailTitle.findViewById(R.id.base_search);
        TextView mTitle = (TextView) mDesignerDetailTitle.findViewById(R.id.base_title);
        mTitle.setText(mDesignerChinese_name + mDesignerEnglish_name);//赋值英文名
        //轮播条的View
        mTopVP = (ViewPager) mDesignerDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mDesignerDetailContent.findViewById(R.id.base_viewpagerIndicator);
        //底部ViewPager的页面
        // 导航栏
        mIndicator = (TabPageIndicator) mDesignerDetailContent.findViewById(R.id.designerdetail_indicator);
        // viewPager页面
        mViewPager = (CustomViewPager) mDesignerDetailContent.findViewById(R.id.designerdetail_viewpager);
    }

    private void initData() {
        mList = new ArrayList<>();
        // 个人简历
        DesignerResumeFragment designerResumeFragment = new DesignerResumeFragment();
        mList.add(designerResumeFragment);
        // 设计故事
        DesignerStoryFragment designerStoryFragment = new DesignerStoryFragment();
        mList.add(designerStoryFragment);
        // 艺术作品
        DesignerWorksFragment designerWorksFragment = new DesignerWorksFragment();
        mList.add(designerWorksFragment);
        // 名家访谈
        DesigneriViewFragment designeriViewFragment = new DesigneriViewFragment();
        mList.add(designeriViewFragment);

    }

    private void initAdapter() {
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);
        //底部导航的ViewPager
        DesignerDetailTapViewPagerAdapter adapter = new DesignerDetailTapViewPagerAdapter(getSupportFragmentManager(), mList, arr);
        mViewPager.setAdapter(adapter);
        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setViewPager(mViewPager);
    }


    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mTopVPI.setOnPageChangeListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_search://分享按钮
                openActivity(SharedActivity.class);
                break;
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
