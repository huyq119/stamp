package cn.com.chinau.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.ApplyForRefuseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.OrderAllListViewGoodsBean;
import cn.com.chinau.dialog.ApplyForInterventionDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * Created by Administrator on 2016/9/5.
 * 商品订单（退款/退货）页面的ExpandableListView适配器
 */
public class OrderRefuseLsitViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private BitmapUtils bitmapUtils;
    private ApplyForInterventionDialog auctiondialog;
    private EditText mEditDes;
    private TextView mSubmit, mCancle;
    ArrayList<OrderAllListViewGoodsBean.Order_list> order_list;
    private String goods_sn;
    private String order_sn;
    private String mToken,mUser_id,mDes;

    public OrderRefuseLsitViewAdapter(Context context, BitmapUtils bitmapUtils, ArrayList<OrderAllListViewGoodsBean.Order_list> order_list) {
        this.context = context;
        this.bitmapUtils = bitmapUtils;
        this.order_list = order_list;
    }

    @Override
    public int getGroupCount() {
        return order_list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return order_list.get(i).getSeller_list().get(0).getOrder_detail_list().size();
    }

    @Override
    public Object getGroup(int i) {
        return order_list.get(i).getSeller_list().get(0);
    }

    @Override
    public Object getChild(int i, int i1) {
        return order_list.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);
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
        // 获取父控件显示的
        OrderAllListViewGoodsBean.Seller_list seller_list = order_list.get(i).getSeller_list().get(0);
        String mOrder_sn = order_list.get(i).getOrder_sn();

