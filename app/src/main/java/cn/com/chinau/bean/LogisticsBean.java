package cn.com.chinau.bean;

import java.util.List;

/**
 * 物流信息的实体类
 * Created by Angle on 2016/8/24.
 */
public class LogisticsBean extends BaseBean {
    public String goods_image;
    public String express_status;
    public String express_no;
    public String service_phone;
    public List<Express> express_info_list;

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getExpress_status() {
        return express_status;
    }

    public void setExpress_status(String express_status) {
        this.express_status = express_status;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getExpress_no() {
        return express_no;
    }

    public void setExpress_no(String express_no) {
        this.express_no = express_no;
    }

    public List<Express> getExpress_info_list() {
        return express_info_list;
    }

    public void setExpress_info_list(List<Express> express_info_list) {
        this.express_info_list = express_info_list;
    }

    public static class Express {
        public String express_time;
        public String express_route;

        public Express(String express_time, String express_route) {
            this.express_time = express_time;
            this.express_route = express_route;
        }

        public String getExpress_time() {
            return express_time;
        }

        public void setExpress_time(String express_time) {
            this.express_time = express_time;
        }

        public String getExpress_route() {
            return express_route;
        }

        public void setExpress_route(String express_route) {
            this.express_route = express_route;
        }
    }
}
