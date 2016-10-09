package cn.com.chinau.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.StampTapDetailBean;
import cn.com.chinau.dialog.AddStampDialog;
import cn.com.chinau.fragment.stampfragment.StampInfoFragment;
import cn.com.chinau.fragment.stampfragment.StampPracticeFragment;
import cn.com.chinau.fragment.stampfragment.StampPriceFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.CustomViewPager;
import cn.com.chinau.view.VerticalScrollView;

/**
 * 邮票目录详情页
 */
public class StampTapDetailActivity extends BaseActivity implements View.OnClickListener,
        View.OnTouchListener, ViewPager.OnPageChangeListener {

    private View mStampTapDetailContent;//内容页面
    private View mStampTapDetailTitle;//标题页面
    private ImageView mShared;//分享按钮
    private ImageView mBack;//返回
    private CustomViewPager mViewPager;
    private TabPageIndicator mIndicator;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;

    private String mStampSn, mStampPrice, mDetail, mStory;//邮票标识(编号，价格)

    private List<Fragment> mList;
    private String[] arr = {"邮票信息", "价格行情", "邮票故事"};
    private StampInfoFragment stampInfoFragment;
    private StampPracticeFragment stampPracticeFragment;
    //    private StampTapDetailBean stampTapDetailBean;
    private TextView mPrice, mAddAlbum, mTitle, mAddStampCount;
    private Button mTopBtn;
    private VerticalScrollView home_SV;
    private View contentView;
    private int lastY = 0;
    private static final int ADDCLOSE = 3;
    private static final int ADDSUCCESS = 1;// 加入邮集成功的标识
    private int scrollY; // 标记上次滑动位置

    private String[] name;//名称
    private String[] stamp_sn;//编号
    private String[] current_price;///当前价格
    private String[] stamp_detail;//邮票信息
    private String[] stamp_story;//邮票故事
    private String mToken, mUser_id;
    private AddStampDialog mAdd;
    private int intStampCount = 0;// 初始化邮集数
    private boolean CountFlag = false;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS://请求数据成功
                    Gson gson = new Gson();
                    StampTapDetailBean fromJson = gson.fromJson((String) msg.obj, StampTapDetailBean.class);
                    ArrayList<StampTapDetailBean.StampTapDetail> mStampTapDetail = fromJson.getStamp_info_list();

                    initAdapter(mStampTapDetail);
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
                case ADDSUCCESS:// 加入邮集
                    Gson gsons = new Gson();
                    BaseBean mBaseBean = gsons.fromJson((String) msg.obj, BaseBean.class);
                    String code = mBaseBean.getRsp_code();
                    if (code.equals("0000")) {
                        mAdd = new AddStampDialog(StampTapDetailActivity.this);
                        mAdd.show();
                        mHandler.sendEmptyMessageDelayed(ADDCLOSE, 2000);
                        intStampCount++;
                        mAddStampCount.setText("+" + String.valueOf(intStampCount));
                        mAddStampCount.setVisibility(View.VISIBLE);// 加入邮集成功后显示邮集数
                    }
                    break;
                case ADDCLOSE:// 关闭弹出框
                    if (mAdd != null) {
                        mAdd.dismiss();
                    }
                    break;
            }
        }
    };
    private SharedPreferences sp;
    private LinearLayout mAddStampLl;


    @Override
    public View CreateTitle() {
        mStampTapDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mStampTapDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mStampTapDetailContent = View.inflate(this, R.layout.activity_stamptapdetail_content, null);
        sp = getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mStampTapDetailContent;
    }


    private void initView() {

        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        //获取传邮市传过来的内容
        Bundle bundle = getIntent().getExtras();
        mStampSn = bundle.getString(StaticField.STAMPDETAIL_SN);//
        mStampPrice = bundle.getString(StaticField.STAMPDETAIL_PRICE);

        mBack = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mStampTapDetailTitle.findViewById(R.id.base_title);
        mShared = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_shared);

        mPrice = (TextView) mStampTapDetailContent.findViewById(R.id.stamp_details_price);
        home_SV = (VerticalScrollView) mStampTapDetailContent.findViewById(R.id.home_SV);
        // 加入邮集
        mAddAlbum = (TextView) mStampTapDetailContent.findViewById(R.id.stamp_details_add_album);
        mAddStampLl = (LinearLayout) mStampTapDetailContent.findViewById(R.id.stamp_details_add_stamp_ll);
        // 邮集数
        mAddStampCount = (TextView) mStampTapDetailContent.findViewById(R.id.stamp_details_add_count);
