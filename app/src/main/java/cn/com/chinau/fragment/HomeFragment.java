package cn.com.chinau.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.AuctionActivity;
import cn.com.chinau.activity.DesignerActivity;
import cn.com.chinau.activity.ScanActivity;
import cn.com.chinau.activity.SearchActivity;
import cn.com.chinau.activity.SelfMallActivity;
import cn.com.chinau.activity.SelfMallDetailActivity;
import cn.com.chinau.activity.StampActivity;
import cn.com.chinau.adapter.HomeFragmentLVAdapter;
import cn.com.chinau.adapter.HomeGridViewAdapter;
import cn.com.chinau.adapter.HomeViewPagerAdapter;
import cn.com.chinau.base.BaseFragment;
import cn.com.chinau.bean.AddressBean;
import cn.com.chinau.bean.CategoryBean;
import cn.com.chinau.bean.CategoryDSFBean;
import cn.com.chinau.bean.CategoryMLBean;
import cn.com.chinau.bean.CategoryRMBean;
import cn.com.chinau.bean.CategorySCBean;
import cn.com.chinau.bean.HomeBean;
import cn.com.chinau.bean.SysParamQueryBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.AutoScrollViewPager;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.NoScrollListView;
import cn.com.chinau.view.ToTopImageView;
import cn.com.chinau.view.WrapGridView;
import cn.com.chinau.zxing.activity.CaptureActivity;

