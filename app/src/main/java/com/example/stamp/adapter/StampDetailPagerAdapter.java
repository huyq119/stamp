package com.example.stamp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.stamp.fragment.stampdetailfragment.StampDetailInfoFragment;

import java.util.List;


/**
 * 主页ViewPager适配器
 *
 * @author Administrator
 */
public class StampDetailPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;//内容Fragment集合
    private String[] arr;//标题数组
//    private Context context;
//    private String detail;

    public StampDetailPagerAdapter(FragmentManager fragmentManager, List<Fragment> list, String[] arr) {
        super(fragmentManager);
        mList = list;
        this.arr = arr;
//        this.context = context;
//        this.detail = detail;
    }

    @Override
    public Fragment getItem(int arg0) {
//        if (arg0 == 0) {// 我的页面
//            return new HomeFragment();
//        } else if (arg0 == 1) {// 行情
//            return new StampTapFragment();
//        } else if (arg0 == 2) {// 订单
//            return new PanStampFragment();
//        } else if (arg0 == 3) {// 订单
//            return new ShopFragment();
//        } else if (arg0 == 4) {// 我的
//            return new MyFragment();
//        }

//        return null;

        return new StampDetailInfoFragment();
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
