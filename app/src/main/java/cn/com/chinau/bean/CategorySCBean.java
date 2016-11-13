package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Date: 2016/11/8 22:19
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 商城类别查询实体类
 */

public class CategorySCBean extends BaseBean {

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
        private ArrayList<SubCategoryOne> subCategory;

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

        public ArrayList<SubCategoryOne> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(ArrayList<SubCategoryOne> subCategory) {
            this.subCategory = subCategory;
        }

        public class SubCategoryOne {
            private String name;
            private String value;
            private String img_url;
            private ArrayList<SubCategoryTwo> subCategory;

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return this.name;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getValue() {
                return this.value;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getImg_url() {
                return this.img_url;
            }

            public void setSubCategory(ArrayList<SubCategoryTwo> subCategory) {
                this.subCategory = subCategory;
            }

            public ArrayList<SubCategoryTwo> getSubCategory() {
                return this.subCategory;
            }

            public class SubCategoryTwo {
                private String name;
                private String value;
                private String img_url;
                private ArrayList<SubCategoryThree>  subCategory;

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return this.name;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getValue() {
                    return this.value;
                }

                public void setImg_url(String img_url) {
                    this.img_url = img_url;
                }

                public String getImg_url() {
                    return this.img_url;
                }

//                public void setSubCategory(String subCategory) {
//                    this.subCategory = subCategory;
//                }
//
//                public String getSubCategory() {
//                    return this.subCategory;
//                }


                public ArrayList<SubCategoryThree> getSubCategory() {
                    return subCategory;
                }

                public void setSubCategory(ArrayList<SubCategoryThree> subCategory) {
                    this.subCategory = subCategory;
               }

                public class SubCategoryThree {

                }
            }
        }

    }
}
