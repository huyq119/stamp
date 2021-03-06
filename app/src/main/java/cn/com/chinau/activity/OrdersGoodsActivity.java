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
import cn.com.chinau.adapter.OrderRefuseLsitViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.OrderAllListViewGoodsBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.OrderGoodsViewPager;

import static cn.com.chinau.StaticField.QB;

/**
 * Created by Administrator on 2016/8/29.
 * 商品订单页面
 */
public class OrdersGoodsActivity extends BaseActivity {

    private View mOrderTitle, mOrderContent;
    private TextView mTitle, mOederTv1, mOederTv2, mOederTv3, mOederTv4, mOederTv5;
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
    private String mToken, mUser_id;// 标识，用户ID
    private static final int num = 0; // 初始化索引
    private OrderAllLsitViewAdapter allLsitViewAdapter;
    private List<OrderAllListViewGoodsBean.Seller_list> groups = new ArrayList<OrderAllListViewGoodsBean.Seller_list>();// 组元素数据列表
    private Map<String, List<OrderAllListViewGoodsBean.Order_detail_list>> goods = new HashMap<String, List<OrderAllListViewGoodsBean.Order_detail_list>>();// 子元素数据列表
    private List<HashMap<String, String>> lists;
    private SharedPreferences sp;
    private ArrayList<OrderAllListViewGoodsBean.Order_list> order_list, order_list1;
    private OrderAllLsitViewAdapter allLsitViewAdapter1;
    private OrderRefuseLsitViewAdapter refuseLsitViewAdapter;
    private OrderAllListViewGoodsBean.Order_detail_list order_detail_list;
    private String mOrder_sn;

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
        initAdapter();
        initstartview();
        return mOrderContent;
    }

    private void initstartview() {
        if (tag.equals("all")) {
            mRadioGroup.check(R.id.RBtn_All);
            mViewPager.setCurrentItem(0);
            GetInitNet(num, QB); // 商品订单列表网络请求
        } else if (tag.equals("payment")) {
            mRadioGroup.check(R.id.RBtn_Payment);
            mViewPager.setCurrentItem(1);
            GetInitNet(num, StaticField.DFK);
        } else if (tag.equals("receiving")) {
            mRadioGroup.check(R.id.RBtn_Receiving);
            mViewPager.setCurrentItem(2);
            GetInitNet(num, StaticField.DSH);
        } else if (tag.equals("completed")) {
            mRadioGroup.check(R.id.RBtn_Complete);
            mViewPager.setCurrentItem(3);
            GetInitNet(num, StaticField.WC);
        } else if (tag.equals("refused")) {
            mRadioGroup.check(R.id.RBtn_Refuse);
            mViewPager.setCurrentItem(4);
            GetInitNet(num, StaticField.TK);
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
        //初始化控件
        mRadioGroup = (RadioGroup) mOrderContent.findViewById(R.id.RG_choose);
        mViewPager = (OrderGoodsViewPager) mOrderContent.findViewById(R.id.order_viewpager);
        mAllBtn = (RadioButton) mOrderContent.findViewById(R.id.RBtn_All);
        mPaymentBtn = (RadioButton) mOrderContent.findViewById(R.id.RBtn_Payment);
        mReceiving = (RadioButton) mOrderContent.findViewById(R.id.RBtn_Receiving);
        mComplete = (RadioButton) mOrderContent.findViewById(R.id.RBtn_Complete);
        mRefuse = (RadioButton) mOrderContent.findViewById(R.id.RBtn_Refuse);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });

    }

    /**
     * 创建出ViewPager所需的ExpandableListView
     */
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
        mOederTv1 = (TextView) v1.findViewById(R.id.no_order_tv);
        mOederTv2 = (TextView) v2.findViewById(R.id.no_order_tv);
        mOederTv3 = (TextView) v3.findViewById(R.id.no_order_tv);
        mOederTv4 = (TextView) v4.findViewById(R.id.no_order_tv);
        mOederTv5 = (TextView) v5.findViewById(R.id.no_order_tv);
    }

    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new MyGroupListener());
        mViewPager.setOnPageChangeListener(new MyPagerChangeListener());

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


        /**
         * 条目点击
         */
        // 全部
        all_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                order_detail_list = order_list.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);
                mOrder_sn = order_list.get(i).getOrder_sn();// 交易订单号
                GetOrderAllListViewGoodsBean(StaticField.QB);
                return false;
            }
        });

        // 待付款
        payment_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                order_detail_list = order_list.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);
                mOrder_sn = order_list.get(i).getOrder_sn();// 交易订单号
                GetOrderAllListViewGoodsBean(StaticField.DFK);
                finish();
                return false;
            }
        });
        // 待收货
        receiving_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                order_detail_list = order_list.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);
                mOrder_sn = order_list.get(i).getOrder_sn();// 交易订单号
                GetOrderAllListViewGoodsBean(StaticField.DSH);

                return false;
            }
        });
        // 已完成
        complete_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                order_detail_list = order_list.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);
                mOrder_sn = order_list.get(i).getOrder_sn();// 交易订单号
                GetOrderAllListViewGoodsBean(StaticField.WC);
                return false;
            }
        });
        //退款/退货
        refuse_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                OrderAllListViewGoodsBean.Order_detail_list order_detail_list = order_list1.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);
                String mOrder_sn = order_list1.get(i).getOrder_sn();// 交易订单号
                String mGoods_sn = order_detail_list.getGoods_sn();// 商品编号
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.ORDER_SN, mOrder_sn);
                bundle.putString(StaticField.GOODS_SN, mGoods_sn);
                bundle.putString(StaticField.ORDERSTATUS, "TK");
                MyLog.LogShitou("TK请求的订单号==编号", mOrder_sn + "=====" + mGoods_sn);
                openActivityWitchAnimation(LookOrderDetailRefuseActivity.class, bundle);
                return false;
            }
        });

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

    }

    /**
     * 订单列表跳转详情传的数据
     *
     * @param type 订单类型
     */
    private void GetOrderAllListViewGoodsBean(String type) {
        String mGoods_sn = order_detail_list.getGoods_sn();// 商品编号
        String mGoods_price = order_detail_list.getGoods_price();// 商品价格
        String mGoods_count = order_detail_list.getGoods_count();// 商品数量
        Bundle bundle = new Bundle();
        bundle.putString(StaticField.ORDER_SN, mOrder_sn);
        bundle.putString(StaticField.GOODS_SN, mGoods_sn);
        bundle.putString(StaticField.GOODS_PRICE, mGoods_price);
        bundle.putString(StaticField.GOODS_COUNT, mGoods_count);
        bundle.putString(StaticField.ORDERSTATUS, type);
        bundle.putString("OrderBuySuccess", "OrdersGoods");
        MyLog.LogShitou(type + "==" + "请求的订单号/编号", mOrder_sn + "=====" + mGoods_sn);
        openActivityWitchAnimation(OrderDetailsActivity.class, bundle);
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
                    GetInitNet(num, QB); // 商品订单列表网络请求
                    break;
                case R.id.RBtn_Payment:// 待付款
                    position = 1;
                    mViewPager.setCurrentItem(1);
                    GetInitNet(num, StaticField.DFK);
                    break;
                case R.id.RBtn_Receiving:// 待收货
                    position = 2;
                    mViewPager.setCurrentItem(2);
                    GetInitNet(num, StaticField.DSH);
                    break;
                case R.id.RBtn_Complete:// 已完成
                    position = 3;
                    mViewPager.setCurrentItem(3);
                    GetInitNet(num, StaticField.WC);
                    break;
                case R.id.RBtn_Refuse://退款
                    position = 4;
                    mViewPager.setCurrentItem(4);
                    GetInitNet(num, StaticField.TK);
                    break;

                default:
                    break;
            }

        }

    }

    /**
     * 商品订单列表网络请求
     *
     * @param num         索引
     * @param orderStatus 订单状态
     */
    private void GetInitNet(final int num, final String orderStatus) {
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
                MyLog.LogShitou(orderStatus + "--" + "商品订单list", result);

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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 全部
                    Gson gson0 = new Gson();
                    OrderAllListViewGoodsBean mOrderContentBean = gson0.fromJson((String) msg.obj, OrderAllListViewGoodsBean.class);
                    String code = mOrderContentBean.getRsp_code();
                    if (code.equals("0000")) {
                        order_list = mOrderContentBean.getOrder_list();
                        if (order_list != null && order_list.size() != 0) {
                            // (全部)适配器
                            all_edListview.setVisibility(View.VISIBLE);
                            allLsitViewAdapter = new OrderAllLsitViewAdapter(OrdersGoodsActivity.this, mBitmap, order_list);// 全部适配器
                            all_edListview.setAdapter(allLsitViewAdapter);
                            allLsitViewAdapter.notifyDataSetChanged();
                            for (int i = 0; i < allLsitViewAdapter.getGroupCount(); i++) {
                                all_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
                            }
                        } else {
                            all_edListview.setVisibility(View.GONE);
                            mOederTv1.setVisibility(View.VISIBLE); // 无信息控件显示
                            mOederTv1.setText("暂无订单信息~");
                        }
                        initListener();
                    }
                    break;
                case StaticField.DFK_SUCCESS:// 待付款
                    Gson gson1 = new Gson();
                    OrderAllListViewGoodsBean mOrderContentBean1 = gson1.fromJson((String) msg.obj, OrderAllListViewGoodsBean.class);
                    String code1 = mOrderContentBean1.getRsp_code();
                    if (code1.equals("0000")) {
                        order_list = mOrderContentBean1.getOrder_list();
                        if (order_list != null && order_list.size() != 0) {
                            // （待付款，待收货，已完成）适配器
                            payment_edListview.setVisibility(View.VISIBLE);
                            allLsitViewAdapter = new OrderAllLsitViewAdapter(OrdersGoodsActivity.this, mBitmap, order_list);// 全部适配器
                            payment_edListview.setAdapter(allLsitViewAdapter);
                            allLsitViewAdapter.notifyDataSetChanged();
                            for (int i = 0; i < allLsitViewAdapter.getGroupCount(); i++) {
                                payment_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
                            }
                        } else {
                            payment_edListview.setVisibility(View.GONE);
                            mOederTv2.setVisibility(View.VISIBLE); // 无信息控件显示
                            mOederTv2.setText("暂无订单信息~");
                        }
                        initListener();
                    }
                    break;
                case StaticField.DSH_SUCCESS:// 代收货
                    Gson gson2 = new Gson();
                    OrderAllListViewGoodsBean mOrderContentBean2 = gson2.fromJson((String) msg.obj, OrderAllListViewGoodsBean.class);
                    String code2 = mOrderContentBean2.getRsp_code();
                    if (code2.equals("0000")) {
                        order_list = mOrderContentBean2.getOrder_list();
                        if (order_list != null && order_list.size() != 0) {
                            // （待付款，待收货，已完成）适配器
                            receiving_edListview.setVisibility(View.VISIBLE);
                            allLsitViewAdapter = new OrderAllLsitViewAdapter(OrdersGoodsActivity.this, mBitmap, order_list);// 全部适配器
                            receiving_edListview.setAdapter(allLsitViewAdapter);
                            allLsitViewAdapter.notifyDataSetChanged();
                            for (int i = 0; i < allLsitViewAdapter.getGroupCount(); i++) {
                                receiving_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
                            }
                        } else {
                            receiving_edListview.setVisibility(View.GONE);
                            mOederTv3.setVisibility(View.VISIBLE); // 无信息控件显示
                            mOederTv3.setText("暂无订单信息~");
                        }
                        initListener();
                    }
                    break;
                case StaticField.WC_SUCCESS:// 已完成
                    Gson gson3 = new Gson();
                    OrderAllListViewGoodsBean mOrderContentBean3 = gson3.fromJson((String) msg.obj, OrderAllListViewGoodsBean.class);
                    String code3 = mOrderContentBean3.getRsp_code();
                    if (code3.equals("0000")) {
                        order_list = mOrderContentBean3.getOrder_list();
                        if (order_list != null && order_list.size() != 0) {
                            // （待付款，待收货，已完成）适配器
                            complete_edListview.setVisibility(View.VISIBLE);
                            allLsitViewAdapter = new OrderAllLsitViewAdapter(OrdersGoodsActivity.this, mBitmap, order_list);// 全部适配器
                            complete_edListview.setAdapter(allLsitViewAdapter);
                            allLsitViewAdapter.notifyDataSetChanged();
                            for (int i = 0; i < allLsitViewAdapter.getGroupCount(); i++) {
                                complete_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
                            }
                        } else {
                            complete_edListview.setVisibility(View.GONE);
                            mOederTv4.setVisibility(View.VISIBLE); // 无信息控件显示
                            mOederTv4.setText("暂无订单信息~");
                        }
                        initListener();
                    }
                    break;
                case StaticField.TK_SUCCESS:// 退款、退货
                    Gson gson4 = new Gson();
                    OrderAllListViewGoodsBean mOrderContentBean4 = gson4.fromJson((String) msg.obj, OrderAllListViewGoodsBean.class);
                    String code4 = mOrderContentBean4.getRsp_code();
                    if (code4.equals("0000")) {
                        order_list1 = mOrderContentBean4.getOrder_list();
                        if (order_list1 != null && order_list1.size() != 0) {
                            // 退货/ 退款适配器
                            refuse_edListview.setVisibility(View.VISIBLE);
                            refuseLsitViewAdapter = new OrderRefuseLsitViewAdapter(OrdersGoodsActivity.this, mBitmap, order_list1);
                            refuse_edListview.setAdapter(refuseLsitViewAdapter);
                            refuseLsitViewAdapter.notifyDataSetChanged();
                            for (int i = 0; i < refuseLsitViewAdapter.getGroupCount(); i++) {
                                refuse_edListview.expandGroup(i);// 初始化时，将ExpandableListView以展开的方式呈现
                            }
                        } else {
                            refuse_edListview.setVisibility(View.GONE);
                            mOederTv5.setVisibility(View.VISIBLE); // 无信息控件显示
                            mOederTv5.setText("暂无订单信息~");
                        }
                        initListener();
                    }
                    break;
            }
        }
    };

}
