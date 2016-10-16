package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.OrderSweepBean;

/**
 * Created by Administrator on 2016/8/12.
 * 回购订单适配器Adapter
 */
public class OrderAdapter extends BaseAdapter {
    private Context context;
    private BitmapUtils bitmapUtils;
    private ArrayList<OrderSweepBean.Orderbean> list;

    public OrderAdapter(Context context, BitmapUtils bitmapUtils, ArrayList<OrderSweepBean.Orderbean> list) {
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
        String status = list.get(i).getOrder_status();

        if (status.equals("0")) {
            holder.OrderStatus.setText("待确认");
            holder.OrderStatus.setTextColor(context.getResources().getColor(R.color.refuse));
        } else if (status.equals("1")) {
            holder.OrderStatus.setText("订单驳回");
            holder.OrderStatus.setTextColor(context.getResources().getColor(R.color.red_font));
        } else if (status.equals("2")) {
            holder.OrderStatus.setText("已完成");
            holder.OrderStatus.setTextColor(context.getResources().getColor(R.color.font));
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
