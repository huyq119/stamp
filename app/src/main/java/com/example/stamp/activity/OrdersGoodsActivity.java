package com.example.stamp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.OrderGoodsPagerAdapter;
import com.example.stamp.adapter.StampDetailPagerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.fragment.ordersgoodsfragment.AllFragment;
import com.example.stamp.fragment.ordersgoodsfragment.CompleteFragment;
import com.example.stamp.fragment.ordersgoodsfragment.PaymentFragent;
import com.example.stamp.fragment.ordersgoodsfragment.ReceivingFragment;
import com.example.stamp.fragment.ordersgoodsfragment.RefuseFragment;
import com.example.stamp.view.CustomViewPager;
import com.example.stamp.view.HorizontalListView;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 * 商品订单页面
 */
public class OrdersGoodsActivity extends BaseActivity {

    private View mOrderTitle, mOrderContent;
    private TextView mTitle;
    private ImageView mback;
    private HorizontalListView hListView;
    private String[] mArr = {"全部", "待付款", "待收货", "已完成", "退款/退货"};
    private List<Fragment> mList;
    private String tag;
    private OrderGoodsPagerAdapter adapter;
    private CustomViewPager mViewPager;
    private TabPageIndicator mIndicator;

    @Override
    public View CreateTitle() {
        mOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderContent = View.inflate(this, R.layout.activity_order_goods, null);
        initViews();
        initAdapter();
        initstartview();
        return mOrderContent;
    }

    private void initstartview() {
        if (tag.equals("all")){
            mViewPager.setCurrentItem(0);
        }else if (tag.equals("payment")){
            mViewPager.setCurrentItem(1);
        } else if (tag.equals("receiving")){
            mViewPager.setCurrentItem(2);
        } else if (tag.equals("completed")){
            mViewPager.setCurrentItem(3);
        }else if (tag.equals("refused")){
            mViewPager.setCurrentItem(4);
        }
    }
    private void initViews() {
        // 获取我的页面传过来的键值
        Bundle bundle = getIntent().getExtras();
        tag = bundle.getString(StaticField.ORDERS);

        mTitle = (TextView) mOrderTitle.findViewById(R.id.base_title);
        mTitle.setText("商品订单");
        mback = (ImageView) mOrderTitle.findViewById(R.id.base_title_back);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });

        //初始化控件
        mViewPager = (CustomViewPager) mOrderContent.findViewById(R.id.order_viewpager);
        mIndicator = (TabPageIndicator) mOrderContent.findViewById(R.id.order_indicator);
    }

    private void initAdapter() {
        mList = new ArrayList<>();
        AllFragment mAll = new AllFragment();// 全部
        mList.add(mAll);
        PaymentFragent mPayment = new PaymentFragent();// 代付款
        mList.add(mPayment);
        ReceivingFragment mReceving = new ReceivingFragment(); // 待收货
        mList.add(mReceving);
        CompleteFragment mDoneDeal = new CompleteFragment();// 已完成
        mList.add(mDoneDeal);
        RefuseFragment mRefuse = new RefuseFragment(); // 退款/退货
        mList.add(mRefuse);

        //导航的ViewPager
        adapter = new OrderGoodsPagerAdapter(getSupportFragmentManager(), mList, mArr);
        mViewPager.setAdapter(adapter);
        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setViewPager(mViewPager);
    }
    @Override
    public void AgainRequest() {

    }
}
