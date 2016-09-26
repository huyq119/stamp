package cn.com.chinau.activity;

import android.view.View;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * 申请提现页面
 */
public class ApplyWithdrawActivity extends BaseActivity {

    private View mApplyWithdrawContent;
    private View mApplyWithdrawTitle;



    @Override
    public View CreateTitle() {
        mApplyWithdrawTitle = View.inflate(this, R.layout.base_back_title, null);
        return mApplyWithdrawTitle;
    }

    @Override
    public View CreateSuccess() {
        mApplyWithdrawContent = View.inflate(this, R.layout.activity_applywithdraw, null);
        return mApplyWithdrawContent;
    }

    @Override
    public void AgainRequest() {

    }
}
