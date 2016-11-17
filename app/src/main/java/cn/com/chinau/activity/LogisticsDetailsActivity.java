package cn.com.chinau.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.LogisticsDetailsListViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.LogisticsBean;
import cn.com.chinau.bitmap.BitmapHelper;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 物流详情页面
 */
public class LogisticsDetailsActivity extends BaseActivity {

    private View mLogisticsDetailsTitle;
    private View mLogisticsDetailsContent;
    private ListView mLogisticsListView;//物流展示信息的ListView
    private List<LogisticsBean.Express> mList;//物流信息的集合
    private String mExpressNo,mOrderStatus;
    private String mToken, mUser_id;// 标识，用户ID
    private SharedPreferences sp;
    private TextView mStatus, mExpress, mOrderSn, mExpressPhone;
    private String mExpress_status, mExpress_no,mExpress_comp, mExpress_phone, mGoods_image;
    private BitmapUtils mBitmap;
    private ImageView mImg;
    private LinearLayout mLogisticsDetailsLl;
    private TextView mOrederTv;


    @Override
    public View CreateTitle() {
        mLogisticsDetailsTitle = View.inflate(this, R.layout.base_back_title, null);
        return mLogisticsDetailsTitle;
    }

    @Override
    public View CreateSuccess() {
        mLogisticsDetailsContent = View.inflate(this, R.layout.activity_logisticsdetails, null);
        sp = getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        mBitmap = BitmapHelper.getBitmapUtils();
        initView();
        initData();
        return mLogisticsDetailsContent;
    }



    private void initView() {
        Bundle bundle = getIntent().getExtras();
        mExpressNo = bundle.getString("ExpressNo", "");
        mOrderStatus= bundle.getString("OrderStatus", "");


        mLogisticsDetailsLl = (LinearLayout) mLogisticsDetailsContent.findViewById(R.id.logisticsDetails_ll);
        mOrederTv = (TextView) mLogisticsDetailsContent.findViewById(R.id.no_order_tv);

        if (mOrderStatus.equals("UNSHIPPED")){
            mLogisticsDetailsLl.setVisibility(View.GONE);
            mOrederTv.setVisibility(View.VISIBLE);
            mOrederTv.setText("暂无物流信息~");
        }

        MyLog.LogShitou("传过来的快递单号",mExpressNo+"=="+mOrderStatus);

        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        ImageView mBack = (ImageView) mLogisticsDetailsTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mLogisticsDetailsTitle.findViewById(R.id.base_title);
        mTitle.setText("物流详情");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        mLogisticsListView = (ListView) mLogisticsDetailsContent.findViewById(R.id.logisticsDetails_LV);
        mImg = (ImageView) mLogisticsDetailsContent.findViewById(R.id.logisticsDetails_img);
        mStatus = (TextView) mLogisticsDetailsContent.findViewById(R.id.logisticsDetails_status);
        mExpress = (TextView) mLogisticsDetailsContent.findViewById(R.id.logisticsDetails_Express);
        mOrderSn = (TextView) mLogisticsDetailsContent.findViewById(R.id.logisticsDetails_OrderSn);
        mExpressPhone = (TextView) mLogisticsDetailsContent.findViewById(R.id.logisticsDetails_Express_phone);

    }

    private void initData() {
        if (!mExpressNo.equals("")){
            GetInitNet(); // 物流详情网络请求
        }

    }

    private void initAdapter() {
        LogisticsDetailsListViewAdapter Adapter = new LogisticsDetailsListViewAdapter(this, mList);
        mLogisticsListView.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();
    }

    @Override
    public void AgainRequest() {

    }


    /**
     * 物流详情网络请求
     */
    private void GetInitNet() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.EXPRESSQUERY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id); // 用户ID
                params.put(StaticField.EXPRESS_NO, mExpressNo);// 快递单号

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("===物流详情数据", result);

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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 物流信息详情
                    Gson gson0 = new Gson();
                    LogisticsBean mLogisticsBean = gson0.fromJson((String) msg.obj, LogisticsBean.class);
                    String code = mLogisticsBean.getRsp_code();
                    String mRsp_msg = mLogisticsBean.getRsp_msg();
                    if (code.equals("0000")) {
                        mExpress_status = mLogisticsBean.getExpress_status();// 订单状态
                        if (mExpress_status.equals("UNSHIPPED")) {
                            mStatus.setText("待收货");
                        }else if (mExpress_status.equals("SHIPPED")) {
                            mStatus.setText("待收货");
                        }else if (mExpress_status.equals("SIGN")) {
                            mStatus.setText("已完成");
                        }else if (mExpress_status.equals("SUCCESS")) {
                            mStatus.setText("已完成");
                        }else if (mExpress_status.equals("FINISH")) {
                            mStatus.setText("已完成");
                        }
                        mExpress_no = mLogisticsBean.getExpress_no();// 快递编号
                        mOrderSn.setText(mExpress_no);
                       mExpress_comp = mLogisticsBean.getExpress_comp();// 快递公司
                        mExpress.setText(mExpress_comp);
                        mExpress_phone = mLogisticsBean.getService_phone();// 官方电话
                        mExpressPhone.setText(mExpress_phone);
                        mGoods_image = mLogisticsBean.getGoods_image();// 商品图片
                        mBitmap.display(mImg,mGoods_image);

                        mList = mLogisticsBean.getExpress_info_list();
                        initAdapter();

                    } else if (code.equals("1002")) {
                        MyToast.showShort(LogisticsDetailsActivity.this, mRsp_msg);
                    }
                    break;

            }
        }
    };
}
