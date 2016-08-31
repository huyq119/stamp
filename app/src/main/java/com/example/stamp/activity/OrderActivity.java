package com.example.stamp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.OrderSweepAdapter;
import com.example.stamp.base.BaseActivity;


import com.example.stamp.bean.OrderSweepBean;
import com.example.stamp.utils.MyLog;


import java.util.ArrayList;
import java.util.Random;

/**
 * 回购订单页面
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener {

    private View mOrderTitle;
    private View mOrderContent;
    private TextView mTitle;
    private ImageView mback;
    private Button mOrderSweep,mOrder;
    private ArrayList<OrderSweepBean.Orderbean> list;
    private ListView mlistview;
    private OrderSweepAdapter adapter;
    private ListView mOederListview;



    @Override
    public View CreateTitle() {
        mOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderContent = View.inflate(this, R.layout.activity_order, null);
        initView();
        initdada();
        initListener();
        initAdapter();

        return mOrderContent;
    }

    private void initView() {
        mTitle= (TextView)mOrderTitle.findViewById(R.id.base_title);
        mTitle.setText("回购订单");
        mback = (ImageView)mOrderTitle.findViewById(R.id.base_title_back);
        mOrderSweep = (Button)mOrderContent.findViewById(R.id.record_sweep);
        mOrder = (Button)mOrderContent.findViewById(R.id.btn_order);
        mOederListview = (ListView)mOrderContent.findViewById(R.id.listView);
        mOederListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Random random = new Random();
                int ran = random.nextInt(5);
                MyLog.e(ran + "");
                if (ran == 0) {
                    openActivityWitchAnimation(OrderCloseActivity.class);

                } else if (ran == 1) {

                    openActivityWitchAnimation(OrderAuditingActivity.class);

                } else if (ran == 2) {

                    openActivityWitchAnimation(OrderFinishActivity.class);
                } else if (ran == 3) {

                    openActivityWitchAnimation(OrderRejectActivity.class);
                } else {

                    openActivityWitchAnimation(OrderWaitActivity.class);
                }
            }
        });

    }
    private void initdada() {


        list = new ArrayList<>();
        list.add(new OrderSweepBean.Orderbean("中国共产党二十八周年诞生纪念邮票3枚全",  "62.0", "64.99", "2016-08-12", "待寄送", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        list.add(new OrderSweepBean.Orderbean("中国共产党二十八周年诞生纪念邮票3枚全",  "62.0", "64.99", "2016-08-25", "订单关闭", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        list.add(new OrderSweepBean.Orderbean("中国共产党二十八周年诞生纪念邮票3枚全",  "69.0", "64.99", "2016-05-13", "审核中", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        list.add(new OrderSweepBean.Orderbean("中国共产党二十八周年诞生纪念邮票3枚全",  "62.0", "64.99", "2016-12-13", "已完成", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        list.add(new OrderSweepBean.Orderbean("中国共产党二十八周年诞生纪念邮票3枚全",  "62.0", "64.99", "2016-10-01", "订单驳回","http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
    }
    private void initListener() {
        mback.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mOrderSweep.setOnClickListener(this);
    }
    private void initAdapter() {
        mOrder.setTextColor(Color.GRAY);
        mOrderSweep.setTextColor(Color.RED);
        adapter = new OrderSweepAdapter(this, mBitmap, list);
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
                startActivity(new Intent(OrderActivity.this,OrderSweepActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.btn_order:
                mOrderSweep.setTextColor(Color.GRAY);
                mOrder.setTextColor(Color.RED);
                startActivity(new Intent(OrderActivity.this,OrderSweepActivity.class));
                overridePendingTransition(0,0);
                break;

            case R.id.base_title_back:
                finishWitchAnimation();
                break;
        }


    }

}
