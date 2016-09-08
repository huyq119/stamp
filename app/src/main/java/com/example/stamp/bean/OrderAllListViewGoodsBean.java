package com.example.stamp.bean;

/**
 * Created by Administrator on 2016/9/5.
 * 商品订单全部页面ListView
 * Goods详情
 */
public class OrderAllListViewGoodsBean {

    private String stamp_img;// 邮票图片
    private String stamp_name; // 名称
    private String stamp_price; // 价钱
    private String stamp_count; // 数量
    private String stamp_status; // 状态
    private String stamp_allCount; // 共几件数量
    private String stamp_allPrice; // 总计

    public OrderAllListViewGoodsBean(String stamp_img,
                                     String stamp_name,
                                     String stamp_price,
                                     String stamp_count,
                                     String stamp_status,
                                     String stamp_allCount,
                                     String stamp_allPrice) {
        this.stamp_img = stamp_img;
        this.stamp_name = stamp_name;
        this.stamp_price = stamp_price;
        this.stamp_count = stamp_count;
        this.stamp_status = stamp_status;
        this.stamp_allCount = stamp_allCount;
        this.stamp_allPrice = stamp_allPrice;
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

    public String getStamp_price() {
        return stamp_price;
    }

    public void setStamp_price(String stamp_price) {
        this.stamp_price = stamp_price;
    }

    public String getStamp_count() {
        return stamp_count;
    }

    public void setStamp_count(String stamp_count) {
        this.stamp_count = stamp_count;
    }

    public String getStamp_status() {
        return stamp_status;
    }

    public void setStamp_status(String stamp_status) {
        this.stamp_status = stamp_status;
    }

    public String getStamp_allCount() {
        return stamp_allCount;
    }

    public void setStamp_allCount(String stamp_allCount) {
        this.stamp_allCount = stamp_allCount;
    }

    public String getStamp_allPrice() {
        return stamp_allPrice;
    }

    public void setStamp_allPrice(String stamp_allPrice) {
        this.stamp_allPrice = stamp_allPrice;
    }
}
