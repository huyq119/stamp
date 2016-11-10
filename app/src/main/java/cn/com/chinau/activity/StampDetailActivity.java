package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.MainActivity;
import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.DesignerDetailTapViewPagerAdapter;
import cn.com.chinau.adapter.HomeViewPagerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.AddShopCartBean;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.StampDetailBean;
import cn.com.chinau.dialog.SharedDialog;
import cn.com.chinau.fragment.stampfragment.StampInfoFragment;
import cn.com.chinau.fragment.stampfragment.StampPracticeFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.OrderGoodsViewPager;
import cn.com.chinau.view.VerticalScrollView;

/**
 * 邮市详情页
 */
public class StampDetailActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, UMShareListener {


    private View mStampDetailTitle, mStampDetailContent;
    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String[] small_images, big_images; // viewPager小图，大图
    private ImageView mBack, mShared, mCollect;// 返回，分享，收藏
    private Button mTopBtn;
    // 标题，商家账号，加入购物车，立即购买
    private TextView mTitle, mSellerNo, mGoodsName, mCurrentPrice, mFreight, mSaleCount, mFeeRate, mFee,
            mGoodsSource, mSellerName, mGoodsStatus, mShoppingCart, mShoppingCount, mAddShoppingCart, mBuyNow, tv_cancel;
    private VerticalScrollView home_SV;
    private View contentView;
    private int lastY = 0;
    private int scrollY; // 标记上次滑动位置
    private String mGoods_sn, mGoodsDetail, mVerifyInfo, mToken, mUser_id, mIsFavorite, mCartGoodsCount;
    private TabPageIndicator mIndicator;
    private StampInfoFragment stampInfoFragment;// 邮票信息
    private StampPracticeFragment stampPracticeFragment;// 鉴定信息
    private String mSharedImage;
    private String mGoods_name;
    private String mShare_url;
    private OrderGoodsViewPager mViewPager;
    private LinearLayout mShoppingLl;
    private SharedPreferences sp;
    private SharedDialog dialog;
    private ImageView mWeiXin, mPengYouQuan;
    private View dialog_finsih;
    private boolean addFlag = true; // 点击加入购物车，立即购买的的标识
    private String mPrice;
    private RadioButton mGoodsDetailBtn,mVerifyInfoBtn;
    private RadioGroup mStampDetailRG;
    private ArrayList<AddShopCartBean> info_list = new ArrayList<>();
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
                    StampDetailBean mStampDetailBean = gson.fromJson((String) msg.obj, StampDetailBean.class);
                    // 赋值头布局显示的图片
                    if (mStampDetailBean.getGoods_images() != null) {
                        String[] mGoods_images = mStampDetailBean.getGoods_images();
//                       MyLog.LogShitou("mGoods_images商品图片001-->:", mGoods_images.length + "");
                        small_images = new String[mGoods_images.length];
                        big_images = new String[mGoods_images.length];
                        for (int i = 0; i < mGoods_images.length; i++) {
                            String[] image = mGoods_images[i].split(",");
                            small_images[i] = image[0];// 小图集合
                            big_images[i] = image[1];// 大图集合
                        }
                        mSharedImage = small_images[0];
//                        MyLog.LogShitou("需要分享显示图片url", mSharedImage);
                    }

                    mGoods_name = mStampDetailBean.getGoods_name();
                    mTitle.setText(mGoods_name);
                    mGoodsName.setText(mGoods_name);
                    mPrice = mStampDetailBean.getCurrent_price();
                    mCurrentPrice.setText("￥" + mPrice);
                    String mFreights = mStampDetailBean.getFreight();
                    mFreight.setText("￥" + mFreights);
                    String mSaleCounts = mStampDetailBean.getSale_count();
                    mSaleCount.setText(mSaleCounts);
                    String mFeeRates = mStampDetailBean.getService_fee_rate();
                    mFeeRate.setText("(" + mFeeRates + "):");
                    String mFees = mStampDetailBean.getService_fee();
                    mFee.setText("￥" + mFees);
                    String mGoodsSources = mStampDetailBean.getGoods_source();
                    MyLog.LogShitou("邮票类型-->:", mGoodsSources);
                    if (mGoodsSources.equals("YS")) {
                        mGoodsSource.setText("邮市");
                    } else if (mGoodsSources.equals("JP")) {
                        mGoodsSource.setText("竞拍");
                    } else if (mGoodsSources.equals("SC_ZY")) {
                        mGoodsSource.setText("自营");
                    } else if (mGoodsSources.equals("SC_DSF")) {
                        mGoodsSource.setText("第三方");
                    }
                    String mSellerNames = mStampDetailBean.getSeller_name();
                    mSellerName.setText(mSellerNames);
                    String mSellerNos = mStampDetailBean.getSeller_no();
                    if (mSellerNos.length() < 11) {
                        mSellerNo.setText(mSellerNos);
                    } else {
                        String mPhone = mSellerNos.substring(0, 3) + "****" + mSellerNos.substring(7, mSellerNos.length());
                        mSellerNo.setText(mPhone);
                    }
                    mShare_url = mStampDetailBean.getShare_url();// 分享地址url

