package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.StampBean;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * 搜索历史适配器
 * Created by Administrator on 2016/8/4.
 */
public class AuctionListViewAdapter extends BaseAdapter {

    private List<StampBean> mList;
    private Context context;
    private BitmapUtils bitmapUtils;

    public AuctionListViewAdapter(Context context, BitmapUtils bitmapUtils, List<StampBean> mList) {
        this.mList = mList;
        this.context = context;
        this.bitmapUtils = bitmapUtils;
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
            view = View.inflate(context, R.layout.stamp_listview_item, null);
            holder.mIcon = (ImageView) view.findViewById(R.id.stamp_icon);
            holder.mTitle = (TextView) view.findViewById(R.id.stamp_title);
            holder.mTime = (TextView) view.findViewById(R.id.stamp_time);
            holder.mStatue = (TextView) view.findViewById(R.id.stamp_status);
            holder.mPrice = (TextView) view.findViewById(R.id.stamp_starting_price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTitle.setText(mList.get(i).getTitle());
        holder.mTime.setText(mList.get(i).getTime());
        holder.mStatue.setText(mList.get(i).getStatus());
        holder.mPrice.setText(mList.get(i).getPrice());

        bitmapUtils.display(holder.mIcon, mList.get(i).getIconUrl());
        return view;
    }

    public class ViewHolder {
        public ImageView mIcon;
        public TextView mTitle, mTime, mStatue, mPrice;
    }
}
