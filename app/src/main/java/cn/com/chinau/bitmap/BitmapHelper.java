package cn.com.chinau.bitmap;


import android.graphics.drawable.Drawable;

import com.lidroid.xutils.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.com.chinau.utils.CacheUtilse;
import cn.com.chinau.utils.UIUtils;


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


    /**
     * @param urlpath
     * @return Bitmap
     * 根据url获取布局背景的对象
     */
    public static Drawable getDrawable(String urlpath){
        Drawable d = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            d = Drawable.createFromStream(in, "background.jpg");
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

}
