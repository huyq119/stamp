package cn.com.chinau.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.OrderAllLsitViewAdapter;
import cn.com.chinau.adapter.OrderGoodsPagerAdapter;
import cn.com.chinau.adapter.OrderPaymentReceivingCompleteLsitViewAdapter;
import cn.com.chinau.adapter.OrderRefuseLsitViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.OrderAllListViewGoodsBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.OrderGoodsViewPager;

/**
 * Created by Administrator on 2016/8/29.
 * 商品订单页面
 */
public class OrdersGoodsActivity extends BaseActivity {

    private View mOrderTitle, mOrderContent;
    private TextView mTitle;
    private ImageView mBack;
    private String tag;
    private OrderGoodsViewPager mViewPager;
    private boolean canscroll = false;
    private RadioGroup mRadioGroup;
    private RadioButton mAllBtn, mPaymentBtn, mReceiving, mComplete, mRefuse;// 全部, 待付款, 待收货, 已完成, 退款/退货
    private View view1, view2, view3, view4, view5;
    private ExpandableListView all_edListview, payment_edListview, receiving_edListview, complete_edListview, refuse_edListview;
    private int position = 0;
    private int mPosition;
    private String mToken,mUser_id;// 标识，用户ID
    private static final int num = 0; // 初始化索引
    private OrderAllLsitViewAdapter allLsitViewAdapter;
    private List<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList> groups = new ArrayList<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList>();// 组元素数据列表
    private Map<String, List<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList.OrderDetailList>> goods = new HashMap<String, List<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList.OrderDetailList>>();// 子元素数据列表