//        mAddStampCount.setVisibility(View.INVISIBLE); // 初始化邮集数组件不可见
        // 置顶
        mTopBtn = (Button) mStampTapDetailContent.findViewById(R.id.base_top_btn);

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


                MyLog.LogShitou("result+邮票目录详情---->", result);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 加入邮集网络请求
     *
     * @param stamp_count 　添加数量
     * @param op_type     操作类型：SC：删除；JR加入；XG修改
     */

    private void UpDateGetInitNet(final String stamp_count, final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.MODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.STAMP_SN, mStampSn);//  邮票编号 （暂时为空）
//                params.put(StaticField.STAMP_COUNT, stamp_count);//  邮票数量
                params.put(StaticField.OP_TYPE, op_type);//  操作类型：SC：删除；JR加入；XG修改

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result加入邮集-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = ADDSUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        });
    }


    /**
     * 传入的是返回的数据
     *
     * @param mStampTapDetail
     */
    private void initAdapter(ArrayList<StampTapDetailBean.StampTapDetail> mStampTapDetail) {
        //获取轮播图的数组
        String[] imageBanner = getBannerData(mStampTapDetail);

        //获取其他数据的数组
        name = new String[mStampTapDetail.size()];
        stamp_sn = new String[mStampTapDetail.size()];
        stamp_detail = new String[mStampTapDetail.size()];
        current_price = new String[mStampTapDetail.size()];
        stamp_story = new String[mStampTapDetail.size()];

        for (int i = 0; i < mStampTapDetail.size(); i++) {
            name[i] = mStampTapDetail.get(i).getName();
            stamp_sn[i] = mStampTapDetail.get(i).getStamp_sn();
            stamp_detail[i] = mStampTapDetail.get(i).getStamp_detail();
            current_price[i] = mStampTapDetail.get(i).getCurrent_price();
            stamp_story[i] = mStampTapDetail.get(i).getStamp_story();
        }


        //设置Fragment
        mList = new ArrayList<>();
        stampInfoFragment = new StampInfoFragment(stamp_detail[0]);
        mList.add(stampInfoFragment);
        StampPriceFragment priceFragment = new StampPriceFragment();
        mList.add(priceFragment);
        stampPracticeFragment = new StampPracticeFragment(stamp_story[0]);
        mList.add(stampPracticeFragment);


        //设置数据
        mTitle.setText(name[0]);
        mPrice.setText("￥" + current_price[0]);

        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, imageBanner, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);

        //底部导航的ViewPager
        StampTapDetailAdapter adapter = new StampTapDetailAdapter(getSupportFragmentManager(), mList, arr);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setViewPager(mViewPager);


    }

    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mAddAlbum.setOnClickListener(this);
        mAddStampLl.setOnClickListener(this);
        mTopBtn.setOnClickListener(this);
        home_SV.setOnTouchListener(this);
        mTopVPI.setOnPageChangeListener(this);
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
            case R.id.stamp_details_add_album://加入邮集

                if (mToken.equals("") || mUser_id.equals("")) {
                    openActivityWitchAnimation(LoginActivity.class);
                } else {
                    UpDateGetInitNet(null, StaticField.JR); // 加入邮集网络请求
                }
                break;
            case R.id.stamp_details_add_stamp_ll://邮集数
                openActivityWitchAnimation(MyStampActivity.class);
                break;
            case R.id.base_top_btn://置顶
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
            mHandler.sendMessageDelayed(mHandler.obtainMessage(StaticField.TOUCH_EVENT_ID, view), StaticField.HOME_SV_COUNT);
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
        } else if (home_SV.getScrollY() < ScreenUtils.getScreenHeight(StampTapDetailActivity.this) / 3) { // 下滑 ScrollView滑动的距离小于当前手机屏幕高度的1/3就隐藏
            mTopBtn.setVisibility(View.GONE);
        } else if (home_SV.getScrollY() > ScreenUtils
                .getScreenHeight(StampTapDetailActivity.this) / 2) {// 上滑 ScrollView滑动的距离大于当前手机屏幕高度的1/2就显示
            mTopBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MyLog.e(position + "页面改变了");

        MyLog.e(stamp_detail[position] + "-" + stamp_story[position]);
        //这里应该把三个页面的数据全部改变
        stampInfoFragment.setInfoContent(stamp_detail[position]);
        stampPracticeFragment.setInfoContent(stamp_story[position]);

        //动态设置数据
        mTitle.setText(name[position]);
        mPrice.setText("￥" + current_price[position]);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 轮播图
     *
     * @param mStampTapDetail 数据源
     * @return 轮播图数组
     */
    public String[] getBannerData(ArrayList<StampTapDetailBean.StampTapDetail> mStampTapDetail) {
        String[] bannerDate = new String[mStampTapDetail.size()];
        for (int i = 0; i < mStampTapDetail.size(); i++) {
            bannerDate[i] = mStampTapDetail.get(i).getStamp_image();
        }
        return bannerDate;
    }
}
