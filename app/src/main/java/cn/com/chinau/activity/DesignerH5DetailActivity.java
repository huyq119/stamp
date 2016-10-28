package cn.com.chinau.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.DesignerDetailsH5Bean;
import cn.com.chinau.dialog.SharedDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * Created by Administrator on 2016/8/19.
 * (设计故事,艺术作品，名家访谈)H5详情页面
 */
public class DesignerH5DetailActivity extends BaseActivity implements View.OnClickListener,UMShareListener {

    private View mStoryDetails;
    private View mStampTapDetailTitle;
    private ImageView mBack;
    private ImageView mShared,mWeiXin,mPengYouQuan;
    private TextView mTitle,tv_cancel;
    private WebView mWB;
    private SharedDialog dialog;
    private String mDetail, mStory_sn, mWorks_sn, mView_sn, mDetailH5, mCategory,mShare_url,mDesignerName,mDesignerImg;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS://详情数据
                    Gson gson = new Gson();
                    DesignerDetailsH5Bean mDetailsH5Bean = gson.fromJson((String) msg.obj, DesignerDetailsH5Bean.class);
                    mShare_url = mDetailsH5Bean.getShare_url(); // 分享地址url
                    mDetailH5 = mDetailsH5Bean.getDetail(); // 获取H5url
                    initSetWebView(mDetailH5);// WEB页面赋值方法
                    MyLog.LogShitou("----------分享地址url",mShare_url+"--"+mDetailH5);
                    break;
            }
        }
    };
    private View dialog_finsih;

    @Override
    public View CreateTitle() {
        mStampTapDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mStampTapDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mStoryDetails = View.inflate(this, R.layout.activity_designer_h5detail_content, null);
        initView();
        return mStoryDetails;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        // 获取设计家详情页面传过来的的值
        Bundle bundle = getIntent().getExtras();
        mDetail = bundle.getString(StaticField.DTEAIL);
        mStory_sn = bundle.getString(StaticField.DESIGNER_STORY_SN);// 设计故事编号
        mWorks_sn = bundle.getString(StaticField.DESIGNER_WORKS_SN);// 艺术作品编号
        mCategory = bundle.getString(StaticField.DESIGNER_ZP_CATEGORY);// 类型
        mView_sn = bundle.getString(StaticField.DESIGNER_VIEW_SN);// 名家访谈编号

        mDesignerName = bundle.getString("DesignerName");// 分享需要的名称
        mDesignerImg = bundle.getString("DesignerImg");// 分享需要的图片

        MyLog.LogShitou("-------------1111111111111111传过来的名字+图片url",mDesignerName+"--"+mDesignerImg);
        mView_sn = bundle.getString(StaticField.DESIGNER_VIEW_SN);// 名家访谈编号

        mBack = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_title_back);
        mShared = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_shared);
        mTitle = (TextView) mStampTapDetailTitle.findViewById(R.id.base_title);
        mWB = (WebView) mStoryDetails.findViewById(R.id.story_details_web);
        if (mDetail != null) {
            if (mDetail.equals("GS")) {
                mTitle.setText("设计故事");
                initData(mStory_sn, mDetail, null);
                MyLog.LogShitou("-----------111","111111");
            } else if (mDetail.equals("ZP")) {
                mTitle.setText("艺术作品");
                initData(mWorks_sn, mDetail, mCategory);
                MyLog.LogShitou("-----------222","2222222");
            } else if (mDetail.equals("FT")) {
                mTitle.setText("名家访谈");
                initData(mView_sn, mDetail, null);
                MyLog.LogShitou("-----------3333","3333333");
            }

        }
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);

    }

    /**
     * WEB页面赋值方法
     * @param H5Url url地址
     */
    private void initSetWebView(String H5Url) {
        if (mWB != null) {
            mWB.loadUrl(H5Url);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.base_shared://分享按钮
               SharedDialog(); // 分享弹出Dialog
                break;
        }
    }

    /**
     * @param sn 编号
     * @param op_type 操作类型
     * @param zp_category 艺术作品类别
     */
    private void initData(final String sn, final String op_type, final String zp_category) {

        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.DESIGNER_DETAIL_QUERY);// url
                params.put(StaticField.DESIGNER_SN, sn);// 编号
                params.put(StaticField.DESIGNER_OP_TYPE, op_type);// 操作类型
                if (op_type.equals("ZP")) {
                    params.put(StaticField.DESIGNER_ZP_CATEGORY, zp_category);// 艺术作品类别
                }
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                if (result.equals("-1") | result.equals("-2") ) {
                    return;
                }

                MyLog.e("设计家详情~~~>" + result);

                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });


    }

    /**
     * 分享弹出Dialog
     */
    private void SharedDialog(){
        dialog = new SharedDialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        mWeiXin = (ImageView) dialog.findViewById(R.id.weixin);
        mPengYouQuan = (ImageView) dialog.findViewById(R.id.pengyouquan);
        // 外部View 点击关闭dialog
        dialog_finsih = dialog.findViewById(R.id.shared_finish);
        dialog_finsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 取消
        tv_cancel = (TextView) dialog.findViewById(R.id.shared_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 微信
        mWeiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedDes(SHARE_MEDIA.WEIXIN);
                dialog.dismiss();
            }
        });
        // 朋友圈
        mPengYouQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedDes(SHARE_MEDIA.WEIXIN_CIRCLE);
                dialog.dismiss();
            }
        });

    }

    /**
     * 调起微信分享的方法
     * @param share_media // 分享类型
     */
    private void SharedDes(SHARE_MEDIA share_media ){
        MyLog.LogShitou("-----222222传过来的名字+图片url",mDesignerName+"--"+mDesignerImg);
        UMImage image = new UMImage(this.getApplicationContext(), mDesignerImg);
        ShareAction shareAction = new ShareAction(this);
        shareAction.withText("微信分享"); // 显示的内容
        shareAction.withMedia(image);// 显示图片的url
        shareAction.withTitle("设计家作品");// 标题
//        shareAction.withTargetUrl(mShare_url); // 分享后点击查看的地址url
        shareAction.setPlatform(share_media); // 分享类型
        shareAction.setCallback(this);// 回调监听
        shareAction.share();
    }


    /**
     * 友盟微信分享回调 (成功，失败，取消)
     * @param share_media 分享类型
     */
    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Toast.makeText(DesignerH5DetailActivity.this, "已分享", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享11", "" + share_media);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//        Toast.makeText(DesignerH5DetailActivity.this, " 分享失败" , Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享22", share_media + "----" + throwable.getMessage());

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
//        Toast.makeText(DesignerH5DetailActivity.this, " 分享取消了", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享33", share_media + "");
    }


}
