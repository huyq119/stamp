package cn.com.chinau.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.dialog.PhoneDialog;

/**
 * 关于我们
 */
public class UsMeActivity extends BaseActivity implements View.OnClickListener {

    private View mUsMeTitle;
    private View mUsMeContent;
    private ImageView mBack;
    private TextView mUserProtocol;//用户协议
    private TextView mCompanyUrl;//官方网站
    private TextView mCallPhone;//客服电话


    @Override
    public View CreateTitle() {
        mUsMeTitle = View.inflate(this, R.layout.base_back_title, null);
        return mUsMeTitle;
    }

    @Override
    public View CreateSuccess() {
        mUsMeContent = View.inflate(this, R.layout.activity_usme_content, null);
        initView();
        initListener();
        return mUsMeContent;
    }


    private void initView() {
        TextView mTitle = (TextView) mUsMeTitle.findViewById(R.id.base_title);
        mTitle.setText("关于我们");
        mBack = (ImageView) mUsMeTitle.findViewById(R.id.base_title_back);
        mUserProtocol = (TextView) mUsMeContent.findViewById(R.id.usMe_service);
        mCompanyUrl = (TextView) mUsMeContent.findViewById(R.id.usMe_net);
        mCallPhone = (TextView) mUsMeContent.findViewById(R.id.usMe_phone);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mUserProtocol.setOnClickListener(this);
        mCompanyUrl.setOnClickListener(this);
        mCallPhone.setOnClickListener(this);
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
            case R.id.usMe_service://用户协议
                openActivityWitchAnimation(ServiceProtocolActivity.class);
                break;
            case R.id.usMe_net://官网网址
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.chinau.com.cn");
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.usMe_phone://客服电话
                PhoneDialog phoneDialog = new PhoneDialog(this,mCallPhone.getText().toString());
                phoneDialog.show();
                break;
        }
    }
}
