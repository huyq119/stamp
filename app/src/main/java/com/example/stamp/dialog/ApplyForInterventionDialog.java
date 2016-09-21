package com.example.stamp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.stamp.R;

/**
 * Created by Administrator on 2016/9/21.
 * (退货/退款)申请平台介入Dialog
 */
public class ApplyForInterventionDialog extends Dialog{

    public ApplyForInterventionDialog(Context context) {
        super(context, R.style.exitdialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_apply_for);
        // 设置点击外部窗口不关闭Dialog
        this.setCanceledOnTouchOutside(false);
    }
}
