package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.AuctionListViewAdapter;
import cn.com.chinau.adapter.StampHorizontalListViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.CategoryBean;
import cn.com.chinau.bean.CategoryGoodsJsonBean;
import cn.com.chinau.bean.GoodsStampBean;
import cn.com.chinau.bitmap.BitmapHelper;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.listener.GestureListener;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.HorizontalListView;

/**
 * 竞拍页面
 */
public class AuctionActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {

    private View mAuctionContent, mAuctionTitle, mBlankView;
    private HorizontalListView hListView;//筛选横向滑动的listView
    private StampHorizontalListViewAdapter hListViewAdapter;//筛选横向花的listView的适配器
    private ArrayList<GoodsStampBean.GoodsList> mList;
    private ListView mListView;
    private PullToRefreshListView listView;
    private ImageView mBack, mSearch;//返回按钮,搜索
    private TextView mTitle,mOederTv;
    // 新中国邮票, 民国邮票, 解放区邮票, 清代邮票
    private RadioButton mNewChinese, mRepublicChina, mLiberatedArea, mQingDynasty;

    private int mColorGray = Color.parseColor("#dddfe3");// 横线灰色
    private int mColorRed = Color.parseColor("#e20000");// 横线红色
    private Button mTopBtn, mSynthesize, mOver, mCamera;// 置顶，综合，即将结束，等待开拍
    private boolean Synthesizeflag;// 综合的标记->升序还是降序false升序,true是降序
    private boolean Overflag;// 即将结束的标记->升序还是降序false升序,true是降序
    private boolean Cameraflag;// 等待开拍的标记->升序还是降序false升序,true是降序

    private boolean scrollFlag = false; // 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private LinearLayout mHeartll;//头部的布局
    private GestureDetector mGestureDetector;
    private int num = 0;//初始索引
    public static final int SUCCESS = 1;
    private GoodsStampBean mGoodsStampBean;
    private String[] string0, string2, string3, string4;
    private RadioGroup mRadioGroup;
    private SharedPreferences sp;
    private ArrayList<CategoryBean.Category.SubCategory> subCategory1;
    private int Flag = 0;  // 类别标记
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case StaticField.SUCCESS://竞拍Lsit
                    Gson gson = new Gson();
                    mGoodsStampBean = gson.fromJson((String) msg.obj, GoodsStampBean.class);
                   String mRsp_code =  mGoodsStampBean.getRsp_code();
                    if (mRsp_code.equals("0000")){
                        if (num == 0) {
//                            mList.clear(); // 清除之前的mList
                            initAdapter();
                            MyLog.LogShitou("====initAdapter==============num","============"+num);

                        } else {

                            //设置GridView的适配器
                            setGridViewAdapter();
                        }
                    }
                    break;
                case 12://筛选
                    Gson gson1 = new Gson();
                    mGoodsStampBean = gson1.fromJson((String) msg.obj, GoodsStampBean.class);
                    String mRsp_code1 = mGoodsStampBean.getRsp_code();
                    if (mRsp_code1.equals("0000")) {
                        if (num == 0) {
//                            ArrayList<GoodsStampBean.GoodsList> goods_list = mGoodsStampBean1.getGoods_list();
//                            if (goods_list !=null && goods_list.size() != 0){
//                                mListView.setVisibility(View.GONE);
//                                AuctionListViewAdapter mListAdapter = new AuctionListViewAdapter(AuctionActivity.this, mBitmap, goods_list);
//                                mListView.setAdapter(mListAdapter);
//                                mListAdapter.notifyDataSetChanged();
//                            }else{
//                                initAdapter();
//                            }

                            mList.clear(); // 清除之前的mList
                            initAdapter();
                        }else{
                            initAdapter();
                        }

                    }
                    break;
//                case 1:
//                   String url0 = (String) msg.obj;
//                    Drawable topDrawable0 = BitmapHelper.getDrawable(url0);
//                    topDrawable0.setBounds(0, 0, topDrawable0.getMinimumWidth(), topDrawable0.getMinimumHeight());
//                    mNewChinese.setCompoundDrawables(null, topDrawable0, null, null);
//                break;
//                case 2:
//                   String url2 = (String) msg.obj;
//                    Drawable topDrawable2 = BitmapHelper.getDrawable(url2);
//                    topDrawable0.setBounds(0, 0, topDrawable0.getMinimumWidth(), topDrawable0.getMinimumHeight());
//                    mNewChinese.setCompoundDrawables(null, topDrawable0, null, null);
//                break;
//                case 3:
//                   String url3 = (String) msg.obj;
//                    Drawable topDrawable3 = BitmapHelper.getDrawable(url3);
//                    topDrawable0.setBounds(0, 0, topDrawable0.getMinimumWidth(), topDrawable0.getMinimumHeight());
//                    mNewChinese.setCompoundDrawables(null, topDrawable0, null, null);
//                break;
//                case 4:
//                   String url4 = (String) msg.obj;
//                    Drawable topDrawable4 = BitmapHelper.getDrawable(url4);
//                    topDrawable0.setBounds(0, 0, topDrawable0.getMinimumWidth(), topDrawable0.getMinimumHeight());
//                    mNewChinese.setCompoundDrawables(null, topDrawable0, null, null);
//                break;