import static cn.com.chinau.R.id.home_SV;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener ,View.OnTouchListener{
    private String mImage, mSummary;// 扫码页面的显示的图片,业务介绍
    private ArrayList<String> mProcess;
    private View mHomeView;//内容页面
    private ImageView mScan;//扫码页面
    private LinearLayout mSearch;//搜索页面
    private Button mStampShop, mMall, mAuction, mScanBTN, mDesigner;//邮市,商城,竞拍,扫码,设计家,置顶
    private ToTopImageView mTopBtn;
    private AutoScrollViewPager mHomeVP;//主页导航轮播条
    private HomeBean mHomeBean;//实体类
    private WrapGridView mGridView;//下面的GridView
    private PullToRefreshScrollView mScrollView;
    private TextView mFirstTitle;//第一个标题
    private TextView mSecondTitle;//第二个标题
    private TextView mThreeTitle;//第三个标题
    private List<HomeBean.Good> mList;//接收
    private int num = 0;//网络请求的角标
    private final static int SYSPARAM = 1;//系统参数请求成功标识
    protected static final int AUTO = 10;// 自动轮训
//    //第一个小布局的图片
//    private ImageView mFirstImage1;
//    private ImageView mFirstImage2;
//    private ImageView mFirstImage3;
//    private ImageView mFirstImage4;
    //第二个小布局的图片
    private ImageView mSecondImage1;
    private ImageView mSecondImage2;
    private ImageView mSecondImage3;
    private ImageView mSecondImage4;
    //第三个小布局的图片
    private ImageView mThreeImage1;
    private ImageView mThreeImage2;
    private ImageView mThreeImage3;
    private ImageView mThreeImage4;
    private ImageView mThreeImage5;
    private SharedPreferences sp;
    private int lastY = 0;
    private int scrollY; // 标记上次滑动位置
    private View contentView;
    private ScrollView scrollView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS: // 首页数据
                    Gson gson = new Gson();
                    mHomeBean = gson.fromJson((String) msg.obj, HomeBean.class);
                    mGroup_list = mHomeBean.getGroup_list();
                    String code = mHomeBean.getRsp_code();
                    if (code.equals("0000")) {
                        if (num == 0) {
                            initAdapter();
                        } else {
                            //设置GridView的适配器
                            setGridViewAdapter();
                        }
                        getSysparamQuery();// 获取系统参数网络请求
                    }
                    break;
                case SYSPARAM:// 获取系统参数
                    String mData = (String) msg.obj;
                    Gson gsons = new Gson();
                    SysParamQueryBean paramQueryBean = gsons.fromJson(mData, SysParamQueryBean.class);
                    String mCode = paramQueryBean.getRsp_code();
                    if (mCode.equals("0000")) {
                        sp.edit().putString("System", mData).commit();
                        getNetAddress(); // 获取网络地址信息数据
                    }
                    break;
                case StaticField.ADDRESSSUCCESS://获取地址省市区
                    String result = (String) msg.obj;
                    Gson gsones = new Gson();
                    AddressBean mAddressBean = gsones.fromJson(result, AddressBean.class);
                    if (mAddressBean.getRsp_code().equals("0000")) {
                        sp.edit().putString("Address", result).commit();

                        GetInitCategory(StaticField.ML); // 邮票目录类别查询网络请求
                        GetInitCategory(StaticField.SC_ZY); // 自营商城类别查询网络请求
                        GetInitCategory(StaticField.SC_DSF); // 第三方类别查询网络请求
                        GetInitCategory(StaticField.YS); // 邮市类别查询网络请求
                        GetInitCategory(StaticField.JP); // 竞拍类别查询网络请求
                        GetInitCategory(StaticField.RM); //热门类别查询网络请求
                    }
                    break;
                case StaticField.CLASS_SUCCESS://热搜类别查询
                    Gson mGsones1 = new Gson();
                    String mCategory1 = (String) msg.obj;
                    CategoryRMBean mCategoryRMBean = mGsones1.fromJson(mCategory1, CategoryRMBean.class);
                    String code1 = mCategoryRMBean.getRsp_code();
                    if (code1.equals("0000")) {
                        sp.edit().putString("Category1", mCategory1).commit();// 保存在本地的热搜类别查询
//                        MyLog.LogShitou("保存在本地的热搜类别",mCategory1);
                    }
                    break;
                case StaticField.ML_SUCCESS://邮票目录类别查询
                    Gson mGsones2 = new Gson();
                    String mCategory2 = (String) msg.obj;
                    CategoryMLBean mCategoryDSFBean1 = mGsones2.fromJson(mCategory2, CategoryMLBean.class);
                    String code2 = mCategoryDSFBean1.getRsp_code();
                    if (code2.equals("0000")) {
                        sp.edit().putString("Category2", mCategory2).commit();// 保存在本地的邮票目录类别查询
//                        MyLog.LogShitou("保存在本地的邮票目录类别",mCategory2);
                    }
                    break;
                case StaticField.SC_SUCCESS://商城类别查询
                    Gson mGsones3 = new Gson();
                    String mCategory3 = (String) msg.obj;
//                    MyLog.LogShitou("商城类别数据",mCategory3);
                    CategorySCBean mCategorySCBean2 = mGsones3.fromJson(mCategory3, CategorySCBean.class);
                    String code3 = mCategorySCBean2.getRsp_code();
                    if (code3.equals("0000")) {
                        sp.edit().putString("Category3", mCategory3).commit();// 保存在本地的商城类别查询

//                        MyLog.LogShitou("保存在本地的商城类别",mCategory3);
//                        MyLog.LogShitou("保存在本地的商城类别","执行了次方法====================");
                    }
                    break;
                case StaticField.DSF_SUCCESS://邮票第三方类别查询
                    Gson mGsones4 = new Gson();
                    String mCategory4 = (String) msg.obj;
                    CategoryDSFBean mCategoryDSFBean3 = mGsones4.fromJson(mCategory4, CategoryDSFBean.class);
                    String code4 = mCategoryDSFBean3.getRsp_code();
                    if (code4.equals("0000")) {
                        sp.edit().putString("Category4", mCategory4).commit();// 保存在本地的第三方类别查询
//                        MyLog.LogShitou("保存在本地的第三方类别",mCategory4);
                    }
                    break;
                case StaticField.YS_SUCCESS://邮市类别查询
                    Gson mGsones5 = new Gson();
                    String mCategory5 = (String) msg.obj;
                    CategoryBean mCategoryBean4 = mGsones5.fromJson(mCategory5, CategoryBean.class);
                    String code5 = mCategoryBean4.getRsp_code();
                    if (code5.equals("0000")) {
                        sp.edit().putString("Category5", mCategory5).commit();// 保存在本地的邮市类别查询
//                        MyLog.LogShitou("保存在本地的邮市类别",mCategory5);
                    }
                    break;
                case StaticField.JP_SUCCESS://竞拍类别查询
                    Gson mGsones6 = new Gson();
                    String mCategory6 = (String) msg.obj;
                    CategoryBean mCategoryBean = mGsones6.fromJson(mCategory6, CategoryBean.class);
                    String code6 = mCategoryBean.getRsp_code();
                    if (code6.equals("0000")) {
                        sp.edit().putString("Category6", mCategory6).commit();// 保存在本地的竞拍类别查询
//                        MyLog.LogShitou("保存在本地的竞拍类别",mCategory6);
                    }
                    break;
            }
        }
    };
    private NoScrollListView mNoScrollLV;
    private List<HomeBean.Group> mGroup_list;//图片那里的集合


    @Override
    public View CreateTitle() {
        View HomeTitle = View.inflate(getActivity(), R.layout.fragment_home_title, null);
        mScan = (ImageView) HomeTitle.findViewById(R.id.home_title_scan);

        // 这三行是为了防止展示到GridView处
        mScan.setFocusable(true);
        mScan.setFocusableInTouchMode(true);
        mScan.requestFocus();
        mSearch = (LinearLayout) HomeTitle.findViewById(R.id.home_search);
        return HomeTitle;
    }

    @Override
    public View CreateSuccess() {
        mHomeView = View.inflate(getActivity(), R.layout.fragment_home_content, null);
        sp = getActivity().getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mHomeView;
    }


    @Override
    public void AgainRequest() {
        initData();
    }

    /**
     * 初始化布局参数
     */
    private void initView() {
        mStampShop = (Button) mHomeView.findViewById(R.id.home_stampshop);
        mMall = (Button) mHomeView.findViewById(R.id.home_mall);
        mAuction = (Button) mHomeView.findViewById(R.id.home_auction);
        mHomeVP = (AutoScrollViewPager) mHomeView.findViewById(R.id.home_viewpager);
        mScanBTN = (Button) mHomeView.findViewById(R.id.home_scan);
        mDesigner = (Button) mHomeView.findViewById(R.id.home_designer);
        mGridView = (WrapGridView) mHomeView.findViewById(R.id.home_WGV);
        mScrollView = (PullToRefreshScrollView) mHomeView.findViewById(home_SV);
        mTopBtn= (ToTopImageView) mHomeView.findViewById(R.id.stamp_top_btn);// 置顶按钮

        mNoScrollLV = (NoScrollListView) mHomeView.findViewById(R.id.home_list);

//        //三个小布局的题目
//        mFirstTitle = (TextView) mHomeView.findViewById(R.id.home_first_title);
//        mSecondTitle = (TextView) mHomeView.findViewById(R.id.home_second_title);
//        mThreeTitle = (TextView) mHomeView.findViewById(R.id.home_three_title);
//        //第一个小布局的图片
//        mFirstImage1 = (ImageView) mHomeView.findViewById(R.id.home_chinese_image1);
//        mFirstImage2 = (ImageView) mHomeView.findViewById(R.id.home_chinese_image2);
//        mFirstImage3 = (ImageView) mHomeView.findViewById(R.id.home_chinese_image3);
//        mFirstImage4 = (ImageView) mHomeView.findViewById(R.id.home_chinese_image4);
//        //第二个小布局的图片
//        mSecondImage1 = (ImageView) mHomeView.findViewById(R.id.home_classical_image1);
//        mSecondImage2 = (ImageView) mHomeView.findViewById(R.id.home_classical_image2);
//        mSecondImage3 = (ImageView) mHomeView.findViewById(R.id.home_classical_image3);
//        mSecondImage4 = (ImageView) mHomeView.findViewById(R.id.home_classical_image4);
//        //第三个小布局的图片
//        mThreeImage1 = (ImageView) mHomeView.findViewById(R.id.home_folk_image1);
//        mThreeImage2 = (ImageView) mHomeView.findViewById(R.id.home_folk_image2);
//        mThreeImage3 = (ImageView) mHomeView.findViewById(R.id.home_folk_image3);
//        mThreeImage4 = (ImageView) mHomeView.findViewById(R.id.home_folk_image4);
//        mThreeImage5 = (ImageView) mHomeView.findViewById(R.id.home_folk_image5);
    }


    private void initData() {
        if (num == 0) {
            mList = new ArrayList<>();
        }
        getHomeInfo(num);//获取首页信息
    }

    private void initAdapter() {
        String[] banners = getBannerString();
        //设置轮播图
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, banners, getActivity());
        mHomeVP.setAdapter(mViewPagerAdapter);
        mHomeVP.startAutoScroll();// 设置轮播图自动轮播,千万别忘记这一步。。。
        CirclePageIndicator mHomeVPI = (CirclePageIndicator) mHomeView.findViewById(R.id.home_viewpagerIndicator);
        //关联两个控件,但是这个必须在设置适配器之后使用
        mHomeVPI.setViewPager(mHomeVP);

        //设置标题的数据
