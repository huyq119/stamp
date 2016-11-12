package cn.com.chinau.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.activity.FirmOrderActivity;
import cn.com.chinau.bean.AuctionRecordBean;

/**
 * 竞拍记录的ListViewAdapter
 * Created by Administrator on 2016/8/21.
 */
public class AuctionRecordListViewAdapter extends BaseAdapter {

    private Context context;
    private BitmapUtils bitmapUtils;
    private List<AuctionRecordBean.Auction> mList;

    public AuctionRecordListViewAdapter(Context context, BitmapUtils bitmapUtils, List<AuctionRecordBean.Auction> mList) {
        this.context = context;
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
            view = View.inflate(context, R.layout.auctionrecord_listview_item, null);
            holder.GoodsImageUrl = (ImageView) view.findViewById(R.id.record_out_icon);
            holder.GoodsName = (TextView) view.findViewById(R.id.auctionRecord_item_Name);
            holder.AuctionStatue = (TextView) view.findViewById(R.id.auctionRecord_item_status);
            holder.CurrentPrice = (LinearLayout) view.findViewById(R.id.auctionRecord_item_currentPrice_LL);// 价格LL
            holder.GoodsPriceTv = (TextView) view.findViewById(R.id.auctionRecord_item_currentPrice_tv);
            holder.GoodsPrice = (TextView) view.findViewById(R.id.auctionRecord_item_currentPrice);
            holder.MarkTimeTv = (TextView) view.findViewById(R.id.auctionRecord_item_time_tv);
            holder.SpareTime = (TextView) view.findViewById(R.id.auctionRecord_item_spareTime);
            holder.PaymentTv = (TextView) view.findViewById(R.id.order_status_payment_tv);// 付款
            holder.OverCloseTv = (TextView) view.findViewById(R.id.order_status_over_close_tv); // 已关闭/已完成

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AuctionRecordBean.Auction auction = mList.get(i);
        holder.GoodsName.setText(auction.getGoods_name());// 商品名称

        //获取状态
        String AuctionStatus = auction.getAuction_status();// 竞拍状态
        //根据状态进行判断
        if (AuctionStatus.equals("JPZ")) {
            holder.AuctionStatue.setText("竞拍中");
            holder.AuctionStatue.setTextColor(context.getResources().getColor(R.color.refuse));
            holder.CurrentPrice.setVisibility(View.VISIBLE);
            holder.GoodsPriceTv.setText("当前价:");
            holder.GoodsPrice.setText("￥" +auction.getGoods_price());
            holder.MarkTimeTv.setText(auction.getCreate_time());
            holder.SpareTime.setVisibility(View.GONE);
        } else if (AuctionStatus.equals("CG")) {
            holder.AuctionStatue.setText("竞拍成功");
            holder.AuctionStatue.setTextColor(context.getResources().getColor(R.color.red_font));
            holder.CurrentPrice.setVisibility(View.VISIBLE);
            holder.GoodsPriceTv.setText("订单金额:");
            holder.GoodsPrice.setText("￥" +auction.getGoods_price());

            String OrderStatus = auction.getOrder_status();// 订单状态
            if (OrderStatus.equals("PAYMENT")){ // 待付款
                holder.PaymentTv.setVisibility(View.VISIBLE); //付款按钮显示
                holder.OverCloseTv.setVisibility(View.GONE);// 隐藏已关闭或已完成按钮
                holder.PaymentTv.setText("付款");
                holder.MarkTimeTv.setText("付款剩余时间:");
                holder.SpareTime.setText(auction.getAuction_end_time());
            }else if(OrderStatus.equals("SUCCESS")){ // 已完成
                holder.PaymentTv.setVisibility(View.INVISIBLE); //付款按钮隐藏
                holder.OverCloseTv.setVisibility(View.VISIBLE); //显示已完成
                holder.OverCloseTv.setText("已完成");
                holder.MarkTimeTv.setText(auction.getCreate_time());
                holder.SpareTime.setVisibility(View.GONE);

            }else if(OrderStatus.equals("CLOSE")){ // 已关闭
                holder.PaymentTv.setVisibility(View.INVISIBLE); //付款按钮隐藏
                holder.OverCloseTv.setVisibility(View.VISIBLE); //显示已关闭
                holder.OverCloseTv.setText("已关闭");
                holder.MarkTimeTv.setText(auction.getCreate_time());
                holder.SpareTime.setVisibility(View.GONE);
            }
        } else if (AuctionStatus.equals("CJ")) {
            holder.AuctionStatue.setText("交易关闭");
            holder.AuctionStatue.setTextColor(context.getResources().getColor(R.color.font_color));
            holder.CurrentPrice.setVisibility(View.INVISIBLE);// 价格栏隐藏
            holder.MarkTimeTv.setText(auction.getCreate_time());
            holder.SpareTime.setVisibility(View.GONE);
        }
        bitmapUtils.display(holder.GoodsImageUrl, mList.get(i).getGoods_img());

        holder.PaymentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转至确认订单页面
                Intent intent = new Intent (context,FirmOrderActivity.class);
                intent.putExtra("AuctionRecord", "AuctionRecord");
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);


            }
        });
        return view;
    }

    public class ViewHolder {
        public ImageView GoodsImageUrl;//商品图片
        public TextView GoodsName,AuctionStatue,GoodsPriceTv,GoodsPrice,MarkTimeTv,SpareTime,MarkPrice,PaymentTv,OverCloseTv;//商品名称
        public LinearLayout CurrentPrice;//当前价格的View
    }
}
