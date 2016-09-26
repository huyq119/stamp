package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * 邮寄目录的bean对象
 * Created by Administrator on 2016/7/28.
 */
public class StampTapBean extends BaseBean {

    public ArrayList<StampList> stamp_list;

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
        public String current_price;// 当前价格

        public StampList( String stamp_name, String current_price, String stamp_img) {
            this.stamp_img = stamp_img;
            this.stamp_name = stamp_name;
            this.current_price = current_price;
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
    }

    @Override
    public String toString() {
        return "StampTapBean{" +
                "stamp_list=" + stamp_list +
                '}';
    }
}