//        setChildContent();
        mNoScrollLV.setAdapter(new HomeFragmentLVAdapter(getActivity(),mGroup_list));
        //设置GridView的适配器
        setGridViewAdapter();


    }
    /**
     * 设置GridView的适配器
     */
    private void setGridViewAdapter() {
        if (mHomeBean != null) {
            List<HomeBean.Good> goods_list = mHomeBean.getGoods_list();
            mList.addAll(goods_list);
            HomeGridViewAdapter Adapter = new HomeGridViewAdapter(getActivity(), mList, mBitmap);
            mGridView.setAdapter(Adapter);

            if (num != 0) {
                mScrollView.onRefreshComplete();
//                mScrollView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 800);
            }
        }
    }


    private void initListener() {
        mSearch.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mStampShop.setOnClickListener(this);
        mMall.setOnClickListener(this);
        mAuction.setOnClickListener(this);
        mScanBTN.setOnClickListener(this);
        mDesigner.setOnClickListener(this);

        //mScrollView他只提供了顶部和底部的上拉、下拉刷新监听，毛用。
        // 于是查看其源码，发现把事件拦截了。而且pullToRefreshScrollView根本就不是scrollview
        // mScrollView他里面提供了一个方法可以或得到Scrollview
        scrollView = mScrollView.getRefreshableView();
        scrollView.setOnTouchListener(this);
//        //图片的点击事件
//        mFirstImage1.setOnClickListener(this);
//        mFirstImage2.setOnClickListener(this);
//        mFirstImage3.setOnClickListener(this);
//        mFirstImage4.setOnClickListener(this);
//        mSecondImage1.setOnClickListener(this);
//        mSecondImage2.setOnClickListener(this);
//        mSecondImage3.setOnClickListener(this);
//        mSecondImage4.setOnClickListener(this);
//        mThreeImage1.setOnClickListener(this);
//        mThreeImage2.setOnClickListener(this);
//        mThreeImage3.setOnClickListener(this);
//        mThreeImage4.setOnClickListener(this);
//        mThreeImage5.setOnClickListener(this);
        // 上拉加载数据
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                num++;
                getHomeInfo(num);
            }
        });

        // 点击条目监听事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String mGoods_sn = mList.get(position).getGoods_sn();
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.GOODS_SN,mGoods_sn);
                // 跳转商品详情
                openActivityWitchAnimation(SelfMallDetailActivity.class,bundle);
            }
        });
    }


    @Override
    public void onClick(View view) {
        Bundle bundle;
        List<HomeBean.Child> child_list;
        switch (view.getId()) {
            case R.id.home_title_scan://标题扫码按钮
                Bundle bundle1 = new Bundle();
                bundle1.putString("SanFragment","HomeFragment");
                openActivityWitchAnimation(CaptureActivity.class,bundle1);
                break;
            case R.id.home_search://标题搜索按钮
                openActivityWitchAnimation(SearchActivity.class);
                break;
            case R.id.home_stampshop://邮市
                openActivityWitchAnimation(StampActivity.class);
                break;
            case R.id.home_mall://商城
                openActivityWitchAnimation(SelfMallActivity.class);
                break;
            case R.id.home_auction://竞拍
                openActivityWitchAnimation(AuctionActivity.class);
                break;
            case R.id.home_scan://扫码按钮
                openActivityWitchAnimation(ScanActivity.class);
                break;
            case R.id.home_designer://设计家按钮
                openActivityWitchAnimation(DesignerActivity.class);
                break;
//            //第一个View的
//            case R.id.home_chinese_image1:
//                child_list = mHomeBean.getGroup_list().get(0).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(0).getTitle());// 标题
//                bundle.putString(StaticField.HOMEURL, child_list.get(0).getH5_url()); // 显示页面的url
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_chinese_image2:
//                child_list = mHomeBean.getGroup_list().get(0).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(1).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(1).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_chinese_image3:
//                child_list = mHomeBean.getGroup_list().get(0).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(2).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(2).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_chinese_image4:
//                child_list = mHomeBean.getGroup_list().get(0).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(3).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(3).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            //第二个View
//            case R.id.home_classical_image1:
//                child_list = mHomeBean.getGroup_list().get(1).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(0).getTitle());//标题
//                bundle.putString(StaticField.HOMEURL, child_list.get(0).getH5_url()); // 显示页面url
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_classical_image2:
//                child_list = mHomeBean.getGroup_list().get(1).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(1).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(1).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_classical_image3:
//                child_list = mHomeBean.getGroup_list().get(1).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(2).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(2).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_classical_image4:
//                child_list = mHomeBean.getGroup_list().get(1).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(3).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(3).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            //第三个View
//            case R.id.home_folk_image1:
//                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(0).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(0).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_folk_image2:
//                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(1).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(1).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_folk_image3:
//                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(2).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(2).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_folk_image4:
//                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(3).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(3).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
//            case R.id.home_folk_image5:
//                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
//                bundle = new Bundle();
//                bundle.putString(StaticField.HOMETITLE, child_list.get(4).getTitle());
//                bundle.putString(StaticField.HOMEURL, child_list.get(4).getH5_url());
//                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
//                break;
        }
    }


    /**
     * 获取首页数据信息
     */
    private void getHomeInfo(final int num) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.HOMEPAGE);
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num));
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM));
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("===============首页数据", result);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                Message msg = handler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取轮播条的集合
     *
     * @return 轮播图的数组
     */
    public String[] getBannerString() {
        String banner = mHomeBean.getBanners();
        String[] banners = banner.split(",");
        return banners;
    }

    /**
     * 设置标题的数据
     *
     * @return 标题的数组
     */
    public void setChildContent() {
//        List<HomeBean.Group> group_list = mHomeBean.getGroup_list();
//        String[] Title = new String[group_list.size()];
//        for (int i = 0; i < group_list.size(); i++) {
//            Title[i] = group_list.get(i).getGroup_name();
//        }
//        mFirstTitle.setText(Title[0]);
//        mSecondTitle.setText(Title[1]);
//        mThreeTitle.setText(Title[2]);

//        //设置第一个View的图片
//        List<HomeBean.Child> child_list = mHomeBean.getGroup_list().get(0).getChild_list();
////        MyLog.e(child_list.get(0).getImg_url());
//        mBitmap.display(mFirstImage1, child_list.get(0).getImg_url());
//        mBitmap.display(mFirstImage2, child_list.get(1).getImg_url());
//        mBitmap.display(mFirstImage3, child_list.get(2).getImg_url());
//        mBitmap.display(mFirstImage4, child_list.get(3).getImg_url());
//
//        //设置第二个View的图片
//        List<HomeBean.Child> child_list1 = mHomeBean.getGroup_list().get(1).getChild_list();
//        mBitmap.display(mSecondImage1, child_list1.get(0).getImg_url());
//        mBitmap.display(mSecondImage2, child_list1.get(1).getImg_url());
//        mBitmap.display(mSecondImage3, child_list1.get(2).getImg_url());
//        mBitmap.display(mSecondImage4, child_list1.get(3).getImg_url());
//
//        //设置第三个View的图片
//        List<HomeBean.Child> child_list2 = mHomeBean.getGroup_list().get(2).getChild_list();
//        mBitmap.display(mThreeImage1, child_list2.get(0).getImg_url());
//        mBitmap.display(mThreeImage2, child_list2.get(1).getImg_url());
//        mBitmap.display(mThreeImage3, child_list2.get(2).getImg_url());
//        mBitmap.display(mThreeImage4, child_list2.get(3).getImg_url());
//        mBitmap.display(mThreeImage5, child_list2.get(4).getImg_url());
    }


    /**
     * 滑动ScrollView监听事件
     * 获取待监控的view对象（scrollView）
     * 实时调起线程，监控是否scroll停止，来判断是否需要显示imageView
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            mTopBtn.tellMe(scrollView);
        }
        return false;
    }

    /**
     * 退出，终止扫描线程
     */
    @Override
    public void onDestroy() {
        mTopBtn.clearCallBacks();
        super.onDestroy();
    }

    /**
     * 获取系统参数网络请求
     */
    private void getSysparamQuery() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SYSPARAM_QUERY);// 接口名称
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);//签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                if (result.equals("-1") | result.equals("-2")) {
                    getSysparamQuery();
                    return;
                }
