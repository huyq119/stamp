package com.example.stamp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.DesignerDetailTapViewPagerAdapter;
import com.example.stamp.adapter.DesignerDetailViewAdapter;
import com.example.stamp.adapter.DesignerDetailWorksAdapter;
import com.example.stamp.adapter.DesignerDetailsStoryAdapter;
import com.example.stamp.adapter.HomeViewPagerAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.DesignerDetailsBean;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.listener.GestureListener;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.ScreenUtils;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.view.OrderGoodsViewPager;
import com.example.stamp.view.VerticalScrollView;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 设计家详情页面
 */
public class DesignerDetailActivity extends BaseActivity implements View.OnClickListener {

    private View mDesignerDetailTitle, mDesignerDetailContent, vResume, vStory, vWorks, vView;
    private ImageView mBack, mShared;//返回按钮,分享按钮
    private ViewPager mTopVP;
    private CirclePageIndicator mTopVPI;
    private TabPageIndicator mIndicator;
    private OrderGoodsViewPager mViewPager;
    private LinearLayout mHearl;
    private String[] arr = {"个人简历", "设计故事", "艺术作品", "名家访谈"};
//    private String[] arrImage = {"http://test.chinau.com.cn:8081/chinau-imgserver/attachment//201305/27/1369638159_QpZWy1.jpg",
//            "http://test.chinau.com.cn:8081/chinau-imgserver/attachment//201305/27/1369638159_QpZWy1.jpg",
//            "http://test.chinau.com.cn:8081/chinau-imgserver/attachment//201305/27/1369638159_QpZWy1.jpg"};
    private String[] arrImage;
    private VerticalScrollView home_SV;
    private Button mTopBtn;// 置顶按钮
    private ListView mStoryList, mWorksList, mViewList;// 设计故事list, 艺术作品list, 名家访谈list
    public ArrayList<DesignerDetailsBean.DesignerStory> mListStory = new ArrayList<>();//存放数据的集合
    public ArrayList<DesignerDetailsBean.DesignerWorks> mListWorks = new ArrayList<>();
    public ArrayList<DesignerDetailsBean.DesignerView> mListView = new ArrayList<>();
    private ArrayList<View> vList;
    private RadioGroup mDesigner_RG;
    private RadioButton mResumeBtn, mStoryBtn, mWorksBtn, mViewBtn;
    private int position = 0;
    private View view1, view2, view3, view4;
    private LinearLayout mHeartll; //头部的布局(viewPager,view)
    private GestureDetector mGestureDetector;
    private VerticalScrollView mDetailSV;
    private boolean scrollFlag = false; // 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private int mPosition;
    private String mDesigner_sn,mResume;// 设计家编号，简历
    private TextView mTitle;
    private DesignerDetailsBean mDetailsBean;
    private WebView mResumeWeb;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS:
                    Gson gson = new Gson();
                    mDetailsBean = gson.fromJson((String) msg.obj, DesignerDetailsBean.class);
                    String mImages = mDetailsBean.getDesigner_images();
                    arrImage = mImages.split(",");
                    String mCHName = mDetailsBean.getChinese_name();
                    String mENName = mDetailsBean.getEnglish_name();

                    mTitle.setText(mCHName + mENName);//赋值中英文名
                    mResume = mDetailsBean.getResume();// 简历H5地址
                    if (mResume!=null){
                        mResumeWeb.loadUrl(mResume);
                    }
                    String mShareUrl = mDetailsBean.getShare_url();// 分享url
                    mListStory = mDetailsBean.getDesign_story_list();// 设计故事
                    mListWorks = mDetailsBean.getWorks_list();// 艺术作品
                    mListView = mDetailsBean.getView_list(); // 名家访谈

                    if (mListStory != null && mListStory.size() != 0) {
                        //设计故事适配器
                        DesignerDetailsStoryAdapter mPanStampAdapter = new DesignerDetailsStoryAdapter(DesignerDetailActivity.this, mListStory, mBitmap);
                        mStoryList.setAdapter(mPanStampAdapter);
                    }
                    if(mListWorks != null && mListWorks.size() != 0){
                        // 艺术作品适配器
                        DesignerDetailWorksAdapter mWorksAdapter = new DesignerDetailWorksAdapter(DesignerDetailActivity.this, mListWorks, mBitmap);
                        mWorksList.setAdapter(mWorksAdapter);
                    }
                    if(mListView != null && mListView.size() != 0){
                        // 名家访谈适配器
                        DesignerDetailViewAdapter mViewAdapter = new DesignerDetailViewAdapter(DesignerDetailActivity.this, mListView, mBitmap);
                        mViewList.setAdapter(mViewAdapter);

                    }

