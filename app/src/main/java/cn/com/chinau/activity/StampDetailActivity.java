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

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.HomeViewPagerAdapter;
import cn.com.chinau.adapter.StampDetailPagerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.StampDetailBean;
import cn.com.chinau.fragment.stampdetailfragment.StampDetailInfoFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.CustomViewPager;
import cn.com.chinau.view.VerticalScrollView;

/**
 * 邮市详情页
 */
public class StampDetailActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {


    private View mStampDetailTitle, mStampDetailContent;
    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String[] arrImage = {"http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};
//    private String[] arrImage;
    private ImageView mBack, mShared, mCollect, mShoppingCart;// 返回，分享，收藏，购物车
    private TextView mTitle, mSellerNo, mAddShoppingCart, mBuyNow,
            mGoodsName,mCurrentPrice,mFreight,mSaleCount,mFeeRate,mFee,
            mGoodsSource,mSellerName;// 标题，商家账号，加入购物车，立即购买
    private Button mTopBtn;
    private VerticalScrollView home_SV;
    private View contentView;
    private int lastY = 0;
    private int scrollY; // 标记上次滑动位置
    private String mGoods_sn,mGoodsDetail,mVerifyInfo;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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
                case StaticField.SUCCESS:// 详情数据
                    Gson gson = new Gson();
                    StampDetailBean mStampDetailBean = gson.fromJson((String)msg.obj, StampDetailBean.class);

                   String mImage = mStampDetailBean.getGoods_images();
                   String mGoods_images = mImage.substring(1,mImage.length()-1);

                    Log.e("mImage~~~~>", mImage+"");
                    arrImage = mGoods_images.split(",");

                    String mGoods_name = mStampDetailBean.getGoods_name();
                    mTitle.setText(mGoods_name);
                    mGoodsName.setText(mGoods_name);
                   String mPrice = mStampDetailBean.getCurrent_price();
                    mCurrentPrice.setText("￥"+mPrice);
                    String mFreights = mStampDetailBean.getFreight();
                    mFreight.setText("￥"+mFreights);
                    String mSaleCounts = mStampDetailBean.getSale_count();
                    mSaleCount.setText(mSaleCounts);
                    String mFeeRates = mStampDetailBean.getService_fee_rate();
                    mFeeRate.setText("("+mFeeRates+"):");
                    String mFees = mStampDetailBean.getService_fee();
                    mFee.setText(mFees);
                    String mGoodsSources = mStampDetailBean.getGoods_source();
                    mGoodsSource.setText(mGoodsSources);
                    String mSellerNames = mStampDetailBean.getSeller_name();
                    mSellerName.setText(mSellerNames);
                    String mSellerNos = mStampDetailBean.getSeller_no();
                    String mPhone = mSellerNos.substring(0, 3) + "****" + mSellerNos.substring(7, mSellerNos.length());
                    mSellerNo.setText(mPhone);
                    mGoodsDetail = mStampDetailBean.getGoods_detail();  // 商家信息H5url
                    mVerifyInfo = mStampDetailBean.getVerify_info(); // 鉴定信息H5url

                    break;
            }
        }
    };



    @Override
    public View CreateTitle() {
        mStampDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mStampDetailTitle;

    }

    @Override
    public View CreateSuccess() {
        mStampDetailContent = View.inflate(this, R.layout.activity_stampdetail_content, null);
        initView();
//        initData();
        initAdapter();
        initListener();
        return mStampDetailContent;
    }

    private void initView() {
        // 获取邮市页面传过来的的值
        Bundle bundle = getIntent().getExtras();
        mGoods_sn = bundle.getString(StaticField.GOODS_SN);
//        Log.e("mGoods_sn编号001~~~~>", mGoods_sn);
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
        mTopBtn = (Button) mStampDetailContent.findViewById(R.id.base_top_btn);

        mGoodsName= (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_goods_name);// 商品名称
        mCurrentPrice= (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_current_price);// 商品售价
        mFreight= (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_freight);// 运费
        mSaleCount= (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_sale_count);// 销量
        mFeeRate= (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_service_fee_rate);// 服务费率
        mFee= (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_service_fee);// 服务费
        mGoodsSource = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_goods_source);// 商品类型
        mSellerName = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_seller_name);// 商家名称
        mSellerNo = (TextView) mStampDetailContent.findViewById(R.id.stamp_details_seller_no);// 商家账号


        mCollect = (ImageView) mStampDetailContent.findViewById(R.id.stamp_details_collect);
        mShoppingCart = (ImageView) mStampDetailContent.findViewById(R.id.stamp_details_shoppingCart);
        mAddShoppingCart = (TextView) mStampDetailContent.findViewById(R.id.stamp_details_addShoppingCart);
        mBuyNow = (TextView) mStampDetailContent.findViewById(R.id.stamp_details_buyNow);

        home_SV = (VerticalScrollView) mStampDetailContent.findViewById(R.id.home_SV);

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

    private void initAdapter() {
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
        mTopBtn.setOnClickListener(this);
        home_SV.setOnTouchListener(this);

    }

    @Override
    public void AgainRequest() {

    }

    /**
     * 商品详情请求的数据
     */
    private void initData() {
        RequestNet();
    }

    /**
     * 商品邮市详情网络请求
     */
    private void RequestNet() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSDETAIL);// 接口名称
                params.put(StaticField.GOODS_SN, mGoods_sn); // 商品编号
                String token = "";
                String user_id = "";
                if (!(token.equals("") || user_id.equals(""))) {
                    params.put(StaticField.TOKEN, token); // 登录标识
                    params.put(StaticField.USER_ID, user_id); // 用户ID
                }
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                Log.e("邮市详情~~~~>", result);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                MyToast.showShort(this, "购物车需要跳转1701页面");
                break;
            case R.id.stamp_details_addShoppingCart:// 加入购物车
                MyToast.showShort(this, "加入购物车");
                break;
            case R.id.stamp_details_buyNow:// 立即购买
                MyToast.showShort(this, "立即购买跳转至1001页面");
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
                .getScreenHeight(StampDetailActivity.this) / 3) { // 下滑 ScrollView滑动的距离小于当前手机屏幕高度的1/3就隐藏
            mTopBtn.setVisibility(View.GONE);
        } else if (home_SV.getScrollY() > ScreenUtils
                .getScreenHeight(StampDetailActivity.this) / 2) {// 上滑 ScrollView滑动的距离大于当前手机屏幕高度的1/2就显示
            mTopBtn.setVisibility(View.VISIBLE);
        }

    }
}
