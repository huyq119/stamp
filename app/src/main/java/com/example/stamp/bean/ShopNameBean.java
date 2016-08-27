package com.example.stamp.bean;

import java.util.ArrayList;

/**
 * 商城名称,对应的字段是Seller_list
 * Created by Administrator on 2016/8/15.
 */
public class ShopNameBean extends BaseBean {

    public ArrayList<SellerBean> seller_list;//卖家列表
    public String goods_total_amount;//商品总金额

    public ShopNameBean(ArrayList<SellerBean> seller_list, String goods_total_amount) {
        this.seller_list = seller_list;
        this.goods_total_amount = goods_total_amount;
    }

    public ArrayList<SellerBean> getSeller_list() {
        return seller_list;
    }

    public void setSeller_list(ArrayList<SellerBean> seller_list) {
        this.seller_list = seller_list;
    }

    public String getGoods_total_amount() {
        return goods_total_amount;
    }

    public void setGoods_total_amount(String goods_total_amount) {
        this.goods_total_amount = goods_total_amount;
    }

    /**
     * 卖家列表数据
     */
    public static class SellerBean {


        public String seller_name;//卖家名称
        public String seller_type;//卖家类型
        public String seller_no;//卖家账号
        public ArrayList<GoodsBean> goods_list;//商品列表


        public SellerBean(String seller_name, String seller_type, String seller_no, ArrayList<GoodsBean> goods_list) {
            this.seller_name = seller_name;
            this.seller_type = seller_type;
            this.seller_no = seller_no;
            this.goods_list = goods_list;
        }

        public ArrayList<GoodsBean> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(ArrayList<GoodsBean> goods_list) {
            this.goods_list = goods_list;
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

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }
    }

    /**
     * 商品信息对象
     */
    public static class GoodsBean {
        public String goods_img;//商品图片
        public String goods_name;//商品名称
        public String goods_sn;//商品编号
        public String goods_price;//商品单价
        public String goods_count;//商品数量


        public GoodsBean(String goods_img, String goods_name, String goods_sn, String goods_price, String goods_count) {
            this.goods_img = goods_img;
            this.goods_name = goods_name;
            this.goods_sn = goods_sn;
            this.goods_price = goods_price;
            this.goods_count = goods_count;
        }

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_count() {
            return goods_count;
        }

        public void setGoods_count(String goods_count) {
            this.goods_count = goods_count;
        }
    }
}
