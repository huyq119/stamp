package cn.com.chinau.activity;

import android.view.View;
import android.widget.Button;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * 快递信息页面
 */
public class FastMailInfoActivity extends BaseActivity implements View.OnClickListener {


    private View mFastMailInfoTitle;
    private View mFastMailInfoContent;
    private Button mSubmit;//提交按钮

    @Override
    public View CreateTitle() {
        mFastMailInfoTitle = View.inflate(this, R.layout.base_back_title, null);
        return mFastMailInfoTitle;
    }

    @Override
    public View CreateSuccess() {
        mFastMailInfoContent = View.inflate(this, R.layout.activity_fastmailinfo, null);
        initView();
        initListener();
        return mFastMailInfoContent;
    }

    private void initView() {
        mSubmit = (Button) mFastMailInfoContent.findViewById(R.id.FastMail_Submit);
    }

    private void initListener() {
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FastMail_Submit://提交按钮
                openActivityWitchAnimation(FastMailSubmitActivity.class);
                break;
        }
    }
}
