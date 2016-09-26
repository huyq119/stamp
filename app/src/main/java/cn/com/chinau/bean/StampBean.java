package cn.com.chinau.bean;

/**
 * 邮市的类
 * Created by Administrator on 2016/8/8.
 */
public class StampBean {

    public String Title;
    public String IconUrl;
    public String Time;
    public String Status;
    public String price;

    public StampBean(String title, String status, String price, String iconUrl) {
        Title = title;
        Status = status;
        this.price = price;
        IconUrl = iconUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getIconUrl() {
        return IconUrl;
    }

    public void setIconUrl(String iconUrl) {
        IconUrl = iconUrl;
    }
}