                    initAdapter();
                    break;
            }
        }
    };



    @Override
    public View CreateTitle() {
        mDesignerDetailTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mDesignerDetailTitle;
    }

    @Override
    public View CreateSuccess() {
        mDesignerDetailContent = View.inflate(this, R.layout.activity_designer_detail, null);
        initView();
        initData();
        //底部导航的ViewPager
        DesignerDetailTapViewPagerAdapter adapter = new DesignerDetailTapViewPagerAdapter(vList, arr);
        mViewPager.setAdapter(adapter);
        initListener();
        return mDesignerDetailContent;
    }

    private void initView() {
        //获取设计家传过来的内容(设计家编号)
        Bundle bundle = getIntent().getExtras();
        mDesigner_sn = bundle.getString(StaticField.DESIGNERSN);

        mBack = (ImageView) mDesignerDetailTitle.findViewById(R.id.base_title_back);
        mShared = (ImageView) mDesignerDetailTitle.findViewById(R.id.base_shared);
        mTitle = (TextView) mDesignerDetailTitle.findViewById(R.id.base_title);
        mTopBtn = (Button) mDesignerDetailContent.findViewById(R.id.base_top_btn);// 置顶
        mTopVP = (ViewPager) mDesignerDetailContent.findViewById(R.id.base_viewpager);  //轮播条的View
        mTopVPI = (CirclePageIndicator) mDesignerDetailContent.findViewById(R.id.base_viewpagerIndicator);

        mHearl = (LinearLayout) mDesignerDetailContent.findViewById(R.id.Desiger_hearl);
//        mDetailSV= (VerticalScrollView) mDesignerDetailContent.findViewById(R.id.Designer_Detail_SV);

        // viewPager页面
        mViewPager = (OrderGoodsViewPager) mDesignerDetailContent.findViewById(R.id.designerdetail_viewpager);
        //初始化控件
        mDesigner_RG = (RadioGroup) mDesignerDetailContent.findViewById(R.id.Designer_RG);
        mResumeBtn = (RadioButton) mDesignerDetailContent.findViewById(R.id.Desiger_Resume_Btn);
        mStoryBtn = (RadioButton) mDesignerDetailContent.findViewById(R.id.Desiger_Story_Btn);
        mWorksBtn = (RadioButton) mDesignerDetailContent.findViewById(R.id.Desiger_Works_Btn);
        mViewBtn = (RadioButton) mDesignerDetailContent.findViewById(R.id.Desiger_View_Btn);

        view1 = mDesignerDetailContent.findViewById(R.id.view1);// line为红色
        view1.setBackgroundResource(R.color.red_font);
        view2 = mDesignerDetailContent.findViewById(R.id.view2);
        view3 = mDesignerDetailContent.findViewById(R.id.view3);
        view4 = mDesignerDetailContent.findViewById(R.id.view4);


        LayoutInflater inflater = LayoutInflater.from(this);
        vResume = inflater.inflate(R.layout.view_designer_resume, null);
        vStory = inflater.inflate(R.layout.view_designer_story, null);
        vWorks = inflater.inflate(R.layout.view_designer_works, null);
        vView = inflater.inflate(R.layout.view_designer_view, null);
        vList = new ArrayList<View>();
        vList.add(vResume);
        vList.add(vStory);
        vList.add(vWorks);
        vList.add(vView);
        mStoryList = (ListView) vStory.findViewById(R.id.designer_story_lv);
        mWorksList = (ListView) vWorks.findViewById(R.id.designer_works_lv);
        mViewList = (ListView) vView.findViewById(R.id.designer_view_lv);
        initGestureListener(); // 滑动lsitview隐藏头布局(viewPager,view)的方法

        mResumeWeb = (WebView) vResume.findViewById(R.id.designer_resume_web);

    }

    /**
     * 滑动lsitview隐藏头布局(viewPager,view)的方法
     */
    private void initGestureListener() {
        mHeartll = (LinearLayout) mDesignerDetailContent.findViewById(R.id.Desiger_hearl);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mHeartll.measure(w, h);
        int height = mHeartll.getMeasuredHeight();
        MyLog.e(height + "");
        GestureListener gestureListener = new GestureListener(mHeartll, height);
        mGestureDetector = new GestureDetector(this, gestureListener);
    }

    /**
     *  设计家详情网络请求
     */
    private void initData() {

        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.DESIGNERDETAIL);// url
                params.put(StaticField.DESIGNERSN, mDesigner_sn);// 编号
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                if (result.equals("-1")) {
                    return;
                }

                MyLog.e("设计家详情~~~>" + result);

                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });


    }

    private void initAdapter() {
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);
    }


    /**
     * 设置监听
     */
    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mTopBtn.setOnClickListener(this);
