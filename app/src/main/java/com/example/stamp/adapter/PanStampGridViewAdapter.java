package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.view.RotateTextView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * 淘邮票的GridView的适配器
 * Created by Administrator on 2016/8/4.
 */
public class PanStampGridViewAdapter extends BaseAdapter {

    private ArrayList<StampTapBean.StampList> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public PanStampGridViewAdapter(Context context, ArrayList<StampTapBean.StampList> list, BitmapUtils bitmap) {
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
        StampTapBean.StampList stampTapBean = list.get(i);
        //设置图片
        bitmap.display(viewHolder.mIcon, stampTapBean.getStamp_img());
        if(i==0||i==1){
            viewHolder.mSource.setText("邮市");
            viewHolder.mName.setText("庚申年");
        }else if(i==2||i==3){
            viewHolder.mSource.setText("商城");
            viewHolder.mName.setText("戊戌年");
        }else if(i==4||i==5){
            viewHolder.mSource.setText("竞拍");
            viewHolder.mName.setText("康熙年");
        }
        viewHolder.mMoney.setText("￥"+stampTapBean.getCurrent_price());


        return view;
    }


    public class ViewHolder {
        public ImageView mIcon;//图片
        public TextView mName, mMoney; //名称,钱数,
        public RotateTextView mSource;// 商品类型
    }
}
