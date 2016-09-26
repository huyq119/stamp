package cn.com.chinau.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * 订单详情审核中页面
 */
public class OrderAuditingActivity extends BaseActivity {

    private View mOrderAuditingTitle;
    private View mOrderAuditingContent;
    private ImageView mBack;


    @Override
    public View CreateTitle() {
        mOrderAuditingTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderAuditingTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderAuditingContent = View.inflate(this, R.layout.activity_orderauditing, null);
        initView();
        return mOrderAuditingContent;
    }


    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mBack = (ImageView) mOrderAuditingTitle.findViewById(R.id.base_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                finishWitchAnimation();
            }
        });
        TextView mTitle = (TextView) mOrderAuditingTitle.findViewById(R.id.base_title);
        mTitle.setText("订单详情");

    }
}
