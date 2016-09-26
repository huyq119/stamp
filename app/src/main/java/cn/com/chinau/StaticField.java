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


    //邮票目录
    public static final String STAMPTAP = "cn.com.chinau.stamplist.query";
    public static final String STAMP = "cn.com.chinau.stamp.query";//详情页
    public static final String ORDER_BY = "order_by";
    public static final String STAMP_SN = "stamp_sn";
    public static final String ZH = "ZH";
    public static final String SJ = "SJ";
    public static final String JG = "JG";
    public static final String ML = "ML";


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
    public static final String QB = "QB";//QB全部
    public static final String SC = "SC";//SC商城
    public static final String YS = "YS";//YS邮市
    public static final String JP = "JP";//JP竞拍

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
}
