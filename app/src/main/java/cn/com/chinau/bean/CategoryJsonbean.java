package cn.com.chinau.bean;

/**
 * Created by Administrator on 2016/10/14.
 * 邮票目录筛上传Json选实体类
 */
public class CategoryJsonBean {

    private String year; // 年代
    private String category; // 类别
    private String theme; // 题材

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
