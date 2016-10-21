package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.FirmOrderExpandableAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.FirmOrderBean;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.dialog.SelectDistributionPopupWindow;
import cn.com.chinau.dialog.SelectPayPopupWindow;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.CustExpandableListView;
import cn.com.chinau.view.VerticalScrollView;

/**
 * 确认订单页面
 */
public class FirmOrderActivity extends BaseActivity implements View.OnClickListener {

    private View mFirmOrderTitle;
    private View mFirmOrderContent;
    private LinearLayout mAddress, mNoAddress;//有收货地址,没有收货地址
    private LinearLayout mPay, mDistribution;//支付方式,配送方式
    private SelectPayPopupWindow mPayPopupWindow;//支付的弹出框
    private SelectDistributionPopupWindow mDistributionPopupWindow;//配送方式的弹出框
    private CustExpandableListView mListView;//底部列表展示
    private ImageView mBack,mPayImg;
    private TextView mTitle,mOkPay,mDistributionTv,mDistributionPrice,mPayNmme,mFeeRate,mAddressName,
            mAddressMobile,mAddressDetail,mNoAddressAdd;

    private LinearLayout mAddress1;
    private String mAuctionRecord;
    private String mShopGoodsJson;// 传过来的Json串
    private SharedPreferences sp;
    private String mToken,mUser_id;

    private ArrayList<FirmOrderBean.AddressList> address_list;
    private ArrayList<FirmOrderBean.ExpreeFee> express_fee;
    private ArrayList<FirmOrderBean.ExpreeComp> express_comp;
    private VerticalScrollView mFirmOrder_SV;

    @Override
    public View CreateTitle() {
        mFirmOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        return mFirmOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mFirmOrderContent = View.inflate(this, R.layout.activity_firmorder, null);
        initView();
        initData();
        initListener();
        return mFirmOrderContent;
    }

