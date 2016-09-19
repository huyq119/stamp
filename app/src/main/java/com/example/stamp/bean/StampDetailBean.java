package com.example.stamp.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/1.
 * 商品（邮票）详情查询
 */
public class StampDetailBean {
    public String goods_images;// 商品图片 (多个商品图片（大小图成对），用逗号分隔)
    public String auction_status;// 竞拍状态：DP未开始(待拍)，JP竞拍中，JS竞拍结束
    public String left_time; // 剩余时间 (针对竞拍商品：未开拍时为距开拍时间，已开拍时为剩余时间)
    public String goods_name; // 商品名称
    public String current_price; //商品售价
    public String goods_status; //商品状态(商品状态:0:下架，1:上架 ，2:待审核， 3:竞拍中 ，4:竞拍完成 ， 5:流拍，6:审核失败  7:已锁定)
    public String market_price; // 市场价
    public String freight; // 运费
    public String sale_count; // 销量
    public String service_fee_rate; // 服务费率
    public String service_fee; // 服务费
    public String goods_detail; // 详述 (商品详述，H5请求地址)
    public String goods_source; // 商品类型 (SC_ZY：自营商城；SC_DSF：第三方商家；YS：邮市；JP：竞拍)
    public String seller_name; // 卖家名称
    public String seller_no; // 卖家账号
    public String cart_goods_count; // 购物车商品数量
    public String is_favorite; // 是否被收藏
    public String verify_info; // 鉴定信息
    public String share_url; // 分享链接地址
    public ArrayList<StampDetail> offer_list;// 出价记录List
    public String rsp_code; // 返回码
    public String rsp_msg; // 返回信息
    public String sign; // 签名



    public String getGoods_images() {
        return goods_images;
    }

    public void setGoods_images(String goods_images) {
        this.goods_images = goods_images;
    }

    public String getAuction_status() {
        return auction_status;
    }

    public void setAuction_status(String auction_status) {
        this.auction_status = auction_status;
    }

    public String getLeft_time() {
        return left_time;
    }

    public void setLeft_time(String left_time) {
        this.left_time = left_time;
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

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getSale_count() {
        return sale_count;
    }

    public void setSale_count(String sale_count) {
        this.sale_count = sale_count;
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

    public String getGoods_detail() {
        return goods_detail;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail;
    }

    public String getGoods_source() {
        return goods_source;
    }

    public void setGoods_source(String goods_source) {
        this.goods_source = goods_source;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_no() {
        return seller_no;
    }

    public void setSeller_no(String seller_no) {
        this.seller_no = seller_no;
    }

    public String getCart_goods_count() {
        return cart_goods_count;
    }

    public void setCart_goods_count(String cart_goods_count) {
        this.cart_goods_count = cart_goods_count;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getVerify_info() {
        return verify_info;
    }

    public void setVerify_info(String verify_info) {
        this.verify_info = verify_info;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public ArrayList<StampDetail> getOffer_list() {
        return offer_list;
    }

    public void setOffer_list(ArrayList<StampDetail> offer_list) {
        this.offer_list = offer_list;
    }

    public static class StampDetail {
        public String offer_time;// 出价时间
        public String mobile; // 出价人手机号
        public String price;// 出价价格

        public StampDetail(String offer_time, String mobile, String price) {
            this.offer_time = offer_time;
            this.mobile = mobile;
            this.price = price;

        }

        public String getOffer_time() {
            return offer_time;
        }

        public void setOffer_time(String offer_time) {
            this.offer_time = offer_time;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

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
}
