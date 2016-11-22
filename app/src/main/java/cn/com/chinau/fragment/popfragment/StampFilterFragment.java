package cn.com.chinau.fragment.popfragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import cn.com.chinau.bean.CategoryBean;
import cn.com.chinau.bean.CategoryGoodsJsonBean;
import cn.com.chinau.dialog.SelfMallPanStampFilterDialog;
import cn.com.chinau.listener.SellMallPanStampGridViewOnItemClickListener;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.view.NoScrollGridView;

/**
 * 淘邮票筛选（邮市）Fragment
 */
public class StampFilterFragment extends BaseDialogFragment implements SellMallPanStampGridViewOnItemClickListener.StampItemClick {


    private int Current = -1;//当前选择的年代角标
    private View mStampView;
    private String mData;
    private TextView mNewChinese, mQingMin, mTheme, mOther;
    private SellMallPanStampGridViewOnItemClickListener mNewChineseListener, mQingMinListener, mThemeListener, mOtherListener;
    private ArrayList<CategoryBean.Category.SubCategory> subCategory1;
    private SharedPreferences sp;
    private String[] mChinese, mQingMins, mThemes, mOthers, mChineseValue, mQingminValue, mThemeValue, mOtherValue;
    private SelfMallPanStampGridViewAdapter mMNewChineseAdapter;
    private SelfMallPanStampGridViewAdapter mMQingMinAdapter;
    private SelfMallPanStampGridViewAdapter mMThemeAdapter;
    private SelfMallPanStampGridViewAdapter mMOtherAdapter;
    private String[] mArryValue;
    private String mToJson;


