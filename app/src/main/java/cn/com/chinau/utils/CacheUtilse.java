package cn.com.chinau.utils;

import android.os.Environment;

import java.io.File;

/**
 * 缓存工具类,主要是获取缓存的地址,这里设置三种缓存 1.json缓存
 * 
 * @author Administrator
 *
 */
public class CacheUtilse {
	public static final String ROOM = "Stamp";// 根目录
	public static final String ICON = "icon";// 根目录

	/**
	 * 将缓存写到那个文件夹中和当前的文件名
	 * 
	 */
	public static File setDir(String str) {
		StringBuilder path = new StringBuilder();

		if (isEnvironment()) {// 存储卡是否可用
			// 获取sdk路径
			path.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
			path.append(File.separator);// 添加斜杠
			path.append(ROOM);
			path.append(File.separator);
			path.append(str);
			path.append(File.separator);
			File file = new File(path.toString());
			if (!file.exists() && !file.isDirectory()) {// 判断是否有这个文件,以及是否是个文件
				file.mkdirs();
			}
			return file;
		} else {
			File cacheDir = UIUtils.getContext().getCacheDir();
			path.append(cacheDir.getAbsolutePath());
			path.append(File.separator);
			return cacheDir;
		}

	}

	/**
	 * 判断存储卡是否可用
	 * 
	 * @return
	 */
	private static boolean isEnvironment() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
