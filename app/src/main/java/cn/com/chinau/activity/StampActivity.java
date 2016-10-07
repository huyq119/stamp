package cn.com.chinau.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.StampHorizontalListViewAdapter;
import cn.com.chinau.adapter.StampMarketGridViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.CategoryBean;
import cn.com.chinau.bean.GoodsStampBean;
import cn.com.chinau.bitmap.BitmapHelper;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.listener.GestureListener;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.HorizontalListView;

/**
 * 邮市页面
 */
public class StampActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {

    private View mStampTitle, mStampContent, mBlankView;
    private HorizontalListView hListView;//横向滑动的listView
    private StampHorizontalListViewAdapter hListViewAdapter;//横向花的listView的适配器
    private ArrayList<GoodsStampBean.GoodsList> mList;
    private GridView mGridView;
    private ImageView mBack, mSearch; // 返回按钮,搜索
    private TextView mTitle;
    // 新中国邮票, 民国邮票, 解放区邮票, 清代邮票
    private RadioButton mNewChinese, mRepublicChina, mLiberatedArea, mQingDynasty;
    private LinearLayout mTitleStamp;

    private int mColorGray = Color.parseColor("#dddfe3");// 横线灰色
    private int mColorRed = Color.parseColor("#e20000");// 横线红色
    private Button mTopBtn, mSynthesize, mSales, mPrice;// 置顶，综合，销量，价格，筛选
    private boolean Synthesizeflag;// 综合的标记->升序还是降序false升序,true是降序
    private boolean Salesflag;// 销量的标记->升序还是降序false升序,true是降序
    private boolean Priceflag;// 价格的标记->升序还是降序false升序,true是降序

    private boolean scrollFlag = false; // 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private StampMarketGridViewAdapter mStampMarAdapter;
    private LinearLayout mHeartll;//头部的布局
    private GestureDetector mGestureDetector;
    private int num = 0;//初始索引
    private GoodsStampBean mGoodsStampBean;
    public static final int SUCCESS = 1;
    private RadioGroup mRadioGroup;

    private String[] string0, string1, string2, string3;

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case StaticField.SUCCESS://套邮票Lsit
                    Gson gson = new Gson();
                    mGoodsStampBean = gson.fromJson((String) msg.obj, GoodsStampBean.class);
                    String mRsp_msg = mGoodsStampBean.getRsp_code();
                    if (mRsp_msg.equals("0000")) {
                        mList = mGoodsStampBean.getGoods_list();
                        if (mList != null && mList.size() != 0) {
                            //竖向ListView设置适配器
                            initAdapter();
                        }
                    }
                    break;
                case SUCCESS://类别查询

                    String[] mArrTitle = (String[]) msg.obj;
                    String title0 = mArrTitle[0];
                    mNewChinese.setText(title0);
                    String title1 = mArrTitle[1];
                    mRepublicChina.setText(title1);
                    String title2 = mArrTitle[2];
                    mLiberatedArea.setText(title2);
                    String title3 = mArrTitle[3];
                    mQingDynasty.setText(title3);
                    MyLog.LogShitou("邮市一级类别----->:", title0 + "--" + title1 + "--" + title2 + "--" + title3);
                    break;
                case 2:
                    Drawable mTopDrawable0 = (Drawable) msg.obj;
                    mNewChinese.setCompoundDrawables(null, mTopDrawable0, null, null);
                    break;
                case 3:
                    Drawable mTopDrawable1 = (Drawable) msg.obj;
                    mRepublicChina.setCompoundDrawables(null, mTopDrawable1, null, null);
                    break;
                case 4:
                    Drawable mTopDrawable2 = (Drawable) msg.obj;
                    mLiberatedArea.setCompoundDrawables(null, mTopDrawable2, null, null);
                    break;
                case 5:
                    Drawable mTopDrawable3 = (Drawable) msg.obj;
                    mQingDynasty.setCompoundDrawables(null, mTopDrawable3, null, null);
                    break;
                case 6:// 二级类别名字
                    List<String[]> mArrList = (List<String[]>) msg.obj;
                    // 获取
                    string0 = mArrList.get(0);
                    string1 = mArrList.get(1);
                    string2 = mArrList.get(2);
                    string3 = mArrList.get(3);

                    // 横向的listView设置适配器
                    hListViewAdapter = new StampHorizontalListViewAdapter(StampActivity.this, string0);
                    hListView.setAdapter(hListViewAdapter);
                    MyLog.LogShitou("邮市二级分类名字的个数-->:", mArrList.size() + "--" + string0.length + "----" + string1.length + "--" + string2.length + "--" + string3.length);

                    break;

