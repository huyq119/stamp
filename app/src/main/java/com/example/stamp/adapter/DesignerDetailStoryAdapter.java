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
 * 设计故事适配器
 */
public class DesignerDetailStoryAdapter extends BaseAdapter{
    private ArrayList<DesignerDetailsBean.DesignerStory> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public DesignerDetailStoryAdapter(Context context, ArrayList<DesignerDetailsBean.DesignerStory> list, BitmapUtils bitmap) {
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
            view = View.inflate(context, R.layout.designer_story_listview_item, null);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.story_item_icon_image);
            viewHolder.mName = (TextView) view.findViewById(R.id.story_item_name_tv);
            viewHolder.mAuthor = (TextView) view.findViewById(R.id.story_author_detail_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        DesignerDetailsBean.DesignerStory mDesignerStory = list.get(i);
        viewHolder.mName.setText(mDesignerStory.getName());
        viewHolder.mAuthor.setText(mDesignerStory.getChinese_name());
        //设置图片
        bitmap.display(viewHolder.mIcon, mDesignerStory.getStory_img());


        return view;
    }


    public class ViewHolder {
        public ImageView mIcon;//图片
        public TextView mName, mAuthor;//名称，作者
    }

}
