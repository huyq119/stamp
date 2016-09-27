package cn.com.chinau.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * 快递信息页面
 */
public class FastMailInfoActivity extends BaseActivity implements View.OnClickListener {


    private View mFastMailInfoTitle;
    private View mFastMailInfoContent;
    private Button mSubmit;//提交按钮
    private ImageView mBack;
    private TextView mTitle;

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
        mBack = (ImageView) mFastMailInfoTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mFastMailInfoTitle.findViewById(R.id.base_title);
        mTitle.setText("快递信息");
        mSubmit = (Button) mFastMailInfoContent.findViewById(R.id.FastMail_Submit);
    }

    private void initListener() {

        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.FastMail_Submit://提交按钮
                openActivityWitchAnimation(FastMailSubmitActivity.class);
                break;
        }
    }
}
