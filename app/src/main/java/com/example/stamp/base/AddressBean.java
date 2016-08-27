package com.example.stamp.base;


import com.example.stamp.bean.BaseBean;

import java.util.List;

/**
 * 地址的javaBean
 * Created by Administrator on 2016/8/19.
 */
public class AddressBean extends BaseBean {

    public List<Address> address_list;

    public AddressBean(List<Address> address_list) {
        this.address_list = address_list;
    }

    public List<Address> getAddress_list() {
        return address_list;
    }

    public void setAddress_list(List<Address> address_list) {
        this.address_list = address_list;
    }

    public static class Address {
        public String name;//名称
        public String mobile;//手机号
        public String address_id;//地址ID
        public String detail;//详细地址
        public String is_default;//是否默认: 0:非默认；1默认

        public Address(String mobile, String name, String address_id, String is_default, String detail) {
            this.mobile = mobile;
            this.name = name;
            this.address_id = address_id;
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
    }
}
