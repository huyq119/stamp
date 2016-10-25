package cn.com.chinau.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.PersonBean;
import cn.com.chinau.utils.StringTimeUtils;

/**
 * Date: 2016/10/25 00:39
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 收支明细list适配器
 */

public class WithdrawAdapter extends BaseAdapter {

    private ArrayList<PersonBean.History> mList;
    private Context context;

    public WithdrawAdapter(ArrayList<PersonBean.History> mList, Context context) {
        super();
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.withdraw_adapter_item, null);
            holder.mTime = (TextView) convertView.findViewById(R.id.withdraw_item_data);
            holder.mName = (TextView) convertView.findViewById(R.id.withdraw_item_name);
            holder.mData = (TextView) convertView.findViewById(R.id.withdraw_item_context);
            holder.mMoney = (TextView) convertView.findViewById(R.id.withdraw_item_money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PersonBean.History history = mList.get(position);
        holder.mTime.setText(StringTimeUtils.getShortTime(history.getTrx_date()));
        holder.mName.setText(history.getName());
        holder.mData.setText(history.getTrx_desc());
        String data = history.getTrx_amount();// 内容
        if (data.contains("+")) {
            holder.mMoney.setTextColor(Color.RED);
            holder.mMoney.setText(history.getTrx_amount());
        } else {
            holder.mMoney.setTextColor(android.graphics.Color.parseColor("#008500"));
            holder.mMoney.setText(history.getTrx_amount());

        }
        return convertView;
    }

    class ViewHolder {
        private TextView mTime, mName, mData, mMoney;// 时间,名称,内容,价值
    }


}
