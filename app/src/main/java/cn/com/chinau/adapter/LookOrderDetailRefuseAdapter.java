package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.bean.LookOrderDetailRefuseBean;
import cn.com.chinau.utils.StringTimeUtils;

/**
 * Created by Administrator on 2016/9/22.
 * (查看订单详情)退款/退货页面List适配器
 */
public class LookOrderDetailRefuseAdapter extends BaseAdapter {
    private Context context;
    private List<LookOrderDetailRefuseBean.RefundLiistBean> mList;

    public LookOrderDetailRefuseAdapter(Context context, List<LookOrderDetailRefuseBean.RefundLiistBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.look_order_detail_listview_item, null);
            holder.mTime = (TextView) view.findViewById(R.id.item_time);
            holder.mState = (TextView) view.findViewById(R.id.item_state);
            holder.mDes = (TextView) view.findViewById(R.id.item_des);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // 组件赋值
        String mRefund_time = mList.get(i).getRefund_time(); //退款退货发起时间
        String time = StringTimeUtils.getStringByFormat(mRefund_time);// 截取去掉时分秒
//        MyLog.LogShitou("===截取去掉时分秒",time+"==="+mRefund_time);
        holder.mTime.setText(time);

        String mStatus = mList.get(i).getRefund_status();// 退款状态
        if (mStatus.equals("INIT")) {
            holder.mState.setText("未付款");
        } else if (mStatus.equals("APPLY_REFUND")) {
            holder.mState.setText("申请退款");
        } else if (mStatus.equals("REJECT")) {
            holder.mState.setText("审核驳回");
        } else if (mStatus.equals("REFUNDING")) {
            holder.mState.setText("退款中");
        } else if (mStatus.equals("REFUNDED")) {
            holder.mState.setText("已退款");
        } else if (mStatus.equals("REFUND_FAIL")) {
            holder.mState.setText("退款失败");
        } else if (mStatus.equals("WAIT_REFUND")) {
            holder.mState.setText("等待审核");
        } else if (mStatus.equals("REJECT_SELLER")) {
//            holder.mState.setText("等待审核");
        }

        holder.mDes.setText(mList.get(i).getDetail()); // 结果说明

        return view;
    }

    public class ViewHolder {
        public TextView mTime, mDes, mState;
    }
}
