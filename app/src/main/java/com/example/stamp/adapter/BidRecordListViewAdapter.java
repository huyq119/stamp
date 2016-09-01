package com.example.stamp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.StampDetailBean;
import com.example.stamp.view.TimeTextView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/1.
 */
public class BidRecordListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StampDetailBean.StampDetail> mList;

    public BidRecordListViewAdapter(Context context, ArrayList<StampDetailBean.StampDetail> mList) {
        this.context = context;
        this.mList = mList;
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
            view = View.inflate(context, R.layout.auction_bidrecord_pop_listview_item, null);
            holder.mTime = (TextView) view.findViewById(R.id.bidRecord_item_time);
            holder.mMobile = (TextView) view.findViewById(R.id.bidRecord_item_tel);
            holder.mPrice= (TextView) view.findViewById(R.id.bidRecord_item_price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (i==0){
            holder.mTime.setTextColor(context.getResources().getColor(R.color.red_font));
            holder.mMobile.setTextColor(context.getResources().getColor(R.color.red_font));
            holder.mPrice.setTextColor(context.getResources().getColor(R.color.red_font));
        }else if(i==1||i==3){
            holder.mTime.setTextColor(context.getResources().getColor(R.color.geay_font));
            holder.mMobile.setTextColor(context.getResources().getColor(R.color.geay_font));
            holder.mPrice.setTextColor(context.getResources().getColor(R.color.geay_font));
        }else if(i==2){
            holder.mTime.setTextColor(context.getResources().getColor(R.color.bule_font));
            holder.mMobile.setTextColor(context.getResources().getColor(R.color.bule_font));
            holder.mPrice.setTextColor(context.getResources().getColor(R.color.bule_font));
        }
        holder.mTime.setText(mList.get(i).getOffer_time());
        holder.mMobile.setText(mList.get(i).getMobile());
        holder.mPrice.setText(mList.get(i).getPrice());

        return view;
    }


    public class ViewHolder {
        public TextView mTime, mMobile, mPrice;
    }
}
