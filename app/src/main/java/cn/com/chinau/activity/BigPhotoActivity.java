package cn.com.chinau.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.lidroid.xutils.BitmapUtils;

import cn.com.chinau.R;
import cn.com.chinau.adapter.BigPhotoAdapter;
import cn.com.chinau.bitmap.BitmapHelper;

/**
 * Created by Administrator on 2016/9/27.
 *  展示图片的activity
 */
public class BigPhotoActivity extends Activity {



    private ViewPager mViewPager;
    private int current;// 当前选择的页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_bigphoto);
        mViewPager = (ViewPager) findViewById(R.id.BigPhoto_viewpager);
        BitmapUtils bitmapUtils = BitmapHelper.getBitmapUtils();
        Intent intent = getIntent();
        if (intent != null) {
            String[] photos = intent.getStringArrayExtra("photo");
            int index = intent.getIntExtra("index", 0);
            if (photos != null) {
                BigPhotoAdapter mVAdapter = new BigPhotoAdapter(bitmapUtils, photos, this);
                mViewPager.setAdapter(mVAdapter);
                mViewPager.setCurrentItem(index);
                Log.e("Bigindex", index + "");

            }
        }

    }


}
