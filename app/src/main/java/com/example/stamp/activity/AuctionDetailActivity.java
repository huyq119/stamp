package com.example.stamp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.HomeViewPagerAdapter;
import com.example.stamp.adapter.StampDetailPagerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.fragment.stampdetailfragment.StampDetailInfoFragment;
import com.example.stamp.view.CustomViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞拍详情页面
 */
public class AuctionDetailActivity extends BaseActivity implements View.OnClickListener{

    private View mAuctionDetailTitle,mAuctionDetailContent;

    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;
    private ImageView mBack,mShared;
    private TextView mTitle,mNumber;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String[] arrImage = {"http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};

    @Override
    public View CreateTitle() {
        mAuctionDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mAuctionDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mAuctionDetailContent = View.inflate(this, R.layout.activity_auctiondetail_content, null);
        initView();
        initAdapter();
        initListener();
        return mAuctionDetailContent;
    }

    private void initView() {
        //下面的之后得删除
        mList = new ArrayList<>();
        StampDetailInfoFragment mStampDetailInfoFragment = new StampDetailInfoFragment();
        mList.add(mStampDetailInfoFragment);
        mList.add(mStampDetailInfoFragment);

        //初始化控件
        mBack = (ImageView) mAuctionDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mAuctionDetailTitle.findViewById(R.id.base_title);
        mTitle.setText("庚申年");
        mShared = (ImageView) mAuctionDetailTitle.findViewById(R.id.base_shared);

        //轮播条的View
        mTopVP = (ViewPager) mAuctionDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mAuctionDetailContent.findViewById(R.id.base_viewpagerIndicator);

        // 商家账号
        mNumber =(TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_number);
        String mBer = "13599258378";
        String mPhone = mBer.substring(0,3) + "****" + mBer.substring(7, mBer.length());
        mNumber.setText(mPhone);


        StampDetailPagerAdapter adapter = new StampDetailPagerAdapter(getSupportFragmentManager(), mList, arr);
        CustomViewPager mViewPager = (CustomViewPager) mAuctionDetailContent.findViewById(R.id.auctiondetail_viewpager);
        mViewPager.setAdapter(adapter);
        TabPageIndicator mIndicator = (TabPageIndicator) mAuctionDetailContent.findViewById(R.id.auctiondetail_indicator);
        mIndicator.setViewPager(mViewPager);

    }

    private void initAdapter(){
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);
    }

    @Override
    public void AgainRequest() {

    }
    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back:// 返回
                finishWitchAnimation();
                break;
            case R.id.base_shared:// 分享
                openActivity(SharedActivity.class);
                break;
        }
    }
}
