package cn.com.chinau.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 我的邮集ViewPager的适配器
 * Created by Angle on 2016/9/25.
 */
public class MyStampViewPagerAdapter extends PagerAdapter {
    private List<View> mList;


    public MyStampViewPagerAdapter(List<View> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList.get(position));
        return (mList.get(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
