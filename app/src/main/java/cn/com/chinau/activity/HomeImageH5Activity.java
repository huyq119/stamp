package cn.com.chinau.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.StaticField;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * 首页图片的h5页面
 */
public class HomeImageH5Activity extends BaseActivity implements View.OnClickListener {

    private View mHomeImageH5Content;
    private View mHomeImageH5Title;
    private WebView mWB;
    private TextView mTitle;
    private ImageView mBack;


    @Override
    public View CreateTitle() {
        mHomeImageH5Title = View.inflate(this, R.layout.base_back_title, null);
        return mHomeImageH5Title;
    }

    @Override
    public View CreateSuccess() {
        mHomeImageH5Content = View.inflate(this, R.layout.activity_homeimageh5, null);
        initView();
        initData();
        initListener();
        return mHomeImageH5Content;
    }


    private void initView() {
        mTitle = (TextView) mHomeImageH5Title.findViewById(R.id.base_title);
        mBack = (ImageView) mHomeImageH5Title.findViewById(R.id.base_title_back);
        mWB = (WebView) mHomeImageH5Content.findViewById(R.id.home_image_wb);
    }

    private void initData() {
        mWB.loadUrl(getURL());
        mTitle.setText(getTitleName());
    }

    private void initListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {
        initData();
    }

    public String getURL() {
        return getIntent().getStringExtra(StaticField.HOMEURL);
    }

    public String getTitleName() {
        return getIntent().getStringExtra(StaticField.HOMETITLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back:
                finishWitchAnimation();
                break;
        }
    }
}
