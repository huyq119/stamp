package com.example.stamp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.HomeViewPagerAdapter;
import com.example.stamp.adapter.StampTapDetailAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.StampTapDetailBean;
import com.example.stamp.fragment.stampfragment.StampInfoFragment;
import com.example.stamp.fragment.stampfragment.StampPracticeFragment;
import com.example.stamp.fragment.stampfragment.StampPriceFragment;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.view.CustomViewPager;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 邮票目录详情页
 */
public class StampTapDetailActivity extends BaseActivity implements View.OnClickListener {

    private View mStampTapDetailContent;//内容页面
    private View mStampTapDetailTitle;//标题页面
    private ImageView mShared;//分享按钮
    private ImageView mBack;//返回
    private CustomViewPager mViewPager;
    private TabPageIndicator mIndicator;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;

    private String mStampSn;//邮票标识

    private List<Fragment> mList;
    private String[] arr = {"邮票信息", "价格行情", "邮票故事"};

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StaticField.SUCCESS://请求数据成功
                    Gson gson = new Gson();
                    stampTapDetailBean = gson.fromJson((String) msg.obj, StampTapDetailBean.class);
                    initAdapter(stampTapDetailBean);
                    break;
            }
        }
    };

    private String[] arrImage = {"http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://img1.3lian.com/2015/a1/114/d/58.jpg", "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};
    private StampInfoFragment stampInfoFragment;
    private StampTapDetailBean stampTapDetailBean;


    @Override
    public View CreateTitle() {
        mStampTapDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mStampTapDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mStampTapDetailContent = View.inflate(this, R.layout.activity_stamptapdetail_content, null);
        initView();
        initData();
        initListener();
        return mStampTapDetailContent;
    }


    private void initView() {
        //获取传邮市传过来的内容
        Bundle bundle = getIntent().getExtras();
        mStampSn = bundle.getString(StaticField.STAMPDETAIL);

        mShared = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_search);
        mBack = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_title_back);
        //轮播条的View
        mTopVP = (ViewPager) mStampTapDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mStampTapDetailContent.findViewById(R.id.base_viewpagerIndicator);
        //底部ViewPager的页面
        mViewPager = (CustomViewPager) mStampTapDetailContent.findViewById(R.id.stamptapdetail_viewpager);
        mIndicator = (TabPageIndicator) mStampTapDetailContent.findViewById(R.id.stamptapdetail_indicator);

    }

    private void initData() {
        RequestNet();
    }

    /**
     * 请求网络的方法,这里请求的是详情的所有内容
     */
    private void RequestNet() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMP);
                params.put(StaticField.STAMP_SN,mStampSn);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                if(result.equals("-1")){
                    return;
                }

                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void initAdapter(StampTapDetailBean stampTapDetailBean) {
        mList = new ArrayList<>();
        stampInfoFragment = new StampInfoFragment();
        mList.add(stampInfoFragment);
        StampPriceFragment priceFragment = new StampPriceFragment();
        mList.add(priceFragment);
        StampPracticeFragment stampPracticeFragment = new StampPracticeFragment();
        mList.add(stampPracticeFragment);

        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);
        //底部导航的ViewPager
        StampTapDetailAdapter adapter = new StampTapDetailAdapter(getSupportFragmentManager(), mList, arr);
        mViewPager.setAdapter(adapter);
        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setViewPager(mViewPager);
    }

    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
//        mTopVPI.setOnPageChangeListener(this);
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

//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(final int position) {
//        MyLog.e(position+"页面改变了");
//
//        stampInfoFragment.setInfoContent(stampTapDetailBean.getStamp_info_list().get(position).getStamp_detail());
////        stampInfoFragment.mVB.loadUrl(stampTapDetailBean.getStamp_info_list().get(position).getStamp_detail());
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
}
