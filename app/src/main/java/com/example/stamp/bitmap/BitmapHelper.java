package com.example.stamp.bitmap;


import com.example.stamp.utils.CacheUtilse;
import com.example.stamp.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;


/**
 * bitmaputils单例模式
 *
 * @author ���
 */
public class BitmapHelper {

    private BitmapHelper() {
    }

    private static BitmapUtils Bitmaputils;

    public static BitmapUtils getBitmapUtils() {
        if (Bitmaputils == null) {
            Bitmaputils = new BitmapUtils(UIUtils.getContext(), CacheUtilse.setDir(CacheUtilse.ICON).getAbsolutePath(), 0.3f);
//			Bitmaputils.configDefaultLoadingImage(R.drawable.logo_new);// 默认背景图片
//			Bitmaputils.configDefaultLoadFailedImage(R.drawable.logo_new);// 加载失败图片
        }
        return Bitmaputils;
    }

}
