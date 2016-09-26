package cn.com.chinau.fragment.popfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.chinau.R;
import cn.com.chinau.adapter.SelfMallPanStampGridViewAdapter;
import cn.com.chinau.base.BaseDialogFragment;
import cn.com.chinau.dialog.SelfMallPanStampFilterDialog;
import cn.com.chinau.listener.SellMallPanStampGridViewOnItemClickListener;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.view.NoScrollGridView;

/**
 * 商城筛选（自营商城）Fragment
 */
public class SelfMallFilterFragment extends BaseDialogFragment implements SellMallPanStampGridViewOnItemClickListener.SelfMallItemClick {

    private String[] arrClass = {"人物", "植物", "节日", "器皿", "字画", "风光", "经济", "农业", "民俗", "动物", "生肖",
            "文化艺术", "政治", "科教", "工业", "交通", "军事", "文学", "邮政", "公共"};
    private String[] arrYear = {"2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005"};
    private String[] arrPerson = {"任平", "于平", "甲钴胺", "邮票", "任平", "于平", "甲钴胺", "邮票", "任平", "于平", "甲钴胺", "邮票"};

    private View mFilterView;
    private int Current = -1;//当前选择的年代角标
    private SellMallPanStampGridViewOnItemClickListener mYearListener, mCategoryListener,mThemeListener;//年份的监听,类别的监听,人物的监听

    private View mSelfMall;
    private String mData;


    public SelfMallFilterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSelfMall = View.inflate(getActivity(), R.layout.fragment_selfmall_filter_dialog, null);
        //设置GridView的数据
        setPopupWindowData();
//        RequestNet();
        return mSelfMall;
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
        SelfMallPanStampGridViewAdapter mCategoryAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), arrClass);
        SelfMallPanStampGridViewAdapter mYearAdapter =  new SelfMallPanStampGridViewAdapter(getActivity(), arrYear);
        SelfMallPanStampGridViewAdapter mThemeAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), arrPerson);

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
        MyLog.e(yearNum + "-" + CategoryNum + "-" + PersonNum);


        String Year = (yearNum == -1) ? "" : arrClass[yearNum];
        String Category = (CategoryNum == -1) ? "" : arrYear[CategoryNum];
        String Person = (PersonNum == -1) ? "" : arrPerson[PersonNum];

        mData = Year + "," + Category + "," + Person;
        setData(mData);
        MyLog.e("点击了啥--->001"+mData);

    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }


}
