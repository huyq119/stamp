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

        public ArrayList<String> pay_type;

        public Auction_rule auction_rule;

        public Bank_icon bank_icon;

        public String register_agreement;

        public Express_comp express_comp;

        public Buyback_scan_summary buyback_scan_summary;

        public void setPay_type(ArrayList<String> pay_type) {
            this.pay_type = pay_type;
        }

        public ArrayList<String> getPay_type() {
            return pay_type;
        }

        public Auction_rule getAuction_rule() {
            return auction_rule;
        }

        public void setAuction_rule(Auction_rule auction_rule) {
            this.auction_rule = auction_rule;
        }

        public Bank_icon getBank_icon() {
            return bank_icon;
        }

        public void setBank_icon(Bank_icon bank_icon) {
            this.bank_icon = bank_icon;
        }

        public String getRegister_agreement() {
            return register_agreement;
        }

        public void setRegister_agreement(String register_agreement) {
            this.register_agreement = register_agreement;
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

        public String rule1 = "500-10000000";
        public String rule2 = "100-500";
        public String rule3 = "1-50";
        public String rule4 = "50-100";

        public class Auction_rule {
            public String rule1;
            public String rule2;
            public String rule3;
            public String rule4;

            public String getRule1() {
                return rule1;
            }

            public void setRule1(String rule1) {
                this.rule1 = rule1;
            }

            public String getRule2() {
                return rule2;
            }

            public void setRule2(String rule2) {
                this.rule2 = rule2;
            }

            public String getRule4() {
                return rule4;
            }

            public void setRule4(String rule4) {
                this.rule4 = rule4;
            }

            public String getRule3() {
                return rule3;
            }

            public void setRule3(String rule3) {
                this.rule3 = rule3;
            }
        }

        public class Bank_icon {
            private String ABC;

            private String BCM;

            private String BOB;

            private String BOC;

            private String CCB;

            private String CEB;

            private String CIB;

            private String CMB;

            private String CMBC;

            private String CNCB;

            private String HXB;

            private String ICBC;

            private String PAB;

            private String PSBC;

            private String SHANGHAI;

            private String SPDB;

            public String getABC() {
                return ABC;
            }

            public void setABC(String ABC) {
                this.ABC = ABC;
            }

            public String getBCM() {
                return BCM;
            }

            public void setBCM(String BCM) {
                this.BCM = BCM;
            }

            public String getBOB() {
                return BOB;
            }

            public void setBOB(String BOB) {
                this.BOB = BOB;
            }

            public String getBOC() {
                return BOC;
            }

            public void setBOC(String BOC) {
                this.BOC = BOC;
            }

            public String getCCB() {
                return CCB;
            }

            public void setCCB(String CCB) {
                this.CCB = CCB;
            }

            public String getCEB() {
                return CEB;
            }

            public void setCEB(String CEB) {
                this.CEB = CEB;
            }

            public String getCIB() {
                return CIB;
            }

            public void setCIB(String CIB) {
                this.CIB = CIB;
            }

            public String getCMB() {
                return CMB;
            }

            public void setCMB(String CMB) {
                this.CMB = CMB;
            }

            public String getCMBC() {
                return CMBC;
            }

            public void setCMBC(String CMBC) {
                this.CMBC = CMBC;
            }

            public String getCNCB() {
                return CNCB;
            }

            public void setCNCB(String CNCB) {
                this.CNCB = CNCB;
            }

            public String getICBC() {
                return ICBC;
            }

            public void setICBC(String ICBC) {
                this.ICBC = ICBC;
            }

            public String getPAB() {
                return PAB;
            }

            public void setPAB(String PAB) {
                this.PAB = PAB;
            }

            public String getHXB() {
                return HXB;
            }

            public void setHXB(String HXB) {
                this.HXB = HXB;
            }

            public String getSHANGHAI() {
                return SHANGHAI;
            }

            public void setSHANGHAI(String SHANGHAI) {
                this.SHANGHAI = SHANGHAI;
            }

            public String getPSBC() {
                return PSBC;
            }

            public void setPSBC(String PSBC) {
                this.PSBC = PSBC;
            }

            public String getSPDB() {
                return SPDB;
            }

            public void setSPDB(String SPDB) {
                this.SPDB = SPDB;
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
