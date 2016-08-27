package com.example.stamp.activity;

import android.view.View;
import android.widget.Button;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.zxing.activity.CaptureActivity;

/**
 * 扫码页面
 */
public class ScanActivity extends BaseActivity implements View.OnClickListener {

    private View mScanContent;
    private View mScanTitle;
    private Button mScan;//扫码回购


    @Override
    public View CreateTitle() {
        mScanTitle = View.inflate(this, R.layout.activity_scan_title, null);
        return mScanTitle;
    }

    @Override
    public View CreateSuccess() {
        mScanContent = View.inflate(this, R.layout.activity_scan_content, null);
        initView();
        initListener();
        return mScanContent;
    }


    private void initView() {
        mScan = (Button) mScanContent.findViewById(R.id.scan_back_buy_now);
    }

    private void initListener() {
        mScan.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_back_buy_now://立即扫码回购按钮
                openActivityWitchAnimation(CaptureActivity.class);
                break;
        }
    }
}
