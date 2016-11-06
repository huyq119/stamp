package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Date: 2016/11/4 14:55
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 订单详情实体类
 */

public class OrderDetailBean extends BaseBean {
    private String order_sn;
    private String create_time;
    private String order_status;
    private String end_pay_time;
    private String name;
    private String mobile;
    private String address;
    private String express_type;
    private String freight;
    private String service_fee_rate;
    private String service_fee;
    private String pay_type;
    private ArrayList<SellerBean> seller_list;

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

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getEnd_pay_time() {
        return end_pay_time;
    }

    public void setEnd_pay_time(String end_pay_time) {
        this.end_pay_time = end_pay_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }

    public ArrayList<SellerBean> getSeller_list() {
        return seller_list;
    }

    public void setSeller_list(ArrayList<SellerBean> seller_list) {
        this.seller_list = seller_list;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getService_fee_rate() {
        return service_fee_rate;
    }

    public void setService_fee_rate(String service_fee_rate) {
        this.service_fee_rate = service_fee_rate;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getExpress_type() {
        return express_type;
    }

    public void setExpress_type(String express_type) {
        this.express_type = express_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public class SellerBean {
        private String seller_name;
        private String seller_no;
        private String express_comp;
        private String express_no;
        private ArrayList<ExpressDetailBean> express_detail_list;// 物流详情列表
        private String seller_type;
        private ArrayList<OrderDetailListBean> order_detail_list;// 订单明细列表

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }

        public String getSeller_no() {
            return seller_no;
        }

        public void setSeller_no(String seller_no) {
            this.seller_no = seller_no;
        }

        public String getExpress_comp() {
            return express_comp;
        }

        public void setExpress_comp(String express_comp) {
            this.express_comp = express_comp;
        }

        public String getExpress_no() {
            return express_no;
        }

        public void setExpress_no(String express_no) {
            this.express_no = express_no;
        }

        public ArrayList<ExpressDetailBean> getExpress_detail_list() {
            return express_detail_list;
        }

        public void setExpress_detail_list(ArrayList<ExpressDetailBean> express_detail_list) {
            this.express_detail_list = express_detail_list;
        }



        public String getSeller_type() {
            return seller_type;
        }

        public void setSeller_type(String seller_type) {
            this.seller_type = seller_type;
        }

        public ArrayList<OrderDetailListBean> getOrder_detail_list() {
            return order_detail_list;
        }

        public void setOrder_detail_list(ArrayList<OrderDetailListBean> order_detail_list) {
            this.order_detail_list = order_detail_list;
        }

        @Override
        public String toString() {
            return "SellerBean{" +
                    "seller_name='" + seller_name + '\'' +
                    ", seller_no='" + seller_no + '\'' +
                    ", express_comp='" + express_comp + '\'' +
                    ", express_no='" + express_no + '\'' +
                    ", express_detail_list=" + express_detail_list +
                    ", seller_type='" + seller_type + '\'' +
                    ", order_detail_list=" + order_detail_list +
                    '}';
        }
    }
    // 物流详情列表
    public class ExpressDetailBean {
        private String express_time;
        private String express_route;

        public String getExpress_time() {
            return express_time;
        }

        public void setExpress_time(String express_time) {
            this.express_time = express_time;
        }

        public String getExpress_route() {
            return express_route;
        }

        public void setExpress_route(String express_route) {
            this.express_route = express_route;
        }
    }
    // 订单明细列表
    public class OrderDetailListBean {
        private String goods_img;
        private String goods_name;
        private String goods_sn;
        private String goods_price;
        private String goods_count;
        private String status;
        private String order_detail_sn;

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrder_detail_sn() {
            return order_detail_sn;
        }

        public void setOrder_detail_sn(String order_detail_sn) {
            this.order_detail_sn = order_detail_sn;
        }

        public String getGoods_count() {
            return goods_count;
        }

        public void setGoods_count(String goods_count) {
            this.goods_count = goods_count;
        }
    }


}
