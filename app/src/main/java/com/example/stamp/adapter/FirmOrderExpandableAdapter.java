package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.ShopNameBean;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * 确认订单的适配器
 * Created by Administrator on 2016/8/15.
 */
public class FirmOrderExpandableAdapter extends BaseExpandableListAdapter {

    private ShopNameBean shopNameBean;
    private Context context;
    private BitmapUtils bitmap;

    public FirmOrderExpandableAdapter(Context context, BitmapUtils bitmap, ShopNameBean shopNameBean) {
        this.shopNameBean = shopNameBean;
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public int getGroupCount() {
        return shopNameBean.getSeller_list().size();
    }

    @Override
    public int getChildrenCount(int i) {
        return shopNameBean.getSeller_list().get(i).getGoods_list().size();
    }

    @Override
    public Object getGroup(int i) {
        return shopNameBean.getSeller_list().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return shopNameBean.getSeller_list().get(i).getGoods_list().get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //外部展示的布局
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder groupHolder;
        if (view == null) {
            groupHolder = new GroupHolder();
            view = View.inflate(context, R.layout.firmorder_expandable_item_group, null);
            groupHolder.mName = (TextView) view.findViewById(R.id.group_name);
            groupHolder.mType = (TextView) view.findViewById(R.id.group_type);
            groupHolder.mTop = view.findViewById(R.id.group_item_top);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }

        if(i==0){
            groupHolder.mTop.setVisibility(View.GONE);
        }else {
            groupHolder.mTop.setVisibility(View.VISIBLE);
        }

        groupHolder.mName.setText(shopNameBean.getSeller_list().get(i).getSeller_name());
        groupHolder.mType.setText(shopNameBean.getSeller_list().get(i).getSeller_type());
        return view;
    }

    //内部展示的布局
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ItemHolder itemHolder;
        if (view == null) {
            itemHolder = new ItemHolder();
            view = View.inflate(context, R.layout.firmorder_expandable_item_child, null);
            itemHolder.mCount = (TextView) view.findViewById(R.id.child_count);
            itemHolder.mName = (TextView) view.findViewById(R.id.child_name);
            itemHolder.mPrice = (TextView) view.findViewById(R.id.child_price);
            itemHolder.mImg = (ImageView) view.findViewById(R.id.child_icon);
            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        ArrayList<ShopNameBean.GoodsBean> goods_list = shopNameBean.getSeller_list().get(i).getGoods_list();

        itemHolder.mCount.setText(goods_list.get(i1).getGoods_count());
        itemHolder.mName.setText(goods_list.get(i1).getGoods_name());
        itemHolder.mPrice.setText(goods_list.get(i1).getGoods_price());

        bitmap.display(itemHolder.mImg, goods_list.get(i1).getGoods_img());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public class GroupHolder {
        /**
         * 卖家类型：SC_ZY自营商城；SC_DSF第三方商家；YS邮市
         */
        public TextView mName, mType;//商城名称,类型
        public View mTop;//顶部间隔

    }

    public class ItemHolder {
        public TextView mName, mPrice, mCount;//名称,单价,数量
        public ImageView mImg;//图片

    }
}
