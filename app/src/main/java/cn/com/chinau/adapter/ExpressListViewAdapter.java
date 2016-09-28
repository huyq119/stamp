package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.Logistics;

/**
 * Created by Administrator on 2016/9/28.
 * 选择快递公司的适配器
 */
public class ExpressListViewAdapter extends BaseAdapter {


    private ArrayList<Logistics> mList;
    private Context context;
    public ExpressListViewAdapter(ArrayList<Logistics> mList, Context context) {
        super();
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.express_listview_item, null);
            holder.Name = (TextView) convertView.findViewById(R.id.express_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Name.setText(mList.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        private TextView Name;

    }


}