                case SUCCESS://一级类别名字
                    String[] mArrTitle = (String[] ) msg.obj;
                    String title0 = mArrTitle[0];
                    mNewChinese.setText(title0);
                    String title1 = mArrTitle[1];
                    mRepublicChina.setText(title1);
                    String title2= mArrTitle[2];
                    mLiberatedArea.setText(title2);
                    String title3 = mArrTitle[3];
                    mQingDynasty.setText(title3);
                    MyLog.LogShitou("竞拍一级类别----->:", title0 + "--" + title2 + "--" + title3 + "--" + title1);
                    break;
                case 2:
                    Drawable mTopDrawable0 = (Drawable) msg.obj;
                    mNewChinese.setCompoundDrawables(null, mTopDrawable0, null, null);
                    break;
                case 3:
                    Drawable mTopDrawable2 = (Drawable) msg.obj;
                    mRepublicChina.setCompoundDrawables(null, mTopDrawable2, null, null);
                    break;
                case 4:
                    Drawable mTopDrawable3 = (Drawable) msg.obj;
                    mLiberatedArea.setCompoundDrawables(null, mTopDrawable3, null, null);
                    break;
                case 5:
                    Drawable mTopDrawable4 = (Drawable) msg.obj;
                    mQingDynasty.setCompoundDrawables(null, mTopDrawable4, null, null);
                    break;
                case 6:// 二级类别名字
                    List<String[]> mArrList = (List<String[]>) msg.obj;
                    // 获取
                    string0 = mArrList.get(0);
                    string2= mArrList.get(1);
                    string3 = mArrList.get(2);
                    string4 = mArrList.get(3);
                    // 横向的listView设置适配器
                    hListViewAdapter = new StampHorizontalListViewAdapter(AuctionActivity.this, string0);
                    hListView.setAdapter(hListViewAdapter);
                    MyLog.LogShitou("竞拍几个数-->:", mArrList.size() + "--" + string0.length + "----" + string2.length + "--" + string3.length + "--" + string4.length);
                    break;
                case 7:
                    Drawable TopDrawable0 = (Drawable) msg.obj;
                    mNewChinese.setCompoundDrawables(null, TopDrawable0, null, null);
                    break;
                case 8:
                    Drawable TopDrawable2 = (Drawable) msg.obj;
                    mRepublicChina.setCompoundDrawables(null, TopDrawable2, null, null);
                    break;
                case 9:
                    Drawable TopDrawable3 = (Drawable) msg.obj;
                    mLiberatedArea.setCompoundDrawables(null, TopDrawable3, null, null);
                    break;
                case 10:
                    Drawable TopDrawable4 = (Drawable) msg.obj;
                    mQingDynasty.setCompoundDrawables(null, TopDrawable4, null, null);
                    break;
                case 11:
                    MyToast.showShort(AuctionActivity.this,"请检查网络连接");
                    break;
                default:
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
        sp = getSharedPreferences(StaticField.NAME,MODE_PRIVATE);
        initView();
        initData();
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
        listView = (PullToRefreshListView) mAuctionContent.findViewById(R.id.auction_lv);// 竖向ListView
        mRadioGroup = (RadioGroup) mAuctionContent.findViewById(R.id.stamp_RadioGroup);

