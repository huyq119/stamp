package cn.com.chinau.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.com.chinau.R;

/**
 * 拨打电话的Dialog
 * Created by Administrator on 2016/8/17.
 */
public class PhoneDialog extends Dialog {

    private Context context;
    private TextView mCall;//呼叫
    private TextView mCancel;//取消
    private String phone;


    public PhoneDialog(Context context, String phone) {
        super(context, R.style.Dialog);
        this.context = context;
        this.phone = phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_phone);

        initView();
        initListener();
    }

    private void initView() {
        mCancel = (TextView) findViewById(R.id.phone_cancel);
        mCall = (TextView) findViewById(R.id.phone_call);

        TextView mPhone = (TextView) findViewById(R.id.phone);
        mPhone.setText(phone);
    }

    private void initListener() {
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntentDial = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                context.startActivity(myIntentDial);
                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
