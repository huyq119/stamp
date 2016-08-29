package com.example.stamp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.example.stamp.utils.ScreenUtils;
import com.example.stamp.view.CustomViewPager;
import com.example.stamp.view.VerticalScrollView;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 设计家详情页面
 */
public class DesignerDetailActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, View.OnTouchListener {

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
    private VerticalScrollView home_SV;
    private Button mTopBtn;// 置顶按钮
    private int scrollY; // 标记上次滑动位置
    private View contentView;
    private int lastY = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticField.TOUCH_EVENT_ID:// SrcollView滑动监听
                    View scroller = (View) msg.obj;
                    if (lastY == scroller.getScrollY()) {
                        handleStop(scroller);
                    } else {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(
                                StaticField.TOUCH_EVENT_ID, scroller), StaticField.HOME_SV_COUNT);
                        lastY = scroller.getScrollY();
                    }
                    break;
            }
        }
    };

    @Override
    public View CreateTitle() {
        mDesignerDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mDesignerDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mDesignerDetailContent = View.inflate(this, R.layout.activity_designer_detail, null);
        initView();
        initData();
        initAdapter();
        initListener();
        return mDesignerDetailContent;
    }


    private void initView() {

        home_SV = (VerticalScrollView) mDesignerDetailContent.findViewById(R.id.home_SV);
        //打开套有 ListVew的 ScrollView的页面布局 默认起始位置在最顶部
        home_SV.smoothScrollTo(0, 0);
        // 置顶
        mTopBtn = (Button) mDesignerDetailContent.findViewById(R.id.designerdetail_top_btn);

        //获取设计家传过来的内容(中英文名)
        Bundle bundle = getIntent().getExtras();
        String mDesignerChinese_name = bundle.getString(StaticField.DESIGNERDETAIL_CHINESE);
        String mDesignerEnglish_name = bundle.getString(StaticField.DESIGNERDETAIL_ENGLISH);

        mBack = (ImageView) mDesignerDetailTitle.findViewById(R.id.base_title_back);
        mShared = (ImageView) mDesignerDetailTitle.findViewById(R.id.base_shared);
        TextView mTitle = (TextView) mDesignerDetailTitle.findViewById(R.id.base_title);
        mTitle.setText(mDesignerChinese_name + mDesignerEnglish_name);//赋值中英文名
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
        mTopBtn.setOnClickListener(this);
        home_SV.setOnTouchListener(this);
    }

    /**
     *  ScrollView 滑动的监听事件
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            mHandler.sendMessageDelayed(
                    mHandler.obtainMessage(StaticField.TOUCH_EVENT_ID, view), StaticField.HOME_SV_COUNT);
        }
        return false;
    }

    /**
     * ScrollView 停止
     *
     * @param view
     */
    private void handleStop(Object view) {
//        Log.i("哈哈~~~>", "停止了");
        ScrollView scroller = (ScrollView) view;
        scrollY = scroller.getScrollY();
        doOnBorderListener(); // 显示置顶按钮的方法
    }


    /**
     * 显示置顶按钮的方法
     *
     * 其中getChildAt表示得到ScrollView的child View， 因为ScrollView只允许一个child
     * view，所以contentView.getMeasuredHeight()表示得到子View的高度,
     * getScrollY()表示得到y轴的滚动距离，getHeight()为scrollView的高度。
     * 当getScrollY()达到最大时加上scrollView的高度就的就等于它内容的高度了啊~
     */
    private void doOnBorderListener() {
        // 滑动到底部底部
        if (contentView != null && contentView.getMeasuredHeight() <= home_SV.getScrollY() + home_SV.getHeight()) {
            mTopBtn.setVisibility(View.VISIBLE);
        } else if (home_SV.getScrollY() <  ScreenUtils
                .getScreenHeight(DesignerDetailActivity.this)/2) { // 下滑 ScrollView滑动的距离小于当前手机屏幕高度的一半就隐藏
            mTopBtn.setVisibility(View.GONE);
        } else if (home_SV.getScrollY() > ScreenUtils
                .getScreenHeight(DesignerDetailActivity.this)) {// 上滑 ScrollView滑动的距离大于当前手机屏幕高度就显示
            mTopBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_shared://分享按钮
                openActivity(SharedActivity.class);
                break;
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.designerdetail_top_btn://置顶
                home_SV.post(new Runnable() {
                    @Override
                    public void run() {
                        // 滚动到顶部
                        home_SV.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                mTopBtn.setVisibility(View.GONE);

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
