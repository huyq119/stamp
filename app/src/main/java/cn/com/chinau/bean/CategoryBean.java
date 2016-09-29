package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/10.
 * 类别查询(不登录) bean
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
        private String name;// 类别
        private String value;
        private String img_url;// 图片
        private ArrayList<SubCategory> subCategory;

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

        public ArrayList<SubCategory> getSubCategory() {
            return subCategory;
        }
        public void setSubCategory(ArrayList<SubCategory> subCategory) {
            this.subCategory = subCategory;
        }
        public class SubCategory {
            private String name;// 新中国邮票，清民区邮票，主题邮册，其他邮品
            private String value;
            private String img_url;// 图片
            private ArrayList<SmllSubCategoryData> subCategory;

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

            public ArrayList<SmllSubCategoryData> getSubCategory() {
                return subCategory;
            }

            public void setSubCategory(ArrayList<SmllSubCategoryData> subCategory) {
                this.subCategory = subCategory;
            }

            public class SmllSubCategoryData {
                private String name;
                private String value;
                private String img_url;// 图片
                private String subCategory;

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

                public String getSubCategory() {
                    return subCategory;
                }

                public void setSubCategory(String subCategory) {
                    this.subCategory = subCategory;
                }

            }
        }


    }

}
