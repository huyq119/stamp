package cn.com.chinau.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class OrderSweepBean {
    public String timestamp;
    public String rsp_code;
    public String rsp_msg;
    public String sign;
    public List<Orderbean> order_list;
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRsp_code() {
        return rsp_code;
    }

    public void setRsp_code(String rsp_code) {
        this.rsp_code = rsp_code;
    }

    public String getRsp_msg() {
        return rsp_msg;
    }

    public void setRsp_msg(String rsp_msg) {
        this.rsp_msg = rsp_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<Orderbean> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(List<Orderbean> order_list) {
        this.order_list = order_list;
    }

  public static class  Orderbean {
        public String goods_name;
        public String current_price;
        public String buyback_price;
        public String income;
        public String create_time;
        public String order_status;
        public String goods_image;
        public String order_sn;
       public Orderbean(String goods_name,  String buyback_price, String income, String create_time, String order_status, String goods_image) {
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
