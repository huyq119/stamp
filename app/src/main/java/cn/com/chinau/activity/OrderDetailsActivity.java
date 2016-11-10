package cn.com.chinau.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.OrderDatilExpandableListAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.OrderDetailBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.utils.Utility;

/**
 * 订单详情页面
 * 所有的订单页面都在这一个类里面,到时候看看区分显示
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener ,OrderDatilExpandableListAdapter.CountOrPrice{


    private View mOrderDetailsTitle;
    private View mOrderDetailsContent;
    private LinearLayout mLogistics;//物流信息页面

//    private ExpandableListView mListView;//数据展示list
    private ExpandableListView mListView;//数据展示list

    private ScrollView mScrollView;
    private LinearLayout mPayment_ll,mEndTimeLl,mAddress_ll;//付款
    private ImageView mBack,mPayImg;
    private TextView mTitle,mStatusTv,mEndTimeTv,mOrderSn,mCreateTime,mFirmOrderName,mCreateMobile,mAddress,
            mExpressType,mFreight,mServiceRate,mServiceFee,mPayType,mGoodsCount,mTotalPrice,mPayforTv
            ,mLookLogisticsTv,mClooseTv;
    private String mGoods_sn, mOrder_sn,mOrderStatus;
    private String mToken, mUser_id;// 标识，用户ID
    private SharedPreferences sp;
//    private ArrayList<OrderDetailBean> mOrderDetailBean;
    private OrderDetailBean mOrderDetailBean;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 全部
                    Gson gson0 = new Gson();
                    OrderDetailBean mOrderDetailBean = gson0.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code = mOrderDetailBean.getRsp_code();
                    String Rsp_msg = mOrderDetailBean.getRsp_msg();
                    if (code.equals("0000")) {
                        String mOrderStatus= mOrderDetailBean.getOrder_status();// 订单状态
                        String mEnd_time = mOrderDetailBean.getEnd_pay_time();// 付款剩余时间

                        if (mOrderStatus.equals("INIT")){ // 待付款
                            mStatusTv.setText("待付款");
                            mEndTimeLl.setVisibility(View.VISIBLE);// 显示剩余时间LL
                            mAddress_ll.setVisibility(View.GONE);// 物流栏隐藏
                            mEndTimeTv.setText(mEnd_time); // 倒计时
                            mPayforTv.setVisibility(View.VISIBLE);// 付款按钮显示

                        }else if (mOrderStatus.equals("UNSHIPPED")){ //待发货
                            mStatusTv.setText("待收货");
                            mEndTimeLl.setVisibility(View.GONE);// 隐藏剩余时间LL
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示

                        }else if (mOrderStatus.equals("SHIPPED")){ // 待收货
                            mStatusTv.setText("待收货");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示

                        }else if (mOrderStatus.equals("SIGN")){ // 交易完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示

                        }else if (mOrderStatus.equals("SUCCESS")){ // 交易完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示

                        }else if (mOrderStatus.equals("CLOSED")){ // 交易关闭
                            mStatusTv.setText("交易关闭");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.GONE);// 物流栏隐藏
                            mClooseTv.setVisibility(View.VISIBLE);// 显示交易关闭

                        }else if (mOrderStatus.equals("FINISH")){ // 已完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示

                        }

                        SetOrderDetailBeanData(mOrderDetailBean); // 给控件赋值

//                        initAdapter(mOrderDetailBean);
                    }else if(code.equals("1002")){
                        MyToast.showShort(OrderDetailsActivity.this,Rsp_msg);
                    }else if(code.equals("2083")){
                        MyToast.showShort(OrderDetailsActivity.this,Rsp_msg);
                    }

                    break;
                case StaticField.DFK_SUCCESS:// 待付款
                    Gson gson1 = new Gson();
                    OrderDetailBean mOrderDetailBean1 = gson1.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code1 = mOrderDetailBean1.getRsp_code();
                    String Rsp_msgs = mOrderDetailBean1.getRsp_msg();
                    if (code1.equals("0000")) {
                        String mOrderStatus= mOrderDetailBean1.getOrder_status();// 订单状态
                        String mEnd_time = mOrderDetailBean1.getEnd_pay_time();// 付款剩余时间
                        if (mOrderStatus.equals("INIT")){ // 待付款
                            mStatusTv.setText("待付款");
                            mEndTimeLl.setVisibility(View.VISIBLE);// 显示剩余时间LL
                            mAddress_ll.setVisibility(View.GONE);// 物流栏隐藏
                            mEndTimeTv.setText(mEnd_time); // 倒计时
                            mPayforTv.setVisibility(View.VISIBLE);// 付款按钮显示
                        }
                        SetOrderDetailBeanData(mOrderDetailBean1); // 给控件赋值
//                        initAdapter(mOrderDetailBean1);

                    }else if(code1.equals("1002")){
                        MyToast.showShort(OrderDetailsActivity.this,Rsp_msgs);
                    }else if(code1.equals("2083")){
                        MyToast.showShort(OrderDetailsActivity.this,Rsp_msgs);
                    }
                    break;
                case StaticField.DSH_SUCCESS:// 代收货
                    Gson gson2 = new Gson();
                    OrderDetailBean mOrderDetailBean2= gson2.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code2 = mOrderDetailBean2.getRsp_code();
                    String Rsp_msg2 = mOrderDetailBean2.getRsp_msg();
                    if (code2.equals("0000")) {
                        String mOrderStatus= mOrderDetailBean2.getOrder_status();// 订单状态
                        if (mOrderStatus.equals("UNSHIPPED")){ //待发货
                            mStatusTv.setText("待收货");
                            mEndTimeLl.setVisibility(View.GONE);// 隐藏剩余时间LL
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示

                        }else if (mOrderStatus.equals("SHIPPED")){ // 待收货
                            mStatusTv.setText("待收货");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                        }

                        SetOrderDetailBeanData(mOrderDetailBean2); // 给控件赋值
//                        initAdapter(mOrderDetailBean2);
                    }else if (code2.equals("1002")){
                        MyToast.showShort(OrderDetailsActivity.this,Rsp_msg2);
                    }
                    break;
                case StaticField.WC_SUCCESS:// 已完成
                    Gson gson3 = new Gson();
                    OrderDetailBean mOrderDetailBean3= gson3.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code3 = mOrderDetailBean3.getRsp_code();
                    String Rsp_msg3 = mOrderDetailBean3.getRsp_msg();
                    if (code3.equals("0000")) {
                        String mOrderStatus= mOrderDetailBean3.getOrder_status();// 订单状态
                        if (mOrderStatus.equals("SIGN")){ // 交易完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示

                        }else if (mOrderStatus.equals("SUCCESS")){ // 交易完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示

                        }else if (mOrderStatus.equals("FINISH")){ // 已完成
                            mStatusTv.setText("已完成");
                            mEndTimeLl.setVisibility(View.GONE);
                            mAddress_ll.setVisibility(View.VISIBLE);// 物流栏显示
                        }
                        SetOrderDetailBeanData(mOrderDetailBean3); // 给控件赋值
//                        initAdapter(mOrderDetailBean3);
                    }else if (code3.equals("1002")){
                        MyToast.showShort(OrderDetailsActivity.this,Rsp_msg3);
                    }
                    break;
//                case StaticField.TK_SUCCESS:// 退款、退货
//                    Gson gson4 = new Gson();
//                    OrderDetailBean mOrderDetailBean4= gson4.fromJson((String) msg.obj, OrderDetailBean.class);
//                    String code4 = mOrderDetailBean4.getRsp_code();
//                    String Rsp_msg4 = mOrderDetailBean4.getRsp_msg();
//                    if (code4.equals("0000")) {
//                        String mOrderStatus= mOrderDetailBean4.getOrder_status();// 订单状态
//                    }
//                    break;
            }
        }
    };
    private String mExpress_no;

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
        mOrder_sn = bundle.getString(StaticField.ORDER_SN); // 交易订单号
        mGoods_sn = bundle.getString(StaticField.GOODS_SN); // 商品编号
        mOrderStatus= bundle.getString(StaticField.ORDERSTATUS); //订单状态
//        MyLog.LogShitou("传过来的商品编号===订单号", mGoods_sn + "===" + mOrder_sn+"===="+mOrderStatus);
        mBack = (ImageView) mOrderDetailsTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mOrderDetailsTitle.findViewById(R.id.base_title);
        mTitle.setText("订单详情");

        mPayment_ll = (LinearLayout) mOrderDetailsContent.findViewById(R.id.orderdetails_payment_ll);// 订单状态ll
        mStatusTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_detail_status_tv);// 订单状态
        mEndTimeLl = (LinearLayout) mOrderDetailsContent.findViewById(R.id.order_detail_endTime_ll);// 付款剩余时间ll
        mEndTimeTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_detail_endTime);// 付款剩余时间tv
        mOrderSn= (TextView) mOrderDetailsContent.findViewById(R.id.order_detail_orderSn);// 订单号
        mCreateTime= (TextView) mOrderDetailsContent.findViewById(R.id.order_detail_mCreate_time);// 下单时间
        mAddress_ll= (LinearLayout) mOrderDetailsContent.findViewById(R.id.orderDetails_Address_ll);// 物流栏

        mFirmOrderName= (TextView) mOrderDetailsContent.findViewById(R.id.firmOrder_name);// 收货人
        mCreateMobile= (TextView) mOrderDetailsContent.findViewById(R.id.firmOrder_mobile);// 电话
        mAddress= (TextView) mOrderDetailsContent.findViewById(R.id.firmOrder_detail_address);// 地址
        mExpressType = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_ExpressType);// 快递类型
        mFreight = (TextView) mOrderDetailsContent.findViewById(R.id.ExpressType_freight);// 快递费
        mServiceRate = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_ServiceRate);// 服务费率
        mServiceFee = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_ServiceFee);// 服务费
        mPayImg = (ImageView) mOrderDetailsContent.findViewById(R.id.order_details_payImg);// 支付方式图片
        mPayType = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_PayType);// 支付方式
        mGoodsCount = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_count);// 商品总件数
        mTotalPrice = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_total_price);// 商品总价
        mPayforTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_Payfor_tv);// 付款按钮
        mLookLogisticsTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_look_logistics_tv);// 查看物流按钮
        mClooseTv = (TextView) mOrderDetailsContent.findViewById(R.id.order_details_cloose_tv);// 交易已关闭

        mListView = (ExpandableListView) mOrderDetailsContent.findViewById(R.id.order_details_lv);


        mScrollView = (ScrollView) mOrderDetailsContent.findViewById(R.id.order_details_sv);
    }

    private void initDate(){
        if (mOrderStatus !=null){
            if (mOrderStatus.equals("QB")){// 全部
                GetInitNet(StaticField.QB);
            }else if(mOrderStatus.equals("DFK")){// 待付款
                GetInitNet(StaticField.DFK);
            }else if(mOrderStatus.equals("DSH")){ // 待收货
                GetInitNet(StaticField.DSH);
            }else if(mOrderStatus.equals("WC")){ // 已完成
                GetInitNet(StaticField.WC);
            }else if(mOrderStatus.equals("TK")){ // 退款
                GetInitNet(StaticField.TK);
            }
        }
    }


    private void initAdapter(OrderDetailBean mOrderDetailBean) {
        OrderDatilExpandableListAdapter expandableAdapter = new OrderDatilExpandableListAdapter(this, mBitmap, mOrderDetailBean);
        mListView.setAdapter(expandableAdapter);
        Utility.setListViewHeightBasedOnChildren(mListView);
//        expandableAdapter.notifyDataSetChanged();
        expandableAdapter.setCountOrPrice(this);
        //让子控件全部展开
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
        //去掉自带按钮
        mListView.setGroupIndicator(null);

        mScrollView.smoothScrollTo(0, 0);
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
//                Bundle bundle = new Bundle();
//                bundle.putString("ExpressNo",mExpress_no);
//                openActivityWitchAnimation(LogisticsDetailsActivity.class, bundle);
                break;
            case R.id.order_details_Payfor_tv://付款按钮
                break;
            case R.id.order_details_look_logistics_tv://查看物流栏
                openActivityWitchAnimation(LogisticsDetailsActivity.class);
                break;
        }
    }


    /**
     * 订单详情网络请求
     */
    private void GetInitNet(final String orderStatus){
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
     * 给控件赋值
     * @param orderDetailBean
     */
    private void SetOrderDetailBeanData(OrderDetailBean orderDetailBean){
        String mOrderSns = orderDetailBean.getOrder_sn();// 订单号
        mOrderSn.setText(mOrderSns);

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
        String mFreights = orderDetailBean.getFreight();// 运费
        mFreight.setText("￥"+mFreights);
        String mServiceRates = orderDetailBean.getService_fee_rate();// 服务费率
        mServiceRate.setText("服务费("+mServiceRates+")");
        String mServiceFees = orderDetailBean.getService_fee();// 服务费
        mServiceFee.setText("￥"+mServiceFees);
        String mPayTypes = orderDetailBean.getPay_type();// 支付方式
        if (mPayTypes.equals("ALIPAY")){
            mPayImg.setImageResource(R.mipmap.zhifubao);
            mPayType.setText("支付宝");
        }else{
            mPayImg.setImageResource(R.mipmap.weixin_pay);
            mPayType.setText("微信");
        }

    }

    /**
     * 适配器回调过来的数据
     * @param count  商品数量
     * @param price 商品总价格
     */
    @Override
    public void GetCountOrPrice(String count, String price,String express_no) {
        mGoodsCount.setText("共"+count+"件商品");
        mTotalPrice.setText("￥" + price);
        mExpress_no = express_no;// 快递单号
    }
}
