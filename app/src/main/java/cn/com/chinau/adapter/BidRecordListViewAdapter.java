package cn.com.chinau.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.bean.StampDetailBean;
import cn.com.chinau.utils.MyLog;

/**
 * Created by Administrator on 2016/9/1.
 * 竞拍出价记录适配器
 */
public class BidRecordListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StampDetailBean.OfferListData> mList;

    public BidRecordListViewAdapter(Context context, ArrayList<StampDetailBean.OfferListData> mList) {
        this.context = context;
        this.mList = mList;
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
            view = View.inflate(context, R.layout.auction_bidrecord_pop_listview_item, null);
            holder.mTime = (TextView) view.findViewById(R.id.bidRecord_item_time);
            holder.mMobile = (TextView) view.findViewById(R.id.bidRecord_item_tel);
            holder.mPrice = (TextView) view.findViewById(R.id.bidRecord_item_price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTime.setText(mList.get(i).getOffer_time());// 出价时间
        holder.mMobile.setText(mList.get(i).getMobile());// 手机号
        String mPrice = mList.get(i).getPrice();
        holder.mPrice.setText("￥" + mPrice);// 价格

        String mUser_id = mList.get(i).getUser_id();
//        MyLog.LogShitou("用户id", mUser_id);
        //用来存储价格的集合
        SharedPreferences sp = context.getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        String myUser_id = sp.getString("userId", "");

        List<String> mArrList = new ArrayList<>();
        for (int j = 0; j < mList.size(); j++) {
            if (mList.get(j).getUser_id().equals("myUser_id")) {
                mArrList.add(mList.get(i).getPrice());
            }
        }
        MyLog.LogShitou("添加到的list的price",mList.get(i).getPrice()+"");




//        if(mUser_id != null|| !myUser_id.equals("")){
//            if(mUser_id.equals(myUser_id)){
//                holder.mTime.setTextColor(context.getResources().getColor(R.color.bule_font));
//                holder.mMobile.setTextColor(context.getResources().getColor(R.color.bule_font));
//                holder.mPrice.setTextColor(context.getResources().getColor(R.color.bule_font));
//            }
//        }

        return view;
    }


    public class ViewHolder {
        public TextView mTime, mMobile, mPrice;
    }
}
