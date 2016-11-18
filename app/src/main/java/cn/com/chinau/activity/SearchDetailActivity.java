package cn.com.chinau.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.ClassifyPopupWindowAdapter;
import cn.com.chinau.adapter.HomeSearchGridViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.SearchFristListBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;


/**
 * 搜索详情页
 */
public class SearchDetailActivity extends BaseActivity implements View.OnClickListener {

    private View mSearchDetail_content;
    private View mSearchDetail_title;
    private GridView mGridView;
    private ImageView mBack,mdelete;
    private Button mClassify,mSynthesize,mSales;//分类，综合，价格
    private View mTopItem;
    private View mClassify_View;// 快递的弹出框
    private ListView mClassifyLV;
    private PopupWindow mClassifyPop;//分类的PopupWindow对象
    private ColorDrawable dw = new ColorDrawable(0x0e000000);// popup的背景
    private int num = 0 ;
    private ArrayList<SearchFristListBean.ResultList> mList;
    private static final String[] mArr = {"全部", "商城", "竞拍", "邮市", "邮票目录"};
    private String previousData; // 传过来的内容
    private EditText mScanContent;
    private boolean Synthesizeflag;// 综合的标记->升序还是降序false升序,true是降序
    private boolean Salesflag;// 价格的标记->升序还是降序false升序,true是降序
    private boolean Classifyflag;// 分类的标记->升序还是降序false升序,true是降序
    private TextView mNoOrderTv;
    private LinearLayout mTitleTabLl;

    @Override
    public View CreateTitle() {
        mSearchDetail_title = View.inflate(this, R.layout.activity_searchdetail_title, null);
        return mSearchDetail_title;
    }

    @Override
    public View CreateSuccess() {
        mSearchDetail_content = View.inflate(this, R.layout.activity_searchdetail_content, null);
        initView();
        initData();
        initListener();
        return mSearchDetail_content;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mGridView = (GridView) mSearchDetail_content.findViewById(R.id.searchDetail_gl);
        mBack = (ImageView) mSearchDetail_title.findViewById(R.id.searchdetail_title_back);
        mScanContent = (EditText) mSearchDetail_title.findViewById(R.id.search_title_scanContent);

        mdelete = (ImageView) mSearchDetail_title.findViewById(R.id.search_title_delete);

        mSynthesize = (Button) mSearchDetail_content.findViewById(R.id.searchDetail_synthesize);
        mSales = (Button) mSearchDetail_content.findViewById(R.id.searchDetail_sales);
        mClassify = (Button) mSearchDetail_content.findViewById(R.id.searchDetail_value);

        mTopItem = mSearchDetail_content.findViewById(R.id.searchDetail_tab);
        mNoOrderTv =(TextView) mSearchDetail_content.findViewById(R.id.no_order_tv);// 搜索无内容显示的布局
        mTitleTabLl =(LinearLayout) mSearchDetail_content.findViewById(R.id.title_tab_ll);// 搜索无内容隐藏的布局
    }


    private void initData() {
        //获取上一个页面传过来的数据
        Bundle bundle = getIntent().getExtras();
         previousData = bundle.getString(StaticField.SEARCHBUNDLE);
        if (previousData != null){
            mScanContent.setText(previousData);
            MyLog.LogShitou("搜索页面传过来的值", previousData);
            setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
            RequestNet(StaticField.ZH, num, StaticField.A, previousData,StaticField.QB);
        }
    }

