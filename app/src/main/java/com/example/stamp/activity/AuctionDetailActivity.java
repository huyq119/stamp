package com.example.stamp.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.HomeViewPagerAdapter;
import com.example.stamp.adapter.StampDetailPagerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.fragment.stampdetailfragment.StampDetailInfoFragment;
import com.example.stamp.utils.MyToast;
import com.example.stamp.utils.ScreenUtils;
import com.example.stamp.view.CustomViewPager;
import com.example.stamp.view.VerticalScrollView;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞拍详情页面
 */
public class AuctionDetailActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{

    private View mAuctionDetailTitle,mAuctionDetailContent;

    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;
    private ImageView mBack,mShared,mCollect;
    private TextView mTitle,mNumber,mSubtract,mCount,mAdd,mBid;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String[] arrImage = {"http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};
    private Button mTopBtn;
    private VerticalScrollView home_SV;
    private View contentView;
    private int lastY = 0;
    private int scrollY; // 标记上次滑动位置
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
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
    private int intCount = 1 ;// 每次出价加减的值
    private String count;// 获取出价的值
    private int goods_storage = 1; //出价最低价

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

        mTopBtn = (Button) mAuctionDetailContent.findViewById(R.id.base_top_btn);
        home_SV = (VerticalScrollView) mAuctionDetailContent.findViewById(R.id.home_SV);
       mCollect =(ImageView) mAuctionDetailContent.findViewById(R.id.auction_collect);
        mSubtract =(TextView) mAuctionDetailContent.findViewById(R.id.auction_subtract);
        mCount=(TextView) mAuctionDetailContent.findViewById(R.id.auction_count);
        mCount.setText("0"); // 初始化值为0
        mAdd =(TextView) mAuctionDetailContent.findViewById(R.id.auction_add);
        mBid =(TextView) mAuctionDetailContent.findViewById(R.id.auction_bid);




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
        mTopBtn.setOnClickListener(this);
        home_SV.setOnTouchListener(this);
        mCollect.setOnClickListener(this);
        mSubtract.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mBid.setOnClickListener(this);
    }

    /**
     * 获取控件值
     */
    private void GetStringText(){
        count = mCount.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        GetStringText();
        switch (view.getId()){
            case R.id.base_title_back:// 返回
                finishWitchAnimation();
                break;
            case R.id.base_shared:// 分享
                openActivity(SharedActivity.class);
                break;
            case R.id.base_top_btn:// 置顶
                home_SV.post(new Runnable() {
                    @Override
                    public void run() {
                        home_SV.fullScroll(ScrollView.FOCUS_UP); // 滚动到顶部
                    }
                });
                mTopBtn.setVisibility(View.GONE);
                break;
            case R.id.auction_collect:// 收藏
                MyToast.showShort(this,"点击了收藏");

                break;
            case R.id.auction_subtract:// 出价减

                if (intCount > 1) {
                    intCount--;
                    mCount.setText(String.valueOf(intCount));
                }else{
                    mCount.setEnabled(false);// 不可点击
                }

                break;
            case R.id.auction_add:// 出价增加
                intCount++;
                mCount.setText(String.valueOf(intCount));
                break;
            case R.id.auction_bid:// 出价
                MyToast.showShort(this,"点击了出价");
                break;
        }
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
                .getScreenHeight(AuctionDetailActivity.this)/3) { // 下滑 ScrollView滑动的距离小于当前手机屏幕高度的1/3就隐藏
            mTopBtn.setVisibility(View.GONE);
        } else if (home_SV.getScrollY() > ScreenUtils
                .getScreenHeight(AuctionDetailActivity.this)/2) {// 上滑 ScrollView滑动的距离大于当前手机屏幕高度的1/2就显示
            mTopBtn.setVisibility(View.VISIBLE);
        }

    }
}
