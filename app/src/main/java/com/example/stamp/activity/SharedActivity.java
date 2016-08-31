package com.example.stamp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.utils.MyToast;

/**
 * 分享的Activity页面
 */
public class SharedActivity extends Activity implements View.OnClickListener {

    private ImageView mWX, mPYQ;//微信朋友圈
    private TextView mCancel;//取消按钮
    private View mFinish;//关闭页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        initView();
        initListener();
    }


    private void initView() {
        mWX = (ImageView) findViewById(R.id.weixin);
        mPYQ = (ImageView) findViewById(R.id.pengyouquan);
        mCancel = (TextView) findViewById(R.id.shared_cancel);
        mFinish = findViewById(R.id.shared_finish);
    }

    private void initListener() {
        mWX.setOnClickListener(this);
        mPYQ.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weixin://微信
                MyToast.showShort(this, "点击了微信按钮");
                break;
            case R.id.pengyouquan://朋友圈
                MyToast.showShort(this, "点击了朋友圈按钮");
                break;
            case R.id.shared_cancel://取消按钮
                finish();
            case R.id.shared_finish://屏幕其他位置关闭页面
                finish();
                break;
        }
    }
}
