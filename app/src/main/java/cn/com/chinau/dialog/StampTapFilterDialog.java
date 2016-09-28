package cn.com.chinau.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.StampTapFilterGridViewAdapter;
import cn.com.chinau.adapter.StampTapFilterThemeGridViewAdapter;
import cn.com.chinau.base.BaseDialogFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.listener.GridViewOnItemClickListener;
import cn.com.chinau.listener.GridViewThemeOnItemClickListener;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.NoScrollGridView;

/**
 * (邮票目录)筛选弹出框
 * Created by Administrator on 2016/8/2.
 */
public class StampTapFilterDialog extends BaseDialogFragment {

    private String[] arr;
    private String[] arrs;
    private String[] arres;
    private View mFilterView;
    private int Current = -1;//当前选择的年代角标
    private GridViewOnItemClickListener mYearListener, mCategoryListener;//年份的监听,类别的监听
    private GridViewThemeOnItemClickListener mThemeListener;// 题材的监听


    public StampTapFilterDialog(String[] arr,String[] arrs,String[] arres) {
        this.arr = arr;
        this.arrs = arrs;
        this.arres = arres;


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建显示的View
        mFilterView = CreateBaseView(inflater, R.layout.stamptap_dialog);

        //设置GridView的数据
        setPopupWindowData();
//        RequestNet();
        return mFilterView;
    }

    /**
     * 请求网络
     */
    private void RequestNet() {
        //请求类别信息
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
                params.put(StaticField.OP_TYPE, StaticField.ML);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code);
                //这里请求的是筛选的布局
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.e(result);
            }
        });
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