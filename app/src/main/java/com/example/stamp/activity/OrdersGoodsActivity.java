package com.example.stamp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.OrderAllLsitViewAdapter;
import com.example.stamp.adapter.OrderGoodsPagerAdapter;
import com.example.stamp.adapter.OrderPaymentReceivingCompleteLsitViewAdapter;
import com.example.stamp.adapter.OrderRefuseLsitViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.OrderAllListViewGoodsBean;
import com.example.stamp.bean.OrderAllListViewGroupBean;
import com.example.stamp.view.OrderGoodsViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private OrderAllLsitViewAdapter allLsitViewAdapter;
    private List<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList> groups = new ArrayList<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList>();// 组元素数据列表
    private Map<String, List<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList.OrderDetailList>> goods = new HashMap<String, List<OrderAllListViewGoodsBean.OrdersDataList.SellerDataList.OrderDetailList>>();// 子元素数据列表

    private List<HashMap<String, String>> lists;


    @Override
    public View CreateTitle() {
        mOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderContent = View.inflate(this, R.layout.activity_order_goods, null);
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
            view1.setBackgroundResource(R.color.red_font);
            view2.setBackgroundResource(R.color.line_bg);
            view3.setBackgroundResource(R.color.line_bg);
            view4.setBackgroundResource(R.color.line_bg);
            view5.setBackgroundResource(R.color.line_bg);
            mViewPager.setCurrentItem(0);
        } else if (tag.equals("payment")) {
            view1.setBackgroundResource(R.color.line_bg);
            view2.setBackgroundResource(R.color.red_font);
            view3.setBackgroundResource(R.color.line_bg);
            view4.setBackgroundResource(R.color.line_bg);
            view5.setBackgroundResource(R.color.line_bg);
            mViewPager.setCurrentItem(1);
        } else if (tag.equals("receiving")) {
            view1.setBackgroundResource(R.color.line_bg);
            view2.setBackgroundResource(R.color.line_bg);
            view3.setBackgroundResource(R.color.red_font);
            view4.setBackgroundResource(R.color.line_bg);
            view5.setBackgroundResource(R.color.line_bg);
            mViewPager.setCurrentItem(2);
        } else if (tag.equals("completed")) {
            view1.setBackgroundResource(R.color.line_bg);
            view2.setBackgroundResource(R.color.line_bg);
            view3.setBackgroundResource(R.color.line_bg);
            view4.setBackgroundResource(R.color.red_font);
            view5.setBackgroundResource(R.color.line_bg);
            mViewPager.setCurrentItem(3);
        } else if (tag.equals("refused")) {
            view1.setBackgroundResource(R.color.line_bg);
            view2.setBackgroundResource(R.color.line_bg);
            view3.setBackgroundResource(R.color.line_bg);
            view4.setBackgroundResource(R.color.line_bg);
            view5.setBackgroundResource(R.color.red_font);
            mViewPager.setCurrentItem(4);
        }
    }

    private void initViews() {
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

        view1 = mOrderContent.findViewById(R.id.view1);// line为红色
        view1.setBackgroundResource(R.color.red_font);
        view2 = mOrderContent.findViewById(R.id.view2);
        view3 = mOrderContent.findViewById(R.id.view3);
        view4 = mOrderContent.findViewById(R.id.view4);
        view5 = mOrderContent.findViewById(R.id.view5);

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


        OrderAllLsitViewAdapter allLsitViewAdapter = new OrderAllLsitViewAdapter(this, mBitmap, groups, goods);// 全部适配器
        // （待付款，待收货，已完成）适配器
        OrderPaymentReceivingCompleteLsitViewAdapter PayRecComLsitViewAdapter = new OrderPaymentReceivingCompleteLsitViewAdapter(this, mBitmap, groups, goods);
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

    }


    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new MyGroupListener());
        mViewPager.setOnPageChangeListener(new MyPagerChangeListener());

        all_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });
        payment_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });
        receiving_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });
        complete_edListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                openActivityWitchAnimation(OrderDetailsActivity.class);
                return false;
            }
        });

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
                    mAllBtn.setTextColor(getResources().getColor(R.color.red_font));
                    mPaymentBtn.setTextColor(getResources().getColor(R.color.black));
                    mReceiving.setTextColor(getResources().getColor(R.color.black));
                    mComplete.setTextColor(getResources().getColor(R.color.black));
                    mRefuse.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.red_font);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    view5.setBackgroundResource(R.color.line_bg);
                    break;
                case 1:
                    mAllBtn.setTextColor(getResources().getColor(R.color.black));
                    mPaymentBtn.setTextColor(getResources().getColor(R.color.red_font));
                    mReceiving.setTextColor(getResources().getColor(R.color.black));
                    mComplete.setTextColor(getResources().getColor(R.color.black));
                    mRefuse.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.red_font);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    view5.setBackgroundResource(R.color.line_bg);

                    break;
                case 2:
                    mAllBtn.setTextColor(getResources().getColor(R.color.black));
                    mPaymentBtn.setTextColor(getResources().getColor(R.color.black));
                    mReceiving.setTextColor(getResources().getColor(R.color.red_font));
                    mComplete.setTextColor(getResources().getColor(R.color.black));
                    mRefuse.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.red_font);
                    view4.setBackgroundResource(R.color.line_bg);
                    view5.setBackgroundResource(R.color.line_bg);

                    break;
                case 3:
                    mAllBtn.setTextColor(getResources().getColor(R.color.black));
                    mPaymentBtn.setTextColor(getResources().getColor(R.color.black));
                    mReceiving.setTextColor(getResources().getColor(R.color.black));
                    mComplete.setTextColor(getResources().getColor(R.color.red_font));
                    mRefuse.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.red_font);
                    view5.setBackgroundResource(R.color.line_bg);

                    break;
                case 4:
                    mAllBtn.setTextColor(getResources().getColor(R.color.black));
                    mPaymentBtn.setTextColor(getResources().getColor(R.color.black));
                    mReceiving.setTextColor(getResources().getColor(R.color.black));
                    mComplete.setTextColor(getResources().getColor(R.color.black));
                    mRefuse.setTextColor(getResources().getColor(R.color.red_font));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    view5.setBackgroundResource(R.color.red_font);

                    break;
                default:
                    break;
            }

        }

        ;
    }

    public class MyGroupListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.RBtn_All:
                    position = 0;
                    view1.setBackgroundResource(R.color.red_font);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    view5.setBackgroundResource(R.color.line_bg);
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.RBtn_Payment:
                    position = 1;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.red_font);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    view5.setBackgroundResource(R.color.line_bg);
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.RBtn_Receiving:
                    position = 2;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.red_font);
                    view4.setBackgroundResource(R.color.line_bg);
                    view5.setBackgroundResource(R.color.line_bg);
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.RBtn_Complete:
                    position = 3;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.red_font);
                    view5.setBackgroundResource(R.color.line_bg);
                    mViewPager.setCurrentItem(3);
                    break;
                case R.id.RBtn_Refuse:
                    position = 4;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    view5.setBackgroundResource(R.color.red_font);
                    mViewPager.setCurrentItem(4);
                    break;

                default:
                    break;
            }

        }

    }


}
