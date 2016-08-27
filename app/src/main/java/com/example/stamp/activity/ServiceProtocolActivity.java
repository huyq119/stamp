package com.example.stamp.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;

/**
 * 服务协议
 */
public class ServiceProtocolActivity extends BaseActivity {


    private View mServiceProtocolTitle;
    private View mServiceProtocolContent;

    @Override
    public View CreateTitle() {
        mServiceProtocolTitle = View.inflate(this, R.layout.base_back_title, null);
        return mServiceProtocolTitle;
    }

    @Override
    public View CreateSuccess() {
        mServiceProtocolContent = View.inflate(this, R.layout.activity_serviceprotocol, null);
        initView();
        return mServiceProtocolContent;
    }

    private void initView() {
        //设置标题
        TextView mTitle = (TextView) mServiceProtocolTitle.findViewById(R.id.base_title);
        mTitle.setText("邮来邮网服务协议");
        //设置返回按钮
        ImageView mBack = (ImageView) mServiceProtocolTitle.findViewById(R.id.base_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        //设置WebView
        WebView ServiceProtocol = (WebView) mServiceProtocolContent.findViewById(R.id.serviceProtocol_vb);
        ServiceProtocol.loadUrl("file:///android_asset/ServiceIndex.html");
    }

    @Override
    public void AgainRequest() {

    }
}
