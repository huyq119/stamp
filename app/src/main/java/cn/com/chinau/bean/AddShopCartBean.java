package cn.com.chinau.bean;

/**
 * Created by Administrator on 2016/10/16.
 * 加入购物车需要的json串数据实体类
 */
public class AddShopCartBean {
    private String goods_sn;// 商品编号
    private String goods_count; // 商品数量



    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(String goods_count) {
        this.goods_count = goods_count;
    }
}
