package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.com.chinau.R;

/**
 * 分类的PopupWindow的适配器
 * Created by Administrator on 2016/8/5.
 */
public class ClassifyPopupWindowAdapter extends BaseAdapter {
    private String[] mArr;
    private Context context;

    public ClassifyPopupWindowAdapter(String[] mArr, Context context) {
        this.mArr = mArr;
        this.context = context;
    }


    @Override
    public int getCount() {
        return mArr.length;
    }

    @Override
    public Object getItem(int position) {
        return mArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.popupwindow_classify_item, null);
            holder.Category = (TextView) convertView.findViewById(R.id.classify);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Category.setText(mArr[position]);
        return convertView;
    }

    class ViewHolder {
        private TextView Category;

    }
}
