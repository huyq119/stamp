package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 * 扫码回购商品详情查询(不登录)
 */
public class ScanBean extends BaseBean {

    public String[] goods_images;// 商品图片
    public String goods_sn;// 商品编号
    public String goods_name;// 商品名称
    public String increase;// 涨幅
    public String current_price;// 当前价格
    public String publish_count;// 发行量
    public String goods_detail;// 商品详情
    public ArrayList<PriceHistory> price_history_list;// 历史价格
    public String buy_time;// 买入时间
    public String buyer;// 姓名
    public String service_fee_rate;// 服务费率
    public String service_fee;// 服务费
    public String buyback_price;// 回购价格
    public String income;// 预计收益
    public String order_detail_sn;// 订单明细编号


    public String[] getGoods_images() {
        return goods_images;
    }

    public void setGoods_images(String[] goods_images) {
        this.goods_images = goods_images;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getIncrease() {
        return increase;
    }

    public void setIncrease(String increase) {
        this.increase = increase;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getPublish_count() {
        return publish_count;
    }

    public void setPublish_count(String publish_count) {
        this.publish_count = publish_count;
    }

    public ArrayList<PriceHistory> getPrice_history_list() {
        return price_history_list;
    }

    public void setPrice_history_list(ArrayList<PriceHistory> price_history_list) {
        this.price_history_list = price_history_list;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getService_fee_rate() {
        return service_fee_rate;
    }

    public void setService_fee_rate(String service_fee_rate) {
        this.service_fee_rate = service_fee_rate;
    }

    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }

    public String getBuyback_price() {
        return buyback_price;
    }

    public void setBuyback_price(String buyback_price) {
        this.buyback_price = buyback_price;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOrder_detail_sn() {
        return order_detail_sn;
    }

    public void setOrder_detail_sn(String order_detail_sn) {
        this.order_detail_sn = order_detail_sn;
    }

    public class PriceHistory {
        private String time;
        private String price;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
