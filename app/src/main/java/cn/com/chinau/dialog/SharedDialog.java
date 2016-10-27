package cn.com.chinau.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import cn.com.chinau.R;

/**
 * Date: 2016/10/27 22:42
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 分享弹出的Dialog
 */

public class SharedDialog extends Dialog {

    public SharedDialog(Context context) {
        super(context, R.style.MyDialogStyleBottom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shared);

        this.setCanceledOnTouchOutside(true); // 点击外部关闭
    }
}
