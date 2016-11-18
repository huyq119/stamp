package cn.com.chinau.bean;

import java.util.List;

/**
 * 保存购物车的实体类，这个对象应该是单例的
 * Created by Angle on 2016/11/18.
 */

public class ShopBean {

//    private static ShopBean mShopBean = null;
//
//
//    public static synchronized ShopBean getInstance(){
//        if(mShopBean==null){
//            mShopBean = new ShopBean();
//        }
//        return mShopBean;
//    }


//    private ShopBean() {
//        seller_list = new ArrayList<>();
//    }

    private List<SellerListBean> seller_list;

    public List<SellerListBean> getSeller_list() {
        return seller_list;
    }

    public void setSeller_list(List<SellerListBean> seller_list) {
        this.seller_list = seller_list;
    }

    public static class SellerListBean {
        /**
         * seller_name : 小时李
         * seller_type : YS
         * seller_no : 28
         * goods_list : [{"goods_img":"http://test.chinau.com.cn:8081/chinau-imgserver/attachment///goods/20160418/2016041821361347886_b.png","goods_name":"测试一口价商品","goods_sn":"111232740725","goods_price":"12.00","goods_count":"3","status":"1"}]
         */

        private String seller_name;
        private String seller_type;
        private String seller_no;
        private List<GoodsListBean> goods_list;
        public boolean isChildSelected;//子View是否显示

        public boolean isChildSelected() {
            return isChildSelected;
        }

        public void setChildSelected(boolean childSelected) {
            isChildSelected = childSelected;
        }


        public SellerListBean(String seller_name, List<GoodsListBean> goods_list, String seller_no, String seller_type, boolean isChildSelected) {
            this.seller_name = seller_name;
            this.goods_list = goods_list;
            this.seller_no = seller_no;
            this.seller_type = seller_type;
            this.isChildSelected = isChildSelected;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }

        public String getSeller_type() {
            return seller_type;
        }

        public void setSeller_type(String seller_type) {
            this.seller_type = seller_type;
        }

        public String getSeller_no() {
            return seller_no;
        }

        public void setSeller_no(String seller_no) {
            this.seller_no = seller_no;
        }

        public List<GoodsListBean> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListBean> goods_list) {
            this.goods_list = goods_list;
        }

        public static class GoodsListBean {
            public GoodsListBean(String goods_img, String goods_name, String goods_sn, String goods_price, int goods_count, String status) {
                this.goods_img = goods_img;
                this.goods_name = goods_name;
                this.goods_sn = goods_sn;
                this.goods_price = goods_price;
                this.goods_count = goods_count;
                this.status = status;
            }

            /**
             * goods_img : http://test.chinau.com.cn:8081/chinau-imgserver/attachment///goods/20160418/2016041821361347886_b.png
             * goods_name : 测试一口价商品
             * goods_sn : 111232740725
             * goods_price : 12.00
             * goods_count : 3
             * status : 1
             */


            private String goods_img;
            private String goods_name;
            private String goods_sn;
            private String goods_price;
            private int goods_count;
            private String status;

            public String getGoods_img() {
                return goods_img;
            }

            public void setGoods_img(String goods_img) {
                this.goods_img = goods_img;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_sn() {
                return goods_sn;
            }

            public void setGoods_sn(String goods_sn) {
                this.goods_sn = goods_sn;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public int getGoods_count() {
                return goods_count;
            }

            public void setGoods_count(int goods_count) {
                this.goods_count = goods_count;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            @Override
            public String toString() {
                return "GoodsListBean{" +
                        "goods_img='" + goods_img + '\'' +
                        ", goods_name='" + goods_name + '\'' +
                        ", goods_sn='" + goods_sn + '\'' +
                        ", goods_price='" + goods_price + '\'' +
                        ", goods_count=" + goods_count +
                        ", status='" + status + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "SellerListBean{" +
                    "seller_name='" + seller_name + '\'' +
                    ", seller_type='" + seller_type + '\'' +
                    ", seller_no='" + seller_no + '\'' +
                    ", goods_list=" + goods_list +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ShopBean{" +
                "seller_list=" + seller_list +
                '}';
    }
}
