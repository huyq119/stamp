package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.FirmOrderExpandableAdapter;
import cn.com.chinau.aliapi.PayResult;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.AddShopCartBean;
import cn.com.chinau.bean.FirmOrderBean;
import cn.com.chinau.bean.OrderPayBean;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.dialog.SelectDistributionPopupWindow;
import cn.com.chinau.dialog.SelectPayPopupWindow;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.ui.MyApplication;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.VerticalScrollView;

import static cn.com.chinau.R.id.FirmOrder;

/**
 * 确认订单页面
 */
public class FirmOrderActivity extends BaseActivity implements View.OnClickListener, SelectDistributionPopupWindow.StringText {

    private View mFirmOrderTitle;
    private View mFirmOrderContent;
    private LinearLayout mAddress, mNoAddress;//有收货地址,没有收货地址
    private LinearLayout mPay, mDistribution;//支付方式,配送方式
    private SelectPayPopupWindow mPayPopupWindow;//支付的弹出框
    private SelectDistributionPopupWindow mDistributionPopupWindow;//配送方式的弹出框
    //    private CustomExpandableListView mListView;//底部列表展示
    private ListView mListView;//底部列表展示
    private ImageView mBack, mPayImg;
    private TextView mTitle, mOkPay, mDistributionTv, mDistributionPrice, mPayNmme, mFeeRate, mAddressName,
            mAddressMobile, mAddressDetail, mNoAddressAdd, mTotalPrice, mGoodsCount;

    private LinearLayout mAddress1;
    private String mAuctionRecord;
    //    private String mShopGoodsJson;// 传过来的Json串
    private SharedPreferences sp;
    private String mToken, mUser_id;

    private ArrayList<FirmOrderBean.AddressList> address_list;
    private ArrayList<FirmOrderBean.ExpreeFee> express_fee;
    private ArrayList<FirmOrderBean.ExpreeComp> express_comp;
    private VerticalScrollView mFirmOrder_SV;

    private static final int REQUESTCODE = 1;
    private String mNoAddressId;// 没有去选择地址获取的地址id
    private String mAddressId;// 去选择地址回调回来的地址id
    private String mTimeId; // 客户订单请求号
    private String mExCompName;
    private String payNmae;
    private IWXAPI api;
    private String address_id;
    private String mGroupSet;
    private ArrayList<AddShopCartBean> info_list = new ArrayList<>();
    //    private HashMap<Integer, Set<ShopNameBean.GoodsBean>> groupSet;
    private Integer key;
    private String goods_sn;
    private String goods_count;

    private boolean isChild;
    private String mPrice, mCount, mSellerList, mFirmOrder;// 价钱，数量
    //    private String groupSet;
    private HashMap<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> groupSet;
    private String info_list1;

