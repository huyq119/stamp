package com.example.stamp.bean;

import java.util.ArrayList;

/**
 * 我的邮集bean对象
 * Created by Administrator on 2016/7/28.
 */
public class MyStampGridViewBean extends BaseBean {

    public String share_url;// 分享url
    public String total_amount;// 总价值
    public ArrayList<StampList> stamp_list;// 我的邮集列表

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public ArrayList<StampList> getStamp_list() {
        return stamp_list;
    }

    public void setStamp_list(ArrayList<StampList> stamp_list) {
        this.stamp_list = stamp_list;
    }

    public static class StampList {
        public String stamp_sn;// 邮票编号
        public String stamp_img;// 邮票图片
        public String stamp_name;// 邮票名称
        public String current_price;// 邮票价格
        public String stamp_count;// 邮票数量

        public StampList(String stamp_name,String stamp_count,String stamp_img) {
            super();

            this.stamp_sn = stamp_name;
            this.stamp_count = stamp_count;
            this.stamp_img = stamp_img;
        }

        public String getStamp_sn() {
            return stamp_sn;
        }

        public void setStamp_sn(String stamp_sn) {
            this.stamp_sn = stamp_sn;
        }

        public String getStamp_img() {
            return stamp_img;
        }

        public void setStamp_img(String stamp_img) {
            this.stamp_img = stamp_img;
        }

        public String getStamp_name() {
            return stamp_name;
        }

        public void setStamp_name(String stamp_name) {
            this.stamp_name = stamp_name;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getStamp_count() {
            return stamp_count;
        }

        public void setStamp_count(String stamp_count) {
            this.stamp_count = stamp_count;
        }
    }

    @Override
    public String toString() {
        return "StampTapBean{" +
                "stamp_list=" + stamp_list +
                '}';
    }
}
