package cn.com.chinau.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import cn.com.chinau.R;


/**
 * Created by Administrator on 2016/8/31.
 * 竞拍出价规则与协议Dialog
 */
public class AuctionRegulationsAgreementDialog extends Dialog{

    public AuctionRegulationsAgreementDialog(Context context) {
        super(context, R.style.exitdialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_agreement);
        // 设置点击外部窗口不关闭Dialog
        this.setCanceledOnTouchOutside(false);
    }
}
