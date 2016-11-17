package cn.com.chinau.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.OrderDatilExpandableListAdapter;
import cn.com.chinau.aliapi.PayResult;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.OrderDetailBean;
import cn.com.chinau.bean.OrderPayBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.VerticalScrollView;

import static cn.com.chinau.activity.FirmOrderActivity.add;

/**
 * 订单详情页面
 * 所有的订单页面都在这一个类里面,到时候看看区分显示
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener, OrderDatilExpandableListAdapter.CountOrPrice {


    private View mOrderDetailsTitle;
    private View mOrderDetailsContent;
    private LinearLayout mLogistics;//物流信息页面

    //    private ExpandableListView mListView;//数据展示list
    private ExpandableListView mListView;//数据展示list

    private VerticalScrollView mScrollView;
    private LinearLayout mPayment_ll, mEndTimeLl, mAddress_ll;//付款
    private ImageView mBack, mPayImg;
    private TextView mTitle, mStatusTv, mEndTimeTv, mOrderSn, mCreateTime, mFirmOrderName, mCreateMobile, mAddress,mRoute,mLogisticsTime,
            mExpressType, mFreight, mServiceRate, mServiceFee, mPayType, mGoodsCount, mTotalPrice, mPayforTv, mLookLogisticsTv, mClooseTv;
    private String mOrderBuySuccess, mGoods_sn, mOrder_sn, mOrderStatus, mOrder_price, mGoods_count, mOrder_Sprice;
    private String mToken, mUser_id;// 标识，用户ID
    private SharedPreferences sp;
    private int mHour, mMin, mSecond;// 时，分，秒
    private boolean isRun = true;
    private OrderDetailBean mOrderDetailBean;
    private String mFreights,mOrderSns,mExpress_no,mExpress_route,mExpress_time,mPayTypes;
    private double mprices;
    private IWXAPI api;

    private Handler mHandler = new Handler() {


        private String mRequestId;
        private String mPayUrl;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 全部
                    Gson gson0 = new Gson();
                    OrderDetailBean mOrderDetailBean = gson0.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code = mOrderDetailBean.getRsp_code();
                    String Rsp_msg = mOrderDetailBean.getRsp_msg();
                    if (code.equals("0000")) {
                         mOrderStatus = mOrderDetailBean.getOrder_status();// 订单状态
                        String mEnd_time = mOrderDetailBean.getEnd_pay_time();// 付款剩余时间
                        MyLog.LogShitou("===0000000==请求下来的剩余时间", mEnd_time);

                        if (mOrderStatus.equals("INIT")) { // 待付款
                            mStatusTv.setText("待付款");
                            mEndTimeLl.setVisibility(View.VISIBLE);// 显示剩余时间LL
                            mAddress_ll.setVisibility(View.GONE);// 物流栏隐藏
//                            mEndTimeTv.setText(mEnd_time); // 倒计时
                            mPayforTv.setVisibility(View.VISIBLE);// 付款按钮显示
                            if (!mEnd_time.equals("")) {
                                startRun(); //开启倒计时
                                TimeData(mEnd_time); // 获取付款剩余是时间
//                                MyLog.LogShitou("===0000000===========", "走这===========");
                            }

                        } else if (mOrderStatus.equals("UNSHIPPED")) { //待发货
                            mStatusTv.setText("待收货");
                            mEndTimeLl.setVisibility(View.GONE);// 隐藏剩余时间LL
                            mAddress_ll.setVisibility(View.GONE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示

                        } else if (mOrderStatus.equals("SHIPPED")) { // 待收货
                            mStatusTv.setText("待收货");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示
                            MyLog.LogShitou("===============订单状态mOrderStatus","==========="+mOrderStatus);
                            GetLogisticsData(mOrderDetailBean); //获取第一条物流信息
                        } else if (mOrderStatus.equals("SIGN")) { // 交易完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示
                            GetLogisticsData(mOrderDetailBean); //获取第一条物流信息

                        } else if (mOrderStatus.equals("SUCCESS")) { // 交易完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示
                            GetLogisticsData(mOrderDetailBean); //获取第一条物流信息
                        } else if (mOrderStatus.equals("CLOSED")) { // 交易关闭
                            mStatusTv.setText("交易关闭");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.GONE);// 物流栏隐藏
                            mClooseTv.setVisibility(View.VISIBLE);// 显示交易关闭

                        } else if (mOrderStatus.equals("FINISH")) { // 已完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示

                            GetLogisticsData(mOrderDetailBean); //获取第一条物流信息
                        }

                        SetOrderDetailBeanData(mOrderDetailBean); // 给控件赋值

//                        initAdapter(mOrderDetailBean);

                    } else if (code.equals("1002")) {
                        MyToast.showShort(OrderDetailsActivity.this, Rsp_msg);
                    } else if (code.equals("2083")) {
                        MyToast.showShort(OrderDetailsActivity.this, Rsp_msg);
                    }

                    break;
                case StaticField.DFK_SUCCESS:// 待付款
                    Gson gson1 = new Gson();
                    OrderDetailBean mOrderDetailBean1 = gson1.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code1 = mOrderDetailBean1.getRsp_code();
                    String Rsp_msgs = mOrderDetailBean1.getRsp_msg();
                    if (code1.equals("0000")) {
                        String mOrderStatus = mOrderDetailBean1.getOrder_status();// 订单状态
                        String mEnd_time = mOrderDetailBean1.getEnd_pay_time();// 付款剩余时间
                        if (mOrderStatus.equals("INIT")) { // 待付款
                            mStatusTv.setText("待付款");
                            mEndTimeLl.setVisibility(View.VISIBLE);// 显示剩余时间LL
                            mAddress_ll.setVisibility(View.GONE);// 物流栏隐藏
//                            mEndTimeTv.setText(mEnd_time); // 倒计时
                            mPayforTv.setVisibility(View.VISIBLE);// 付款按钮显示

                            if (!mEnd_time.equals("")) {
                                startRun(); //开启倒计时
                                TimeData(mEnd_time); // 获取付款剩余是时间
                                MyLog.LogShitou("===0000000===========", "走这===========");
                            }
                        }
                        SetOrderDetailBeanData(mOrderDetailBean1); // 给控件赋值
//                        initAdapter(mOrderDetailBean1);

                    } else if (code1.equals("1002")) {
                        MyToast.showShort(OrderDetailsActivity.this, Rsp_msgs);
                    } else if (code1.equals("2083")) {
                        MyToast.showShort(OrderDetailsActivity.this, Rsp_msgs);
                    }
                    break;
                case StaticField.DSH_SUCCESS:// 代收货
                    Gson gson2 = new Gson();
                    OrderDetailBean mOrderDetailBean2 = gson2.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code2 = mOrderDetailBean2.getRsp_code();
                    String Rsp_msg2 = mOrderDetailBean2.getRsp_msg();
                    if (code2.equals("0000")) {
                         mOrderStatus = mOrderDetailBean2.getOrder_status();// 订单状态
                        if (mOrderStatus.equals("UNSHIPPED")) { //待发货
                            mStatusTv.setText("待收货");
                            mEndTimeLl.setVisibility(View.GONE);// 隐藏剩余时间LL
                            mAddress_ll.setVisibility(View.GONE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示
                        } else if (mOrderStatus.equals("SHIPPED")) { // 待收货
                            mStatusTv.setText("待收货");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示
                            GetLogisticsData(mOrderDetailBean2); //获取第一条物流信息
                        }

                        SetOrderDetailBeanData(mOrderDetailBean2); // 给控件赋值
//                        initAdapter(mOrderDetailBean2);
                    } else if (code2.equals("1002")) {
                        MyToast.showShort(OrderDetailsActivity.this, Rsp_msg2);
                    }
                    break;
                case StaticField.WC_SUCCESS:// 已完成
                    Gson gson3 = new Gson();
                    OrderDetailBean mOrderDetailBean3 = gson3.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code3 = mOrderDetailBean3.getRsp_code();
                    String Rsp_msg3 = mOrderDetailBean3.getRsp_msg();
                    if (code3.equals("0000")) {
                         mOrderStatus = mOrderDetailBean3.getOrder_status();// 订单状态
                        if (mOrderStatus.equals("SIGN")) { // 交易完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示
                            mExpress_route =  mOrderDetailBean3.getSeller_list().get(0).getExpress_detail_list().get(0).getExpress_route();
                            mRoute.setText(mExpress_route);// 快递内容
                            mExpress_time = mOrderDetailBean3.getSeller_list().get(0).getExpress_detail_list().get(0).getExpress_time();
                            mLogisticsTime.setText(mExpress_time); // 快递时间

                        } else if (mOrderStatus.equals("SUCCESS")) { // 交易完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示

                        } else if (mOrderStatus.equals("FINISH")) { // 已完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                            mLookLogisticsTv.setVisibility(View.VISIBLE);// 查看物流按钮显示
                        }
                        SetOrderDetailBeanData(mOrderDetailBean3); // 给控件赋值
                        GetLogisticsData(mOrderDetailBean3); //获取第一条物流信息
//                        initAdapter(mOrderDetailBean3);
                    } else if (code3.equals("1002")) {
                        MyToast.showShort(OrderDetailsActivity.this, Rsp_msg3);
                    }
                    break;
                case 10:
                    computeTime(); // 倒计时计算
                    mEndTimeTv.setText(mMin + "分" + mSecond + "秒");
                    if (mMin == 0 && mSecond == 0) {
                        mStatusTv.setText("交易关闭");
                        mEndTimeLl.setVisibility(View.GONE);
                        mAddress_ll.setVisibility(View.GONE);// 物流栏隐藏
                        mClooseTv.setVisibility(View.VISIBLE);// 显示交易关闭
                    }

                    break;
                case StaticField.WX_SUCCESS:// 微信订单支付
                    Gson gson = new Gson();
                    OrderPayBean mOrderPayBean = gson.fromJson((String) msg.obj, OrderPayBean.class);
                    String mCodes = mOrderPayBean.getRsp_code();
                    String mMsg = mOrderPayBean.getRsp_msg();

                    if (mCodes.equals("0000")) {
                        String mPayUrls = mOrderPayBean.getPay_url();// 支付请求地址
//                        String mRequestId = mOrderPayBean.getRequest_id();// 交易订单号
//                        String mPayRequestId = mOrderPayBean.getPay_request_id();// 平台支付请求号

                        if (mPayUrls != null) {
                            String[] payurl = mPayUrls.split("&");
                            // 获取各个字符串
                            String signs = payurl[0];
                            String timestamps = payurl[1];
                            String noncestrs = payurl[2];
                            String partnerids = payurl[3];
                            String prepayids = payurl[4];
                            String packages = payurl[5];
                            String appids = payurl[6];

                            MyLog.LogShitou("payurl详细信息", signs + "--" + timestamps + "--" + noncestrs + "--" +
                                    partnerids + "--" + prepayids + "--" + packages + "--" + appids);
                            // 截取字符串
                            String[] mSign = signs.split("=");
                            String[] mTimestamp = timestamps.split("=");
                            String[] mNoncestr = noncestrs.split("=");
                            String[] mPartnerid = partnerids.split("=");
                            String[] mPrepayid = prepayids.split("=");
                            String[] mAppid = appids.split("=");

                            // 获取需要的字段
                            String sign = mSign[1];
                            String timestamp = mTimestamp[1];
                            String noncestr = mNoncestr[1];
                            String partnerid = mPartnerid[1];
                            String prepayid = mPrepayid[1];
                            String appid = mAppid[1];

                            MyLog.LogShitou("微信支付需要的值", sign + "--" + timestamp + "--" + noncestr + "--" + partnerid + "--" + prepayid + "--" + appid);
                            // app注册微信
                            api = WXAPIFactory.createWXAPI(OrderDetailsActivity.this, StaticField.APP_ID);
                            api.registerApp(StaticField.APP_ID);
                            if (api != null) {
                                // 微信支付请求
                                PayReq req = new PayReq();
                                req.appId = appid; // 应用ID
                                req.partnerId = partnerid; // 商户号
                                req.prepayId = prepayid; // 预支付交易会话ID
                                req.packageValue = "Sign=WXPay"; // 	暂填写固定值Sign=WXPay
                                req.nonceStr = noncestr; // 随机字符串
                                req.timeStamp = timestamp; // 时间戳
                                req.sign = sign; // 签名
                                req.extData = "app data";
                                api.sendReq(req);
                                MyLog.LogShitou("这值是啥00req", api.sendReq(req) + "");
                            }
                        }
                    } else {
                        MyToast.showShort(OrderDetailsActivity.this, mMsg);
                    }

                    break;
                case StaticField.ALI_SUCCESS: //支付宝订单支付
                    Gson gsons = new Gson();
                    OrderPayBean mOrderPayBeans = gsons.fromJson((String) msg.obj, OrderPayBean.class);
                    String mCodess = mOrderPayBeans.getRsp_code();
                    String mMsgs = mOrderPayBeans.getRsp_msg();
                    if (mCodess.equals("0000")) {
                        mPayUrl = mOrderPayBeans.getPay_url();  // 支付请求地址
                        mRequestId = mOrderPayBeans.getRequest_id();// 交易订单号
                        mOrder_price = mOrderPayBeans.getOrder_amount();// 订单金额
//                        String mPayRequestId = mOrderPayBeans.getPay_request_id();// 平台支付请求号
                        MyLog.LogShitou("mPayUrl+支付宝请求需要的串", mPayUrl);
                        // 发起支付宝支付
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(OrderDetailsActivity.this);
                                Map<String, String> result = alipay.payV2(mPayUrl, true);
                                MyLog.LogShitou("result+支付宝请求", result.toString());
                                Message msg = new Message();
                                msg.what = StaticField.CG_SUCCESS;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } else {
                        MyToast.showShort(OrderDetailsActivity.this, mMsgs);
                    }
                    break;

                case StaticField.CG_SUCCESS:// 支付宝请求
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    MyLog.LogShitou("result+支付宝请求", payResult.toString() + "--" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Bundle bundle = new Bundle();
                        bundle.putString(StaticField.ORDERS, "receiving");
                        openActivityWitchAnimation(OrderBuySuccessActivity.class, bundle);
                        finish();
                    } else {
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderDetailsActivity.this, "正在支付中...",
                                    Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            Toast.makeText(OrderDetailsActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(OrderDetailsActivity.this, "你已取消支付",
                                    Toast.LENGTH_LONG).show();
                        } else if (TextUtils.equals(resultStatus, "6002")) {
                            Toast.makeText(OrderDetailsActivity.this, "网络连接失败",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(OrderDetailsActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    break;


            }
        }
    };


    @Override
    public View CreateTitle() {
        mOrderDetailsTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderDetailsTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderDetailsContent = View.inflate(this, R.layout.activity_order_details, null);
        sp = getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        initDate();
        initListener();
        return mOrderDetailsContent;
    }


    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        Bundle bundle = getIntent().getExtras();

        mOrderBuySuccess = bundle.getString("OrderBuySuccess"); // 交易订单号
        if (mOrderBuySuccess.equals("OrdersGoods")) {
            mOrder_sn = bundle.getString(StaticField.ORDER_SN); // 交易订单号
            mGoods_sn = bundle.getString(StaticField.GOODS_SN); // 商品编号
            mOrder_price = bundle.getString(StaticField.GOODS_PRICE); // 商品价格
            mGoods_count = bundle.getString(StaticField.GOODS_COUNT); // 商品编号
            mOrderStatus = bundle.getString(StaticField.ORDERSTATUS); //订单状态
            MyLog.LogShitou("=============订单列表来的","=============订单列表来的");
        }
        mBack = (ImageView) mOrderDetailsTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mOrderDetailsTitle.findViewById(R.id.base_title);
        mTitle.setText("订单详情");

        mPayment_ll = (LinearLayout) mOrderDetailsContent.findViewById(R.id.orderdetails_payment_ll);// 订单状态ll
        mStatusTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_detail_status_tv);// 订单状态
        mEndTimeLl = (LinearLayout) mOrderDetailsContent.findViewById(R.id.order_detail_endTime_ll);// 付款剩余时间ll
        mEndTimeTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_detail_endTime);// 付款剩余时间tv
        mOrderSn = (TextView) mOrderDetailsContent.findViewById(R.id.order_detail_orderSn);// 订单号
        mCreateTime = (TextView) mOrderDetailsContent.findViewById(R.id.order_detail_mCreate_time);// 下单时间
        mAddress_ll = (LinearLayout) mOrderDetailsContent.findViewById(R.id.orderDetails_Address_ll);// 物流栏
        mRoute = (TextView) mOrderDetailsContent.findViewById(R.id.logistics_route);// 物流内容
        mLogisticsTime = (TextView) mOrderDetailsContent.findViewById(R.id.logistics_time);// 物流内容

        mFirmOrderName = (TextView) mOrderDetailsContent.findViewById(R.id.firmOrder_name);// 收货人
        mCreateMobile = (TextView) mOrderDetailsContent.findViewById(R.id.firmOrder_mobile);// 电话
        mAddress = (TextView) mOrderDetailsContent.findViewById(R.id.firmOrder_detail_address);// 地址
        mExpressType = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_ExpressType);// 快递类型
        mFreight = (TextView) mOrderDetailsContent.findViewById(R.id.ExpressType_freight);// 快递费
        mServiceRate = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_ServiceRate);// 服务费率
        mServiceFee = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_ServiceFee);// 服务费
        mPayImg = (ImageView) mOrderDetailsContent.findViewById(R.id.order_details_payImg);// 支付方式图片
        mPayType = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_PayType);// 支付方式
        mGoodsCount = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_count);// 商品总件数
        mGoodsCount.setText("共" + mGoods_count + "件商品");
        mTotalPrice = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_total_price);// 商品总价
//        mTotalPrice.setText("￥"+ mprice);
        mPayforTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_Payfor_tv);// 付款按钮
        mLookLogisticsTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_look_logistics_tv);// 查看物流按钮
        mClooseTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_cloose_tv);// 交易已关闭

        mListView = (ExpandableListView) mOrderDetailsContent.findViewById(R.id.order_details_lv);

        mScrollView = (VerticalScrollView) mOrderDetailsContent.findViewById(R.id.order_details_sv);
    }

    private void initDate() {
        if (mOrderStatus != null) {
            if (mOrderStatus.equals("QB")) {// 全部
                GetInitNet(StaticField.QB);
            } else if (mOrderStatus.equals("DFK")) {// 待付款
                GetInitNet(StaticField.DFK);
            } else if (mOrderStatus.equals("DSH")) { // 待收货
                GetInitNet(StaticField.DSH);
            } else if (mOrderStatus.equals("WC")) { // 已完成
                GetInitNet(StaticField.WC);
            } else if (mOrderStatus.equals("TK")) { // 退款
                GetInitNet(StaticField.TK);
            }
        }
    }


    private void initAdapter(OrderDetailBean mOrderDetailBean) {
        OrderDatilExpandableListAdapter expandableAdapter = new OrderDatilExpandableListAdapter(this, mBitmap, mOrderDetailBean);
        mListView.setAdapter(expandableAdapter);
//        expandableAdapter.notifyDataSetChanged();
//        expandableAdapter.setCountOrPrice(this);
//        //让子控件全部展开
//        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
//            mListView.expandGroup(i);
//        }
//        //去掉自带按钮
//        mListView.setGroupIndicator(null);
//
//        mScrollView.smoothScrollTo(0, 0);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mAddress_ll.setOnClickListener(this);
        mPayforTv.setOnClickListener(this);
        mLookLogisticsTv.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.orderDetails_Address_ll://物流信息页面
                Bundle bundle = new Bundle();
                bundle.putString("ExpressNo",mExpress_no);
                bundle.putString("OrderStatus", mOrderStatus);
                openActivityWitchAnimation(LogisticsDetailsActivity.class, bundle);
                break;
            case R.id.order_details_Payfor_tv://付款按钮

                if (mPayTypes.equals("ALIPAY")) {
                    OrderPayNet("ALIPAY"); // 订单支付网络请求
                } else {
                    OrderPayNet("WXPAY"); //订单支付网络请求
                }


                break;
            case R.id.order_details_look_logistics_tv://查看物流栏
                Bundle bundle1 = new Bundle();
                bundle1.putString("ExpressNo", mExpress_no);
                bundle1.putString("OrderStatus", mOrderStatus);
                openActivityWitchAnimation(LogisticsDetailsActivity.class,bundle1);
                break;
        }
    }


    /**
     * 订单详情网络请求
     */
    private void GetInitNet(final String orderStatus) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSORDERDETAIL);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id); // 用户ID
                // 订单状态：QB：全部；DFK：待付款；DSH：待收货；WC：已完成；TK：退款/退货
                params.put(StaticField.ORDERSTATUS, orderStatus); // 订单状态
                params.put(StaticField.ORDER_SN, mOrder_sn);// 交易订单号
                params.put(StaticField.GOODS_SN, mGoods_sn); // 商品编号

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou(orderStatus + "--" + "商品详情数据", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                if (orderStatus.equals("QB")) {// 全部
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (orderStatus.equals("DFK")) { // 待付款
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.DFK_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (orderStatus.equals("DSH")) { // 待收货
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.DSH_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (orderStatus.equals("WC")) { // 已完成
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.WC_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (orderStatus.equals("TK")) { // 退款
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.TK_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 订单支付网络请求
     * @param payNmae 支付类型
     */

    private void OrderPayNet(final String payNmae) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ORDERPAY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 收货地址ID
                params.put(StaticField.TTRXORDERSN, mOrderSns);// 收货地址ID

                if (payNmae.equals("WXPAY")) {
                    MyLog.LogShitou("=====微信生成订单参数", mToken + "--" + mUser_id + "--" + mOrderSns );

//                    params.put(StaticField.TTRXORDERSN, mToJson);//  商品信息：所有商品的json字符串
                    String mapSort = SortUtils.MapSort(params);
                    String md5code = Encrypt.MD5(mapSort);
                    params.put(StaticField.SIGN, md5code);
                    String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                    MyLog.LogShitou("result微信支付订单", result);

                    if (result.equals("-1") | result.equals("-2")) {
                        return;
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.WX_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                    MyLog.LogShitou("微信", "微信-----------------------");
                } else if (payNmae.equals("ALIPAY")) {
//                    params.put(StaticField.GOODESINFO, mToJson);//  商品信息：所有商品的json字符串

                    MyLog.LogShitou("=====支付宝生成订单参数", mToken + "--" + mUser_id + "--" + mOrderSns );
                    String mapSort = SortUtils.MapSort(params);
                    String md5code = Encrypt.MD5(mapSort);
                    params.put(StaticField.SIGN, md5code);
                    String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                    MyLog.LogShitou("支付宝支付订单", result);

                    if (result.equals("-1") | result.equals("-2")) {
                        return;
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.ALI_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                    MyLog.LogShitou("支付宝", "支付宝-----------------------");
                }
            }
        });
    }

    /**
     * 给控件赋值
     *
     * @param orderDetailBean
     */
    private void SetOrderDetailBeanData(OrderDetailBean orderDetailBean) {
         mOrderSns = orderDetailBean.getOrder_sn();// 订单编号

        mOrderSn.setText(mOrderSns);
        mExpress_no = orderDetailBean.getSeller_list().get(0).getExpress_no();// 快递单号

        String mCreate_time = orderDetailBean.getCreate_time();// 下单时时间
        mCreateTime.setText(mCreate_time);
        String mName = orderDetailBean.getName();// 收件人
        mFirmOrderName.setText(mName);
        String mMobiles = orderDetailBean.getMobile();// 手机号
        mCreateMobile.setText(mMobiles);
        String mAddresss = orderDetailBean.getAddress();// 地址
        mAddress.setText(mAddresss);
        String mExpressTypes = orderDetailBean.getExpress_type();// 配送方式
        mExpressType.setText(mExpressTypes);
        // 运费
        mFreights = orderDetailBean.getFreight();
        mFreight.setText("￥" + mFreights);
        String mServiceRates = orderDetailBean.getService_fee_rate();// 服务费率
        mServiceRate.setText("服务费(" + mServiceRates + ")");
        String mServiceFees = orderDetailBean.getService_fee();// 服务费
        mServiceFee.setText("￥" + mServiceFees);

        if (mGoods_count != null) {
            DecimalFormat df = new DecimalFormat("######0.00");// 保留2位小数
            int count = Integer.valueOf(mGoods_count).intValue(); //转成int

            String strmFreights = mFreights.trim().replaceAll(",", "").trim();// 快递费去掉首尾空格和逗号
            double freights = Double.parseDouble(strmFreights); //快递费转double
            double freightsprice = freights * count;//快递费总价钱
            double mServicePrice = Double.parseDouble(mServiceFees.replaceAll(",", "").trim()); //最后服务费转double价钱
            String feePrice = df.format(freightsprice);// 最后快递费总价
            String ServicePrice = df.format(mServicePrice);// 最后服务费总价
            double feePrices = Double.parseDouble(feePrice); //快递费转double
            double ServicePrices = Double.parseDouble(ServicePrice); //服务费转double

            if (mOrderBuySuccess.equals("OrdersGoods")) {
                //换算商品总价
                String str = mOrder_price.trim().replaceAll(",", "").trim();// 价钱去掉首尾空格和逗号
                double countPrice = Double.parseDouble(str); //价钱转double
                MyLog.LogShitou("商品订单价钱转double+++商品数量", countPrice + "====" + count);
                double price = countPrice * count;//总价钱
                String mprice = df.format(price);// 最后商品总价
                mprices = Double.parseDouble(mprice); //商品价钱转double
                MyLog.LogShitou("订单列表查看订单", "======订单列表查看订单=============");
            }


            double price1 = add(mprices, feePrices); // 商品价+快递费总价+服务费
            double mTotalPrices = add(price1, ServicePrices); // 三个数相加的总费用（商品价+快递费+服务费）
            mTotalPrice.setText("￥" + mTotalPrices); // 最后合计总价
            MyLog.LogShitou("==========合计总价", "mTotalPrices=" + mTotalPrices);

             mPayTypes = orderDetailBean.getPay_type();// 支付方式
            if (mPayTypes.equals("ALIPAY")) {
                mPayImg.setImageResource(R.mipmap.zhifubao);
                mPayType.setText("支付宝");
            } else {
                mPayImg.setImageResource(R.mipmap.weixin_pay);
                mPayType.setText("微信");
            }
        }
    }

    /**
     * 获取第一条物流信息
     * @param orderDetailBean
     */
    private void GetLogisticsData(OrderDetailBean orderDetailBean){
        mExpress_route =  orderDetailBean.getSeller_list().get(0).getExpress_detail_list().get(0).getExpress_route();
        mRoute.setText(mExpress_route);// 快递内容
        mExpress_time = orderDetailBean.getSeller_list().get(0).getExpress_detail_list().get(0).getExpress_time();
        mLogisticsTime.setText(mExpress_time); // 快递时间
    }

    /**
     * 适配器回调过来的数据
     *
     * @param count 商品数量
     * @param price 商品总价格
     */
    @Override
    public void GetCountOrPrice(String count, String price, String express_no) {
//        mGoodsCount.setText("共" + count + "件商品");
//        mTotalPrice.setText("￥" + price);
//        mExpress_no = express_no;// 快递单号
    }

    /**
     * 获取付款剩余是时间
     *
     * @param endDatas 请求下来的时间
     */
    public void TimeData(String endDatas) {

        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String newDates = sDateFormat.format(new java.util.Date());
        MyLog.LogShitou("=========获取的当前时间==剩余时间", newDates + "===" + endDatas);
        try {
            java.util.Date begin = dfs.parse(newDates);
            java.util.Date end = dfs.parse(endDatas);
            long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒

            long mHours = (int) between % (24 * 3600) / 3600;
            long mMins = (int) between % 3600 / 60;
            long mSeconds = (int) between % 60 / 60;

            // long转int类型
            mHour = (int) mHours;
            mMin = (int) mMins;
            mSecond = (int) mSeconds;

            String times = mHour + ":" + mMin + ":" + mSecond;
            String times2 = mMin + "分" + mSecond + "秒";
            MyLog.LogShitou("=========剩余时间差", times + "==" + times2);

        } catch (ParseException e) {
            e.printStackTrace();
            MyLog.LogShitou("时间格式错误");
        }


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
