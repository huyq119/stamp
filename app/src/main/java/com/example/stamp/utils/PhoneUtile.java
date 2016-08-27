package com.example.stamp.utils;

import android.text.TextUtils;

/**
 * 判断电话号码的格式
 * 
 * @author Administrator
 *
 */
public class PhoneUtile {
	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3、4、5、6、7、8、9中的一个，其他位置的可以为0-9
		 */
		// "[1]"代表第1位为数字1，"[3456789]"代表第二位可以为3、4、5、6、7、8、9中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		String telRegex = "^((1[358][0-9])|(14[57])|(17[0678]))\\d{8}$";
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else
			return mobileNums.matches(telRegex);
	}

	/**
	 * 判断手机号码是否合理
	 * 
	 * @param phoneNums
	 */
	public static boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断一个字符串的位数
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean isMatchLength(String str, int length) {
		if (str.isEmpty()) {
			return false;
		} else {
			return str.length() == length ? true : false;
		}
	}

	/**
	 * 获取phone_info信息的
	 */
	public static String getPhone_info() {
		String phone_info = "Android" + android.os.Build.VERSION.RELEASE;
		String resolution = Resolution.getResolution();
		phone_info += resolution;
		return phone_info;
	}

	/**
	 * 判断姓名
	 */
	public static boolean verifyNickname(String Chinese) {
		if (Chinese == null || Chinese.length() == 0) {
			return false;
		}
		int len = 0;
		char[] nickchar = Chinese.toCharArray();
		for (int i = 0; i < nickchar.length; i++) {
			if (isChinese(nickchar[i])) {
				len += 2;
			} else {
				len += 1;
			}
		}
		if (len < 4 || len > 8) {
			// edt_username.setError("正确的昵称应该为\n1、4-15个字符\n2、2-7个汉字\n3、不能是邮箱和手机号");
			return false;
		}
		return true;
	}

	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
}
