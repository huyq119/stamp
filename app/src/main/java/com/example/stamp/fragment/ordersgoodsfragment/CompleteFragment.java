package com.example.stamp.fragment.ordersgoodsfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;

/**
 * Created by Administrator on 2016/9/2.
 * 已完成Fragment页面
 */
public class CompleteFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View CompleteView = View.inflate(getActivity(), R.layout.fragment_ordersgoods_complete, null);

        return CompleteView;
    }
}
