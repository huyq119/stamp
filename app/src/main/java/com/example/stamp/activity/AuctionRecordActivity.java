package com.example.stamp.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.stamp.R;
import com.example.stamp.adapter.AuctionRecordListViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.AuctionRecordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞拍记录页面
 */
public class AuctionRecordActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View mAuctionRecordContent;
    private View mAuctionRecordTitle;
    private ImageView mRecordBack;//返回
    private ListView mRecordListView;
    private Button mRecordAll, mRecording, mRecordOut, mRecordSuccess;//全部 竞拍中 已出局 竞拍成功
    private AuctionRecordListViewAdapter mAdapter;
    private List<AuctionRecordBean.Auction> mList;


    @Override
    public View CreateTitle() {
        mAuctionRecordTitle = View.inflate(this, R.layout.base_back_title, null);
        return mAuctionRecordTitle;
    }

    @Override
    public View CreateSuccess() {
        mAuctionRecordContent = View.inflate(this, R.layout.activity_auctionrecord_content, null);
        initView();
        initData();
        initListener();
        return mAuctionRecordContent;
    }

    private void initView() {
        mRecordBack = (ImageView) mAuctionRecordTitle.findViewById(R.id.base_title_back);
        mRecordListView = (ListView) mAuctionRecordContent.findViewById(R.id.record_auction_LV);
        mRecordAll = (Button) mAuctionRecordContent.findViewById(R.id.record_all);
        mRecording = (Button) mAuctionRecordContent.findViewById(R.id.btn_recording);
        mRecordOut = (Button) mAuctionRecordContent.findViewById(R.id.btn_recordout);
        mRecordSuccess = (Button) mAuctionRecordContent.findViewById(R.id.record_success);
    }

    private void initData() {
        mList = new ArrayList<>();

        AuctionRecordBean.Auction auction ;
        auction = new AuctionRecordBean.Auction("竞拍中","10920","12093.90","19:23:20","庚申年","20:02:23","http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(auction);
        auction = new AuctionRecordBean.Auction("已出局","10920","12093.90","19:23:20","庚申年","20:02:23","http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(auction);
        auction = new AuctionRecordBean.Auction("竞拍成功","10920","12093.90","19:23:20","庚申年","20:02:23","http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(auction);
        auction = new AuctionRecordBean.Auction("竞拍成功","10920","12093.90","19:23:20","庚申年","20:02:23","http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(auction);
        auction = new AuctionRecordBean.Auction("竞拍成功","10920","12093.90","19:23:20","庚申年","20:02:23","http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(auction);
        mAdapter = new AuctionRecordListViewAdapter(this, mBitmap, mList);
        mRecordListView.setAdapter(mAdapter);//竞拍记录竞拍中适配器

    }

    private void initListener() {
        mRecordBack.setOnClickListener(this);
        mRecordAll.setOnClickListener(this);
        mRecording.setOnClickListener(this);
        mRecordOut.setOnClickListener(this);
        mRecordSuccess.setOnClickListener(this);
        mRecordListView.setOnItemClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.record_all://全部
                mAdapter = new AuctionRecordListViewAdapter(this, mBitmap, mList);
                mRecordListView.setAdapter(mAdapter);
                break;
            case R.id.btn_recording://竞拍中
                mAdapter = new AuctionRecordListViewAdapter(this, mBitmap, mList);
                mRecordListView.setAdapter(mAdapter);//竞拍记录竞拍中适配器
                break;
            case R.id.btn_recordout://已出局
                mAdapter = new AuctionRecordListViewAdapter(this, mBitmap, mList);
                mRecordListView.setAdapter(mAdapter);//竞拍记录已出局适配器
                break;
            case R.id.record_success:///竞拍成功
                mAdapter = new AuctionRecordListViewAdapter(this, mBitmap, mList);
                mRecordListView.setAdapter(mAdapter);//竞拍记录竞拍中适配器
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        openActivityWitchAnimation(SelfMallDetailActivity.class);
    }
}
