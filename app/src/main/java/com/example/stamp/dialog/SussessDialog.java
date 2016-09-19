package com.example.stamp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.stamp.R;

/**
 * Created by Administrator on 2016/9/19.
 */
public class SussessDialog extends Dialog {


    public TextView mContext;// 内容

    public SussessDialog(Context context) {
        super(context, R.style.exitdialog);
        View view = View.inflate(context, R.layout.dialog_sussess, null);
        mContext = (TextView) view.findViewById(R.id.sussess_context);
        super.setContentView(view);
    }

    public void setText(String text) {
        mContext.setText(text);
    }


}
