package cn.com.chinau.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.AuctionListViewAdapter;
import cn.com.chinau.adapter.StampHorizontalListViewAdapter;
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
 * 竞拍页面
 */
public class AuctionActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {

    private View mAuctionContent, mAuctionTitle, mBlankView;
    private HorizontalListView hListView;//筛选横向滑动的listView
    private StampHorizontalListViewAdapter hListViewAdapter;//筛选横向花的listView的适配器
    private ArrayList<GoodsStampBean.GoodsList> mList;
    private ListView mListView;
    private ImageView mBack, mSearch;//返回按钮,搜索

    private String[] mArrTitle = {"新中国邮票", "民国邮票", "解放区邮票", "清代邮票"};
    private String[] mArr = {"全部", "编年邮票", "新JT邮票", "编号邮票", "文革邮票", "老纪特邮票", "普通邮票"};
    private TextView mTitle;
    // 新中国邮票, 民国邮票, 解放区邮票, 清代邮票
    private RadioButton mNewChinese, mRepublicChina, mLiberatedArea, mQingDynasty;

    private int mColorGray = Color.parseColor("#dddfe3");// 横线灰色
    private int mColorRed = Color.parseColor("#e20000");// 横线红色
    private Button mTopBtn, mSynthesize, mOver, mCamera;// 置顶，综合，即将结束，等待开拍
    private boolean Synthesizeflag;// 综合的标记->升序还是降序false升序,true是降序
    private boolean Overflag;// 即将结束的标记->升序还是降序false升序,true是降序
    private boolean Cameraflag;// 等待开拍的标记->升序还是降序false升序,true是降序

    private boolean scrollFlag = false; // 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private LinearLayout mHeartll;//头部的布局
    private GestureDetector mGestureDetector;
    private int num = 0;//初始索引
    public static final int SUCCESS = 1;
    private GoodsStampBean mGoodsStampBean;
    private String[] string0, string2, string3, string4;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case StaticField.SUCCESS://竞拍Lsit
                    Gson gson = new Gson();
                    mGoodsStampBean = gson.fromJson((String) msg.obj, GoodsStampBean.class);
                   String mRsp_msg =  mGoodsStampBean.getRsp_code();
                    if (mRsp_msg.equals("0000")){

                        mList = mGoodsStampBean.getGoods_list();
                        MyLog.LogShitou("竞拍列表有几条-->:", mList.size() + "");
                        if (mList != null && mList.size() != 0) {
                            //竖向ListView设置适配器
                            initAdapter();
                        }
                    }else{
                        MyToast.showShort(AuctionActivity.this,"请检查网络连接");
                    }
                    break;
                case SUCCESS://一级类别名字

                    String[] mArrTitle = (String[] ) msg.obj;
                    String title0 = mArrTitle[0];
                    mNewChinese.setText(title0);
                    String title2 = mArrTitle[2];
                    mRepublicChina.setText(title2);
                    String title3 = mArrTitle[3];
                    mLiberatedArea.setText(title3);
                    String title4 = mArrTitle[4];
                    mQingDynasty.setText(title4);
                    MyLog.LogShitou("竞拍一级类别----->:", title0 + "--" + title2 + "--" + title3 + "--" + title4);
                    break;
                case 2:
                    Drawable mTopDrawable0 = (Drawable) msg.obj;
                    mNewChinese.setCompoundDrawables(null, mTopDrawable0, null, null);
                    break;
                case 3:
                    Drawable mTopDrawable2 = (Drawable) msg.obj;
                    mRepublicChina.setCompoundDrawables(null, mTopDrawable2, null, null);
                    break;
                case 4:
                    Drawable mTopDrawable3 = (Drawable) msg.obj;
                    mLiberatedArea.setCompoundDrawables(null, mTopDrawable3, null, null);
                    break;
                case 5:
                    Drawable mTopDrawable4 = (Drawable) msg.obj;
                    mQingDynasty.setCompoundDrawables(null, mTopDrawable4, null, null);
                    break;
                case 6:// 二级类别名字
                    List<String[]> mArrList = (List<String[]>) msg.obj;
                    // 获取
                    string0 = mArrList.get(0);
                    string2= mArrList.get(2);
                    string3 = mArrList.get(3);
                    string4 = mArrList.get(4);

                // 横向的listView设置适配器
                hListViewAdapter = new StampHorizontalListViewAdapter(AuctionActivity.this, string0);
                hListView.setAdapter(hListViewAdapter);
                MyLog.LogShitou("竞拍几个数-->:", mArrList.size() + "--" + string0.length + "----" + string2.length + "--" + string3.length + "--" + string4.length);

                    break;

