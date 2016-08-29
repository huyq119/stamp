package com.example.stamp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.activity.AuctionActivity;
import com.example.stamp.activity.DesignerActivity;
import com.example.stamp.activity.HomeImageH5Activity;
import com.example.stamp.activity.ScanActivity;
import com.example.stamp.activity.SearchActivity;
import com.example.stamp.activity.SelfMallActivity;
import com.example.stamp.activity.SelfMallDetailActivity;
import com.example.stamp.activity.StampActivity;
import com.example.stamp.adapter.HomeGridViewAdapter;
import com.example.stamp.adapter.HomeViewPagerAdapter;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.bean.HomeBean;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.view.WrapGridView;
import com.example.stamp.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private View mHomeView;//内容页面
    private ImageView mScan;//扫码页面
    private LinearLayout mSearch;//搜索页面
    private Button mStampShop, mMall, mAuction, mScanBTN, mDesigner;//邮市,商城,竞拍,扫码,设计家
    private ViewPager mHomeVP;//主页导航轮播条
    private HomeBean mHomeBean;//实体类
    private WrapGridView mGridView;//下面的GridView
    private PullToRefreshScrollView mScrollView;
    private TextView mFirstTitle;//第一个标题
    private TextView mSecondTitle;//第二个标题
    private TextView mThreeTitle;//第三个标题
    private List<HomeBean.Good> mList;//接收
    private int num = 0;//网络请求的角标
    //第一个小布局的图片
    private ImageView mFirstImage1;
    private ImageView mFirstImage2;
    private ImageView mFirstImage3;
    private ImageView mFirstImage4;
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS:
                    Gson gson = new Gson();
                    mHomeBean = gson.fromJson((String) msg.obj, HomeBean.class);
                    if (num == 0) {
                        initAdapter();
                    } else {
                        //设置GridView的适配器
                        setGridViewAdapter();
                    }
                    break;
            }
        }
    };


    @Override
    public View CreateTitle() {
        View HomeTitle = View.inflate(getActivity(), R.layout.fragment_home_title, null);
        mScan = (ImageView) HomeTitle.findViewById(R.id.home_title_scan);
        mSearch = (LinearLayout) HomeTitle.findViewById(R.id.home_search);
        return HomeTitle;
    }

    @Override
    public View CreateSuccess() {
        mHomeView = View.inflate(getActivity(), R.layout.fragment_home_content, null);
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
        mHomeVP = (ViewPager) mHomeView.findViewById(R.id.home_viewpager);
        mScanBTN = (Button) mHomeView.findViewById(R.id.home_scan);
        mDesigner = (Button) mHomeView.findViewById(R.id.home_designer);
        mGridView = (WrapGridView) mHomeView.findViewById(R.id.home_WGV);
        mScrollView = (PullToRefreshScrollView) mHomeView.findViewById(R.id.home_SV);
        //三个小布局的题目
        mFirstTitle = (TextView) mHomeView.findViewById(R.id.home_first_title);
        mSecondTitle = (TextView) mHomeView.findViewById(R.id.home_second_title);
        mThreeTitle = (TextView) mHomeView.findViewById(R.id.home_three_title);
        //第一个小布局的图片
        mFirstImage1 = (ImageView) mHomeView.findViewById(R.id.home_chinese_image1);
        mFirstImage2 = (ImageView) mHomeView.findViewById(R.id.home_chinese_image2);
        mFirstImage3 = (ImageView) mHomeView.findViewById(R.id.home_chinese_image3);
        mFirstImage4 = (ImageView) mHomeView.findViewById(R.id.home_chinese_image4);
        //第二个小布局的图片
        mSecondImage1 = (ImageView) mHomeView.findViewById(R.id.home_classical_image1);
        mSecondImage2 = (ImageView) mHomeView.findViewById(R.id.home_classical_image2);
        mSecondImage3 = (ImageView) mHomeView.findViewById(R.id.home_classical_image3);
        mSecondImage4 = (ImageView) mHomeView.findViewById(R.id.home_classical_image4);
        //第三个小布局的图片
        mThreeImage1 = (ImageView) mHomeView.findViewById(R.id.home_folk_image1);
        mThreeImage2 = (ImageView) mHomeView.findViewById(R.id.home_folk_image2);
        mThreeImage3 = (ImageView) mHomeView.findViewById(R.id.home_folk_image3);
        mThreeImage4 = (ImageView) mHomeView.findViewById(R.id.home_folk_image4);
        mThreeImage5 = (ImageView) mHomeView.findViewById(R.id.home_folk_image5);
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
        CirclePageIndicator mHomeVPI = (CirclePageIndicator) mHomeView.findViewById(R.id.home_viewpagerIndicator);
        //关联两个控件,但是这个必须在设置适配器之后使用
        mHomeVPI.setViewPager(mHomeVP);

        //设置标题的数据
        setChildContent();
        //设置GridView的适配器
        setGridViewAdapter();

    }


    private void initListener() {
        mSearch.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mStampShop.setOnClickListener(this);
        mMall.setOnClickListener(this);
        mAuction.setOnClickListener(this);
        mScanBTN.setOnClickListener(this);
        mDesigner.setOnClickListener(this);
        //图片的点击事件
        mFirstImage1.setOnClickListener(this);
        mFirstImage2.setOnClickListener(this);
        mFirstImage3.setOnClickListener(this);
        mFirstImage4.setOnClickListener(this);
        mSecondImage1.setOnClickListener(this);
        mSecondImage2.setOnClickListener(this);
        mSecondImage3.setOnClickListener(this);
        mSecondImage4.setOnClickListener(this);
        mThreeImage1.setOnClickListener(this);
        mThreeImage2.setOnClickListener(this);
        mThreeImage3.setOnClickListener(this);
        mThreeImage4.setOnClickListener(this);
        mThreeImage5.setOnClickListener(this);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                num++;
                getHomeInfo(num);
            }
        });
    }


    @Override
    public void onClick(View view) {
        Bundle bundle;
        List<HomeBean.Child> child_list;
        switch (view.getId()) {
            case R.id.home_title_scan://标题扫码按钮
                openActivityWitchAnimation(CaptureActivity.class);
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
            //第一个View的
            case R.id.home_chinese_image1:
                child_list = mHomeBean.getGroup_list().get(0).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(0).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(0).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_chinese_image2:
                child_list = mHomeBean.getGroup_list().get(0).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(0).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(1).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_chinese_image3:
                child_list = mHomeBean.getGroup_list().get(0).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(0).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(2).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_chinese_image4:
                child_list = mHomeBean.getGroup_list().get(0).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(0).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(3).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            //第二个View
            case R.id.home_classical_image1:
                child_list = mHomeBean.getGroup_list().get(1).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(1).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(0).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_classical_image2:
                child_list = mHomeBean.getGroup_list().get(1).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(1).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(1).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_classical_image3:
                child_list = mHomeBean.getGroup_list().get(1).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(1).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(2).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_classical_image4:
                child_list = mHomeBean.getGroup_list().get(1).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(1).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(3).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            //第三个View
            case R.id.home_folk_image1:
                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(2).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(0).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_folk_image2:
                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(2).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(1).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_folk_image3:
                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(2).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(2).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_folk_image4:
                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(2).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(3).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
            case R.id.home_folk_image5:
                child_list = mHomeBean.getGroup_list().get(2).getChild_list();
                bundle = new Bundle();
                bundle.putString(StaticField.HOMETITLE, mHomeBean.getGroup_list().get(2).getGroup_name());
                bundle.putString(StaticField.HOMEURL, child_list.get(4).getH5_url());
                openActivityWitchAnimation(HomeImageH5Activity.class, bundle);
                break;
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
                MyLog.e(num+"");
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM));
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.e(result);

                if (result.equals("-1")) {
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
        List<HomeBean.Group> group_list = mHomeBean.getGroup_list();
        String[] Title = new String[group_list.size()];
        for (int i = 0; i < group_list.size(); i++) {
            Title[i] = group_list.get(i).getGroup_name();
        }
        mFirstTitle.setText(Title[0]);
        mSecondTitle.setText(Title[1]);
        mThreeTitle.setText(Title[2]);

        //设置第一个View的图片
        List<HomeBean.Child> child_list = mHomeBean.getGroup_list().get(0).getChild_list();
        MyLog.e(child_list.get(0).getImg_url());
        mBitmap.display(mFirstImage1, child_list.get(0).getImg_url());
        mBitmap.display(mFirstImage2, child_list.get(1).getImg_url());
        mBitmap.display(mFirstImage3, child_list.get(2).getImg_url());
        mBitmap.display(mFirstImage4, child_list.get(3).getImg_url());

        //设置第二个View的图片
        List<HomeBean.Child> child_list1 = mHomeBean.getGroup_list().get(1).getChild_list();
        mBitmap.display(mSecondImage1, child_list1.get(0).getImg_url());
        mBitmap.display(mSecondImage2, child_list1.get(1).getImg_url());
        mBitmap.display(mSecondImage3, child_list1.get(2).getImg_url());
        mBitmap.display(mSecondImage4, child_list1.get(3).getImg_url());

        //设置第三个View的图片
        List<HomeBean.Child> child_list2 = mHomeBean.getGroup_list().get(2).getChild_list();
        mBitmap.display(mThreeImage1, child_list2.get(0).getImg_url());
        mBitmap.display(mThreeImage2, child_list2.get(1).getImg_url());
        mBitmap.display(mThreeImage3, child_list2.get(2).getImg_url());
        mBitmap.display(mThreeImage4, child_list2.get(3).getImg_url());
        mBitmap.display(mThreeImage5, child_list2.get(4).getImg_url());
    }

    /**
     * 设置GridView的适配器
     */
    private void setGridViewAdapter() {
        List<HomeBean.Good> goods_list = mHomeBean.getGoods_list();
        mList.addAll(goods_list);
        HomeGridViewAdapter Adapter = new HomeGridViewAdapter(getActivity(), mList, mBitmap);
        mGridView.setAdapter(Adapter);
        //这句是为了防止展示到GridView处
        mGridView.requestChildFocus(mHomeVP, null);
        if(num!=0){
            mScrollView.onRefreshComplete();
        }
    }

}
