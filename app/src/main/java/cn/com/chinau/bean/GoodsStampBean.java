package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * 商品List的bean对象
 * Created by Administrator on 2016/7/28.
 */
public class GoodsStampBean extends BaseBean {

    public ArrayList<GoodsList> goods_list;

    public ArrayList<GoodsList> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(ArrayList<GoodsList> goods_list) {
        this.goods_list = goods_list;
    }

    public class GoodsList {
        public String goods_sn;// 商品编号
        public String goods_name;// 商品名称
        public String current_price;// 商品价格
        public String goods_img;// 商品图片
        public String goods_source;// 商品类型
        public String left_time;// 剩余时间
        public String auction_status;// 竞拍状态

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

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

        public String getGoods_source() {
            return goods_source;
        }

        public void setGoods_source(String goods_source) {
            this.goods_source = goods_source;
        }

        public String getLeft_time() {
            return left_time;
        }

        public void setLeft_time(String left_time) {
            this.left_time = left_time;
        }

        public String getAuction_status() {
            return auction_status;
        }

        public void setAuction_status(String auction_status) {
            this.auction_status = auction_status;
        }
    }


}
