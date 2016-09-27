package cn.com.chinau.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import cn.com.chinau.R;
import cn.com.chinau.activity.BigPhotoActivity;
import cn.com.chinau.ui.MyApplication;

/**
 * Created by Administrator on 2016/9/27.
 * 扫码回购详情页面的viewpager
 */
public class BigPhotoAdapter extends PagerAdapter {

    private String[] arr;
    private BitmapUtils bitmap;
    private BigPhotoActivity bigPhotoActivity;

    public BigPhotoAdapter(BitmapUtils bitmap, String[] arr, BigPhotoActivity bigPhotoActivity) {
        super();
        this.arr = arr;
        this.bitmap = bitmap;
        this.bigPhotoActivity = bigPhotoActivity;
    }

    @Override
    public int getCount() {
        return arr.length;
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
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView image = new ImageView(MyApplication.getApplication());
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        String photourl = arr[position];
        bitmap.display(image, photourl);
        container.addView(image);
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bigPhotoActivity.finish();
                bigPhotoActivity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

            }
        });
        return image;
    }


}
