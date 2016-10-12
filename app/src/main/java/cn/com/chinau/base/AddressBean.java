package cn.com.chinau.base;


import java.util.ArrayList;

import cn.com.chinau.bean.BaseBean;

/**
 * 管理收货地址的Bean
 * Created by Administrator on 2016/8/19.
 */
public class AddressBean extends BaseBean {

    public ArrayList<Address> address_list;

    public AddressBean(ArrayList<Address> address_list) {
        this.address_list = address_list;
    }

    public ArrayList<Address> getAddress_list() {
        return address_list;
    }

    public void setAddress_list(ArrayList<Address> address_list) {
        this.address_list = address_list;
    }

    public static class Address {
        public String name;//名称
        public String mobile;//手机号
        public String address_id;//地址ID
        public String detail;//详细地址
        public String is_default;//是否默认: 0:非默认；1默认
        public String prov;//省code值
        public String city;//市code值
        public String area;//区code值

        public Address(String mobile, String name, String is_default, String detail) {
            this.mobile = mobile;
            this.name = name;
            this.is_default = is_default;
            this.detail = detail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProv() {
            return prov;
        }

        public void setProv(String prov) {
            this.prov = prov;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }
}
