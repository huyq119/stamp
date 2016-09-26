package cn.com.chinau.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.com.chinau.fragment.stampfragment.StampInfoFragment;
import cn.com.chinau.fragment.stampfragment.StampPracticeFragment;
import cn.com.chinau.fragment.stampfragment.StampPriceFragment;

import java.util.List;

/**
 * 邮票目录详情Fragment的适配器
 * Created by Administrator on 2016/8/4.
 */
public class StampTapDetailAdapter extends FragmentPagerAdapter {


    private List<Fragment> mList;//内容Fragment集合
    private String[] arr;//标题数组
    private String mDetail, mStory;

    public StampTapDetailAdapter(FragmentManager fragmentManager, List<Fragment> list, String[] arr, String mDetail, String mStory) {
        super(fragmentManager);
        mList = list;
        this.arr = arr;
        this.mDetail = mDetail;
        this.mStory = mStory;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new StampInfoFragment(mDetail);
        } else if (position == 1) {
            return new StampPriceFragment();
        } else if (position == 2) {
            return new StampPracticeFragment(mStory);
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arr[position];
    }
}
