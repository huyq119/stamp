package cn.com.chinau.activity;

import android.view.View;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * 快递提交成功页面
 */
public class FastMailSubmitActivity extends BaseActivity {

    private View mFastMailSubmitContent;
    private View mFastMailSubmitTitle;


    @Override
    public View CreateTitle() {
        mFastMailSubmitTitle = View.inflate(this, R.layout.base_back_title, null);
        return mFastMailSubmitTitle;
    }

    @Override
    public View CreateSuccess() {
        mFastMailSubmitContent = View.inflate(this, R.layout.activity_fastmailsubmit, null);
        return mFastMailSubmitContent;
    }

    @Override
    public void AgainRequest() {

    }
}
