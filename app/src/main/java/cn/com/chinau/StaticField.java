package cn.com.chinau;

import cn.com.chinau.utils.Version;


/**
 * 静态字段
 * Created by Administrator on 2016/8/3.
 */
public class StaticField {
    // 短信验证码发送(登录)
    public static final String SMS_SEND = "cn.com.chinau.sms.send";//短信验证码发送(登录)
    public static final String MOBILE = "mobile";// 手机号
    // 短信类型（DL登录，ZC注册，TX提现，BK绑定银行卡，JB解绑银行卡，XG修改银行卡，XGDL修改登录密码，XGTX修改提现密码，WJMM忘记密码，）
    public static final String SMS_TYPE = "sms_type";
    public static final int CODE_SUCCESS = 10;
    public static final long GIVENTIME = 1000 * 60 * 10;// 设定时间

    // 登录、注册
    public static final String LOGIN_REGISTER = "cn.com.chinau.user.login.register";
    public static final String SMS_CODE = "sms_code";// 验证码
    public static final String IS_AGREE = "is_agree";// 是否勾选协议
    public static final String PASSWORD = "password";// 密码
    public static final String ZC = "ZC";// 注册
    public static final String DL = "DL";// 登录
    // 忘记密码，重置密码，设置提现密码
    public static final String NAME = "stamp";// 保存在本地的文件名
    public static final String WJMM = "WJMM";// 忘记密码
    public static final String PWD_MODIFY = "cn.com.chinau.pwd.modify";// 修改密码
    public static final String NEW_PWD = "new_pwd";// 新密码
    public static final String XGDL = "XGDL";// 修改登录密码
    public static final String CZDL = "CZDL";// 重置登录密码
    public static final String XGTX = "XGTX";// 修改提现密码


    // 后期更改的值
    public static final String PHONE_INFO = "phone_info";
    public static final String APP_KEY_CODE = "1001001";
    public static final String VERSION_CODE = Version.getVersion();
    public static final String ROOT = "http://test.chinau.com.cn:8090/app/gateway.do";
    public final static String TOKEN = "token";// 用户登录之后的标识，如果用户已登录，需要传
    public final static String USER_ID = "user_id";// 用户ID，如果用户已登录，需要传
    //常用的字段
    public static final int SUCCESS = 0;
    public static final int TOUCH_EVENT_ID = 2;// SrcollView滑动的ID
    public static final int HOME_SV_COUNT = 1;// Handler发送的结果码
    public static final int ADDSUCCESS = 1;// Handler发送的结果码(加入成功)
    public static final int DeleteSUCCESS = 4;// Handler发送的结果码（删除成功）
    public static final int ADDSHOPPINGCARTSUCCESS = 3;// Handler发送的结果码（加入购物车）
    public static final int PRICESUCCESS = 3;// Handler发送的结果码（出价）
    public static final int QUERYSUCCESS = 1;// Handler发送的结果码（地址查询）
    public static final int DETAILSUCCESS = 2;// Handler发送的结果码（编辑地址查询）
    public static final int ADDRESSSUCCESS = 2;// Handler发送的结果码（获取地址省市区）
    public static final int SelectSUCCESS = 2;// Handler发送的结果码（点击的是默认按钮还是编辑按钮）

    public static final int CLASS_SUCCESS = 3;// Handler发送的结果码（热搜类别查询）
    public static final int ML_SUCCESS = 4;// Handler发送的结果码（邮票目录类别查询）
    public static final int SC_SUCCESS = 5;// Handler发送的结果码（商城类别查询）
    public static final int DSF_SUCCESS = 6;// Handler发送的结果码（第三方类别查询）
    public static final int YS_SUCCESS = 7;// Handler发送的结果码（邮市类别查询）
    public static final int JP_SUCCESS = 8;// Handler发送的结果码（竞拍类别查询）

    public static final int QB_SUCCESS = 1;// Handler发送的结果码（查询竞拍记录全部）
    public static final int JPZ_SUCCESS = 2;// Handler发送的结果码（查询竞拍记录竞拍中）
    public static final int CJ_SUCCESS = 3;// Handler发送的结果码（查询竞拍记录出局）
    public static final int CG_SUCCESS = 4;// Handler发送的结果码（查询竞拍记录成功）

    public static final int DFK_SUCCESS = 1;// Handler发送的结果码（订单查询待付款）
    public static final int DSH_SUCCESS = 2;// Handler发送的结果码（订单查询待收货）
    public static final int WC_SUCCESS = 3;// Handler发送的结果码（订单查询已完成）
    public static final int TK_SUCCESS = 4;// Handler发送的结果码（订单查询退款）
    public static final int WX_SUCCESS = 1;// Handler发送的结果码（微信支付）
    public static final int ALI_SUCCESS = 2;// Handler发送的结果码（支付宝支付）




    //步长
    public final static int OFFSETNUM = 20;

