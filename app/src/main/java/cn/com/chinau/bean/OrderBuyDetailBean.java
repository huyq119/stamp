package cn.com.chinau.bean;

/**
 * Created by Administrator on 2016/10/16.
 *  回购订单详情Bean
 */
public class OrderBuyDetailBean extends BaseBean{
    private String order_sn;// 交易订单号
    private String goods_name; // 商品名称
    private String current_price; // 当前价格
    private String buyback_price; // 回购价格
    private String income; // 预计收益
    private String service_fee_rate; // 服务费率
    private String service_fee; // 服务费
    private String buyer; // 购买人
    private String mobile; // 手机号
    private String buy_time; // 购买时间
    private String create_time; // 创建时间
    private String order_status; // 订单状态
//    private String desc; // 描述
    private String buyback_desc; // 描述

    private String express_comp; // 快递公司
    private String express_no; // 快递单号
    private String back_express_comp; // 回寄快递公司
    private String back_express_no; // 回寄快递单号

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

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getBuyback_price() {
        return buyback_price;
    }

    public void setBuyback_price(String buyback_price) {
        this.buyback_price = buyback_price;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExpress_comp() {
        return express_comp;
    }

    public void setExpress_comp(String express_comp) {
        this.express_comp = express_comp;
    }

    public String getBack_express_no() {
        return back_express_no;
    }

    public void setBack_express_no(String back_express_no) {
        this.back_express_no = back_express_no;
    }

    public String getBack_express_comp() {
        return back_express_comp;
    }

    public void setBack_express_comp(String back_express_comp) {
        this.back_express_comp = back_express_comp;
    }

    public String getExpress_no() {
        return express_no;
    }

    public void setExpress_no(String express_no) {
        this.express_no = express_no;
    }

//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }


    public String getBuyback_desc() {
        return buyback_desc;
    }

    public void setBuyback_desc(String buyback_desc) {
        this.buyback_desc = buyback_desc;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
}
