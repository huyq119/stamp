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
 * 艺术作品适配器
 */
public class DesignerDetailWorksAdapter extends BaseAdapter{
    private ArrayList<DesignerDetailsBean.DesignerWorks> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public DesignerDetailWorksAdapter(Context context, ArrayList<DesignerDetailsBean.DesignerWorks> list, BitmapUtils bitmap) {
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
            view = View.inflate(context, R.layout.designer_works_listview_item, null);
            viewHolder.mImg = (ImageView) view.findViewById(R.id.works_works_img);
            viewHolder.mName = (TextView) view.findViewById(R.id.works_name_tv);
            viewHolder.mSerialNo = (TextView) view.findViewById(R.id.works_serial_no_tv);
            viewHolder.mClass = (TextView) view.findViewById(R.id.works_class_tv);
            viewHolder.mCategory = (TextView) view.findViewById(R.id.works_category_tv);
            viewHolder.mPublishDate = (TextView) view.findViewById(R.id.works_publish_date_tv);
            viewHolder.mSuitCount = (TextView) view.findViewById(R.id.works_suit_count_tv);
            viewHolder.mCurrentPrice = (TextView) view.findViewById(R.id.works_current_price_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        DesignerDetailsBean.DesignerWorks mDesignerWorks = list.get(i);
        viewHolder.mName.setText(mDesignerWorks.getWorks_name());
        viewHolder.mSerialNo.setText(mDesignerWorks.getSerial_no());
        String mCategory = mDesignerWorks.getCategory();
        if (mCategory.equals("ML")){
            viewHolder.mCategory.setText("邮票目录");
        }else if(mCategory.equals("SC")){
            viewHolder.mCategory.setText("签名邮品");
        }else if(mCategory.equals("YS")){
            viewHolder.mCategory.setText("邮市");
        }
        viewHolder.mClass.setText(mDesignerWorks.getCategory());
        viewHolder.mPublishDate.setText(mDesignerWorks.getPublish_date());
        viewHolder.mSuitCount.setText(mDesignerWorks.getSuit_count());
        viewHolder.mCurrentPrice.setText(mDesignerWorks.getCurrent_price());
        //设置图片
        bitmap.display(viewHolder.mImg, mDesignerWorks.getWorks_img());

        return view;
    }


    public class ViewHolder {
        public ImageView mImg;//图片
        public TextView mName, mSerialNo,mCategory,mPublishDate,mSuitCount,mCurrentPrice,mClass;//名称，作者
    }

}
