package com.example.stamp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.stamp.utils.MyLog;
import com.example.stamp.view.OrderGoodsViewPager;
import com.example.stamp.view.VerticalScrollView;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

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
    private String[] arrImage = {"http://f.hiphotos.baidu.com/image/h%3D200/sign=a31c9680a1773912db268261c8198675/730e0cf3d7ca7bcb5f591712b6096b63f624a8e9.jpg",
             "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg",
            "http://pic29.nipic.com/20130602/7447430_191109497000_2.jpg"};
    private VerticalScrollView home_SV;
    private Button mTopBtn;// 置顶按钮
    private ListView mStoryList, mWorksList, mViewList;// 设计故事list, 艺术作品list, 名家访谈list
    public ArrayList<DesignerDetailsBean.DesignerStory> mListStory = new ArrayList<>();//存放数据的集合
    public ArrayList<DesignerDetailsBean.DesignerWorks> mListWorks = new ArrayList<>();
    public ArrayList<DesignerDetailsBean.DesignerView> mListView = new ArrayList<>();
    private ArrayList<View> vList;
    private RadioGroup mDesigner_RG;
    private RadioButton mResumeBtn,mStoryBtn,mWorksBtn,mViewBtn;
    private int position = 0;
    private View view1,view2,view3,view4;

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
        initAdapter();
        initListener();
        return mDesignerDetailContent;
    }

    private void initView() {
        //获取设计家传过来的内容(中英文名)
        Bundle bundle = getIntent().getExtras();
        String mDesignerChinese_name = bundle.getString(StaticField.DESIGNERDETAIL_CHINESE);
        String mDesignerEnglish_name = bundle.getString(StaticField.DESIGNERDETAIL_ENGLISH);

        mBack = (ImageView) mDesignerDetailTitle.findViewById(R.id.base_title_back);
        mShared = (ImageView) mDesignerDetailTitle.findViewById(R.id.base_shared);
        TextView mTitle = (TextView) mDesignerDetailTitle.findViewById(R.id.base_title);
        mTitle.setText(mDesignerChinese_name + mDesignerEnglish_name);//赋值中英文名
        mTopBtn = (Button) mDesignerDetailContent.findViewById(R.id.base_top_btn);// 置顶
        mTopVP = (ViewPager) mDesignerDetailContent.findViewById(R.id.base_viewpager);  //轮播条的View
        mTopVPI = (CirclePageIndicator) mDesignerDetailContent.findViewById(R.id.base_viewpagerIndicator);
        mHearl = (LinearLayout) mDesignerDetailContent.findViewById(R.id.Desiger_hearl);
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
    }

    /**
     * 添加数据
     */
    private void initData() {
        mListStory = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mListStory.add(new DesignerDetailsBean.DesignerStory("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg", "陈绍华:《葵已年，未采用稿》", "陈绍华" + i));
        }
        mListWorks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mListWorks.add(new DesignerDetailsBean.DesignerWorks("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg", "庚申年（金猴）" + i, "T.4" + i, "编年邮票", "1980-02-15", "1枚/套", "￥1000000.00"));
        }
        mListView = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mListView.add(new DesignerDetailsBean.DesignerView("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg", "邮来邮网专访《豫园》特种邮票设计师张安朴" + i, "1980-02-1" + i));
        }

    }

    private void initAdapter() {
        //顶部导航的View
        HomeViewPagerAdapter mViewPagerAdapter = new HomeViewPagerAdapter(mBitmap, arrImage, this);
        mTopVP.setAdapter(mViewPagerAdapter);
        mTopVPI.setVisibility(View.VISIBLE);
        mTopVPI.setViewPager(mTopVP);

        //底部导航的ViewPager
        DesignerDetailTapViewPagerAdapter adapter = new DesignerDetailTapViewPagerAdapter(vList, arr);
        mViewPager.setAdapter(adapter);

        //设计故事适配器
        DesignerDetailsStoryAdapter mPanStampAdapter = new DesignerDetailsStoryAdapter(this, mListStory, mBitmap);
        mStoryList.setAdapter(mPanStampAdapter);
        // 艺术作品适配器
        DesignerDetailWorksAdapter mWorksAdapter = new DesignerDetailWorksAdapter(this, mListWorks, mBitmap);
        mWorksList.setAdapter(mWorksAdapter);
        // 名家访谈适配器
        DesignerDetailViewAdapter mViewAdapter = new DesignerDetailViewAdapter(this, mListView, mBitmap);
        mViewList.setAdapter(mViewAdapter);

        mStoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openActivityWitchAnimation(DesignerStoryDetailActivity.class);// 跳转设计故事详情页
            }
        });
    }


    /**
     * 设置监听
     */
    private void initListener() {
        mShared.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mTopBtn.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new DesignerViewPager());
        mDesigner_RG.setOnCheckedChangeListener(new MyGroupListener());
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
            MyLog.e("哈哈~~~>", "--->" + position);
            switch (position) {
                case 0:
                    mHearl.setVisibility(View.VISIBLE);
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
                    mHearl.setVisibility(View.GONE);
                    mResumeBtn.setTextColor(getResources().getColor(R.color.black));
                    mStoryBtn.setTextColor(getResources().getColor(R.color.red_font));
                    mWorksBtn.setTextColor(getResources().getColor(R.color.black));
                    mViewBtn.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.red_font);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.line_bg);
                    break;
                case 2:
                    mHearl.setVisibility(View.GONE);
                    mResumeBtn.setTextColor(getResources().getColor(R.color.black));
                    mStoryBtn.setTextColor(getResources().getColor(R.color.black));
                    mWorksBtn.setTextColor(getResources().getColor(R.color.red_font));
                    mViewBtn.setTextColor(getResources().getColor(R.color.black));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.red_font);
                    view4.setBackgroundResource(R.color.line_bg);
                    break;
                case 3:
                    mHearl.setVisibility(View.GONE);
                    mResumeBtn.setTextColor(getResources().getColor(R.color.black));
                    mStoryBtn.setTextColor(getResources().getColor(R.color.black));
                    mWorksBtn.setTextColor(getResources().getColor(R.color.black));
                    mViewBtn.setTextColor(getResources().getColor(R.color.red_font));
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.red_font);
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
                    break;
                case R.id.Desiger_Works_Btn:
                    position = 2;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.red_font);
                    view4.setBackgroundResource(R.color.line_bg);
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.Desiger_View_Btn:
                    position = 3;
                    view1.setBackgroundResource(R.color.line_bg);
                    view2.setBackgroundResource(R.color.line_bg);
                    view3.setBackgroundResource(R.color.line_bg);
                    view4.setBackgroundResource(R.color.red_font);
                    mViewPager.setCurrentItem(3);
                    break;

                default:
                    break;
            }

        }

    }
}
