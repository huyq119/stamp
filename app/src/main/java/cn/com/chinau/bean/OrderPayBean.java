package cn.com.chinau.bean;

/**
 * Date: 2016/10/23 00:52
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 订单支付请求解析实体类
 */

public class OrderPayBean extends BaseBean{
    private String request_id;// 交易订单号
    private String pay_request_id;// 平台支付请求号
    private String external_id;// 平台订单流水号
    private String order_amount;// 订单金额
    private String pay_amount;// 支付金额
    private String discount;// 优惠金额
    private String pay_type;// 支付方式
    private String pay_url;// 支付请求地址

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getPay_request_id() {
        return pay_request_id;
    }

    public void setPay_request_id(String pay_request_id) {
        this.pay_request_id = pay_request_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_url() {
        return pay_url;
    }

    public void setPay_url(String pay_url) {
        this.pay_url = pay_url;
    }

    @Override
    public String toString() {
        return "OrderPayBean{" +
                "request_id='" + request_id + '\'' +
                ", pay_request_id='" + pay_request_id + '\'' +
                ", external_id='" + external_id + '\'' +
                ", order_amount='" + order_amount + '\'' +
                ", pay_amount='" + pay_amount + '\'' +
                ", discount='" + discount + '\'' +
                ", pay_type='" + pay_type + '\'' +
                ", pay_url='" + pay_url + '\'' +
                '}';
    }
}
