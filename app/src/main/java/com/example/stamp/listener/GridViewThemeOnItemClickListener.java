package com.example.stamp.listener;

import android.view.View;
import android.widget.AdapterView;

import com.example.stamp.adapter.StampTapFilterPersonGridViewAdapter;

/**
 * GridViewThemeOnItemClickListener
 * 邮票目录筛选题材的条目点击监听
 * Created by Administrator on 2016/8/2.
 */
public class GridViewThemeOnItemClickListener implements AdapterView.OnItemClickListener {
    private int position;
    private StampTapFilterPersonGridViewAdapter adapter;

    public GridViewThemeOnItemClickListener(int position, StampTapFilterPersonGridViewAdapter adapter) {
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
