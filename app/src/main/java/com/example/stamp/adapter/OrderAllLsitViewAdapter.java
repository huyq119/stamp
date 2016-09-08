package com.example.stamp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.bean.OrderAllListViewGoodsBean;
import com.example.stamp.bean.OrderAllListViewGroupBean;
import com.example.stamp.utils.MyToast;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 * 商品订单全部Fragment的ExpandableListView适配器
 */
public class OrderAllLsitViewAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<OrderAllListViewGroupBean> groups ;
    private Map<String, List<OrderAllListViewGoodsBean>> goods;
    private BitmapUtils bitmapUtils;
    /**
     * 构造函数
     *
     * @param groups   组元素列表
     * @param goods 子元素列表
     * @param context  上下文
     */

    public OrderAllLsitViewAdapter(Context context,BitmapUtils bitmapUtils,List<OrderAllListViewGroupBean> groups, Map<String, List<OrderAllListViewGoodsBean>> goods) {
        this.context = context;
        this.groups = groups;
        this.goods = goods;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        String groupName= groups.get(i).getGoods_name();
        return goods.get(groupName).size();
    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        List<OrderAllListViewGoodsBean> childs = goods.get(groups.get(i).getGoods_name());
        return childs.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    //表示孩子是否和组ID是跨基础数据的更改稳定
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final GroupViewHolder gholder;
        if (view == null) {
            gholder = new GroupViewHolder();
            view = View.inflate(context, R.layout.view_ordersgoods_all_group_item, null);
            gholder.mName = (TextView) view.findViewById(R.id.item_goods_name);
            gholder.mEntry = (TextView) view.findViewById(R.id.item_goods_order_entry);

            view.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) view.getTag();
        }
        final OrderAllListViewGroupBean group = (OrderAllListViewGroupBean) getGroup(i);


        if (i==0){
            gholder.mName.setText("邮票商城");
            gholder.mEntry.setText("竞拍");
        }else if (i==1){
            gholder.mName.setText("邮票加盟商城");
            gholder.mEntry.setText("自营");
        }
        else if (i==2){
            gholder.mName.setText("我爱集邮商城");
            gholder.mEntry.setText("邮市");
        }

        return view;


    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final GoodsViewHolder goodsholder;
        if (view == null) {
            goodsholder = new GoodsViewHolder();
            view = View.inflate(context, R.layout.view_ordersgoods_all_goods_item, null);
            goodsholder.img = (ImageView) view.findViewById(R.id.item_stamp_img);
            goodsholder.mNames = (TextView) view.findViewById(R.id.item_stamp_name);
            goodsholder.mPrice = (TextView) view.findViewById(R.id.item_stamp_price);
            goodsholder.mCount = (TextView) view.findViewById(R.id.item_stamp_count);
            goodsholder.mStatus= (TextView) view.findViewById(R.id.item_stamp_status);
            goodsholder.mAllCount= (TextView) view.findViewById(R.id.item_stamp_allCount);
            goodsholder.mAllPrice= (TextView) view.findViewById(R.id.item_stamp_allPrice);
            goodsholder.mBtnLl= (LinearLayout) view.findViewById(R.id.item_stamp_btn_ll);
            goodsholder.mLogistics = (TextView) view.findViewById(R.id.check_logistics_btn);
            goodsholder.mPayment = (TextView) view.findViewById(R.id.payment_btn);

            view.setTag(goodsholder);
        } else {
            goodsholder = (GoodsViewHolder) view.getTag();
        }

        // 赋值
        final OrderAllListViewGoodsBean goodsBean = (OrderAllListViewGoodsBean) getChild(i, i1);
        if (i==0){
//            bitmapUtils.display(goodsholder.img, goodsBean.getStamp_img());
            goodsholder.mNames.setText(goodsBean.getStamp_name());
            goodsholder.mPrice.setText(goodsBean.getStamp_price());
            goodsholder.mCount.setText(goodsBean.getStamp_count());
            goodsholder.mStatus.setText(goodsBean.getStamp_status());
            goodsholder.mAllCount.setText(goodsBean.getStamp_allCount());
            goodsholder.mAllPrice.setText(goodsBean.getStamp_allPrice());
            goodsholder.mBtnLl.setVisibility(View.GONE);
        }else if (i==1){
//            bitmapUtils.display(goodsholder.img, goodsBean.getStamp_img());
            goodsholder.mNames.setText(goodsBean.getStamp_name());
            goodsholder.mPrice.setText(goodsBean.getStamp_price());
            goodsholder.mCount.setText(goodsBean.getStamp_count());
            goodsholder.mStatus.setText("待付款");
            goodsholder.mAllCount.setText(goodsBean.getStamp_allCount());
            goodsholder.mAllPrice.setText(goodsBean.getStamp_allPrice());
            goodsholder.mBtnLl.setVisibility(View.VISIBLE);
            goodsholder.mPayment.setVisibility(View.VISIBLE);
            goodsholder.mLogistics.setVisibility(View.GONE);
        }else if (i==2){
//            bitmapUtils.display(goodsholder.img, goodsBean.getStamp_img());
            goodsholder.mNames.setText(goodsBean.getStamp_name());
            goodsholder.mPrice.setText(goodsBean.getStamp_price());
            goodsholder.mCount.setText(goodsBean.getStamp_count());
            goodsholder.mStatus.setText("待收货");
            goodsholder.mAllCount.setText(goodsBean.getStamp_allCount());
            goodsholder.mAllPrice.setText(goodsBean.getStamp_allPrice());
            goodsholder.mBtnLl.setVisibility(View.VISIBLE);
            goodsholder.mLogistics.setVisibility(View.VISIBLE);
            goodsholder.mPayment.setVisibility(View.GONE);
        }else if (i==3){
//            bitmapUtils.display(goodsholder.img, goodsBean.getStamp_img());
            goodsholder.mNames.setText(goodsBean.getStamp_name());
            goodsholder.mPrice.setText(goodsBean.getStamp_price());
            goodsholder.mCount.setText(goodsBean.getStamp_count());
            goodsholder.mStatus.setText("已完成");
            goodsholder.mAllCount.setText(goodsBean.getStamp_allCount());
            goodsholder.mAllPrice.setText(goodsBean.getStamp_allPrice());
            goodsholder.mBtnLl.setVisibility(View.VISIBLE);
            goodsholder.mLogistics.setVisibility(View.VISIBLE);
            goodsholder.mPayment.setVisibility(View.GONE);
        }

        goodsholder.mPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.showShort(context,"付款...");
            }
        });
        goodsholder.mLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.showShort(context,"查看了物流...");
            }
        });

//        bitmapUtils.display(goodsholder.img, goodsBean.getStamp_img());
        return view;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 组元素绑定器
     */
    private class GroupViewHolder {
        private TextView mName,mEntry; // 商城，购买类型
    }
    /**
     * 子元素绑定器
     */
    private class GoodsViewHolder {
        private ImageView img;
        private LinearLayout mBtnLl;
        private TextView mNames,mPrice,mCount,mStatus,mAllCount,mAllPrice,mLogistics,mPayment; // 名称，价格
    }

}
