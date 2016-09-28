package cn.com.chinau.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Des3加密
 * 
 * @author Administrator
 *
 */
public class Des3Encode {
	// 定义加密算法，DESede即3DES
	private static final String Algorithm = "DESede";
	// 加密密钥
	private static final String PASSWORD_CRYPT_KEY = "C3BofasVsQlfaj4R8KuPw7Jc";

	/**
	 * 加密方法
	 * 
	 * @param src
	 *            源数据的字节数组
	 * @return
	 */
	public static byte[] encryptMode(byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
			// 实例化Cipher
			Cipher cipher = Cipher.getInstance(Algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, deskey);
			return cipher.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密函数
	 * 
	 * @param src
	 *            密文的字节数组
	 * @return
	 */
	public static byte[] decryptMode(byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据字符串生成密钥24位的字节数组
	 * 
	 * @param keyStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
		byte[] key = new byte[24];
		byte[] temp = keyStr.getBytes("UTF-8");

		if (key.length > temp.length) {
			System.arraycopy(temp, 0, key, 0, temp.length);
		} else {
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	}

	// 下面是关于加密和解密的方法
	// String msg = "使用3DES对数据进行加密";
	// System.out.println("【加密前】：" + msg);
	//
	// // 加密
	// byte[] secretArr = DES3Utils.encryptMode(msg.getBytes());
	// System.out.println("【加密后】：" + new String(secretArr));
	//
	// // 解密
	// byte[] myMsgArr = DES3Utils.decryptMode(secretArr);
	// System.out.println("【解密后】：" + new String(myMsgArr));
}