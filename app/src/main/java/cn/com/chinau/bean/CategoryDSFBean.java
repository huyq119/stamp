package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/12.
 * 第三方类别查询实体类
 */
public class CategoryDSFBean extends BaseBean {

    private ArrayList<Category> category;

    public ArrayList<Category> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    public class Category {
        private String name;// 一级类别
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

        public ArrayList<SubCategory> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(ArrayList<SubCategory> subCategory) {
            this.subCategory = subCategory;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public class SubCategory {
            private String name;// 一级类别名字
            private String value;
            private String img_url;// 图片
            private ArrayList<SubCategorys> subCategory;

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

//            public String getSubCategory() {
//                return subCategory;
//            }
//
//            subCategorypublic void setSubCategory(String subCategory) {
//                this.subCategory = subCategory;
//            }


            public ArrayList<SubCategorys> getSubCategory() {
                return subCategory;
            }

            public void setSubCategory(ArrayList<SubCategorys> subCategory) {
                this.subCategory = subCategory;
            }

            public class SubCategorys{

            }
        }
    }

}
