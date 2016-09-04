package com.example.stamp.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.StampMarketGridViewAdapter;
import com.example.stamp.adapter.StampTapGridViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.dialog.PanStampFilterDialog;
import com.example.stamp.dialog.StampTapFilterDialog;
import com.example.stamp.fragment.popfragment.SelfMallFilterFragment;
import com.example.stamp.fragment.popfragment.SelfMallFragment;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.ScreenUtils;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;

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
    private ArrayList<StampTapBean.StampList> mList;
    private List<Fragment> mPopupList;//展示PopupWindow页面的Fragment的集合
    private String[] arr = {"自营商城", "第三方商家"};
    private int mCount;
    private boolean scrollFlag,Salesflag,Priceflag,Synthesizeflag; // 标记是否滑动,销量，价格,综合
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private String[]  arrClass= {"人物", "植物", "节日", "器皿", "字画", "风光", "经济", "农业", "民俗", "动物", "生肖",
            "文化艺术", "政治", "科教", "工业", "交通", "军事", "文学", "邮政", "公共"};
    private String[] arrYear = {"2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005"};
    private String[] arrPopson= {"任平", "于平", "甲钴胺", "邮票","任平", "于平", "甲钴胺", "邮票","任平", "于平", "甲钴胺", "邮票"};
    private TextView mTitle;

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
        initAdapter();
        initListener();
        return mSelfMallContent;
    }

    private void initView() {

        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add(new StampTapBean.StampList("庚申年", "￥1000.0" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        }
        mBack = (ImageView) mSelfMallTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mSelfMallTitle.findViewById(R.id.base_title);
        mTitle.setText("商城");
        mSearch = (ImageView) mSelfMallTitle.findViewById(R.id.base_search);

        mSynthesize = (Button) mSelfMallContent.findViewById(R.id.self_synthesize);
        mSales = (Button) mSelfMallContent.findViewById(R.id.self_sales);
        mPrice = (Button) mSelfMallContent.findViewById(R.id.self_price);
        mFilter = (Button) mSelfMallContent.findViewById(R.id.self_filter);
        mGridView = (GridView) mSelfMallContent.findViewById(R.id.selfMall_gl);
        mTopBtn = (Button) mSelfMallContent.findViewById(R.id.selsf_top_btn);

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
                openActivityWitchAnimation(SelfMallDetailActivity.class);
            }
        });
    }

    private void initAdapter() {
//        if (gvAdapter == null) {
        //为GridView设置适配器
        mStampMarAdapter = new StampMarketGridViewAdapter(this, mList, mBitmap);
//        }
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
//        RequestNet(StaticField.ZH, num, StaticField.A);
        for (int i = 0; i < 20; i++) {
            mList.add(new StampTapBean.StampList("庚申年", "￥1000.0" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        }
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
            case R.id.selsf_top_btn://置顶
                setListViewPos(0);
                mTopBtn.setVisibility(View.GONE);// 回到顶部后置顶按钮隐藏
                break;
            case R.id.self_filter://筛选按钮
                // 筛选有点问题
                setPopupWindowListData();
                PanStampFilterDialog filterDialogFragment = new PanStampFilterDialog(mPopupList, arr);
                filterDialogFragment.show(getSupportFragmentManager(), StaticField.PANSTAMPFILTERDIALOG);
                break;
            case R.id.self_synthesize://综合
                setOtherButton(mSales, mPrice);
                Salesflag = true;
                Priceflag = true;
                // true代表降序,false代表升序
                if (Synthesizeflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
//                    RequestNet(StaticField.ZH, num, StaticField.D);

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("庚申年", "￥1000.0" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
                    }
                    initAdapter();

                    Synthesizeflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
//                    RequestNet(StaticField.ZH, num, StaticField.A);

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("戊戌年", "￥200000.0" + i, "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"));
                    }
                    initAdapter();
                    Synthesizeflag = true;
                }
                break;
            case R.id.self_sales://销量
                setOtherButton(mSynthesize, mPrice);
                Synthesizeflag = true;
                Priceflag = true;

                if (Salesflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSales, Color.parseColor("#ff0000"));
//                    RequestNet(StaticField.SJ, num, StaticField.D);

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("戊戌年", "￥30000.0" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
                    }
                    initAdapter();

                    Salesflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSales, Color.parseColor("#ff0000"));
//                    RequestNet(StaticField.SJ, num, StaticField.A);

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("乾隆年", "￥10120000.0" + i, "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"));
                    }
                    initAdapter();

                    Salesflag = true;
                }
                break;
            case R.id.self_price://价格
                setOtherButton(mSynthesize, mSales);
                Synthesizeflag = true;
                Salesflag = true;

                if (Priceflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mPrice, Color.parseColor("#ff0000"));
//                    RequestNet(StaticField.JG, num, StaticField.D);

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("康熙年", "￥1000.0" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
                    }
                    initAdapter();

                    Priceflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mPrice, Color.parseColor("#ff0000"));
//                    RequestNet(StaticField.JG, num, StaticField.A);

                    mList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        mList.add(new StampTapBean.StampList("乾隆年", "￥41111000.0" + i, "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"));
                    }
                    initAdapter();

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
        SelfMallFilterFragment mallFragment = new SelfMallFilterFragment(arrClass,arrYear,arrPopson);
        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
    }


}
