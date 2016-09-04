package com.example.stamp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.stamp.fragment.ordersgoodsfragment.AllFragment;
import com.example.stamp.fragment.ordersgoodsfragment.CompleteFragment;
import com.example.stamp.fragment.ordersgoodsfragment.PaymentFragent;
import com.example.stamp.fragment.ordersgoodsfragment.ReceivingFragment;
import com.example.stamp.fragment.ordersgoodsfragment.RefuseFragment;

import java.util.List;


/**
 * 商品订单ViewPager适配器
 *
 * @author Administrator
 */
public class OrderGoodsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;//内容Fragment集合
    private String[] arr;//标题数组

    public OrderGoodsPagerAdapter(FragmentManager fragmentManager, List<Fragment> list, String[] arr) {
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
            return new AllFragment();// 全部
        } else if (position == 1) {
            return new PaymentFragent(); // 待付款
        } else if (position == 2) {
            return new ReceivingFragment(); // 待收货
        } else if (position == 3) {
            return new CompleteFragment(); // 已完成
        } else if (position == 4) {
            return new RefuseFragment();// 退款/退货
        }
        return null;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arr[position];
    }


}