    private void initAdapter() {
        HomeSearchGridViewAdapter mHomeSearchAdapter = new HomeSearchGridViewAdapter(this, mList, mBitmap);
        mGridView.setAdapter(mHomeSearchAdapter);
        mHomeSearchAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        mClassify.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mScanContent.setOnClickListener(this);
        mdelete.setOnClickListener(this);
        mSynthesize.setOnClickListener(this);
        mSales.setOnClickListener(this);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String goods_sn = mList.get(i).getSn(); // 获取商品编号
               String goods_type = mList.get(i).getType(); // 获取商品编号
                Bundle bundle = new Bundle();
                MyLog.LogShitou("显示的是哪个商家",goods_type);
                if (goods_type.equals("SC")){
                    bundle.putString(StaticField.GOODS_SN,goods_sn);//传入商品编号
                    openActivityWitchAnimation(SelfMallDetailActivity.class,bundle);
                }else if(goods_type.equals("YS")){
                    bundle.putString(StaticField.GOODS_SN,goods_sn);//传入商品编号
                    openActivityWitchAnimation(StampDetailActivity.class,bundle);
                }else if(goods_type.equals("JP")){
                    bundle.putString(StaticField.GOODS_SN,goods_sn);//传入商品编号
                    openActivityWitchAnimation(AuctionDetailActivity.class,bundle);
                }else if(goods_type.equals("ML")){
                    bundle.putString(StaticField.STAMPDETAIL_SN,goods_sn);//传入商品编号
                    openActivityWitchAnimation(StampTapDetailActivity.class,bundle);
                }
            }
        });
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchdetail_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.search_title_scanContent://搜索
                //传递的搜索内容
                finishWitchAnimation();
                break;
            case R.id.search_title_delete://删除输入的内容按钮
                mScanContent.setText("");
                break;
            case R.id.searchDetail_synthesize://综合
                setOtherButton(mSales,mClassify);// (价格，分类)
                Salesflag = true;
//                Classifyflag = true;
                // true代表降序,false代表升序
                if (Synthesizeflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.D, previousData,StaticField.QB);
                    Synthesizeflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.A, previousData,StaticField.QB);
                    Synthesizeflag = true;
                }
                break;
            case R.id.searchDetail_sales://价格
                setOtherButton(mSynthesize,mClassify);// (综合，分类)
                Synthesizeflag = true;
//                Classifyflag = true;
                if (Salesflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.D, previousData,StaticField.QB);
                    Salesflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSales, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.D, previousData,StaticField.QB);
                    Salesflag = true;
                }

                break;
            case R.id.searchDetail_value://分类
                setOtherButton(mSynthesize,mSales);// (综合，价格)
                Synthesizeflag = true;
                Salesflag = true;
                setDrawable(R.mipmap.top_arrow_top, mClassify, Color.parseColor("#ff0000"));
                showPopupWindow();

