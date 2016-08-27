package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.DesignerDetailsBean;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 * 名家访谈适配器
 */
public class DesignerDetailViewAdapter extends BaseAdapter{
    private ArrayList<DesignerDetailsBean.DesignerView> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public DesignerDetailViewAdapter(Context context, ArrayList<DesignerDetailsBean.DesignerView> list, BitmapUtils bitmap) {
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
            view = View.inflate(context, R.layout.designer_view_listview_item, null);
            viewHolder.mImage = (ImageView) view.findViewById(R.id.view_image_iv);
            viewHolder.mTitle = (TextView) view.findViewById(R.id.view_title_tv);
            viewHolder.mPublishDate = (TextView) view.findViewById(R.id.view_publish_date_tv);
            viewHolder.mAuthor = (TextView) view.findViewById(R.id.view_author_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        DesignerDetailsBean.DesignerView mDesignerView = list.get(i);
        viewHolder.mTitle.setText(mDesignerView.getView_title());
        viewHolder.mPublishDate.setText(mDesignerView.getPublish_date());
        viewHolder.mAuthor.setText("张安朴");
        //设置图片
        bitmap.display(viewHolder.mImage, mDesignerView.getView_image());


        return view;
    }


    public class ViewHolder {
        public ImageView mImage;//图片
        public TextView mTitle, mPublishDate,mAuthor;//名称，发布日期，作者
    }

}