                case 7:
                    Drawable TopDrawable0 = (Drawable) msg.obj;
                    mNewChinese.setCompoundDrawables(null, TopDrawable0, null, null);
                    break;
                case 8:
                    Drawable TopDrawable1 = (Drawable) msg.obj;
                    mRepublicChina.setCompoundDrawables(null, TopDrawable1, null, null);
                    break;
                case 9:
                    Drawable TopDrawable2 = (Drawable) msg.obj;
                    mLiberatedArea.setCompoundDrawables(null, TopDrawable2, null, null);
                    break;
                case 10:
                    Drawable TopDrawable3 = (Drawable) msg.obj;
                    mQingDynasty.setCompoundDrawables(null, TopDrawable3, null, null);
                    break;
                case 11:
                    MyToast.showShort(StampActivity.this, "请检查网络连接");
                    break;
                case 12:
                    MyToast.showShort(StampActivity.this, "请检查网络连接");
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public View CreateTitle() {
        mStampTitle = View.inflate(this, R.layout.base_search_title, null);
        return mStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mStampContent = View.inflate(this, R.layout.activity_stamp_content, null);
        initView();
        initData();
        initListener();
        return mStampContent;
    }


    private void initView() {
        mBack = (ImageView) mStampTitle.findViewById(R.id.base_title_back);
        mSearch = (ImageView) mStampTitle.findViewById(R.id.base_search);
        hListView = (HorizontalListView) mStampContent.findViewById(R.id.stamp_hl);
        mGridView = (GridView) mStampContent.findViewById(R.id.stamp_gl);

        mTopBtn = (Button) mStampContent.findViewById(R.id.base_top_btn);// 置顶

        hListView = (HorizontalListView) mStampContent.findViewById(R.id.stamp_hl);//横向ListView

        mRadioGroup = (RadioGroup) mStampContent.findViewById(R.id.stamp_RadioGroup);
        mNewChinese = (RadioButton) mStampContent.findViewById(R.id.stamp_newchinese_btn);
        mRepublicChina = (RadioButton) mStampContent.findViewById(R.id.stamp_republicChina_btn);
        mLiberatedArea = (RadioButton) mStampContent.findViewById(R.id.stamp_liberatedArea_btn);
        mQingDynasty = (RadioButton) mStampContent.findViewById(R.id.stamp_qingDynasty_btn);
        mRadioGroup.check(R.id.stamp_newchinese_btn);

        mSynthesize = (Button) mStampContent.findViewById(R.id.stamp_synthesize);
        mSales = (Button) mStampContent.findViewById(R.id.stamp_sales);
        mPrice = (Button) mStampContent.findViewById(R.id.stamp_price);

        initGestureListener(); // 滑动lsitview隐藏导航栏的方法
    }

//    private Drawable loadImageFromNetwork(String imageUrl) {
//        Drawable drawable = null;
//        try {
//            // 可以在这里通过文件名来判断，是否本地有此图片
//            drawable = Drawable.createFromStream(
//                    new URL(imageUrl).openStream(), "image.png");
//        } catch (IOException e) {
//            Log.d("test", e.getMessage());
//        }
//        if (drawable == null) {
//            Log.d("test", "null drawable");
//        } else {
//            Log.d("test", "not null drawable");
//        }
//
//        return drawable;
//    }

    /**
     * 滑动lsitview隐藏导航栏的方法
     */
    private void initGestureListener() {
        mHeartll = (LinearLayout) mStampContent.findViewById(R.id.stamp_heart);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mHeartll.measure(w, h);
        int height = mHeartll.getMeasuredHeight();
        MyLog.e(height + "");
        GestureListener gestureListener = new GestureListener(mHeartll, height);
        mGestureDetector = new GestureDetector(this, gestureListener);
    }

    private void initAdapter() {
        //内容GridView设置适配器
        mStampMarAdapter = new StampMarketGridViewAdapter(this, mList, mBitmap);
        mGridView.setAdapter(mStampMarAdapter);
        mStampMarAdapter.notifyDataSetChanged();
    }

