package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import cn.com.chinau.R;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.utils.MyLog;

/**
 * 确认订单的适配器
 * Created by Administrator on 2016/8/15.
 */
public class FirmOrderExpandableAdapter extends BaseAdapter {

//    private ShopNameBean shopNameBean;
private HashMap<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> groupSet;
    private Context context;
    private BitmapUtils bitmap;
    private String goods_count,goods_img,goods_name,goods_price;
    private Set<ShopNameBean.SellerBean.GoodsBean> goodsBeen;

    public FirmOrderExpandableAdapter(Context context, BitmapUtils bitmap,HashMap<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> groupSet) {
        this.groupSet = groupSet;
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public int getCount() {
        return groupSet.size();
    }

    @Override
    public Object getItem(int position) {
        return groupSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        if (convertView == null) {
            itemHolder = new ItemHolder();
            convertView = View.inflate(context, R.layout.firmorder_expandable_item_child, null);
            itemHolder.mCount = (TextView) convertView.findViewById(R.id.child_count);
            itemHolder.mName = (TextView) convertView.findViewById(R.id.child_name);
            itemHolder.mPrice = (TextView) convertView.findViewById(R.id.child_price);
            itemHolder.mImg = (ImageView) convertView.findViewById(R.id.child_icon);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }


        // 循环出空间数据

//        goodsBeen = groupSet.get(position);
//        ShopNameBean.GoodsBean list = iterator.next();
//        goods_count = list.getGoods_count();// 商品数量
//        goods_img = list.getGoods_img();// 商品图片url
//        goods_name = list.getGoods_name();// 商品名字
//        goods_price = list.getGoods_price();// 商品价格
//        MyLog.LogShitou("传过来选中的编号+数量", goods_count + "--" + goods_img+"--"+goods_name+"--"+goods_price);
//
//        itemHolder.mCount.setText("x"+goods_count);
//        itemHolder.mName.setText(goods_name);
//        itemHolder.mPrice.setText("￥"+goods_price);
//        bitmap.display(itemHolder.mImg, goods_img);


        // 循环出组件数据
        for (HashMap.Entry<Integer, Set<ShopNameBean.SellerBean.GoodsBean>> entry : groupSet.entrySet()) {
            Set<ShopNameBean.SellerBean.GoodsBean>  value = entry.getValue(); // 拿到循环后的value值
            for (int i = 0; i < value.size(); i++) {
                Iterator<ShopNameBean.SellerBean.GoodsBean> iterator = value.iterator();
                ShopNameBean.SellerBean.GoodsBean next = iterator.next();
                goods_count = next.getGoods_count();// 商品数量
                goods_img = next.getGoods_img();// 商品图片url
                goods_name = next.getGoods_name();// 商品名字
                goods_price = next.getGoods_price();// 商品价格
                MyLog.LogShitou("传过来选中的编号+数量", goods_count + "--" + goods_img+"--"+goods_name+"--"+goods_price);

                itemHolder.mCount.setText("x"+ goods_count);
                itemHolder.mName.setText(goods_name);
                itemHolder.mPrice.setText("￥"+goods_price);
                bitmap.display(itemHolder.mImg, goods_img);

            }
        }


        return convertView;
    }

    public class ItemHolder {
        public TextView mName, mPrice, mCount;//名称,单价,数量
        public ImageView mImg;//图片

    }




    /*@Override
    public int getGroupCount() {
        return groupSet.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupSet.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupSet.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return groupSet.get(i1).;
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

        itemHolder.mCount.setText(goods_list.get(i1).getGoods_count()+"");
        itemHolder.mName.setText(goods_list.get(i1).getGoods_name());
        itemHolder.mPrice.setText(goods_list.get(i1).getGoods_price()+"");

        bitmap.display(itemHolder.mImg, goods_list.get(i1).getGoods_img());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public class GroupHolder {
        *//**//**
         * 卖家类型：SC_ZY自营商城；SC_DSF第三方商家；YS邮市
         *//**//*
        public TextView mName, mType;//商城名称,类型
        public View mTop;//顶部间隔

    }

    public class ItemHolder {
        public TextView mName, mPrice, mCount;//名称,单价,数量
        public ImageView mImg;//图片


    }*/



}
