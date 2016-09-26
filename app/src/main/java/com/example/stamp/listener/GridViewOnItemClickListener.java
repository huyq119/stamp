package com.example.stamp.listener;

import android.view.View;
import android.widget.AdapterView;

import com.example.stamp.adapter.StampTapFilterGridViewAdapter;

/**
 * （邮票目录）筛选GridViewOnItemClickListener的条目点击监听
 * Created by Administrator on 2016/8/2.
 */
public class GridViewOnItemClickListener implements AdapterView.OnItemClickListener {
    private int position;
    private StampTapFilterGridViewAdapter adapter;

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