    //淘邮票页面的Dialog标识
    public final static String PANSTAMPFILTERDIALOG = "panStampFilterDialog";
    //邮票目录页面的Dialog标识
    public final static String STAMPTAPFILTERDIALOG = "StampTapFilterDialog";
    //搜索历史
    public final static String SEARCHHISTORICAL = "SearchHistorical";
    //搜索页面传递的内容
    public final static String SEARCHBUNDLE = "SearchBundle";
    //是否登录
    public final static String ISPHONE = "isPhone";
    //首页的Url字段
    public static final String HOMEURL = "homeUrl";
    public static final String HOMETITLE = "homeTitle";

    // 都需要的字段
    public static final String APP_KEY = "app_key";
    public static final String SERVICE_TYPE = "service_type";
    public static final String VERSION = "version";
    public static final String SIGN = "sign";
    public static final String IMEI = "imei";

    //首页请求地址
    public static final String HOMEPAGE = "cn.com.chinau.homepage";
    public static final String OP_TYPE = "op_type";
    public static final String CURRENT_INDEX = "current_index";
    public static final String OFFSET = "offset";
    public static final String SYSPARAM_QUERY = "cn.com.chinau.sysparam.query";// 系统参数接口

    //邮票目录
    public static final String STAMPTAP = "cn.com.chinau.stamplist.query";
    public static final String STAMP = "cn.com.chinau.stamp.query";//详情页
    public static final String ORDER_BY = "order_by";
    public static final String STAMP_SN = "stamp_sn";
    public static final String ZH = "ZH";
    public static final String SJ = "SJ";
    public static final String JG = "JG";
    public static final String ML = "ML";
    public static final String SC_ZY = "SC_ZY";
    public static final String SC_DSF = "SC_DSF";
    public static final String CATEGORY = "category";


    public static final String A = "A";//升序
    public static final String D = "D";//降序
    public static final String SORT_TYPE = "sort_type";

    //邮票分类
    public static final String STAMPCATEGORY = "cn.com.chinau.category.query";


    //邮市目录传值的键
    public static final String STAMPDETAIL_SN = "stampDetail_sn";// 编号
    public static final String STAMPDETAIL_PRICE = "stampDetail_price";// 当前价格

    //设计家列表查询
    public static final String DESIGNERLIST = "cn.com.chinau.designerlist.query";
    // 设计家详情查询
    public static final String DESIGNERDETAIL = "cn.com.chinau.designer.query";
    // 设计故事+艺术作品+名家访谈详情查询(不登录，H5链接地址)
    public static final String DESIGNER_DETAIL_QUERY = "cn.com.chinau.designer.detail.query";
    //设计家传值中文名的键
    public static final String DESIGNERDETAIL_CHINESE = "designerDetail_chinese";
    //设计家传值英文名的键
    public static final String DESIGNERDETAIL_ENGLISH = "designerDetail_english";
    public static final String DESIGNERSN = "designer_sn";// 设计家编号
    public static final String DESIGNER_RESUME = "resume";// 列表详情H5
    public static final String DESIGNER_STORY_SN = "Story_sn";//设计故事编号
    public static final String DESIGNER_WORKS_SN = "Works_sn";//艺术作品编号
    public static final String DESIGNER_VIEW_SN = "View_sn";//名家访谈编号
    public static final String GS = "GS";//设计故事
    public static final String ZP = "ZP";//艺术作品
    public static final String FT = "FT";//名家访谈
    public static final String DESIGNER_SN = "sn";// 编号
    public static final String DESIGNER_OP_TYPE = "op_type";// 操作类型
    public static final String DESIGNER_ZP_CATEGORY = "zp_category";// 操作类型
    public static final String DTEAIL = "detail";// 跳转至（设计故事，艺术作品，名家访谈详情页面）的标识键


    //淘邮票
    public static final String GOODSLIST = "cn.com.chinau.goodslist.query";
    public static final String GOODS_SOURCE = "goods_source";
    public static final String GOODSSOURCE = "SC_ZY,SC_DSF,YS,JP";
    public static final String XL = "XL";

    //省市区县查询
    public static final String NATION = "cn.com.chinau.nation.query";

    //搜索页面
    public static final String SEARCH = "cn.com.chinau.search";
    public static final String CONDITION = "query_condition";//搜索条件
    public static final String SCOPE = "query_scope";//查询范围
    public static final String SORT_BY = "sort_by";
    public static final String QB = "QB"; //QB全部
    public static final String SC = "SC"; //SC商城
    public static final String YS = "YS"; //YS邮市
    public static final String JP = "JP"; //JP竞拍
    public static final String RM = "RM"; //RM热门

    // 我的页面，商品订单跳转的标识键
    public static final String ORDERS = "orders";

    // 商城
    public static final String GOODSMALL = "SC_ZY,SC_DSF";
    //竞拍
    public static final String JS = "JS";//即将结束
    public static final String KP = "KP";//等待开拍

    // 商品详情查询（不等录）
    public static final String GOODSDETAIL = "cn.com.chinau.goods.query";
    public static final String GOODS_SN = "goods_sn";