        mNewChinese = (RadioButton) mAuctionContent.findViewById(R.id.auction_newchinese_btn);
        mRadioGroup.check(R.id.auction_newchinese_btn);
        mRepublicChina = (RadioButton) mAuctionContent.findViewById(R.id.auction_republicChina_btn);
        mLiberatedArea = (RadioButton) mAuctionContent.findViewById(R.id.auction_liberatedArea_btn);
        mQingDynasty = (RadioButton) mAuctionContent.findViewById(R.id.auction_qingDynasty_btn);
        mSynthesize = (Button) mAuctionContent.findViewById(R.id.auction_synthesize);
        mOver = (Button) mAuctionContent.findViewById(R.id.auction_over);
        mCamera = (Button) mAuctionContent.findViewById(R.id.auction_camera);
        mOederTv = (TextView )mAuctionContent.findViewById(R.id.no_order_tv);
        initGestureListener();// 滑动lsitview隐藏导航栏的方法

    }
    /**
     * 初始化第一个按钮
     */
    private void initData() {
        GetInitCategory(); //获取保存在本地竞拍类别数据
        if (num == 0) {
            mList = new ArrayList<>();
        }
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));

        RequestNet(StaticField.ZH, num, StaticField.D,"");
    }

    private void initAdapter() {
        setGridViewAdapter(); // 设置GridView的适配器
    }

    /**
     * 设置GridView的适配器
     */
    private void setGridViewAdapter() {
        if (mGoodsStampBean != null) {
            List<GoodsStampBean.GoodsList> goods_list = mGoodsStampBean.getGoods_list();
            mList.addAll(goods_list);
           if (mList !=null){
               listView.setVisibility(View.VISIBLE);
               AuctionListViewAdapter mListAdapter = new AuctionListViewAdapter(AuctionActivity.this, mBitmap, mList);
               mListView.setAdapter(mListAdapter);
               mListAdapter.notifyDataSetChanged();

//            //这句是为了防止展示到listView处
//            listView.requestChildFocus(mHeartll, null);
               MyLog.LogShitou("num=====3=======", "" + num);
               if (num != 0) {
                   MyLog.LogShitou("===1=====到这一部了吗", mList.size() + "=======" + num);
                   mListView.setSelection(mListAdapter.getCount() - 20);

                   //解决调用onRefreshComplete方法去停止刷新操作,无法取消刷新的bug
                   listView.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           listView.onRefreshComplete();
                       }
                   }, 800);
               }
           }else{
               GoneOrVisibleView(); // ListView为空时显示的布局
           }

        }
    }

    // ListView为空时显示的布局
    private void GoneOrVisibleView() {
        mListView.setVisibility(View.GONE);
        mOederTv.setVisibility(View.VISIBLE); // 无信息控件显示
        mOederTv.setText("暂无竞拍商品信息~");
    }

    /**
     * 滑动lsitview隐藏导航栏的方法
     */
    private void initGestureListener() {
        mHeartll = (LinearLayout) mAuctionContent.findViewById(R.id.auction_heart);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mHeartll.measure(w, h);
        int height = mHeartll.getMeasuredHeight();
        MyLog.e(height + "");
        GestureListener gestureListener = new GestureListener(mHeartll, height);
        mGestureDetector = new GestureDetector(this, gestureListener);
    }

    // 获取保存在本地竞拍类别数据
    private void GetCategoryData(){
        String Category6 = sp.getString("Category6","");
        MyLog.LogShitou("获取本地竞拍类别数据", Category6);
        if(Category6 != null){
            Gson gson = new Gson();
            CategoryBean mCategoryBean = gson.fromJson(Category6, CategoryBean.class);
            ArrayList<CategoryBean.Category> mCategory = mCategoryBean.getCategory();
            CategoryBean.Category category = mCategory.get(0);
            ArrayList<CategoryBean.Category.SubCategory> subCategory1 = category.getSubCategory();

            int sub = subCategory1.size();// 获取subCategory1的个数
            String[] mArrTitle = new String[sub];// 一级分类
            String[] mImgUrl = new String[sub];// url
            //二级分类
            List<String[]> mArrList = new ArrayList<>();
//        List<String[]> mImgUrlList = new ArrayList<>();
            // 循环出一级分类的名字
            for (int i = 0; i < subCategory1.size(); i++) {
                mArrTitle[i] = subCategory1.get(i).getName();
                mImgUrl[i] = subCategory1.get(i).getImg_url();// 图片的url
                MyLog.LogShitou("竞拍一级类别0001----->:", mArrTitle[i] + "--" + mImgUrl[i]);
                ArrayList<CategoryBean.Category.SubCategory.SmllSubCategoryData> subCategory = subCategory1.get(i).getSubCategory();
                String[] mArr = new String[subCategory.size()];
                // 循环出二级分类名字
                for (int j = 0; j < subCategory.size(); j++) {
                    mArr[j] = subCategory.get(j).getName();
//                        MyLog.LogShitou("竞拍二级类别0" + j + "----->:", mArr[j]);
                }
                mArrList.add(mArr);
            }
            // 获取单个的一级标题Title
            String title0 = mArrTitle[0];
            mNewChinese.setText(title0);
            String title1 = mArrTitle[2];
            mRepublicChina.setText(title1);
            String title2 = mArrTitle[3];
            mLiberatedArea.setText(title2);
            String title3 = mArrTitle[4];
            mQingDynasty.setText(title3);
            MyLog.LogShitou("竞拍一级类别----->:", title0 + "--" + title1 + "--" + title2 + "--" + title3);

            // 获取二级分类title的list
            string0 = mArrList.get(0);
            string2 = mArrList.get(2);
            string3 = mArrList.get(3);
            string4 = mArrList.get(4);

            // 横向的listView设置适配器
            hListViewAdapter = new StampHorizontalListViewAdapter(AuctionActivity.this, string0);
            hListView.setAdapter(hListViewAdapter);
            MyLog.LogShitou("竞拍二级分类title的个数-->:", mArrList.size() + "----" + string0.length + "--" + string2.length + "--" + string3.length + "--" + string4.length);

            // 获取单个的一级标题url
            String imgurl0 = mImgUrl[0];
            String imgurl2 = mImgUrl[2];
            String imgurl3 = mImgUrl[3];
            String imgurl4 = mImgUrl[4];

            // 如个url为空，设置默认图片
            Drawable drawable = getResources().getDrawable(R.mipmap.weixin);
            // 判断url是否为空
            if (imgurl0.equals("")) {
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mNewChinese.setCompoundDrawables(null, drawable, null, null);
            } else {

                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = imgurl0;
                mHandler.sendMessage(msg);

            }
            if (imgurl2.equals("")) {
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mNewChinese.setCompoundDrawables(null, drawable, null, null);

            } else {
                Drawable topDrawable2 = BitmapHelper.getDrawable(imgurl2);
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.obj = topDrawable2;
                mHandler.sendMessage(msg);

                topDrawable2.setBounds(0, 0, topDrawable2.getMinimumWidth(), topDrawable2.getMinimumHeight());
                mNewChinese.setCompoundDrawables(null, topDrawable2, null, null);
            }
            if (imgurl3.equals("")) {
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mNewChinese.setCompoundDrawables(null, drawable, null, null);
            } else {
                Drawable topDrawable3 = BitmapHelper.getDrawable(imgurl3);

                Message msg = mHandler.obtainMessage();
                msg.what = 3;
                msg.obj = topDrawable3;
                mHandler.sendMessage(msg);

                topDrawable3.setBounds(0, 0, topDrawable3.getMinimumWidth(), topDrawable3.getMinimumHeight());
                mNewChinese.setCompoundDrawables(null, topDrawable3, null, null);

            }
            if (imgurl4.equals("")) {
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mNewChinese.setCompoundDrawables(null, drawable, null, null);
            } else {
                Drawable topDrawable4 = BitmapHelper.getDrawable(imgurl4);

                Message msg = mHandler.obtainMessage();
                msg.what = 4;
                msg.obj = topDrawable4;
                mHandler.sendMessage(msg);

                topDrawable4.setBounds(0, 0, topDrawable4.getMinimumWidth(), topDrawable4.getMinimumHeight());
                mNewChinese.setCompoundDrawables(null, topDrawable4, null, null);
            }
        }
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
        mListView = listView.getRefreshableView();
        mListView.setOnScrollListener(this);
        // 上拉加载更多
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                num++;
                RequestNet(StaticField.ZH, num, StaticField.A,"");
                MyLog.LogShitou("num===2====", "" + num);
            }
        });

        //横向ListView的点击事件
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                num =0; // 初始化索引
                if (Flag == 0) {
                    //设置点击背景的方法
                    hListViewAdapter.setSelection(i);
                    hListViewAdapter.notifyDataSetChanged();
                    String name = subCategory1.get(0).getName();
                    String mValue = subCategory1.get(0).getValue();// 一级类别标题
                    String mValues = subCategory1.get(0).getSubCategory().get(i).getValue();//  二级类别标题
                    String tojson = GoodsJsonBean(mValue, mValues);

//                    MyLog.LogShitou("-000===转换成的Json", tojson+"=="+mValue+"=="+mValues+"=="+name);
                    RequestNet(StaticField.ZH, num, StaticField.A, tojson); // 筛选请求

                } else if (Flag == 1) {
                    //设置点击背景的方法
                    hListViewAdapter.setSelection(i);
                    hListViewAdapter.notifyDataSetChanged();
                    String name = subCategory1.get(1).getName();
                    String mValue = subCategory1.get(1).getValue();
                    String mValues = subCategory1.get(1).getSubCategory().get(i).getValue();
                    String tojson = GoodsJsonBean(mValue, mValues);
//                    MyLog.LogShitou("-111===转换成的Json", tojson+"=="+mValue+"=="+mValues+"=="+name);
                    RequestNet(StaticField.ZH, num, StaticField.A, tojson); // 筛选请求
                } else if (Flag == 2) {
                    //设置点击背景的方法
                    hListViewAdapter.setSelection(i);
                    hListViewAdapter.notifyDataSetChanged();
                    String name = subCategory1.get(2).getName();
                    String mValue = subCategory1.get(2).getValue();
                    String mValues = subCategory1.get(2).getSubCategory().get(i).getValue();

                    String tojson = GoodsJsonBean(mValue, mValues);
//                    MyLog.LogShitou("-222===转换成的Json", tojson+"=="+mValue+"=="+mValues+"=="+name);
                    RequestNet(StaticField.ZH, num, StaticField.A, tojson); // 筛选请求
                } else if (Flag == 3) {
                    //设置点击背景的方法
                    hListViewAdapter.setSelection(i);
                    hListViewAdapter.notifyDataSetChanged();
                    String name = subCategory1.get(3).getName();
                    String mValue = subCategory1.get(3).getValue();
                    String mValues = subCategory1.get(3).getSubCategory().get(i).getValue();
                    String tojson = GoodsJsonBean(mValue, mValues);
//                    MyLog.LogShitou("-333===转换成的Json", tojson+"=="+mValue+"=="+mValues+"=="+name);
                    RequestNet(StaticField.ZH, num, StaticField.A, tojson); // 筛选请求
                }
            }
        });
        // mListView的条目监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //去往竞拍详情页
                Bundle bundle = new Bundle();
                String mGoods_sn = mList.get(i-1).getGoods_sn();
                bundle.putString(StaticField.GOODS_SN, mGoods_sn);// 传入商品编号
