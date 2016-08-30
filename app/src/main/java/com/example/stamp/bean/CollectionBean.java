package com.example.stamp.bean;

import java.util.ArrayList;

/**
 * 收藏页面的类
 * Created by Administrator on 2016/8/21.
 */
public class CollectionBean extends BaseBean {

    public ArrayList<Collection> goods_list;

    public CollectionBean(ArrayList<Collection> goods_list) {
        this.goods_list = goods_list;
    }

    public static class Collection {
        public String goods_name;// 商品名称
        public String goods_sn;//商品编号
        public String goods_img;// 商品图片
        public String goods_price;//商品价格
        public String goods_type;//商品类型：SC商城，YS邮市，JP竞拍
        public String goods_status;//商品状态：WH无货；YH有货；JPZ竞拍中；WKS未开始；YJS已结束
        public  boolean isChoosed;
        public Collection(String goods_name, String goods_status, String goods_type, String goods_price, String goods_sn, String goods_img) {
            this.goods_name = goods_name;
            this.goods_status = goods_status;
            this.goods_type = goods_type;
            this.goods_price = goods_price;
            this.goods_sn = goods_sn;
            this.goods_img = goods_img;
        }
        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
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

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

        public String getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(String goods_type) {
            this.goods_type = goods_type;
        }

        public String getGoods_status() {
            return goods_status;
        }

        public void setGoods_status(String goods_status) {
            this.goods_status = goods_status;
        }
    }
}
