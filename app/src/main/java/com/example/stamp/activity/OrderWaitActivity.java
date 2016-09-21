package com.example.stamp.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.utils.MyToast;

/**
 * 回购订单待寄送页面
 */
public class OrderWaitActivity extends BaseActivity implements View.OnClickListener {

    private View mOrderWaitTitle;
    private View mOrderWaitContent;
    private ImageView mBack;
    private Button mSend;


    @Override
    public View CreateTitle() {
        mOrderWaitTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderWaitTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderWaitContent = View.inflate(this, R.layout.activity_orderwait, null);
        initView();
        initListener();
        return mOrderWaitContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mBack = (ImageView) mOrderWaitTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mOrderWaitTitle.findViewById(R.id.base_title);
        mTitle.setText("订单详情");
        mSend = (Button) mOrderWaitContent.findViewById(R.id.btn_send);


    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back:
                finishWitchAnimation();
                break;
            case R.id.btn_send:
                MyToast.showShort(this, "点击了去寄送");
                break;
            default:
                break;
        }
    }
}
