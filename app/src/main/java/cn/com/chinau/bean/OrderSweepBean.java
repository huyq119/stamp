package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/30.
 * 回购订单bean
 */
public class OrderSweepBean extends BaseBean{
    public ArrayList<Orderbean> order_list; //订单列表

    public ArrayList<Orderbean> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<Orderbean> order_list) {
        this.order_list = order_list;
    }

    public static class Orderbean {
        public String goods_image;
        public String goods_name;
        public String current_price; // 当前价
        public String buyback_price;//回购价
        public String income; //预计收益
        public String create_time; //订单创建时间
        //订单的状态，buyback_type传PT时：0-待审核； 1-审核失败；2-审核成功；buyback_type 传SM时：INIT-待寄送, CLOSE-订单关闭, AUDITING-审核中, FINISH-已完成, REFUSE-驳回
        public String order_status; //订单状态
        public String order_sn; //交易订单号

        public Orderbean(String goods_name, String buyback_price, String income, String create_time, String order_status, String goods_image) {
            this.goods_name = goods_name;
            //this.current_price = current_price;
            this.buyback_price = buyback_price;
            this.income = income;
            this.create_time = create_time;
            this.order_status = order_status;
            this.goods_image = goods_image;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
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

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        @Override
        public String toString() {
            return "Orderbean{" +
                    "goods_name='" + goods_name + '\'' +
                    ", current_price='" + current_price + '\'' +
                    ", buyback_price='" + buyback_price + '\'' +
                    ", income='" + income + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", order_status='" + order_status + '\'' +
                    ", goods_image='" + goods_image + '\'' +
                    ", order_sn='" + order_sn + '\'' +
                    '}';
        }
    }
}
