package cn.com.chinau.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.utils.MyLog;

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
        mWB.getSettings().setJavaScriptEnabled(true); // 设置webview支持javascript
    }

    private void initData() {
        mWB.loadUrl(getURL());
        mTitle.setText(getTitleName());
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mWB.setWebViewClient(new MywebViewClient());// 设置监听事件
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

     // 如果使用不过期的shouldOverrideUrlLoading H5还是会跳转
    // 点击webView元素的监听事件
    private class MywebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.stopLoading();
            if (url != null) {
                String [] mUrrl = url.split("="); // 点击web页面获取的url,截取以"="为分割符
               String mGoods_sn = mUrrl[1]; // 获取点击web页面获取的商品编号
                MyLog.LogShitou("==========","邮票编号="+mGoods_sn);
                if(mGoods_sn !=null){
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticField.GOODS_SN, mGoods_sn);
                    openActivityWitchAnimation(SelfMallDetailActivity.class, bundle);
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }


    }
}