//                if (Classifyflag) {
//                    setDrawable(R.mipmap.top_arrow_bottom, mClassify, Color.parseColor("#ff0000"));
//                    Classifyflag = false;
//                } else {
//
//                    Classifyflag = true;
//                }
                break;
            default:
                break;
        }
    }


    /**
     * 展示分类弹出的PopupWindow
     */
    private void showPopupWindow() {
        if (mClassify_View == null) {
            mClassify_View = View.inflate(this, R.layout.popupwindow_classify, null);
        }
        mClassifyLV = (ListView) mClassify_View.findViewById(R.id.classify_lv);

        ClassifyPopupWindowAdapter adapter = new ClassifyPopupWindowAdapter(mArr, this);
        mClassifyLV.setAdapter(adapter);

        if (mClassifyPop == null) {
            mClassifyPop = new PopupWindow(mClassify_View, ScreenUtils.getScreenWidth(this), ViewGroup.LayoutParams.MATCH_PARENT, true);
        }
        // 为了响应返回键和界面外的其他界面
        mClassifyPop.setBackgroundDrawable(dw);
        mClassifyPop.showAsDropDown(mTopItem, 0, 0);

        mClassifyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String qb = mArr[i];
//               String sc = mArr[1];
//               String jp = mArr[2];
//               String ys = mArr[3];
//               String ml = mArr[4];
            MyLog.LogShitou("分类点击了啥",mArr[i]);

                if(qb.equals("全部")){
                    MyLog.LogShitou("全部分类点击了啥",mArr[i]);
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    setDrawable(R.mipmap.top_arrow_normal, mClassify, Color.parseColor("#666666"));
                    RequestNet(StaticField.ZH, num, StaticField.D, previousData,StaticField.QB);
                    mClassifyPop.dismiss();
                }else if(qb.equals("商城")){
                    MyLog.LogShitou("商城分类点击了啥",mArr[i]);
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    setDrawable(R.mipmap.top_arrow_normal, mClassify, Color.parseColor("#666666"));
                    RequestNet(StaticField.ZH, num, StaticField.D, previousData,StaticField.SC);
                    mClassifyPop.dismiss();
                }else if(qb.equals("竞拍")){
                    MyLog.LogShitou("竞拍分类点击了啥",mArr[i]);
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    setDrawable(R.mipmap.top_arrow_normal, mClassify, Color.parseColor("#666666"));
                    RequestNet(StaticField.ZH, num, StaticField.D, previousData,StaticField.JP);
                    mClassifyPop.dismiss();
                }else if(qb.equals("邮市")){
                    MyLog.LogShitou("邮市分类点击了啥",mArr[i]);
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    setDrawable(R.mipmap.top_arrow_normal, mClassify, Color.parseColor("#666666"));
                    RequestNet(StaticField.ZH, num, StaticField.D, previousData,StaticField.YS);
                    mClassifyPop.dismiss();
                }else if(qb.equals("邮票目录")){
                    MyLog.LogShitou("邮票目录分类点击了啥",mArr[i]);
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    setDrawable(R.mipmap.top_arrow_normal, mClassify, Color.parseColor("#666666"));
                    RequestNet(StaticField.ZH, num, StaticField.D, previousData,StaticField.ML);
                    mClassifyPop.dismiss();
                }

            }
        });
    }

    /**
     * 搜索结果的请求方法
     * @param Order_By 排序条件
     * @param index 索引
     * @param Sort 顺序
     * @param search 搜索条件
     * @param scope 查询范围
     */
    private void RequestNet(final String Order_By, final int index, final String Sort, final String search, final String scope) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SEARCH); // 接口名称
                params.put(StaticField.SCOPE, scope);//查询范围：QB全部；SC商城；YS邮市；JP竞拍；ML邮票目录；
                params.put(StaticField.ORDER_BY, Order_By); // ZH综合；JG价格
                params.put(StaticField.SORT_BY, Sort); // A升序；D降序
                params.put(StaticField.CURRENT_INDEX, String.valueOf(index));
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM));

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                // 搜索条件是不需要签名加密的，所以放在此行
                params.put(StaticField.CONDITION, search);//搜索条件
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou(scope+"-"+"list搜索内容",result);
                if (result.equals("-1") |result.equals("-2")) {
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS:
                    Gson gson = new Gson();
                    SearchFristListBean mSearchFristListBean = gson.fromJson((String) msg.obj, SearchFristListBean.class);
                    String code = mSearchFristListBean.getRsp_code();
                    if(code.equals("0000")){
                        mList = mSearchFristListBean.getResult_list();
                        if(mList !=null && mList.size() != 0){
                            mTitleTabLl.setVisibility(View.VISIBLE);
                            mTopItem.setVisibility(View.VISIBLE);
                            mGridView.setVisibility(View.VISIBLE);
                            mNoOrderTv.setVisibility(View.GONE);
                            initAdapter();
                        }else{
                            mTitleTabLl.setVisibility(View.GONE);// 隐藏筛选栏
                            mTopItem.setVisibility(View.GONE); // 隐藏筛选栏下的View
                            mGridView.setVisibility(View.GONE);
                            mNoOrderTv.setVisibility(View.VISIBLE); // 无信息控件显示
                            mNoOrderTv.setText(R.string.no_search_des);// 显示无搜索内信息的内容
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 设置其他button的方法
     *
     * @param btn1
     */
    private void setOtherButton(Button btn1,Button btn2) {
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
