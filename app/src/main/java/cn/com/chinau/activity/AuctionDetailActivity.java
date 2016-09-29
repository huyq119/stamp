package  cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import cn.com.chinau.adapter.BidRecordListViewAdapter;
import cn.com.chinau.adapter.HomeViewPagerAdapter;
import cn.com.chinau.adapter.StampDetailPagerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.StampDetailBean;
import cn.com.chinau.dialog.AuctionRegulationsAgreementDialog;
import cn.com.chinau.fragment.stampdetailfragment.StampDetailInfoFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.ListViewHeight;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.CustomViewPager;
import cn.com.chinau.view.VerticalScrollView;

/**
 * 竞拍详情页面
 */
public class AuctionDetailActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private View mAuctionDetailTitle, mAuctionDetailContent,contentView;
    private String[] arr = {"邮票信息", "鉴定信息"};
    private List<Fragment> mList;
    private ImageView mBack, mShared, mCollect, mArrows;
    private TextView mTitle, mNumber, mSubtract, mCount, mAdd, mBid, mRecordTv, mBidCount;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String[] arrImage = {"http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};
    private Button mTopBtn,mKonwBtn;
    private VerticalScrollView home_SV;
    private int lastY = 0;
    private int scrollY; // 标记上次滑动位置
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
                    BaseBean mBaseBean = gson.fromJson((String)msg.obj, BaseBean.class);
                    String rsp_msg = mBaseBean.getRsp_code();
                    if (rsp_msg.equals("2100")){

                        MyToast.showShort(AuctionDetailActivity.this,mBaseBean.getRsp_msg());
                    }
                    break;
            }
        }
    };

    private int intCount = 0;// 每次出价加减的值
    private String count,mGoods_sn;// 获取出价的值
    private int goods_storage = 1; //出价最低价
    private AuctionRegulationsAgreementDialog auctiondialog; // 协议dialog
    private boolean bidFlag = false,addFlag;// 出价标识,加价标识
    private LinearLayout mRecordLl;
    private ListView mBidRecordLV;
    private boolean bidRecordFlag = false;// 出价记录标识
    private FrameLayout mBidRecordFl;
    private ArrayList<StampDetailBean.OfferListData> mBidList;
    private SharedPreferences sp;

    @Override
    public View CreateTitle() {
        mAuctionDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mAuctionDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mAuctionDetailContent = View.inflate(this, R.layout.activity_auctiondetail_content, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_PRIVATE);
        initView();
        initData();
        initAdapter();
        initListener();
        return mAuctionDetailContent;
    }

    private void initView() {

        // 获取竞拍页面传过来的的值
        Bundle bundle = getIntent().getExtras();
        mGoods_sn = bundle.getString(StaticField.GOODS_SN);
        MyLog.LogShitou("mGoods_sn编号001~~~~>", mGoods_sn);
        //下面的之后得删除
        mList = new ArrayList<>();
        StampDetailInfoFragment mStampDetailInfoFragment = new StampDetailInfoFragment(this,null);
        mList.add(mStampDetailInfoFragment);
        mList.add(mStampDetailInfoFragment);

        mBidList = new ArrayList<>();
        String mphone = SetMobile("13699253527");// 设置电话号码
        for (int i = 0; i < 4; i++){
            mBidList.add(new StampDetailBean.OfferListData("2016/6/23 14:23:53",mphone,"￥9999.99"));
        }

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
        mRecordLl = (LinearLayout) mAuctionDetailContent.findViewById(R.id.bid_record_ll);
        mArrows = (ImageView) mAuctionDetailContent.findViewById(R.id.arrows_img);
        mRecordTv = (TextView) mAuctionDetailContent.findViewById(R.id.bid_record_tv);
        mBidRecordFl = (FrameLayout) mAuctionDetailContent.findViewById(R.id.auction_bidrecord_frame);
        mBidCount = (TextView) mAuctionDetailContent.findViewById(R.id.bid_count);// 出价次数
        mBidCount.setText("25");
        // 出价记录ListView
        mBidRecordLV = (ListView) mAuctionDetailContent.findViewById(R.id.bidRecord_listview);



        mCollect = (ImageView) mAuctionDetailContent.findViewById(R.id.auction_collect);
        mSubtract = (TextView) mAuctionDetailContent.findViewById(R.id.auction_subtract);
        mCount = (TextView) mAuctionDetailContent.findViewById(R.id.auction_count);
        mCount.setText("0"); // 初始化值为0
        mAdd = (TextView) mAuctionDetailContent.findViewById(R.id.auction_add);
        mBid = (TextView) mAuctionDetailContent.findViewById(R.id.auction_bid);


        // 商家账号
        mNumber = (TextView) mAuctionDetailContent.findViewById(R.id.auction_detail_number);
        String mBer = "13599258378";
        String mTel = SetMobile(mBer);
        mNumber.setText(mTel);


        StampDetailPagerAdapter adapter = new StampDetailPagerAdapter(getSupportFragmentManager(), mList,arr);
        CustomViewPager mViewPager = (CustomViewPager) mAuctionDetailContent.findViewById(R.id.auctiondetail_viewpager);
        mViewPager.setAdapter(adapter);
        TabPageIndicator mIndicator = (TabPageIndicator) mAuctionDetailContent.findViewById(R.id.auctiondetail_indicator);
        mIndicator.setViewPager(mViewPager);

    }

    private void initAdapter() {
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);

        // 出价记录ListView
        BidRecordListViewAdapter mBidRecordAdapter = new BidRecordListViewAdapter(this,mBidList);
        mBidRecordLV.setAdapter(mBidRecordAdapter);
        mBidRecordLV.setEnabled(false);
        ListViewHeight.setListViewHeight(mBidRecordLV);
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
                String token = sp.getString("token","");
                String user_id = sp.getString("userId","");
                MyLog.LogShitou("标识+ID-->", token+"--"+user_id);
                if (!token.equals("") || !user_id.equals("") ){
                    params.put(StaticField.TOKEN, token); // 登录标识
                    params.put(StaticField.USER_ID, user_id); // 用户ID
                    MyLog.LogShitou("01-->", token+"--"+user_id);
                }

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

               MyLog.LogShitou("竞拍详情001~~~~>", result);
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
                MyToast.showShort(this, "点击了收藏");

                break;
            case R.id.auction_subtract:// 出价减

                if (intCount > 1) {
                    intCount--;
                    mCount.setText(String.valueOf(intCount));
                } else {
                    mCount.setEnabled(false);// 不可点击
                }

                break;
            case R.id.auction_add:// 出价增加
                // 是否是第一次加价
                if (addFlag) {
                    intCount++;
                    mCount.setText(String.valueOf(intCount));
                } else {
                    DialogAgreement();// 出价协议Dialog
                    addFlag = true;
                }




                break;
            case R.id.auction_bid:// 出价
                if (count.equals("0")) {
                    MyToast.showShort(this, "请先加价");
                } else {
                    // 是否是第一次出价
                    if (bidFlag) {
                        mBid.setEnabled(false);

//                        MyToast.showShort(this, "出价成功");
                    } else {
                        DialogAgreement();// 出价协议Dialog
                        bidFlag = true;

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
                    auctiondialog.dismiss();
                    MyToast.showShort(AuctionDetailActivity.this, "出价成功");
                    mSubtract.setEnabled(false);
                    mAdd.setEnabled(false);
                } else {
                    bidFlag = false;
                    auctiondialog.dismiss();
                }


            }
        });
    }

    /**
     * 隐藏手机号中间4位的方法
     * @param mobile // 手机号
     * @return
     */
    private String SetMobile(String mobile){
        String mMobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        return mMobile;
    }


}
