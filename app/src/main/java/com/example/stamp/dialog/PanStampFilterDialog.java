package com.example.stamp.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;
import com.example.stamp.adapter.PanStampDialogPagerAdapter;
import com.example.stamp.base.BaseDialogFragment;
import com.example.stamp.fragment.popfragment.SelfMallFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选对话框
 * Created by Administrator on 2016/8/2.
 */
public class PanStampFilterDialog extends BaseDialogFragment {

    private List<Fragment> mList;
    private String[] arr = {"自营商城", "第三方商家", "邮市", "竞拍"};
    private List<Fragment> mPopupList;//展示PopupWindow页面的Fragment的集合


    public PanStampFilterDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建显示的View
        View mFilterView = CreateBaseView(inflater, R.layout.base_viewpagerindicator);


        //初始化集合
        mPopupList = new ArrayList<>();

        SelfMallFragment mallFragment = new SelfMallFragment();

        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);

        //初始化控件
        FragmentPagerAdapter adapter = new PanStampDialogPagerAdapter(getChildFragmentManager(), mList, arr);
        ViewPager pager = (ViewPager) mFilterView.findViewById(R.id.baseStamp_vp);
        pager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator) mFilterView.findViewById(R.id.baseStamp_indicator);
        indicator.setViewPager(pager);

        return mFilterView;
    }


}
