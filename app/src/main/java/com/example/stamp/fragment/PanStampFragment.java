package com.example.stamp.fragment;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.activity.SearchActivity;
import com.example.stamp.adapter.PanStampGridViewAdapter;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.bean.GoodsStampBean;
import com.example.stamp.dialog.SelfMallPanStampFilterDialog;
import com.example.stamp.fragment.popfragment.PanStampFilterFragment;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.ScreenUtils;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.zxing.activity.CaptureActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 淘邮票页面
 */
public class PanStampFragment extends BaseFragment implements View.OnClickListener,AbsListView.OnScrollListener {


    private View mPanStampContent;//内容View
    private View mPanStampTitle;//标题View
    private Button mFilter,mTopBtn,mSynthesize,mSales,mValue;//筛选,置顶,综合，销量，价格
    private List<Fragment> mPopupList;//展示PopupWindow页面的Fragment的集合
    private String[] arr = {"自营商城", "第三方商家", "邮市", "竞拍"};
    private GridView mPanStampGV;//本页的GridView

    private ArrayList<GoodsStampBean.GoodsList> mList;//存放数据的集合
    private ImageView mScan,mSearch;// 扫码，搜索
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private boolean scrollFlag;
    private boolean SynthesizeFlag;// 综合的标记->（false升序,true是降序）
    private boolean SalesFlag;// 销量的标记->（false升序,true是降序）
    private boolean ValueFlag;// 价格的标记标记->（降序false升序,true是降序）
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
        mPanStampTitle = View.inflate(getActivity(), R.layout.fragment_panstamp_title, null);
        return mPanStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mPanStampContent = View.inflate(getActivity(), R.layout.fragment_panstamp_content, null);
        initView();
        initData();
        initListener();
        return mPanStampContent;
    }


    private void initView() {
        mScan = (ImageView) mPanStampTitle.findViewById(R.id.panstamp_title_scan);
        mSearch = (ImageView) mPanStampTitle.findViewById(R.id.panstamp_search);

        mSynthesize = (Button) mPanStampContent.findViewById(R.id.panStamp_synthesize);
        mSales = (Button) mPanStampContent.findViewById(R.id.panStamp_sales);
        mValue= (Button) mPanStampContent.findViewById(R.id.panStamp_value);
        mFilter = (Button) mPanStampContent.findViewById(R.id.panStamp_filter);

        mPanStampGV = (GridView) mPanStampContent.findViewById(R.id.panstamp_gl);
        mTopBtn = (Button) mPanStampContent.findViewById(R.id.stamp_top_btn);
    }


    /**
     * 装载适配器
     */
    private void initAdapter() {
        PanStampGridViewAdapter mPanStampAdapter = new PanStampGridViewAdapter(getActivity(), mList, mBitmap);
        mPanStampGV.setAdapter(mPanStampAdapter);
        mPanStampAdapter.notifyDataSetChanged();
    }


    /**
     * 设置监听
     */
    private void initListener() {
        mScan.setOnClickListener(this);
        mSearch.setOnClickListener(this);

        mSynthesize.setOnClickListener(this);
        mSales.setOnClickListener(this);
        mValue.setOnClickListener(this);
        mFilter.setOnClickListener(this);

        mTopBtn.setOnClickListener(this);
        mPanStampGV.setOnScrollListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    /**
     * 添加数据
     */
    private void initData() {
        if (mList != null) {
            mList = new ArrayList<>();
        }
        // 初始化第一个按钮
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num, StaticField.D);
    }

    /**
     *  套邮票list网络请求
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
                params.put(StaticField.GOODS_SOURCE,StaticField.GOODSSOURCE ); // 商品类型(SC_ZY,SC_DSF,YS,JP)
                params.put(StaticField.ORDER_BY, Order_By); // 排序条件(排序的维度：ZH综合；XL销量；JG价格)
                params.put(StaticField.SORT_TYPE, Sort); // 排序方式(A：升序；D：降序)
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code); // 签名

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                Log.e("result+淘邮票~~~~>", result);
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
            case R.id.panstamp_title_scan://扫码
                openActivityWitchAnimation(CaptureActivity.class);
                break;
            case R.id.panstamp_search://搜索
                openActivityWitchAnimation(SearchActivity.class);
                break;
            case R.id.panStamp_synthesize://综合
                Log.e("综合~~>","综合。。。。。");
                setOtherButton(mSales, mValue);
                SalesFlag = true;// 销量为true
                ValueFlag = true;// 价格为true
                // true代表降序,false代表升序
                if (SynthesizeFlag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.D);
                    SynthesizeFlag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.A);
                    SynthesizeFlag = true;
                }
                break;
            case R.id.panStamp_sales://销量
                Log.e("销量~~>","销量。。。。。");
                setOtherButton(mSynthesize, mValue);
                SynthesizeFlag = true;// 综合为true
                ValueFlag = true;// 价格为true
                // true代表降序,false代表升序
                if (SalesFlag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num, StaticField.D);
                    SalesFlag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num, StaticField.A);
                    SalesFlag = true;
                }
                break;
            case R.id.panStamp_value://价格
                Log.e("价格~~>","价格。。。。。");
                setOtherButton(mSynthesize, mSales);
                SynthesizeFlag = true;// 综合为true
                SalesFlag = true;// 销量为true
                // true代表降序,false代表升序
                if (ValueFlag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mValue, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.D);
                    ValueFlag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mValue, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.A);
                    ValueFlag = true;
                }
                break;
            case R.id.panStamp_filter://筛选
                setPopupWindowListData();
                SelfMallPanStampFilterDialog filterDialogFragment = new SelfMallPanStampFilterDialog(mPopupList, arr);
                filterDialogFragment.show(getChildFragmentManager(), StaticField.PANSTAMPFILTERDIALOG);
                break;
            case R.id.stamp_top_btn://置顶
                setListViewPos(0);
                mTopBtn.setVisibility(View.GONE);// 置顶后隐藏按钮
                break;
            default:
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
            mPanStampGV.smoothScrollToPosition(pos);
        } else {
            mPanStampGV.setSelection(pos);
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
                if (mPanStampGV.getLastVisiblePosition() == (mPanStampGV
                        .getCount() - 1)) {
                    mTopBtn.setVisibility(View.VISIBLE);
                }
                // 判断滚动到顶部
                if (mPanStampGV.getFirstVisiblePosition() == 0) {
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
                && ScreenUtils.getScreenViewBottomHeight(mPanStampGV) >= ScreenUtils
                .getScreenHeight(getActivity())) {
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
     * 这个最后看在哪里写
     */
    private void setPopupWindowListData() {
        //初始化集合
        mPopupList = new ArrayList<>();
        PanStampFilterFragment mPanStampFragment = new PanStampFilterFragment();
        mPopupList.add(mPanStampFragment);
        mPopupList.add(mPanStampFragment);
        mPopupList.add(mPanStampFragment);
        mPopupList.add(null);
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
