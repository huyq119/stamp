package cn.com.chinau.activity;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.HomeViewPagerAdapter;
import cn.com.chinau.adapter.StampTapDetailAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.StampTapDetailBean;
import cn.com.chinau.fragment.stampfragment.StampInfoFragment;
import cn.com.chinau.fragment.stampfragment.StampPracticeFragment;
import cn.com.chinau.fragment.stampfragment.StampPriceFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.CustomViewPager;
import cn.com.chinau.view.VerticalScrollView;

/**
 * 邮票目录详情页
 */
public class StampTapDetailActivity extends BaseActivity implements View.OnClickListener,
        View.OnTouchListener {

    private View mStampTapDetailContent;//内容页面
    private View mStampTapDetailTitle;//标题页面
    private ImageView mShared;//分享按钮
    private ImageView mBack;//返回
    private CustomViewPager mViewPager;
    private TabPageIndicator mIndicator;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;

    private String mStampSn, mStampPrice,mDetail,mStory;//邮票标识(编号，价格)

    private List<Fragment> mList;
    private String[] arr = {"邮票信息", "价格行情", "邮票故事"};
    private String [] mImages;
    private StampInfoFragment stampInfoFragment;
    //    private StampTapDetailBean stampTapDetailBean;
    private TextView mPrice, mAddAlbum, mTitle;
    private Button mTopBtn;
    private VerticalScrollView home_SV;
    private View contentView;
    private int lastY = 0;
    private int scrollY; // 标记上次滑动位置
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS://请求数据成功
                    Gson gson = new Gson();
                    StampTapDetailBean fromJson = gson.fromJson((String) msg.obj, StampTapDetailBean.class);

                    ArrayList<StampTapDetailBean.StampTapDetail> mStampTapDetail = fromJson.getStamp_info_list();
                    String mImage = mStampTapDetail.get(0).getStamp_image();
                    // 获取的字符串转成数组,以逗号隔开
                    mImages =   mImage.split(",");
                    Log.e("mImages+图片~~~~>", mImage);

                    String mName = mStampTapDetail.get(0).getName();
                    mTitle.setText(mName);
                    String mSn = mStampTapDetail.get(0).getStamp_sn();
                    mDetail = mStampTapDetail.get(0).getStamp_detail();

                    String mCurrent_price = mStampTapDetail.get(0).getCurrent_price();
                    mPrice.setText("￥" + mCurrent_price);
                    mStory = mStampTapDetail.get(0).getStamp_story();

                    Log.e("信息，故事~~~~>", mDetail +"-------"+ mStory);
                    initAdapter();
                    break;
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
        mStampSn = bundle.getString(StaticField.STAMPDETAIL_SN);
        mStampPrice = bundle.getString(StaticField.STAMPDETAIL_PRICE);

        mBack = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mStampTapDetailTitle.findViewById(R.id.base_title);
        mShared = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_shared);

        mPrice = (TextView) mStampTapDetailContent.findViewById(R.id.stamp_details_price);
        home_SV = (VerticalScrollView) mStampTapDetailContent.findViewById(R.id.home_SV);
        // 加入邮集
        mAddAlbum = (TextView) mStampTapDetailContent.findViewById(R.id.stamp_details_add_album);
        // 置顶
        mTopBtn = (Button) mStampTapDetailContent.findViewById(R.id.stamp_top_detail_top_btn);

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
                params.put(StaticField.STAMP_SN, mStampSn);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                Log.e("result+邮票目录详情---->", result);
                if (result.equals("-1")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void initAdapter() {
        mList = new ArrayList<>();
        stampInfoFragment = new StampInfoFragment(mDetail);
        mList.add(stampInfoFragment);
        StampPriceFragment priceFragment = new StampPriceFragment();
        mList.add(priceFragment);
        StampPracticeFragment stampPracticeFragment = new StampPracticeFragment(mStory);
        mList.add(stampPracticeFragment);

        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, mImages, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);
        //底部导航的ViewPager
        StampTapDetailAdapter adapter = new StampTapDetailAdapter(getSupportFragmentManager(), mList, arr,mDetail,mStory);
        mViewPager.setAdapter(adapter);
        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setViewPager(mViewPager);


    }

    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mAddAlbum.setOnClickListener(this);
        mTopBtn.setOnClickListener(this);
        home_SV.setOnTouchListener(this);
//        mTopVPI.setOnPageChangeListener(this);
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
            case R.id.stamp_details_add_album://加入邮寄
                Toast.makeText(this, "开发中...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stamp_top_detail_top_btn://置顶
                home_SV.post(new Runnable() {
                    @Override
                    public void run() {
                        home_SV.fullScroll(ScrollView.FOCUS_UP);  // 滚动到顶部
                    }
                });
                mTopBtn.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * ScrollView 滑动的监听事件
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
     * <p/>
     * 其中getChildAt表示得到ScrollView的child View， 因为ScrollView只允许一个child
     * view，所以contentView.getMeasuredHeight()表示得到子View的高度,
     * getScrollY()表示得到y轴的滚动距离，getHeight()为scrollView的高度。
     * 当getScrollY()达到最大时加上scrollView的高度就的就等于它内容的高度了啊~
     */
    private void doOnBorderListener() {
        // 滑动到底部底部
        if (contentView != null && contentView.getMeasuredHeight() <= home_SV.getScrollY() + home_SV.getHeight()) {
            mTopBtn.setVisibility(View.VISIBLE);
        } else if (home_SV.getScrollY() < ScreenUtils
                .getScreenHeight(StampTapDetailActivity.this) / 3) { // 下滑 ScrollView滑动的距离小于当前手机屏幕高度的1/3就隐藏
            mTopBtn.setVisibility(View.GONE);
        } else if (home_SV.getScrollY() > ScreenUtils
                .getScreenHeight(StampTapDetailActivity.this) / 2) {// 上滑 ScrollView滑动的距离大于当前手机屏幕高度的1/2就显示
            mTopBtn.setVisibility(View.VISIBLE);
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
