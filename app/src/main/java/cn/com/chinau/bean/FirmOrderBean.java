package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Date: 2016/10/20 23:20
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 确认订单页面获取数据实体类
 */
public class FirmOrderBean extends BaseBean{

    private String service_fee_rate; // 服务费率
    private String service_fee;// 服务费
    private ArrayList<ExpreeComp> express_comp; // 快递公司
    private ArrayList<ExpreeFee> express_fee;// 运费
    private ArrayList<AddressList> address_list;// 地址列表

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

    public ArrayList<ExpreeFee> getExpress_fee() {
        return express_fee;
    }

    public void setExpress_fee(ArrayList<ExpreeFee> express_fee) {
        this.express_fee = express_fee;
    }

    public ArrayList<ExpreeComp> getExpress_comp() {
        return express_comp;
    }

    public void setExpress_comp(ArrayList<ExpreeComp> express_comp) {
        this.express_comp = express_comp;
    }

    public ArrayList<AddressList> getAddress_list() {
        return address_list;
    }

    public void setAddress_list(ArrayList<AddressList> address_list) {

        this.address_list = address_list;
    }

    // 快递公司
    public class ExpreeComp{
        private String name;// 公司代号
        private String value;// 公司名称

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    // 运费
    public class ExpreeFee{
        private String name;// 公司代号
        private String value;// 价格

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    // 地址列表
    public class AddressList {
        private String name;// 名字
        private String mobile;// 电话
        private String address_id;// 地址Id
        private String detail; // 详情地址
        private String is_default; // 是否默认地址

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }
    }


}
