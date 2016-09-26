package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.bean.DesignerBean;

/**
 * 设计家适配器
 * Created by Administrator on 2016/8/4.
 */
public class DesignerListViewAdapter extends BaseAdapter {

    private List<DesignerBean.Designer> mList;
    private Context context;
    private BitmapUtils bitmapUtils;

    public DesignerListViewAdapter(Context context, BitmapUtils bitmapUtils, List<DesignerBean.Designer> mList) {
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
            view = View.inflate(context, R.layout.designer_listview_item, null);
            holder.mIcon = (ImageView) view.findViewById(R.id.designer_item_icon);
            holder.mChName = (TextView) view.findViewById(R.id.designer_item_chName);
            holder.mEnName = (TextView) view.findViewById(R.id.designer_item_enName);
            holder.mDetail = (TextView) view.findViewById(R.id.designer_item_detail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mChName.setText(mList.get(i).getChinese_name());
        holder.mEnName.setText(mList.get(i).getEnglish_name());
        holder.mDetail.setText(mList.get(i).getResume());

        bitmapUtils.display(holder.mIcon, mList.get(i).getHeader_img());
        return view;
    }

    public class ViewHolder {
        public ImageView mIcon;
        public TextView mChName,mEnName, mDetail;
    }
}
