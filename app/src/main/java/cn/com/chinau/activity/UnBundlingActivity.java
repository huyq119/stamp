package cn.com.chinau.activity;

import android.view.View;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * 解绑银行卡页面
 */
public class UnBundlingActivity extends BaseActivity {


    private View mUnBundlingTitle;
    private View mUnBundlingContent;

    @Override
    public View CreateTitle() {
        mUnBundlingTitle = View.inflate(this, R.layout.base_back_title, null);
        return mUnBundlingTitle;
    }

    @Override
    public View CreateSuccess() {
        mUnBundlingContent = View.inflate(this, R.layout.activity_unbundling, null);
        return mUnBundlingContent;
    }

    @Override
    public void AgainRequest() {

    }
}
