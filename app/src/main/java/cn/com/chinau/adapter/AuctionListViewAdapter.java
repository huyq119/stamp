package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.bean.GoodsStampBean;
import cn.com.chinau.view.TimeTextView;

/**
 * 竞拍ListView适配器
 * Created by Administrator on 2016/8/4.
 */
public class AuctionListViewAdapter extends BaseAdapter {

    private List<GoodsStampBean.GoodsList> mList;
    private Context context;
    private BitmapUtils bitmapUtils;

    public AuctionListViewAdapter(Context context, BitmapUtils bitmapUtils, List<GoodsStampBean.GoodsList> mList) {
        this.mList = mList;
        this.context = context;
        this.bitmapUtils = bitmapUtils;
    }

    @Override
    public int getCount() {
        return mList.size();
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
            view = View.inflate(context, R.layout.stamp_listview_item, null);
            holder.mIcon = (ImageView) view.findViewById(R.id.stamp_icon);
            holder.mTitle = (TextView) view.findViewById(R.id.stamp_title);
            holder.mTimeTv = (TextView) view.findViewById(R.id.stamp_time_tv);
            holder.mTimeLl = (LinearLayout) view.findViewById(R.id.stamp_time_ll);
            holder.mTime = (TimeTextView) view.findViewById(R.id.stamp_time);
            holder.mStatue = (TextView) view.findViewById(R.id.stamp_status);
            holder.mStampPrice = (TextView) view.findViewById(R.id.stamp_price);
            holder.mPrice = (TextView) view.findViewById(R.id.stamp_starting_price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final GoodsStampBean.GoodsList mGoodsList = mList.get(i);
        holder.mTitle.setText(mGoodsList.getGoods_name());
        holder.mTime.setText(mGoodsList.getLeft_time());
        String mStatues = mGoodsList.getAuction_status();
        holder.mPrice.setText("￥" +mGoodsList.getCurrent_price());


        if (mStatues.equals("DP")) {
            holder.mStatue.setText("未开始");
            holder.mTimeTv.setText("距离开拍：");
            holder.mStampPrice.setText("起拍价：");
            holder.mStatue.setTextColor(context.getResources().getColor(R.color.red_font));
        } else if (mStatues.equals("JP")) {
            holder.mStatue.setText("竞拍中");
            holder.mStatue.setTextColor(context.getResources().getColor(R.color.refuse));
            holder.mTimeTv.setText("剩余时间：");
            holder.mStampPrice.setText("当前价：");
        } else if (mStatues.equals("JS")) {
            holder.mStatue.setText("已结束");
            holder.mTimeTv.setText("距离开拍：");
            holder.mStampPrice.setText("成交价：");
            holder.mTimeLl.setVisibility(View.INVISIBLE);
            holder.mStatue.setTextColor(context.getResources().getColor(R.color.font_color));
        }


        int[] time = {01, 01, 05};

        holder.mTime.setTimes(time);
        if (!holder.mTime.isRun()) {
            holder.mTime.run();
        }
        bitmapUtils.display(holder.mIcon,mGoodsList.getGoods_img());
        return view;
    }

    public class ViewHolder {
        public ImageView mIcon;
        public LinearLayout mTimeLl;
        public TimeTextView mTime;
        public TextView mTitle, mStatue, mPrice, mTimeTv,mStampPrice;
    }
}
