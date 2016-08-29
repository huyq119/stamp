package com.example.stamp;

import com.example.stamp.utils.Version;

/**
 * 静态字段
 * Created by Administrator on 2016/8/3.
 */
public class StaticField {
    // 后期更改的值
    public static final String PHONE_INFO = "phone_info";
    public static final String APP_KEY_CODE = "1001001";
    public static final String VERSION_CODE = Version.getVersion();
    public static final String ROOT = "http://test.chinau.com.cn:8090/app/gateway.do";

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
    //设计家传值中文名的键
    public static final String DESIGNERDETAIL_CHINESE = "designerDetail_chinese";
    //设计家传值英文名的键
    public static final String DESIGNERDETAIL_ENGLISH = "designerDetail_english";

    //淘邮票
    public static final String GOODSLIST = "cn.com.chinau.goodslist.query";
    public static final String GOODS_SOURCE = "goods_source";

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

}
