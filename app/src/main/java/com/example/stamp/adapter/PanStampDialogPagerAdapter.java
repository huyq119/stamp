package com.example.stamp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.stamp.fragment.popfragment.SelfMallFilterFragment;

import java.util.List;


/**
 * 主页ViewPager适配器
 *
 * @author Administrator
 */
public class PanStampDialogPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;//内容Fragment集合
    private String[] arr;//标题数组

    public PanStampDialogPagerAdapter(FragmentManager fragmentManager, List<Fragment> list, String[] arr) {
        super(fragmentManager);
        mList = list;
        this.arr = arr;
    }

    @Override
    public Fragment getItem(int arg0) {
//        if (arg0 == 0) {// 我的页面
//            return new SelfMallFilterFragment(arrClass,arrYear,arrPopson);
//        } else if (arg0 == 1) {// 行情
//            return new StampTapFragment();
//        }

//        return null;

       return new SelfMallFilterFragment();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arr[position];
    }


}