                case 7:
                    Drawable TopDrawable0 = (Drawable) msg.obj;
                    mNewChinese.setCompoundDrawables(null, TopDrawable0, null, null);
                    break;
                case 8:
                    Drawable TopDrawable2 = (Drawable) msg.obj;
                    mRepublicChina.setCompoundDrawables(null, TopDrawable2, null, null);
                    break;
                case 9:
                    Drawable TopDrawable3 = (Drawable) msg.obj;
                    mLiberatedArea.setCompoundDrawables(null, TopDrawable3, null, null);
                    break;
                case 10:
                    Drawable TopDrawable4 = (Drawable) msg.obj;
                    mQingDynasty.setCompoundDrawables(null, TopDrawable4, null, null);
                    break;
                case 11:
                    MyToast.showShort(AuctionActivity.this,"请检查网络连接");
                    break;
                default:
                    break;
            }
        }
    };
    private RadioGroup mRadioGroup;

    @Override
    public View CreateTitle() {
        mAuctionTitle = View.inflate(this, R.layout.base_search_title, null);
        return mAuctionTitle;
    }

    @Override
    public View CreateSuccess() {
        mAuctionContent = View.inflate(this, R.layout.activity_auction_content, null);
        initView();
        initData();
        initListener();
        return mAuctionContent;
    }


    private void initView() {
        mBack = (ImageView) mAuctionTitle.findViewById(R.id.base_title_back);
        mSearch = (ImageView) mAuctionTitle.findViewById(R.id.base_search);
        mTitle = (TextView) mAuctionTitle.findViewById(R.id.base_title);
        mTitle.setText("竞拍");
        mTopBtn = (Button) mAuctionContent.findViewById(R.id.base_top_btn);// 置顶
        hListView = (HorizontalListView) mAuctionContent.findViewById(R.id.stamp_hl);//横向ListView
        mListView = (ListView) mAuctionContent.findViewById(R.id.auction_lv);// 竖向ListView
        mRadioGroup = (RadioGroup) mAuctionContent.findViewById(R.id.stamp_RadioGroup);

        mNewChinese = (RadioButton) mAuctionContent.findViewById(R.id.auction_newchinese_btn);
        mRadioGroup.check(R.id.auction_newchinese_btn);
        mRepublicChina = (RadioButton) mAuctionContent.findViewById(R.id.auction_republicChina_btn);
        mLiberatedArea = (RadioButton) mAuctionContent.findViewById(R.id.auction_liberatedArea_btn);
        mQingDynasty = (RadioButton) mAuctionContent.findViewById(R.id.auction_qingDynasty_btn);
        mSynthesize = (Button) mAuctionContent.findViewById(R.id.auction_synthesize);
        mOver = (Button) mAuctionContent.findViewById(R.id.auction_over);
        mCamera = (Button) mAuctionContent.findViewById(R.id.auction_camera);

        initGestureListener();// 滑动lsitview隐藏导航栏的方法

    }

    /**
     * 滑动lsitview隐藏导航栏的方法
     */
    private void initGestureListener() {
        mHeartll = (LinearLayout) mAuctionContent.findViewById(R.id.auction_heart);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mHeartll.measure(w, h);
        int height = mHeartll.getMeasuredHeight();
        MyLog.e(height + "");
        GestureListener gestureListener = new GestureListener(mHeartll, height);
        mGestureDetector = new GestureDetector(this, gestureListener);
    }


    private void initAdapter() {
        //竖向ListView设置适配器
        AuctionListViewAdapter mListAdapter = new AuctionListViewAdapter(AuctionActivity.this, mBitmap, mList);
        mListView.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化第一个按钮
     */
    private void initData() {
        GetInitCategory();
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num, StaticField.A);
    }

    /**
     * 设置监听
     */
    private void initListener() {
        mTopBtn.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mNewChinese.setOnClickListener(this);
        mRepublicChina.setOnClickListener(this);
        mLiberatedArea.setOnClickListener(this);
        mQingDynasty.setOnClickListener(this);
        mSynthesize.setOnClickListener(this);
        mOver.setOnClickListener(this);
        mCamera.setOnClickListener(this);
        mListView.setOnScrollListener(this);

        //横向ListView的点击事件
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //设置点击背景的方法
                hListViewAdapter.setSelection(i);
                hListViewAdapter.notifyDataSetChanged();
                MyToast.showShort(AuctionActivity.this,"点击二级分类的名字--->:"+i);
            }
        });
        // mListView的条目监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //去往竞拍详情页
                Bundle bundle = new Bundle();
                String mGoods_sn = mGoodsStampBean.getGoods_list().get(i).getGoods_sn();
                bundle.putString(StaticField.GOODS_SN, mGoods_sn);// 传入商品编号
                MyLog.LogShitou("mGoods_sn商品编号-->", mGoods_sn);
                openActivityWitchAnimation(AuctionDetailActivity.class, bundle);
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
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
     * 竞拍类别查询网络请求
     */
    private void GetInitCategory() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
                params.put(StaticField.OP_TYPE, "JP");// 竞拍
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("竞拍类别查询--->", result);
                if (result.equals("-1")) {
                    GetInitCategory();
                    return;
                }

                Gson gsons = new Gson();
                CategoryBean mCategoryBean = gsons.fromJson(result, CategoryBean.class);
               String mRsp_code = mCategoryBean.getRsp_code();
                // 判断code是否返回0000
                if(mRsp_code.equals("0000")){
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
                        MyLog.LogShitou("竞拍一级类别0001----->:", mArrTitle[i] + "--" + mImgUrl[i]);
                        ArrayList<CategoryBean.Category.SubCategory.SmllSubCategoryData> subCategory = subCategory1.get(i).getSubCategory();
                        String[] mArr = new String[subCategory.size()];
                        // 循环出二级分类名字
                        for (int j = 0; j < subCategory.size(); j++) {
                            mArr[j] = subCategory.get(j).getName();
//                        MyLog.LogShitou("竞拍二级类别0" + j + "----->:", mArr[j]);
                        }
                        mArrList.add(mArr);
                    }

                    String url = "http://test.chinau.com.cn:8081/chinau-imgserver/attachment//designer/20160429/20160429081b712e-ed1a-4341-839f-af6571109a03.png";
                    // 获取单个的url
                    String imgurl0 = mImgUrl[0];
                    String imgurl2 = mImgUrl[2];
                    String imgurl3 = mImgUrl[3];
                    String imgurl4 = mImgUrl[4];

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
                    if (imgurl2.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 8;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);

                    } else {

                        Drawable topDrawable2 = BitmapHelper.getDrawable(imgurl2);
                        topDrawable2.setBounds(0, 0, topDrawable2.getMinimumWidth(), topDrawable2.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 3;
                        msg.obj = topDrawable2;
                        mHandler.sendMessage(msg);
                    }
                    if (imgurl3.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 9;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);
                    } else {

                        Drawable topDrawable3 = BitmapHelper.getDrawable(imgurl3);
                        topDrawable3.setBounds(0, 0, topDrawable3.getMinimumWidth(), topDrawable3.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 4;
                        msg.obj = topDrawable3;
                        mHandler.sendMessage(msg);
                    }
                    if (imgurl4.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 10;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);

                    } else {

                        Drawable topDrawable4 = BitmapHelper.getDrawable(imgurl4);
                        topDrawable4.setBounds(0, 0, topDrawable4.getMinimumWidth(), topDrawable4.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 5;
                        msg.obj = topDrawable4;
                        mHandler.sendMessage(msg);
                    }

                    // 发送二级分类名字
                    Message msg = mHandler.obtainMessage();
                    msg.what = 6;
                    msg.obj = mArrList;
                    mHandler.sendMessage(msg);
                }else{
                    mHandler.sendEmptyMessage(11);
                }
            }
        });
    }

    /**
     * 商城list网络请求
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
                params.put(StaticField.GOODS_SOURCE, StaticField.JP); // 商品类型
                params.put(StaticField.ORDER_BY, Order_By); // 排序条件(排序的维度：ZH综合；XL销量；JG价格)
                params.put(StaticField.SORT_TYPE, Sort); // 排序方式(A：升序；D：降序)
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code); // 签名

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("result+竞拍~~~~>", result);
                if (result.equals("-1")) {
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
            case R.id.auction_newchinese_btn://新中国邮票
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string0);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.auction_republicChina_btn:// 民国邮票
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string2);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.auction_liberatedArea_btn://解放区邮票
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string3);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.auction_qingDynasty_btn://清代邮票
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string4);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.base_top_btn://置顶
                setListViewPos(0);
                mTopBtn.setVisibility(View.GONE);// 回到顶部后置顶按钮隐藏
                break;
            case R.id.auction_synthesize://综合
                setOtherButton(mOver, mCamera);
                Overflag = true;
                Cameraflag = true;
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
            case R.id.auction_over://即将结束
                setOtherButton(mSynthesize, mCamera);
                Synthesizeflag = true;
                Cameraflag = true;

                if (Overflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mOver, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JS, num, StaticField.D);
                    Overflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mOver, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JS, num, StaticField.A);
                    Overflag = true;
                }
                break;
            case R.id.auction_camera://等待开拍
                setOtherButton(mSynthesize, mOver);
                Synthesizeflag = true;
                Overflag = true;

                if (Cameraflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mCamera, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.KP, num, StaticField.D);
                    Cameraflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mCamera, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.KP, num, StaticField.A);
                    Cameraflag = true;
                }
                break;
        }
    }

    /**
     * 竖向ListView滑动状态改变监听的方法
     */

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
//        MyLog.e("滑动了几条啊~~~>",mListView.getLastVisiblePosition()+"-->"+mListView.getFirstVisiblePosition());
        switch (i) {
            // 当不滚动时
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                scrollFlag = false;
                // 判断滚动到底部
                if (mListView.getLastVisiblePosition() == (mListView
                        .getCount() - 1)) {
                    mTopBtn.setVisibility(View.VISIBLE);
                }
                // 判断滚动到顶部
                if (mListView.getFirstVisiblePosition() == 0) {
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
        mCount = totalItemCount;

        if (scrollFlag
                && ScreenUtils.getScreenViewBottomHeight(mListView) >= ScreenUtils
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
            mListView.smoothScrollToPosition(pos);
        } else {
            mListView.setSelection(pos);
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