    // 个人信息查询
    public static final String PERSONQUERY = "cn.com.chinau.userinfo.query";
    // 扫码回购商品详情
    public static final String QUERY = "cn.com.chinau.buyback.goods.query";
    //条码或二维码，可据此查询到该商品是否从平台购买，每个商品有一个唯一码
    public static final String SCAN_CODE = "scan_code";
    // 确认回购
    public static final String CONFIRM = "cn.com.chinau.buyback.confirm";
    // 操作类型，确认回购
    public static final String HG = "HG";
    // 订单明细编号(原订单) ，确认回购（HG）时传，物流（WL）时不传
    public static final String ORDER_DETAIL_SN = "order_detail_sn";
    public static final String EXPRESS_COMP = "express_comp";// 快递公司
    public static final String EXPRESS_NO = "express_no";// 快递单号
    public static final String ORDER_SN = "order_sn";// 回购订单编号
    public static final String WL = "WL";// HG确认回购；WL物流

    // 竞拍记录List
    public static final String AUCTIONREC = "cn.com.chinau.auctionrec.query";
    public static final String AUCTION_STATUS = "auction_status";// 竞拍状态
    public static final String AUCTIONOFFER = "cn.com.chinau.auction.offer";// 竞拍商品出价
    public static final String AUCTIONPRICE = "auction_price";// 出价


    //竞拍状态：QB全部；CJ出局；CG成功，JPZ竞拍中；
    public static final String CG = "CG";
    public static final String CJ = "CJ";
    public static final String JPZ = "JPZ";
    // 回购订单List
    public static final String ORDERLIST = "cn.com.chinau.buyback.orderlist.query";
    public static final String ORDERDETAIL = "cn.com.chinau.buyback.order.query";// 订详情
    public static final String BUYBACK_TYPE = "buyback_type"; //订单类型：SM：扫码回购；PT：普通回购
    public static final String SM = "SM";
    public static final String PT = "PT";
    public static final int ORDERS_SUCCESS = 1;
    // 我的邮集List
    public static final String ALBUMLIST = "cn.com.chinau.album.list";
    // 修改邮集
    public static final String MODIFY = "cn.com.chinau.album.modify";
    public static final String STAMP_COUNT = "stamp_count";// 邮票数量
    // 操作类型：SC：删除；JR加入；XG修改
    public static final String JR = "JR";
    public static final String XG = "XG";
    // 收藏夹列表
    public static final String FAVORITELIST = "cn.com.chinau.favorite.list.query";
    // 修改收藏状态
    public static final String FAVORITEMODIFY = "cn.com.chinau.favorite.modify";

    public static final String GOODS_TYPE = "goods_type";// 商品类型
    // 收货地址管理list
    public static final String ADDRESSLIST = "cn.com.chinau.address.list.query";
    public static final String ADDRESS_ID = "address_id";
    public static final String LB = "LB";
    public static final String XQ = "XQ";
    public static final String IS_DEFAULT = "is_default";
    public static final String XZ = "XZ";
    public static final String NAMES = "name";
    public static final String ADDRESS_DETAIL = "detail";
    public static final String PROV = "prov";
    public static final String CITY = "city";
    public static final String AREA = "area";
    // 修改地址url（删除；修改；新增 ）
    public static final String UPDATERESSMODIFY= "cn.com.chinau.address.modify";

    // 商品订单list
    public static final String GOODSORDERLIST = "cn.com.chinau.orderlist.query";
    public static final String ORDERSTATUS = "order_status";
    public static final String DFK = "DFK";// 待付款
    public static final String DSH = "DSH";// 待收货
    public static final String WC = "WC"; // 已完成
    public static final String TK = "TK";// 退款
    // 购物车
    public static final String SHOPCARTQUERY = "cn.com.chinau.shopcart.query";
    // 修改购物车
    public static final String SHOPCARTMODIFY = "cn.com.chinau.shopcart.modify";
    public static final String GOODESINFO = "goods_info";
    // 购物车去接算（确认订单）
    public static final String SHOPCARTSETTLE = "cn.com.chinau.shopcart.settle";
    // 订单支付
    public static final String ORDERPAY = "cn.com.chinau.order.pay";
    public static final String REQUESTID = "client_request_id"; //客户订单请求号
    public static final String PAYAMOUNT = "pay_amount"; //支付金额
    public static final String PAYTYPE= "pay_type"; //支付方式
    public static final String ALIPAY= "ALIPAY"; //支付宝
    public static final String WXPAY= "WXPAY"; // 微信
    // APPID
    public static final String APP_ID= "wxf9abf6e67f0caed4";
    public static final String PACKAGE= "Sign=WXPay";

    public static class ShowMsgActivity {
        public static final String STitle = "showmsg_title";
        public static final String SMessage = "showmsg_message";
        public static final String BAThumbData = "showmsg_thumb_data";
    }
}
