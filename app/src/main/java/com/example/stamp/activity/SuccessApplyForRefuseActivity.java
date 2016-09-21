package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * Created by Administrator on 2016/9/21.
 * （申请成功）退款/退货页面
 */
public class SuccessApplyForRefuseActivity extends BaseActivity {


    private View mRefuseTitle,mRefuseContent;
    private ImageView mBack;
    private TextView mTitle,mLookOrder;

    @Override
    public View CreateTitle() {
        mRefuseTitle = View.inflate(this, R.layout.base_back_title, null);
        return mRefuseTitle;
    }

    @Override
    public View CreateSuccess() {
        mRefuseContent = View.inflate(this, R.layout.activity_success_refuse_intervene, null);
        initView();
        initListener();
        return mRefuseContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mBack =(ImageView) mRefuseTitle.findViewById(R.id.base_title_back);
        mTitle =(TextView) mRefuseTitle.findViewById(R.id.base_title);
        mTitle.setText("退款/退货");
        mLookOrder =(TextView) mRefuseContent.findViewById(R.id.look_order);

    }
    private void initListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        mLookOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转至查看订单详情页面
                openActivityWitchAnimation(LookOrderDetailRefuseActivity.class);
            }
        });
    }



}
