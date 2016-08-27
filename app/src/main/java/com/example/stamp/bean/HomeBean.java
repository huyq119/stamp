package com.example.stamp.bean;

import java.util.List;

/**
 * 首页信息的实体类
 * Created by Angle on 2016/8/25.
 */
public class HomeBean extends BaseBean {

    public String banners;//轮播图
    public List<Group> group_list;
    public List<Good> goods_list;

    public String getBanners() {
        return banners;
    }

    public void setBanners(String banners) {
        this.banners = banners;
    }

    public List<Group> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(List<Group> group_list) {
        this.group_list = group_list;
    }

    public List<Good> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<Good> goods_list) {
        this.goods_list = goods_list;
    }

    public class Group {
        public String group_name;
        public List<Child> child_list;

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public List<Child> getChild_list() {
            return child_list;
        }

        public void setChild_list(List<Child> child_list) {
            this.child_list = child_list;
        }
    }

    public class Good {
        public String goods_sn;//商品编号
        public String goods_img;//商品图片
        public String goods_name;//商品名称
        public String current_price;//当前价格

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
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

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }
    }

    public class Child {
        public String h5_url;//h5地址
        public String img_url;//图片\

        public String getH5_url() {
            return h5_url;
        }

        public void setH5_url(String h5_url) {
            this.h5_url = h5_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
