package cn.com.chinau.fragment.popfragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.SelfMallPanStampGridViewAdapter;
import cn.com.chinau.base.BaseDialogFragment;
import cn.com.chinau.bean.CategoryGoodsJsonBean;
import cn.com.chinau.bean.CategorySCBean;
import cn.com.chinau.dialog.SelfMallPanStampFilterDialog;
import cn.com.chinau.listener.SellMallPanStampGridViewOnItemClickListener;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.view.NoScrollGridView;

/**
 * 商城筛选（自营商城）Fragment
 */
public class SelfMallFilterFragment extends BaseDialogFragment implements SellMallPanStampGridViewOnItemClickListener.SelfMallItemClick {

    private View mFilterView;
    private int Current = -1;//当前选择的年代角标
    private SellMallPanStampGridViewOnItemClickListener mYearListener, mCategoryListener, mThemeListener;//年份的监听,类别的监听,人物的监听

    private View mSelfMall;
    private String mData;
    private SharedPreferences sp;
    private String[] mTitle, mArrTitle0, mArrTitle1, mArrTitle2, mArrValue0, mArrValue1, mArrValue2;
    private TextView mYears, mCategorys, mTheme; // 年代，类别，人物
    private String CategoryValue;
    private String mToJson;

    public SelfMallFilterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSelfMall = View.inflate(getActivity(), R.layout.fragment_selfmall_filter_dialog, null);
        sp = getActivity().getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        GetCategory();
        //设置GridView的数据
        setPopupWindowData();
        return mSelfMall;
    }

    private void initView() {
        mYears = (TextView) mSelfMall.findViewById(R.id.selfmall_pop_category_title);
        mCategorys = (TextView) mSelfMall.findViewById(R.id.selfmall_pop_year_title);
        mTheme = (TextView) mSelfMall.findViewById(R.id.selfmall_pop_theme_title);
    }

    /**
     * 获取保存在本地的自营商城类别数据
     */
    private void GetCategory() {
        String mCategory = sp.getString("Category3", "");
        if (!mCategory.equals("")) {
            MyLog.LogShitou("========获取本地类别商城数据", mCategory);
            Gson gson = new Gson();
            CategorySCBean mCategoryDSFBean3 = gson.fromJson(mCategory, CategorySCBean.class);
            ArrayList<CategorySCBean.Category> categoryList = mCategoryDSFBean3.getCategory();
//            MyLog.LogShitou("=====类别商城数据个数", "size===========" + categoryList.size());

            mTitle = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                mTitle[i] = categoryList.get(i).getName();
//                MyLog.LogShitou("第三方1级mTitle", mTitle[i]);
            }

            CategorySCBean.Category SubCategory0 = categoryList.get(0);
            CategorySCBean.Category SubCategory1 = categoryList.get(1);
            CategorySCBean.Category SubCategory2 = categoryList.get(2);

            ArrayList<CategorySCBean.Category.SubCategoryOne> subCategory0 = SubCategory0.getSubCategory();
            ArrayList<CategorySCBean.Category.SubCategoryOne> subCategory1 = SubCategory1.getSubCategory();
            ArrayList<CategorySCBean.Category.SubCategoryOne> subCategory2 = SubCategory2.getSubCategory();

            int sub0 = subCategory0.get(0).getSubCategory().size();// 获取subCategory0的个数
            int sub1 = subCategory1.size();// 获取subCategory1的个数
            int sub2 = subCategory2.size();// 获取subCategory2的个数

            mArrTitle0 = new String[sub0];// 一级分类name
            mArrValue0 = new String[sub0];// 一级分类value

            mArrTitle1 = new String[sub1];// 一级分类name
            mArrValue1 = new String[sub1];// 一级分类value

            mArrTitle2 = new String[sub2];// 一级分类name
            mArrValue2 = new String[sub2];// 一级分类value

            for (int i = 0; i < subCategory0.size(); i++) {
                CategoryValue = subCategory0.get(i).getValue();// 一级类别CategoryValue值
                MyLog.LogShitou("类别==Value值", CategoryValue);
            }

            for (int i = 0; i < subCategory0.get(0).getSubCategory().size(); i++) {
                mArrTitle0[i] = subCategory0.get(0).getSubCategory().get(i).getName();
                mArrValue0[i] = subCategory0.get(0).getSubCategory().get(i).getValue();
//                MyLog.LogShitou("年代2级Title0==Value值", mArrTitle0[i] + "===" + mArrValue0[i]);
            }
            for (int i = 0; i < subCategory1.size(); i++) {
                mArrTitle1[i] = subCategory1.get(i).getName();
                mArrValue1[i] = subCategory1.get(i).getValue();
//                MyLog.LogShitou("类别2级Title1==Value值", mArrTitle1[i] + "==" + mArrValue1[i]);
            }
            for (int i = 0; i < subCategory2.size(); i++) {
                mArrTitle2[i] = subCategory2.get(i).getName();
                mArrValue2[i] = subCategory2.get(i).getValue();
//                MyLog.LogShitou("人物2级Title2==Value值", mArrTitle2[i]+"=="+mArrValue2[i]);
            }

        }

        // 获取一级标题数据并赋值
        String title1 = mTitle[0];
        mYears.setText(title1);

        String title2 = mTitle[1];
        mCategorys.setText(title2);

        String title3 = mTitle[2];
        mTheme.setText(title3);

    }

    /**
     * 设置PopupWindow页面的数据
     */
    private void setPopupWindowData() {
        //类别的GridView
        NoScrollGridView mCategoryGV = (NoScrollGridView) mSelfMall.findViewById(R.id.selfmall_pop_category);
        //年代的GridView
        NoScrollGridView mYearGV = (NoScrollGridView) mSelfMall.findViewById(R.id.selfmall_pop_years);
        //人物的GridView
        NoScrollGridView mThemeGV = (NoScrollGridView) mSelfMall.findViewById(R.id.selfmall_pop_theme);

        //创建适配器
        SelfMallPanStampGridViewAdapter mCategoryAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mArrTitle0);
        SelfMallPanStampGridViewAdapter mYearAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mArrTitle1);
        SelfMallPanStampGridViewAdapter mThemeAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mArrTitle2);

        //设置适配器
        mYearGV.setAdapter(mYearAdapter);
        mCategoryGV.setAdapter(mCategoryAdapter);
        mThemeGV.setAdapter(mThemeAdapter);

        //设置监听
        //年份的监听
        mYearListener = new SellMallPanStampGridViewOnItemClickListener(Current, mYearAdapter);
        mYearListener.setSelfMallItemClick(this);
        mYearGV.setOnItemClickListener(mYearListener);
        //类别的监听
        mCategoryListener = new SellMallPanStampGridViewOnItemClickListener(Current, mCategoryAdapter);
        mCategoryListener.setSelfMallItemClick(this);
        mCategoryGV.setOnItemClickListener(mCategoryListener);
        //题材的监听
        mThemeListener = new SellMallPanStampGridViewOnItemClickListener(Current, mThemeAdapter);
        mThemeListener.setSelfMallItemClick(this);
        mThemeGV.setOnItemClickListener(mThemeListener);


        SelfMallPanStampFilterDialog selfMallPanStampFilterDialog = (SelfMallPanStampFilterDialog) getParentFragment();
        selfMallPanStampFilterDialog.setClickReset(new SelfMallPanStampFilterDialog.ClickReset() {
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

//        MyLog.LogShitou("筛选点击的位置Position", CategoryNum + "-" + yearNum + "-" + PersonNum);

        String Category = (CategoryNum == -1) ? "" : mArrValue0[CategoryNum];
        String Year = (yearNum == -1) ? "" : mArrValue1[yearNum];
        String Person = (PersonNum == -1) ? "" : mArrValue2[PersonNum];

        // 首先判断Category类别是否选中
        if (!Category.equals("")) { // 首先判断Category类别是否选中
            CategoryGoodsJsonBean mGoodsJsonBean = new CategoryGoodsJsonBean();
            if (!Year.equals("")) { // 判断Year年代是否选中
                mGoodsJsonBean.setYear(Year);
            }
            if (!Person.equals("")) { // 判断Person人物是否选中
                mGoodsJsonBean.setDesigner(Person);
            }

            CategoryGoodsJsonBean.CategoryBean mCategoryBean = new CategoryGoodsJsonBean.CategoryBean();
            mCategoryBean.setCategory(CategoryValue);// 一级类别
            CategoryGoodsJsonBean.CategoryBean.SubBean mSubBean = new CategoryGoodsJsonBean.CategoryBean.SubBean();
            mSubBean.setSub(Category);// 二级
            mCategoryBean.setSubCategory(mSubBean);
            mGoodsJsonBean.setCategory(mCategoryBean);

            mToJson = new Gson().toJson(mGoodsJsonBean);

            MyLog.LogShitou("======选中类别的Json串", "mToJson===" + mToJson);
        } else { // 不选中Category类别
            CategoryGoodsJsonBean mGoodsJsonBean = new CategoryGoodsJsonBean();
            if (!Year.equals("")) {  // 判断Year年代是否选中
                mGoodsJsonBean.setYear(Year);
            }
            if (!Person.equals("")) { // 判断Person人物是否选中
                mGoodsJsonBean.setDesigner(Person);
            }
            mToJson = new Gson().toJson(mGoodsJsonBean);

            MyLog.LogShitou("====选中年代或设计家的Json串", "mToJson===" + mToJson);
        }
        // 全选Json格式     {"category":{"category":"274","subCategory":{"sub":"521"}},"designer":"590","year":"640"}
        // 只选类别格式     {"category": {"category": "543","subCategory": {"sub": "550"}}}
        // 只选年代或设计家 {"designer":"590","year":"640"}
        setData(mToJson);
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }


}
