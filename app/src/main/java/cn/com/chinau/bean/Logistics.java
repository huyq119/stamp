package cn.com.chinau.bean;

/**
 * Created by Administrator on 2016/9/28.
 *物流公司的类
 */
public class Logistics {


    public String abb;// 简称
    public String name;// 名称

    public Logistics(String abb, String name) {
        super();
        this.abb = abb;
        this.name = name;
    }

    public String getAbb() {
        return abb;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
