package cn.com.chinau.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;

/**
 * Date: 2016/11/12 19:23
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 支付成功页面
 */

public class OrderBuySuccessActivity extends BaseActivity{


    private View mOrderBuyTitle,mOrderBuyContent;
    private TextView mLookOrder,mTitle;
    private ImageView mBack;
    private String mOrder;

    @Override
    public View CreateTitle() {
        mOrderBuyTitle = View.inflate(this, R.layout.base_back_title, null);
        mBack = (ImageView) mOrderBuyTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mOrderBuyTitle.findViewById(R.id.base_title);
        mTitle.setText("支付成功");
        return mOrderBuyTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderBuyContent = View.inflate(this, R.layout.activity_success_orderbuy, null);
        mBack = (ImageView) mOrderBuyTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mOrderBuyTitle.findViewById(R.id.base_title);
        mLookOrder = (TextView) mOrderBuyContent.findViewById(R.id.look_order);
        GetIntent();
        initListener();
        return mOrderBuyContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void GetIntent(){
        Bundle bundle = getIntent().getExtras();
        mOrder = bundle.getString(StaticField.ORDERS); // 页面标识
    }


    private void initListener(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWitchAnimation();
            }
        });

        mLookOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.ORDERS, mOrder); // 待收货
                openActivityWitchAnimation(OrdersGoodsActivity.class,bundle); // 跳转订单列表待收货页面
                finish();
            }
        });
    }


}
