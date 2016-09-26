package cn.com.chinau.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.OrderBean;

/**
 * Created by Administrator on 2016/8/12.
 * 回购订单适配器Adapter
 */
public class OrderAdapter extends BaseAdapter {
    private Context context;
    private BitmapUtils bitmapUtils;
    private ArrayList<OrderBean> list;

    public OrderAdapter(Context context, BitmapUtils bitmapUtils, ArrayList<OrderBean> list) {
        this.context = context;
        this.bitmapUtils = bitmapUtils;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            view = View.inflate(context, R.layout.order_item_listview, null);
            holder.OrderTime = (TextView) view.findViewById(R.id.order_time);
            holder.OrderStatus = (TextView) view.findViewById(R.id.order_status);
            holder.OrderAbums = (TextView) view.findViewById(R.id.order_ablums);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.OrderTime.setText(list.get(i).getCreate_time());
        // holder.OrderStatus.setText(list.get(i).getOrderstatus());
        String status = list.get(i).getOrder_status();
        holder.OrderStatus.setText(status);
        if (status.equals("待确认")) {
            holder.OrderStatus.setTextColor(Color.parseColor("#ff9900"));
        } else if (status.equals("已完成")) {
            holder.OrderStatus.setTextColor(Color.GRAY);
        } else if (status.equals("订单驳回")) {
            holder.OrderStatus.setTextColor(Color.parseColor("#e20000"));
        }
        holder.OrderAbums.setText(list.get(i).getGoods_name());
        return view;
    }
    public class ViewHolder {
        public TextView OrderTime;
        public TextView OrderStatus;
        public TextView OrderAbums;
    }

}
