package com.example.stamp.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.example.stamp.bean.StampBean;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.view.HorizontalListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 邮市页面
 */
public class StampActivity extends BaseActivity implements View.OnClickListener ,AbsListView.OnScrollListener{

    private View mStampTitle, mStampContent, mBlankView;
    private HorizontalListView hListView;//横向滑动的listView
    private StampHorizontalListViewAdapter hListViewAdapter;//横向花的listView的适配器
    private ArrayList<StampTapBean.StampList> mList;
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
    private Button mTopBtn, mSynthesize, mSales, mPrice ;// 置顶，综合，销量，价格，筛选
    private boolean Synthesizeflag;// 综合的标记->升序还是降序false升序,true是降序
    private boolean Salesflag;// 销量的标记->升序还是降序false升序,true是降序
    private boolean Priceflag;// 价格的标记->升序还是降序false升序,true是降序

    private boolean scrollFlag = false; // 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private int currentTop;
    private int mLastFirstTop;
    private int touchSlop = 10;
    private StampMarketGridViewAdapter mStampMarAdapter;

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
        initAdapter();
        initListener();
        return mStampContent;
    }


    private void initView() {
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add(new StampTapBean.StampList("庚申年", "￥1000.0" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        }
        mBack = (ImageView) mStampTitle.findViewById(R.id.base_title_back);
        mSearch = (ImageView) mStampTitle.findViewById(R.id.base_search);
        hListView = (HorizontalListView) mStampContent.findViewById(R.id.stamp_hl);
        mGridView = (GridView) mStampContent.findViewById(R.id.stamp_gl);

        mTopBtn = (Button) mStampContent.findViewById(R.id.stamp_top_btn);// 置顶

        mTitleStamp = (LinearLayout) mStampContent.findViewById(R.id.stamp_titleStamp_ll);
        hListView = (HorizontalListView) mStampContent.findViewById(R.id.stamp_hl);//横向ListView
        mBlankView = mStampContent.findViewById(R.id.stamp_blank_view);

        mNewChinese = (LinearLayout) mStampContent.findViewById(R.id.stamp_newchinese_ll);
        mStampContent.findViewById(R.id.newChinese_view).setBackgroundColor(mColorRed);// 初始化新中国邮票
        mRepublicChina = (LinearLayout) mStampContent.findViewById(R.id.stamp_republicChina_ll);
        mLiberatedArea = (LinearLayout) mStampContent.findViewById(R.id.stamp_liberatedArea_ll);
        mQingDynasty = (LinearLayout) mStampContent.findViewById(R.id.stamp_qingDynasty_ll);

        mSynthesize = (Button) mStampContent.findViewById(R.id.stamp_synthesize);
        mSales = (Button) mStampContent.findViewById(R.id.stamp_sales);
        mPrice = (Button) mStampContent.findViewById(R.id.stamp_price);
    }


    private void initAdapter() {
        //内容GridView设置适配器
        mStampMarAdapter = new StampMarketGridViewAdapter(this, mList, mBitmap);
        mGridView.setAdapter(mStampMarAdapter);

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
//                Log.e("有数据吗~~~>",result);
//                MyLog.e(result);
//            }
//        });

        if (mList != null) {
            mList = new ArrayList<>();
        }

        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
//        RequestNet(StaticField.ZH, num, StaticField.A);
        for (int i = 0; i < 20; i++) {
            mList.add(new StampTapBean.StampList("庚申年", "￥1000.0" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        }
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
                // 通知横向hListView父窗口放行onTouch点击事件
                hListView.requestDisallowInterceptTouchEvent(true);
                return false;
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
            case R.id.stamp_top_btn://置顶
                setListViewPos(0);
                mTitleStamp.setVisibility(View.VISIBLE);
                hListView.setVisibility(View.VISIBLE);
                mBlankView.setVisibility(View.VISIBLE);
                mTopBtn.setVisibility(View.GONE);// 回到顶部后置顶按钮隐藏
                break;
            case R.id.stamp_synthesize://综合
                setOtherButton(mSales, mPrice);// (销量，价格)
                Salesflag = true;
                Priceflag = true;
                // true代表降序,false代表升序
                if (Synthesizeflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("庚申年", "￥20000.0" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
                    }
                    initAdapter();
                    mStampMarAdapter.notifyDataSetChanged();

//                    RequestNet(StaticField.ZH, num, StaticField.D);
                    Synthesizeflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("戊戌年", "￥100000.0" + i, "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"));
                    }
                    initAdapter();
                    mStampMarAdapter.notifyDataSetChanged();
//                    RequestNet(StaticField.ZH, num, StaticField.D);

                    Synthesizeflag = true;
                }
                break;
            case R.id.stamp_sales://价格
                setOtherButton(mSynthesize, mPrice);// (综合，价格)
                Synthesizeflag = true;
                Priceflag = true;

                if (Salesflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSales, Color.parseColor("#ff0000"));

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("戊戌年", "￥1000.0" + i, "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"));
                    }
                    initAdapter();
                    mStampMarAdapter.notifyDataSetChanged();

