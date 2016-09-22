package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.LookOrderDetailRefuseAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.LookOrderDetailRefuseBean;
import com.example.stamp.dialog.PhoneDialog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/22.
 * (查看订单详情)退款/退货页面
 */
public class LookOrderDetailRefuseActivity extends BaseActivity{

    private View mRefuseTitle,mRefuseContent;
    private ImageView mBack;
    private TextView mTitle,mServiceTel;
    private ListView mListView;
    private ArrayList<LookOrderDetailRefuseBean> mLsit;

    @Override
    public View CreateTitle() {
        mRefuseTitle = View.inflate(this, R.layout.base_back_title, null);
        return mRefuseTitle;
    }

    @Override
    public View CreateSuccess() {
        mRefuseContent = View.inflate(this, R.layout.activity_look_order_detail_refuse, null);
        initView();
        initAdapter();
        return mRefuseContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mBack =(ImageView) mRefuseTitle.findViewById(R.id.base_title_back);
        mTitle =(TextView) mRefuseTitle.findViewById(R.id.base_title);
        mTitle.setText("退款/退货");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        mListView =(ListView) mRefuseContent.findViewById(R.id.look_order_detail_listview);
        mServiceTel =(TextView) mRefuseContent.findViewById(R.id.Service_tel);
        mServiceTel.setOnClickListener(new View.OnClickListener() {//客服电话
            @Override
            public void onClick(View view) {
                PhoneDialog phoneDialog = new PhoneDialog(LookOrderDetailRefuseActivity.this,mServiceTel.getText().toString());
                phoneDialog.show();
            }
        });

    }
    private void initAdapter(){
        mLsit = new ArrayList();
        mLsit.add(new LookOrderDetailRefuseBean("2016-09-9","申请退款",null));
        mLsit.add(new LookOrderDetailRefuseBean("2016-09-10","退款驳回（卖家）","售出不退"));
        mLsit.add(new LookOrderDetailRefuseBean("2016-09-11","申请平台介入",null));
        mLsit.add(new LookOrderDetailRefuseBean("2016-09-12","退款关闭","平台拒绝退款"));
        LookOrderDetailRefuseAdapter mAdapter = new LookOrderDetailRefuseAdapter(this, mLsit);
        mListView.setAdapter(mAdapter);
    }

}
