package cn.com.chinau.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * Date: 2016/10/25 01:14
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 提现成功页面
 */

public class ActivityAplayCashSuccessActivity extends BaseActivity {


    private View mAplayCashSuccessTitle;
    private ImageView mBack;
    private TextView mTitle;

    @Override
    public View CreateTitle() {
        mAplayCashSuccessTitle = View.inflate(this, R.layout.base_back_title, null);
        return mAplayCashSuccessTitle;
    }

    @Override
    public View CreateSuccess() {

       View mAplayCashSuccessConcent = View.inflate(this, R.layout.activity_aplay_cash_success, null);
        intiView();
        initListener ();
        return mAplayCashSuccessConcent;
    }

    @Override
    public void AgainRequest() {

    }

    private void intiView(){
        mBack =  (ImageView)mAplayCashSuccessTitle.findViewById(R.id.base_title_back);
        mTitle =  (TextView)mAplayCashSuccessTitle.findViewById(R.id.base_title);
        mTitle.setText("提交成功");
    }

    private void initListener (){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAplayCashSuccessActivity.this, WithdrawActivity.class);
                intent.putExtra("withdrawSuccess", "withdrawSuccess");
                startActivity(intent);
                finish();
            }
        });
    }

}
