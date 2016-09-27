package cn.com.chinau.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import cn.com.chinau.activity.BigPhotoActivity;
import cn.com.chinau.ui.MyApplication;

/**
 * Created by Administrator on 2016/9/27.
 * 扫码回购详情页面的viewpager适配器
 */
public class BackMarketAdapter extends PagerAdapter {

    private String[] arr;
    private BitmapUtils bitmap;
    private Context context;
    public BackMarketAdapter(BitmapUtils bitmap, String[] arr, Context context) {
        super();
        this.arr = arr;
        this.bitmap = bitmap;
        this.context = context;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int index = position % arr.length;
        ImageView image = new ImageView(MyApplication.getApplication());
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        String photourl = arr[index];
        bitmap.display(image, photourl);
        container.addView(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BigPhotoActivity.class);
                intent.putExtra("photo", arr);
                intent.putExtra("index", index);
                context.startActivity(intent);
            }
        });
        return image;
    }


}
