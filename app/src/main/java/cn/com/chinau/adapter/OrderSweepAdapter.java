package cn.com.chinau.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.OrderSweepBean;

/**
 * Created by Administrator on 2016/8/30.
 * 扫码回购订单适配器
 */
public class OrderSweepAdapter extends BaseAdapter {
    private Context context;
    private BitmapUtils bitmapUtils;
    private ArrayList<OrderSweepBean.Orderbean> list;

    public OrderSweepAdapter(Context context, BitmapUtils bitmapUtils, ArrayList<OrderSweepBean.Orderbean> list) {
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.order_sweep_item, null);
            holder.ScanIcon = (ImageView) convertView.findViewById(R.id.sweep_icon);
            holder.Scantime = (TextView) convertView.findViewById(R.id.sweep_time);
            holder.Scanstatus = (TextView) convertView.findViewById(R.id.sweep_status);
            holder.collect = (TextView) convertView.findViewById(R.id.collect_sweep);
            holder.Repurchase = (TextView) convertView.findViewById(R.id.repurchase_price);
            holder.Earnings = (TextView) convertView.findViewById(R.id.expected_earnings);
            holder.percent = (TextView) convertView.findViewById(R.id.percent);
            holder.ScanLinear = (LinearLayout) convertView.findViewById(R.id.scan_learlayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Scantime.setText(list.get(i).getCreate_time());
        holder.collect.setText(list.get(i).getGoods_name());
        holder.Repurchase.setText("回购价:￥" + list.get(i).getBuyback_price());
        holder.percent.setText(list.get(i).getIncome()); // 预计收益

        String ScanCodestatus = list.get(i).getOrder_status();// 订单状态

        if (ScanCodestatus.equals("INIT")) {// 待寄送
            holder.Earnings.setText("预计收益:");
            holder.Scanstatus.setTextColor(Color.parseColor("#ff9900"));
            holder.ScanLinear.setVisibility(View.VISIBLE);
            holder.Scanstatus.setText("待寄送");
        } else if (ScanCodestatus.equals("CLOSE")) { // 订单关闭
            holder.Scanstatus.setTextColor(Color.parseColor("#666"));
            holder.ScanLinear.setVisibility(View.INVISIBLE);
            holder.Scanstatus.setText("订单关闭");
        } else if (ScanCodestatus.equals("AUDITING")) {// 审核中
            holder.Earnings.setText("预计收益:");
            holder.Scanstatus.setTextColor(Color.parseColor("#ff9900"));
            holder.ScanLinear.setVisibility(View.VISIBLE);
            holder.Scanstatus.setText("审核中");
        } else if (ScanCodestatus.equals("FINISH")) { // 已完成
            holder.Earnings.setText("收益:");
            holder.ScanLinear.setVisibility(View.VISIBLE);
            holder.Scanstatus.setTextColor(Color.parseColor("#666"));
            holder.Scanstatus.setText("已完成");
        } else if (ScanCodestatus.equals("REFUSE")) { //订单驳回
            holder.Scanstatus.setTextColor(Color.parseColor("#e20000"));
            holder.Earnings.setText("收益:");
            holder.ScanLinear.setVisibility(View.VISIBLE);
            holder.Scanstatus.setText("订单驳回");
        }else{

        }

        bitmapUtils.display(holder.ScanIcon, list.get(i).getGoods_image());
        return convertView;
    }

    public class ViewHolder {
        private TextView Scantime;
        private TextView Scanstatus;
        private TextView collect;
        private TextView Repurchase;
        private TextView Earnings;
        private TextView percent;
        private ImageView ScanIcon;
        private LinearLayout ScanLinear;


    }
}
