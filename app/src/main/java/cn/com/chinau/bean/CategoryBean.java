package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/10.
 * 类别查询(不登录) bean （邮市，竞拍）
 */
public class CategoryBean extends BaseBean {

    private ArrayList<Category> category;

    public ArrayList<Category> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    public class Category {
        private String name;
        private String value;
        private String img_url;
        private ArrayList<SubCategory> subCategory;// 一级标题List

        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setValue(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
        public void setImg_url(String img_url){
            this.img_url = img_url;
        }
        public String getImg_url(){
            return this.img_url;
        }
        public void setSubCategory(ArrayList<SubCategory> subCategory){
            this.subCategory = subCategory;
        }
        public ArrayList<SubCategory> getSubCategory(){
            return this.subCategory;
        }

        public class SubCategory {
            private String name;
            private String value;
            private String img_url;
            private ArrayList<SmllSubCategoryData> subCategory;// 二级标题list

            public void setName(String name){
                this.name = name;
            }
            public String getName(){
                return this.name;
            }
            public void setValue(String value){
                this.value = value;
            }
            public String getValue(){
                return this.value;
            }
            public void setImg_url(String img_url){
                this.img_url = img_url;
            }
            public String getImg_url(){
                return this.img_url;
            }
            public void setSubCategory(ArrayList<SmllSubCategoryData> subCategory){
                this.subCategory = subCategory;
            }
            public ArrayList<SmllSubCategoryData> getSubCategory(){
                return this.subCategory;
            }

            public class SmllSubCategoryData {
                private String name;
                private String value;
                private String img_url;// 图片
//                private String subCategory;
                private ArrayList<SmllSubCategoryTwo> subCategory;// 二级标题list

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getImg_url() {
                    return img_url;
                }

                public void setImg_url(String img_url) {
                    this.img_url = img_url;
                }

//                public String getSubCategory() {
//                    return subCategory;
//                }
//
//                public void setSubCategory(String subCategory) {
//                    this.subCategory = subCategory;
//                }


                public ArrayList<SmllSubCategoryTwo> getSubCategory() {
                    return subCategory;
                }

                public void setSubCategory(ArrayList<SmllSubCategoryTwo> subCategory) {
                    this.subCategory = subCategory;
                }


                public class SmllSubCategoryTwo{

                }
            }
        }
    }

}
