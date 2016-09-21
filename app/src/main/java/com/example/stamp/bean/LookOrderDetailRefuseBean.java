package com.example.stamp.bean;

/**
 * Created by Administrator on 2016/9/22.
 * （查看订单详情）退货/退款
 */
public class LookOrderDetailRefuseBean extends BaseBean {

    public String time;// 时间
    public String type;// 类型
    public String state;// 状态

    public LookOrderDetailRefuseBean(String time,String type, String state){
        this.time = time;
        this.type = type;
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "LookOrderDetailRefuseBean{" +
                "time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
