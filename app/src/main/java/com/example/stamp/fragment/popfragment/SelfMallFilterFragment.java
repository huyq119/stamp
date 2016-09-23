package com.example.stamp.fragment.popfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;
import com.example.stamp.adapter.StampTapFilterGridViewAdapter;
import com.example.stamp.base.BaseDialogFragment;
import com.example.stamp.dialog.SelfMallFilterDialog;
import com.example.stamp.listener.GridViewOnItemClickListener;
import com.example.stamp.utils.MyLog;
import com.example.stamp.view.NoScrollGridView;

/**
 * 商城页面筛选Fragment（自营商城，第三方商家）
 */
public class SelfMallFilterFragment extends BaseDialogFragment implements GridViewOnItemClickListener.SelfMallItemClick {

    private String[] arrClass = {"人物", "植物", "节日", "器皿", "字画", "风光", "经济", "农业", "民俗", "动物", "生肖",
            "文化艺术", "政治", "科教", "工业", "交通", "军事", "文学", "邮政", "公共"};
    private String[] arrYear = {"2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005"};
    private String[] arrPerson = {"任平", "于平", "甲钴胺", "邮票", "任平", "于平", "甲钴胺", "邮票", "任平", "于平", "甲钴胺", "邮票"};

    private View mFilterView;
    private int Current = -1;//当前选择的年代角标
    private GridViewOnItemClickListener mYearListener, mCategoryListener;//年份的监听,类别的监听
    private GridViewOnItemClickListener mThemeListener;// 人物的监听
    private View mSelfMall;
    private String mData;


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
        StampTapFilterGridViewAdapter mYearAdapter =  new StampTapFilterGridViewAdapter(getActivity(), arrClass);
        StampTapFilterGridViewAdapter mCategoryAdapter = new StampTapFilterGridViewAdapter(getActivity(), arrYear);
        StampTapFilterGridViewAdapter mThemeAdapter = new StampTapFilterGridViewAdapter(getActivity(), arrPerson);

        //设置适配器
        mYearGV.setAdapter(mYearAdapter);
        mCategoryGV.setAdapter(mCategoryAdapter);
        mThemeGV.setAdapter(mThemeAdapter);

        //设置监听
        //年份的监听
        mYearListener = new GridViewOnItemClickListener(Current, mYearAdapter);
        mYearListener.setSelfMallItemClick(this);
        mYearGV.setOnItemClickListener(mYearListener);
        //类别的监听
        mCategoryListener = new GridViewOnItemClickListener(Current, mCategoryAdapter);
        mCategoryListener.setSelfMallItemClick(this);
        mCategoryGV.setOnItemClickListener(mCategoryListener);
        //题材的监听
        mThemeListener = new GridViewOnItemClickListener(Current, mThemeAdapter);
        mThemeListener.setSelfMallItemClick(this);
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

        SelfMallFilterDialog selfMallFilterDialog = (SelfMallFilterDialog) getParentFragment();
        selfMallFilterDialog.setClickReset(new SelfMallFilterDialog.ClickReset() {
            @Override
            public void setReset() {
                mYearListener.setPosition();
                mCategoryListener.setPosition();
                mThemeListener.setPosition();
            }
        });
    }

    @Override
    public void GetClickItem() {
        int yearNum = mYearListener.getPosition();
        int CategoryNum = mCategoryListener.getPosition();
        int PersonNum = mThemeListener.getPosition();
        MyLog.e(yearNum + "-" + CategoryNum + "-" + PersonNum);


        String Year = (yearNum == -1) ? "" : arrClass[yearNum];
        String Category = (CategoryNum == -1) ? "" : arrYear[CategoryNum];
        String Person = (PersonNum == -1) ? "" : arrPerson[PersonNum];

        mData = Year + "," + Category + "," + Person;
        setData(mData);
        MyLog.e(mData);

    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }


}
