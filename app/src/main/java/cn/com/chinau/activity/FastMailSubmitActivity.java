package cn.com.chinau.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.utils.MyToast;

/**
 * 快递提交成功页面
 */
public class FastMailSubmitActivity extends BaseActivity implements View.OnClickListener{

    private View mFastMailSubmitContent;
    private View mFastMailSubmitTitle;
    private ImageView mBack;
    private TextView mTitle,mSubmit;


    @Override
    public View CreateTitle() {
        mFastMailSubmitTitle = View.inflate(this, R.layout.base_back_title, null);
        return mFastMailSubmitTitle;
    }

    @Override
    public View CreateSuccess() {
        mFastMailSubmitContent = View.inflate(this, R.layout.activity_fastmailsubmit, null);
        initView();
        initListener();
        return mFastMailSubmitContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mBack = (ImageView) mFastMailSubmitTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mFastMailSubmitTitle.findViewById(R.id.base_title);
        mTitle.setText("提交成功");
        mSubmit = (TextView) mFastMailSubmitContent.findViewById(R.id.commit_sumbit);
    }

    private void initListener() {

        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.commit_sumbit://查看订单按钮
                MyToast.showShort(this,"点击了查看订单");
                break;
        }
    }
}
