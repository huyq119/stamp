package cn.com.chinau.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;


/**
 * 商品订单ViewPager适配器
 *
 * @author Administrator
 */
public class OrderGoodsPagerAdapter extends PagerAdapter {

    private List<View> list;
    public OrderGoodsPagerAdapter(List<View> list){
        this.list=list;
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
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager)container).removeView(list.get(position));
    }
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(list.get(position));
        return list.get(position);
    }



}
