package com.example.stamp.bean;

import java.util.List;

/**
 * 省市县实体类
 * Created by Administrator on 2016/8/22.
 */
public class AddressBean {
    private List<Province> province_list;

    public List<Province> getProvince_list() {
        return province_list;
    }

    public void setProvince_list(List<Province> province_list) {
        this.province_list = province_list;
    }

    public class Province {
        private String province_code; //省份代码
        private String province_name;//省份名称
        private List<City> city_list;

        public String getProvince_code() {
            return province_code;
        }

        public void setProvince_code(String province_code) {
            this.province_code = province_code;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public List<City> getCity_list() {
            return city_list;
        }

        public void setCity_list(List<City> city_list) {
            this.city_list = city_list;
        }
    }

    public class City {
        private String city_code; //省份代码
        private String city_name;//省份名称
        private List<Area> area_list;

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public List<Area> getArea_list() {
            return area_list;
        }

        public void setArea_list(List<Area> area_list) {
            this.area_list = area_list;
        }
    }

    public static class Area {
        private String area_code;
        private String area_name;

        public Area( String area_name,String area_code) {
            this.area_code = area_code;
            this.area_name = area_name;
        }

        public String getArea_code() {
            return area_code;
        }

        public void setArea_code(String area_code) {
            this.area_code = area_code;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }
    }
}
