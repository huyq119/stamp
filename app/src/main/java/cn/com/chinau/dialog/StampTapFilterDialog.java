package cn.com.chinau.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.adapter.StampTapFilterGridViewAdapter;
import cn.com.chinau.adapter.StampTapFilterThemeGridViewAdapter;
import cn.com.chinau.base.BaseDialogFragment;
import cn.com.chinau.listener.GridViewOnItemClickListener;
import cn.com.chinau.listener.GridViewThemeOnItemClickListener;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.view.NoScrollGridView;

/**
 * (邮票目录)筛选弹出框
 * Created by Administrator on 2016/8/2.
 */
public class StampTapFilterDialog extends BaseDialogFragment {

    private String[] title,arr,arrs,arres;

    private View mFilterView;
    private int Current = -1;//当前选择的年代角标
    private GridViewOnItemClickListener mYearListener, mCategoryListener;//年份的监听,类别的监听
    private GridViewThemeOnItemClickListener mThemeListener;// 题材的监听
    private TextView mYears,mCategory,mTheme;


    public StampTapFilterDialog(String[] title,String[] arr,String[] arrs,String[] arres) {
        this.title = title;
        this.arr = arr;
        this.arrs = arrs;
        this.arres = arres;


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建显示的View
        mFilterView = CreateBaseView(inflater, R.layout.stamptap_dialog);
        initView();
        initData();
        //设置GridView的数据
        setPopupWindowData();
//        RequestNet();
        return mFilterView;
    }

    private void initData(){
      String title1 =  title[0];
        mYears.setText(title1);
        String title2 =  title[1];
        mCategory.setText(title2);
        String title3 =  title[2];
        mTheme.setText(title3);
    }

    private void initView(){
      mYears = (TextView) mFilterView.findViewById(R.id.pop_title_years);
      mCategory = (TextView) mFilterView.findViewById(R.id.pop_title_category);
      mTheme = (TextView) mFilterView.findViewById(R.id.pop_title_theme);
    }

    /**
     * 设置PopupWindow页面的数据
     */
    private void setPopupWindowData() {
        //年代的GridView
        NoScrollGridView mYearGV = (NoScrollGridView) mFilterView.findViewById(R.id.stamptap_pop_years);
        //类别的GridView
        NoScrollGridView mCategoryGV = (NoScrollGridView) mFilterView.findViewById(R.id.stamptap_pop_category);
        //题材的GridView
        NoScrollGridView mThemeGV = (NoScrollGridView) mFilterView.findViewById(R.id.stamptap_pop_theme);

        //创建适配器
        StampTapFilterGridViewAdapter mYearAdapter = new StampTapFilterGridViewAdapter(getActivity(), arr);
        StampTapFilterGridViewAdapter mCategoryAdapter = new StampTapFilterGridViewAdapter(getActivity(), arrs);
        StampTapFilterThemeGridViewAdapter mThemeAdapter = new StampTapFilterThemeGridViewAdapter(getActivity(), arres);

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
        mFilterView.findViewById(R.id.stamptap_pop_resetting).setOnClickListener(new View.OnClickListener() {
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
                StampTapFilterDialog.this.dismiss();
            }
        });
    }

}
