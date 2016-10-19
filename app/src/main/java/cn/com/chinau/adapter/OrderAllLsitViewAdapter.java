package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;
import java.util.Map;

import cn.com.chinau.R;
import cn.com.chinau.bean.OrderAllListViewGoodsBean;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;

/**
 * Created by Administrator on 2016/9/5.
 * 商品订单（全部）页面的ExpandableListView适配器
 */
public class OrderAllLsitViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<OrderAllListViewGoodsBean.Order_list.Seller_list> groups ;
    private Map<String, List<OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list>> goods;
    private BitmapUtils bitmapUtils;

    /**
     *
     * @param context
     * @param bitmapUtils
     * @param goods
     */
    public OrderAllLsitViewAdapter(Context context,BitmapUtils bitmapUtils,List<OrderAllListViewGoodsBean.Order_list.Seller_list> groups,
                                   Map<String, List<OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list>> goods) {
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
        String groupName = groups.get(i).getSeller_name();
        return goods.get(groupName).size();


    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        List<OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list> childs = goods.get(groups.get(i).getSeller_name());
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
            gholder.mView = (LinearLayout) view.findViewById(R.id.blank_view_ll);
            gholder.mName = (TextView) view.findViewById(R.id.item_goods_name);
            gholder.mEntry = (TextView) view.findViewById(R.id.item_goods_order_entry);

            view.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) view.getTag();
        }
        final OrderAllListViewGoodsBean.Order_list.Seller_list group = (OrderAllListViewGoodsBean.Order_list.Seller_list)getGroup(i);
        String mName = group.getSeller_name();// 卖家名称
        gholder.mName.setText(mName);
        String mType = group.getSeller_type(); // 卖家类型
        if (mType.equals("SC_ZY")) {
            gholder.mEntry.setText("自营");

        } else if (mType.equals("SC_ZY")) {
            gholder.mEntry.setText("SC_DSF");
        }
        if (mType.equals("YS")) {
            gholder.mEntry.setText("邮市");
        }
        if (mType.equals("JP")) {
            gholder.mEntry.setText("竞拍");
        }
        if (i == 0) {
            gholder.mView.setVisibility(View.GONE);
        } else {
            gholder.mView.setVisibility(View.VISIBLE);
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
            goodsholder.mStatus = (TextView) view.findViewById(R.id.item_stamp_status);
            goodsholder.mAllCount = (TextView) view.findViewById(R.id.item_stamp_allCount);
            goodsholder.mAllPrice = (TextView) view.findViewById(R.id.item_stamp_allPrice);
            goodsholder.mBtnLl = (LinearLayout) view.findViewById(R.id.item_stamp_btn_ll);
            goodsholder.mLogistics = (TextView) view.findViewById(R.id.check_logistics_btn);
            goodsholder.mPayment = (TextView) view.findViewById(R.id.payment_btn);

            view.setTag(goodsholder);
        } else {
            goodsholder = (GoodsViewHolder) view.getTag();
        }

        // 子控件List
        final OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list goodsBean = (OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list) getChild(i, i1);

        // 获取子控件的值
        String mImg = goodsBean.getGoods_img();
        String mName = goodsBean.getGoods_name();
        String mGoodeSn = goodsBean.getGoods_sn();// 商品编号
        String mPrice = goodsBean.getGoods_price();
        String mCounts = goodsBean.getGoods_count();// 商品数量
        String mStatues = goodsBean.getStatus(); // 订单状态
        String mDetailSn = goodsBean.getOrder_detail_sn(); // 订单明细状态
        // 赋值
        bitmapUtils.display(goodsholder.img, mImg);
        goodsholder.mNames.setText(mName); // 名称
        goodsholder.mPrice.setText("￥" + mPrice); // 价钱
        goodsholder.mCount.setText(mCounts); // 数量
        goodsholder.mAllCount.setText(mCounts); // 总数量

        int count =Integer.valueOf(mCounts).intValue(); //转成int
        double countPrice =Double.parseDouble(mPrice); //价钱转double
        MyLog.LogShitou("价钱转double",countPrice+"");
        double price = countPrice * count;//总价钱
        goodsholder.mAllPrice.setText(String.valueOf(price));// 总价
        MyLog.LogShitou("总价",price+"");

        if (mStatues.equals("INIT")){ // 待付款
            goodsholder.mStatus.setText("待付款");
            goodsholder.mBtnLl.setVisibility(View.VISIBLE);
            goodsholder.mPayment.setVisibility(View.VISIBLE);
            goodsholder.mLogistics.setVisibility(View.GONE);
        }else if (mStatues.equals("UNSHIPPED")){ //待发货
            goodsholder.mStatus.setText("待发货");
            goodsholder.mBtnLl.setVisibility(View.GONE);
        }else if (mStatues.equals("SHIPPED")){ // 待收货
            goodsholder.mStatus.setText("待收货");
            goodsholder.mBtnLl.setVisibility(View.VISIBLE);
            goodsholder.mLogistics.setVisibility(View.VISIBLE);
            goodsholder.mPayment.setVisibility(View.GONE);

        }else if (mStatues.equals("SIGN")){ // 已签收
            goodsholder.mStatus.setText("已签收");
            goodsholder.mBtnLl.setVisibility(View.VISIBLE);
            goodsholder.mLogistics.setVisibility(View.VISIBLE);
            goodsholder.mPayment.setVisibility(View.GONE);

        }else if (mStatues.equals("SUCCESS")){ // 交易完毕
            goodsholder.mStatus.setText("已完成");
            goodsholder.mBtnLl.setVisibility(View.VISIBLE);
            goodsholder.mLogistics.setVisibility(View.VISIBLE);
            goodsholder.mPayment.setVisibility(View.GONE);

        }else if (mStatues.equals("CLOSED")){ // 交易关闭
            goodsholder.mStatus.setText("交易关闭");
            goodsholder.mBtnLl.setVisibility(View.GONE);
        }

        goodsholder.mPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.showShort(context, "付款...");
            }
        });
        goodsholder.mLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.showShort(context, "查看了物流...");
            }
        });


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
        private TextView mName, mEntry; // 商城，购买类型
        private LinearLayout mView; //分割项
    }

    /**
     * 子元素绑定器
     */
    private class GoodsViewHolder {
        private ImageView img;
        private LinearLayout mBtnLl;
        private TextView mNames, mPrice, mCount, mStatus, mAllCount, mAllPrice, mLogistics, mPayment; // 名称，价格
    }

}
