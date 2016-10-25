package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.dialog.WithdrawDialog;
import cn.com.chinau.utils.MyLog;

/**
 * Date: 2016/10/25 11:22
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 空的添加银行
 */

public class CashEmptyActivity extends BaseActivity implements View.OnClickListener {
    private View mCashEmptyTitle;
    private ImageView mBack;
    private TextView mTitle, tv_addcard;
    private View mCashEmptyCoenct;
    private int isRemitPwd;// 是否设置银行卡标识
    private SharedPreferences sp;

    @Override
    public View CreateTitle() {
        mCashEmptyTitle = View.inflate(this, R.layout.base_back_title, null);
        return mCashEmptyTitle;
    }

    @Override
    public View CreateSuccess() {
        mCashEmptyCoenct = View.inflate(this, R.layout.activity_cash_card_empty, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_APPEND);
        initView();
        initListener();
        return mCashEmptyCoenct;
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_back:// 返回
                finishWitchAnimation();
                break;
            case R.id.tv_addcard:// 添加银行卡
                isRemitPwd = sp.getInt("isRemitPwd", 0);
                MyLog.LogShitou("isRemitPwd是否设置提现密码的值", isRemitPwd + "");
                if (isRemitPwd == 1) {// 已经提现密码
                    Bundle bundle = new Bundle();
                    bundle.putString("CashEmpty","CashEmpty");
               openActivityWitchAnimation(ReviseCardActivity.class,bundle);
//                    startActivity(new Intent(CashEmptyActivity.this, ReviseCardActivity.class));
                } else {
                    // 弹出框提示需要先设置提现密码
                    WithdrawDialog dialog = new WithdrawDialog(CashEmptyActivity.this);
                    dialog.show();
                }

                break;
        }
    }


    private void initView() {
        mBack = (ImageView) mCashEmptyTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mCashEmptyTitle.findViewById(R.id.base_title);
        mTitle.setText("添加银行卡");
        tv_addcard = (TextView) mCashEmptyCoenct.findViewById(R.id.tv_addcard);


    }

    private void initListener() {
        mBack.setOnClickListener(this);
        tv_addcard.setOnClickListener(this);
    }
}
