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
import cn.com.chinau.bean.StampTapBean;

/**
 * 商城GridView的适配器
 * Created by Administrator on 2016/8/4.
 */
public class SelfMallGridViewAdapter extends BaseAdapter {

    private ArrayList<StampTapBean.StampList> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public SelfMallGridViewAdapter(Context context, ArrayList<StampTapBean.StampList> list, BitmapUtils bitmap) {
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
            view = View.inflate(context, R.layout.stamp_market_gridview_item, null);
            viewHolder.mName = (TextView) view.findViewById(R.id.Stamp_Market_name);
            viewHolder.mPrice = (TextView) view.findViewById(R.id.Stamp_Market_price);
            viewHolder.mImg = (ImageView) view.findViewById(R.id.Stamp_Market_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        StampTapBean.StampList mStampList = list.get(i);
        viewHolder.mName.setText(mStampList.getStamp_name());
        viewHolder.mPrice.setText(mStampList.getCurrent_price());
        //设置图片
        bitmap.display(viewHolder.mImg, mStampList.getStamp_img());


        return view;
    }
    public class ViewHolder {
        public ImageView mImg ;//图片
        public TextView mName,mPrice;//名称,价格
    }
}