//                MyLog.LogShitou("mGoods_sn竞拍商品编号-->", mGoods_sn);
                openActivityWitchAnimation(AuctionDetailActivity.class, bundle);
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
        initData();
    }


    /**
     * 筛选数据获取的Json串
     *
     * @param mValue  一级Value值
     * @param mValues 二级Value值
     * @return
     */
    private String GoodsJsonBean(String mValue, String mValues) {
        CategoryGoodsJsonBean mGoodsJsonBean = new CategoryGoodsJsonBean();
        CategoryGoodsJsonBean.CategoryBean mCategoryBean = new CategoryGoodsJsonBean.CategoryBean();
        mCategoryBean.setCategory(mValue);// 一级
        CategoryGoodsJsonBean.CategoryBean.SubBean mSubBean = new CategoryGoodsJsonBean.CategoryBean.SubBean();
        mSubBean.setSub(mValues);// 二级
        mCategoryBean.setSubCategory(mSubBean);
        mGoodsJsonBean.setCategory(mCategoryBean);
        String toJson = new Gson().toJson(mGoodsJsonBean);
        return toJson;
    }
    /**
     * 竞拍类别查询网络请求
     */
    private void GetInitCategory() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
                params.put(StaticField.OP_TYPE, "JP");// 竞拍
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("竞拍类别查询--->", result);
                if (result.equals("-1")|result.equals("-2")) {
                    GetInitCategory();
                    return;
                }
                Gson gsons = new Gson();
                CategoryBean mCategoryBean = gsons.fromJson(result, CategoryBean.class);
                String mRsp_code = mCategoryBean.getRsp_code();
                // 判断code是否返回0000
                if(mRsp_code.equals("0000")){
                    ArrayList<CategoryBean.Category> mCategory = mCategoryBean.getCategory();
                    CategoryBean.Category category = mCategory.get(0);
                    subCategory1 = category.getSubCategory();
                    // 外分类
                    int sub = subCategory1.size();// 获取subCategory1的个数
                    String[] mArrTitle = new String[sub];// 一级分类
                    String[] mImgUrl = new String[sub];// url
                    Message msge = mHandler.obtainMessage();
                    msge.what = SUCCESS;
                    msge.obj = mArrTitle;
                    mHandler.sendMessage(msge);
                    //子分类
                    List<String[]> mArrList = new ArrayList<>();
                    List<String[]> mImgUrlList = new ArrayList<>();
                    // 循环出一级分类的名字
                    for (int i = 0; i < subCategory1.size(); i++) {
                        mArrTitle[i] = subCategory1.get(i).getName();
                        mImgUrl[i] = subCategory1.get(i).getImg_url();// 图片的url
                        MyLog.LogShitou("竞拍一级类别0001----->:", mArrTitle[i] + "--" + mImgUrl[i]);
                        ArrayList<CategoryBean.Category.SubCategory.SmllSubCategoryData> subCategory = subCategory1.get(i).getSubCategory();
                        String[] mArr = new String[subCategory.size()];
                        // 循环出二级分类名字
                        for (int j = 0; j < subCategory.size(); j++) {
                            mArr[j] = subCategory.get(j).getName();
//                        MyLog.LogShitou("竞拍二级类别0" + j + "----->:", mArr[j]);
                        }
                        mArrList.add(mArr);
                    }
                    String url = "http://test.chinau.com.cn:8081/chinau-imgserver/attachment//designer/20160429/20160429081b712e-ed1a-4341-839f-af6571109a03.png";
                    // 获取单个的url
                    String imgurl0 = mImgUrl[0];
                    String imgurl2 = mImgUrl[1];
                    String imgurl3 = mImgUrl[2];
                    String imgurl4 = mImgUrl[3];
                    Drawable drawable = getResources().getDrawable(R.mipmap.weixin); // 如个url为空，设置默认图片
                    // 判断url是否为空
                    if (imgurl0.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 7;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);
                    } else {
                        Drawable topDrawable0 = BitmapHelper.getDrawable(imgurl0);
                        topDrawable0.setBounds(0, 0, topDrawable0.getMinimumWidth(), topDrawable0.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 2;
                        msg.obj = topDrawable0;
                        mHandler.sendMessage(msg);
                    }
                    if (imgurl2.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 8;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);
                    } else {
                        Drawable topDrawable2 = BitmapHelper.getDrawable(imgurl2);
                        topDrawable2.setBounds(0, 0, topDrawable2.getMinimumWidth(), topDrawable2.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 3;
                        msg.obj = topDrawable2;
                        mHandler.sendMessage(msg);
                    }
                    if (imgurl3.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 9;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);
                    } else {
                        Drawable topDrawable3 = BitmapHelper.getDrawable(imgurl3);
                        topDrawable3.setBounds(0, 0, topDrawable3.getMinimumWidth(), topDrawable3.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 4;
                        msg.obj = topDrawable3;
                        mHandler.sendMessage(msg);
                    }
                    if (imgurl4.equals("")) {
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 10;
                        msg.obj = drawable;
                        mHandler.sendMessage(msg);
                    } else {
                        Drawable topDrawable4 = BitmapHelper.getDrawable(imgurl4);
                        topDrawable4.setBounds(0, 0, topDrawable4.getMinimumWidth(), topDrawable4.getMinimumHeight());
                        Message msg = mHandler.obtainMessage();
                        msg.what = 5;
                        msg.obj = topDrawable4;
                        mHandler.sendMessage(msg);
                    }
                    // 发送二级分类名字
                    Message msg = mHandler.obtainMessage();
                    msg.what = 6;
                    msg.obj = mArrList;
                    mHandler.sendMessage(msg);
                }else{
                    mHandler.sendEmptyMessage(11);
                }
            }
        });
    }

    /**
     * 商城list网络请求
     *
     * @param Order_By 类别
     * @param index    角标
     * @param Sort     排序
     */
    private void RequestNet(final String Order_By, final int index, final String Sort,final String mJson) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.GOODSLIST);// 接口名称
                params.put(StaticField.CURRENT_INDEX, String.valueOf(index)); // 当前记录索引
                params.put(StaticField.GOODS_SOURCE, StaticField.JP); // 商品类型
                params.put(StaticField.ORDER_BY, Order_By); // 排序条件(排序的维度：ZH综合；JS即将结束；KP等待开拍)
                params.put(StaticField.SORT_TYPE, Sort); // 排序方式(A：升序；D：降序)
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                if (!mJson.equals("")) {
                    params.put(StaticField.RUERY_CONDITION, mJson); // 查询条件，json字符串
                    MyLog.LogShitou("mJson======", "mJson==" + mJson);
                }
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou(Order_By+"/=="+"result+竞拍List", result);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                if (mJson.equals("")){

                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                    MyLog.LogShitou("走到这了==========111111111111111", "走到哪了=========1111");
                }else{
                    Message msg = mHandler.obtainMessage();
                    msg.what = 12;// 筛选
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                    MyLog.LogShitou("走到这了吗==========2222222222222222", "走到哪了=========2222");
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
            case R.id.auction_newchinese_btn://新中国邮票
                Flag = 0;
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string0);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.auction_republicChina_btn:// 民国邮票
                Flag = 1;
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string2);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.auction_liberatedArea_btn://解放区邮票
                Flag = 2;
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string3);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.auction_qingDynasty_btn://清代邮票
                Flag = 3;
                hListViewAdapter = new StampHorizontalListViewAdapter(this, string4);
                hListView.setAdapter(hListViewAdapter);
                hListViewAdapter.notifyDataSetChanged();
                break;
            case R.id.base_top_btn://置顶
                setListViewPos(0);
                mTopBtn.setVisibility(View.GONE);// 回到顶部后置顶按钮隐藏
                break;
            case R.id.auction_synthesize://综合
                    mList.clear();
                setOtherButton(mOver, mCamera);
                Overflag = true;
                Cameraflag = true;
                // true代表降序,false代表升序
                if (Synthesizeflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.D,"");
                    MyLog.LogShitou("00000======DDDDD=======num","============"+num);
                    Synthesizeflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.A,"");
                    MyLog.LogShitou("00000=====AAAA========num","============"+num);
                    Synthesizeflag = true;
                }
                break;
            case R.id.auction_over://即将结束
                mList.clear();
                setOtherButton(mSynthesize, mCamera);
                Synthesizeflag = true;
                Cameraflag = true;

                if (Overflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mOver, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JS, num, StaticField.D,"");
                    MyLog.LogShitou("111111111========DDDDDDD=====num","============"+num);
                    Overflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mOver, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JS, num, StaticField.A,"");
                    MyLog.LogShitou("11111111111=======AAAAAAAAAAAA=====num","============"+num);
                    Overflag = true;
                }
                break;
            case R.id.auction_camera://等待开拍
                mList.clear();

                setOtherButton(mSynthesize, mOver);
                Synthesizeflag = true;
                Overflag = true;

                if (Cameraflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mCamera, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.KP, num, StaticField.D,"");
                    MyLog.LogShitou("2222222=======DDDDDDDDDD=====num","============"+num);
                    Cameraflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mCamera, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.KP, num, StaticField.A,"");
                    MyLog.LogShitou("22222=======AAAAAA=====num","============"+num);
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
                        .getCount() - 1)) {
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
