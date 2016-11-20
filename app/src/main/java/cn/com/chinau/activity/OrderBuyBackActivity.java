package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.OrderAdapter;
import cn.com.chinau.adapter.OrderSweepAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.OrderSweepBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 扫码回购订单页面
 */
public class OrderBuyBackActivity extends BaseActivity implements View.OnClickListener {

    private View mOrderTitle;
    private View mOrderContent;
    private TextView mTitle,mOederTv;
    private ImageView mback;
    private RadioGroup mRadioGroup;
    private RadioButton mOrderSweepBtn,mOrderBtn;
    private ArrayList<OrderSweepBean.Orderbean> mList;
    private ListView mlistview;
    private OrderSweepAdapter adapter;
    private ListView mOederListview;
    private String mToken,mUser_id,result;
    private SharedPreferences sp;
    private int num = 0;//初始索引
    private OrderAdapter adapters;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 扫码回购
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    OrderSweepBean mOrderSweepBean = gson.fromJson(msge, OrderSweepBean.class);
                    String mRsp_code = mOrderSweepBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mOrderSweepBean.getOrder_list();
                        if (mList != null ) {
                            mOederListview.setVisibility(View.VISIBLE);
                            initAdapter();
                        }else {
                            GoneOrVisibleView(); // ListView为空时显示的布局
                        }
                    }
                    break;
                case StaticField.ORDERS_SUCCESS: // 回购
                    Gson gsons = new Gson();
                    OrderSweepBean mOrderSweepBeans = gsons.fromJson((String) msg.obj, OrderSweepBean.class);
                    String mRsp_codes = mOrderSweepBeans.getRsp_code();
                    if (mRsp_codes.equals("0000")) {
                        mList = mOrderSweepBeans.getOrder_list();
                        if (mList != null && mList.size() != 0) {
                              adapters =  new OrderAdapter(OrderBuyBackActivity.this, mBitmap, mList);
                            mOederListview.setAdapter(adapters);
                            adapters.notifyDataSetChanged();
                        }else {
                            GoneOrVisibleView();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public View CreateTitle() {
        mOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderContent = View.inflate(this, R.layout.activity_order, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_PRIVATE);
        initView();
        initDada();
        initListener();
        return mOrderContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        mTitle= (TextView)mOrderTitle.findViewById(R.id.base_title);
        mTitle.setText("回购订单");
        mback = (ImageView)mOrderTitle.findViewById(R.id.base_title_back);


        mRadioGroup = (RadioGroup)mOrderContent.findViewById(R.id.radioGroup);
        mOrderSweepBtn = (RadioButton)mOrderContent.findViewById(R.id.record_sweep_btn);
        mOrderBtn = (RadioButton)mOrderContent.findViewById(R.id.order_btn);
        mRadioGroup.check(R.id.record_sweep_btn);// 默认选中
        mOederListview = (ListView)mOrderContent.findViewById(R.id.listView);
        mOederTv = (TextView)mOrderContent.findViewById(R.id.no_order_tv);


    }

    private void initDada() {
        GetInitNet(num,StaticField.SM);
    }

    private void initListener() {
        mback.setOnClickListener(this);
        mOrderSweepBtn.setOnClickListener(this);
        mOrderBtn.setOnClickListener(this);

        mOederListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mOrder_sn =  mList.get(i).getOrder_sn();
                Bundle bundle = new Bundle();
                bundle.putString("Order_sn",mOrder_sn);
//                MyLog.LogShitou("mOrder_sn回购订单的编号",mOrder_sn);
                openActivityWitchAnimation(ScanOrderBuyDetailActivity.class,bundle);
            }
        });
    }

    // ListView为空时显示的布局
    private void GoneOrVisibleView() {
        mOederListview.setVisibility(View.GONE);
        mOederTv.setVisibility(View.VISIBLE); // 无信息控件显示
        mOederTv.setText("暂无订单信息~");
    }

    private void initAdapter() {
        adapter = new OrderSweepAdapter(this, mBitmap, mList);
        mOederListview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.record_sweep_btn://扫码回购
                GetInitNet(num,StaticField.SM);
                break;
            case R.id.order_btn: // 回购订单
                GetInitNet(num,StaticField.PT);
                break;

            case R.id.base_title_back:
                finishWitchAnimation();
                break;
        }

    }

    /**
     * 回购订单列表的网络请求
     * @param num 索引
     * @param buyback_type 回购类型
     */
    private void GetInitNet(final int num, final String buyback_type){
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ORDERLIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                params.put(StaticField.BUYBACK_TYPE, buyback_type); // //订单类型：SM：扫码回购；PT：普通回购

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou(buyback_type+"-:"+"回购订单List-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                if (buyback_type.equals("SM")){
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }else {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.ORDERS_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

}
