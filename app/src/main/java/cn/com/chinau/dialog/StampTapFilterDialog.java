package cn.com.chinau.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.adapter.StampTapFilterGridViewAdapter;
import cn.com.chinau.adapter.StampTapFilterThemeGridViewAdapter;
import cn.com.chinau.base.BaseDialogFragment;
import cn.com.chinau.bean.CategoryJsonBean;
import cn.com.chinau.bean.CategoryMLBean;
import cn.com.chinau.listener.GridViewOnItemClickListener;
import cn.com.chinau.listener.GridViewThemeOnItemClickListener;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.view.NoScrollGridView;

/**
 * (邮票目录)筛选弹出框
 * Created by Administrator on 2016/8/2.
 */
@SuppressLint("ValidFragment")
public class StampTapFilterDialog extends BaseDialogFragment {

    private String category;

    private View mFilterView;
    private int Current = -1;//当前选择的年代角标
    private GridViewOnItemClickListener mYearListener, mCategoryListener;//年份的监听,类别的监听
    private GridViewThemeOnItemClickListener mThemeListener;// 题材的监听
    private TextView mYears, mCategory, mTheme; // 年代，类别，题材
    private JsonData mJsonData;
    private String[] mTitle, mArrTitle0, mArrTitle1, mArrTitle2,mArrValue0,mArrValue1,mArrValue2;

    public StampTapFilterDialog(String category) {
        this.category = category;

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
        return mFilterView;
    }


    private void initData() {
        // 获取串过来的字符串象category
        if (!category.equals("")) {
            Gson gson = new Gson();
            CategoryMLBean mCategoryRMBean = gson.fromJson(category, CategoryMLBean.class);
            ArrayList<CategoryMLBean.Category> list = mCategoryRMBean.getCategory();

            mTitle = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                mTitle[i] = list.get(i).getName();
                MyLog.LogShitou("标题mTitle", mTitle[i]);
            }
            CategoryMLBean.Category SubCategory0 = list.get(0);
            CategoryMLBean.Category SubCategory1 = list.get(1);
            CategoryMLBean.Category SubCategory2 = list.get(2);
            ArrayList<CategoryMLBean.Category.SubCategory> subCategory0 = SubCategory0.getSubCategory();
            ArrayList<CategoryMLBean.Category.SubCategory> subCategory1 = SubCategory1.getSubCategory();
            ArrayList<CategoryMLBean.Category.SubCategory> subCategory2 = SubCategory2.getSubCategory();

            int sub0 = subCategory0.size();// 获取subCategory0的个数
            int sub1 = subCategory1.size();// 获取subCategory1的个数
            int sub2 = subCategory2.size();// 获取subCategory2的个数
            mArrTitle0 = new String[sub0];// 一级分类name
            mArrValue0 = new String[sub0];// 一级分类value

            mArrTitle1 = new String[sub1];// 一级分类name
            mArrValue1 = new String[sub1];// 一级分类value

            mArrTitle2 = new String[sub2];// 一级分类name
            mArrValue2 = new String[sub2];// 一级分类value

            for (int i = 0; i < subCategory0.size(); i++) {
                mArrTitle0[i] = subCategory0.get(i).getName();
                mArrValue0[i] = subCategory0.get(i).getValue();
//                MyLog.LogShitou("年代2级Title0==Value值", mArrTitle0[i]+"==="+mArrValue0[i] );
            }
            for (int i = 0; i < subCategory1.size(); i++) {
                mArrTitle1[i] = subCategory1.get(i).getName();
                mArrValue1[i] = subCategory1.get(i).getValue();
//                MyLog.LogShitou("类别2级Title1==Value值", mArrTitle1[i]+"=="+mArrValue1[i]);
            }
            for (int i = 0; i < subCategory2.size(); i++) {
                mArrTitle2[i] = subCategory2.get(i).getName();
                mArrValue2[i] = subCategory2.get(i).getValue();
//                MyLog.LogShitou("题材2级Title2==Value值", mArrTitle2[i]+"=="+mArrValue2[i]);
            }
        }

        // 获取一级标题数据并赋值
        String title1 = mTitle[0];
        mYears.setText(title1);

        String title2 = mTitle[1];
        mCategory.setText(title2);

        String title3 = mTitle[2];
        mTheme.setText(title3);
    }

    private void initView() {
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
        StampTapFilterGridViewAdapter mYearAdapter = new StampTapFilterGridViewAdapter(getActivity(),mArrTitle0);
        StampTapFilterGridViewAdapter mCategoryAdapter = new StampTapFilterGridViewAdapter(getActivity(),  mArrTitle1);
        StampTapFilterThemeGridViewAdapter mThemeAdapter = new StampTapFilterThemeGridViewAdapter(getActivity(), mArrTitle2);

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
        mFilterView.findViewById(R.id.stamptap_pop_ensure);
        mFilterView.setOnClickListener(new View.OnClickListener() {

            private String mData;

            @Override
            public void onClick(View view) {
                //所有PopupWindow选择的内容

                int yearNum = mYearListener.getPosition();
                int CategoryNum = mCategoryListener.getPosition();
                int PersonNum = mThemeListener.getPosition();

//                MyLog.LogShitou("邮票目录筛选点击了啥Position", yearNum + "_" + CategoryNum + "_" + PersonNum);

                String year = (yearNum == -1) ? "" : mArrValue0[yearNum];
                String category = (CategoryNum == -1) ? "" : mArrValue1[CategoryNum];
                String theme = (PersonNum == -1) ? "" : mArrValue2[PersonNum];

                mData = year + "," + category + "," + theme;
//                setData(mData);
                MyLog.LogShitou("筛选点击了啥mData",mData);
                // 装在实体类
                CategoryJsonBean mJsonBean = new CategoryJsonBean();
                mJsonBean.setCategory(category);
                mJsonBean.setYear(year);
                mJsonBean.setTheme(theme);
                // 转换成Json串
                String  tojson = new Gson().toJson(mJsonBean);
                MyLog.LogShitou("转换成的Json", tojson);

                mJsonData.GetJsonData(tojson);// 给接口赋值需要传的值

                StampTapFilterDialog.this.dismiss();

            }
        });
    }

    // 定义一个接口给Fragment
    public interface JsonData{
        void GetJsonData(String tojson);
    }

    public void setmJsonData(JsonData mJsonData) {
        this.mJsonData = mJsonData;
    }

}
