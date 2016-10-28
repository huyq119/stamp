package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.HashMap;

import cn.com.chinau.MainActivity;
import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.HomeViewPagerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.AddShopCartBean;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.StampDetailBean;
import cn.com.chinau.dialog.SharedDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 商城详情页
 */
public class SelfMallDetailActivity extends BaseActivity implements View.OnClickListener ,UMShareListener{

    private View mSelfMallDetailTitle;
    private View mSelfMallDetailContent;
    private TextView mTitle, mGoodsName, mCurrentPrice, mMarketPrice, mFreight, mSaleCount, mGoodsSource,
            mGoodsStatus, mAddShopping, mGoBuy, mShoppingCount,tv_cancel;

    private ImageView mBack, mShared, mCollection, mShopping,mWeiXin,mPengYouQuan;
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private String mGoods_sn, mGoodsDetai, mToken, mUser_id, mIsFavorite;
    private String[] small_images, big_images;
    private WebView mWeb; // 邮票信息url
    private LinearLayout mShoppingLl; // 购物车

    private String mSharedImage;
    private String mShare_url;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 商城详情数据
                    Gson gson = new Gson();
                    StampDetailBean mStampDetailBean = gson.fromJson((String) msg.obj, StampDetailBean.class);
                    String mCode = mStampDetailBean.getRsp_code();
                    if (mCode.equals("0000")) {
                        // 赋值头布局显示的图片
                        if (mStampDetailBean.getGoods_images() != null) {
                            String[] mGoods_images = mStampDetailBean.getGoods_images();
                            small_images = new String[mGoods_images.length];
                            big_images = new String[mGoods_images.length];
                            for (int i = 0; i < mGoods_images.length; i++) {
                                String[] image = mGoods_images[i].split(",");
                                small_images[i] = image[0];// 小图集合
                                big_images[i] = image[1];// 大图集合
                            }
                            mSharedImage = small_images[0];
                            MyLog.LogShitou("需要分享显示图片url", mSharedImage);
                        }

                        mGoods_name = mStampDetailBean.getGoods_name();
                        mTitle.setText(mGoods_name); // 标题
                        mGoodsName.setText(mGoods_name);// 商品名称
                        String mPrice = mStampDetailBean.getCurrent_price();
                        mCurrentPrice.setText("￥" + mPrice);// 售价
                        String mMarketPrices = mStampDetailBean.getMarket_price();
                        mMarketPrice.setText("￥" + mMarketPrices);// 市场价
                        String mSaleCounts = mStampDetailBean.getSale_count();
                        mSaleCount.setText(mSaleCounts);// 销量

                        String mGoodsSources = mStampDetailBean.getGoods_source();
                        MyLog.LogShitou("邮票类型-->:", mGoodsSources);
                        if (mGoodsSources.equals("YS")) {
                            mGoodsSource.setText("邮市");
                        } else if (mGoodsSources.equals("JP")) {
                            mGoodsSource.setText("竞拍");
                        } else if (mGoodsSources.equals("SC_ZY")) {
                            mGoodsSource.setText("自营");
                        } else if (mGoodsSources.equals("SC_DSF")) {
                            mGoodsSource.setText("第三方");
                        }
                        mShare_url = mStampDetailBean.getShare_url(); // 分享地址url
                        mGoodsDetai = mStampDetailBean.getGoods_detail();  // 商家信息H5url
                        MyLog.LogShitou("商城详情H5url-->:", mGoodsDetai);
                        if (mGoodsDetai != null) {
                            if (mWeb != null) {
                                mWeb.loadUrl(mGoodsDetai);
                            }
                        }
                        String mGoodsStatuss = mStampDetailBean.getGoods_status();// 商品状态
                        MyLog.LogShitou("商品状态-->:", mGoodsStatuss);
                        if (mGoodsStatuss.equals("0")) {
                            mGoodsStatus.setVisibility(View.VISIBLE);
                            mGoodsStatus.setText("已下架");
                            // 加入购物车，立即购买按钮背景，字体置灰，不可点击
                            mAddShopping.setBackgroundColor(getResources().getColor(R.color.gary));
                            mGoBuy.setBackgroundColor(getResources().getColor(R.color.gary));
                            mAddShopping.setTextColor(getResources().getColor(R.color.font));
                            mGoBuy.setTextColor(getResources().getColor(R.color.font));
                            mAddShopping.setEnabled(false);
                            mGoBuy.setEnabled(false);
                        }
                        mIsFavorite = mStampDetailBean.getIs_favorite();// 收藏状态
                        MyLog.LogShitou("商品收藏状态-->:", mIsFavorite);
                        if (mIsFavorite.equals("0")) { // 未收藏
                            mCollection.setImageResource(R.mipmap.collection);
                        } else if (mIsFavorite.equals("1")) { // 已收藏
                            mCollection.setImageResource(R.mipmap.collections);
                        }

                        String mCartGoodsCount = mStampDetailBean.getCart_goods_count();// 购物车里的商品数量
                        MyLog.LogShitou("商品数量-->:", mCartGoodsCount);
                        if (!mCartGoodsCount.equals("0")&& !mCartGoodsCount.equals("")) {
                            mShoppingCount.setVisibility(View.VISIBLE);
                            mShoppingCount.setText("+"+mCartGoodsCount);
                            MyLog.LogShitou("商品数量---002>","走了吗。。。。。。。。。。");
                        }

                        initAdapter();
                    }

