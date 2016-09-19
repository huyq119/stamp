package com.example.stamp.utils;

import java.util.Random;

/**
 * Created by Administrator on 2016/9/19.
 *
 * 生成4位随机验证码
 */
public class FourCode {


    public static String getCode() {
        Random r = new Random();
        String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";// 列出所有的字母数字
        String result = "";
        for (int i = 0; i < 4; i++)// 循环4次，输出四个数
        {
            int a = r.nextInt(62);// 从0-61中随机一个数，作为字符串的索引
            result += str.substring(a, a + 1);// 从字符串中利用索引找到输出它
        }
        return result;
    }

}
