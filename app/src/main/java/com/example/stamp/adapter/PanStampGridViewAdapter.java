package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.StampTapBean;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * 套邮票的GridView的适配器
 * Created by Administrator on 2016/8/4.
 */
public class PanStampGridViewAdapter extends BaseAdapter {

    private ArrayList<StampTapBean> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public PanStampGridViewAdapter(Context context, ArrayList<StampTapBean> list, BitmapUtils bitmap) {
        this.list = list;
        this.context = context;
        this.bitmap = bitmap;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.panstamp_gridview_item, null);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.PanStamp_item_icon);
            viewHolder.mMoney = (TextView) view.findViewById(R.id.PanStamp_item_money);
            viewHolder.mTitle = (TextView) view.findViewById(R.id.PanStamp_item_title);
//            viewHolder.mTag = (ImageView) view.findViewById(R.id.PanStamp_item_tag);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        //设置数据
//        StampTapBean stampTapBean = list.get(i);
//        viewHolder.mTitle.setText(stampTapBean.getTitle());
//        viewHolder.mMoney.setText(stampTapBean.getMoney());
//        //设置图片
//        bitmap.display(viewHolder.mIcon, stampTapBean.getIcon());


        return view;
    }


    public class ViewHolder {
        public ImageView mIcon, mTag;//图片,标签按钮
        public TextView mTitle, mMoney;//名称,钱数
    }
}
