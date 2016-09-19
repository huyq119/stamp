package com.example.stamp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.stamp.ui.MyApplication;
import com.lidroid.xutils.BitmapUtils;


/**
 * ViewPager的适配器
 * 
 * @author Administrator
 */
public class HomeViewPagerAdapter extends PagerAdapter {

	private String[] arr;
	private BitmapUtils bitmap;

	public HomeViewPagerAdapter(BitmapUtils bitmap, String[] arr, Context context) {
		super();
		this.arr = arr;
		this.bitmap = bitmap;
	}

	@Override
	public int getCount() {
		return arr.length ;
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
//		final int index = position % arr.length;
		ImageView image = new ImageView(MyApplication.getApplication());
		image.setScaleType(ImageView.ScaleType.FIT_XY);
		String Banners = arr[position];
		bitmap.display(image, Banners);
		container.addView(image);
		return image;
	}

}
