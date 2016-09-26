package cn.com.chinau.utils;

import java.util.Arrays;
import java.util.HashMap;

import cn.com.chinau.StaticField;

/**
 * 排序的方法
 * 
 * @author Administrator
 *
 */
public class SortUtils {
	/**
	 * 数组排序的方法
	 * 
	 * @param arr
	 *            要排序的数组
	 * @return 排序之后的数组
	 */
	public static String[] sort(String[] arr) {
		Arrays.sort(arr);
		return arr;
	}

	/**
	 * 数组的排序
	 * 
	 * @param map
	 *            要排序的数组
	 * @return 排序后的字符串
	 */
	public static String MapSort(HashMap<String, String> map) {
		// 添加都有的字段
		map.put(StaticField.APP_KEY, StaticField.APP_KEY_CODE); //服务器端分配统一编号
		map.put(StaticField.VERSION, StaticField.VERSION_CODE); //版本号
		// 获取手机信息
		String phone_info = PhoneUtile.getPhone_info();
		map.put(StaticField.PHONE_INFO, phone_info); //手机信息
		map.put(StaticField.IMEI, IMEI.getIMEI());//手机IMEI码
		// 下面是排序
		Object[] key_arr = map.keySet().toArray();
		Arrays.sort(key_arr);
		String sign = "";
		for (Object key : key_arr) {
			Object value = map.get(key);
			sign += value.toString();
		}
		String s = sign + "C3BofasVsQlfaj4R8KuPw7Jc";
		MyLog.e("s", s);
		return s;
	}
}
