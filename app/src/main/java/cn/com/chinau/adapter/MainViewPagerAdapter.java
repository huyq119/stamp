package cn.com.chinau.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.com.chinau.fragment.HomeFragment;
import cn.com.chinau.fragment.MyFragment;
import cn.com.chinau.fragment.PanStampFragment;
import cn.com.chinau.fragment.ShopFragment;
import cn.com.chinau.fragment.StampTapFragment;

import java.util.List;


/**
 * 主页ViewPager适配器
 *
 * @author Administrator
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;

    public MainViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> list) {
        super(fragmentManager);
        mList = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        if (arg0 == 0) {// 我的页面
            return new HomeFragment();
        } else if (arg0 == 1) {// 行情
            return new StampTapFragment();
        } else if (arg0 == 2) {// 订单
            return new PanStampFragment();
        } else if (arg0 == 3) {// 订单
            return new ShopFragment();
        } else if (arg0 == 4) {// 我的
            return new MyFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

}
