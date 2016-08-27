package com.example.stamp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.stamp.fragment.designerdetailfragment.DesignerResumeFragment;
import com.example.stamp.fragment.designerdetailfragment.DesignerStoryFragment;
import com.example.stamp.fragment.designerdetailfragment.DesignerWorksFragment;
import com.example.stamp.fragment.designerdetailfragment.DesigneriViewFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 * 设计家详情viewPager适配器
 */
public class DesignerDetailTapViewPagerAdapter extends FragmentPagerAdapter {


    private List<Fragment> mList;//内容Fragment集合
    private String[] arr;//标题数组

    public DesignerDetailTapViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> list, String[] arr) {
        super(fragmentManager);
        mList = list;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new DesignerResumeFragment();
        } else if (position == 1) {
            return new DesignerStoryFragment();
        } else if (position == 2) {
            return new DesignerWorksFragment();
        } else if (position == 3) {
            return new DesigneriViewFragment();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arr[position];
    }
}

