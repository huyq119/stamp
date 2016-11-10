package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/22.
 * （查看订单详情）退货/退款
 */
public class LookOrderDetailRefuseBean extends BaseBean {
    public String order_sn;// 交易订单号
    public String create_time;// 订单创建时间
    public ArrayList<RefundLiistBean> refund_rec_list;// 退款记录列表

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public ArrayList<RefundLiistBean> getRefund_rec_list() {
        return refund_rec_list;
    }

    public void setRefund_rec_list(ArrayList<RefundLiistBean> refund_rec_list) {
        this.refund_rec_list = refund_rec_list;
    }


    public class RefundLiistBean{

    public String refund_time;// 退款退货发起时间
    public String refund_status;// 退款状态
    public String detail;// 结果说明

        public String getRefund_time() {
            return refund_time;
        }

        public void setRefund_time(String refund_time) {
            this.refund_time = refund_time;
        }

        public String getRefund_status() {
            return refund_status;
        }

        public void setRefund_status(String refund_status) {
            this.refund_status = refund_status;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }





}
