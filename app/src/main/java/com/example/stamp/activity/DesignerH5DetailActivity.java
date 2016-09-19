package com.example.stamp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.DesignerDetailsH5Bean;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/19.
 * (设计故事,艺术作品，名家访谈)H5详情页面
 */
public class DesignerH5DetailActivity extends BaseActivity implements View.OnClickListener {

    private View mStoryDetails;
    private View mStampTapDetailTitle;
    private ImageView mBack;
    private ImageView mShared;
    private TextView mTitle;
    private WebView mWB;
    private String mDetail, mStory_sn, mWorks_sn, mView_sn, mDetailH5, mCategory;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS://详情数据
                    Gson gson = new Gson();
                    String mStr = (String) msg.obj;
                    DesignerDetailsH5Bean mDetailsH5Bean = gson.fromJson(mStr, DesignerDetailsH5Bean.class);
                    mDetailH5 = mDetailsH5Bean.getDetail(); // 获取H5url
                    initSetWebView(mDetailH5);// WEB页面赋值方法
                    break;
            }
        }
    };

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

        mBack = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_title_back);
        mShared = (ImageView) mStampTapDetailTitle.findViewById(R.id.base_shared);
        mTitle = (TextView) mStampTapDetailTitle.findViewById(R.id.base_title);
        mWB = (WebView) mStoryDetails.findViewById(R.id.story_details_web);
        if (mDetail != null) {
            if (mDetail.equals("GS")) {
                mTitle.setText("设计故事");
                initData(mStory_sn, mDetail, null);
            } else if (mDetail.equals("ZP")) {
                mTitle.setText("艺术作品");
                initData(mWorks_sn, mDetail, mCategory);
            } else if (mDetail.equals("FT")) {
                mTitle.setText("名家访谈");
                initData(mView_sn, mDetail, null);
            }

        }
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);

    }
    /**
     * WEB页面赋值方法
     *
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
                openActivity(SharedActivity.class);
                break;
        }
    }

    /**
     * 设计家h5详情网络请求
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

                if (result.equals("-1")) {
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
}
