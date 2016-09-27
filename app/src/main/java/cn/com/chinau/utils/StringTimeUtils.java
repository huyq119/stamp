package cn.com.chinau.utils;

/**
 * Created by Administrator on 2016/9/27.
 * 字符串事件截取工具类
 */
public class StringTimeUtils {

    /**
     * 去掉字符串的最后几位
     *
     * @param str
     *            要操作的字符串
     * @param index
     *            去掉的位数
     * @return
     */
    public static String LastIndex(String str, int index) {
        return str.substring(0, str.length() - index);
    }
    /**
     * 邮来邮网项目截取时间的方法
     *
     * @param str
     *            传入的时间字符串
     * @return
     */
    public static String getLongTime(String str) {
        String mTime = LastIndex(str, 3);
        String year = mTime.substring(0, 4);
        String month = mTime.substring(4, 6);
        String day = mTime.substring(6, 8);
        String hour = mTime.substring(8, 10);
        String minute = mTime.substring(10, 12);
        String sec = mTime.substring(12, 14);

        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + sec;
    }
    /**
     * 邮来邮网项目截取时间的方法
     *
     * @param str
     *            传入的时间字符串
     * @return
     */
    public static String getShortTime(String str) {
        String mTime = LastIndex(str, 3);
        String year = mTime.substring(0, 4);
        String month = mTime.substring(4, 6);
        String day = mTime.substring(6, 8);

        return year + "-" + month + "-" + day;
    }


}
