package com.example.stamp.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.PanStampGridViewAdapter;
import com.example.stamp.adapter.StampMarketGridViewAdapter;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.dialog.PanStampFilterDialog;
import com.example.stamp.fragment.popfragment.SelfMallFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 淘邮票页面
 */
public class PanStampFragment extends BaseFragment implements View.OnClickListener {


    private View mPanStampContent;//内容View
    private View mPanStampTitle;//标题View
    private Button mFilter;//筛选
    private List<Fragment> mPopupList;//展示PopupWindow页面的Fragment的集合
    private String[] arr = {"自营商城", "第三方商家", "邮市", "竞拍"};
    private GridView mPanStampGV;//本页的GridView

    private ArrayList<StampTapBean.StampList> mList;//存放数据的集合

    @Override
    public View CreateTitle() {
        mPanStampTitle = View.inflate(getActivity(), R.layout.fragment_panstamp_title, null);
        return mPanStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mPanStampContent = View.inflate(getActivity(), R.layout.fragment_panstamp_content, null);
        initView();
        initAdapter();
        initListener();
        return mPanStampContent;
    }


    private void initView() {
        mFilter = (Button) mPanStampContent.findViewById(R.id.panStamp_filter);
        mPanStampGV = (GridView) mPanStampContent.findViewById(R.id.panstamp_gl);
    }


    private void initAdapter() {
        mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mList.add(new StampTapBean.StampList( "庚申年" ,"10000.0"+ i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        }

        PanStampGridViewAdapter mPanStampAdapter = new PanStampGridViewAdapter(getActivity(), mList, mBitmap);
        mPanStampGV.setAdapter(mPanStampAdapter);
    }

    private void initListener() {
        mFilter.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.panStamp_filter://筛选按钮
                setPopupWindowListData();
                PanStampFilterDialog filterDialogFragment = new PanStampFilterDialog(mPopupList, arr);
                filterDialogFragment.show(getChildFragmentManager(), StaticField.PANSTAMPFILTERDIALOG);
                break;

        }
    }

    /**
     * 这个最后看在哪里写
     */
    private void setPopupWindowListData() {
        //初始化集合
        mPopupList = new ArrayList<>();

        SelfMallFragment mallFragment = new SelfMallFragment();

        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
    }
}