    @Override
    public View CreateTitle() {
        mFirmOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        return mFirmOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mFirmOrderContent = View.inflate(this, R.layout.activity_firmorder, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mFirmOrderContent;
    }

    private void initView() {
        String date = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());// 获取时间毫秒级
        int count = (int) (Math.random() * 9000) + 1000; // 获取4位随机数
        //获取时间毫秒级加4位随机数
        mTimeId = date + count;
        MyLog.LogShitou("时间毫秒级+4位随机数", date + count);

        GetStringData(); // 获取传过来的数据


        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        api = WXAPIFactory.createWXAPI(this, StaticField.APP_ID);
        api.registerApp(StaticField.APP_ID);

        mBack = (ImageView) mFirmOrderTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mFirmOrderTitle.findViewById(R.id.base_title);
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
        mListView = (ListView) mFirmOrderContent.findViewById(R.id.firmOrder_expandableLV);
        mOkPay = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_ok_pay);
        mDistributionTv = (TextView) mFirmOrderContent.findViewById(R.id.distribution_tv);
        mDistributionPrice = (TextView) mFirmOrderContent.findViewById(R.id.distribution_price_tv);
        mPayImg = (ImageView) mFirmOrderContent.findViewById(R.id.firmorder_pay_img);
        mPayNmme = (TextView) mFirmOrderContent.findViewById(R.id.firmorder_pay_name);
        mFeeRate = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_fee_rate);// 服务费率
        mTotalPrice = (TextView) mFirmOrderContent.findViewById(R.id.totl_price);// 价钱
        mTotalPrice.setText("￥" + mPrice);// 赋值总价钱
        mGoodsCount = (TextView) mFirmOrderContent.findViewById(R.id.goods_count);// 数量
        mGoodsCount.setText("共" + mCount + "件商品");
    }

    // 获取传过来的数据
    private void GetStringData() {
        Intent intent = getIntent();
        mCount = intent.getStringExtra("Count");// 获取传过来的数量
        mPrice = intent.getStringExtra("Price");// 获取传过来的总价钱

        String mSellerList = sp.getString("SellerList", "");// 获取保存在本地的购物车总数据

        MyLog.LogShitou("获取保存在本地的总数据", mSellerList);


//        groupSet1 = intent.getStringExtra("GroupSet");// 获取传过来的总价钱
//        ArrayList<String> noJsonToList = getNoJsonToList(groupSet1);
//        MyLog.LogShitou("传过来选中List", noJsonToList.toString());
//        StringBuffer  strb=new StringBuffer();
//        strb.append("\"aaa\":{");
//        for(int i=0;i<noJsonToList.size();i++){
////	     		list.set(i, "{"+list.get(i)+"}");
//            strb.append(noJsonToList.get(i));
//            strb.append(",");
//        }
//        String returnStr=strb.toString().trim().substring(0,strb.toString().trim().length()-1);
//        returnStr="{"+returnStr+"}}";
//        MyLog.LogShitou("传过来选中的编号+数量", returnStr);


        groupSet = MyApplication.getGroupSet();// 传过来的集合数据
        for (HashMap.Entry<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> entry : groupSet.entrySet()) {
//            key = entry.getKey();
            Set<ShopNameBean.SellerBean.GoodsBean> value = entry.getValue(); // 拿到循环后的value值
            for (int i = 0; i < value.size(); i++) {
                Iterator<ShopNameBean.SellerBean.GoodsBean> iterator = value.iterator();
                ShopNameBean.SellerBean.GoodsBean next = iterator.next();
                goods_sn = next.getGoods_sn();// 商品编号
                goods_count = next.getGoods_count();// 商品数量
                isChild = next.isChildSelected();
                MyLog.LogShitou("---1---编号+数量Json", goods_sn + "--" + goods_count);
            }
            // 添加数据到AddShopCartBean生成Json
            AddShopCartBean mAddShopCartBean = new AddShopCartBean();
            mAddShopCartBean.setGoods_sn(goods_sn);
            mAddShopCartBean.setGoods_count(goods_count);
            info_list.add(mAddShopCartBean); // 添加到list
            MyLog.LogShitou("==2===最后需要的编号+数量Json", info_list.toString());
        }
    }

    /**
     * @param soureStr 源字符
     * @return list
     */
    public static ArrayList<String> getNoJsonToList(String soureStr) {
        ArrayList<String> list = new ArrayList<String>();
        String arr1[] = soureStr.split("=");
        for (int i = 1; i < arr1.length; i++) {
            String arr2[] = arr1[i].split("],");
            for (int j = 0; j < arr2.length; j++) {
                if (j % 2 == 0) {
                    String str4 = arr2[j].trim().substring(1, arr2[j].trim().length());
//                      str4=str4.replace("GoodsBean","GoodsBean"+j);
//                    str4=str4.replaceAll("GoodsBean","GoodsBean"+j);
                    list.add(str4);
                }
            }
        }
        if (list.size() > 0) {
            String str = list.get(list.size() - 1).trim().substring(0, list.get(list.size() - 1).trim().length() - 1).trim();
            list.set(list.size() - 1, str.substring(0, str.length() - 1));
        }
        ;

//        StringBuffer  strb=new StringBuffer();
//        strb.append("\"aaa\":[");
//        for(int i=0;i<list.size();i++){
////	     		list.set(i, "{"+list.get(i)+"}");
//            strb.append(list.get(i));
//            strb.append(",");
//        }
//
//        String returnStr=strb.toString().trim().substring(0,strb.toString().trim().length()-1);
//        returnStr=returnStr+"]";


//        StringBuffer strbuffer=new StringBuffer();
//        for(int i=0;i<list.size();i++){
//            list.set(i, "{"+list.get(i)+"}");
//        }
//        list.set(i, "\"Goods_list\":"+"[{"+list.get(i)+"}]");
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(0).replace("GoodsBean", "GoodsBean" + i));

        }
        return list;
    }


    private void initData() {
        MyLog.LogShitou("最后需要的Json串", info_list.toString());
        setFalseData();
        FirmOrderNet(info_list.toString());// 确认订单list网络请求
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

    // 再次获取焦点方法
    @Override
    protected void onResume() {
        super.onResume();


    }

    /**
     * 设置假数据
     */
    private void setFalseData() {
        if (groupSet != null) {
            FirmOrderExpandableAdapter expandableAdapter = new FirmOrderExpandableAdapter(this, mBitmap, groupSet);
            mListView.setAdapter(expandableAdapter);
            expandableAdapter.notifyDataSetChanged();
        }
        //让子控件全部展开
//        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
//            mListView.expandGroup(i);
//        }
//        //去掉自带按钮
//        mListView.setGroupIndicator(null);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FirmOrder_distribution://配送方式
                mDistributionPopupWindow = new SelectDistributionPopupWindow(this, express_comp, express_fee);
                mDistributionPopupWindow.SetStringText(this);
                mDistributionPopupWindow.showAtLocation(this.findViewById(FirmOrder), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.FirmOrder_pay://支付方式
                mPayPopupWindow = new SelectPayPopupWindow(this, mPayWindowListener);
                //设置layout在PopupWindow中显示的位置,因为这里写了全屏的所以就没有居中
                mPayPopupWindow.showAtLocation(this.findViewById(FirmOrder), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.FirmOrder_NoAddress://没有收获地址
                openActivityWitchAnimation(ManagerAddressActivity.class);
                break;
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.firmOrder_ok_pay://确认并付款
                mExCompName = mDistributionTv.getText().toString().trim();
                payNmae = mPayNmme.getText().toString().trim();
                OrderPayNet(); // 订单支付网络请求
                break;
            case R.id.FirmOrder_Address: // 选择收货地址
                Intent intent = new Intent();
                intent.setClass(this, ChooseReceiverAddress.class);
                startActivityForResult(intent, REQUESTCODE);//REQUESTCODE定义一个整型做为请求对象标识
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
    /*private View.OnClickListener mDistributionWindowListener = new View.OnClickListener() {
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
    };*/


    /**
     * 确认订单list网络请求
     *
     * @param Goods_info 商品json串
     */
    private void FirmOrderNet(final String Goods_info) {
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
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 订单支付网络请求
     */
    private void OrderPayNet() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ORDERPAY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 收货地址ID
                if (mAddressId != null) {
                    params.put(StaticField.ADDRESS_ID, mAddressId);// 收货地址ID
                } else {
                    params.put(StaticField.ADDRESS_ID, mNoAddressId);// 收货地址ID
                }
                if (mExCompName.equals("顺丰")) {
                    params.put(StaticField.EXPRESS_COMP, "sf");// 快递公司
                } else if (mExCompName.equals("中国邮政速递")) {
                    params.put(StaticField.EXPRESS_COMP, "ems");// 快递公司
                }
                params.put(StaticField.REQUESTID, mTimeId);// 客户订单请求号
                params.put(StaticField.PAYAMOUNT, "0.01");// 支付金额

                if (payNmae.equals("微信")) {
                    params.put(StaticField.PAYTYPE, StaticField.WXPAY);// 支付方式
                    params.put(StaticField.GOODESINFO, info_list.toString());//  商品信息：所有商品的json字符串

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
                } else if (payNmae.equals("支付宝")) {
                    params.put(StaticField.PAYTYPE, StaticField.ALIPAY);// 支付方式
                    params.put(StaticField.GOODESINFO, info_list.toString());//  商品信息：所有商品的json字符串
                    MyLog.LogShitou("生成订单参数", mToken + "--" + mUser_id + "--" + mAddressId + "--" + mNoAddressId + "--" + mTimeId + "--" + info_list.toString());
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

    private Handler mHandler = new Handler() {

        private String mPayUrl;
        private IWXAPI api;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case StaticField.SUCCESS:// 确认订单list
                    Gson gsones = new Gson();
                    FirmOrderBean mFirmOrderBean = gsones.fromJson((String) msg.obj, FirmOrderBean.class);
                    String mCode = mFirmOrderBean.getRsp_code();
                    String mMsges = mFirmOrderBean.getRsp_msg();
                    if (mCode.equals("0000")) {
                        String mFeeRates = mFirmOrderBean.getService_fee_rate();// 服务费率
                        mFeeRate.setText("￥" + mFeeRates);
                        // 快递公司
                        express_comp = mFirmOrderBean.getExpress_comp();
                        mDistributionTv.setText(express_comp.get(0).getValue());
                        // 快递费价格
                        express_fee = mFirmOrderBean.getExpress_fee();
                        mDistributionPrice.setText("￥" + express_fee.get(0).getValue());

                        // 地址列表
                        address_list = mFirmOrderBean.getAddress_list();
                        if (address_list != null && address_list.size() != 0) {
                            mAddress.setVisibility(View.VISIBLE);// 显示收货地址布局
                            mNoAddress.setVisibility(View.GONE); // 隐藏无收货地址布局
                            String mName = address_list.get(0).getName();
                            mAddressName.setText(mName);
                            String mMobile = address_list.get(0).getMobile();
                            mAddressMobile.setText(mMobile);
                            String mDetail = address_list.get(0).getDetail();
                            mAddressDetail.setText(mDetail);
                            mNoAddressId = address_list.get(0).getAddress_id();
                        } else {
                            mNoAddress.setVisibility(View.VISIBLE); // 显示无收货地址布局
                            mAddress.setVisibility(View.GONE);// 隐藏收货地址布局
                        }
                    } else {
                        MyToast.showShort(FirmOrderActivity.this, "结算商品信息异常");
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
                            api = WXAPIFactory.createWXAPI(FirmOrderActivity.this, StaticField.APP_ID);
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
                        MyToast.showShort(FirmOrderActivity.this, mMsg);
                    }

                    break;
                case StaticField.ALI_SUCCESS: //支付宝订单支付
                    Gson gsons = new Gson();
                    OrderPayBean mOrderPayBeans = gsons.fromJson((String) msg.obj, OrderPayBean.class);
                    String mCodess = mOrderPayBeans.getRsp_code();
                    String mMsgs = mOrderPayBeans.getRsp_msg();
                    if (mCodess.equals("0000")) {
                        mPayUrl = mOrderPayBeans.getPay_url();  // 支付请求地址
//                        String mRequestId = mOrderPayBeans.getRequest_id();// 交易订单号
//                        String mPayRequestId = mOrderPayBeans.getPay_request_id();// 平台支付请求号
                        MyLog.LogShitou("mPayUrl+支付宝请求需要的串", mPayUrl);
                        // 发起支付宝支付
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(FirmOrderActivity.this);
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
                        MyToast.showShort(FirmOrderActivity.this, mMsgs);
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
                        Toast.makeText(FirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(FirmOrderActivity.this, "正在支付中...",
                                    Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            Toast.makeText(FirmOrderActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(FirmOrderActivity.this, "你已取消支付",
                                    Toast.LENGTH_LONG).show();
                        } else if (TextUtils.equals(resultStatus, "6002")) {
                            Toast.makeText(FirmOrderActivity.this, "网络连接失败",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FirmOrderActivity.this, "支付失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 获取选中的配送方式
     *
     * @param name
     * @param valuel
     */
    @Override
    public void GetStringText(String name, String valuel) {
        mDistributionTv.setText(name);// 配送名字
        mDistributionPrice.setText(valuel); // 配送价格

        MyLog.LogShitou("获取的配送，价格", name + "--" + valuel);
    }

    /**
     * 获取选中地址栏的信息
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String mName = data.getStringExtra("name");
                    String mMobile = data.getStringExtra("mobile");
                    String mDetail = data.getStringExtra("detail");
                    mAddressId = data.getStringExtra("addressId");
                    mAddressName.setText(mName);
                    mAddressMobile.setText(mMobile);
                    mAddressDetail.setText(mDetail);
                    MyLog.LogShitou("回调回来的地址详细信息", mName + "--" + mMobile + "--" + mDetail + "--" + mAddressId);
                }
                break;
            default:
                break;
        }


    }

}
