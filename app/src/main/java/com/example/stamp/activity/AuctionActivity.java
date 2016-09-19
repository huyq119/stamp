package com.example.stamp.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.AuctionListViewAdapter;
import com.example.stamp.adapter.StampHorizontalListViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.GoodsStampBean;
import com.example.stamp.bean.StampBean;
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
    private TextView mTitle,mNoListTV;
    // 新中国邮票, 民国邮票, 解放区邮票, 清代邮票
    private LinearLayout mNewChinese, mRepublicChina, mLiberatedArea, mQingDynasty;

    private int mColorGray = Color.parseColor("#dddfe3");// 横线灰色
    private int mColorRed = Color.parseColor("#e20000");// 横线红色
    private Button mTopBtn, mSynthesize, mOver, mCamera;// 置顶，综合，即将结束，等待开拍
    private boolean Synthesizeflag;// 综合的标记->升序还是降序false升序,true是降序
    private boolean Overflag;// 即将结束的标记->升序还是降序false升序,true是降序
    private boolean Cameraflag;// 等待开拍的标记->升序还是降序false升序,true是降序

    private boolean scrollFlag = false; // 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private AuctionListViewAdapter mListAdapter;
    private LinearLayout mHeartll;//头部的布局
    private GestureDetector mGestureDetector;
    private int num = 0;//初始索引
    private GoodsStampBean mGoodsStampBean;
    private Handler mHandler = new Handler() {



        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case StaticField.SUCCESS://商城Lsit
                    Gson gson = new Gson();
                     mGoodsStampBean = gson.fromJson((String) msg.obj, GoodsStampBean.class);
                    mList = mGoodsStampBean.getGoods_list();
                    if (mList!=null&&mList.size()!=0){
                        //竖向ListView设置适配器
                        AuctionListViewAdapter mListAdapter = new AuctionListViewAdapter(AuctionActivity.this, mBitmap, mList);
                        mListView.setAdapter(mListAdapter);
                        mListAdapter.notifyDataSetChanged();
                    }else{
                        mListView.setVisibility(View.GONE);
                        mNoListTV.setVisibility(View.VISIBLE);
                    }


                    break;
            }
        }
    };

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
        initAdapter();
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
        mNoListTV = (TextView) mAuctionContent.findViewById(R.id.auction_no_lv_tv);
        mNewChinese = (LinearLayout) mAuctionContent.findViewById(R.id.auction_newchinese_ll);
        mAuctionContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorRed);// 初始化新中国邮票
        mRepublicChina = (LinearLayout) mAuctionContent.findViewById(R.id.auction_republicChina_ll);
        mLiberatedArea = (LinearLayout) mAuctionContent.findViewById(R.id.auction_liberatedArea_ll);
        mQingDynasty = (LinearLayout) mAuctionContent.findViewById(R.id.auction_qingDynasty_ll);
        mSynthesize = (Button) mAuctionContent.findViewById(R.id.auction_synthesize);
        mOver = (Button) mAuctionContent.findViewById(R.id.auction_over);
        mCamera = (Button) mAuctionContent.findViewById(R.id.auction_camera);

        initGestureListener();

    }

    /**
     *滑动lsitview隐藏导航栏的方法
     */
    private void initGestureListener(){
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

        // 横向的listView设置适配器
        hListViewAdapter = new StampHorizontalListViewAdapter(this, mArr);
        hListView.setAdapter(hListViewAdapter);

    }

    /**
     * 初始化第一个按钮
     */
    private void initData() {
        // 以下注释的网络请求别删数据正确后要用
//        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                HashMap<String, String> params = new HashMap<>();
//                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
//                params.put(StaticField.OP_TYPE, "JP");
//                String mapSort = SortUtils.MapSort(params);
//                String md5code = Encrypt.MD5(mapSort);
//                params.put(StaticField.SIGN, md5code);
//
//                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
//
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
                openActivityWitchAnimation(AuctionDetailActivity.class,bundle);
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

    }
    /**
     *  商城list网络请求
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
                params.put(StaticField.GOODS_SOURCE,StaticField.JP); // 商品类型
                params.put(StaticField.ORDER_BY, Order_By); // 排序条件(排序的维度：ZH综合；XL销量；JG价格)
                params.put(StaticField.SORT_TYPE, Sort); // 排序方式(A：升序；D：降序)
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code); // 签名

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                Log.e("result+竞拍~~~~>", result);
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
            case R.id.auction_newchinese_ll://新中国邮票
                mAuctionContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorRed);
                mAuctionContent.findViewById(R.id.republicChina_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.liberatedArea_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.qingDynasty_view).setBackgroundColor(mColorGray);
                break;
            case R.id.auction_republicChina_ll:// 民国邮票
                mAuctionContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.republicChina_view).setBackgroundColor(mColorRed);
                mAuctionContent.findViewById(R.id.liberatedArea_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.qingDynasty_view).setBackgroundColor(mColorGray);
                break;
            case R.id.auction_liberatedArea_ll://解放区邮票
                mAuctionContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.republicChina_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.liberatedArea_view).setBackgroundColor(mColorRed);
                mAuctionContent.findViewById(R.id.qingDynasty_view).setBackgroundColor(mColorGray);
                break;
            case R.id.auction_qingDynasty_ll://清代邮票
                mAuctionContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.republicChina_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.liberatedArea_view).setBackgroundColor(mColorGray);
                mAuctionContent.findViewById(R.id.qingDynasty_view).setBackgroundColor(mColorRed);
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
                        .getCount()-1)) {
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
