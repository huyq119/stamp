package cn.com.chinau.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import java.util.Map;

import cn.com.chinau.MainActivity;
import cn.com.chinau.StaticField;
import cn.com.chinau.utils.MyLog;

/**
 * Date: 2016/10/23 12:06
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 微信分享发起的回调
 */

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, StaticField.APP_ID, false);
        api.registerApp(StaticField.APP_ID);
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
    public void onResp(BaseResp baseResp) {
        int result = 0;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

//                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = R.string.errcode_deny;
                break;
            default:
//                result = R.string.errcode_unknown;
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
//        Log.d(TAG, "onPayFinish, errCode = " + baseReq.errCode);
        finish();
    }

    @Override
    protected void handleIntent(Intent intent) {
        mWxHandler.setAuthListener(new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                MyLog.e("UMWXHandler fsdfsdfs");
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
            }
        });
        super.handleIntent(intent);
    }


}
