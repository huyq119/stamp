package cn.com.chinau.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import cn.com.chinau.R;


/**
 * Created by Administrator on 2016/9/19.
 * 进度对话框
 */
public class SendProgressDialog extends Dialog {
    public SendProgressDialog(Context context) {
        super(context, R.style.exitdialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_progress);
        this.setCanceledOnTouchOutside(false);
    }

}
