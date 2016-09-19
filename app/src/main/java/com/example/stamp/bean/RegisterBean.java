package com.example.stamp.bean;

/**
 * Created by Administrator on 2016/9/19.
 * 注册
 */
public class RegisterBean extends BaseBean{
    public String user_id;// 用户id
    public String token;// 用户登录之后的标识

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
