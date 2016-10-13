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
import cn.com.chinau.bean.CategoryDSFBean;
import cn.com.chinau.dialog.SelfMallPanStampFilterDialog;
import cn.com.chinau.listener.SellMallPanStampGridViewOnItemClickListener;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.view.NoScrollGridView;

/**
 * 商城筛选（第三方商家）Fragment
 */
public class ThreeMallFilterFragment extends BaseDialogFragment implements SellMallPanStampGridViewOnItemClickListener.SelfMallItemClick{


    private int Current = -1;//当前选择的年代角标
    private SellMallPanStampGridViewOnItemClickListener mYearListener, mCategoryListener,mThemeListener;//年份的监听,类别的监听,人物的监听
    private View mSelfMall;
    private String mData,mCategorys,mYearses;
    private SharedPreferences sp;
    private String[] mTitle;
    private String[] mArrTitle0;
    private String[] mArrTitle1;
    private TextView mCategory,mYears;


    public ThreeMallFilterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSelfMall = View.inflate(getActivity(), R.layout.fragment_threemall_filter_dialog, null);

        sp = getActivity().getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);

        GetCategory(); // 获取保存在本地的类别数据
        setPopupWindowData(); //设置GridView的数据

        return mSelfMall;
    }

    /**
     * 获取保存在本地的类别数据
     */
    private void GetCategory(){
        String mCategory = sp.getString("Category4","");
        Gson gson = new Gson();
        CategoryDSFBean mCategoryDSFBean3 = gson.fromJson(mCategory, CategoryDSFBean.class);
        ArrayList<CategoryDSFBean.Category> CategoryList =  mCategoryDSFBean3.getCategory();

        mTitle = new String[CategoryList.size()];
        for (int i = 0; i < CategoryList.size(); i++) {
            mTitle[i] = CategoryList.get(i).getName();
            MyLog.LogShitou("第三方1级mTitle", mTitle[i]);
        }
        mCategorys = mTitle[0];
        mYearses = mTitle[0];


        CategoryDSFBean.Category SubCategory0 = CategoryList.get(0);
        CategoryDSFBean.Category SubCategory1 = CategoryList.get(1);
//        CategoryRMBean.Category SubCategory2 = list.get(2);
        ArrayList<CategoryDSFBean.Category.SubCategory> subCategory0 = SubCategory0.getSubCategory();
        ArrayList<CategoryDSFBean.Category.SubCategory> subCategory1 = SubCategory1.getSubCategory();
//        ArrayList<CategoryRMBean.Category.SubCategory> subCategory2 = SubCategory2.getSubCategory();

        int sub0 = subCategory0.size();// 获取subCategory0的个数
        int sub1 = subCategory1.size();// 获取subCategory1的个数
//        int sub2 = subCategory2.size();// 获取subCategory2的个数
        mArrTitle0 = new String[sub0];// 一级分类
        mArrTitle1 = new String[sub1];// 一级分类
//        mArrTitle2 = new String[sub2];// 一级分类

        for (int i = 0; i < subCategory0.size(); i++) {
            mArrTitle0[i] = subCategory0.get(i).getName();
            MyLog.LogShitou("2级Title1", mArrTitle0[i]);
        }
        for (int i = 0; i < subCategory1.size(); i++) {
            mArrTitle1[i] = subCategory1.get(i).getName();
            MyLog.LogShitou("2级Title1", mArrTitle1[i]);
        }

//        for (int i = 0; i < subCategory2.size(); i++) {
//            mArrTitle2[i] = subCategory2.get(i).getName();
//            MyLog.LogShitou("2级Title1", mArrTitle2[i]);
//        }


    }

    /**
     * 设置PopupWindow页面的数据
     */
    private void setPopupWindowData() {
        mCategory =(TextView) mSelfMall.findViewById(R.id.threemall_title_pop_category);
        mCategory.setText(mCategorys);
      mYears =(TextView) mSelfMall.findViewById(R.id.threemall_title_pop_years);
        mYears.setText(mYearses);

        //类别的GridView
        NoScrollGridView mCategoryGV = (NoScrollGridView) mSelfMall.findViewById(R.id.threemall_pop_category);
        //年代的GridView
        NoScrollGridView mYearGV = (NoScrollGridView) mSelfMall.findViewById(R.id.threemall_pop_years);
        //题材的GridView
//        NoScrollGridView mThemeGV = (NoScrollGridView) mSelfMall.findViewById(R.id.selfmall_pop_theme);

        //创建适配器
        SelfMallPanStampGridViewAdapter mCategoryAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mArrTitle0);
        SelfMallPanStampGridViewAdapter mYearAdapter =  new SelfMallPanStampGridViewAdapter(getActivity(), mArrTitle1);
//        SelfMallPanStampGridViewAdapter mThemeAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), arrPerson);

        //设置适配器
        mCategoryGV.setAdapter(mCategoryAdapter);
        mYearGV.setAdapter(mYearAdapter);
//        mThemeGV.setAdapter(mThemeAdapter);

        //设置监听
        //类别的监听
        mCategoryListener = new SellMallPanStampGridViewOnItemClickListener(Current, mCategoryAdapter);
        mCategoryListener.setSelfMallItemClick(this);
        mCategoryGV.setOnItemClickListener(mCategoryListener);
        //年份的监听
        mYearListener = new SellMallPanStampGridViewOnItemClickListener(Current, mYearAdapter);
        mYearListener.setSelfMallItemClick(this);
        mYearGV.setOnItemClickListener(mYearListener);
        //题材的监听
//        mThemeListener = new SellMallPanStampGridViewOnItemClickListener(Current, mThemeAdapter);
//        mThemeListener.setSelfMallItemClick(this);
//        mThemeGV.setOnItemClickListener(mThemeListener);

        SelfMallPanStampFilterDialog selfMallPanStampFilterDialog = (SelfMallPanStampFilterDialog) getParentFragment();
        selfMallPanStampFilterDialog.setClickReset(new SelfMallPanStampFilterDialog.ClickReset() {
            @Override
            public void setReset() {
                MyLog.e("ThreeMallFilterFragment-->");
                mYearListener.setPosition();
                mCategoryListener.setPosition();
//                mThemeListener.setPosition();
            }
        });
    }

    @Override
    public void GetClickItem() {
        int yearNum = mYearListener.getPosition();
        int CategoryNum = mCategoryListener.getPosition();
//        int PersonNum = mThemeListener.getPosition();
        MyLog.e(CategoryNum + "-" + yearNum);


        String Category = (CategoryNum == -1) ? "" :mArrTitle0[CategoryNum];
        String Year = (yearNum == -1) ? "" : mArrTitle1[yearNum];
//        String Person = (PersonNum == -1) ? "" : arrPerson[PersonNum];

        mData = Category + "," + Year;
        setData(mData);
        MyLog.e("点击了啥002--->"+mData);

    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

}
