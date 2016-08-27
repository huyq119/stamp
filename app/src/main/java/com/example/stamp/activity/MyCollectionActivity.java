package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.CollectionListViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.CollectionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏页面
 */
public class MyCollectionActivity extends BaseActivity {


    private View mCollectionTitle;
    private View mCollectionContent;
    private ListView mCollection_lv;

    private List<CollectionBean.Collection> mList;

    @Override
    public View CreateTitle() {
        mCollectionTitle = View.inflate(this, R.layout.base_font_title, null);
        return mCollectionTitle;
    }

    @Override
    public View CreateSuccess() {
        mCollectionContent = View.inflate(this, R.layout.activity_mycollection_content, null);
        initView();
        initData();
        initAdapter();
        return mCollectionContent;
    }


    private void initView() {
        ImageView Back = (ImageView) mCollectionTitle.findViewById(R.id.base_title_back);
        TextView Title = (TextView) mCollectionTitle.findViewById(R.id.base_title);
        Title.setText("收藏夹");
        TextView mEdit = (TextView) mCollectionTitle.findViewById(R.id.base_edit);
        mEdit.setText("编辑");
        mCollection_lv = (ListView) mCollectionContent.findViewById(R.id.collection_listView);

    }

    private void initData() {
        mList = new ArrayList<>();
        CollectionBean.Collection collection;
        collection = new CollectionBean.Collection("庚申年", "JPZ", "SC", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "WH", "SC", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "YH", "YS", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "WKS", "SC", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
        collection = new CollectionBean.Collection("庚申年", "YJS", "JP", "123.45", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
        mList.add(collection);
    }

    private void initAdapter() {
        CollectionListViewAdapter adapter = new CollectionListViewAdapter(this, mBitmap, mList);
        mCollection_lv.setAdapter(adapter);
    }

    @Override
    public void AgainRequest() {

    }
}
