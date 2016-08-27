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
 * 竞拍详情页
 */
public class AuctionDetailActivity extends BaseActivity {


    private View mAuctionDetailTitle;
    private View mAuctionDetailContent;

    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;

    @Override
    public View CreateTitle() {
        mAuctionDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mAuctionDetailTitle;

    }

    @Override
    public View CreateSuccess() {
        mAuctionDetailContent = View.inflate(this, R.layout.activity_auctiondetail_content, null);
        initView();
        return mAuctionDetailContent;
    }

    private void initView() {
        //下面的之后得删除
        mList = new ArrayList<>();
        StampDetailInfoFragment mStampDetailInfoFragment = new StampDetailInfoFragment();
        mList.add(mStampDetailInfoFragment);
        mList.add(mStampDetailInfoFragment);


        //初始化控件
        StampDetailPagerAdapter adapter = new StampDetailPagerAdapter(getSupportFragmentManager(), mList, arr);
        CustomViewPager mViewPager = (CustomViewPager) mAuctionDetailContent.findViewById(R.id.auctiondetail_viewpager);
        mViewPager.setAdapter(adapter);
        TabPageIndicator mIndicator = (TabPageIndicator) mAuctionDetailContent.findViewById(R.id.auctiondetail_indicator);
        mIndicator.setViewPager(mViewPager);
    }
    @Override
    public void AgainRequest() {

    }
}
