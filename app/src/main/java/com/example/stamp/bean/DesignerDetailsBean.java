package com.example.stamp.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 * 设计家详情bean对象
 */
public class DesignerDetailsBean extends BaseBean{

    /**
     * 设计故事list
     */
    public ArrayList<DesignerStory> designer_list;

    public ArrayList<DesignerStory> getDesigner_list() {
        return designer_list;
    }

    public void setDesigner_list(ArrayList<DesignerStory> designer_list) {
        this.designer_list = designer_list;
    }

    public static class DesignerStory{
        public String story_sn;// 编号
        public String story_img; // 图片
        public String name; // 作品名称
        public String chinese_name; // 作者名字


        public DesignerStory(String mStory_img, String mName, String mChinese_name) {
            story_img = mStory_img;
            name = mName;
            chinese_name = mChinese_name;
        }


        public String getStory_sn() {
            return story_sn;
        }

        public void setStory_sn(String story_sn) {
            this.story_sn = story_sn;
        }

        public String getStory_img() {
            return story_img;
        }

        public void setStory_img(String story_img) {
            this.story_img = story_img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getChinese_name() {
            return chinese_name;
        }

        public void setChinese_name(String chinese_name) {
            this.chinese_name = chinese_name;
        }
    }

    /**
     * 艺术作品list
     */
    public ArrayList<DesignerWorks> works_list;

    public ArrayList<DesignerWorks> getWorks_list() {
        return works_list;
    }

    public void setWorks_list(ArrayList<DesignerWorks> works_list) {
        this.works_list = works_list;
    }

    public static class DesignerWorks{
        public String works_img;// 作品图片
        public String works_name;// 作品名称
        public String chinese_name;// 中文名
        public String serial_no;// 志编号
        public String works_sn;// 作品编号
        public String category;// 类别
        public String publish_date;// 发行日期
        public String suit_count; // 全套枚数
        public String current_price;// 当前价格

        public DesignerWorks(String works_img,
                             String works_name,
                             String serial_no,
                             String category,
                             String publish_date,
                             String suit_count,
                             String current_price
                             ) {
            this.works_img = works_img;
            this.works_name = works_name;
            this.serial_no = serial_no;
            this.category = category;
            this.publish_date = publish_date;
            this.suit_count = suit_count;
            this.current_price = current_price;



        }

        public String getWorks_img() {
            return works_img;
        }

        public void setWorks_img(String works_img) {
            this.works_img = works_img;
        }

        public String getChinese_name() {
            return chinese_name;
        }

        public void setChinese_name(String chinese_name) {
            this.chinese_name = chinese_name;
        }

        public String getSerial_no() {
            return serial_no;
        }

        public void setSerial_no(String serial_no) {
            this.serial_no = serial_no;
        }

        public String getWorks_sn() {
            return works_sn;
        }

        public void setWorks_sn(String works_sn) {
            this.works_sn = works_sn;
        }

        public String getWorks_name() {
            return works_name;
        }

        public void setWorks_name(String works_name) {
            this.works_name = works_name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSuit_count() {
            return suit_count;
        }

        public void setSuit_count(String suit_count) {
            this.suit_count = suit_count;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }
    }

    /**
     * 名家访谈List
     */
    public ArrayList<DesignerView> view_list;

    public ArrayList<DesignerView> getView_list() {
        return view_list;
    }

    public void setView_list(ArrayList<DesignerView> view_list) {
        this.view_list = view_list;
    }

    public static class DesignerView{
        public String view_image;// 访谈图片
        public String view_title;// 访谈标题
        public String view_sn;// 访谈编号
        public String publish_date;// 发布日期

        public DesignerView(String view_image,
                            String view_title,
                            String publish_date) {

            this.view_image = view_image;
            this.view_title = view_title;
            this.publish_date = publish_date;

        }

        public String getView_image() {
            return view_image;
        }

        public void setView_image(String view_image) {
            this.view_image = view_image;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getView_sn() {
            return view_sn;
        }

        public void setView_sn(String view_sn) {
            this.view_sn = view_sn;
        }

        public String getView_title() {
            return view_title;
        }

        public void setView_title(String view_title) {
            this.view_title = view_title;
        }
    }
}