                    mGoodsDetail = mStampDetailBean.getGoods_detail();  // 邮票信息H5url
                    mVerifyInfo = mStampDetailBean.getVerify_info(); // 鉴定信息H5url

                    if (mGoodsDetail != null && mVerifyInfo != null){
                        mStampInfo_wb.loadUrl(mGoodsDetail);
                        mStampPractice_wb.loadUrl(mVerifyInfo);
                    }
                    MyLog.LogShitou("邮票信息的H5url", mGoodsDetail );
                    MyLog.LogShitou("鉴定信息的H5url", mVerifyInfo );

                    mIsFavorite = mStampDetailBean.getIs_favorite();// 收藏状态
//                    MyLog.LogShitou("商品收藏状态", mIsFavorite);
                    if (mIsFavorite.equals("0")) { // 未收藏
                        mCollect.setImageResource(R.mipmap.collection);
                    } else if (mIsFavorite.equals("1")) { // 已收藏
                        mCollect.setImageResource(R.mipmap.collections);
                    }

                    mCartGoodsCount = mStampDetailBean.getCart_goods_count();// 购物车里的商品数量

                    MyLog.LogShitou("商品数量-->:", mCartGoodsCount);
                    if (!mCartGoodsCount.equals("0") && !mCartGoodsCount.equals("")) {
                        mShoppingCount.setVisibility(View.VISIBLE);
                        mShoppingCount.setText("+" + mCartGoodsCount);
//                        MyLog.LogShitou("商品数量---002>", "走了吗。。。。。。。。。。");
                    }

                    String mGoodsStatuss = mStampDetailBean.getGoods_status();// 商品状态
                    MyLog.LogShitou("商品状态-->:", mGoodsStatuss);
                    if (mGoodsStatuss.equals("0")) {
                        mGoodsStatus.setVisibility(View.VISIBLE);
                        mGoodsStatus.setText("已下架");
                        // 加入购物车，立即购买按钮背景，字体置灰，不可点击
                        mAddShoppingCart.setBackgroundColor(getResources().getColor(R.color.gary));
                        mBuyNow.setBackgroundColor(getResources().getColor(R.color.gary));
                        mAddShoppingCart.setTextColor(getResources().getColor(R.color.font));
                        mBuyNow.setTextColor(getResources().getColor(R.color.font));
                        mAddShoppingCart.setEnabled(false);
                        mBuyNow.setEnabled(false);
                    }

