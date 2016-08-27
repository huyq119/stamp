package com.example.stamp.fragment.scandetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;

/**
 * 扫码回购详情页展示的第一个View
 * Created by Administrator on 2016/8/17.
 */
public class ScanDetailSecondFragment extends Fragment {

    private View mScanDetailView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mScanDetailView = View.inflate(getActivity(), R.layout.fragment_scandetail_second, null);
        return mScanDetailView;
    }

    public void initView() {

    }
}
