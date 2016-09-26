package cn.com.chinau.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.bean.MyStampGridViewBean;
import cn.com.chinau.utils.MyLog;

/**
 * 我的邮集中GridView的适配器
 * Created by Angle on 2016/9/25.
 */
public class GridViewAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;

    private List<MyStampGridViewBean.StampList> mList;
    private int index;
    private BitmapUtils bitmap;
    //这个是编辑按钮的标记
    private boolean flag;


    //每页显示的最大的数量
    private int PagerNum;

    public GridViewAdapter(Context context, List<MyStampGridViewBean.StampList> mList, int index, BitmapUtils bitmap, boolean flag) {
        this.mList = mList;
        this.index = index;
        this.bitmap = bitmap;
        this.flag = flag;
        MyLog.e("传入的" + flag);
        this.mLayoutInflater = LayoutInflater.from(context);
        PagerNum = context.getResources().getInteger(R.integer.PagerCount) * 2;
    }

    @Override
    public int getCount() {
        //这里判断数量的时候应该注意看是否是大于最大值了
        return mList.size() > (index + 1) * PagerNum ? PagerNum : (mList.size() - index * PagerNum);
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position + index * PagerNum);
    }

    @Override
    public long getItemId(int position) {
        return position + index * PagerNum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.mystamp_gridview_item, parent, false);
            holder.mImg = (ImageView) convertView.findViewById(R.id.mystamp_item_img);
            holder.mName = (TextView) convertView.findViewById(R.id.mystamp_item_name);
            holder.mCount = (TextView) convertView.findViewById(R.id.mystamp_item_count);
            holder.mNotCount = (TextView) convertView.findViewById(R.id.mystamp_item_nocCunt);
            holder.mEdit = (LinearLayout) convertView.findViewById(R.id.stamp_Edit);
            holder.mNotEdit = (LinearLayout) convertView.findViewById(R.id.stamp_NoEdit);
            holder.mAdd = (TextView) convertView.findViewById(R.id.mystamp_item_add);
            holder.mSubtract = (TextView) convertView.findViewById(R.id.mystamp_item_subtract);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //这里根据传入的flag进行判断是否进入编辑模式
        MyLog.e(flag + "");
        if (!flag) {//true代表编辑模式
            holder.mEdit.setVisibility(View.GONE);
            holder.mNotEdit.setVisibility(View.VISIBLE);
        } else {//false代表非编辑模式
            holder.mEdit.setVisibility(View.VISIBLE);
            holder.mNotEdit.setVisibility(View.GONE);
        }


        /**
         * 在给View绑定显示的数据时，计算正确的position = position + mIndex * mPageSize，
         */
        int pos = position + index * PagerNum;
        //标题
        holder.mName.setText(mList.get(pos).getStamp_name());
        holder.mCount.setText(mList.get(pos).getStamp_count());
        holder.mNotCount.setText(mList.get(pos).getStamp_count());
        bitmap.display(holder.mImg, mList.get(pos).getStamp_img());

        holder.mAdd.setTag(pos);
        holder.mAdd.setOnClickListener(this);
        holder.mSubtract.setTag(pos);
        holder.mSubtract.setOnClickListener(this);

        return convertView;
    }


    public class ViewHolder {
        public ImageView mImg;//图片
        public TextView mName, mCount, mNotCount, mAdd, mSubtract;//名称,数量,非编辑状态下数量，添加按钮，减少按钮
        public LinearLayout mEdit, mNotEdit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mystamp_item_add://添加
                MyStampGridViewBean.StampList stampAdd = mList.get((int) v.getTag());
                String addCount = stampAdd.getStamp_count();
                String newAddCount = String.valueOf(Integer.valueOf(addCount) + 1);
                mList.get((int) v.getTag()).setStamp_count(newAddCount);
                notifyDataSetChanged();
                break;
            case R.id.mystamp_item_subtract://减少
                MyStampGridViewBean.StampList stampSubtract= mList.get((int) v.getTag());
                String subtractCount = stampSubtract.getStamp_count();
                int newNum = Integer.valueOf(subtractCount) - 1;
                if (newNum<0)
                    return;
                String newSubtractCount = String.valueOf(newNum);
                mList.get((int) v.getTag()).setStamp_count(newSubtractCount);
                notifyDataSetChanged();
                break;
        }
    }

}
