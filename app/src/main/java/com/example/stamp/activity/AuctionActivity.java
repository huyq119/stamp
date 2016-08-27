package com.example.stamp.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.stamp.R;
import com.example.stamp.adapter.StampHorizontalListViewAdapter;
import com.example.stamp.adapter.AuctionListViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.StampBean;
import com.example.stamp.view.HorizontalListView;

import java.util.ArrayList;

/**
 * 竞拍页面
 */
public class AuctionActivity extends BaseActivity implements View.OnClickListener {

    private View mAuctionContent;
    private View mAuctionTitle;
    private HorizontalListView hListView;//横向滑动的listView
    private StampHorizontalListViewAdapter hListViewAdapter;//横向花的listView的适配器
    private ArrayList<StampBean> mList;
    private ListView mListView;
    private ImageView mBack, mSearch;//返回按钮,搜索

    private String[] mArr = {"红豆", "大红豆", "绿豆", "红豆", "大红豆", "绿豆", "大红豆", "红豆", "红豆", "大红豆", "绿豆", "大红豆", "红豆"};

    @Override
    public View CreateTitle() {
        mAuctionTitle = View.inflate(this, R.layout.base_detail_title, null);
        return mAuctionTitle;
    }

    @Override
    public View CreateSuccess() {
        mAuctionContent = View.inflate(this, R.layout.activity_auction_content, null);
        initView();
        initAdapter();
        initListener();
        return mAuctionContent;
    }


    private void initView() {
        mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mList.add(new StampBean("庚申年", "未开始", "￥1000.00" + i, "距离开拍:10小时10分18秒", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        }

        mBack = (ImageView) mAuctionTitle.findViewById(R.id.base_title_back);
        mSearch = (ImageView) mAuctionTitle.findViewById(R.id.base_search);
        hListView = (HorizontalListView) mAuctionContent.findViewById(R.id.stamp_hl);
        mListView = (ListView) mAuctionContent.findViewById(R.id.stamp_lv);
    }

    private void initAdapter() {
        //为ListView设置适配器
        AuctionListViewAdapter mListAdapter = new AuctionListViewAdapter(this, mBitmap, mList);
        mListView.setAdapter(mListAdapter);

        //横向的listView设置适配器
        hListViewAdapter = new StampHorizontalListViewAdapter(this, mArr);
        hListView.setAdapter(hListViewAdapter);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        //横向ListView的点击事件
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //设置点击背景的方法
                hListViewAdapter.setSelection(i);
                hListViewAdapter.notifyDataSetChanged();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //去往邮市详情页
                openActivityWitchAnimation(StampDetailActivity.class);
            }
        });

    }

    @Override
    public void AgainRequest() {

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
        }
    }
}
