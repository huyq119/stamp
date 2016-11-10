package cn.com.chinau.bean;

/**
 * Date: 2016/11/8 15:07
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 邮市，竞拍类别查询Json实体类
 */

public class CategoryGoodsJsonBean {


    private CategoryBean year;// 年代
    private CategoryBean designer; // 设计家
    private CategoryBean category; // 类别

    public CategoryBean getYear() {
        return year;
    }

    public void setYear(CategoryBean year) {
        this.year = year;
    }

    public CategoryBean getDesigner() {
        return designer;
    }

    public void setDesigner(CategoryBean designer) {
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
