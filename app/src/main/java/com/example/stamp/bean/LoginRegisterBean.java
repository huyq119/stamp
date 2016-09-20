package com.example.stamp.bean;

/**
 * Created by Administrator on 2016/9/19.
 * 登录、注册
 */
public class LoginRegisterBean extends BaseBean{
    public String userId;// 用户id
    public String token;// 用户登录之后的标识

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
