package cn.com.chinau.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.SearchActivity;
import cn.com.chinau.activity.StampTapDetailActivity;
import cn.com.chinau.adapter.StampTapGridViewAdapter;
import cn.com.chinau.base.BaseFragment;
import cn.com.chinau.bean.CategoryRMBean;
import cn.com.chinau.bean.StampTapBean;
import cn.com.chinau.dialog.StampTapFilterDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.zxing.activity.CaptureActivity;

/**
 * 邮票目录页面
 */
public class StampTapFragment extends BaseFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, AbsListView.OnScrollListener,StampTapFilterDialog.JsonData {


    private static final int STAMPCONTENT = 0;

    private View mStampTitle;//标题
    private ImageView mScan;//扫码按钮
    private ImageView mSearch;//搜索按钮
    private GridView mGrid;
    private View mStampTapContent;
    private Button mSynthesize, mRelease, mPrice, mFilter, mTop;

    private StampTapGridViewAdapter gvAdapter;

    private boolean Synthesizeflag;// 综合的标记->升序还是降序false升序,true是降序
    private boolean Salesflag;// 发行时间的标记->升序还是降序false升序,true是降序
    private boolean Priceflag;// 价格的标记标记->升序还是降序false升序,true是降序

    private ArrayList<StampTapBean.StampList> mList;
    private int num = 0;//初始索引
    private boolean scrollFlag; // 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private SharedPreferences sp;
    private String[] mTitle, mArrTitle0, mArrTitle1, mArrTitle2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case STAMPCONTENT://邮票信息内容
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    StampTapBean stampTapBean = gson.fromJson(result, StampTapBean.class);
                    String code = stampTapBean.getRsp_code();
                    if (code.equals("0000")) {
//                        if (mList.size() !=0){
                            mList = stampTapBean.getStamp_list();
                            initAdapter();
//                        }
                    }
                    break;
            }
        }
    };

    @Override
    public View CreateTitle() {
        mStampTitle = View.inflate(getActivity(), R.layout.fragment_stamptap_title, null);
        return mStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mStampTapContent = View.inflate(getActivity(), R.layout.fragment_stamptap_content, null);

        sp = getActivity().getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mStampTapContent;
    }


    private void initView() {

        mScan = (ImageView) mStampTitle.findViewById(R.id.stamptap_title_scan);
        mSearch = (ImageView) mStampTitle.findViewById(R.id.stamptap_search);
        mGrid = (GridView) mStampTapContent.findViewById(R.id.stamptap_gl);
        mFilter = (Button) mStampTapContent.findViewById(R.id.stamptap_filter);
        mSynthesize = (Button) mStampTapContent.findViewById(R.id.stampTap_synthesize);
        mRelease = (Button) mStampTapContent.findViewById(R.id.stampTap_sales);
        mPrice = (Button) mStampTapContent.findViewById(R.id.stampTap_price);
        mTop = (Button) mStampTapContent.findViewById(R.id.stamp_top_btn);// 置顶

    }


    private void initData() {
        GetCategory(); // 获取在本地的邮票目录类别数据

        if (mList != null) {
            mList = new ArrayList<>();
        }
        // 初始化第一个按钮
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num, StaticField.D,""); // 邮票目录请求网络的方法
    }

    /**
     * 获取在本地的邮票目录类别数据
     */
    private void GetCategory() {
        String mCategory = sp.getString("Category2", "");
        Gson gson = new Gson();
        CategoryRMBean mCategoryRMBean = gson.fromJson(mCategory, CategoryRMBean.class);
        ArrayList<CategoryRMBean.Category> list = mCategoryRMBean.getCategory();

        mTitle = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            mTitle[i] = list.get(i).getName();
            MyLog.LogShitou("1级mTitle", mTitle[i]);

        }

        CategoryRMBean.Category SubCategory0 = list.get(0);
        CategoryRMBean.Category SubCategory1 = list.get(1);
        CategoryRMBean.Category SubCategory2 = list.get(2);
        ArrayList<CategoryRMBean.Category.SubCategory> subCategory0 = SubCategory0.getSubCategory();
        ArrayList<CategoryRMBean.Category.SubCategory> subCategory1 = SubCategory1.getSubCategory();
        ArrayList<CategoryRMBean.Category.SubCategory> subCategory2 = SubCategory2.getSubCategory();

        int sub0 = subCategory0.size();// 获取subCategory0的个数
        int sub1 = subCategory1.size();// 获取subCategory1的个数
        int sub2 = subCategory2.size();// 获取subCategory2的个数
        mArrTitle0 = new String[sub0];// 一级分类
        mArrTitle1 = new String[sub1];// 一级分类
        mArrTitle2 = new String[sub2];// 一级分类

        for (int i = 0; i < subCategory0.size(); i++) {
            mArrTitle0[i] = subCategory0.get(i).getName();
            MyLog.LogShitou("2级Title0", mArrTitle0[i]);
        }
        for (int i = 0; i < subCategory1.size(); i++) {
            mArrTitle1[i] = subCategory1.get(i).getName();
            MyLog.LogShitou("2级Title1", mArrTitle1[i]);
        }
        for (int i = 0; i < subCategory2.size(); i++) {
            mArrTitle2[i] = subCategory2.get(i).getName();
            MyLog.LogShitou("2级Title2", mArrTitle2[i]);
        }

    }

    /**
     * 邮票目录请求网络的方法
     *
     * @param Order_By 类别
     * @param index    角标
     * @param Sort     排序
     */
    private void RequestNet(final String Order_By, final int index, final String Sort,final String category) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPTAP);// 接口名称
                params.put(StaticField.CURRENT_INDEX, String.valueOf(index)); // 当前记录索引
                params.put(StaticField.ORDER_BY, Order_By); // 排序条件(排序的维度：ZH综合；SJ时间；JG价格)
                params.put(StaticField.SORT_TYPE, Sort); // 排序方式(A：升序；D：降序)

                if (!category.equals("")){
                    params.put(StaticField.CATEGORY, category); // 类别，json串
                }
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code); // 签名

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("邮票目录list",result);

                if (result.equals("-1") |result.equals("-2")  ) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.obj = result;
                msg.what = STAMPCONTENT;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void initAdapter() {
        //为GridView设置适配器
        gvAdapter = new StampTapGridViewAdapter(getActivity(), mList, mBitmap);
        mGrid.setAdapter(gvAdapter);
        gvAdapter.notifyDataSetChanged();

    }

    private void initListener() {
        mGrid.setOnItemClickListener(this);
        mGrid.setOnScrollListener(this);
        mFilter.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mSynthesize.setOnClickListener(this);
        mRelease.setOnClickListener(this);
        mPrice.setOnClickListener(this);
        mTop.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {
        initData();
    }

    // 回调json串方法
    @Override
    public void GetJsonData(String tojson) {
        String mToJson = tojson;
        MyLog.LogShitou("传过来的Json串",mToJson);
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num, StaticField.D,mToJson); // 邮票目录请求网络的方法
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stamptap_filter://筛选按钮
                StampTapFilterDialog stampTapFilterDialog = new StampTapFilterDialog(mTitle, mArrTitle0, mArrTitle1, mArrTitle2);
                stampTapFilterDialog.setmJsonData(this);
                stampTapFilterDialog.show(getFragmentManager(), StaticField.STAMPTAPFILTERDIALOG);
                break;
            case R.id.stamptap_title_scan://扫码按钮
                openActivityWitchAnimation(CaptureActivity.class);
                break;
            case R.id.stamptap_search://搜索按钮
                openActivityWitchAnimation(SearchActivity.class);
                break;
            case R.id.stampTap_synthesize://综合
                setOtherButton(mRelease, mPrice);
                Salesflag = true;
                Priceflag = true;
                // true代表降序,false代表升序
                if (Synthesizeflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.D,"");
                    Synthesizeflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.A,"");
                    Synthesizeflag = true;
                }
                break;
            case R.id.stampTap_sales://发行时间
                setOtherButton(mSynthesize, mPrice);
                Synthesizeflag = true;
                Priceflag = true;

                if (Salesflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mRelease, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.SJ, num, StaticField.D,"");
                    Salesflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mRelease, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.SJ, num, StaticField.A,"");
                    Salesflag = true;
                }
                break;
            case R.id.stampTap_price://价格
                setOtherButton(mSynthesize, mRelease);
                Synthesizeflag = true;
                Salesflag = true;

                if (Priceflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.D,"");
                    Priceflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.A,"");
                    Priceflag = true;
                }
                break;
            case R.id.stamp_top_btn://置顶
                setListViewPos(0);
                mTop.setVisibility(View.GONE);// 回到顶部后置顶按钮消失
                break;
        }
    }


    /**
     * GridView条目监听方法
     *
     * @param adapterView
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String stamp_sn = mList.get(i).getStamp_sn();// 编号
        String current_price = mList.get(i).getCurrent_price(); // 当前价格
        Bundle bundle = new Bundle();
        bundle.putString(StaticField.STAMPDETAIL_SN, stamp_sn);
        bundle.putString(StaticField.STAMPDETAIL_PRICE, current_price);
        openActivityWitchAnimation(StampTapDetailActivity.class, bundle);
    }

    /**
     * GridView滑动状态改变监听的方法
     */
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        switch (i) {
            // 当不滚动时
            case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                scrollFlag = false;
                // 判断滚动到底部
                if (mGrid.getLastVisiblePosition() == (mGrid
                        .getCount() - 1)) {
                    mTop.setVisibility(View.VISIBLE);
                }
                // 判断滚动到顶部
                if (mGrid.getFirstVisiblePosition() == 0) {
                    mTop.setVisibility(View.GONE);
                }
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                scrollFlag = true;
                break;
            case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
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
                && ScreenUtils.getScreenViewBottomHeight(mGrid) >= ScreenUtils
                .getScreenHeight(getActivity())) {
            if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                Log.e("上滑~~~~>", "上滑" + firstVisibleItem + "-" + visibleItemCount + "-" + totalItemCount);
                mTop.setVisibility(View.VISIBLE);
            } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                Log.e("下滑~~~~>", "下滑" + firstVisibleItem + "-" + visibleItemCount + "-" + totalItemCount);
                mTop.setVisibility(View.GONE);
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
            mGrid.smoothScrollToPosition(pos);
            Log.e("这是多少~~>001", "hehe--" + pos);
        } else {
            Log.e("这是多少~~>002", "haha--" + pos);
            mGrid.setSelection(pos);
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
        Drawable drawable = getActivity().getResources().getDrawable(ID);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        button.setCompoundDrawables(null, null, drawable, null);
        button.setTextColor(color);
    }



}
