package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.Expandable1Adapter;
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
import cn.com.chinau.view.CustomExpandableListView;
import cn.com.chinau.view.VerticalScrollView;

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
//        private ListView mListView;//底部列表展示
    private CustomExpandableListView mListView;//底部列表展示
    private ImageView mBack, mPayImg;
    private TextView mTitle, mOkPay, mDistributionTv, mDistributionPrice, mPayNmme, mFeeRate, mFee, mAddressName,
            mAddressMobile, mAddressDetail, mNoAddressAdd, mTotalPrice, mGoodsCount;

    private LinearLayout mAddress1;
    private String mFirmOrder;
    //    private String mShopGoodsJson;// 传过来的Json串
    private SharedPreferences sp;
    private String mToken, mUser_id;

    private ArrayList<FirmOrderBean.AddressList> address_list;
    private ArrayList<FirmOrderBean.ExpreeFee> express_fee;
    private ArrayList<FirmOrderBean.ExpreeComp> express_comp;
    private VerticalScrollView mFirmOrder_SV;

    private static final int REQUESTCODE = 1111;
    private String mNoAddressId;// 没有去选择地址获取的地址id
    private String mAddressId;// 去选择地址回调回来的地址id
    private String mTimeId; // 客户订单请求号
    private String mExCompName;
    private String payNmae;
    private IWXAPI api;
    private String address_id;
    private String mGroupSet;
    private ArrayList<AddShopCartBean> info_list = new ArrayList<>();
    ;
    //    private HashMap<Integer, Set<ShopNameBean.GoodsBean>> groupSet;
    private Integer key;
    private String goods_sn;
    private String goods_count;

    private boolean isChild;
    private String mPrice, mCount, mSellerList;// 价钱，数量
    //    private String groupSet;
    private Hashtable<ShopNameBean.SellerBean, ArrayList<ShopNameBean.SellerBean.GoodsBean>> groupSet;
    private String groupSet1;
    private String mRequestId,mOrder_price;
    private String mPayUrl;
    private String mGoods_sn, mAuction_sn;
    private String mToJson;
    private ArrayList<Integer> keylsit;
    private String mShopNameBean;
    private List<ShopNameBean> listShopNameBean;
    private ShopNameBean shopNameBean;
    private ArrayList<ShopNameBean.SellerBean> seller_list;
    private ShopNameBean.SellerBean sellerBean;
    private Integer integer;
    private double mTotalPrices;
    private int mSellerNo;

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
        // 这三行是为了防止展示到ListView处
        mBack.setFocusable(true);
        mBack.setFocusableInTouchMode(true);
        mBack.requestFocus();

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
        mListView = (CustomExpandableListView) mFirmOrderContent.findViewById(R.id.firmOrder_expandableLV);
        mOkPay = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_ok_pay);
        mDistributionTv = (TextView) mFirmOrderContent.findViewById(R.id.distribution_tv);
        mDistributionPrice = (TextView) mFirmOrderContent.findViewById(R.id.distribution_price_tv);
        mPayImg = (ImageView) mFirmOrderContent.findViewById(R.id.firmorder_pay_img);
        mPayNmme = (TextView) mFirmOrderContent.findViewById(R.id.firmorder_pay_name);
        mFeeRate = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_fee_rate);// 服务费率
        mFee = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_fee);// 服务费
        mTotalPrice = (TextView) mFirmOrderContent.findViewById(R.id.totl_price);// 价钱
