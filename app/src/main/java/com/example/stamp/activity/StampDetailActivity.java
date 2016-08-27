package com.example.stamp.activity;

import android.support.v4.app.Fragment;
import android.view.View;

import com.example.stamp.R;
import com.example.stamp.adapter.StampDetailPagerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.fragment.stampdetailfragment.StampDetailInfoFragment;
import com.example.stamp.view.CustomViewPager;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮市详情页面
 */
public class StampDetailActivity extends BaseActivity {

    private View mStampDetailTitle;
    private View mStampDetailContent;

    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;

    @Override
    public View CreateTitle() {
        mStampDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mStampDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mStampDetailContent = View.inflate(this, R.layout.activity_stampdetail_content, null);
        initView();
        return mStampDetailContent;
    }

    private void initView() {
        //下面的之后得删除
        mList = new ArrayList<>();
        StampDetailInfoFragment mStampDetailInfoFragment = new StampDetailInfoFragment();
        mList.add(mStampDetailInfoFragment);
        mList.add(mStampDetailInfoFragment);


        //初始化控件
        StampDetailPagerAdapter adapter = new StampDetailPagerAdapter(getSupportFragmentManager(), mList, arr);
        CustomViewPager mViewPager = (CustomViewPager) mStampDetailContent.findViewById(R.id.stampdetail_viewpager);
        mViewPager.setAdapter(adapter);
        TabPageIndicator mIndicator = (TabPageIndicator) mStampDetailContent.findViewById(R.id.stampdetail_indicator);
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    public void AgainRequest() {

    }
}
