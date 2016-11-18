package cn.com.chinau.fragment;


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

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.AuctionDetailActivity;
import cn.com.chinau.activity.SearchActivity;
import cn.com.chinau.activity.SelfMallDetailActivity;
import cn.com.chinau.activity.StampDetailActivity;
import cn.com.chinau.adapter.PanStampGridViewAdapter;
import cn.com.chinau.base.BaseFragment;
import cn.com.chinau.bean.GoodsStampBean;
import cn.com.chinau.dialog.SelfMallPanStampFilterDialog;
import cn.com.chinau.fragment.popfragment.AuctionFilterFragment;
import cn.com.chinau.fragment.popfragment.SelfMallFilterFragment;
import cn.com.chinau.fragment.popfragment.StampFilterFragment;
import cn.com.chinau.fragment.popfragment.ThreeMallFilterFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.zxing.activity.CaptureActivity;


/**
 * 淘邮票页面
 */
public class PanStampFragment extends BaseFragment implements View.OnClickListener, AbsListView.OnScrollListener, SelfMallPanStampFilterDialog.ClickEnsure {

    private View mPanStampContent;//内容View
    private View mPanStampTitle;//标题View
    private Button mFilter, mTopBtn, mSynthesize, mSales, mValue;//筛选,置顶,综合，销量，价格
    private List<Fragment> mPopupList;//展示PopupWindow页面的Fragment的集合
    private String[] arr = {"自营商城", "第三方商家", "邮市", "竞拍"};
    private GridView mPanStampGV;//本页的GridView
    private PullToRefreshGridView gridView;//本页的GridView
    private GoodsStampBean mGoodsStampBean;
    private ArrayList<GoodsStampBean.GoodsList> mList;//存放数据的集合
    private ImageView mScan, mSearch;// 扫码，搜索
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private boolean scrollFlag;
    private boolean SynthesizeFlag;// 综合的标记->（false升序,true是降序）
    private boolean SalesFlag;// 销量的标记->（false升序,true是降序）
    private boolean ValueFlag;// 价格的标记标记->（降序false升序,true是降序）
    private int num = 0;//初始索引
    private SelfMallFilterFragment mallFragment;
    private ThreeMallFilterFragment mThreeFragment;
    private SelfMallPanStampFilterDialog mSelfPanDialog;
    private StampFilterFragment mStampFragment;
    private AuctionFilterFragment mAuctionFragment;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case StaticField.SUCCESS://套邮票Lsit
                    Gson gson = new Gson();
                    mGoodsStampBean = gson.fromJson((String) msg.obj, GoodsStampBean.class);
                    String code = mGoodsStampBean.getRsp_code();
                    if (code.equals("0000")) {
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
                    String mRsp_code1 = mGoodsStampBean1.getRsp_code();
                    if (mRsp_code1.equals("0000")) {

                        if (num == 0) {
                            ArrayList<GoodsStampBean.GoodsList> stamp_list = mGoodsStampBean1.getGoods_list();
                            PanStampGridViewAdapter mPanStampAdapter = new PanStampGridViewAdapter(getActivity(), stamp_list, mBitmap);
                            gridView.setAdapter(mPanStampAdapter);
                            mPanStampAdapter.notifyDataSetChanged();
                        }
                    }
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
        mValue = (Button) mPanStampContent.findViewById(R.id.panStamp_value);
        mFilter = (Button) mPanStampContent.findViewById(R.id.panStamp_filter);

        gridView = (PullToRefreshGridView) mPanStampContent.findViewById(R.id.panstamp_gl);
        mTopBtn = (Button) mPanStampContent.findViewById(R.id.stamp_top_btn);
    }