//        mTotalPrice.setText("￥" + mPrice);// 赋值总价钱
        mGoodsCount = (TextView) mFirmOrderContent.findViewById(R.id.goods_count);// 数量
        mGoodsCount.setText("共" + mCount + "件商品");
    }

    // 获取传过来的数据
    private void GetStringData() {
        Intent intent = getIntent();
        mFirmOrder = intent.getStringExtra("FirmOrder");// 获取是哪个页面来的适配器标识，
        mCount = intent.getStringExtra("Count");// 获取传过来的数量
        mPrice = intent.getStringExtra("Price");// 获取传过来的总价钱

        MyLog.LogShitou("===========适配器传过来的数据", mCount+"=="+mPrice);

        if (mFirmOrder.equals("AuctionRecordAdapter")) {
            mGoods_sn = intent.getStringExtra("Goodes_sn");// 获取传过来的商品编号
            mAuction_sn = intent.getStringExtra("Auction_sn");// 获取传过来的竞拍编号
            // 添加数据到AddShopCartBean生成Json
            mToJson = AddShopCartBean(mGoods_sn, mCount);
            MyLog.LogShitou("===========竞拍记录适配器组装Json串", mToJson);
        } else {
            shopNameBean = (ShopNameBean)getIntent().getSerializableExtra("shopNameBean_data");

            groupSet = MyApplication.getGroupSet();
            mSellerNo =  groupSet.size();// 获取卖家数(计算总价用)
            MyLog.LogShitou("传过来的groupSet", groupSet.toString());


            ArrayList<HashMap<String,String>> jsonList = new ArrayList<>();
            if (groupSet != null) {
                for (Hashtable.Entry<ShopNameBean.SellerBean, ArrayList<ShopNameBean.SellerBean.GoodsBean>> entry : groupSet.entrySet()) {

                    ArrayList<ShopNameBean.SellerBean.GoodsBean> value = entry.getValue(); // 拿到循环后的value值

                    for (ShopNameBean.SellerBean.GoodsBean bean:
                    value) {
                        String a = bean.getGoods_sn();
                        String b = bean.getGoods_count();
                        HashMap<String,String> map = new HashMap<>();
                        map.put("goods_sn",a);
                        map.put("goods_count",b);
                        jsonList.add(map);
                    }


                }
                mToJson = new Gson().toJson(jsonList);
                MyLog.LogShitou("购物车适配器数据转换成的Json", mToJson);
            }
        }
    }



    public List<AddShopCartBean> TabtoString(Hashtable<ShopNameBean.SellerBean, Set<ShopNameBean.SellerBean.GoodsBean>> tab) {

        List<AddShopCartBean> list = new ArrayList<>();

        Enumeration<Set<ShopNameBean.SellerBean.GoodsBean>> elements = tab.elements();
        while (elements.hasMoreElements()) {
            Set<ShopNameBean.SellerBean.GoodsBean> goodsBeen = elements.nextElement();
//            for (int i = 0; i < goodsBeen.size(); i++) {
            Iterator<ShopNameBean.SellerBean.GoodsBean> it = goodsBeen.iterator();
            while (it.hasNext()) {
                ShopNameBean.SellerBean.GoodsBean next = it.next();
                String goods_count = next.getGoods_count();
                String goods_sn = next.getGoods_sn();
                AddShopCartBean bean = new AddShopCartBean(goods_sn, goods_count);
                list.add(bean);
//                }
            }
        }
        return list;
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
        MyLog.LogShitou("最后需要的Json串", mToJson);
        setFalseData();
        FirmOrderNet(mToJson);// 确认订单list网络请求
    }

    private void initListener() {
        mPay.setOnClickListener(this);
        mDistribution.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mOkPay.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mNoAddress.setOnClickListener(this);
        // 点击不可回缩
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    @Override
    public void AgainRequest() {

    }

    // 再次获取焦点方法
    @Override
    protected void onResume() {
        super.onResume();
//        FirmOrderNet(mToJson);// 确认订单list网络请求
        MyLog.LogShitou("==========onResume","-=================onResume");
    }

    /**
     * 设置假数据
     */
    private void setFalseData() {

        if (groupSet != null) {
//            FirmOrderExpandableAdapter expandableAdapter = new FirmOrderExpandableAdapter(this, mBitmap, groupSet);

            Expandable1Adapter expandableAdapter = new Expandable1Adapter(this, mBitmap, groupSet,shopNameBean);
            mListView.setAdapter(expandableAdapter);
            expandableAdapter.notifyDataSetChanged();

            //让子控件全部展开
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
        //去掉自带按钮
        mListView.setGroupIndicator(null);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FirmOrder_distribution://配送方式
                mDistributionPopupWindow = new SelectDistributionPopupWindow(this, express_comp, express_fee);
                mDistributionPopupWindow.SetStringText(this);
                mDistributionPopupWindow.showAtLocation(this.findViewById(R.id.FirmOrder), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.FirmOrder_pay://支付方式
                mPayPopupWindow = new SelectPayPopupWindow(this, mPayWindowListener);
                //设置layout在PopupWindow中显示的位置,因为这里写了全屏的所以就没有居中
                mPayPopupWindow.showAtLocation(this.findViewById(R.id.FirmOrder), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.FirmOrder_NoAddress://没有收获地址
                Intent intent1 = new Intent(this, EditReceiptAddressActivity.class);
                intent1.putExtra("Address", "FirOrder");

                startActivityForResult(intent1,3333);
                //跳转动画
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

//                openActivityWitchAnimation(EditReceiptAddressActivity.class);
                break;
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.firmOrder_ok_pay://确认并付款

                if(mNoAddress.getVisibility() == View.VISIBLE && mAddress.getVisibility() == View.GONE){
                    Toast.makeText(getApplicationContext(),"请选择收货地址",Toast.LENGTH_LONG).show();
                    return;
                }

                mExCompName = mDistributionTv.getText().toString().trim();
                payNmae = mPayNmme.getText().toString().trim();
                MyLog.LogShitou("===========地址id", mExCompName + "==" + payNmae);

                if (mAddressId != null | mNoAddressId != null) {
                    OrderPayNet(); // 订单支付网络请求
                } else {
                    MyToast.showShort(FirmOrderActivity.this, "订单信息异常");
                }
                break;
            case R.id.FirmOrder_Address: // 选择收货地址
                Intent intent = new Intent();
                intent.setClass(this, ChooseReceiverAddress.class);
                startActivityForResult(intent, REQUESTCODE);//REQUESTCODE定义一个整型做为请求对象标识
                //跳转动画
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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

                if (mFirmOrder.equals("AuctionRecordAdapter")) {
                    MyLog.LogShitou("============竞拍编号", "===============" + mAuction_sn);
                    params.put(StaticField.AUCTIONSN, mAuction_sn);//  竞拍编号
                }
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou(Goods_info+"/==/"+"result结算列表", result);

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
                params.put(StaticField.PAYAMOUNT, String.valueOf(mTotalPrices));// 支付金额

                if (mFirmOrder.equals("AuctionRecordAdapter")) {
                    MyLog.LogShitou("============竞拍编号", "===============" + mAuction_sn);
                    params.put(StaticField.AUCTIONSN, mAuction_sn);//  竞拍编号
                }

                if (payNmae.equals("微信")) {
                    params.put(StaticField.PAYTYPE, StaticField.WXPAY);// 支付方式
                    params.put(StaticField.GOODESINFO, mToJson);//  商品信息：所有商品的json字符串

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
                    params.put(StaticField.GOODESINFO, mToJson);//  商品信息：所有商品的json字符串

                    MyLog.LogShitou("生成订单参数", mToken + "--" + mUser_id + "--" + mAddressId + "--" + mNoAddressId + "--" + mTimeId + "--" + mToJson);

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

        private double douPrice;

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

                        mFeeRate.setText("服务费(" + mFeeRates + ")");
                        String servicePrice = mFirmOrderBean.getService_fee();
                        mFee.setText("￥" + servicePrice);

                        // 快递公司
                        express_comp = mFirmOrderBean.getExpress_comp();
                        mDistributionTv.setText(express_comp.get(0).getValue());
                        // 快递费价格
                        express_fee = mFirmOrderBean.getExpress_fee();
                        mDistributionPrice.setText("￥" + express_fee.get(0).getValue());

                        String str = express_fee.get(0).getValue().replaceAll(",","").trim();// 快递费去掉首尾空格
                        double countPrice =Double.parseDouble(str); // 快递费转double

                        if (mFirmOrder.equals("AuctionRecordAdapter")) {
                            int count =Integer.valueOf(mCount).intValue(); //数量转成int
                            MyLog.LogShitou("价钱转double商品数量",countPrice+"===="+count);
                            double price = countPrice * count;//快递总价钱
                            MyLog.LogShitou("======快递费总价",price+"");

                            DecimalFormat df   = new DecimalFormat("######0.00");// 保留2位小数
                            String mprice = df.format(price);// 最后的快递费
                             douPrice =Double.parseDouble(mprice); // 最后快递费转double
                            MyLog.LogShitou("======double快递费总价",price+"");
                        }else{
                            int count =Integer.valueOf(mSellerNo).intValue(); //数量转成int
//                            String str = express_fee.get(0).getValue().replaceAll(",","").trim();// 快递费去掉首尾空格
//                            double countPrice =Double.parseDouble(str); // 快递费转double
                            double price = countPrice * count;//快递总价钱
                            MyLog.LogShitou("======快递费总价",price+"");

                            DecimalFormat df   = new DecimalFormat("######0.00");// 保留2位小数
                            String mprice = df.format(price);// 最后的快递费
                             douPrice =Double.parseDouble(mprice); // 最后快递费转double
                            MyLog.LogShitou("======double快递费总价",price+"");
                        }

                        String serviceFee = servicePrice.replaceAll(",","").trim();// 服务费去掉首尾空格
                        double mServicePrice =Double.parseDouble(serviceFee); //最后服务费转double价钱
                        double mtotalPrice =Double.parseDouble(mPrice.replaceAll(",","").trim()); //最后商品的价钱转double()

                        double price1 = add(douPrice,mServicePrice); // 快递费和服务费总价
                        mTotalPrices =  add(price1,mtotalPrice); // 三个数相加的总费用

                        mTotalPrice.setText("￥" + mTotalPrices);// 赋值总价钱
                        MyLog.LogShitou("==============总价",mTotalPrices+"");

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
                        MyToast.showShort(FirmOrderActivity.this, mMsges);
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

//                            MyLog.LogShitou("微信支付需要的值", sign + "--" + timestamp + "--" + noncestr + "--" + partnerid + "--" + prepayid + "--" + appid);
                            // app注册微信
                            api = WXAPIFactory.createWXAPI(FirmOrderActivity.this, StaticField.APP_ID);
                            api.registerApp(StaticField.APP_ID);
                            if (api != null) {
                                // 微信支付请求
                                PayReq req = new PayReq();
                                req.appId = appid; // 应用ID
                                MyLog.LogShitou("微信支付需要的值appid",appid);
                                req.partnerId = partnerid; // 商户号
                                MyLog.LogShitou("微信支付需要的值partnerid",partnerid);
                                req.prepayId = prepayid; // 预支付交易会话ID
                                MyLog.LogShitou("微信支付需要的值prepayid",prepayid);
                                req.packageValue = "Sign=WXPay"; // 暂填写固定值Sign=WXPay
                                req.nonceStr = noncestr; // 随机字符串
                                MyLog.LogShitou("微信支付需要的值noncestr",noncestr);
                                req.timeStamp = timestamp; // 时间戳
                                MyLog.LogShitou("微信支付需要的值timestamp",timestamp);

                                req.sign = sign; // 签名
                                MyLog.LogShitou("微信支付需要的值sign",sign);
//                                req.extData = "app data";
                                api.sendReq(req);

                                MyLog.LogShitou("微信支付需要的值", sign + "--" + timestamp + "--" + noncestr + "--" + partnerid + "--" + prepayid + "--" + appid);


//                                MyLog.LogShitou("这值是啥00req", api.sendReq(req) + "");
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
                        mRequestId = mOrderPayBeans.getRequest_id();// 交易订单号
                        mOrder_price = mOrderPayBeans.getOrder_amount();// 订单金额
//                        String mPayRequestId = mOrderPayBeans.getPay_request_id();// 平台支付请求号
//                        MyLog.LogShitou("mPayUrl+支付宝请求需要的串", mPayUrl);
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
//                    payResult.getMemo()
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(FirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString(StaticField.ORDERS, "receiving");
                        openActivityWitchAnimation(OrderBuySuccessActivity.class, bundle); // 跳转至成功页面
                        finish();

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

        int count =Integer.valueOf(mSellerNo).intValue(); //数量转成int
        String str = valuel.replaceAll("￥","").trim();// 快递费去掉首尾空格
        double countPrice =Double.parseDouble(str); // 快递费转double

        String serviceFee = mFee.getText().toString().trim().replaceAll("￥","");// 服务费去掉首尾空格

        MyLog.LogShitou("价钱转double商品数量",countPrice+"===="+count);

        double price = countPrice * count;//快递总价钱
        MyLog.LogShitou("======快递费总价",price+"");
        DecimalFormat df   = new DecimalFormat("######0.00");// 保留2位小数
        String mprice = df.format(price);// 最后的快递费
        double douPrice =Double.parseDouble(mprice); // 最后快递费转double
        MyLog.LogShitou("======double快递费总价",price+"");

        double mServicePrice =Double.parseDouble(serviceFee); //最后服务费转double价钱
        double mtotalPrice =Double.parseDouble(mPrice.trim()); //最后商品的价钱转double

        double price1 = add(douPrice,mServicePrice); // 快递费和服务费总价
        // 三个数相加的总费用
        mTotalPrices = add(price1,mtotalPrice);

        mTotalPrice.setText("￥" + mTotalPrices);// 赋值总价钱
        MyLog.LogShitou("==============总价", mTotalPrices +"");




    }

    /**
     * 获取选中地址栏的信息
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 3333:
                if (resultCode == 33){
                    FirmOrderNet(mToJson);// 确认订单list网络请求
                }

                break;
            case REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    String mName = data.getStringExtra("name");
                    String mMobile = data.getStringExtra("mobile");
                    String mDetail = data.getStringExtra("detail");
                    mAddressId = data.getStringExtra("addressId");
                    mAddressName.setText(mName);
                    mAddressMobile.setText(mMobile);
                    mAddressDetail.setText(mDetail);
                    MyLog.LogShitou("回调回来的地址详细信息", mName + "--" + mMobile + "--" + mDetail + "--" + mAddressId);
                }else if(resultCode == 22){
                    FirmOrderNet(mToJson);// 确认订单list网络请求
                }
                break;
            default:
                break;
        }
    }


    /**
     * @param goodsSn    商品编号
     * @param goodsCount 商品数量
     * @return Json串
     * <p>
     * 添加数据到AddShopCartBean生成Json
     */
    private String AddShopCartBean(String goodsSn, String goodsCount) {
        if (goodsSn != null && !goodsSn.equals("") && goodsCount != null && !goodsCount.equals("")) {
            AddShopCartBean mAddShopCartBean = new AddShopCartBean();
            mAddShopCartBean.setGoods_sn(goodsSn);
            mAddShopCartBean.setGoods_count(goodsCount);
            info_list.add(mAddShopCartBean); // 添加到list
        }
        return info_list.toString();
    }

    /** * 提供精确的加法运算
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

}
