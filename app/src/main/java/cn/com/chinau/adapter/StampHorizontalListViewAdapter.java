package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.com.chinau.R;


/**
 * 邮市横向滑动的listView
 * Created by Administrator on 2016/8/5.
 */
public class StampHorizontalListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] mArr;
    private int clickTemp = 0;//点击的条目

    public StampHorizontalListViewAdapter(Context context, String[] mArr) {
        this.context = context;
        this.mArr = mArr;
    }

    @Override
    public int getCount() {
        return mArr.length;
    }

    //标识选择的Item
    public void setSelection(int position) {
        clickTemp = position;
    }

    @Override
    public Object getItem(int i) {
        return mArr[i];
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
            view = View.inflate(context, R.layout.stamp_horlistview_item, null);
            holder.mCategory = (TextView) view.findViewById(R.id.stamap_hLv_category);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mCategory.setText(mArr[i]);

        //点击变换背景
        if (clickTemp == i) {
            holder.mCategory.setBackgroundResource(R.drawable.vpi__tab_unselected_pressed_holo);
        } else {
            holder.mCategory.setBackgroundResource(R.drawable.vpi__tab_unselected_holo);
        }
        return view;
    }


    public class ViewHolder {
        public TextView mCategory;
    }
}
