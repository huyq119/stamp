package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.MyStampGridViewBean;

/**
 * 我的邮寄GridView的适配器
 * Created by Administrator on 2016/8/4.
 */
public class MyStampGridViewAdapter extends BaseAdapter {

    private ArrayList<MyStampGridViewBean.StampList> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public MyStampGridViewAdapter(Context context, ArrayList<MyStampGridViewBean.StampList> list, BitmapUtils bitmap) {
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
            view = View.inflate(context, R.layout.mystamp_gridview_item, null);
            viewHolder.mImg = (ImageView) view.findViewById(R.id.mystamp_item_img);
            viewHolder.mName = (TextView) view.findViewById(R.id.mystamp_item_name);
            viewHolder.mCount= (TextView) view.findViewById(R.id.mystamp_item_count);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        MyStampGridViewBean.StampList mStampList = list.get(i);
//        viewHolder.mName.setText(mStampList.getStamp_name());
        viewHolder.mName.setText("庚申年");
        viewHolder.mCount.setText(mStampList.getStamp_count());
        //设置图片
        bitmap.display(viewHolder.mImg, mStampList.getStamp_img());


        return view;
    }


    public class ViewHolder {
        public ImageView mImg ;//图片
        public TextView mName,mCount;//名称,数量
    }
}
