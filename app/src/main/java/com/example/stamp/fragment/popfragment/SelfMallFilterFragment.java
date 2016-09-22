package com.example.stamp.fragment.popfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;
import com.example.stamp.adapter.StampTapFilterGridViewAdapter;
import com.example.stamp.adapter.StampTapFilterThemeGridViewAdapter;
import com.example.stamp.base.BaseDialogFragment;
import com.example.stamp.listener.GridViewOnItemClickListener;
import com.example.stamp.listener.GridViewThemeOnItemClickListener;
import com.example.stamp.view.NoScrollGridView;

/**
 * 商城页面筛选Fragment（自营商城，第三方商家）
 */
public class SelfMallFilterFragment extends BaseDialogFragment {

    private String[]  arrClass= {"人物", "植物", "节日", "器皿", "字画", "风光", "经济", "农业", "民俗", "动物", "生肖",
            "文化艺术", "政治", "科教", "工业", "交通", "军事", "文学", "邮政", "公共"};
    private String[] arrYear = {"2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005"};
    private String[] arrPopson= {"任平", "于平", "甲钴胺", "邮票","任平", "于平", "甲钴胺", "邮票","任平", "于平", "甲钴胺", "邮票"};

    private View mFilterView;
    private int Current = -1;//当前选择的年代角标
    private GridViewOnItemClickListener mYearListener, mCategoryListener;//年份的监听,类别的监听
    private GridViewThemeOnItemClickListener mThemeListener;// 人物的监听
    private View mSelfMall;

//    public SelfMallFilterFragment(String[] arr,String[] arrs,String[] arres) {
//        this.arr = arr;
//        this.arrs = arrs;
//        this.arres = arres;
//    }

    public SelfMallFilterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSelfMall = View.inflate(getActivity(), R.layout.fragment_selfmall, null);
        //设置GridView的数据
        setPopupWindowData();
//Log.e("",arr.toString()+"--"+arrs+"---"+arres);
//        RequestNet();
        return mSelfMall;
    }

    /**
     * 设置PopupWindow页面的数据
     */
    private void setPopupWindowData() {
        //年代的GridView
        NoScrollGridView mYearGV = (NoScrollGridView) mSelfMall.findViewById(R.id.selfmall_pop_years);
        //类别的GridView
        NoScrollGridView mCategoryGV = (NoScrollGridView) mSelfMall.findViewById(R.id.selfmall_pop_category);
        //题材的GridView
        NoScrollGridView mThemeGV = (NoScrollGridView) mSelfMall.findViewById(R.id.selfmall_pop_theme);

        //创建适配器
        StampTapFilterGridViewAdapter mYearAdapter = new StampTapFilterGridViewAdapter(getActivity(), arrClass);
        StampTapFilterGridViewAdapter mCategoryAdapter = new StampTapFilterGridViewAdapter(getActivity(), arrYear);
        StampTapFilterThemeGridViewAdapter mThemeAdapter = new StampTapFilterThemeGridViewAdapter(getActivity(), arrPopson);

        //设置适配器
        mYearGV.setAdapter(mYearAdapter);
        mCategoryGV.setAdapter(mCategoryAdapter);
        mThemeGV.setAdapter(mThemeAdapter);

        //设置监听
        //年份的监听
        mYearListener = new GridViewOnItemClickListener(Current, mYearAdapter);
        mYearGV.setOnItemClickListener(mYearListener);
        //类别的监听
        mCategoryListener = new GridViewOnItemClickListener(Current, mCategoryAdapter);
        mCategoryGV.setOnItemClickListener(mCategoryListener);
        //题材的监听
        mThemeListener = new GridViewThemeOnItemClickListener(Current, mThemeAdapter);
        mThemeGV.setOnItemClickListener(mThemeListener);

        //重置按钮
       /* mFilterView.findViewById(R.id.stamptap_pop_resetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYearListener.setPosition();
                mCategoryListener.setPosition();
                mThemeListener.setPosition();
            }
        });
        //确定按钮
        mFilterView.findViewById(R.id.stamptap_pop_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //所有PopupWindow选择的内容
                MyLog.e(mYearListener.getPosition() + "_" + mCategoryListener.getPosition() + "_" + mThemeListener.getPosition());
                SelfMallFilterFragment.this.dismiss();
            }
        });*/
    }
}
