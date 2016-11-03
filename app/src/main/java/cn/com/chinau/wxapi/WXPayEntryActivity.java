package cn.com.chinau.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.com.chinau.StaticField;
import cn.com.chinau.utils.MyLog;

/**
 * Date: 2016/10/23 11:56
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 微信支付发起的回调
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, StaticField.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }


    @Override
    public void onResp(BaseResp resp) {
        MyLog.LogShitou("WXPayEntryActivity回调微信支付的结果", "errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = resp.errCode;
            if (errCode == -1) {
                Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_LONG).show();
                MyLog.LogShitou("是否到这了11", errCode + "");
            } else if (errCode == 0) {
                Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_LONG).show();
                MyLog.LogShitou("是否到这了22", errCode + "");
            } else if (errCode == 2) {
                Toast.makeText(getApplicationContext(), "取消支付", Toast.LENGTH_LONG).show();
                MyLog.LogShitou("是否到这了33", errCode + "");
            }
            finish();
        }

//
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("提示");
//            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//            builder.show();
//        }
    }

}

