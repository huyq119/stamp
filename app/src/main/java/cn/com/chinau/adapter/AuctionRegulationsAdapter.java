package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.bean.SysParamQueryBean;

/**
 * Created by Administrator on 2016/10/10.
 * 竞拍出价规则与协议Dialog适配器
 */
public class AuctionRegulationsAdapter extends BaseAdapter {

    private List<SysParamQueryBean.Sys_param_value.Auction_rule> mList;
    private Context context;

    public AuctionRegulationsAdapter(Context context, List<SysParamQueryBean.Sys_param_value.Auction_rule> mList) {
        this.mList = mList;
        this.context = context;
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
            view = View.inflate(context, R.layout.dialog_regulations_listview_item, null);
            holder.mScope = (TextView) view.findViewById(R.id.item_scope);
            holder.mPrice = (TextView) view.findViewById(R.id.item_price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mScope.setText(mList.get(i).getScope());
        holder.mPrice.setText("+"+mList.get(i).getPrice());
        return view;
    }

    public class ViewHolder {
        public TextView mScope, mPrice;// 范围，价格
    }

}