//        mStoryList.setOnScrollListener(this);
//        mWorksList.setOnScrollListener(this);
//        mViewList.setOnScrollListener(this);
        mViewPager.setOnPageChangeListener(new DesignerViewPager());
        mDesigner_RG.setOnCheckedChangeListener(new MyGroupListener());
        mStoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mStory_sn = mDetailsBean.getDesign_story_list().get(i).getStory_sn();
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.DTEAIL, "GS");
                bundle.putString(StaticField.DESIGNER_STORY_SN, mStory_sn);
                openActivityWitchAnimation(DesignerH5DetailActivity.class,bundle);// 跳转详情页
            }
        });
        mWorksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mWorks_sn = mDetailsBean.getWorks_list().get(i).getWorks_sn();
                String mCategory= mDetailsBean.getWorks_list().get(i).getCategory();
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.DTEAIL, "ZP");
                bundle.putString(StaticField.DESIGNER_WORKS_SN, mWorks_sn);
                bundle.putString(StaticField.DESIGNER_ZP_CATEGORY, mCategory);
                openActivityWitchAnimation(DesignerH5DetailActivity.class,bundle);// 跳转详情页
            }
        });
        mViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mView_sn = mDetailsBean.getView_list().get(i).getView_sn();
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.DTEAIL, "FT");
                bundle.putString(StaticField.DESIGNER_VIEW_SN, mView_sn);
                openActivityWitchAnimation(DesignerH5DetailActivity.class,bundle);// 跳转详情页
            }
        });


        mResumeWeb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

        mStoryList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        mWorksList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        mViewList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        StoryListViewListener();// 设计故事StoryListView滑动监听事件
        WorksListViewListener();// 艺术作品StoryListView滑动监听事件
        ViewListViewListener();// 名家访谈ViewListView滑动监听事件

    }

    @Override
    public void AgainRequest() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_shared://分享按钮
                openActivity(SharedActivity.class);
                break;
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.base_top_btn://置顶

                if (mPosition == 0) {

                } else if (mPosition == 1) {
                    StoryListView(0);
                } else if (mPosition == 2) {
                    WorksListView(0);
                } else {
                    ViewListView(0);
                }
                mTopBtn.setVisibility(View.GONE);// 回到顶部后置顶按钮隐藏
                break;

        }
    }


    /**
     * 滑动ViewPager监听方法
     */
    private class DesignerViewPager implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            MyLog.e("哈哈~~~>", "--->" + mPosition);
            switch (position) {
                case 0:
//                    mHearl.setVisibility(View.VISIBLE);
                    mResumeBtn.setTextColor(getResources().getColor(R.color.red_font));
                    mStoryBtn.setTextColor(getResources().getColor(R.color.black));
                    mWorksBtn.setTextColor(getResources().getColor(R.color.black));
                    mViewBtn.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.red_font);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);

                    break;
                case 1:
//                    mHearl.setVisibility(View.GONE);
                    mResumeBtn.setTextColor(getResources().getColor(R.color.black));
                    mStoryBtn.setTextColor(getResources().getColor(R.color.red_font));
                    mWorksBtn.setTextColor(getResources().getColor(R.color.black));
                    mViewBtn.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.red_font);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    StoryListViewListener();// 设计故事StoryListView滑动监听事件
                    break;
                case 2:
//                    mHearl.setVisibility(View.GONE);
                    mResumeBtn.setTextColor(getResources().getColor(R.color.black));
                    mStoryBtn.setTextColor(getResources().getColor(R.color.black));
                    mWorksBtn.setTextColor(getResources().getColor(R.color.red_font));
                    mViewBtn.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.red_font);
                    view4.setBackgroundResource(R.color.line_bg);
                    WorksListViewListener();// 艺术作品StoryListView滑动监听事件
                    break;
                case 3:
