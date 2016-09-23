package com.example.stamp.listener;

import android.view.View;
import android.widget.AdapterView;

import com.example.stamp.adapter.StampTapFilterGridViewAdapter;

/**
 * GridViewOnItemClickListener的条目点击监听
 * Created by Administrator on 2016/8/2.
 */
public class GridViewOnItemClickListener implements AdapterView.OnItemClickListener {
    private int position;
    private StampTapFilterGridViewAdapter adapter;
    private SelfMallItemClick mSelfMallItemClick;


    public GridViewOnItemClickListener(int position, StampTapFilterGridViewAdapter adapter) {
        this.position = position;
        this.adapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (position != i) {
            position = i;
            adapter.setSeclection(i);
            adapter.notifyDataSetChanged();
        } else {
            position = -1;
            adapter.setSeclection(adapter.getCount() + 1);
            adapter.notifyDataSetChanged();
        }

        //TODO 这里写个回调,这里是改变的数据
        mSelfMallItemClick.GetClickItem();
    }

    /**
     * 这个是接口的点击回调
     */
    public interface SelfMallItemClick {
        void GetClickItem();
    }

    public void setSelfMallItemClick(SelfMallItemClick selfMallItemClick) {
        mSelfMallItemClick = selfMallItemClick;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition() {
        position = -1;
        adapter.setSeclection(adapter.getCount() + 1);
        adapter.notifyDataSetChanged();
    }
}