                    initAdapter();
                    break;
                case StaticField.ADDSUCCESS:// 收藏成功
                    Gson gsons = new Gson();
                    BaseBean mBaseBean = gsons.fromJson((String) msg.obj, BaseBean.class);
                    String mRsp_code = mBaseBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mCollect.setImageResource(R.mipmap.collections);
                        initData();// 再次详情请求,获取收藏状态
                    }
                    break;
                case StaticField.DeleteSUCCESS:// 取消收藏成功
                    Gson gsonss = new Gson();
                    BaseBean mBaseBeans = gsonss.fromJson((String) msg.obj, BaseBean.class);
                    String mRsp_codes = mBaseBeans.getRsp_code();
                    if (mRsp_codes.equals("0000")) {
                        mCollect.setImageResource(R.mipmap.collection);
                        initData(); // 再次详情请求,获取收藏状态
                    }
                    break;
                case StaticField.ADDSHOPPINGCARTSUCCESS: // 加入购物车
                    Gson gsones = new Gson();
                    BaseBean mBasebean = gsones.fromJson((String) msg.obj, BaseBean.class);
                    if (mBasebean.getRsp_code().equals("0000")) {
                        String mCount = mShoppingCount.getText().toString().trim();
                        String count = mCount.substring(1);// 去掉"+"号
//                        MyLog.LogShitou("请求下来的数量", "--->:" + mCount);
                        int mShopCount = Integer.valueOf(count).intValue();// 转int
                        mShopCount++; // 数量自增
                        mShoppingCount.setVisibility(View.VISIBLE);// 加入购物车成功后显示数量
                        mShoppingCount.setText("+" + String.valueOf(mShopCount));

                        MyLog.LogShitou("加入后的数量", "--->:" + mShopCount);
                        mHandler.sendEmptyMessage(5); // 发送提示添加成功
                    }
                    break;
                case StaticField.GOBUY: // 立即购买
                    Gson gsones1 = new Gson();
                    BaseBean mBasebean1 = gsones1.fromJson((String) msg.obj, BaseBean.class);
                    if (mBasebean1.getRsp_code().equals("0000")) {
                        Intent intent = new Intent(StampDetailActivity.this, MainActivity.class);
                        intent.putExtra("Login", "StampDetail");
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                    }
                    break;

                case 5:
                    MyToast.showShort(StampDetailActivity.this, "添加成功");
                    break;

                default:
                    break;
            }
        }
    };
    private View view1,view2,vStampInfo,vStampPractice;
    private ArrayList<View> vList;
    private WebView mStampInfo_wb,mStampPractice_wb;
    private int mPosition;
    private int position =0;

    @Override
    public View CreateTitle() {
        mStampDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mStampDetailTitle;

    }

    @Override
    public View CreateSuccess() {
        mStampDetailContent = View.inflate(this, R.layout.activity_stampdetail_content, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mStampDetailContent;
    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        // 获取邮市页面传过来的的值
        Bundle bundle = getIntent().getExtras();
        mGoods_sn = bundle.getString(StaticField.GOODS_SN);// 商品编号
//        Log.e("mGoods_sn编号001~~~~>", mGoods_sn);
        //初始化控件
        mBack = (ImageView) mStampDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mStampDetailTitle.findViewById(R.id.base_title);
        mTitle.setText("庚申年");
        mShared = (ImageView) mStampDetailTitle.findViewById(R.id.base_shared);
        mTopBtn = (Button) mStampDetailContent.findViewById(R.id.base_top_btn);

        mGoodsName = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_goods_name);// 商品名称
        mCurrentPrice = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_current_price);// 商品售价
        mFreight = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_freight);// 运费
        mSaleCount = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_sale_count);// 销量
        mFeeRate = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_service_fee_rate);// 服务费率
        mFee = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_service_fee);// 服务费
        mGoodsSource = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_goods_source);// 商品类型
        mSellerName = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_seller_name);// 商家名称
        mSellerNo = (TextView) mStampDetailContent.findViewById(R.id.stamp_details_seller_no);// 商家账号
        mGoodsStatus = (TextView) mStampDetailContent.findViewById(R.id.stamp_detail_goods_status);// 商品状态

        mCollect = (ImageView) mStampDetailContent.findViewById(R.id.stamp_details_collect);// 收藏
        mShoppingLl = (LinearLayout) mStampDetailContent.findViewById(R.id.stamp_details_shopping_ll);// 购物车
        mShoppingCount = (TextView) mStampDetailContent.findViewById(R.id.stamp_details_shoppingCart);// 购物车商品数量
        mShoppingCount.setText("+0");
        mAddShoppingCart = (TextView) mStampDetailContent.findViewById(R.id.stamp_details_addShoppingCart);// 加入购物车
        mBuyNow = (TextView) mStampDetailContent.findViewById(R.id.stamp_details_buyNow);// 立即购买

        home_SV = (VerticalScrollView) mStampDetailContent.findViewById(R.id.home_SV);

        //轮播条的View
        mTopVP = (ViewPager) mStampDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mStampDetailContent.findViewById(R.id.base_viewpagerIndicator);

        // 底部viewPager页面
        mViewPager = (OrderGoodsViewPager) mStampDetailContent.findViewById(R.id.stampdetail_viewpager);

        mStampDetailRG = (RadioGroup) mStampDetailContent.findViewById(R.id.StampDetail_RG);
        mGoodsDetailBtn = (RadioButton) mStampDetailContent.findViewById(R.id.StampDetail_GoodsDetail_Btn);
        mVerifyInfoBtn = (RadioButton) mStampDetailContent.findViewById(R.id.StampDetail_VerifyInfo_Btn);

        view1 = mStampDetailContent.findViewById(R.id.view1);// line为红色
        view1.setBackgroundResource(R.color.red_font);
        view2 = mStampDetailContent.findViewById(R.id.view2);

        LayoutInflater inflater = LayoutInflater.from(this);
        vStampInfo = inflater.inflate(R.layout.fragment_stamp_info, null);
        vStampPractice = inflater.inflate(R.layout.fragment_stamppractice, null);
        vList = new ArrayList<View>();
        vList.add(vStampInfo);
        vList.add(vStampPractice);

        mStampInfo_wb = (WebView) vStampInfo.findViewById(R.id.StampInfo_wb);
        mStampPractice_wb = (WebView) vStampPractice.findViewById(R.id.StampPractice_wb);

        //底部导航的ViewPager
        DesignerDetailTapViewPagerAdapter adapter = new DesignerDetailTapViewPagerAdapter(vList, arr);
        mViewPager.setAdapter(adapter);


