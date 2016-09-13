package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.GoodsStampBean;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.view.RotateTextView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * 淘邮票的GridView的适配器
 * Created by Administrator on 2016/8/4.
 */
public class PanStampGridViewAdapter extends BaseAdapter {

    private ArrayList<GoodsStampBean.GoodsList> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public PanStampGridViewAdapter(Context context, ArrayList<GoodsStampBean.GoodsList> list, BitmapUtils bitmap) {
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
            viewHolder.mName = (TextView) view.findViewById(R.id.PanStamp_item_title);
            viewHolder.mMoney = (TextView) view.findViewById(R.id.PanStamp_item_money);
            viewHolder.mSource = (RotateTextView) view.findViewById(R.id.PanStamp_item_goodsSource);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        GoodsStampBean.GoodsList mGoodsList = list.get(i);
        //设置图片
        viewHolder.mName.setText(mGoodsList.getGoods_name());
        viewHolder.mMoney.setText("￥"+mGoodsList.getCurrent_price());
        bitmap.display(viewHolder.mIcon, mGoodsList.getGoods_img());
        viewHolder.mSource.setText(mGoodsList.getGoods_source());

    return view;
}


public class ViewHolder {
    public ImageView mIcon;//图片
    public TextView mName, mMoney; //名称,钱数,
    public RotateTextView mSource;// 商品类型
}
}
