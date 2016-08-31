package com.example.stamp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.OrderAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.OrderBean;
import com.example.stamp.bean.OrderSweepBean;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/8/31.
 */
public class OrderSweepActivity extends BaseActivity implements View.OnClickListener {

    private View mOrderTitle;
    private View mOrderContent;
    private TextView mTitle;
    private ImageView mback;
    private Button mOrderSweep, mOrder;
    private ListView mOederListview;
    private ArrayList<OrderBean> mList;
    private OrderAdapter adapter;


    @Override
    public View CreateTitle() {
        mOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        initdata();
        return mOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderContent = View.inflate(this, R.layout.activity_order, null);
        initView();
        initdata();
        initadapter();
        initListener();
        return mOrderContent;
    }

    private void initView() {
        mTitle = (TextView) mOrderTitle.findViewById(R.id.base_title);
        mTitle.setText("回购订单");
        mback = (ImageView) mOrderTitle.findViewById(R.id.base_title_back);
        mOrderSweep = (Button) mOrderContent.findViewById(R.id.record_sweep);
        mOrder = (Button) mOrderContent.findViewById(R.id.btn_order);
        mOederListview = (ListView) mOrderContent.findViewById(R.id.listView);
        mOederListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openActivityWitchAnimation(OrderDetailsActivity.class);
            }
        });
    }

    private void initdata() {
        mList = new ArrayList<OrderBean>();
        for (int i = 0; i < 4; i++) {
            mList.add(new OrderBean("2016-3-12 15:32:45", "待确认", "十二生肖珍藏册"));
            mList.add(new OrderBean("2016-3-12 15:32:45", "已完成", "十二生肖珍藏册"));
            mList.add(new OrderBean("2016-3-12 15:32:45", "订单驳回", "十二生肖珍藏册"));
        }
    }

    private void initListener() {
        mback.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mOrderSweep.setOnClickListener(this);


    }

    private void initadapter() {
        mOrder.setTextColor(Color.RED);
        mOrderSweep.setTextColor(Color.GRAY);
        adapter = new OrderAdapter(this, mBitmap, mList);
        mOederListview.setAdapter(adapter);


    }

    @Override
    public void AgainRequest() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.record_sweep:
                mOrderSweep.setTextColor(Color.GRAY);
                mOrder.setTextColor(Color.RED);
                startActivity(new Intent(OrderSweepActivity.this,OrderActivity.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.btn_order:
                mOrderSweep.setTextColor(Color.GRAY);
                mOrder.setTextColor(Color.RED);
                startActivity(new Intent(OrderSweepActivity.this,OrderSweepActivity.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.base_title_back:
                finishWitchAnimation();
                break;


        }

    }
}
