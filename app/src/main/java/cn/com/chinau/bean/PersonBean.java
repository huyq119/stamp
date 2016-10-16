package cn.com.chinau.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/20.
 * 个人信息查询
 */
public class PersonBean {

    public ArrayList<History> act_history_list; // 账户收支明细
    public String balance;// 余额
    public String has_bind_card;// 是否绑定银行卡
    public String has_remit_pwd;// 是否已经设置提现密码
    public String header_image;// 头像图片url
    public ArrayList<Order> order_list;// 订单列表
    public String rsp_code;
    public String rsp_msg;
    public String show_update;// 是否显示版本更新选项
    public String sign;
    public String unusable_balance;// 不可用余额

    public class Order {

    }

    public class History {
        public String trx_date;// 交易时间
        public String name;// 订单或商品名称
        public String trx_amount;// 交易金额
        public String trx_desc;// 交易详述
        public String getTrx_date() {
            return trx_date;
        }
        public void setTrx_date(String trx_date) {
            this.trx_date = trx_date;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getTrx_amount() {
            return trx_amount;
        }
        public void setTrx_amount(String trx_amount) {
            this.trx_amount = trx_amount;
        }
        public String getTrx_desc() {
            return trx_desc;
        }
        public void setTrx_desc(String trx_desc) {
            this.trx_desc = trx_desc;
        }
        @Override
        public String toString() {
            return "History [trx_date=" + trx_date + ", name=" + name + ", trx_amount=" + trx_amount + ", trx_desc=" + trx_desc + "]";
        }

    }

    public ArrayList<History> getAct_history_list() {
        return act_history_list;
    }

    public void setAct_history_list(ArrayList<History> act_history_list) {
        this.act_history_list = act_history_list;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getHas_bind_card() {
        return has_bind_card;
    }

    public void setHas_bind_card(String has_bind_card) {
        this.has_bind_card = has_bind_card;
    }

    public String getHas_remit_pwd() {
        return has_remit_pwd;
    }

    public void setHas_remit_pwd(String has_remit_pwd) {
        this.has_remit_pwd = has_remit_pwd;
    }

    public String getHeader_image() {
        return header_image;
    }

    public void setHeader_image(String header_image) {
        this.header_image = header_image;
    }

    public ArrayList<Order> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<Order> order_list) {
        this.order_list = order_list;
    }

    public String getRsp_code() {
        return rsp_code;
    }

    public void setRsp_code(String rsp_code) {
        this.rsp_code = rsp_code;
    }

    public String getRsp_msg() {
        return rsp_msg;
    }

    public void setRsp_msg(String rsp_msg) {
        this.rsp_msg = rsp_msg;
    }

    public String getShow_update() {
        return show_update;
    }

    public void setShow_update(String show_update) {
        this.show_update = show_update;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUnusable_balance() {
        return unusable_balance;
    }

    public void setUnusable_balance(String unusable_balance) {
        this.unusable_balance = unusable_balance;
    }

    @Override
    public String toString() {
        return "PersonBean [act_history_list=" + act_history_list + ", balance=" + balance + ", has_bind_card=" + has_bind_card + ", has_remit_pwd=" + has_remit_pwd + ", header_image=" + header_image + ", order_list=" + order_list + ", rsp_code=" + rsp_code + ", rsp_msg=" + rsp_msg + ", show_update=" + show_update + ", sign=" + sign + ", unusable_balance=" + unusable_balance + "]";
    }


}
