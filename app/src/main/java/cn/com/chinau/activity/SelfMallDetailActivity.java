package cn.com.chinau.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.HomeViewPagerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.StampDetailBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 商城详情页
 */
public class SelfMallDetailActivity extends BaseActivity implements View.OnClickListener {


    private View mSelfMallDetailTitle;
    private View mSelfMallDetailContent;
    private TextView mTitle;//市场价
    private ImageView mBack, mShared;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String[] arrImage = {"http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
            "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};
    private String mGoods_sn;
    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 商城详情数据

                    break;
            }
        }
    };
    @Override
    public View CreateTitle() {
        mSelfMallDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mSelfMallDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mSelfMallDetailContent = View.inflate(this, R.layout.activity_selfmalldetail_content, null);
        initView();
//        initData();
        initAdapter();
        initListener();
        return mSelfMallDetailContent;
    }

    private void initView() {
        // 获取商城页面传过来的的值
        Bundle bundle = getIntent().getExtras();
//        mGoods_sn = bundle.getString(StaticField.GOODS_SN);

        mBack = (ImageView) mSelfMallDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mSelfMallDetailTitle.findViewById(R.id.base_title);
        mTitle.setText("庚申年");
        mShared = (ImageView) mSelfMallDetailTitle.findViewById(R.id.base_shared);

        //轮播条的View
        mTopVP = (ViewPager) mSelfMallDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mSelfMallDetailContent.findViewById(R.id.base_viewpagerIndicator);
    }

    private void initAdapter() {
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);

    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mShared.setOnClickListener(this);
    }

    /**
     * 商城详情网络数据
     */
    private void initData() {
        RequestNet();
    }

    /**
     * 商品商城详情网络请求
     */
    private void RequestNet() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSDETAIL);// 接口名称
                params.put(StaticField.GOODS_SN, mGoods_sn); // 商品编号
                String token = "";
                String user_id = "";
                if (!(token.equals("") || user_id.equals(""))) {
                    params.put(StaticField.TOKEN, token); // 登录标识
                    params.put(StaticField.USER_ID, user_id); // 用户ID
                }
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                Log.e("商城详情~~~~>", result);
                if (result.equals("-1")) {
                    return;
                }
                Gson gson = new Gson();
                StampDetailBean mStampDetailBean = gson.fromJson(result, StampDetailBean.class);
                Log.e("商城详情mStampDetailBean-->", mStampDetailBean + "");
                String mImage = mStampDetailBean.getGoods_images();

//                Message msg = mHandler.obtainMessage();
//                msg.what = StaticField.SUCCESS;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_shared://分享按钮
                openActivity(SharedActivity.class);
                break;
        }
    }
}
