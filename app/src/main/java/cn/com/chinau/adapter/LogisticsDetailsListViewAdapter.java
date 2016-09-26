package cn.com.chinau.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.bean.LogisticsBean;

/**
 * 物流信息的ListView适配器
 * Created by Angle on 2016/8/24.
 */
public class LogisticsDetailsListViewAdapter extends BaseAdapter {

    private Context context;
    private List<LogisticsBean.Express> mList;

    public LogisticsDetailsListViewAdapter(Context context, List<LogisticsBean.Express> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.logisticsdetails_listview_item, null);
            holder.mRoute = (TextView) view.findViewById(R.id.logistics_route);
            holder.mTime = (TextView) view.findViewById(R.id.logistics_time);
            holder.mTop = view.findViewById(R.id.logistics_top);
            holder.mBottom = view.findViewById(R.id.logistics_bottom);
            holder.mIcon = (ImageView) view.findViewById(R.id.logistics_image);
            holder.mBottomLine = view.findViewById(R.id.logisticsDetails_bottom);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mRoute.setText(mList.get(i).getExpress_route());
        holder.mTime.setText(mList.get(i).getExpress_time());
        if (i == 0) {
            holder.mRoute.setTextColor(Color.parseColor("#25ae5f"));
            holder.mTime.setTextColor(Color.parseColor("#25ae5f"));
            holder.mTop.setVisibility(View.INVISIBLE);
            holder.mBottom.setVisibility(View.VISIBLE);
            holder.mIcon.setImageResource(R.mipmap.circle_green);
            holder.mBottomLine.setVisibility(View.VISIBLE);
        } else if (i + 1 == mList.size()) {//说明是最后一条
            holder.mRoute.setTextColor(Color.parseColor("#999999"));
            holder.mTime.setTextColor(Color.parseColor("#999999"));
            holder.mTop.setVisibility(View.VISIBLE);
            holder.mBottom.setVisibility(View.INVISIBLE);
            holder.mIcon.setImageResource(R.mipmap.circle_grey);
            holder.mBottomLine.setVisibility(View.GONE);
        } else {
            holder.mRoute.setTextColor(Color.parseColor("#999999"));
            holder.mTime.setTextColor(Color.parseColor("#999999"));
            holder.mTop.setVisibility(View.VISIBLE);
            holder.mBottom.setVisibility(View.VISIBLE);
            holder.mIcon.setImageResource(R.mipmap.circle_grey);
            holder.mBottomLine.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public class ViewHolder {
        public TextView mTime, mRoute;//物流发生时间,物流行程
        public View mTop, mBottom;
        public ImageView mIcon;
        public View mBottomLine;//底部的线
    }
}
