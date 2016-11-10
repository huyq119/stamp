package cn.com.chinau.activity;


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

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.StampMarketGridViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.GoodsStampBean;
import cn.com.chinau.dialog.SelfMallPanStampFilterDialog;
import cn.com.chinau.fragment.popfragment.SelfMallFilterFragment;
import cn.com.chinau.fragment.popfragment.ThreeMallFilterFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 商城页面
 */
public class SelfMallActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, SelfMallPanStampFilterDialog.ClickEnsure {

    private View mSelfMallTitle, mSelfMallContent;
    private TextView mMarketPrice;//市场价
    private ImageView mBack, mSearch;
    private Button mSynthesize, mSales, mPrice, mFilter, mTopBtn;// 综合，销量，价格，筛选,置顶
    private PullToRefreshGridView gridView;
    private StampMarketGridViewAdapter mStampMarAdapter;
    private ArrayList<GoodsStampBean.GoodsList> mList;
    private List<Fragment> mPopupList;//展示PopupWindow页面的Fragment的集合
    private String[] arr = {"自营商城", "第三方商家"};
    private int mCount;
    private boolean scrollFlag,Salesflag,Priceflag,Synthesizeflag; // 标记是否滑动,销量，价格,综合
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
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
                   String mCode = mGoodsStampBean.getRsp_code();
                    if (mCode.equals("0000")){
                        if (num == 0) {
                            initAdapter();
                        } else {
                            //设置GridView的适配器
                            setGridViewAdapter();
                        }
                    }
                    break;
                case StaticField.CG_SUCCESS://筛选
                    Gson gson1 = new Gson();
                    GoodsStampBean mGoodsStampBean1 = gson1.fromJson((String) msg.obj, GoodsStampBean.class);
                   String mCode1 = mGoodsStampBean1.getRsp_code();
                    if (mCode1.equals("0000")){
                        if (num == 0) {
                            ArrayList<GoodsStampBean.GoodsList> goods_list = mGoodsStampBean1.getGoods_list();
                            //为GridView设置适配器
                            StampMarketGridViewAdapter mStampMarAdapter = new StampMarketGridViewAdapter(SelfMallActivity.this, goods_list, mBitmap);
                            //内容GridView设置适配器
                            mGridView.setAdapter(mStampMarAdapter);
                            mStampMarAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };
    private SelfMallPanStampFilterDialog mSelfPanDialog;
    private SelfMallFilterFragment mallFragment;
    private ThreeMallFilterFragment mThreeFragment;
    private GridView mGridView;


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
        gridView = (PullToRefreshGridView) mSelfMallContent.findViewById(R.id.selfMall_gl);
        mTopBtn = (Button) mSelfMallContent.findViewById(R.id.base_top_btn);

    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mSynthesize.setOnClickListener(this);
        mSales.setOnClickListener(this);
        mPrice.setOnClickListener(this);
        mFilter.setOnClickListener(this);
        mTopBtn.setOnClickListener(this);
        mGridView = gridView.getRefreshableView();
        mGridView.setOnScrollListener(this);
        // GridView条目监听事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mGoods_sn = mList.get(i).getGoods_sn();
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.GOODS_SN, mGoods_sn);
                openActivityWitchAnimation(SelfMallDetailActivity.class,bundle);
            }
        });
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                num++;
                RequestNet(StaticField.ZH, num,StaticField.GOODSMALL, StaticField.A,"");
                MyLog.LogShitou("num===2====",""+num);
            }
        });
    }

    private void initData() {
        if (num == 0) {
            mList = new ArrayList<>();
        }
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num,StaticField.GOODSMALL, StaticField.A,"");
        MyLog.LogShitou("num=====1=======", "" + num);
    }

    private void initAdapter() {
        setGridViewAdapter();
    }
    /**
     * 设置GridView的适配器
     */
    private void setGridViewAdapter() {
        if (mGoodsStampBean != null) {
            List<GoodsStampBean.GoodsList> goods_list = mGoodsStampBean.getGoods_list();
            mList.addAll(goods_list);
            //为GridView设置适配器
            mStampMarAdapter = new StampMarketGridViewAdapter(this, mList, mBitmap);
            //内容GridView设置适配器
            mGridView.setAdapter(mStampMarAdapter);
            mStampMarAdapter.notifyDataSetChanged();

            //这句是为了防止展示到GridView处
//            gridView.requestChildFocus(mHeartll, null);
            MyLog.LogShitou("num=====3=======",""+num);
            if (num != 0) {
                MyLog.LogShitou("===1=====到这一部了吗",mList.size()+"======="+num);
                mGridView.setSelection(mStampMarAdapter.getCount()-20);
                //解决调用onRefreshComplete方法去停止刷新操作,无法取消刷新的bug
                gridView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gridView.onRefreshComplete();
                    }
                }, 800);
            }
        }
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
    private void RequestNet(final String Order_By, final int index,final String source, final String Sort,final String mJson) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSLIST);// 接口名称
                params.put(StaticField.CURRENT_INDEX, String.valueOf(index)); // 当前记录索引
                params.put(StaticField.GOODS_SOURCE,source); // 商品类型
                params.put(StaticField.ORDER_BY, Order_By); // 排序条件(排序的维度：ZH综合；XL销量；JG价格)
                params.put(StaticField.SORT_TYPE, Sort); // 排序方式(A：升序；D：降序)
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code); // 签名

                if (!mJson.equals("")) {
                    params.put(StaticField.RUERY_CONDITION, mJson); // 查询条件，json字符串
                    MyLog.LogShitou("mJson======", "mJson==" + mJson);
                }

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou(source+"/=="+"result+商城list", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                if (mJson.equals("")){
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }else{
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.CG_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
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
                mSelfPanDialog = new SelfMallPanStampFilterDialog(mPopupList,arr);
                mSelfPanDialog.show(getSupportFragmentManager(), StaticField.PANSTAMPFILTERDIALOG);
                mSelfPanDialog.setClickEnsure(this);
                break;
            case R.id.self_synthesize://综合
                setOtherButton(mSales, mPrice);
                Salesflag = true;
                Priceflag = true;
                // true代表降序,false代表升序
                if (Synthesizeflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.GOODSMALL,StaticField.D,"");
                    Synthesizeflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.GOODSMALL,StaticField.A,"");
                    Synthesizeflag = true;
                }
                break;
            case R.id.self_sales://销量
                setOtherButton(mSynthesize, mPrice);
                Synthesizeflag = true;
                Priceflag = true;

                if (Salesflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num,StaticField.GOODSMALL, StaticField.D,"");
                    Salesflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num, StaticField.GOODSMALL,StaticField.A,"");
                    Salesflag = true;
                }
                break;
            case R.id.self_price://价格
                setOtherButton(mSynthesize, mSales);
                Synthesizeflag = true;
                Salesflag = true;

                if (Priceflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num,StaticField.GOODSMALL, StaticField.D,"");
                    Priceflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.GOODSMALL,StaticField.A,"");
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
     * 添加筛选Fragment页面
     */
    private void setPopupWindowListData() {
        //初始化集合
        mPopupList = new ArrayList<>();
        // 自营商城
        mallFragment = new SelfMallFilterFragment();
        mPopupList.add(mallFragment);
        // 第三方商城
        mThreeFragment = new ThreeMallFilterFragment();
        mPopupList.add(mThreeFragment);
    }


    @Override
    public void setEnsureData() {
        String mToJson = mallFragment.getData();
        String mJson = mThreeFragment.getJson();

        MyLog.LogShitou("上传json",mToJson+"---"+mJson);

        if (mToJson != null){
            RequestNet(StaticField.ZH, num, StaticField.SC_ZY,StaticField.A,mToJson);
            MyLog.LogShitou("自营请求","-=====自营商城请求"+"=="+mToJson);
        }
        if(mJson!=null){
            RequestNet(StaticField.ZH, num, StaticField.SC_DSF,StaticField.A,mJson);
            MyLog.LogShitou("第三方请求","-=====第三方请求"+"=="+mToJson);
        }
        mSelfPanDialog.dismiss();
    }
}