    private void initView() {

        Intent intent = getIntent();
        mShopGoodsJson = intent.getStringExtra("ShopGoodsJson");

        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        mBack =(ImageView) mFirmOrderTitle.findViewById(R.id.base_title_back);
        mTitle =(TextView) mFirmOrderTitle.findViewById(R.id.base_title);
        mTitle.setText("确认订单");

        mFirmOrder_SV = (VerticalScrollView) mFirmOrderContent.findViewById(R.id.firmOrder_SV);

        //有地址显示的地址布局
        mAddress = (LinearLayout) mFirmOrderContent.findViewById(R.id.FirmOrder_Address);
        mAddressName = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_name);
        mAddressMobile = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_mobile);
        mAddressDetail = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_detail_address);
        //无地址显示的布局
        mNoAddress = (LinearLayout) mFirmOrderContent.findViewById(R.id.FirmOrder_NoAddress);
        mNoAddressAdd = (TextView) mFirmOrderContent.findViewById(R.id.NoAddress_add);

        mPay = (LinearLayout) mFirmOrderContent.findViewById(R.id.FirmOrder_pay);
        mDistribution = (LinearLayout) mFirmOrderContent.findViewById(R.id.FirmOrder_distribution);
        mListView = (CustExpandableListView) mFirmOrderContent.findViewById(R.id.firmOrder_expandableLV);
        mOkPay = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_ok_pay);
        mDistributionTv = (TextView) mFirmOrderContent.findViewById(R.id.distribution_tv);
        mDistributionPrice = (TextView) mFirmOrderContent.findViewById(R.id.distribution_price_tv);
        mPayImg = (ImageView) mFirmOrderContent.findViewById(R.id.firmorder_pay_img);
        mPayNmme = (TextView) mFirmOrderContent.findViewById(R.id.firmorder_pay_name);
        mFeeRate = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_fee_rate);// 服务费率

    }

    private void initData() {
        setFalseData();
        FirmOrderNet(mShopGoodsJson.toString());// 确认订单list网络请求
    }

    private void initListener() {
        mPay.setOnClickListener(this);
        mDistribution.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mOkPay.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mNoAddress.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }
    /**
     * 设置假数据
     */
    private void setFalseData() {
        ArrayList<ShopNameBean.GoodsBean> mGoodsBean = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ShopNameBean.GoodsBean goodsBean = new ShopNameBean.GoodsBean("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg", "38596", "庚申年" + i, "1500", "1");
            mGoodsBean.add(goodsBean);
        }

        ArrayList<ShopNameBean.SellerBean> mSellerBean = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ShopNameBean.SellerBean sellerBean = new ShopNameBean.SellerBean("淘宝自营商城", "338955", "自营", mGoodsBean);
            mSellerBean.add(sellerBean);
        }

        ShopNameBean shopNameBean = new ShopNameBean(mSellerBean, "35000");

        FirmOrderExpandableAdapter expandableAdapter = new FirmOrderExpandableAdapter(this, mBitmap, shopNameBean);
        mListView.setAdapter(expandableAdapter);
        //让子控件全部展开
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
        //去掉自带按钮
        mListView.setGroupIndicator(null);
    }

    /**
     * 是否有收货地址的判断
     *
     * @return true, 有收获地址 false,没有收获地址
     */
    public boolean isAddress() {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FirmOrder_pay://支付方式
                mPayPopupWindow = new SelectPayPopupWindow(this, mPayWindowListener);
                //设置layout在PopupWindow中显示的位置,因为这里写了全屏的所以就没有居中
                mPayPopupWindow.showAtLocation(this.findViewById(R.id.FirmOrder), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.FirmOrder_distribution://配送方式
                mDistributionPopupWindow = new SelectDistributionPopupWindow(this, mDistributionWindowListener,express_comp,express_fee);
                mDistributionPopupWindow.showAtLocation(this.findViewById(R.id.FirmOrder), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.FirmOrder_NoAddress://没有收获地址
                openActivityWitchAnimation(ManagerAddressActivity.class);
                break;
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.firmOrder_ok_pay://确认并付款
                MyToast.showShort(this,"调取微信或者支付宝页面。。。");
                break;
            case R.id.FirmOrder_Address: // 选择收货地址
                openActivityWitchAnimation(ChooseReceiverAddress.class);
                break;
        }
    }

    /**
     * 支付方式的内部监听
     */
    private View.OnClickListener mPayWindowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPayPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.Wechat_click://点击了微信按钮
                    mPayImg.setImageResource(R.mipmap.weixin_pay);
                    mPayNmme.setText("微信");
                    break;
                case R.id.Alipay_click://点击了支付宝按钮
                    mPayImg.setImageResource(R.mipmap.zhifubao);
                    mPayNmme.setText("支付宝");
                    break;
            }
        }
    };

    /**
     * 配送方式的内部监听
     */
    private View.OnClickListener mDistributionWindowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mDistributionPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.EMS_click_image://Ems的点击按钮
                    ImageView img =  (ImageView) view;
                    ImageView img2 =  (ImageView) view;
                    img.setImageResource(R.mipmap.circle_select_click);
                    img2.setImageResource(R.mipmap.circle_select);
                    mDistributionTv.setText(express_comp.get(0).getValue());
                    mDistributionPrice.setText("￥"+express_fee.get(0).getValue());
                    break;
                case R.id.SF_click_image://顺丰的点击按钮
                    ImageView img0 =  (ImageView) view;
                    ImageView img1 =  (ImageView) view;
                    img0.setImageResource(R.mipmap.circle_select_click);
                    img1.setImageResource(R.mipmap.circle_select);
                    mDistributionTv.setText( express_comp.get(1).getValue());
                    mDistributionPrice.setText("￥"+express_fee.get(1).getValue());
                    break;
            }
        }
    };


    /**
     * 确认订单list网络请求
     * @param Goods_info 商品json串
     */
    private void FirmOrderNet(final String Goods_info){
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SHOPCARTSETTLE);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODESINFO, Goods_info);//  商品信息：所有商品的json字符串

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result结算列表", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.DeleteSUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case StaticField.DeleteSUCCESS:// 删除
                    Gson gsones = new Gson();
                    FirmOrderBean mFirmOrderBean = gsones.fromJson((String)msg.obj,FirmOrderBean.class);
                   String mCode = mFirmOrderBean.getRsp_code();
                    if (mCode.equals("0000")){
                     String mFeeRates =  mFirmOrderBean.getService_fee_rate();// 服务费率
                        mFeeRate.setText("￥"+mFeeRates);
                        // 快递公司
                        express_comp = mFirmOrderBean.getExpress_comp();
                        // 快递费价格
                        express_fee = mFirmOrderBean.getExpress_fee();

                        // 地址列表
                        address_list = mFirmOrderBean.getAddress_list();
                        if (address_list!=null && address_list.size() != 0){
                            mAddress.setVisibility(View.VISIBLE);// 显示收货地址布局
                            mNoAddress.setVisibility(View.GONE); // 隐藏无收货地址布局
                            String mName = address_list.get(0).getName();
                            mAddressName.setText(mName);
                            String mMobile = address_list.get(0).getMobile();
                            mAddressMobile.setText(mMobile);
                            String mDetail = address_list.get(0).getDetail();
                            mAddressDetail.setText(mDetail);
                        }else{
                            mNoAddress.setVisibility(View.VISIBLE); // 显示无收货地址布局
                            mAddress.setVisibility(View.GONE);// 隐藏收货地址布局
                        }
                    }
                    break;
            }


        }
    };

}
