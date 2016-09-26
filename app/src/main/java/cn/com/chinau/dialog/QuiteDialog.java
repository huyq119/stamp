package cn.com.chinau.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.chinau.R;

/**
 * Created by Administrator on 2016/9/20.
 * 退出对话框
 */
public class QuiteDialog extends Dialog {


    private TextView mCancel;// 取消按钮
    private TextView mEnsure;// 确定按钮
    private LinearLayout mQuit;
    private LinearLayout mLogin, mNoLogin;
    private Context context;
    public static final String name = "stamp";
    private SharedPreferences sp;

    public QuiteDialog(Context context, LinearLayout mQuit, LinearLayout mLogin, LinearLayout mNoLogin) {
        super(context, R.style.exitdialog);
        this.context = context;
        this.mQuit = mQuit;
        this.mLogin = mLogin;
        this.mNoLogin = mNoLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        setContentView(R.layout.dialog_quite);
        // 设置为我们的布局
        this.setCanceledOnTouchOutside(false);
        // 设置为点击对话框之外的区域对话框不消失
        mEnsure = (TextView) findViewById(R.id.alertdialog_ensure);// 确定
        mCancel = (TextView) findViewById(R.id.alertdialog_cancel);// 取消
        // 设置事件
        mEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("token", "").commit();
                sp.edit().putString("userId", "").commit();
                sp.edit().putString("phone", "").commit();
                mQuit.setVisibility(View.GONE);
                mLogin.setVisibility(View.GONE);
                mNoLogin.setVisibility(View.VISIBLE);
                dismiss();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