//                    RequestNet(StaticField.SJ, num, StaticField.D);
                    Salesflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mSales, Color.parseColor("#ff0000"));

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("庚申年", "￥2000000.0" + i, "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg"));
                    }
                    initAdapter();
                    mStampMarAdapter.notifyDataSetChanged();

//                    RequestNet(StaticField.SJ, num, StaticField.A);
                    Salesflag = true;
                }
                break;
            case R.id.stamp_price://等待开拍
                setOtherButton(mSynthesize, mSales);// (综合，销量)
                Synthesizeflag = true;
                Salesflag = true;

                if (Priceflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mPrice, Color.parseColor("#ff0000"));
//                    RequestNet(StaticField.JG, num, StaticField.D);

                    mList = new ArrayList<>();

                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("乾隆年", "￥100.0" + i, "http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg"));
                    }
                    initAdapter();
                    mStampMarAdapter.notifyDataSetChanged();

                    Priceflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mPrice, Color.parseColor("#ff0000"));
//                    RequestNet(StaticField.JG, num, StaticField.A);
                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("康熙年", "￥2120000.0" + i, "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"));
                    }
                    initAdapter();
                    mStampMarAdapter.notifyDataSetChanged();http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg
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
        View firstChildView = absListView.getChildAt(0);
        if (firstChildView != null) {
            currentTop = absListView.getChildAt(0).getTop();
        } else {
            //GridView初始化的时候会回调onScroll方法，此时getChildAt(0)仍是为空的
            return;
        }
        //判断上次可见的第一个位置和这次可见的第一个位置
        if (firstVisibleItem != lastVisibleItemPosition) {
            //不是同一个位置
            if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                mTitleStamp.setVisibility(View.GONE);
                hListView.setVisibility(View.GONE);
                mBlankView.setVisibility(View.GONE);
            } else {// 下滑
                mTitleStamp.setVisibility(View.VISIBLE);
                hListView.setVisibility(View.VISIBLE);
                mBlankView.setVisibility(View.VISIBLE);
            }
            mLastFirstTop = currentTop;
        } else {
            //是同一个位置
            if (Math.abs(currentTop - mLastFirstTop) > touchSlop) {
                //避免动作执行太频繁或误触，加入touchSlop判断，具体值可进行调整
                if (currentTop > mLastFirstTop) { // 下滑
                    mTitleStamp.setVisibility(View.VISIBLE);
                    hListView.setVisibility(View.VISIBLE);
                    mBlankView.setVisibility(View.VISIBLE);
//                    Log.i("cs", "equals--->up");
                } else if (currentTop < mLastFirstTop) { // 上滑
                    mTitleStamp.setVisibility(View.GONE);
                    hListView.setVisibility(View.GONE);
                    mBlankView.setVisibility(View.GONE);
//                    Log.i("cs", "equals--->down");
                }
                mLastFirstTop = currentTop;
            }
        }
        lastVisibleItemPosition = firstVisibleItem;
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
