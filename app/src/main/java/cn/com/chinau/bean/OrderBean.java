package cn.com.chinau.bean;

/**
 * Created by Administrator on 2016/8/31.
 */
public class OrderBean {
    public String create_time;
    public String order_status;
    public String goods_name;

    public OrderBean(String create_time, String order_status, String goods_name) {
        this.create_time = create_time;
        this.order_status = order_status;
        this.goods_name = goods_name;
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

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "create_time='" + create_time + '\'' +
                ", order_status='" + order_status + '\'' +
                ", goods_name='" + goods_name + '\'' +
                '}';
    }
}
