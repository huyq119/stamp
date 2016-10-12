package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/12.
 * 首页数据搜索list实体类
 */
public class SearchFristListBean extends BaseBean {

    public ArrayList<ResultList> result_list;

    public ArrayList<ResultList> getResult_list() {
        return result_list;
    }

    public void setResult_list(ArrayList<ResultList> result_list) {
        this.result_list = result_list;
    }

    public class ResultList {
        public String sn;// 商品编号
        public String type;// 商品类型
        public String image;// 商品图片
        public String price;// 商品价格
        public String priceVal;//
        public String name;// 商品名称

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPriceVal() {
            return priceVal;
        }

        public void setPriceVal(String priceVal) {
            this.priceVal = priceVal;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
