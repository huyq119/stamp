package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.OrderDetailBean;
import cn.com.chinau.utils.MyLog;

/**
 * Date: 2016/11/4 17:32
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 订单详情适配器
 */

public class OrderDatilExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private BitmapUtils bitmap;

    private OrderDetailBean mOrderDetailBean;
    private CountOrPrice mCountOrPrice;
    private String mExpress_no;

    public OrderDatilExpandableListAdapter(Context context, BitmapUtils bitmap, OrderDetailBean mOrderDetailBean) {
        this.mOrderDetailBean = mOrderDetailBean;
        this.context = context;
        this.bitmap = bitmap;
    }

// 组列表项的数量
    @Override
    public int getGroupCount() {
        MyLog.LogShitou("======适配器11","=========="+mOrderDetailBean.getSeller_list().size());
        return mOrderDetailBean.getSeller_list().size();
    }

// 组所包含的子列表项的数量
    @Override
    public int getChildrenCount(int groupPosition) {
        MyLog.LogShitou("======适配器22","=========="+ mOrderDetailBean.getSeller_list().get(groupPosition).getOrder_detail_list().size());
        return mOrderDetailBean.getSeller_list().get(groupPosition).getOrder_detail_list().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        MyLog.LogShitou("======适配器33","=========="+ mOrderDetailBean.getSeller_list().get(groupPosition));
        return mOrderDetailBean.getSeller_list().get(0);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        MyLog.LogShitou("======适配器44", groupPosition+"=========="+childPosition);
        return mOrderDetailBean.getSeller_list().get(0).getOrder_detail_list().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = View.inflate(context, R.layout.shop_expandable_item_group, null);
            groupHolder.mName = (TextView) convertView.findViewById(R.id.group_name);
            groupHolder.mType = (TextView) convertView.findViewById(R.id.group_type);
            groupHolder.selected = (ImageView) convertView.findViewById(R.id.image_selected);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        //父View的实体类
        OrderDetailBean.SellerBean sellerBean = mOrderDetailBean.getSeller_list().get(groupPosition);
        //快递单号
        mExpress_no = sellerBean.getExpress_no();
        String mName = sellerBean.getSeller_name(); //卖家名称
        groupHolder.mName.setText(mName);
        String mTypes = sellerBean.getSeller_type(); // 卖家类型
        MyLog.LogShitou("======名称+类型55", mName+"=========="+mTypes);
        String Seller_no = sellerBean.getSeller_no();
        if (mTypes.equals("SC_ZY")) {
            groupHolder.mType.setText("自营");
        } else if (mTypes.equals("SC_DSF")) {
            groupHolder.mType.setText("第三方");
        } else if (mTypes.equals("YS")) {
            groupHolder.mType.setText("邮市");
        } else if (mTypes.equals("JP")) {
            groupHolder.mType.setText("竞拍");
        }
        return null;
    }

    public class GroupHolder {
        /**
         * 卖家类型：SC_ZY自营商城；SC_DSF第三方商家；YS邮市
         */
        public TextView mName, mType;//商城名称,类型
        public ImageView selected;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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

        ArrayList<OrderDetailBean.OrderDetailListBean> order_detail_list = mOrderDetailBean.getSeller_list().get(0).getOrder_detail_list();
        itemHolder.mName.setText(order_detail_list.get(childPosition).getGoods_name());

        String mGoods_count = order_detail_list.get(childPosition).getGoods_count();
        String mGoods_price = order_detail_list.get(childPosition).getGoods_price();

        itemHolder.mCount.setText("x" + mGoods_count);// 数量

        itemHolder.mPrice.setText("￥" + mGoods_price);// 价格

        MyLog.LogShitou("商品数量", mGoods_count+"====="+mGoods_price );

        //换算商品总价
        int count = Integer.valueOf(mGoods_count).intValue(); //转成int
        String str = mGoods_price.trim().replaceAll(",", "");// 价钱去掉首尾空格和逗号
        double countPrice = Double.parseDouble(str); //价钱转double
        MyLog.LogShitou("价钱转double+++商品数量", countPrice + "====" + count);
        double price = countPrice * count;//总价钱
        DecimalFormat df = new DecimalFormat("######0.00");// 保留2位小数
        String mprice = df.format(price);

        mCountOrPrice.GetCountOrPrice(mGoods_count,mprice,mExpress_no);// 装载的数据

//        goodsholder.mAllPrice.setText("￥"+mprice);// 显示的总价

        bitmap.display(itemHolder.mImg, order_detail_list.get(childPosition).getGoods_img());

        return convertView;
    }

    public class ItemHolder {
        public TextView mName, mPrice, mCount;//名称,单价,数量
        public ImageView mImg;//图片

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    // 定义一个接口装载数量，与总价
    public interface CountOrPrice {
        void GetCountOrPrice(String count, String price,String express_no);
    }

    public void setCountOrPrice(CountOrPrice mCountOrPrice) {
        this.mCountOrPrice = mCountOrPrice;
    }
}
