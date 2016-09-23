package com.example.stamp.activity;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.StampMarketGridViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.GoodsStampBean;
import com.example.stamp.dialog.SelfMallPanStampFilterDialog;
import com.example.stamp.fragment.popfragment.SelfMallFilterFragment;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.ScreenUtils;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 商城页面
 */
public class SelfMallActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {

    private View mSelfMallTitle, mSelfMallContent;
    private TextView mMarketPrice;//市场价
    private ImageView mBack, mSearch;
    private Button mSynthesize, mSales, mPrice, mFilter, mTopBtn;// 综合，销量，价格，筛选,置顶
    private GridView mGridView;
    private StampMarketGridViewAdapter mStampMarAdapter;
    private ArrayList<GoodsStampBean.GoodsList> mList;
    private List<Fragment> mPopupList;//展示PopupWindow页面的Fragment的集合
    private String[] arr = {"自营商城", "第三方商家"};
    private int mCount;
    private boolean scrollFlag,Salesflag,Priceflag,Synthesizeflag; // 标记是否滑动,销量，价格,综合
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
//    private String[]  arrClass= {"人物", "植物", "节日", "器皿", "字画", "风光", "经济", "农业", "民俗", "动物", "生肖",
//            "文化艺术", "政治", "科教", "工业", "交通", "军事", "文学", "邮政", "公共"};
//    private String[] arrYear = {"2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005"};
//    private String[] arrPopson= {"任平", "于平", "甲钴胺", "邮票","任平", "于平", "甲钴胺", "邮票","任平", "于平", "甲钴胺", "邮票"};
    private TextView mTitle;
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
                    initAdapter();
                    break;
            }
        }
    };


    @Override
    public View CreateTitle() {
        mSelfMallTitle = View.inflate(this, R.layout.base_search_title, null);
        return mSelfMallTitle;
    }

    @Override
    public View CreateSuccess() {
        mSelfMallContent = View.inflate(this, R.layout.activity_selfmall_content, null);
        initView();
        initData();
        initListener();
        return mSelfMallContent;
    }

    private void initView() {

        mBack = (ImageView) mSelfMallTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mSelfMallTitle.findViewById(R.id.base_title);
        mTitle.setText("商城");
        mSearch = (ImageView) mSelfMallTitle.findViewById(R.id.base_search);

        mSynthesize = (Button) mSelfMallContent.findViewById(R.id.self_synthesize);
        mSales = (Button) mSelfMallContent.findViewById(R.id.self_sales);
        mPrice = (Button) mSelfMallContent.findViewById(R.id.self_price);
        mFilter = (Button) mSelfMallContent.findViewById(R.id.self_filter);
        mGridView = (GridView) mSelfMallContent.findViewById(R.id.selfMall_gl);
        mTopBtn = (Button) mSelfMallContent.findViewById(R.id.base_top_btn);

    }

    private void initListener() {
        mGridView.setOnScrollListener(this);
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mSynthesize.setOnClickListener(this);
        mSales.setOnClickListener(this);
        mPrice.setOnClickListener(this);
        mFilter.setOnClickListener(this);
        mTopBtn.setOnClickListener(this);

        // GridView条目监听事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mGoods_sn = mGoodsStampBean.getGoods_list().get(i).getGoods_sn();
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.GOODS_SN, mGoods_sn);
                openActivityWitchAnimation(SelfMallDetailActivity.class,bundle);
            }
        });
    }

    private void initAdapter() {
        if (mStampMarAdapter == null) {
        //为GridView设置适配器
        mStampMarAdapter = new StampMarketGridViewAdapter(this, mList, mBitmap);
        }
        //内容GridView设置适配器
        mGridView.setAdapter(mStampMarAdapter);
        mStampMarAdapter.notifyDataSetChanged();

    }

    private void initData() {
        // 以下注释的网络请求别删数据正确后要用
//        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                HashMap<String, String> params = new HashMap<>();
//                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
//                params.put(StaticField.OP_TYPE, "SC_ZY");
//                String mapSort = SortUtils.MapSort(params);
//                String md5code = Encrypt.MD5(mapSort);
//                params.put(StaticField.SIGN, md5code);
//
//                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
//                Log.e("有数据吗~~~>",result);
//
//            }
//        });



        if (mList != null) {
            mList = new ArrayList<>();
        }

        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num, StaticField.A);
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
                params.put(StaticField.GOODS_SOURCE,StaticField.GOODSMALL); // 商品类型
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
            case R.id.base_top_btn://置顶
                setListViewPos(0);
                mTopBtn.setVisibility(View.GONE);// 回到顶部后置顶按钮隐藏
                break;
            case R.id.self_filter://筛选按钮
                // 筛选有点问题
                setPopupWindowListData();
                SelfMallPanStampFilterDialog filterDialogFragment = new SelfMallPanStampFilterDialog(mPopupList, arr);
                filterDialogFragment.show(getSupportFragmentManager(), StaticField.PANSTAMPFILTERDIALOG);
                break;
            case R.id.self_synthesize://综合
                setOtherButton(mSales, mPrice);
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
            case R.id.self_sales://销量
                setOtherButton(mSynthesize, mPrice);
                Synthesizeflag = true;
                Priceflag = true;

                if (Salesflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num, StaticField.D);
                    Salesflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num, StaticField.A);
                    Salesflag = true;
                }
                break;
            case R.id.self_price://价格
                setOtherButton(mSynthesize, mSales);
                Synthesizeflag = true;
                Salesflag = true;

                if (Priceflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.D);
                    Priceflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.A);
                    Priceflag = true;
                }
                break;
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
     * GridView滑动状态改变监听的方法
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
     * @param absListView
     * @param firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
     * @param visibleItemCount 当前能看见的列表项个数（小半个也算）
     * @param totalItemCount totalItemCount：列表项共数
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

    /**
     * 筛选Fragment页面
     */
    private void setPopupWindowListData() {
        //初始化集合
        mPopupList = new ArrayList<>();
        SelfMallFilterFragment mallFragment = new SelfMallFilterFragment();
        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
    }


}