//                    mHearl.setVisibility(View.GONE);
                    mResumeBtn.setTextColor(getResources().getColor(R.color.black));
                    mStoryBtn.setTextColor(getResources().getColor(R.color.black));
                    mWorksBtn.setTextColor(getResources().getColor(R.color.black));
                    mViewBtn.setTextColor(getResources().getColor(R.color.red_font));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.red_font);
                    ViewListViewListener();// 名家访谈ViewListView滑动监听事件
                    break;
            }
        }
    }

    public class MyGroupListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.Desiger_Resume_Btn:
                    position = 0;
                    view1.setBackgroundResource(R.color.red_font);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.Desiger_Story_Btn:
                    position = 1;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.red_font);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    mViewPager.setCurrentItem(1);
                    StoryListViewListener();// 设计故事StoryListView滑动监听事件
                    break;
                case R.id.Desiger_Works_Btn:
                    position = 2;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.red_font);
                    view4.setBackgroundResource(R.color.line_bg);
                    mViewPager.setCurrentItem(2);
                    WorksListViewListener();// 艺术作品StoryListView滑动监听事件
                    break;
                case R.id.Desiger_View_Btn:
                    position = 3;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.red_font);
                    mViewPager.setCurrentItem(3);
                    ViewListViewListener();// 名家访谈ViewListView滑动监听事件
                    break;

                default:
                    break;
            }

        }

    }

    /**
     * 设计故事StoryListView滑动监听事件
     */
    private void StoryListViewListener() {
        mStoryList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (mStoryList.getLastVisiblePosition() == (mStoryList.getCount() - 1)) {
                            mTopBtn.setVisibility(View.VISIBLE);
                        }
                        if (mStoryList.getFirstVisiblePosition() == 0) {
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

            // firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
            // visibleItemCount 当前能看见的列表项个数（小半个也算）
            // totalItemCount   totalItemCount：列表项共数
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag && ScreenUtils.getScreenViewBottomHeight(mStoryList) >= ScreenUtils.getScreenHeight(DesignerDetailActivity.this)) {
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
        });
    }

    /**
     * 艺术作品StoryListView滑动监听事件
     */
    private void WorksListViewListener() {
        mWorksList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (mWorksList.getLastVisiblePosition() == (mWorksList.getCount() - 1)) {
                            mTopBtn.setVisibility(View.VISIBLE);
                        }
                        if (mWorksList.getFirstVisiblePosition() == 0) {
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

            // firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
            // visibleItemCount 当前能看见的列表项个数（小半个也算）
            // totalItemCount   totalItemCount：列表项共数
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag && ScreenUtils.getScreenViewBottomHeight(mWorksList) >= ScreenUtils.getScreenHeight(DesignerDetailActivity.this)) {
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
        });

    }

    /**
     * 名家访谈ViewListView滑动监听事件
     */
    private void ViewListViewListener() {
        mViewList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (mViewList.getLastVisiblePosition() == (mViewList.getCount() - 1)) {
                            mTopBtn.setVisibility(View.VISIBLE);
                        }
                        if (mViewList.getFirstVisiblePosition() == 0) {
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

            // firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
            // visibleItemCount 当前能看见的列表项个数（小半个也算）
            // totalItemCount   totalItemCount：列表项共数
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag && ScreenUtils.getScreenViewBottomHeight(mViewList) >= ScreenUtils.getScreenHeight(DesignerDetailActivity.this)) {
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
        });
    }

    /**
     * 滚动StoryListView到指定位置
     */
    private void StoryListView(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= mCount) {
            mStoryList.smoothScrollToPosition(pos);
        } else {
            mStoryList.setSelection(pos);
        }
    }

    /**
     * 滚动WorksListView到指定位置
     */
    private void WorksListView(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= mCount) {
            mWorksList.smoothScrollToPosition(pos);
        } else {
            mWorksList.setSelection(pos);
        }
    }

    /**
     * 滚动ViewListView到指定位置
     */

    private void ViewListView(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= mCount) {
            mViewList.smoothScrollToPosition(pos);
        } else {
            mViewList.setSelection(pos);
        }
    }
}
