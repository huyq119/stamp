package cn.com.chinau.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.activity.WithdrawPressWordActivity;

/**
 * Date: 2016/10/25 11:49
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 设置提现密码对话框
 */

public class WithdrawDialog extends Dialog {

    private TextView mCancel;// 取消按钮
    private TextView mEnsure;// 确定按钮
    private Context context;
    private TextView mTitle;// 标题

    public WithdrawDialog(Context context) {
        super(context, R.style.exitdialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_withdraw);
        // 设置为点击对话框之外的区域对话框不消失
        this.setCanceledOnTouchOutside(false);
        mEnsure = (TextView) findViewById(R.id.withdraw_ensure);// 确定
        mCancel = (TextView) findViewById(R.id.withdraw_cancel);// 取消
        // 确定
        mEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转至设置提现密码页面
                context.startActivity(new Intent(context, WithdrawPressWordActivity.class));
                dismiss();
            }
        });
        // 取消
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
