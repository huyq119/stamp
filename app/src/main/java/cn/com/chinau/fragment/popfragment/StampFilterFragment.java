package cn.com.chinau.fragment.popfragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.SelfMallPanStampGridViewAdapter;
import cn.com.chinau.base.BaseDialogFragment;
import cn.com.chinau.bean.CategoryBean;
import cn.com.chinau.dialog.SelfMallPanStampFilterDialog;
import cn.com.chinau.listener.AuctionStampGridViewOnItemClickListener;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.view.NoScrollGridView;

/**
 * 淘邮票筛选（邮市）Fragment
 */
public class StampFilterFragment extends BaseDialogFragment{


    private int Current = -1;//当前选择的年代角标
    private View mStampView;
    private String mData;
    private TextView mNewChinese, mQingMin, mTheme, mOther;
    private AuctionStampGridViewOnItemClickListener mNewChineseListener, mQingMinListener, getmThemeListener, mThemeListener, mOtherListener;
    private ArrayList<CategoryBean.Category.SubCategory> subCategory1;
    private SharedPreferences sp;
    private String[] mChinese, mQingMins, mThemes, mOthers, mChineseValue, mQingminValue, mThemeValue, mOtherValue;
    private NoScrollGridView mNewChineseGV,mQingMinGV,mThemeGV,mOtherGV;


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

            String[] mArrTitle = new String[sub];// 一级分类
            //二级分类
            ArrayList<String[]> mArrList = new ArrayList<>();
            ArrayList<String[]> mArrListValue = new ArrayList<>();
            // 循环出一级分类的名字
            for (int i = 0; i < subCategory1.size(); i++) {
                mArrTitle[i] = subCategory1.get(i).getName();

                MyLog.LogShitou("邮市一级类别0001----->:", mArrTitle[i]);
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
        mNewChineseGV = (NoScrollGridView) mStampView.findViewById(R.id.stamp_pop_newchinese);
        //清民区GridView
         mQingMinGV = (NoScrollGridView) mStampView.findViewById(R.id.stamp_pop_qingmin);
        //主题GridView
         mThemeGV = (NoScrollGridView) mStampView.findViewById(R.id.stamp_pop_theme);
        //其他GridView
         mOtherGV = (NoScrollGridView) mStampView.findViewById(R.id.stamp_pop_other);

        //创建适配器
        SelfMallPanStampGridViewAdapter mNewChineseAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mChinese);
        SelfMallPanStampGridViewAdapter mQingMinAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mQingMins);
        SelfMallPanStampGridViewAdapter mThemeAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mThemes);
        SelfMallPanStampGridViewAdapter mOtherAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), mOthers);

        //设置适配器
        mNewChineseGV.setAdapter(mNewChineseAdapter);
        mQingMinGV.setAdapter(mQingMinAdapter);
        mThemeGV.setAdapter(mThemeAdapter);
        mOtherGV.setAdapter(mOtherAdapter);

// =============GridView条目监听

        // 新中国
        mNewChineseGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNewChineseListener.getPosition();
                mQingMinListener.setPosition();
                mThemeListener.setPosition();
                mOtherListener.setPosition();

            }
        });

        // 清明
        mQingMinGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNewChineseListener.setPosition();
                mQingMinListener.getPosition();
                mThemeListener.setPosition();
                mOtherListener.setPosition();
            }
        });

        // 主题
        mThemeGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNewChineseListener.setPosition();
                mQingMinListener.setPosition();
                mThemeListener.getPosition();
                mOtherListener.setPosition();
            }
        });

        // 其他
        mOtherGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNewChineseListener.setPosition();
                mQingMinListener.setPosition();
                mThemeListener.setPosition();
                mThemeListener.getPosition();
            }
        });

        SelfMallPanStampFilterDialog selfMallPanStampFilterDialog = (SelfMallPanStampFilterDialog) getParentFragment();
        selfMallPanStampFilterDialog.setClickReset(new SelfMallPanStampFilterDialog.ClickReset() {
            @Override
            public void setReset() {
                MyLog.LogShitou("===========点击了重置按钮","=======================重置按钮");
                mNewChineseListener.setPosition();
                mQingMinListener.setPosition();
                mThemeListener.setPosition();
                mOtherListener.setPosition();
            }
        });


    }



    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

}
