package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.BidRecordListViewAdapter;
import cn.com.chinau.adapter.HomeViewPagerAdapter;
import cn.com.chinau.adapter.StampDetailPagerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.StampDetailBean;
import cn.com.chinau.dialog.AuctionRegulationsAgreementDialog;
import cn.com.chinau.dialog.SharedDialog;
import cn.com.chinau.fragment.stampfragment.StampInfoFragment;
import cn.com.chinau.fragment.stampfragment.StampPracticeFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.ListViewHeight;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.StringTimeUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.CustomViewPager;
import cn.com.chinau.view.VerticalScrollView;


/**
 * 竞拍详情页面
 */
public class AuctionDetailActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, UMShareListener {
    private View mAuctionDetailTitle, mAuctionDetailContent, contentView;
    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;
    private ImageView mBack, mShared, mCollect, mArrows;
    private TextView mTitle, mNumber, mSubtract, mCount, mAdd, mBid, mRecordTv, mBidCount,
            mGoodsName, mTimeTv, mTime, mStatus, mPrivce, mFreight, mFeeRate, mServiceFee, mSellerName,
            mGoodsSource, mOverTv, tv_cancel;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private Button mTopBtn, mKonwBtn;
    private VerticalScrollView home_SV;
    private String[] small_images, big_images; // viewPager小图，大图
    private int lastY = 0;
    private int scrollY; // 标记上次滑动位置
    private double intCount;// 每次出价加减的值
    private String count, mGoods_sn, mGoodsDetail, mVerifyInfo, mToken, mUser_id, mIsFavorite, mPrice, mAuctionRecord;// 获取出价的值
    private int goods_storage = 1; //出价最低价
    private AuctionRegulationsAgreementDialog auctiondialog; // 协议dialog
    private boolean bidFlag = false, addFlag;// 出价标识,加价标识
    private LinearLayout mRecordLl,mTimeLl;
    private ListView mBidRecordLV;
    private boolean bidRecordFlag = false;// 查看出价记录标识
    private FrameLayout mBidRecordFl;
    private ArrayList<StampDetailBean.OfferListData> mBidList;
    private SharedPreferences sp;
    private CustomViewPager mViewPager;
    private TabPageIndicator mIndicator;
    private StampInfoFragment stampInfoFragment;
    private StampPracticeFragment stampPracticeFragment;
    private String mShare_url, mSharedImage;
    private String mGoods_name;
    private int mHour ,mMin,mSecond;// 时，分，秒
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
                case StaticField.SUCCESS:// 竞拍详情数据
                    Gson gson = new Gson();
                    StampDetailBean mStampDetailBean = gson.fromJson((String) msg.obj, StampDetailBean.class);
                    String mCode = mStampDetailBean.getRsp_code();
                    String mMsg = mStampDetailBean.getRsp_msg();
                    if (mCode.equals("0000")) {
                        // 赋值头布局显示的图片
                        if (mStampDetailBean.getGoods_images() != null) {
                            String[] mGoods_images = mStampDetailBean.getGoods_images();
                            small_images = new String[mGoods_images.length];
                            big_images = new String[mGoods_images.length];
                            for (int i = 0; i < mGoods_images.length; i++) {
                                String[] image = mGoods_images[i].split(",");
                                small_images[i] = image[0];// 小图集合
                                big_images[i] = image[1];// 大图集合
                            }

                            mSharedImage = small_images[0];
                            MyLog.LogShitou("需要分享显示图片url", mSharedImage);
                        }

                        String mLeft_time = mStampDetailBean.getLeft_time(); // 剩余时间

                        if (mLeft_time != null){
                            startRun(); //开启倒计时
                            // 将时间转换成时分秒格式
                            int times = Integer.valueOf(mLeft_time).intValue(); // 转int类型
                            String time = StringTimeUtils.calculatTime(times); //  将毫秒转换成时分秒格式
                            String[] mTimes = time.split(",");
                            String time2 = mTimes[0];// 获取小时
                            mHour = Integer.valueOf(time2).intValue(); // 转int类型
                            String time3 = mTimes[1]; // 分钟
                            mMin = Integer.valueOf(time3).intValue();
                            String time4 = mTimes[2]; // 秒
                            mSecond = Integer.valueOf(time4).intValue();

                            MyLog.LogShitou("=====剩余时间", "=======/"+mHour + "时" + mMin + "分" + mSecond+"秒");
                        }

                        mGoods_name = mStampDetailBean.getGoods_name();
                        mTitle.setText(mGoods_name);
                        mGoodsName.setText(mGoods_name);
                        mPrice = mStampDetailBean.getCurrent_price();// 出价价格
                        mPrivce.setText("￥" + mPrice);
                        mCount.setText(mPrice);
                        String status = mStampDetailBean.getAuction_status();
                        if (status.equals("DP")) {
                            mTimeLl.setVisibility(View.VISIBLE);
                            mStatus.setText("未开始");
                            mTimeTv.setText("距离开拍:");
                            mBid.setBackgroundColor(getResources().getColor(R.color.gary));
                            mBid.setTextColor(getResources().getColor(R.color.font));
                            mSubtract.setEnabled(false);//减号不可点击
                            mAdd.setEnabled(false);// 加号不可点击
                            mBid.setEnabled(false);// 出价按钮不可点击
                        } else if (status.equals("JP")) {
                            mTimeLl.setVisibility(View.VISIBLE);
                            mStatus.setText("竞拍中");
                            mTimeTv.setText("剩余时间:");
                        } else if (status.equals("JS")) {
                            mTimeLl.setVisibility(View.GONE);
                            mStatus.setText("已结束");

                            mOverTv.setVisibility(View.VISIBLE);
                            mOverTv.setText("已结束");
                            mBid.setBackgroundColor(getResources().getColor(R.color.gary));
                            mBid.setTextColor(getResources().getColor(R.color.font));
                            mSubtract.setEnabled(false);//减号不可点击
                            mAdd.setEnabled(false);// 加号不可点击
                            mBid.setEnabled(false);// 出价按钮不可点击

                        }
                        // 商品状态
                        String mGoodesStatus = mStampDetailBean.getGoods_status();
                        MyLog.LogShitou("商品状态是多少", mGoodesStatus);
                        if (mGoodesStatus.equals("0")) { // 下架
                            mOverTv.setVisibility(View.VISIBLE);
                            mOverTv.setText("已结束");
                            mBid.setEnabled(false);// 出价按钮不可点击
                            mBid.setBackgroundColor(getResources().getColor(R.color.gary));
                            mBid.setTextColor(getResources().getColor(R.color.font));
                            mSubtract.setEnabled(false);//减号不可点击
                            mAdd.setEnabled(false);// 加号不可点击
                        }

                        String mFreights = mStampDetailBean.getFreight();
                        mFreight.setText("￥" + mFreights);
                        String mFeeRates = mStampDetailBean.getService_fee_rate();
                        mFeeRate.setText("(" + mFeeRates + "):");
                        String mFees = mStampDetailBean.getService_fee();
                        mServiceFee.setText("￥" + mFees);
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
                            mNumber.setText(mSellerNos);
                        } else {
                            String mPhone = mSellerNos.substring(0, 3) + "****" + mSellerNos.substring(7, mSellerNos.length());
                            mNumber.setText(mPhone);
                        }
                        mIsFavorite = mStampDetailBean.getIs_favorite();// 收藏状态
                        MyLog.LogShitou("商品收藏状态-->:", mIsFavorite);
                        if (mIsFavorite.equals("0")) { // 未收藏
                            mCollect.setImageResource(R.mipmap.collection);
                        } else if (mIsFavorite.equals("1")) { // 已收藏
                            mCollect.setImageResource(R.mipmap.collections);
                        }
                        mShare_url = mStampDetailBean.getShare_url(); // 分享地址url
                        mGoodsDetail = mStampDetailBean.getGoods_detail();  // 商家信息H5url
                        mVerifyInfo = mStampDetailBean.getVerify_info(); // 鉴定信息H5url
                        MyLog.LogShitou("竞拍详情请求下来的H5url-->:", mGoodsDetail + "--" + mVerifyInfo);

