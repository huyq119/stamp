package com.example.stamp.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.AuctionListViewAdapter;
import com.example.stamp.adapter.StampMarketGridViewAdapter;
import com.example.stamp.adapter.StampHorizontalListViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.GoodsStampBean;
import com.example.stamp.bean.StampBean;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.listener.GestureListener;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.ScreenUtils;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.view.HorizontalListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 邮市页面
 */
public class StampActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {

    private View mStampTitle, mStampContent, mBlankView;
    private HorizontalListView hListView;//横向滑动的listView
    private StampHorizontalListViewAdapter hListViewAdapter;//横向花的listView的适配器
    private ArrayList<GoodsStampBean.GoodsList> mList;
    private GridView mGridView;
    private ImageView mBack, mSearch;//返回按钮,搜索
    private String[] mArrTitle = {"新中国邮票", "民国邮票", "解放区邮票", "清代邮票"};
    private String[] mArr = {"全部", "编年邮票", "新JT邮票", "编号邮票", "文革邮票", "老纪特邮票", "普通邮票"};
    private TextView mTitle;
    // 新中国邮票, 民国邮票, 解放区邮票, 清代邮票
    private LinearLayout mNewChinese, mRepublicChina, mLiberatedArea, mQingDynasty,
            mTitleStamp;

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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case StaticField.SUCCESS://套邮票Lsit
                    Gson gson = new Gson();
                    GoodsStampBean mGoodsStampBean = gson.fromJson((String) msg.obj, GoodsStampBean.class);
                    mList = mGoodsStampBean.getGoods_list();
                    initAdapter();
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

        mNewChinese = (LinearLayout) mStampContent.findViewById(R.id.stamp_newchinese_ll);
        mStampContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorRed);// 初始化新中国邮票
        mRepublicChina = (LinearLayout) mStampContent.findViewById(R.id.stamp_republicChina_ll);
        mLiberatedArea = (LinearLayout) mStampContent.findViewById(R.id.stamp_liberatedArea_ll);
        mQingDynasty = (LinearLayout) mStampContent.findViewById(R.id.stamp_qingDynasty_ll);

        mSynthesize = (Button) mStampContent.findViewById(R.id.stamp_synthesize);
        mSales = (Button) mStampContent.findViewById(R.id.stamp_sales);
        mPrice = (Button) mStampContent.findViewById(R.id.stamp_price);

        initGestureListener(); // 滑动lsitview隐藏导航栏的方法
    }

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

        //横向的listView设置适配器
        hListViewAdapter = new StampHorizontalListViewAdapter(this, mArr);
        hListView.setAdapter(hListViewAdapter);

    }

    private void initData() {
        // 别删数据正确后要用
//        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                HashMap<String, String> params = new HashMap<>();
//                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
//                params.put(StaticField.OP_TYPE, "YS");
//                String mapSort = SortUtils.MapSort(params);
//                String md5code = Encrypt.MD5(mapSort);
//                params.put(StaticField.SIGN, md5code);
//
//                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
//
//                Log.e("有数据吗~~~>",result);
//
//                MyLog.e(result);
//            }
//        });

        if (mList != null) {
            mList = new ArrayList<>();
        }

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
            }
        });

        //GridView的条目点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //去往邮市详情页
                openActivityWitchAnimation(StampDetailActivity.class);
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

    }

    /**
     *  邮市list网络请求
     * @param Order_By 类别
     * @param index 角标
     * @param Sort 排序
     */
    private void RequestNet(final String Order_By, final int index, final String Sort) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSLIST);// 接口名称
                params.put(StaticField.CURRENT_INDEX, String.valueOf(index)); // 当前记录索引
                params.put(StaticField.GOODS_SOURCE,StaticField.YS ); // 商品类型(SC_ZY,SC_DSF,YS,JP)
                params.put(StaticField.ORDER_BY, Order_By); // 排序条件(排序的维度：ZH综合；XL销量；JG价格)
                params.put(StaticField.SORT_TYPE, Sort); // 排序方式(A：升序；D：降序)
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code); // 签名

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                Log.e("result+邮市~~~~>", result);
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
            case R.id.stamp_newchinese_ll://新中国邮票
                mStampContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorRed);
                mStampContent.findViewById(R.id.republicChina_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.liberatedArea_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.qingDynasty_view).setBackgroundColor(mColorGray);
                break;
            case R.id.stamp_republicChina_ll:// 民国邮票
                mStampContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.republicChina_view).setBackgroundColor(mColorRed);
                mStampContent.findViewById(R.id.liberatedArea_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.qingDynasty_view).setBackgroundColor(mColorGray);
                break;
            case R.id.stamp_liberatedArea_ll://解放区邮票
                mStampContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.republicChina_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.liberatedArea_view).setBackgroundColor(mColorRed);
                mStampContent.findViewById(R.id.qingDynasty_view).setBackgroundColor(mColorGray);
                break;
            case R.id.stamp_qingDynasty_ll://清代邮票
                mStampContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.republicChina_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.liberatedArea_view).setBackgroundColor(mColorGray);
                mStampContent.findViewById(R.id.qingDynasty_view).setBackgroundColor(mColorRed);
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
                    RequestNet(StaticField.ZH, num, StaticField.D);
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
