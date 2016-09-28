package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 * 系统参数bean类
 */
public class SysParamQueryBean extends BaseBean {

    private Sys_param_value sys_param_value;

    public Sys_param_value getSys_param_value() {
        return sys_param_value;
    }

    public void setSys_param_value(Sys_param_value sys_param_value) {
        this.sys_param_value = sys_param_value;
    }

    public class Sys_param_value {

        private ArrayList<String> pay_type;

        private ArrayList<Auction_rule> auction_rule;

        private ArrayList<Bank_icon> bank_icon;

        private String register_agreement;

        private Express_comp express_comp;

        private Buyback_scan_summary buyback_scan_summary;

        public ArrayList<String> getPay_type() {
            return pay_type;
        }

        public void setPay_type(ArrayList<String> pay_type) {
            this.pay_type = pay_type;
        }

        public ArrayList<Auction_rule> getAuction_rule() {
            return auction_rule;
        }

        public void setAuction_rule(ArrayList<Auction_rule> auction_rule) {
            this.auction_rule = auction_rule;
        }

        public String getRegister_agreement() {
            return register_agreement;
        }

        public void setRegister_agreement(String register_agreement) {
            this.register_agreement = register_agreement;
        }

        public ArrayList<Bank_icon> getBank_icon() {
            return bank_icon;
        }

        public void setBank_icon(ArrayList<Bank_icon> bank_icon) {
            this.bank_icon = bank_icon;
        }

        public Express_comp getExpress_comp() {
            return express_comp;
        }

        public void setExpress_comp(Express_comp express_comp) {
            this.express_comp = express_comp;
        }

        public Buyback_scan_summary getBuyback_scan_summary() {
            return buyback_scan_summary;
        }

        public void setBuyback_scan_summary(Buyback_scan_summary buyback_scan_summary) {
            this.buyback_scan_summary = buyback_scan_summary;
        }

        public class Auction_rule {
            private String scope;
            private String price;

            public void setScope(String scope) {
                this.scope = scope;
            }

            public String getScope() {
                return this.scope;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPrice() {
                return this.price;
            }
        }

        public class Bank_icon {
            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public class Express_comp {

            private String shunfeng;

            private String ems;

            public String getShunfeng() {
                return shunfeng;
            }

            public void setShunfeng(String shunfeng) {
                this.shunfeng = shunfeng;
            }

            public String getEms() {
                return ems;
            }

            public void setEms(String ems) {
                this.ems = ems;
            }
        }


        public class Buyback_scan_summary {
            private ArrayList<String> process;

            private String summary;

            private String image;

            public ArrayList<String> getProcess() {
                return process;
            }

            public void setProcess(ArrayList<String> process) {
                this.process = process;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }


    }

}
