package com.example.stamp.bean;

import java.util.ArrayList;

/**
 * 设计家List
 * Created by Administrator on 2016/8/9.
 */
public class DesignerBean extends BaseBean {

    public ArrayList<Designer> designer_list;

    public ArrayList<Designer> getDesigner_list() {
        return designer_list;
    }

    public void setDesigner_list(ArrayList<Designer> designer_list) {
        this.designer_list = designer_list;
    }

    public class Designer {
        public String designer_sn;//设计家编号
        public String chinese_name;//中文名称
        public String english_name;// 英文名称
        public String resume;//简历
        public String header_img;//头像


        public String getDesigner_sn() {
            return designer_sn;
        }

        public void setDesigner_sn(String designer_sn) {
            this.designer_sn = designer_sn;
        }

        public String getResume() {
            return resume;
        }

        public void setResume(String resume) {
            this.resume = resume;
        }

        public String getEnglish_name() {
            return english_name;
        }

        public void setEnglish_name(String english_name) {
            this.english_name = english_name;
        }

        public String getChinese_name() {
            return chinese_name;
        }

        public void setChinese_name(String chinese_name) {
            this.chinese_name = chinese_name;
        }

        public String getHeader_img() {
            return header_img;
        }

        public void setHeader_img(String header_img) {
            this.header_img = header_img;
        }
    }
}