//        goodsholder = new GoodsViewHolder();
//        goodsholder.mRefuse_Intervene.setTag();// 获取交易订单号

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
        // 子控件List
        OrderAllListViewGoodsBean.Order_detail_list order_detail_list = order_list.get(i).getSeller_list().get(0).getOrder_detail_list().get(i1);

        String mOrder_status = order_list.get(i).getOrder_status();// 获取订单状态
        goodsholder.mRefuse_Intervene.setTag(i + "," + i1);// 获取父控件i,子控件i1

        if (order_list != null) {
            // 获取子控件的值
            String mImg = order_detail_list.getGoods_img();
            String mName = order_detail_list.getGoods_name();
            String mPrice = order_detail_list.getGoods_price();
            String mCounts = order_detail_list.getGoods_count();// 商品数量
            String mStatues = order_detail_list.getStatus(); // 订单明细状态


//            String mDetailSn = order_detail_list.getOrder_detail_sn(); // 订单明细编号
            // 赋值
            bitmapUtils.display(goodsholder.img, mImg);
            goodsholder.mNames.setText(mName); // 名称
            goodsholder.mPrice.setText(mPrice); // 价钱
            goodsholder.mCount.setText(mCounts); // 数量

//            int count =Integer.valueOf(mCounts).intValue(); //转成int
//            double countPrice =Double.parseDouble(mPrice); //价钱转double
//            MyLog.LogShitou("价钱转double",countPrice+"");
//            double price = countPrice * count;//总价钱
//            MyLog.LogShitou("总价",price+"");

            if (mStatues.equals("REFUND")) { // 退货/退款
                goodsholder.mRejectLl.setVisibility(View.GONE);
                goodsholder.mRefuse_Intervene.setVisibility(View.VISIBLE);
                goodsholder.mRefuse_Intervene.setText("退货/退款");
                goodsholder.mAudit.setVisibility(View.GONE);
                goodsholder.mClose.setVisibility(View.GONE);
            } else if (mStatues.equals("WAIT_REFUND")) { //退款审核中
                goodsholder.mRejectLl.setVisibility(View.GONE);
                goodsholder.mRefuse_Intervene.setVisibility(View.GONE);
                goodsholder.mAudit.setVisibility(View.VISIBLE);
                goodsholder.mAudit.setText("退款审核中");
                goodsholder.mClose.setVisibility(View.GONE);

            } else if (mStatues.equals("REFUNDING")) { // 退款中
                goodsholder.mRejectLl.setVisibility(View.GONE);
                goodsholder.mRefuse_Intervene.setVisibility(View.GONE);
                goodsholder.mAudit.setVisibility(View.VISIBLE);
                goodsholder.mAudit.setText("退款中");
                goodsholder.mClose.setVisibility(View.GONE);
            } else if (mStatues.equals("REJECT_SELLER")) { // 退款驳回（卖家）
                goodsholder.mRejectLl.setVisibility(View.VISIBLE);
                goodsholder.mReject.setText("退款驳回（卖家）");
                goodsholder.mRefuse_Intervene.setVisibility(View.VISIBLE);
                goodsholder.mRefuse_Intervene.setText("申请平台介入");
                goodsholder.mAudit.setVisibility(View.GONE);
                goodsholder.mClose.setVisibility(View.GONE);

            } else if (mStatues.equals("REJECT")) { // 退款驳回（平台）
                goodsholder.mRejectLl.setVisibility(View.VISIBLE);
                goodsholder.mReject.setText("退款驳回（平台）");
                goodsholder.mRefuse_Intervene.setVisibility(View.GONE);
                goodsholder.mAudit.setVisibility(View.GONE);
                goodsholder.mClose.setVisibility(View.GONE);
            } else if (mStatues.equals("REFUNDED")) { // 退款完成
                goodsholder.mRejectLl.setVisibility(View.GONE);
                goodsholder.mRefuse_Intervene.setVisibility(View.GONE);
                goodsholder.mAudit.setVisibility(View.GONE);
                goodsholder.mClose.setVisibility(View.VISIBLE);
                goodsholder.mClose.setText("已完成");
            } else if (mStatues.equals("REFUND_FAIL")) { // 退款关闭
                goodsholder.mRejectLl.setVisibility(View.GONE);
                goodsholder.mRefuse_Intervene.setVisibility(View.GONE);
                goodsholder.mAudit.setVisibility(View.GONE);
                goodsholder.mClose.setVisibility(View.VISIBLE);
                goodsholder.mClose.setText("退款关闭");
            }
        }

        // 点击退款/退货，申请平台介入监听事件
        goodsholder.mRefuse_Intervene.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
                String[] split = tag.split(",");
                String group_i = split[0];
                String child_i1 = split[1];
                order_sn = order_list.get(Integer.valueOf(group_i)).getOrder_sn();// 获取订单号
                // 获取商品编号
                goods_sn = order_list.get(Integer.valueOf(group_i)).getSeller_list().get(0).getOrder_detail_list().get(Integer.valueOf(child_i1)).getGoods_sn();

                String mText = goodsholder.mRefuse_Intervene.getText().toString().trim();
                MyLog.LogShitou("点击退款获取商品编号和订单编号", "=商品编号=" + goods_sn + "==订单号=" + order_sn + "==" + mText);
                if (mText.equals("退货/退款")) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticField.ORDER_SN, order_sn);// 订单编号
                    bundle.putString(StaticField.GOODS_SN, goods_sn); // 商品编号
                    intent.setClass(context, ApplyForRefuseActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    DialogApplyFor(); // 申请平台介入Dialog
                }
            }
        });
        return view;
    }

    // 获取用户名，标识
    private void GetUserIdToken(){
        SharedPreferences sp = context.getSharedPreferences(StaticField.NAME, context.MODE_PRIVATE);
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
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
        // 名称，价格,数量，驳回，退货/退款，退款审核中，退款关闭
        private TextView mNames, mPrice, mCount, mReject, mRefuse_Intervene, mAudit, mClose;
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
                 mDes = mEditDes.getText().toString().trim();
                if (!TextUtils.isEmpty(mDes)) {
                    GetUserIdToken(); // 获取用户名，标识
                    ApplyForRefuseIntent(StaticField.PTJR);//
//                    MyToast.showShort(context, "提交成功...");
                    auctiondialog.dismiss();
                } else {
                    MyToast.showShort(context, "请输入申请原因。。。");
                }
            }
        });
    }

    // 提交申请退款网络请求
    private void ApplyForRefuseIntent(final String refund_type) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ORDERREFUND);// 申请退款接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id); // 用户ID
                params.put(StaticField.REFUNDTYPE, refund_type); // 退款类型
//                if (refund_type.equals(StaticField.TH)) {
//                    params.put(StaticField.EXPRESS_COMP, mExpressComp); // 快递公司
//                    params.put(StaticField.EXPRESS_NO, mCourieNO); // 快递单号
//                }
//                params.put(StaticField.GOODS_SN, goods_sn); // 商品编号
                params.put(StaticField.ORDER_SN, order_sn); // 订单编号

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                params.put(StaticField.DETAIL, mDes); // 退款说明
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("====提交申请", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }

        });

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 提交退款申请
                    Gson gson = new Gson();
                    BaseBean mBaseBean = gson.fromJson((String) msg.obj, BaseBean.class);
                    if (mBaseBean.getRsp_code().equals("0000")) {
                        MyToast.showShort(context, "申请成功");
                    } else {
                        MyToast.showShort(context, mBaseBean.getRsp_msg());
                    }
                    break;

            }

        }
    };



}
