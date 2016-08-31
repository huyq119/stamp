package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.ShopNameBean;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 购物车的适配器
 * Created by Administrator on 2016/8/15.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {

    private ShopNameBean shopNameBean;
    private Context context;
    private BitmapUtils bitmap;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public ExpandableAdapter(Context context, BitmapUtils bitmap, ShopNameBean shopNameBean) {
        this.shopNameBean = shopNameBean;
        this.context = context;
        this.bitmap = bitmap;
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }
    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < shopNameBean.getSeller_list().size(); i++) {
            getIsSelected().put(i, false);
        }
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
            view = View.inflate(context, R.layout.shop_expandable_item_group, null);
            groupHolder.mName = (TextView) view.findViewById(R.id.group_name);
            groupHolder.mType = (TextView) view.findViewById(R.id.group_type);
            groupHolder.checkBox= (CheckBox)view.findViewById(R.id.checkbox);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }
        groupHolder.mName.setText(shopNameBean.getSeller_list().get(i).getSeller_name());
        groupHolder.mType.setText(shopNameBean.getSeller_list().get(i).getSeller_type());
        // 根据isSelected来设置checkbox的选中状况
        groupHolder.checkBox.setChecked(getIsSelected().get(i));
        return view;
    }

    //内部展示的布局
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ItemHolder itemHolder;
        if (view == null) {
            itemHolder = new ItemHolder();
            view = View.inflate(context, R.layout.shop_expandable_item_child, null);
            itemHolder.mCount = (TextView) view.findViewById(R.id.child_count);
            itemHolder.mName = (TextView) view.findViewById(R.id.child_name);
            itemHolder.mPrice = (TextView) view.findViewById(R.id.child_price);
            itemHolder.mImg = (ImageView) view.findViewById(R.id.child_icon);
            itemHolder.checkBox = (CheckBox)view.findViewById(R.id.checkbox_shop);
            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) view.getTag();
        }
        ArrayList<ShopNameBean.GoodsBean> goods_list = shopNameBean.getSeller_list().get(i).getGoods_list();
        itemHolder.mCount.setText(goods_list.get(i1).getGoods_count()+"");
        itemHolder.mName.setText(goods_list.get(i1).getGoods_name());
        itemHolder.mPrice.setText(goods_list.get(i1).getGoods_price()+"");
        // 根据isSelected来设置checkbox的选中状况
        itemHolder.checkBox.setChecked(getIsSelected().get(i1));
        bitmap.display(itemHolder.mImg, goods_list.get(i1).getGoods_img());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }
    public static void setIsSelected(HashMap<Integer, Boolean> isSelected){
        ExpandableAdapter.isSelected = isSelected;
    }

    public class GroupHolder {
        /**
         * 卖家类型：SC_ZY自营商城；SC_DSF第三方商家；YS邮市
         */
        public TextView mName, mType;//商城名称,类型
        public CheckBox checkBox;

    }

    public class ItemHolder {
        public TextView mName, mPrice, mCount;//名称,单价,数量
        public ImageView mImg;//图片
        public CheckBox checkBox;

    }
}
