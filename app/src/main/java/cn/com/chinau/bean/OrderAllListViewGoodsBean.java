package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 * 商品订单列表查询ListView
 * Goods详情
 */
public class OrderAllListViewGoodsBean extends BaseBean {

    public ArrayList<Order_list> order_list;

    public OrderAllListViewGoodsBean(ArrayList<Order_list> order_list) {
        this.order_list = order_list;
    }

    public ArrayList<Order_list> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<Order_list> order_list) {
        this.order_list = order_list;
    }

    @Override
    public String toString() {
        return "OrderAllListViewGoodsBean{" +
                "order_list=" + order_list +
                '}';
    }

    // 订单列表
    public static class Order_list {
        private String order_sn; //交易订单号
        private ArrayList<Seller_list> seller_list; //卖家列表
        private String pay_time;//支付时间
        private String process_status; //处理状态
        private String create_time; //订单创建时间
        private String order_status; //订单状态

        public Order_list(String order_sn, String pay_time, ArrayList<Seller_list> seller_list, String create_time, String process_status, String order_status) {
            this.order_sn = order_sn;
            this.pay_time = pay_time;
            this.seller_list = seller_list;
            this.create_time = create_time;
            this.process_status = process_status;
            this.order_status = order_status;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getOrder_sn() {
            return this.order_sn;
        }

        public void setSeller_list(ArrayList<Seller_list> seller_list) {
            this.seller_list = seller_list;
        }

        public ArrayList<Seller_list> getSeller_list() {
            return this.seller_list;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getPay_time() {
            return this.pay_time;
        }

        public void setProcess_status(String process_status) {
            this.process_status = process_status;
        }

        public String getProcess_status() {
            return this.process_status;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCreate_time() {
            return this.create_time;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getOrder_status() {
            return this.order_status;
        }

        @Override
        public String toString() {
            return "Order_list{" +
                    "order_sn='" + order_sn + '\'' +
                    ", seller_list=" + seller_list +
                    ", pay_time='" + pay_time + '\'' +
                    ", process_status='" + process_status + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", order_status='" + order_status + '\'' +
                    '}';
        }
    }

    //卖家列表
    public static class Seller_list {
        public String seller_name;// 卖家名称
        public String seller_no; // 卖家编号
        public String seller_type; // 卖家类型
        public ArrayList<Order_detail_list> order_detail_list; //订单明细列表

        public Seller_list(String seller_name, String seller_no, String seller_type, ArrayList<Order_detail_list> order_detail_list) {
            this.seller_name = seller_name;
            this.seller_no = seller_no;
            this.seller_type = seller_type;
            this.order_detail_list = order_detail_list;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }

        public String getSeller_name() {
            return this.seller_name;
        }

        public void setSeller_no(String seller_no) {
            this.seller_no = seller_no;
        }

        public String getSeller_no() {
            return this.seller_no;
        }

        public void setSeller_type(String seller_type) {
            this.seller_type = seller_type;
        }

        public String getSeller_type() {
            return this.seller_type;
        }

        public void setOrder_detail_list(ArrayList<Order_detail_list> order_detail_list) {
            this.order_detail_list = order_detail_list;
        }

        public ArrayList<Order_detail_list> getOrder_detail_list() {
            return this.order_detail_list;
        }

        @Override
        public String toString() {
            return "Seller_list{" +
                    "seller_name='" + seller_name + '\'' +
                    ", seller_no='" + seller_no + '\'' +
                    ", seller_type='" + seller_type + '\'' +
                    ", order_detail_list=" + order_detail_list +
                    '}';
        }
    }

    //订单明细列表
    public static class Order_detail_list {
        private String goods_img; //商品图片
        private String goods_name; //商品名称
        private String goods_sn; //商品编号
        private String goods_price; //商品价格
        private String goods_count; //商品数量
        private String status; //订单明细状态
        private String order_detail_sn;//订单明细编号

        public Order_detail_list(String goods_img, String goods_name, String goods_sn, String goods_price, String goods_count, String status, String order_detail_sn) {
            this.goods_img = goods_img;
            this.goods_name = goods_name;
            this.goods_sn = goods_sn;
            this.goods_price = goods_price;
            this.goods_count = goods_count;
            this.status = status;
            this.order_detail_sn = order_detail_sn;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

        public String getGoods_img() {
            return this.goods_img;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_name() {
            return this.goods_name;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getGoods_sn() {
            return this.goods_sn;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_price() {
            return this.goods_price;
        }

        public void setGoods_count(String goods_count) {
            this.goods_count = goods_count;
        }

        public String getGoods_count() {
            return this.goods_count;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setOrder_detail_sn(String order_detail_sn) {
            this.order_detail_sn = order_detail_sn;
        }

        public String getOrder_detail_sn() {
            return this.order_detail_sn;
        }

        
        @Override
        public String toString() {
            return "Order_detail_list{" +
                    "goods_img='" + goods_img + '\'' +
                    ", goods_name='" + goods_name + '\'' +
                    ", goods_sn='" + goods_sn + '\'' +
                    ", goods_price='" + goods_price + '\'' +
                    ", goods_count='" + goods_count + '\'' +
                    ", status='" + status + '\'' +
                    ", order_detail_sn='" + order_detail_sn + '\'' +
                    '}';
        }
    }
}
