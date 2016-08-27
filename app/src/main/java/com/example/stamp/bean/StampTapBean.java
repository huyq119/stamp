package com.example.stamp.bean;

import java.util.ArrayList;

/**
 * 邮寄目录的bean对象
 * Created by Administrator on 2016/7/28.
 */
public class StampTapBean extends BaseBean {
//    public String Money;//价值
//    public String Title;//名称
//    public String Icon;//图片url
//
//    public StampTapBean(String money, String title, String icon) {
//        Money = money;
//        Title = title;
//        Icon = icon;
//    }
//
//    public String getMoney() {
//        return Money;
//    }
//
//    public void setMoney(String money) {
//        Money = money;
//    }
//
//    public String getTitle() {
//        return Title;
//    }
//
//    public void setTitle(String title) {
//        Title = title;
//    }
//
//    public String getIcon() {
//        return Icon;
//    }
//
//    public void setIcon(String icon) {
//        Icon = icon;
//    }

    public ArrayList<StampList> stamp_list;

    public ArrayList<StampList> getStamp_list() {
        return stamp_list;
    }

    public void setStamp_list(ArrayList<StampList> stamp_list) {
        this.stamp_list = stamp_list;
    }

    public class StampList {
        public String stamp_sn;
        public String stamp_img;
        public String stamp_name;
        public String current_price;

        public String getStamp_sn() {
            return stamp_sn;
        }

        public void setStamp_sn(String stamp_sn) {
            this.stamp_sn = stamp_sn;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getStamp_name() {
            return stamp_name;
        }

        public void setStamp_name(String stamp_name) {
            this.stamp_name = stamp_name;
        }

        public String getStamp_img() {
            return stamp_img;
        }

        public void setStamp_img(String stamp_img) {
            this.stamp_img = stamp_img;
        }
    }

    @Override
    public String toString() {
        return "StampTapBean{" +
                "stamp_list=" + stamp_list +
                '}';
    }
}
