package com.example.stamp.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 * 商品订单列表查询ListView
 * Goods详情
 */
public class OrderAllListViewGoodsBean extends BaseBean{

    private ArrayList<OrdersDataList> order_list;

    public ArrayList<OrdersDataList> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<OrdersDataList> order_list) {
        this.order_list = order_list;
    }

    public static class OrdersDataList {
        public String order_sn;// 订单编号
        public ArrayList<SellerDataList> seller_list;// 卖家列表
        public String pay_time;// 支付时间
        public String process_status;// 处理状态
        public String create_time;// 订单创建时间
        public String order_status;// 订单状态

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public ArrayList<SellerDataList> getSeller_list() {
            return seller_list;
        }

        public void setSeller_list(ArrayList<SellerDataList> seller_list) {
            this.seller_list = seller_list;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getProcess_status() {
            return process_status;
        }

        public void setProcess_status(String process_status) {
            this.process_status = process_status;
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


        public static class SellerDataList{
            public String seller_name;// 卖家名称
            public String seller_no; //  卖家账号
            public String seller_type; // 卖家类型
            public ArrayList<OrderDetailList> order_detail_list; // 订单明细列表

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

            public String getSeller_type() {
                return seller_type;
            }

            public void setSeller_type(String seller_type) {
                this.seller_type = seller_type;
            }

            public ArrayList<OrderDetailList> getOrder_detail_list() {
                return order_detail_list;
            }

            public void setOrder_detail_list(ArrayList<OrderDetailList> order_detail_list) {
                this.order_detail_list = order_detail_list;
            }

            public static class OrderDetailList{
                public String goods_img;// 商品图片
                public String goods_name; // 商品名称
                public String goods_sn; // 商品编号
                public String goods_price; // 商品价格
                public String goods_count; // 商品数量
                public String status; // 订单明细状态
                public String order_detail_sn; // 订单明细编号

                public OrderDetailList(String goods_img,
                                       String goods_name,
                                       String goods_price,
                                       String goods_count,
                                       String status ) {
                    this.goods_img = goods_img;
                    this.goods_name = goods_name;
                    this.goods_price = goods_price;
                    this.goods_count = goods_count;
                    this.status = status;
                }

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

                public String getGoods_count() {
                    return goods_count;
                }

                public void setGoods_count(String goods_count) {
                    this.goods_count = goods_count;
                }

                public String getOrder_detail_sn() {
                    return order_detail_sn;
                }

                public void setOrder_detail_sn(String order_detail_sn) {
                    this.order_detail_sn = order_detail_sn;
                }

            }
        }
    }
}
