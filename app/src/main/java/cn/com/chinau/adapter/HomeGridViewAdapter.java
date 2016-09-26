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
import cn.com.chinau.bean.HomeBean;

/**
 * 套邮票的GridView的适配器
 * Created by Administrator on 2016/8/4.
 */
public class HomeGridViewAdapter extends BaseAdapter {

    private List<HomeBean.Good> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public HomeGridViewAdapter(Context context, List<HomeBean.Good> list, BitmapUtils bitmap) {
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
            view = View.inflate(context, R.layout.home_gridview_item, null);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.home_item_icon);
            viewHolder.mMoney = (TextView) view.findViewById(R.id.home_item_money);
            viewHolder.mTitle = (TextView) view.findViewById(R.id.home_item_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        HomeBean.Good good = list.get(i);
        viewHolder.mTitle.setText(good.getGoods_name());
        viewHolder.mMoney.setText("￥"+good.getCurrent_price());
        //设置图片
        bitmap.display(viewHolder.mIcon, good.getGoods_img());
        return view;
    }


    public class ViewHolder {
        public ImageView mIcon;//图片,标签按钮
        public TextView mTitle, mMoney;//名称,钱数
    }
}
