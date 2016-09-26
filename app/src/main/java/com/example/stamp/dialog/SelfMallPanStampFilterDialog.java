package com.example.stamp.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.PanStampDialogPagerAdapter;
import com.example.stamp.base.BaseDialogFragment;
import com.example.stamp.utils.MyLog;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 * （商城，淘邮票）共用的筛选Dialog
 */
public class SelfMallPanStampFilterDialog extends BaseDialogFragment implements View.OnClickListener {

    private List<Fragment> mList;
    private String[] arr;
    private View mFilterView;
    private TextView mResetting;
    private TextView mEnsure;

    private ClickEnsure mClickEnsure;
    private ClickReset mClickReset;

    public SelfMallPanStampFilterDialog(List<Fragment> mList, String[] arr) {
        this.mList = mList;
        this.arr = arr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //创建显示的View
        mFilterView = CreateBaseView(inflater, R.layout.base_viewpagerindicator);


        //初始化控件
        FragmentPagerAdapter adapter = new PanStampDialogPagerAdapter(getChildFragmentManager(), mList, arr);
        ViewPager pager = (ViewPager) mFilterView.findViewById(R.id.baseStamp_vp);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) mFilterView.findViewById(R.id.baseStamp_indicator);
        indicator.setViewPager(pager);


        initView();
        initListener();

        return mFilterView;
    }


    private void initView() {
        //重置按钮,确定按钮
        mResetting = (TextView) mFilterView.findViewById(R.id.stamptap_pop_resetting);
        mEnsure = (TextView) mFilterView.findViewById(R.id.stamptap_pop_ensure);
    }

    private void initListener() {
        mResetting.setOnClickListener(this);
        mEnsure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stamptap_pop_resetting://重置按钮
                mClickReset.setReset();
                MyLog.e("重置按钮被执行了");
                break;
            case R.id.stamptap_pop_ensure://确定按钮
                mClickEnsure.setEnsureData();
                break;
        }
    }

    //////////////////////////////////////////////////////
    public interface ClickEnsure{
        void setEnsureData();
    }
    //确定按钮的回调
    public void setClickEnsure(ClickEnsure clickEnsure) {
        mClickEnsure = clickEnsure;
    }
    //////////////////////////////////////////////////////


    //////////////////////////////////////////////////////
    public interface ClickReset{
        void setReset();
    }

    public void setClickReset(ClickReset clickReset) {
        mClickReset = clickReset;
    }
    //////////////////////////////////////////////////////
}
