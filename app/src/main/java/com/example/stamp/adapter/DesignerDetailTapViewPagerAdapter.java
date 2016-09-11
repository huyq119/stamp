package com.example.stamp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.fragment.designerdetailfragment.DesignerResumeFragment;
import com.example.stamp.fragment.designerdetailfragment.DesignerStoryFragment;
import com.example.stamp.fragment.designerdetailfragment.DesignerWorksFragment;
import com.example.stamp.fragment.designerdetailfragment.DesigneriViewFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 * 设计家详情viewPager适配器
 */
public class DesignerDetailTapViewPagerAdapter extends PagerAdapter {

    private List<View> list;
    private String[] arr;//标题数组
    public DesignerDetailTapViewPagerAdapter(List<View> list,String[] arr){
        this.list=list;
        this.arr = arr;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0==arg1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(list.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(list.get(position));
        return list.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arr[position];
    }

}