                        mBidList = mStampDetailBean.getOffer_list();// 出价记录list
                        // 循环出User_id是否有自己的id，有addFlag = true;没有addFlag = false;
                        for (int j = 0; j < mBidList.size(); j++) {
                            String mUser_id = mBidList.get(j).getUser_id();
                            String myUser_id = sp.getString("userId", "");
                            if (myUser_id.equals(mUser_id)) {
                                addFlag = true;// 出价加
                                bidFlag = true; //
                                MyLog.LogShitou(mUser_id + "到这了吗1", mUser_id);
                            }
                        }

                        if (mBidList != null && mBidList.size() != 0) {
                            mBidCount.setText(mBidList.size() + "");// 出价次数
                        } else {
                            mBidCount.setText("0");
                        }
                        initAdapter();
                    } else {
                        MyToast.showShort(AuctionDetailActivity.this, mMsg);
                    }

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
                case StaticField.PRICESUCCESS:// 出价成功
                    Gson gsones = new Gson();
                    BaseBean mBaseBeanees = gsones.fromJson((String) msg.obj, BaseBean.class);
                    String mRsp_codes1 = mBaseBeanees.getRsp_code();
                    String mRsp_msg = mBaseBeanees.getRsp_msg();
                    if (mRsp_codes1.equals("0000")) {
                        initData();// 再次请求网络的更新价格
                        MyToast.showShort(AuctionDetailActivity.this, "出价成功");
                    } else if (mRsp_codes1.equals("2102")) {
                        MyToast.showShort(AuctionDetailActivity.this, mRsp_msg);
                    }
                case 10:
                    computeTime(); // 倒计时计算
                    mTime.setText(mHour+"时"+mMin+"分"+mSecond+"秒");
                    if (mHour==0&&mMin==0&&mSecond==0) {
//                        mTime.setVisibility(View.GONE);
                        mTimeLl.setVisibility(View.GONE);
                    }

                    break;
            }
        }
    };
    private ImageView mWeiXin, mPengYouQuan;
    private SharedDialog dialog;
    private View dialog_finsih;
    private int recLen = 11;
    private boolean isRun = true;

    @Override
    public View CreateTitle() {
        mAuctionDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mAuctionDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mAuctionDetailContent = View.inflate(this, R.layout.activity_auctiondetail_content, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mAuctionDetailContent;
    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        // 获取竞拍页面传过来的的值
        Bundle bundle = getIntent().getExtras();
        mAuctionRecord = bundle.getString("AuctionRecord", "");
        if (mAuctionRecord.equals("AuctionRecord")) {
            mGoods_sn = bundle.getString(StaticField.GOODS_SN);
            MyLog.LogShitou("竞拍记录传的编号001~~~~>", mGoods_sn);
        } else {
            mGoods_sn = bundle.getString(StaticField.GOODS_SN);
            MyLog.LogShitou("竞拍传的编号002~~~~>", mGoods_sn);
        }

        //初始化控件
        mBack = (ImageView) mAuctionDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mAuctionDetailTitle.findViewById(R.id.base_title);
        mTitle.setText("庚申年");
        mShared = (ImageView) mAuctionDetailTitle.findViewById(R.id.base_shared);


        mTopBtn = (Button) mAuctionDetailContent.findViewById(R.id.base_top_btn);
        home_SV = (VerticalScrollView) mAuctionDetailContent.findViewById(R.id.home_SV);
        mRecordLl = (LinearLayout) mAuctionDetailContent.findViewById(R.id.bid_record_ll);
        mArrows = (ImageView) mAuctionDetailContent.findViewById(R.id.arrows_img);
        mRecordTv = (TextView) mAuctionDetailContent.findViewById(R.id.bid_record_tv);
        mBidRecordFl = (FrameLayout) mAuctionDetailContent.findViewById(R.id.auction_bidrecord_frame);
        mBidCount = (TextView) mAuctionDetailContent.findViewById(R.id.bid_count);// 出价次数

        // 出价记录ListView
        mBidRecordLV = (ListView) mAuctionDetailContent.findViewById(R.id.bidRecord_listview);

        mCollect = (ImageView) mAuctionDetailContent.findViewById(R.id.auction_collect);
        mSubtract = (TextView) mAuctionDetailContent.findViewById(R.id.auction_subtract);// 减号
        mCount = (TextView) mAuctionDetailContent.findViewById(R.id.auction_count); // 金额
        mAdd = (TextView) mAuctionDetailContent.findViewById(R.id.auction_add);// 加号
        mBid = (TextView) mAuctionDetailContent.findViewById(R.id.auction_bid);

        mGoodsName = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_goods_name);// 邮票名称
        mTimeLl  = (LinearLayout) mAuctionDetailContent.findViewById(R.id.auction_detail_time_ll);// 时间ll
        mTimeTv = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_time_tv);// 时间类型
        mTime = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_time);// 倒计时
        mStatus = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_status);// 竞拍状态
        mPrivce = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_current_price);// 商品售价
        mFreight = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_freight);// 运费
        mFeeRate = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_service_fee_rate);// 费率
        mServiceFee = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_service_fee);// 服务费
        mGoodsSource = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_goods_source);// 商家类型
        mSellerName = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_seller_name);// 商家名称
        mNumber = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_number);// 商家账号
        mOverTv = (TextView) mAuctionDetailContent.findViewById(R.id.auction_deatail_over_tv);// 商品状态是否结束

        //顶部的ViewPager
        mTopVP = (ViewPager) mAuctionDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mAuctionDetailContent.findViewById(R.id.base_viewpagerIndicator);
        // 底部的ViewPager
        mViewPager = (CustomViewPager) mAuctionDetailContent.findViewById(R.id.auctiondetail_viewpager);
        mIndicator = (TabPageIndicator) mAuctionDetailContent.findViewById(R.id.auctiondetail_indicator);


    }

    private void initAdapter() {

        mList = new ArrayList<>();
        stampInfoFragment = new StampInfoFragment(mGoodsDetail);
        mList.add(stampInfoFragment);
        stampPracticeFragment = new StampPracticeFragment(mVerifyInfo);
        mList.add(stampPracticeFragment);

        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, small_images, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);

        // 出价记录ListView
        BidRecordListViewAdapter mBidRecordAdapter = new BidRecordListViewAdapter(this, mBidList);
        mBidRecordLV.setAdapter(mBidRecordAdapter);
        mBidRecordLV.setEnabled(false);
        ListViewHeight.setListViewHeight(mBidRecordLV);

        //底部导航的ViewPager
        StampDetailPagerAdapter adapter = new StampDetailPagerAdapter(getSupportFragmentManager(), mList, arr);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    public void AgainRequest() {

    }

    /**
     * 商品竞拍详情请求的数据
     */
    private void initData() {
        RequestNet();
    }

    /**
     * 商品竞拍详情网络请求
     */
    private void RequestNet() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSDETAIL);// 接口名称
                params.put(StaticField.GOODS_SN, mGoods_sn); // 商品编号

                MyLog.LogShitou("标识+ID-->", mToken + "--" + mUser_id);
                if (!mToken.equals("") || !mUser_id.equals("")) {
                    params.put(StaticField.TOKEN, mToken); // 登录标识
                    params.put(StaticField.USER_ID, mUser_id); // 用户ID
                    MyLog.LogShitou("01-->", mToken + "--" + mUser_id);
                }

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("竞拍详情001~~~~>", result);
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
                MyLog.LogShitou("result收藏修改-->:", result);

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
     * 竞拍出价网络请求
     *
     * @param mPrice 出价价格
     */
    private void AuctionGoodsPriceDate(final String mPrice) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.AUCTIONOFFER);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODS_SN, mGoods_sn);//  邮票编号
                params.put(StaticField.AUCTIONPRICE, mPrice);// 出价的价格

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result出价-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.PRICESUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        });
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
        mRecordLl.setOnClickListener(this);

    }

    /**
     * 获取控件值
     */
    private void GetStringText() {
        count = mCount.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        GetStringText();
        switch (view.getId()) {
            case R.id.base_title_back:// 返回
                finishWitchAnimation();
                break;
            case R.id.base_shared:// 分享
                SharedDialog(); //分享弹出Dialog
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
            case R.id.bid_record_ll:// 查看出价记录

                if (bidRecordFlag) {
                    mBidRecordFl.setVisibility(View.GONE);
                    mArrows.setBackgroundResource(R.mipmap.arrows_up);
                    mRecordTv.setText("点击查看出价记录");
                    bidRecordFlag = false;
                } else {
                    mBidRecordFl.setVisibility(View.VISIBLE);
                    mArrows.setBackgroundResource(R.mipmap.arrows_donw);
                    mRecordTv.setText("点击收起出价记录");
                    home_SV.post(new Runnable() {
                        @Override
                        public void run() {
                            home_SV.fullScroll(ScrollView.FOCUS_DOWN); // 滚动到底部
                        }
                    });
                    bidRecordFlag = true;
                }


                break;
            case R.id.auction_collect:// 收藏
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
            case R.id.auction_subtract:// 出价减
                String counts = mCount.getText().toString().trim(); // 获取出价价格
                String mCountes = counts.replace(",", "");
                intCount = Double.parseDouble(mCountes);
                MyLog.LogShitou("减后当前价是多少", counts + "--" + intCount);
                if (intCount <= 50) {
                    intCount--;
                    mCount.setText(String.valueOf(intCount));
                } else if (intCount > 50 && intCount <= 100) {
                    intCount -= 2;
                    mCount.setText(String.valueOf(intCount));
                } else if (intCount > 100 && intCount <= 500) {
                    intCount -= 5;
                    mCount.setText(String.valueOf(intCount));
                } else if (intCount > 500 && intCount <= 10000000) {
                    intCount -= 10;
                    mCount.setText(String.valueOf(intCount));
                }

                break;
            case R.id.auction_add:// 出价增加
                if (mToken.equals("") || mUser_id.equals("")) {
                    openActivityWitchAnimation(LoginActivity.class);
                } else {
                    // 是否是第一次加价
                    if (addFlag) {
                        String countes = mCount.getText().toString().trim(); // 获取出价价格
                        String mCountess = countes.replace(",", "");
                        intCount = Double.parseDouble(mCountess);
//                        intCount =Double.valueOf(counts).doubleValue();
                        MyLog.LogShitou("增加后的当前价是多少", countes + "--" + intCount);
                        if (intCount <= 50) {
                            intCount++;
                            mCount.setText(String.valueOf(intCount));
                        } else if (intCount > 50 && intCount <= 100) {
                            intCount += 2;
                            mCount.setText(String.valueOf(intCount));
                        } else if (intCount > 100 && intCount <= 500) {
                            intCount += 5;
                            mCount.setText(String.valueOf(intCount));
                        } else if (intCount > 500 && intCount <= 10000000) {
                            intCount += 10;
                            mCount.setText(String.valueOf(intCount));
                        }
                    } else {
                        DialogAgreement();// 出价协议Dialog
                    }
                }

                break;
            case R.id.auction_bid:// 出价
                if (mToken.equals("") || mUser_id.equals("")) {
                    openActivityWitchAnimation(LoginActivity.class);
                } else {
                    if (count.equals("0")) {
                        MyToast.showShort(this, "请先加价");
                    } else {
                        // 是否是第一次出价
                        if (bidFlag) {

                            AuctionGoodsPriceDate(String.valueOf(intCount));
                        } else {
                            DialogAgreement();// 出价协议Dialog
                            bidFlag = true;
                        }
                    }
                }

                break;
        }
    }

    /**
     * ScrollView 滑动的监听事件
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        MyLog.LogShitou("motionEvent值是几=======",motionEvent+"");
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
                .getScreenHeight(AuctionDetailActivity.this) / 3) { // 下滑 ScrollView滑动的距离小于当前手机屏幕高度的1/3就隐藏
            mTopBtn.setVisibility(View.GONE);
        } else if (home_SV.getScrollY() > ScreenUtils
                .getScreenHeight(AuctionDetailActivity.this) / 2) {// 上滑 ScrollView滑动的距离大于当前手机屏幕高度的1/2就显示
            mTopBtn.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 出价协议Dialog
     */
    private void DialogAgreement() {
        auctiondialog = new AuctionRegulationsAgreementDialog(this);
        auctiondialog.show();
        mKonwBtn = (Button) auctiondialog.findViewById(R.id.iKonw_btn);
        mKonwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bidFlag) {
                    AuctionGoodsPriceDate(String.valueOf(intCount));
                    auctiondialog.dismiss();
                } else {
                    addFlag = true;
                    auctiondialog.dismiss();
                }


            }
        });
    }

    /**
     * 隐藏手机号中间4位的方法
     *
     * @param mobile // 手机号
     * @return
     */
    private String SetMobile(String mobile) {
        String mMobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        return mMobile;
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
        Toast.makeText(AuctionDetailActivity.this, "已分享", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享11", "" + share_media);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//        Toast.makeText(AuctionDetailActivity.this, " 分享失败" , Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享22", share_media + "----" + throwable.getMessage());

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
//        Toast.makeText(AuctionDetailActivity.this, " 分享取消了", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享33", share_media + "");
    }



    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000); // 停止1000ms
                        Message message = Message.obtain();
                        message.what = 10;
                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
//                    mDay--;
                }
            }
        }
    }
}
