package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * 邮票目录详情页
 * Created by Administrator on 2016/8/11
 */
public class StampTapDetailBean extends BaseBean {

    public ArrayList<StampTapDetail> stamp_info_list;

    public ArrayList<StampTapDetail> getStamp_info_list() {
        return stamp_info_list;
    }
    public void setStamp_info_list(ArrayList<StampTapDetail> stamp_info_list) {
        this.stamp_info_list = stamp_info_list;
    }

    public class StampTapDetail {
        public String stamp_image;
        public String name;
        public String stamp_sn;
        public String stamp_detail;
        public String current_price;
        public String price_history_list;
        public String stamp_story;

        public String getStamp_image() {
            return stamp_image;
        }

        public void setStamp_image(String stamp_image) {
            this.stamp_image = stamp_image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStamp_sn() {
            return stamp_sn;
        }

        public void setStamp_sn(String stamp_sn) {
            this.stamp_sn = stamp_sn;
        }

        public String getStamp_detail() {
            return stamp_detail;
        }

        public void setStamp_detail(String stamp_detail) {
            this.stamp_detail = stamp_detail;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getPrice_history_list() {
            return price_history_list;
        }

        public void setPrice_history_list(String price_history_list) {
            this.price_history_list = price_history_list;
        }

        public String getStamp_story() {
            return stamp_story;
        }

        public void setStamp_story(String stamp_story) {
            this.stamp_story = stamp_story;
        }


    }


}
