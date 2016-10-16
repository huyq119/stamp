package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.OrderBuyDetailBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 扫码回购订单详情页面
 */
public class ScanOrderBuyDetailActivity extends BaseActivity implements View.OnClickListener {

    private View mOrderAuditingTitle;
    private View mOrderAuditingContent;
    private ImageView mBack;
    private String Order_sn;
    private String mToken, mUser_id;
    private SharedPreferences sp;
    private TextView mOrderStatus, mOrderStatusDes, mOrderSn, mOrderTime, mOrderName, mOrderTimeBuy, mOrderPrice, mOrderIncome, mOrderFeeRate,
            mOrderBackPrice, mOrderUsers, mOrderPhone, mOrderExpressName, mOrderExpressNum, mOrderExpressNameBack, mOrderExpressNumBack;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 订单详情
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    OrderBuyDetailBean mOrderBuyDetailBean = gson.fromJson(msge, OrderBuyDetailBean.class);
                    String mRsp_code = mOrderBuyDetailBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        String mStatus = mOrderBuyDetailBean.getOrder_status();// 订单状态
                        if (mStatus.equals("INIT")) {// 待寄送
                            mOrderStatus.setText("待寄送");
                            mLlOrderSend.setVisibility(View.VISIBLE);
                            mLlOrderExpressName.setVisibility(View.GONE);
                            mLlOrderExpressNum.setVisibility(View.GONE);
                        } else if (mStatus.equals("CLOSE")) { // 订单关闭
                            mOrderStatus.setText("订单关闭");
                            mLlOrderExpressName.setVisibility(View.GONE);
                            mLlOrderExpressNum.setVisibility(View.GONE);
                        } else if (mStatus.equals("AUDITING")) {// 审核中
                            mOrderStatus.setText("审核中");
                            mLlOrderExpressName.setVisibility(View.VISIBLE);
                            mLlOrderExpressNum.setVisibility(View.VISIBLE);
                            mOrderExpressName.setText(mOrderBuyDetailBean.getBack_express_comp()); // 快递公司
                            mOrderExpressNum.setText(mOrderBuyDetailBean.getBack_express_no()); // 快递单号
                        } else if (mStatus.equals("FINISH")) { // 已完成
                            mOrderStatus.setText("已完成");
                            mLlOrderExpressName.setVisibility(View.VISIBLE);
                            mLlOrderExpressNum.setVisibility(View.VISIBLE);
                            mOrderStatusDes.setVisibility(View.VISIBLE);
                            mOrderStatusDes.setText("回购款项请到余额中查收");
                            mOrderExpressName.setText(mOrderBuyDetailBean.getBack_express_comp()); // 快递公司
                            mOrderExpressNum.setText(mOrderBuyDetailBean.getBack_express_no()); // 快递单号
                        } else if (mStatus.equals("REFUSE")) { //订单驳回
                            mOrderStatus.setText("订单驳回");
                            mLlOrderExpressName.setVisibility(View.VISIBLE);
                            mLlOrderExpressNum.setVisibility(View.VISIBLE);
                            mOrderStatusDes.setVisibility(View.VISIBLE);
                            mLlOrderDetailExpressName.setVisibility(View.VISIBLE);
                            mOrderStatusDes.setText("邮品品相破损严重不予进行回购");
                            mOrderExpressName.setText(mOrderBuyDetailBean.getBack_express_comp()); // 快递公司
                            mOrderExpressNum.setText(mOrderBuyDetailBean.getBack_express_no()); // 快递单号
                        }
                        mOrderSn.setText(mOrderBuyDetailBean.getOrder_sn()); // 订单编号
                        mOrderTime.setText(mOrderBuyDetailBean.getCreate_time()); // 订单创建时间
                        mOrderName.setText(mOrderBuyDetailBean.getGoods_name()); // 商品名称
                        mOrderTimeBuy.setText(mOrderBuyDetailBean.getBuy_time()); // 购买时间
                        mOrderPrice.setText("￥" + mOrderBuyDetailBean.getCurrent_price()); // 当前价格
                        mOrderIncome.setText(mOrderBuyDetailBean.getIncome()); // 预计收益
                        mOrderFeeRate.setText(mOrderBuyDetailBean.getService_fee_rate()); // 服务费率
                        mOrderBackPrice.setText("￥" + mOrderBuyDetailBean.getBuyback_price()); //  回购价格
                        mOrderUsers.setText(mOrderBuyDetailBean.getBuyer()); // 用户
                        mOrderPhone.setText(mOrderBuyDetailBean.getMobile()); // 手机号

                    }
                    break;
            }
        }
    };
    private LinearLayout mLlOrderSend, mLlOrderExpressName, mLlOrderExpressNum, mLlOrderDetailExpressName;
    private Button mBtnOrderSend;


    @Override
    public View CreateTitle() {
        mOrderAuditingTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderAuditingTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderAuditingContent = View.inflate(this, R.layout.activity_orderauditing, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mOrderAuditingContent;
    }


    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        Bundle bundle = getIntent().getExtras();
        Order_sn = bundle.getString("Order_sn", "");
        MyLog.LogShitou("传过来的订单编号", Order_sn);

        mBack = (ImageView) mOrderAuditingTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mOrderAuditingTitle.findViewById(R.id.base_title);
        mTitle.setText("订单详情");
        mOrderStatus = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_status);
        mOrderStatusDes = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_status_des);
        mOrderSn = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_sn);
        mOrderTime = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_time);
        mOrderName = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_name);
        mOrderTimeBuy = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_time_buy);
        mOrderPrice = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_current_price);
        mOrderIncome = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_income);
        mOrderFeeRate = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_service_fee_rate);
        mOrderBackPrice = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_back_price);
        mOrderUsers = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_users);
        mOrderPhone = (TextView) mOrderAuditingContent.findViewById(R.id.tv_order_phone);
        mOrderExpressName = (TextView) mOrderAuditingContent.findViewById(R.id.tv_Express_name);
        mOrderExpressNum = (TextView) mOrderAuditingContent.findViewById(R.id.tv_express_num);
        mLlOrderSend = (LinearLayout) mOrderAuditingContent.findViewById(R.id.ll_order_send);
        mBtnOrderSend = (Button) mOrderAuditingContent.findViewById(R.id.btn_order_send);
        mLlOrderExpressName = (LinearLayout) mOrderAuditingContent.findViewById(R.id.ll_order_express_name);
        mLlOrderExpressNum = (LinearLayout) mOrderAuditingContent.findViewById(R.id.ll_order_express_num);
        // 回寄快递信息
        mLlOrderDetailExpressName = (LinearLayout) mOrderAuditingContent.findViewById(R.id.ll_order_detail_express_name);
        // 回寄快递公司
        mOrderExpressNameBack = (TextView) mOrderAuditingContent.findViewById(R.id.tv_Express_name_back);
        // 回寄快递编号
        mOrderExpressNumBack = (TextView) mOrderAuditingContent.findViewById(R.id.tv_Express_num_back);


    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mBtnOrderSend.setOnClickListener(this);
    }

    private void initData() {
        GetOdeerDetail(); // 订单详情网络请求
    }


    /**
     * 订单详情网络请求
     */
    private void GetOdeerDetail() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ORDERDETAIL);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.ORDER_SN, Order_sn); // 回购订单编号

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("回购订单详情", result);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back: // 返回
                finishWitchAnimation();
                break;
            case R.id.btn_order_send: // 去寄送
                Bundle bundle = new Bundle();
                bundle.putString("ScanOrderBuyDetail","ScanOrderBuyDetail");
                openActivityWitchAnimation(FastMailInfoActivity.class,bundle);
                break;
        }
    }
}
