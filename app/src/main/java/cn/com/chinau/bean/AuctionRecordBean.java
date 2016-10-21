package cn.com.chinau.bean;

import java.util.List;

/**
 * 竞拍记录bean
 * Created by Administrator on 2016/8/21.
 */
public class AuctionRecordBean extends BaseBean{

    public List<Auction> auction_rec_list;

    public List<Auction> getAuction_rec_list() {
        return auction_rec_list;
    }

    public void setAuction_rec_list(List<Auction> auction_rec_list) {
        this.auction_rec_list = auction_rec_list;
    }

    public AuctionRecordBean(List<Auction> auction_rec_list) {
        this.auction_rec_list = auction_rec_list;
    }

    public static class Auction {
        public String auction_status;//竞拍状态
        public String order_status;//订单状态
        public String auction_sn;//竞拍记录
        public String goods_sn;// 商品编号
        public String auction_end_time;//竞拍结束时间
        public String goods_name;//商品名称
        public String goods_img;//商品图片
        public String goods_price;//商品价格
        public String create_time;//竞拍出价时间

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getAuction_sn() {
            return auction_sn;
        }

        public void setAuction_sn(String auction_sn) {
            this.auction_sn = auction_sn;
        }

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getAuction_status() {
            return auction_status;
        }

        public void setAuction_status(String auction_status) {
            this.auction_status = auction_status;
        }

        public String getAuction_end_time() {
            return auction_end_time;
        }

        public void setAuction_end_time(String auction_end_time) {
            this.auction_end_time = auction_end_time;
        }

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
