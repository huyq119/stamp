package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

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
//    private List<OrderAllListViewGoodsBean.Order_list.Seller_list> groups ;
//    private Map<String, List<OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list>> goods;
    private OrderAllListViewGoodsBean mOrderBean;
    private BitmapUtils bitmapUtils;
    ArrayList<OrderAllListViewGoodsBean.Order_list> order_list;
    /**
     *
     * @param context
     * @param bitmapUtils
     */
    public OrderAllLsitViewAdapter(Context context,BitmapUtils bitmapUtils,ArrayList<OrderAllListViewGoodsBean.Order_list> order_list) {
        this.context = context;
        this.bitmapUtils = bitmapUtils;
        this.order_list = order_list;
//        this.mOrderBean = mOrderBean;

    }
    // 父控件的条数
    @Override
    public int getGroupCount() {
        return order_list.size();
    }

    // 父控件的显示数据
    @Override
    public Object getGroup(int i) {
        return order_list.get(i).getSeller_list().get(0);
    }

    // 子控件的条数
    @Override
    public int getChildrenCount(int i) {
        return order_list.get(i).getSeller_list().get(0).getOrder_detail_list().size();
    }

    // 子控件显示数据
    @Override
    public Object getChild(int i, int i1) {
        return order_list.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);
    }
    @Override
    public long getGroupId(int i) {
//        MyLog.LogShitou("=======getGroupId===55555这个值是多少","============"+i);
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
//        MyLog.LogShitou("=======getChildId===6666这个值是多少",i+"======="+i1);
        return i1;
    }

    //表示孩子是否和组ID是跨基础数据的更改稳定
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final GroupViewHolder gholder;
        if (view == null) {
            gholder = new GroupViewHolder();
            view = View.inflate(context, R.layout.view_ordersgoods_all_group_item, null);
            gholder.mView = (LinearLayout) view.findViewById(R.id.blank_view_ll); // 商家上面的view布局
            gholder.mName = (TextView) view.findViewById(R.id.item_goods_name);// 商家名称
            gholder.mEntry = (TextView) view.findViewById(R.id.item_goods_order_entry); // 商家类型

            view.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) view.getTag();
        }
        // 获取父控件显示的
        OrderAllListViewGoodsBean.Seller_list seller_list = order_list.get(i).getSeller_list().get(0);
//        MyLog.LogShitou("========111父控件seller_list",seller_list.toString());
        String mName = seller_list.getSeller_name();// 卖家名称
        gholder.mName.setText(mName);
        String mType = seller_list.getSeller_type(); // 卖家类型
        if (mType.equals("SC_ZY")) {
            gholder.mEntry.setText("自营");

        } else if (mType.equals("SC_DSF")) {
            gholder.mEntry.setText("第三方");
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
        OrderAllListViewGoodsBean.Order_detail_list  order_detail_list = order_list.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);
//        MyLog.LogShitou("==ziizi===子控件List",order_detail_list.toString());
        if (order_detail_list !=null){
            // 获取子控件的值
            String mImg = order_detail_list.getGoods_img();
            String mName = order_detail_list.getGoods_name();
            String mGoodeSn = order_detail_list.getGoods_sn();// 商品编号
            String mPrice = order_detail_list.getGoods_price();
            String mCounts = order_detail_list.getGoods_count();// 商品数量
            String mStatues = order_detail_list.getStatus(); // 订单明细状态
            String mDetailSn = order_detail_list.getOrder_detail_sn(); // 订单明细编号
            // 赋值
            bitmapUtils.display(goodsholder.img, mImg);
            goodsholder.mNames.setText(mName); // 名称
            goodsholder.mPrice.setText(mPrice); // 价钱
            goodsholder.mCount.setText(mCounts); // 数量
            goodsholder.mAllCount.setText(mCounts); // 总数量

            int count =Integer.valueOf(mCounts).intValue(); //转成int
            String str = mPrice.trim().replaceAll(",","");// 价钱去掉首尾空格和逗号
            double countPrice =Double.parseDouble(str); //价钱转double

            MyLog.LogShitou("价钱转double+++商品数量",countPrice+"===="+count);

            double price = countPrice * count;//总价钱

            goodsholder.mAllPrice.setText(String.valueOf(price));// 显示的总价

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
        }

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
