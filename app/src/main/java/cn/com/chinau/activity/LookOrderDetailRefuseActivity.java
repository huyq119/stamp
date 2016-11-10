package cn.com.chinau.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.LookOrderDetailRefuseAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.LookOrderDetailRefuseBean;
import cn.com.chinau.bean.OrderDetailBean;
import cn.com.chinau.dialog.PhoneDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * Created by Administrator on 2016/9/22.
 * (查看订单详情)退款/退货详情页面
 */
public class LookOrderDetailRefuseActivity extends BaseActivity {

    private View mRefuseTitle, mRefuseContent;
    private ImageView mBack;
    private TextView mTitle, mServiceTel,mOrderSn,mTime;
    private ListView mListView;
    private ArrayList<LookOrderDetailRefuseBean.RefundLiistBean> mLsit;
    private String mToken, mUser_id;// 标识，用户ID
    private SharedPreferences sp;
    private String mOrder_sn, mGoods_sn, mOrderStatus;

    @Override
    public View CreateTitle() {
        mRefuseTitle = View.inflate(this, R.layout.base_back_title, null);
        return mRefuseTitle;
    }

    @Override
    public View CreateSuccess() {
        mRefuseContent = View.inflate(this, R.layout.activity_look_order_detail_refuse, null);
        sp = getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        initData();
        return mRefuseContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        Bundle bundle = getIntent().getExtras();
        mOrder_sn = bundle.getString(StaticField.ORDER_SN); // 交易订单号
        mGoods_sn = bundle.getString(StaticField.GOODS_SN); // 商品编号
        mOrderStatus = bundle.getString(StaticField.ORDERSTATUS); //订单状态
        MyLog.LogShitou("订单号+商品编号+类型",mOrder_sn+"=="+mGoods_sn+"==="+mOrderStatus);
        mBack = (ImageView) mRefuseTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mRefuseTitle.findViewById(R.id.base_title);
        mTitle.setText("退款/退货");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        mListView = (ListView) mRefuseContent.findViewById(R.id.look_order_detail_listview);
        mServiceTel = (TextView) mRefuseContent.findViewById(R.id.Service_tel);
        mServiceTel.setOnClickListener(new View.OnClickListener() {//客服电话
            @Override
            public void onClick(View view) {
                PhoneDialog phoneDialog = new PhoneDialog(LookOrderDetailRefuseActivity.this, mServiceTel.getText().toString());
                phoneDialog.show();
            }
        });
        mOrderSn= (TextView) mRefuseContent.findViewById(R.id.look_order_detail_orderSn);
        mTime= (TextView) mRefuseContent.findViewById(R.id.look_order_detail_time);

    }

    private void initData() {
        GetInitNet();// 退款详情网络请求
    }

    private void initAdapter() {
        LookOrderDetailRefuseAdapter mAdapter = new LookOrderDetailRefuseAdapter(this, mLsit);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 退款详情网络请求
     */
    private void GetInitNet() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.REFUNDDETAIL);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id); // 用户ID
                params.put(StaticField.ORDER_SN, mOrder_sn);// 交易订单号
                params.put(StaticField.GOODS_SN, mGoods_sn); // 商品编号

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("退款详情数据", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                if (mOrderStatus.equals("TK")) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (mOrderStatus.equals("QB")) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.CG_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }
        });

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// 退款详情
            switch (msg.what) {
                case StaticField.SUCCESS:// TK
                    Gson gson0 = new Gson();
                    LookOrderDetailRefuseBean mRefuseBean = gson0.fromJson((String) msg.obj, LookOrderDetailRefuseBean.class);
                    String code = mRefuseBean.getRsp_code();
                    if (code.equals("0000")) {
                        mOrderSn.setText(mRefuseBean.getOrder_sn());
                        mTime.setText(mRefuseBean.getCreate_time());
                        mLsit = mRefuseBean.getRefund_rec_list();
                        initAdapter();
                    }else if (code.equals("1002")){
                        MyToast.showShort(LookOrderDetailRefuseActivity.this, "请重新登录");
                    }
                    break;
                case StaticField.CG_SUCCESS://     QB
                    Gson gson1 = new Gson();
                    OrderDetailBean mOrderDetailBean1 = gson1.fromJson((String) msg.obj, OrderDetailBean.class);
                    String code1 = mOrderDetailBean1.getRsp_code();
                    String Rsp_msg1 = mOrderDetailBean1.getRsp_msg();
                    break;
            }

        }
    };

}