    public StampFilterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStampView = View.inflate(getActivity(), R.layout.fragment_stamp_filter_dialog, null);
        sp = getActivity().getSharedPreferences(StaticField.NAME, getActivity().MODE_PRIVATE);
        initView();
        GetCategoryData();
        //设置GridView的数据
        setPopupWindowData();
        return mStampView;
    }


    private void initView() {
        mNewChinese = (TextView) mStampView.findViewById(R.id.stamp_pop_newchinese_title);
        mQingMin = (TextView) mStampView.findViewById(R.id.stamp_pop_qingmin_title);
        mTheme = (TextView) mStampView.findViewById(R.id.stamp_pop_theme_title);
        mOther = (TextView) mStampView.findViewById(R.id.stamp_pop_other_title);
    }

    // 获取保存在本地邮市类别数据
    private void GetCategoryData() {
        String category5 = sp.getString("Category5", "");
        MyLog.LogShitou("获取本地邮市类别数据", category5);
        if (category5 != null) {
            Gson gson = new Gson();
            CategoryBean mCategoryBean = gson.fromJson(category5, CategoryBean.class);
            ArrayList<CategoryBean.Category> mCategory = mCategoryBean.getCategory();// 一级标题list
            CategoryBean.Category category = mCategory.get(0);
            subCategory1 = category.getSubCategory();// 二级标题list

            int sub = subCategory1.size();// 获取subCategory1的个数
            MyLog.LogShitou("-==============sub", "sub==" + sub);

            String[] mArrTitle = new String[sub];// 一级分类name
            // 一级分类Value
            mArryValue = new String[sub];
            //二级分类
            ArrayList<String[]> mArrList = new ArrayList<>();
            ArrayList<String[]> mArrListValue = new ArrayList<>();
            // 循环出一级分类的名字
            for (int i = 0; i < subCategory1.size(); i++) {
                mArrTitle[i] = subCategory1.get(i).getName();
                mArryValue[i] = subCategory1.get(i).getValue();

                MyLog.LogShitou("邮市一级类别0001----->:", mArrTitle[i]+"=="+mArryValue[i]);
                ArrayList<CategoryBean.Category.SubCategory.SmllSubCategoryData> subCategory = subCategory1.get(i).getSubCategory();
                String[] mArr = new String[subCategory.size()];
                String[] mArrValue = new String[subCategory.size()];
                // 循环出二级分类名字
                for (int j = 0; j < subCategory.size(); j++) {
                    mArr[j] = subCategory.get(j).getName();
                    mArrValue[j] = subCategory.get(j).getValue();
                    MyLog.LogShitou("邮市二级类别0", mArr[j] + "===" + mArrValue[j]);
                }
                mArrList.add(mArr);
                mArrListValue.add(mArrValue);
            }
            // 获取单个的一级标题Title
            String title0 = mArrTitle[0];
            mNewChinese.setText(title0);
            String title1 = mArrTitle[1];
            mQingMin.setText(title1);
            String title2 = mArrTitle[2];
            mTheme.setText(title2);
            String title3 = mArrTitle[3];
            mOther.setText(title3);
            MyLog.LogShitou("邮市一级类别----->:", title0 + "--" + title1 + "--" + title2 + "--" + title3);

            // 获取二级分类name
            mChinese = mArrList.get(0);
            mQingMins = mArrList.get(1);
            mThemes = mArrList.get(2);
            mOthers = mArrList.get(3);
            // 获取二级分类value值
            mChineseValue = mArrListValue.get(0);
            mQingminValue = mArrListValue.get(1);
            mThemeValue = mArrListValue.get(2);
            mOtherValue = mArrListValue.get(3);

        }
    }


    /**
     * 设置PopupWindow页面的数据
     */
    private void setPopupWindowData() {
        //新中国GridView
        NoScrollGridView mNewChineseGV = (NoScrollGridView) mStampView.findViewById(R.id.stamp_pop_newchinese);
        //清民区GridView
        NoScrollGridView mQingMinGV = (NoScrollGridView) mStampView.findViewById(R.id.stamp_pop_qingmin);
        //主题GridView
        NoScrollGridView mThemeGV = (NoScrollGridView) mStampView.findViewById(R.id.stamp_pop_theme);
        //其他GridView
        NoScrollGridView mOtherGV = (NoScrollGridView) mStampView.findViewById(R.id.stamp_pop_other);

        //创建适配器
        mMNewChineseAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mChinese);
        mMQingMinAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mQingMins);
        mMThemeAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mThemes);
        mMOtherAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mOthers);

        //设置适配器
        mNewChineseGV.setAdapter(mMNewChineseAdapter);
        mQingMinGV.setAdapter(mMQingMinAdapter);
        mThemeGV.setAdapter(mMThemeAdapter);
        mOtherGV.setAdapter(mMOtherAdapter);

        //设置监听
        //新中国的监听
        mNewChineseListener = new SellMallPanStampGridViewOnItemClickListener(Current, mMNewChineseAdapter);
        mNewChineseListener.setStampItemClick(this);
        mNewChineseGV.setOnItemClickListener(mNewChineseListener);
        //清民的监听
        mQingMinListener = new SellMallPanStampGridViewOnItemClickListener(Current, mMQingMinAdapter);
        mQingMinListener.setStampItemClick(this);
        mQingMinGV.setOnItemClickListener(mQingMinListener);
        //题材的监听
        mThemeListener = new SellMallPanStampGridViewOnItemClickListener(Current, mMThemeAdapter);
        mThemeListener.setStampItemClick(this);
        mThemeGV.setOnItemClickListener(mThemeListener);
        //其他的监听
        mOtherListener = new SellMallPanStampGridViewOnItemClickListener(Current, mMOtherAdapter);
        mOtherListener.setStampItemClick(this);
        mOtherGV.setOnItemClickListener(mOtherListener);

        SelfMallPanStampFilterDialog selfMallPanStampFilterDialog = (SelfMallPanStampFilterDialog) getParentFragment();
        selfMallPanStampFilterDialog.setClickReset(new SelfMallPanStampFilterDialog.ClickReset() {
            @Override
            public void setReset() {
                MyLog.LogShitou("===========点击了重置按钮", "=======================重置按钮");
                mNewChineseListener.setPosition();
                mQingMinListener.setPosition();
                mThemeListener.setPosition();
                mOtherListener.setPosition();
            }
        });
    }

    @Override
    public void GetClickItem(SelfMallPanStampGridViewAdapter adapter) {
        if (adapter == mMNewChineseAdapter) {
            mQingMinListener.setPosition();
            mThemeListener.setPosition();
            mOtherListener.setPosition();
        } else if (adapter == mMQingMinAdapter) {
            mNewChineseListener.setPosition();
            mThemeListener.setPosition();
            mOtherListener.setPosition();
        } else if (adapter == mMThemeAdapter) {
            mNewChineseListener.setPosition();
            mQingMinListener.setPosition();
            mOtherListener.setPosition();
        } else {
            mNewChineseListener.setPosition();
            mQingMinListener.setPosition();
            mThemeListener.setPosition();
        }


        int chineseNum = mNewChineseListener.getPosition();
        int qingminNum = mQingMinListener.getPosition();
        int themeNum = mThemeListener.getPosition();
        int otherNum = mOtherListener.getPosition();
        MyLog.e(chineseNum + "-" + qingminNum + "-" + themeNum + "-" + otherNum);


        String chinese = (chineseNum == -1) ? "" : mChineseValue[chineseNum];
        String qingmin = (qingminNum == -1) ? "" : mQingminValue[qingminNum];
        String theme = (themeNum == -1) ? "" : mThemeValue[themeNum];
        String other = (otherNum == -1) ? "" : mOtherValue[otherNum];

        // 选中组装Json串
        CategoryGoodsJsonBean mGoodsJsonBean = new CategoryGoodsJsonBean();
        CategoryGoodsJsonBean.CategoryBean mCategoryBean = new CategoryGoodsJsonBean.CategoryBean();
//        mCategoryBean.setCategory(CategoryValue);// 一级类别Value
//        CategoryGoodsJsonBean.CategoryBean.SubBean mSubBean = new CategoryGoodsJsonBean.CategoryBean.SubBean();

        if(!TextUtils.isEmpty(chinese)){ // 新中国
            mCategoryBean.setCategory(mArryValue[0]);// 一级类别Value
            CategoryGoodsJsonBean.CategoryBean.SubBean mSubBean = new CategoryGoodsJsonBean.CategoryBean.SubBean();
            mSubBean.setSub(chinese);// 二级Value
            mCategoryBean.setSubCategory(mSubBean);
            mGoodsJsonBean.setCategory(mCategoryBean);
            mToJson = new Gson().toJson(mGoodsJsonBean);
            MyLog.LogShitou("Goods======选中一类别的Json串", "mToJson===" + mToJson);

        }else if(!TextUtils.isEmpty(qingmin)){ // 清民区
            mCategoryBean.setCategory(mArryValue[1]);// 一级类别Value
            CategoryGoodsJsonBean.CategoryBean.SubBean mSubBean = new CategoryGoodsJsonBean.CategoryBean.SubBean();
            mSubBean.setSub(qingmin);// 二级Value
            mCategoryBean.setSubCategory(mSubBean);
            mGoodsJsonBean.setCategory(mCategoryBean);
            mToJson = new Gson().toJson(mGoodsJsonBean);
            MyLog.LogShitou("Metal======选中一类别的Json串", "mToJson===" + mToJson);

        }else if(!TextUtils.isEmpty(theme)){ // 外国
            mCategoryBean.setCategory(mArryValue[2]);// 一级类别Value
            CategoryGoodsJsonBean.CategoryBean.SubBean mSubBean = new CategoryGoodsJsonBean.CategoryBean.SubBean();
            mSubBean.setSub(theme);// 二级Value
            mCategoryBean.setSubCategory(mSubBean);
            mGoodsJsonBean.setCategory(mCategoryBean);
            mToJson = new Gson().toJson(mGoodsJsonBean);
            MyLog.LogShitou("Items======选中一类别的Json串", "mToJson===" + mToJson);

        }else if(!TextUtils.isEmpty(other)){ // 各类
            mCategoryBean.setCategory(mArryValue[3]);// 一级类别Value
            CategoryGoodsJsonBean.CategoryBean.SubBean mSubBean = new CategoryGoodsJsonBean.CategoryBean.SubBean();
            mSubBean.setSub(other);// 二级Value
            mCategoryBean.setSubCategory(mSubBean);
            mGoodsJsonBean.setCategory(mCategoryBean);
            mToJson = new Gson().toJson(mGoodsJsonBean);

            MyLog.LogShitou("Brand======选中一类别的Json串", "mToJson===" + mToJson);
        }



        mData = chinese + "," + qingmin + "," + theme + "," + other;
        setData(mToJson);
        MyLog.LogShitou("点击了邮市啥002--->","=="+mToJson);



    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

}
