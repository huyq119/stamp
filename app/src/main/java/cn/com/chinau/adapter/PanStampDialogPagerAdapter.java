package cn.com.chinau.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
       return mList.get(arg0);
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
