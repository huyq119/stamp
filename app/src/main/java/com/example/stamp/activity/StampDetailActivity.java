package com.example.stamp.activity;

import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stamp.R;
import com.example.stamp.adapter.HomeViewPagerAdapter;
import com.example.stamp.adapter.StampDetailPagerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.fragment.stampdetailfragment.StampDetailInfoFragment;
import com.example.stamp.utils.MyToast;
import com.example.stamp.view.CustomViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮市详情页
 */
public class StampDetailActivity extends BaseActivity implements View.OnClickListener{


    private View mStampDetailTitle,mStampDetailContent;
    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String[] arrImage = {"http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};
    private ImageView mBack,mShared,mCollect,mShoppingCart;// 返回，分享，收藏，购物车
    private TextView mTitle,mNumber,mAddShoppingCart,mBuyNow;// 标题，商家账号，加入购物车，立即购买

    @Override
    public View CreateTitle() {
        mStampDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mStampDetailTitle;

    }

    @Override
    public View CreateSuccess() {
        mStampDetailContent = View.inflate(this, R.layout.activity_stampdetail_content, null);
        initView();
        initAdapter();
        initListener();
        return mStampDetailContent;
    }

    private void initView() {
        //下面的之后得删除
        mList = new ArrayList<>();
        StampDetailInfoFragment mStampDetailInfoFragment = new StampDetailInfoFragment();
        mList.add(mStampDetailInfoFragment);
        mList.add(mStampDetailInfoFragment);

        //初始化控件
        mBack = (ImageView) mStampDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mStampDetailTitle.findViewById(R.id.base_title);
        mTitle.setText("庚申年");
        mShared = (ImageView) mStampDetailTitle.findViewById(R.id.base_shared);

        // 商家账号
        mNumber =(TextView) mStampDetailContent.findViewById(R.id.stamp_details_number);
        String mBer = "13899255786";
        // 隐藏中间4位
        String mPhone = mBer.substring(0,3) + "****" + mBer.substring(7, mBer.length());
        mNumber.setText(mPhone);
        mCollect =(ImageView) mStampDetailContent.findViewById(R.id.stamp_details_collect);
        mShoppingCart =(ImageView) mStampDetailContent.findViewById(R.id.stamp_details_shoppingCart);
        mAddShoppingCart =(TextView) mStampDetailContent.findViewById(R.id.stamp_details_addShoppingCart);
        mBuyNow =(TextView) mStampDetailContent.findViewById(R.id.stamp_details_buyNow);

        //轮播条的View
        mTopVP = (ViewPager) mStampDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mStampDetailContent.findViewById(R.id.base_viewpagerIndicator);

        //初始化控件
        StampDetailPagerAdapter adapter = new StampDetailPagerAdapter(getSupportFragmentManager(), mList, arr);
        CustomViewPager mViewPager = (CustomViewPager) mStampDetailContent.findViewById(R.id.stampdetail_viewpager);
        mViewPager.setAdapter(adapter);
        TabPageIndicator mIndicator = (TabPageIndicator) mStampDetailContent.findViewById(R.id.stampdetail_indicator);
        mIndicator.setViewPager(mViewPager);
    }

    private void initAdapter(){
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);
    }

    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mCollect.setOnClickListener(this);
        mShoppingCart.setOnClickListener(this);
        mAddShoppingCart.setOnClickListener(this);
        mBuyNow.setOnClickListener(this);

    }
    @Override
    public void AgainRequest() {

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
            case R.id.stamp_details_collect:// 收藏
                MyToast.showShort(this, "点击了收藏");
                break;
            case R.id.stamp_details_shoppingCart:// 购物车
                MyToast.showShort(this, "点击了购物车需要跳转1701页面");
                break;
            case R.id.stamp_details_addShoppingCart:// 加入购物车
                MyToast.showShort(this, "点击了加入购物车");
                break;
            case R.id.stamp_details_buyNow:// 立即购买
                MyToast.showShort(this, "点击了立即购买");
                break;
        }
    }
}