    /**
     * 添加数据
     */
    private void initData() {
        if (num == 0) {
            mList = new ArrayList<>();
        }
        // 初始化第一个按钮
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num,StaticField.GOODSSOURCE, StaticField.D, "");
    }

    /**
     * 装载适配器
     */
    private void initAdapter() {
        setGridViewAdapter();
    }

    /**
     * 设置GridView的适配器
     */
    private void setGridViewAdapter() {
        if (mGoodsStampBean != null) {
            List<GoodsStampBean.GoodsList> stamp_list = mGoodsStampBean.getGoods_list();
            ;
            mList.addAll(stamp_list);
            //为GridView设置适配器
            PanStampGridViewAdapter mPanStampAdapter = new PanStampGridViewAdapter(getActivity(), mList, mBitmap);
            gridView.setAdapter(mPanStampAdapter);
            mPanStampAdapter.notifyDataSetChanged();
//            MyLog.LogShitou("num=====3=======",""+num);
            if (num != 0) {
                mPanStampGV.setSelection(mPanStampAdapter.getCount() - 20);
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
        // 获取需要刷新的mPanStampGV
        mPanStampGV = gridView.getRefreshableView();
        mPanStampGV.setOnScrollListener(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mSource = mList.get(i).getGoods_source();
                String mGoodsSn = mList.get(i).getGoods_sn();
                Bundle bundle = new Bundle();
                MyLog.LogShitou("哪个商家-->:", mSource);
                if (mSource.equals("SC_ZY")) {
                    bundle.putString(StaticField.GOODS_SN, mGoodsSn);
                    openActivityWitchAnimation(SelfMallDetailActivity.class, bundle);
                } else if (mSource.equals("YS")) {
                    bundle.putString(StaticField.GOODS_SN, mGoodsSn);
                    openActivityWitchAnimation(StampDetailActivity.class, bundle);
                } else if (mSource.equals("JP")) {
                    bundle.putString(StaticField.GOODS_SN, mGoodsSn);
                    openActivityWitchAnimation(AuctionDetailActivity.class, bundle);
                } else if (mSource.equals("SC_DSF")) {
                    bundle.putString(StaticField.GOODS_SN, mGoodsSn);
                    openActivityWitchAnimation(SelfMallDetailActivity.class, bundle);
                }
            }
        });

        // 上拉加载更多
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                num++;
                RequestNet(StaticField.ZH, num, StaticField.GOODSSOURCE,StaticField.D, "");
                MyLog.LogShitou("num===2====", "" + num);
            }
        });
    }

    @Override
    public void AgainRequest() {

    }


    /**
     * 套邮票list网络请求
     *
     * @param Order_By 类别
     * @param index    角标
     * @param Sort     排序
     */
    private void RequestNet(final String Order_By, final int index, final String source, final String Sort, final String mJson) {
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
                    MyLog.LogShitou("==1111=========mJson有值吗", mJson);
                    params.put(StaticField.RUERY_CONDITION, mJson); // 类别，json串
                }

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("result+淘邮票", result);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                if (mJson.equals("")) {
                    MyLog.LogShitou("net==2222=========num", "num=====" + num);
                    Message msg = mHandler.obtainMessage();
                    msg.obj = result;
                    msg.what = StaticField.SUCCESS;
                    mHandler.sendMessage(msg);
                } else { // 筛选后请求的数据
                    Message msg = mHandler.obtainMessage();
                    msg.obj = result;
                    msg.what = StaticField.CG_SUCCESS;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.panstamp_title_scan://扫码
                Bundle bundle1 = new Bundle();
                bundle1.putString("SanFragment", "PanStampFragment");
                openActivityWitchAnimation(CaptureActivity.class, bundle1);
                break;
            case R.id.panstamp_search://搜索
                openActivityWitchAnimation(SearchActivity.class);
                break;
            case R.id.panStamp_synthesize://综合
                mList.clear();

                Log.e("综合~~>", "综合。。。。。");
                setOtherButton(mSales, mValue);
                SalesFlag = true;// 销量为true
                ValueFlag = true;// 价格为true
                // true代表降序,false代表升序
                if (SynthesizeFlag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num,StaticField.GOODSSOURCE, StaticField.D, "");
                    SynthesizeFlag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num,StaticField.GOODSSOURCE, StaticField.A, "");
                    SynthesizeFlag = true;
                }
                break;
            case R.id.panStamp_sales://销量
                mList.clear();
                Log.e("销量~~>", "销量。。。。。");
                setOtherButton(mSynthesize, mValue);
                SynthesizeFlag = true;// 综合为true
                ValueFlag = true;// 价格为true
                // true代表降序,false代表升序
                if (SalesFlag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num,StaticField.GOODSSOURCE, StaticField.D, "");
                    SalesFlag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.XL, num,StaticField.GOODSSOURCE, StaticField.A, "");
                    SalesFlag = true;
                }
                break;
            case R.id.panStamp_value://价格
                mList.clear();
                Log.e("价格~~>", "价格。。。。。");
                setOtherButton(mSynthesize, mSales);
                SynthesizeFlag = true;// 综合为true
                SalesFlag = true;// 销量为true
                // true代表降序,false代表升序
                if (ValueFlag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mValue, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num,StaticField.GOODSSOURCE, StaticField.D, "");
                    ValueFlag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mValue, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num,StaticField.GOODSSOURCE, StaticField.A, "");
                    ValueFlag = true;
                }
                break;
            case R.id.panStamp_filter://筛选
                // 筛选有点问题
                setPopupWindowListData();
                mSelfPanDialog = new SelfMallPanStampFilterDialog(mPopupList, arr);
                mSelfPanDialog.setClickEnsure(this);
                mSelfPanDialog.show(getChildFragmentManager(), StaticField.PANSTAMPFILTERDIALOG);
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
//        mPopupList = new ArrayList<>();
//        ThreeMallFilterFragment mPanStampFragment = new ThreeMallFilterFragment();
//        mPopupList.add(mPanStampFragment);
//        mPopupList.add(mPanStampFragment);
//        mPopupList.add(mPanStampFragment);
//        mPopupList.add(null);

        //初始化集合
        mPopupList = new ArrayList<>();
        // 自营商城
        mallFragment = new SelfMallFilterFragment();
        mPopupList.add(mallFragment);
        // 第三方商家
        mThreeFragment = new ThreeMallFilterFragment();
        mPopupList.add(mThreeFragment);
        // 邮市
        mStampFragment = new StampFilterFragment();
        mPopupList.add(mStampFragment);
        // 竞拍
        mAuctionFragment = new AuctionFilterFragment();
        mPopupList.add(mAuctionFragment);
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


    /**
     * 筛选回调的回来的数据
     */
    @Override
    public void setEnsureData() {
        String mToJson = mallFragment.getData();
        String mJson = mThreeFragment.getJson();
        String mStampdata = mStampFragment.getData();
        String mAuctiondata = mAuctionFragment.getData();

        MyLog.LogShitou("===========请求上传的Json串", mToJson+"==/"+mJson+"==/"+mStampdata+"==/"+mAuctiondata);

        //  此处注意请求网络里面的字段数据StaticField.SC_ZY,StaticField.SC_DSF
        //  筛选商品类型不同，传不同类型
        // 自营商城
        if (mToJson != null) {
            setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
            RequestNet(StaticField.ZH, num, StaticField.SC_ZY,StaticField.D, mToJson); // 淘邮票请求网络的方法
            MyLog.LogShitou("===点中了自营商城-->" , mToJson );
        }
        // 第三方
        if (mJson != null) {
            setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
            RequestNet(StaticField.ZH, num, StaticField.SC_DSF,StaticField.D, mJson); // 淘邮票请求网络的方法
            MyLog.LogShitou("===点中了第三方-->" , mJson);
        }


        // 邮市
        if (mStampdata != null) {

        }
        // 竞拍
        if (mAuctiondata != null) {


        }

        mSelfPanDialog.dismiss();
    }
}
