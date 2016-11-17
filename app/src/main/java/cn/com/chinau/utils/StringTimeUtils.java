package cn.com.chinau.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
     * @return 获取的是年-月-日 时-分-秒
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
     * @return 获取的是年-月-日
     */
    public static String getShortTime(String str) {
        String mTime = LastIndex(str, 3);
        String year = mTime.substring(0, 4);
        String month = mTime.substring(4, 6);
        String day = mTime.substring(6, 8);

//        return year + month + day;
        return year + "-" + month + "-" + day;
    }

    /**
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @return String 转换后的String类型的日期时间 格式为yyyy-MM-dd
     */
    public static String getStringByFormat(String strDate) {
       String YMD = "yyyy-MM-dd";
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(YMD);
            c.setTime(mSimpleDateFormat.parse(strDate));

            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(YMD);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 将毫秒转换成时分秒格式
     * @param milliSecondTime 毫秒参数
     * @return
     */
    public static String calculatTime(int milliSecondTime) {

        int hour = milliSecondTime /(60*60*1000);
        int minute = (milliSecondTime - hour*60*60*1000)/(60*1000);
        int seconds = (milliSecondTime - hour*60*60*1000 - minute*60*1000)/1000;

        if(seconds >= 60 )
        {
            seconds = seconds % 60;
            minute+=seconds/60;
        }
        if(minute >= 60)
        {
            minute = minute % 60;
            hour  += minute/60;
        }

        String sh = "";
        String sm ="";
        String ss = "";
        if(hour <10) {
            sh = "0" + String.valueOf(hour);
        }else {
            sh = String.valueOf(hour);
        }
        if(minute <10) {
            sm = "0" + String.valueOf(minute);
        }else {
            sm = String.valueOf(minute);
        }
        if(seconds <10) {
            ss = "0" + String.valueOf(seconds);
        }else {
            ss = String.valueOf(seconds);
        }

        return sh +","+sm+","+ ss;
    }




}
