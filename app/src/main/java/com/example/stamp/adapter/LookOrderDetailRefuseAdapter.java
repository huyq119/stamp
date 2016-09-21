package com.example.stamp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.LookOrderDetailRefuseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 * (查看订单详情)退款/退货页面List适配器
 */
public class LookOrderDetailRefuseAdapter extends BaseAdapter{
    private Context context;
    private List<LookOrderDetailRefuseBean> mList;
    public LookOrderDetailRefuseAdapter(Context context,List<LookOrderDetailRefuseBean> mList) {
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
            view = View.inflate(context, R.layout.look_order_detail_listview_item, null);
            holder.mTime = (TextView) view.findViewById(R.id.item_time);
            holder.mType = (TextView) view.findViewById(R.id.item_type);
            holder.mState = (TextView) view.findViewById(R.id.item_state);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        LookOrderDetailRefuseBean mbean = mList.get(i);
        Log.e("时间--->",mbean.getTime()+"---"+mbean.getType()+"--"+mbean.getState());
        holder.mTime.setText(mbean.getTime());
        holder.mType.setText(mbean.getType());
        holder.mState.setText(mbean.getState());

        return view;
    }
    public class ViewHolder {
        public TextView mTime, mType, mState;
    }
}
