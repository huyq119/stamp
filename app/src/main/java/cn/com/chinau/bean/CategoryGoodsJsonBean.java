package cn.com.chinau.bean;

/**
 * Date: 2016/11/8 15:07
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 自营商城，邮市，竞拍类别查询上传Json实体类
 */

public class CategoryGoodsJsonBean {


    private String year;// 年代
    private String designer; // 设计家
    private CategoryBean category; // 类别

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public static class CategoryBean{

        private String category; // 一级类别
        private SubBean subCategory; // 二级类别

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public SubBean getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(SubBean subCategory) {
            this.subCategory = subCategory;
        }

        public static class SubBean{
            private String sub; // 二级类别

            public String getSub() {
                return sub;
            }

            public void setSub(String sub) {
                this.sub = sub;
            }
        }
    }

}
