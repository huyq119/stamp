package com.example.stamp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * Created by Administrator on 2016/9/19.
 * 服务协议
 */
public class ServiceTermActivity extends BaseActivity{

    private WebView mServiceVb;
    private ImageView mBack;
    private TextView mTitle;
    private View ServiceTitle,ServiceContent;

    @Override
    public View CreateTitle() {
        ServiceTitle = View.inflate(this, R.layout.base_back_title, null);

        return null;
    }

    @Override
    public View CreateSuccess() {
        ServiceContent = View.inflate(this, R.layout.activity_serviceterm, null);
        initView();

        return null;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView(){
        mBack = (ImageView) ServiceTitle.findViewById(R.id.base_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        mTitle = (TextView) ServiceTitle.findViewById(R.id.base_title);
        mTitle.setText("邮来邮网服务协议");
        mServiceVb = (WebView) ServiceContent.findViewById(R.id.serviceterm_vb);
    }


}