    /**
     * 添加网络数据
     */
    private void initData() {
        GetInitCategory();// 类别查询网络请求
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num, StaticField.A);
    }

    private void initListener() {
        mTopBtn.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);

        mNewChinese.setOnClickListener(this);
        mRepublicChina.setOnClickListener(this);
        mLiberatedArea.setOnClickListener(this);
        mQingDynasty.setOnClickListener(this);

        mSynthesize.setOnClickListener(this);
        mSales.setOnClickListener(this);
        mPrice.setOnClickListener(this);

        mGridView.setOnScrollListener(this);
        //横向ListView的点击事件
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //设置点击背景的方法
                hListViewAdapter.setSelection(i);
                hListViewAdapter.notifyDataSetChanged();
                MyToast.showShort(StampActivity.this, "点击二级分类的名字--->:" + i);
            }
        });

        //GridView的条目点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //去往邮市详情页
                Bundle bundle = new Bundle();
                String mGoods_sn = mGoodsStampBean.getGoods_list().get(i).getGoods_sn();
                bundle.putString(StaticField.GOODS_SN, mGoods_sn);// 传入商品编号
                openActivityWitchAnimation(StampDetailActivity.class, bundle);
            }
        });
        mGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

    }


    @Override
    public void AgainRequest() {
        initData();
    }


    /**
     * 邮市类别查询网络请求
     */
    private void GetInitCategory() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
                params.put(StaticField.OP_TYPE, "YS");// 邮市
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("邮市类别查询--->", result);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Gson gsons = new Gson();
                CategoryBean mCategoryBean = gsons.fromJson(result, CategoryBean.class);
                String mRsp_code = mCategoryBean.getRsp_code();
                // 判断code是否返回0000
                if (mRsp_code.equals("0000")) {
                    ArrayList<CategoryBean.Category> mCategory = mCategoryBean.getCategory();
                    CategoryBean.Category category = mCategory.get(0);
                    ArrayList<CategoryBean.Category.SubCategory> subCategory1 = category.getSubCategory();

                    // 外分类
                    int sub = subCategory1.size();// 获取subCategory1的个数
                    String[] mArrTitle = new String[sub];// 一级分类
                    String[] mImgUrl = new String[sub];// url
                    Message msge = mHandler.obtainMessage();
                    msge.what = SUCCESS;
                    msge.obj = mArrTitle;
                    mHandler.sendMessage(msge);

                    //子分类
                    List<String[]> mArrList = new ArrayList<>();
                    List<String[]> mImgUrlList = new ArrayList<>();
                    // 循环出一级分类的名字
                    for (int i = 0; i < subCategory1.size(); i++) {
                        mArrTitle[i] = subCategory1.get(i).getName();
                        mImgUrl[i] = subCategory1.get(i).getImg_url();// 图片的url
                        MyLog.LogShitou("邮市一级类别0001----->:", mArrTitle[i] + "--" + mImgUrl[i]);
                        ArrayList<CategoryBean.Category.SubCategory.SmllSubCategoryData> subCategory = subCategory1.get(i).getSubCategory();
                        String[] mArr = new String[subCategory.size()];
                        // 循环出二级分类名字
                        for (int j = 0; j < subCategory.size(); j++) {
                            mArr[j] = subCategory.get(j).getName();
//                        MyLog.LogShitou("竞拍二级类别0" + j + "----->:", mArr[j]);
                        }
                        mArrList.add(mArr);
                    }

//                String url = "http://test.chinau.com.cn:8081/chinau-imgserver/attachment//designer/20160429/20160429081b712e-ed1a-4341-839f-af6571109a03.png";
                    // 获取单个的url
                    String imgurl0 = mImgUrl[0];
                    String imgurl1 = mImgUrl[1];
                    String imgurl2 = mImgUrl[2];
                    String imgurl3 = mImgUrl[3];

                    Drawable drawable = getResources().getDrawable(R.mipmap.weixin); // 如个url为空，设置默认图片
                    // 判断url是否为空
                    if (imgurl0.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

                        Message msg = mHandler.obtainMessage();
                        msg.what = 7;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);

                    } else {

                        Drawable topDrawable0 = BitmapHelper.getDrawable(imgurl0);
                        topDrawable0.setBounds(0, 0, topDrawable0.getMinimumWidth(), topDrawable0.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 2;
                        msg.obj = topDrawable0;
                        mHandler.sendMessage(msg);
                    }
                    if (imgurl1.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 8;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);

                    } else {

                        Drawable topDrawable1 = BitmapHelper.getDrawable(imgurl1);
                        topDrawable1.setBounds(0, 0, topDrawable1.getMinimumWidth(), topDrawable1.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 3;
                        msg.obj = topDrawable1;
                        mHandler.sendMessage(msg);
                    }
                    if (imgurl2.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 9;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);
                    } else {

                        Drawable topDrawable2 = BitmapHelper.getDrawable(imgurl2);
                        topDrawable2.setBounds(0, 0, topDrawable2.getMinimumWidth(), topDrawable2.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 4;
                        msg.obj = topDrawable2;
                        mHandler.sendMessage(msg);
                    }
                    if (imgurl3.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 10;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);

                    } else {

                        Drawable topDrawable3 = BitmapHelper.getDrawable(imgurl3);
                        topDrawable3.setBounds(0, 0, topDrawable3.getMinimumWidth(), topDrawable3.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 5;
                        msg.obj = topDrawable3;
                        mHandler.sendMessage(msg);
                    }


                    // 发送二级分类名字
                    Message msg = mHandler.obtainMessage();
                    msg.what = 6;
                    msg.obj = mArrList;
                    mHandler.sendMessage(msg);

                } else {
                    mHandler.sendEmptyMessage(11);
                }


            }
        });
    }


    /**
     * 邮市list网络请求
     *
     * @param Order_By 类别
     * @param index    角标
     * @param Sort     排序
     */
    private void RequestNet(final String Order_By, final int index, final String Sort) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSLIST);// 接口名称
                params.put(StaticField.CURRENT_INDEX, String.valueOf(index)); // 当前记录索引
                params.put(StaticField.GOODS_SOURCE, StaticField.YS); // 商品类型(SC_ZY,SC_DSF,YS,JP)
                params.put(StaticField.ORDER_BY, Order_By); // 排序条件(排序的维度：ZH综合；XL销量；JG价格)
                params.put(StaticField.SORT_TYPE, Sort); // 排序方式(A：升序；D：降序)
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code); // 签名

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("邮市List~~~~>", result);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_search://搜索按钮
                openActivityWitchAnimation(SearchActivity.class);
                break;
            case R.id.stamp_newchinese_btn://新中国邮票

                hListViewAdapter = new StampHorizontalListViewAdapter(this, string0);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();

                break;
            case R.id.stamp_republicChina_btn:// 民国邮票

                hListViewAdapter = new StampHorizontalListViewAdapter(this, string1);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.stamp_liberatedArea_btn://解放区邮票
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string2);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();

                break;
            case R.id.stamp_qingDynasty_btn://清代邮票
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string3);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.base_top_btn://置顶
                setListViewPos(0);
                mTopBtn.setVisibility(View.GONE);// 回到顶部后置顶按钮隐藏
                break;
            case R.id.stamp_synthesize://综合
                setOtherButton(mSales, mPrice);// (销量，价格)
                Salesflag = true;
                Priceflag = true;
                // true代表降序,false代表升序
                if (Synthesizeflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.D);
                    Synthesizeflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.A);
                    Synthesizeflag = true;
                }
                break;
            case R.id.stamp_sales://销量
                setOtherButton(mSynthesize, mPrice);// (综合，价格)
                Synthesizeflag = true;
                Priceflag = true;

                if (Salesflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num, StaticField.D);
                    Salesflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num, StaticField.A);
                    Salesflag = true;
                }
                break;
            case R.id.stamp_price://价格
                setOtherButton(mSynthesize, mSales);// (综合，销量)
                Synthesizeflag = true;
                Salesflag = true;

                if (Priceflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.D);
                    Priceflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.A);
                    Priceflag = true;
                }
                break;

        }
    }


    /**
     * 竖向GridView滑动状态改变监听的方法
     */

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        switch (i) {
            // 当不滚动时
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                scrollFlag = false;
                // 判断滚动到底部
                if (mGridView.getLastVisiblePosition() == (mGridView
                        .getCount() - 1)) {
                    mTopBtn.setVisibility(View.VISIBLE);
                }
                // 判断滚动到顶部
                if (mGridView.getFirstVisiblePosition() == 0) {
                    mTopBtn.setVisibility(View.GONE);
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                scrollFlag = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                scrollFlag = false;
                break;
        }

    }

    /**
     * 置顶按钮显示的方法
     *
     * @param absListView
     * @param firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
     * @param visibleItemCount 当前能看见的列表项个数（小半个也算）
     * @param totalItemCount   totalItemCount：列表项共数
     */
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 当开始滑动且GridView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
        mCount = totalItemCount;

        if (scrollFlag
                && ScreenUtils.getScreenViewBottomHeight(mGridView) >= ScreenUtils
                .getScreenHeight(this)) {
            if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                mTopBtn.setVisibility(View.VISIBLE);
            } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                mTopBtn.setVisibility(View.GONE);
            } else {
                return;
            }


            lastVisibleItemPosition = firstVisibleItem;
        }
    }


    /**
     * 滚动GridView到指定位置
     *
     * @param pos
     */

    private void setListViewPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= mCount) {
            mGridView.smoothScrollToPosition(pos);
        } else {
            mGridView.setSelection(pos);
        }
    }

    /**
     * 设置其他button的方法
     *
     * @param btn1
     * @param btn2
     */
    private void setOtherButton(Button btn1, Button btn2) {
        setDrawable(R.mipmap.top_arrow_normal, btn1, Color.parseColor("#666666"));
        setDrawable(R.mipmap.top_arrow_normal, btn2, Color.parseColor("#666666"));
    }

    /**
     * 设置按钮
     */
    private void setDrawable(int ID, Button button, int color) {
        Drawable drawable = getResources().getDrawable(ID);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        button.setCompoundDrawables(null, null, drawable, null);
        button.setTextColor(color);
    }


}
