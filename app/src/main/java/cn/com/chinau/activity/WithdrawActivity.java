package cn.com.chinau.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;

/**
 * 提现页面
 */
public class WithdrawActivity extends BaseActivity implements View.OnClickListener {


    private View mWithdrawTitle;
    private View mWithdrawContent;
    private ImageView mBack;
    private TextView mCard;//银行卡按钮
    private TextView mWithdraw;//提现页面

    private boolean flag = false;//只作为一个标记,之后要删除的

    @Override
    public View CreateTitle() {
        mWithdrawTitle = View.inflate(this, R.layout.base_back_title, null);
        return mWithdrawTitle;
    }

    @Override
    public View CreateSuccess() {
        mWithdrawContent = View.inflate(this, R.layout.activity_withdraw, null);
        initView();
        initListener();
        return mWithdrawContent;
    }



    private void initView() {
        TextView mTitle = (TextView) mWithdrawTitle.findViewById(R.id.base_title);
        mTitle.setText("可提现余额");

        mBack = (ImageView) mWithdrawTitle.findViewById(R.id.base_title_back);
        mCard = (TextView) mWithdrawContent.findViewById(R.id.withdraw_card);
        mWithdraw = (TextView) mWithdrawContent.findViewById(R.id.withdraw_card_cash);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mCard.setOnClickListener(this);
        mWithdraw.setOnClickListener(this);
    }
    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.withdraw_card://银行卡页面
                //这里要判断一下是否绑定银行卡
                if(flag){

                }else{
                    openActivityWitchAnimation(CardActivity.class);
                }
                break;
            case R.id.withdraw_card_cash://申请提现
                openActivityWitchAnimation(ApplyWithdrawActivity.class);
                break;
        }
    }
}