//                MyLog.LogShitou("系统参数-->", result);
                Message msg = handler.obtainMessage();
                msg.what = SYSPARAM;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取网络地址信息数据
     */
    public void getNetAddress() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.NATION);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
//                MyLog.LogShitou("result获取省市区的地址-->:", result);
                //发送请求
                Message msg = handler.obtainMessage();
                msg.obj = result;
                msg.what = StaticField.ADDRESSSUCCESS;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 类别查询网络请求
     */
    private void GetInitCategory(final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
                params.put(StaticField.OP_TYPE, op_type);// 操作类型
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

//                MyLog.LogShitou(op_type+"类别查询数据", result);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                if(op_type.equals(StaticField.ML)){// 邮票目录
                    Message msg = handler.obtainMessage();
                    msg.what = StaticField.ML_SUCCESS;
                    msg.obj = result;
                    handler.sendMessage(msg);
                }else {
                    if (op_type.equals(StaticField.SC_ZY)) { // 商城
                        Message msg = handler.obtainMessage();
                        msg.what = StaticField.SC_SUCCESS;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    } else if (op_type.equals(StaticField.SC_DSF)) {// 第三方
                        Message msg = handler.obtainMessage();
                        msg.what = StaticField.DSF_SUCCESS;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    } else if (op_type.equals(StaticField.YS)) { // 邮市
                        Message msg = handler.obtainMessage();
                        msg.what = StaticField.YS_SUCCESS;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    } else if (op_type.equals(StaticField.JP)) {// 竞拍
                        Message msg = handler.obtainMessage();
                        msg.what = StaticField.JP_SUCCESS;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    } else if (op_type.equals(StaticField.RM)) { // 热搜
                        Message msg = handler.obtainMessage();
                        msg.what = StaticField.CLASS_SUCCESS;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }
}
