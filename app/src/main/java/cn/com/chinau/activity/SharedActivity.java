package cn.com.chinau.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import cn.com.chinau.R;
import cn.com.chinau.utils.MyLog;

/**
 * 分享的Activity页面
 */
public class SharedActivity extends Activity implements View.OnClickListener {

    private ImageView mWX, mPYQ;//微信朋友圈
    private TextView mCancel;//取消按钮
    private View mFinish;//关闭页面
    private String mSharedImage, mGoods_name, mShare_url;
    private UMImage image;
    private boolean isText = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

//        UmengTool.getSignature(SharedActivity.this);
        initView();
        initListener();
    }


    private void initView() {
        // 获取邮市页面传过来的的值
        Bundle bundle = getIntent().getExtras();
        mSharedImage = bundle.getString("SharedImage");// 商品图片url
        mGoods_name = bundle.getString("Goods_name");// 商品名称
        mShare_url = bundle.getString("Share_url");// 分享地址url
        MyLog.LogShitou("传过来的url",mSharedImage+"--"+mShare_url+"--"+mGoods_name);
        image = new UMImage(this.getApplicationContext(), mSharedImage);
        mWX = (ImageView) findViewById(R.id.weixin);
        mPYQ = (ImageView) findViewById(R.id.pengyouquan);
        mCancel = (TextView) findViewById(R.id.shared_cancel);
        mFinish = findViewById(R.id.shared_finish);
    }

    private void initListener() {
        mWX.setOnClickListener(this);
        mPYQ.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weixin://微信
                ShareAction shareAction = new ShareAction(SharedActivity.this);
                shareAction.withText("微信分享"); // 显示的内容
                shareAction.withMedia(image);// 显示图片的url
                    shareAction.withTitle(mGoods_name);// 标题
                shareAction.withTargetUrl(mShare_url); // 分享后点击查看的地址url
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).share();
                finish();
                break;
            case R.id.pengyouquan://朋友圈
                ShareAction shareAction1 = new ShareAction(SharedActivity.this);
                shareAction1.withText("微信分享"); // 显示的内容
                shareAction1.withMedia(image);// 显示图片的url
                shareAction1.withTitle(mGoods_name);// 标题
                shareAction1.withTargetUrl(mShare_url); // 分享后点击查看的地址url
                shareAction1.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener)
                        .share();
                finish();
                break;
            case R.id.shared_cancel://取消按钮
                finish();
            case R.id.shared_finish://屏幕其他位置关闭页面
                finish();
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(SharedActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            MyLog.LogShitou("platform分享11", "" + platform);

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SharedActivity.this, platform + " 分享失败啦" + t.getMessage(), Toast.LENGTH_SHORT).show();
            MyLog.LogShitou("platform分享22", platform + "----" + t.getMessage());

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SharedActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            MyLog.LogShitou("platform分享33", platform + "");
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }


}
