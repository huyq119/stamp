package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stamp.R;

import java.util.List;

/**
 * 搜索历史适配器
 * Created by Administrator on 2016/8/4.
 */
public class SearchListViewAdapter extends BaseAdapter {

    private List<String> mList;
    private Context context;

    public SearchListViewAdapter(Context context, List<String> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.search_listview_item, null);
            holder.String = (TextView) view.findViewById(R.id.search_item_string);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.String.setText(mList.get(i));
        return view;
    }

    public class ViewHolder {
        public TextView String;
    }
}