                    break;
                case StaticField.ADDSUCCESS:// 收藏成功
                    Gson gsons = new Gson();
                    BaseBean mBaseBean = gsons.fromJson((String) msg.obj, BaseBean.class);
                    String mRsp_code = mBaseBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mCollection.setImageResource(R.mipmap.collections);
                        initData();// 再次详情请求,获取收藏状态
                    }
                    break;
                case StaticField.DeleteSUCCESS:// 取消收藏成功
                    Gson gsonss = new Gson();
                    BaseBean mBaseBeans = gsonss.fromJson((String) msg.obj, BaseBean.class);
                    String mRsp_codes = mBaseBeans.getRsp_code();
                    if (mRsp_codes.equals("0000")) {
                        mCollection.setImageResource(R.mipmap.collection);
                        initData(); // 再次详情请求,获取收藏状态
                    }
                    break;
                case StaticField.ADDSHOPPINGCARTSUCCESS:
                    Gson gsones = new Gson();
                    BaseBean mBasebean = gsones.fromJson((String) msg.obj,BaseBean.class);
                    if (mBasebean.getRsp_code().equals("0000")){
                        String mCount = mShoppingCount.getText().toString().trim();
                        String count = mCount.substring(1);// 去掉"+"号
                        int mShopCount=Integer.valueOf(count).intValue();// 转int
                        MyLog.LogShitou("请求下来的数量","--->:"+mCount+"--"+mShopCount);
                        mShopCount++; // 数量自增
                        mShoppingCount.setVisibility(View.VISIBLE);// 加入购物车成功后显示数量
                        mShoppingCount.setText("+" + String.valueOf(mShopCount));
                        MyLog.LogShitou("加入后的数量","--->:"+mShopCount);
                    }
                    break ;
            }
        }
    };
    private SharedPreferences sp;
    private String mGoods_name;
    private SharedDialog dialog;
    private View dialog_finsih;


    @Override
    public View CreateTitle() {
        mSelfMallDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mSelfMallDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mSelfMallDetailContent = View.inflate(this, R.layout.activity_selfmalldetail_content, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mSelfMallDetailContent;
    }

    @Override
    public void AgainRequest() {

    }



    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        // 获取商城页面传过来的的值
        Bundle bundle = getIntent().getExtras();
        mGoods_sn = bundle.getString(StaticField.GOODS_SN);

        mBack = (ImageView) mSelfMallDetailTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mSelfMallDetailTitle.findViewById(R.id.base_title);
        mShared = (ImageView) mSelfMallDetailTitle.findViewById(R.id.base_shared);


        mGoodsName = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_detail_goods_name);// 邮票名称
        mCurrentPrice = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_detail_current_price);// 售价
        mMarketPrice = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_detail_market_price);// 市场价
        mFreight = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_detail_freight);// 运费
        mSaleCount = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_sale_count);// 销量
        mGoodsSource = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_detail_goods_source);// 邮票类型
        mWeb = (WebView) mSelfMallDetailContent.findViewById(R.id.mall_detail_web);// 邮票信息url
        mGoodsStatus = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_deatail_goods_status);// 是否下架
        mCollection = (ImageView) mSelfMallDetailContent.findViewById(R.id.mall_detail_collection);// 收藏
        mShoppingLl = (LinearLayout) mSelfMallDetailContent.findViewById(R.id.mall_details_shopping_ll);// 购物车ll
        mShoppingCount = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_details_shopping_count);// 购物车数量
        mShoppingCount.setText("+0");
        mAddShopping = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_detail_addshopping);// 加入购物车
        mGoBuy = (TextView) mSelfMallDetailContent.findViewById(R.id.mall_detail_gobuy);// 立即购买


        //轮播条的View
        mTopVP = (ViewPager) mSelfMallDetailContent.findViewById(R.id.base_viewpager);
        mTopVPI = (CirclePageIndicator) mSelfMallDetailContent.findViewById(R.id.base_viewpagerIndicator);
    }

    private void initAdapter() {
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, small_images, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);

    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mShared.setOnClickListener(this);
        mShoppingLl.setOnClickListener(this);// 点击购物车
        mCollection.setOnClickListener(this);
        mAddShopping.setOnClickListener(this);
        mGoBuy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_shared://分享按钮
            SharedDialog(); // 分享弹出Dialog
                break;
            case R.id.mall_detail_collection://收藏

                if (mToken.equals("") || mUser_id.equals("")) {
                    openActivityWitchAnimation(LoginActivity.class);
                } else {

                    if (mIsFavorite.equals("0")) {
                        DeleteGetInitNet(StaticField.JR);// 修改收藏网络请求
                    } else if (mIsFavorite.equals("1")) {
                        DeleteGetInitNet(StaticField.SC);
                    }
                }
                break;
            case R.id.mall_details_shopping_ll://购物车

                Intent intent = new Intent(SelfMallDetailActivity.this, MainActivity.class);
                intent.putExtra("Login", "SelfMallDetail");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;

            case R.id.mall_detail_addshopping://加入购物车
                if (mToken.equals("") || mUser_id.equals("")) {
                    openActivityWitchAnimation(LoginActivity.class);
                } else {
                    AddShopCartBean mAddShopCartBean = new AddShopCartBean();
                    mAddShopCartBean.setGoods_count("1");
                    mAddShopCartBean.setGoods_sn(mGoods_sn);
                    String mSnJson = new Gson().toJson(mAddShopCartBean); // 转json串
                    String toJson = "["+mSnJson+"]"; // 转数组
                    AddShopCart(toJson); // 加入购物车网络请求
                    MyLog.LogShitou("加入购物车转换的Json",toJson);

                }
                break;
            case R.id.mall_detail_gobuy://立即购买
                Intent intents = new Intent(SelfMallDetailActivity.this, MainActivity.class);
                intents.putExtra("Login", "SelfMallDetail");
                startActivity(intents);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                MyToast.showShort(this, "点击了立即购买");
                break;
        }
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

                if (!mToken.equals("") || !mUser_id.equals("")) {
                    params.put(StaticField.TOKEN, mToken); // 登录标识
                    params.put(StaticField.USER_ID, mUser_id); // 用户ID
                    MyLog.LogShitou("标识与用户id-->:", mToken + "-" + mUser_id);
                }
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                Log.e("商城详情~~~~>", result);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 修改收藏夹网络请求
     *
     * @param op_type 操作类型：SC删除,JR加入
     */
    private void DeleteGetInitNet(final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.FAVORITEMODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODS_SN, mGoods_sn);//  邮票编号
                params.put(StaticField.OP_TYPE, op_type);// 操作类型

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result收藏修改-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                if (op_type.equals("JR")) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.ADDSUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.DeleteSUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }

            }
        });
    }

    /**
     * 加入购物车网络请求
     * @param Goods_info  商品信息：所有商品的json字符串
     */
    private void AddShopCart(final String Goods_info) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SHOPCARTMODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODS_SN, mGoods_sn);//  邮票编号
                params.put(StaticField.GOODESINFO, Goods_info);//  商品信息：所有商品的json字符串
                params.put(StaticField.OP_TYPE, StaticField.JR);// 操作类型：SC删除；JR加入；XG修改
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result加入购物车", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.ADDSHOPPINGCARTSUCCESS;
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
        UMImage image = new UMImage(this.getApplicationContext(), mSharedImage);
        ShareAction shareAction = new ShareAction(this);
        shareAction.withText("微信分享"); // 显示的内容
        shareAction.withMedia(image);// 显示图片的url
        shareAction.withTitle(mGoods_name);// 标题
        shareAction.withTargetUrl(mShare_url); // 分享后点击查看的地址url
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
        Toast.makeText(SelfMallDetailActivity.this, "已分享", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享11", "" + share_media);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//        Toast.makeText(SelfMallDetailActivity.this, " 分享失败" , Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享22", share_media + "----" + throwable.getMessage());

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
//        Toast.makeText(SelfMallDetailActivity.this, " 分享取消了", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享33", share_media + "");
    }

}
