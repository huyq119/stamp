package cn.com.chinau.bean;

/**
 * 所有请求返回数据的基类
 * Created by Administrator on 2016/8/10.
 */
public class BaseBean {

    public String timestamp;
    public String rsp_code;
    public String rsp_msg;
    public String sign;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRsp_code() {
        return rsp_code;
    }

    public void setRsp_code(String rsp_code) {
        this.rsp_code = rsp_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRsp_msg() {
        return rsp_msg;
    }

    public void setRsp_msg(String rsp_msg) {
        this.rsp_msg = rsp_msg;
    }
}
