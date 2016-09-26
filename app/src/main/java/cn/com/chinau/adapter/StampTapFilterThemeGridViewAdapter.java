package cn.com.chinau.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.com.chinau.R;

/**
 * 邮票目录筛选布局的（题材）的GridView适配器
 * Created by Administrator on 2016/7/29.
 */
public class StampTapFilterThemeGridViewAdapter extends BaseAdapter {

    private Context context;
    private String[] arr;
    private int clickTemp = -1;

    public StampTapFilterThemeGridViewAdapter(Context context, String[] arr) {
        this.context = context;
        this.arr = arr;
    }

    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    @Override
    public int getCount() {
        return arr.length;
    }

    @Override
    public Object getItem(int i) {
        return arr[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        viewHolder holder;
        if (view == null) {
            holder = new viewHolder();
            view = View.inflate(context, R.layout.stamptap_filter_gridview_item, null);
            holder.mContent = (TextView) view.findViewById(R.id.stamptap_filter_theme_item_content);
            holder.mContent.setVisibility(View.VISIBLE);
            holder.mContents = (TextView) view.findViewById(R.id.stamptap_filter_item_content);
            holder.mContents.setVisibility(View.GONE);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }

        holder.mContent.setText(arr[i]);

        //点击变换背景
        if (clickTemp == i) {
            holder.mContent.setBackgroundResource(R.mipmap.red_item_bg);
            holder.mContent.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.mContent.setBackgroundResource(R.drawable.rounded_rectangle);
            holder.mContent.setTextColor(Color.parseColor("#333333"));
        }

        return view;
    }


    public class viewHolder {
        public TextView mContent,mContents;
    }

}