    private List<HashMap<String, String>> lists;
    private SharedPreferences sp;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 我的邮集
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
//                    MyStampGridViewBean mOrderSweepBean = gson.fromJson(msge, MyStampGridViewBean.class);
//                    String mRsp_code = mOrderSweepBean.getRsp_code();
//                    if (mRsp_code.equals("0000")) {
//                        mList = mOrderSweepBean.getStamp_list();
//                        MyLog.LogShitou("我的邮集有几条-->:", mList.size() + "");
//                        if (mList != null && mList.size() != 0) {
//                            initAdapter();
//                        }else {
//                            MyToast.showShort(MyStampActivity.this,"我的邮集为空。。。");
//                        }
//                    }

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
        mOrderContent = View.inflate(this, R.layout.activity_order_goods, null);
        sp = getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initViews();
        initDatas();
        initAdapter();
        initstartview();
        initListener();
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    canscroll = true;
                } else {
                    canscroll = false;
                }
                return false;
            }
        });
        all_edListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    canscroll = false;
                }
                return canscroll;
            }
        });
        return mOrderContent;
    }


    private void initstartview() {
        if (tag.equals("all")) {
            mRadioGroup.check(R.id.RBtn_All);
            mViewPager.setCurrentItem(0);
        } else if (tag.equals("payment")) {
            mRadioGroup.check(R.id.RBtn_Payment);
            mViewPager.setCurrentItem(1);
        } else if (tag.equals("receiving")) {
            mRadioGroup.check(R.id.RBtn_Receiving);
            mViewPager.setCurrentItem(2);
        } else if (tag.equals("completed")) {
            mRadioGroup.check(R.id.RBtn_Complete);
            mViewPager.setCurrentItem(3);
        } else if (tag.equals("refused")) {
            mRadioGroup.check(R.id.RBtn_Refuse);
            mViewPager.setCurrentItem(4);
        }
    }

    private void initViews() {

        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        // 获取我的页面传过来的键值
        Bundle bundle = getIntent().getExtras();
        tag = bundle.getString(StaticField.ORDERS);
        mBack = (ImageView) mOrderTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mOrderTitle.findViewById(R.id.base_title);
        mTitle.setText("商品订单");

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });

        //初始化控件
        mRadioGroup = (RadioGroup) mOrderContent.findViewById(R.id.RG_choose);
        mViewPager = (OrderGoodsViewPager) mOrderContent.findViewById(R.id.order_viewpager);
        mAllBtn = (RadioButton) mOrderContent.findViewById(R.id.RBtn_All);
        mPaymentBtn = (RadioButton) mOrderContent.findViewById(R.id.RBtn_Payment);
        mReceiving = (RadioButton) mOrderContent.findViewById(R.id.RBtn_Receiving);
        mComplete = (RadioButton) mOrderContent.findViewById(R.id.RBtn_Complete);
        mRefuse = (RadioButton) mOrderContent.findViewById(R.id.RBtn_Refuse);


    }

    private void initAdapter() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View v1 = inflater.inflate(R.layout.view_ordersgoods_all_edlistview, null);
        View v2 = inflater.inflate(R.layout.view_ordersgoods_all_edlistview, null);
        View v3 = inflater.inflate(R.layout.view_ordersgoods_all_edlistview, null);
        View v4 = inflater.inflate(R.layout.view_ordersgoods_all_edlistview, null);
        View v5 = inflater.inflate(R.layout.view_ordersgoods_all_edlistview, null);
        List<View> list = new ArrayList<View>();
        list.add(v1);
        list.add(v2);
        list.add(v3);
        list.add(v4);
        list.add(v5);
        OrderGoodsPagerAdapter adapter = new OrderGoodsPagerAdapter(list);
        mViewPager.setAdapter(adapter);

        all_edListview = (ExpandableListView) v1.findViewById(R.id.all_expand_ListView);
        payment_edListview = (ExpandableListView) v2.findViewById(R.id.all_expand_ListView);
        receiving_edListview = (ExpandableListView) v3.findViewById(R.id.all_expand_ListView);
        complete_edListview = (ExpandableListView) v4.findViewById(R.id.all_expand_ListView);
        refuse_edListview = (ExpandableListView) v5.findViewById(R.id.all_expand_ListView);

        // (全部)适配器
        OrderAllLsitViewAdapter allLsitViewAdapter = new OrderAllLsitViewAdapter(this, mBitmap, groups, goods);// 全部适配器
        // （待付款，待收货，已完成）适配器
        OrderPaymentReceivingCompleteLsitViewAdapter PayRecComLsitViewAdapter = new OrderPaymentReceivingCompleteLsitViewAdapter(this, mBitmap, groups, goods);
        // （退款/退货）适配器
        OrderRefuseLsitViewAdapter RefuseLsitViewAdapter = new OrderRefuseLsitViewAdapter(this, mBitmap, groups, goods);// 退货/ 退款适配器

        all_edListview.setAdapter(allLsitViewAdapter);
        payment_edListview.setAdapter(PayRecComLsitViewAdapter);
        receiving_edListview.setAdapter(PayRecComLsitViewAdapter);
        complete_edListview.setAdapter(PayRecComLsitViewAdapter);
        refuse_edListview.setAdapter(RefuseLsitViewAdapter);
        for (int i = 0; i < allLsitViewAdapter.getGroupCount(); i++) {
            all_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
            payment_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
            receiving_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
            complete_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
            refuse_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
        }
        //ed_listview点击不收缩
        all_edListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        //ed_listview点击不收缩
        payment_edListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        //ed_listview点击不收缩
        receiving_edListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        //ed_listview点击不收缩
        complete_edListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        //ed_listview点击不收缩
        refuse_edListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

    }

    private void initDatas() {
        groups = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            groups.add(new OrderAllListViewGoodsBean.OrdersDataList.SellerDataList());
            List<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList.OrderDetailList> products = new ArrayList<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList.OrderDetailList>();
            for (int j = 0; j < 2; j++) {
                products.add(new OrderAllListViewGoodsBean.OrdersDataList.SellerDataList.OrderDetailList("http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg", "庚申年", "100000.00", "1", "交易关闭"));
            }
            goods.put(groups.get(i).getSeller_name(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key

        }

        GetInitNet(num,StaticField.QB); // 商品订单列表网络请求

    }


    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new MyGroupListener());
        mViewPager.setOnPageChangeListener(new MyPagerChangeListener());
        //全部
        all_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });
        // 待付款
        payment_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });
        // 待收货
        receiving_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });
        // 已完成
        complete_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });
        //退款/退货
        refuse_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });

    }

    @Override
    public void AgainRequest() {

    }

    /**
     * 滑动ViewPager监听事件
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            switch (position) {
                case 0:
                    mRadioGroup.check(R.id.RBtn_All);

                    break;
                case 1:
                    mRadioGroup.check(R.id.RBtn_Payment);
                    break;
                case 2:
                    mRadioGroup.check(R.id.RBtn_Receiving);
                    break;
                case 3:
                    mRadioGroup.check(R.id.RBtn_Complete);

                    break;
                case 4:
                    mRadioGroup.check(R.id.RBtn_Refuse);
                    break;
                default:
                    break;
            }

        }

        ;
    }

    /**
     * 点击RadioButton监听事件
     */
    public class MyGroupListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.RBtn_All:// 全部
                    position = 0;
                    mViewPager.setCurrentItem(0);
                    GetInitNet(num,StaticField.QB); // 商品订单列表网络请求
                    break;
                case R.id.RBtn_Payment:// 待付款
                    position = 1;
                    mViewPager.setCurrentItem(1);
                    GetInitNet(num,StaticField.DFK);
                    break;
                case R.id.RBtn_Receiving:// 待收货
                    position = 2;
                    mViewPager.setCurrentItem(2);
                    GetInitNet(num,StaticField.DSH);
                    break;
                case R.id.RBtn_Complete:// 已完成
                    position = 3;
                    mViewPager.setCurrentItem(3);
                    GetInitNet(num,StaticField.WC);
                    break;
                case R.id.RBtn_Refuse://退款
                    position = 4;
                    mViewPager.setCurrentItem(4);
                    GetInitNet(num,StaticField.TK);
                    break;

                default:
                    break;
            }

        }

    }

    /**
     * 商品订单列表网络请求
     * @param num 索引
     * @param orderStatus 订单状态
     */
    private void GetInitNet(final int num,final String orderStatus){
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSORDERLIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id); // 用户ID
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 索引
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                // 订单状态：QB：全部；DFK：待付款；DSH：待收货；WC：已完成；TK：退款/退货
                params.put(StaticField.ORDERSTATUS, orderStatus);

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("商品订单list-->:",result);
                if (result.equals("-1")|result.equals("-2")) {
                    return;
                }

//                Message msgSuccess = handler.obtainMessage();
//                msgSuccess.what = StaticField.SUCCESS;
//                msgSuccess.obj = result;
//                handler.sendMessage(msgSuccess);


            }
        });
    }


}
