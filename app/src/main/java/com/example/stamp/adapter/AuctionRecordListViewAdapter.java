package com.example.stamp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.AuctionRecordBean;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * 竞拍记录的ListViewAdapter
 * Created by Administrator on 2016/8/21.
 */
public class AuctionRecordListViewAdapter extends BaseAdapter {

    private Context content;
    private BitmapUtils bitmapUtils;
    private List<AuctionRecordBean.Auction> mList;

    public AuctionRecordListViewAdapter(Context content, BitmapUtils bitmapUtils, List<AuctionRecordBean.Auction> mList) {
        this.content = content;
        this.bitmapUtils = bitmapUtils;
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
            view = View.inflate(content, R.layout.auctionrecord_listview_item, null);
            holder.GoodsImageUrl = (ImageView) view.findViewById(R.id.record_out_icon);
            holder.GoodsName = (TextView) view.findViewById(R.id.auctionRecord_item_Name);
            holder.GoodsPrice = (TextView) view.findViewById(R.id.auctionRecord_item_currentPrice);
            holder.AuctionStatue = (TextView) view.findViewById(R.id.auctionRecord_item_status);
            holder.CurrentPrice = (LinearLayout) view.findViewById(R.id.auctionRecord_item_currentPrice_LL);
            holder.PaymentSuccessStatus = (TextView) view.findViewById(R.id.auctionRecord_item_successStatus);
            holder.MarkTime = (TextView) view.findViewById(R.id.auctionRecord_item_time);
            holder.MarkPrice = (TextView) view.findViewById(R.id.auctionRecord_item_current);
            holder.Time = (TextView) view.findViewById(R.id.auctionRecord_item_spareTime);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AuctionRecordBean.Auction auction = mList.get(i);
        //获取状态
        String Status = auction.getAuction_status();
        //根据状态进行判断
        if (Status.equals("竞拍中")) {
            holder.AuctionStatue.setText("竞拍中");
            holder.AuctionStatue.setTextColor(Color.parseColor("#ff9900"));
            //竞拍中的时候,时间应该显示为竞拍结束时间
            holder.MarkPrice.setText("当前价:");
            //把时间标识隐藏
            holder.MarkTime.setVisibility(View.GONE);
        } else if (Status.equals("竞拍成功")) {
            holder.AuctionStatue.setText("竞拍成功");
            holder.AuctionStatue.setTextColor(Color.parseColor("#e20000"));
            //TODO 这里看看之后给的字段应该不对,和服务器调一下
            //这里要根据返回的字段进行判断

        } else if (Status.equals("已出局")) {
            holder.AuctionStatue.setText("已出局");
            holder.AuctionStatue.setTextColor(Color.parseColor("#999999"));
            //隐藏价格栏
            holder.CurrentPrice.setVisibility(View.INVISIBLE);
            //把时间标识隐藏
            holder.MarkTime.setVisibility(View.GONE);
        }

        holder.GoodsName.setText(auction.getGoods_name());
        holder.GoodsPrice.setText(auction.getGoods_price());
        bitmapUtils.display(holder.GoodsImageUrl, mList.get(i).getGoods_imag());



        return view;
    }


    public class ViewHolder {
        public TextView GoodsName;//商品名称
        public TextView GoodsPrice;//商品价格
        public TextView AuctionStatue;//竞拍状态
        public TextView MarkTime;//时间前面的显示字段
        public TextView Time;//时间
        public TextView MarkPrice;//价格前面显示的字段
        public TextView PaymentSuccessStatus;//付款成功后的状态
        public LinearLayout CurrentPrice;//当前价格的View
        public ImageView GoodsImageUrl;//商品图片
    }
}
