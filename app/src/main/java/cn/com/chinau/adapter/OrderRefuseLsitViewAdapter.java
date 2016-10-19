package cn.com.chinau.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;
import java.util.Map;

import cn.com.chinau.R;
import cn.com.chinau.activity.ApplyForRefuseActivity;
import cn.com.chinau.bean.OrderAllListViewGoodsBean;
import cn.com.chinau.dialog.ApplyForInterventionDialog;
import cn.com.chinau.utils.MyToast;

/**
 * Created by Administrator on 2016/9/5.
 * 商品订单（退款/退货）页面的ExpandableListView适配器
 */
public class OrderRefuseLsitViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<OrderAllListViewGoodsBean.Order_list.Seller_list> groups ;
    private Map<String, List<OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list>> goods;
    private BitmapUtils bitmapUtils;
    private ApplyForInterventionDialog auctiondialog;
    private EditText mEditDes;
    private TextView mSubmit, mCancle;

    /**
     * 构造函数
     *
     * @param groups  组元素列表
     * @param goods   子元素列表
     * @param context 上下文
     */

    public OrderRefuseLsitViewAdapter(Context context, BitmapUtils bitmapUtils, List<OrderAllListViewGoodsBean.Order_list.Seller_list> groups, Map<String, List<OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list>> goods) {
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
//        String mName = group.getSeller_name();
//        String mType = group.getSeller_type();

        if (i == 0) {
            gholder.mName.setText("邮票商城");
            gholder.mEntry.setText("竞拍");
            gholder.mView.setVisibility(View.GONE);
        } else if (i == 1) {
            gholder.mName.setText("邮票加盟商城");
            gholder.mEntry.setText("自营");
            gholder.mView.setVisibility(View.VISIBLE);
        } else if (i == 2) {
            gholder.mName.setText("我爱集邮商城");
            gholder.mEntry.setText("邮市");
            gholder.mView.setVisibility(View.VISIBLE);
        } else if (i == 3) {
            gholder.mName.setText("邮票收藏商城");
            gholder.mEntry.setText("第三方");
            gholder.mView.setVisibility(View.VISIBLE);
        }

        return view;


    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final GoodsViewHolder goodsholder;
        if (view == null) {
            goodsholder = new GoodsViewHolder();
            view = View.inflate(context, R.layout.view_ordersgoods_refuse_goods_item, null);
            goodsholder.img = (ImageView) view.findViewById(R.id.item_stamp_img);
            goodsholder.mNames = (TextView) view.findViewById(R.id.item_stamp_name);
            goodsholder.mPrice = (TextView) view.findViewById(R.id.item_stamp_price);
            goodsholder.mCount = (TextView) view.findViewById(R.id.item_stamp_count);
            goodsholder.mRejectLl = (LinearLayout) view.findViewById(R.id.item_refuse_reject_ll);
            goodsholder.mReject = (TextView) view.findViewById(R.id.item_refuse_reject_tv);
            goodsholder.mRefuse_Intervene = (TextView) view.findViewById(R.id.check_refuse_intervene);
            goodsholder.mAudit = (TextView) view.findViewById(R.id.item_refuse_audit_tv);
            goodsholder.mClose = (TextView) view.findViewById(R.id.item_refuse_close);
            view.setTag(goodsholder);
        } else {
            goodsholder = (GoodsViewHolder) view.getTag();
        }

        // 赋值
        final OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list goodsBean = (OrderAllListViewGoodsBean.Order_list.Seller_list.Order_detail_list) getChild(i, i1);

//        bitmapUtils.display(goodsholder.img, goodsBean.getGoods_img());
        if (i == 0) {
            goodsholder.mNames.setText(goodsBean.getGoods_name());
            goodsholder.mPrice.setText(goodsBean.getGoods_price());
            goodsholder.mCount.setText(goodsBean.getGoods_count());
            goodsholder.mRejectLl.setVisibility(View.GONE);
            goodsholder.mRefuse_Intervene.setVisibility(View.VISIBLE);
            goodsholder.mRefuse_Intervene.setText("退货/退款");
            goodsholder.mAudit.setVisibility(View.GONE);
            goodsholder.mClose.setVisibility(View.GONE);

        } else if (i == 1) {
            goodsholder.mNames.setText(goodsBean.getGoods_name());
            goodsholder.mPrice.setText(goodsBean.getGoods_price());
            goodsholder.mCount.setText(goodsBean.getGoods_count());
            goodsholder.mRejectLl.setVisibility(View.GONE);
            goodsholder.mRefuse_Intervene.setVisibility(View.GONE);
            goodsholder.mAudit.setVisibility(View.VISIBLE);
            goodsholder.mAudit.setText("退款审核中");
            goodsholder.mClose.setVisibility(View.GONE);
        } else if (i == 2) {
            goodsholder.mNames.setText(goodsBean.getGoods_name());
            goodsholder.mPrice.setText(goodsBean.getGoods_price());
            goodsholder.mCount.setText(goodsBean.getGoods_count());
            goodsholder.mRejectLl.setVisibility(View.GONE);
            goodsholder.mRefuse_Intervene.setVisibility(View.GONE);
            goodsholder.mAudit.setVisibility(View.GONE);
            goodsholder.mClose.setVisibility(View.VISIBLE);
            goodsholder.mClose.setText("退款关闭");
        } else if (i == 3) {
            goodsholder.mNames.setText(goodsBean.getGoods_name());
            goodsholder.mPrice.setText(goodsBean.getGoods_price());
            goodsholder.mCount.setText(goodsBean.getGoods_count());
            goodsholder.mRejectLl.setVisibility(View.VISIBLE);
            goodsholder.mReject.setText("退款驳回（卖家）");
            goodsholder.mRefuse_Intervene.setVisibility(View.VISIBLE);
            goodsholder.mRefuse_Intervene.setText("申请平台介入");
            goodsholder.mAudit.setVisibility(View.GONE);
            goodsholder.mClose.setVisibility(View.GONE);
        }

        goodsholder.mRefuse_Intervene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mText = goodsholder.mRefuse_Intervene.getText().toString().trim();
                if (mText.equals("退货/退款")) {
                    Intent intent = new Intent(context, ApplyForRefuseActivity.class);
                    context.startActivity(intent);
                } else {
                    DialogApplyFor(); // 申请平台介入Dialog
                }
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
        private ImageView img;// 图片
        private LinearLayout mRejectLl; //驳回LL
        private TextView mNames, mPrice, mCount, mReject, mRefuse_Intervene, mAudit, mClose; // 名称，价格,数量，驳回，退货/退款，退款审核中，退款关闭
        private View mView; //分割项
    }

    /**
     * 申请平台介入Dialog
     */
    private void DialogApplyFor() {
        auctiondialog = new ApplyForInterventionDialog(context);
        auctiondialog.show();
        mEditDes = (EditText) auctiondialog.findViewById(R.id.edit_des);
        mCancle = (TextView) auctiondialog.findViewById(R.id.btn_cancel);
        mSubmit = (TextView) auctiondialog.findViewById(R.id.btn_submit);

        // 取消
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auctiondialog.dismiss();
            }
        });

        // 提交
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mDes = mEditDes.getText().toString().trim();
                if (!TextUtils.isEmpty(mDes)) {
                    MyToast.showShort(context, "提交成功...");
                    auctiondialog.dismiss();
                } else {
                    MyToast.showShort(context, "请输入申请原因。。。");
                }
            }
        });
    }

}