//        //底部ViewPager的页面
//        mViewPager = (CustomViewPager) mStampDetailContent.findViewById(R.id.stampdetail_viewpager);
//        mViewPager.setVisibility(View.VISIBLE);
//        mIndicator = (TabPageIndicator) mStampDetailContent.findViewById(R.id.stampdetail_indicator);
    }

    private void initAdapter() {
        //顶部导航的Viewpager
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, small_images, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);

//        mList = new ArrayList<>();
//        stampInfoFragment = new StampInfoFragment(mGoodsDetail);
//        mList.add(stampInfoFragment);
//        stampPracticeFragment = new StampPracticeFragment(mVerifyInfo);
//        mList.add(stampPracticeFragment);

//        //底部导航的ViewPager
//        StampDetailPagerAdapter adapter = new StampDetailPagerAdapter(getSupportFragmentManager(), mList, arr);
//        mViewPager.setAdapter(adapter);
//        mViewPager.setOffscreenPageLimit(2);
//        mIndicator.setVisibility(View.VISIBLE);
//        mIndicator.setViewPager(mViewPager);



    }


    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mTopBtn.setOnClickListener(this);
        home_SV.setOnTouchListener(this);
        mCollect.setOnClickListener(this);
        mShoppingLl.setOnClickListener(this);
        mAddShoppingCart.setOnClickListener(this);

        mBuyNow.setOnClickListener(this);
        // mViewPager 滑动事件监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                MyLog.e("哈哈~~~>", "--->" + mPosition);
                switch (position) {
                    case 0:
                        mGoodsDetailBtn.setTextColor(getResources().getColor(R.color.red_font));
                        mVerifyInfoBtn.setTextColor(getResources().getColor(R.color.black));
                        view1.setBackgroundResource(R.color.red_font);
                        view2.setBackgroundResource(R.color.line_bg);

                        break;
                    case 1:
                        mGoodsDetailBtn.setTextColor(getResources().getColor(R.color.black));
                        mVerifyInfoBtn.setTextColor(getResources().getColor(R.color.red_font));
                        view1.setBackgroundResource(R.color.line_bg);
                        view2.setBackgroundResource(R.color.red_font);
                        break;
                }
            }
        });
        // mStampDetailRG点击事件监听
        mStampDetailRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.StampDetail_GoodsDetail_Btn:// 邮票信息
                        position = 0;
                        view1.setBackgroundResource(R.color.red_font);
                        view2.setBackgroundResource(R.color.line_bg);
                        mViewPager.setCurrentItem(0); // 显示邮票信息页
                        break;
                    case R.id.StampDetail_VerifyInfo_Btn: // 鉴定信息
                        position = 1;
                        view1.setBackgroundResource(R.color.line_bg);
                        view2.setBackgroundResource(R.color.red_font);
                        mViewPager.setCurrentItem(1);// 显示鉴定信息页
                        break;
                }
            }
        });

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back:// 返回
                finishWitchAnimation();
                break;
            case R.id.base_shared:// 分享
                SharedDialog(); // 分享弹出Dialog
                break;
            case R.id.stamp_details_collect:// 收藏
                if (mToken.equals("") || mUser_id.equals("")) {
                    openActivityWitchAnimation(LoginActivity.class);
                } else {
                    if (mIsFavorite.equals("0")) {
                        DeleteGetInitNet(StaticField.JR);// 修改收藏网络请求
                    } else if (mIsFavorite.equals("1")) {
                        DeleteGetInitNet(StaticField.SC);
                    }
                }
                break;
            case R.id.stamp_details_shopping_ll:// 购物车
                Intent intent = new Intent(StampDetailActivity.this, MainActivity.class);
                intent.putExtra("Login", "StampDetail");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

                break;
            case R.id.stamp_details_addShoppingCart:// 加入购物车
                addFlag = true; // 点击加入购物车的标识
                if (mToken.equals("") || mUser_id.equals("")) {
                    openActivityWitchAnimation(LoginActivity.class);
                } else {
                    if (mGoods_sn != null) {
                        AddShopCartBean mAddShopCartBean = new AddShopCartBean();
                        mAddShopCartBean.setGoods_count("1");
                        mAddShopCartBean.setGoods_sn(mGoods_sn);
                        String mSnJson = new Gson().toJson(mAddShopCartBean); // 转json串
                        String toJson = "[" + mSnJson + "]"; // 转数组
                        AddShopCart(toJson); // 加入购物车网络请求
                        MyLog.LogShitou("加入购物车转换的Json", toJson);
                    }
                }

                break;
            case R.id.stamp_details_buyNow:// 立即购买
                addFlag = false; // 点击立即购买的标识
                if (mToken.equals("") || mUser_id.equals("")) {
                    openActivityWitchAnimation(LoginActivity.class);
                } else {
                    if (mGoods_sn != null) {

                        AddShopCartBean mAddShopCartBean = new AddShopCartBean();
                        mAddShopCartBean.setGoods_count("1");
                        mAddShopCartBean.setGoods_sn(mGoods_sn);
                        String mSnJson = new Gson().toJson(mAddShopCartBean); // 转json串
                        String toJson = "[" + mSnJson + "]"; // 转数组
                        AddShopCart(toJson); // 立即后买加入购物车网络请求
                        MyLog.LogShitou("立即购买加入购物车转换的Json", toJson);
                    }
                }
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
     * 商品邮市详情网络请求
     */
    private void RequestNet() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSDETAIL);// 接口名称
                params.put(StaticField.GOODS_SN, mGoods_sn); // 商品编号
                if (!mToken.equals("") || !mUser_id.equals("")) {
                    params.put(StaticField.TOKEN, mToken); // 登录标识
                    params.put(StaticField.USER_ID, mUser_id); // 用户ID
                    MyLog.LogShitou("标识与用户id-->:", mToken + "-" + mUser_id);
                }
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("result邮市详情~~~~>", result);
                if (result.equals("-1") | result.equals("-2") ) {
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
     * 修改收藏夹网络请求
     *
     * @param op_type 操作类型：SC删除,JR加入
     */
    private void DeleteGetInitNet(final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.FAVORITEMODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODS_SN, mGoods_sn);//  邮票编号
                params.put(StaticField.OP_TYPE, op_type);// 操作类型

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou(op_type + "-" + "收藏修改-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                if (op_type.equals("JR")) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.ADDSUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.DeleteSUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }

            }
        });
    }

    /**
     * 加入购物车网络请求
     *
     * @param Goods_info 商品信息：所有商品的json字符串
     */
    private void AddShopCart(final String Goods_info) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SHOPCARTMODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODESINFO, Goods_info);//  商品信息：所有商品的json字符串
                params.put(StaticField.OP_TYPE, StaticField.JR);// 操作类型：SC删除；JR加入；XG修改

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result加入购物车", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                if (addFlag) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.ADDSHOPPINGCARTSUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.GOBUY;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }


            }
        });
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

    /**
     * 分享弹出Dialog
     */
    private void SharedDialog() {
        dialog = new SharedDialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        mWeiXin = (ImageView) dialog.findViewById(R.id.weixin);
        mPengYouQuan = (ImageView) dialog.findViewById(R.id.pengyouquan);

        // 外部View 点击关闭dialog
        dialog_finsih = dialog.findViewById(R.id.shared_finish);
        dialog_finsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 取消
        tv_cancel = (TextView) dialog.findViewById(R.id.shared_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 微信
        mWeiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedDes(SHARE_MEDIA.WEIXIN);
                dialog.dismiss();
            }
        });
        // 朋友圈
        mPengYouQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedDes(SHARE_MEDIA.WEIXIN_CIRCLE);
                dialog.dismiss();
            }
        });

    }

    /**
     * 调起微信分享的方法
     *
     * @param share_media // 分享类型
     */
    private void SharedDes(SHARE_MEDIA share_media) {
        UMImage image = new UMImage(this.getApplicationContext(), mSharedImage);
        ShareAction shareAction = new ShareAction(this);
        shareAction.withText("微信分享"); // 显示的内容
        shareAction.withMedia(image);// 显示图片的url
        shareAction.withTitle(mGoods_name);// 标题
        shareAction.withTargetUrl(mShare_url); // 分享后点击查看的地址url
        shareAction.setPlatform(share_media); // 分享类型
        shareAction.setCallback(this);// 回调监听
        shareAction.share();
    }


    /**
     * 友盟微信分享回调 (成功，失败，取消)
     *
     * @param share_media 分享类型
     */
    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Toast.makeText(StampDetailActivity.this, "已分享", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享11", "" + share_media);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//        Toast.makeText(StampDetailActivity.this, " 分享失败" , Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享22", share_media + "----" + throwable.getMessage());

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
//        Toast.makeText(StampDetailActivity.this, " 分享取消了", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享33", share_media + "");
    }
}
