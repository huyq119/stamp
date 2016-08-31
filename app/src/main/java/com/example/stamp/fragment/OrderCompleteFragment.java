package com.example.stamp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.stamp.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/29.
 */
public class OrderCompleteFragment extends Fragment {

    private ExpandableListView expand_listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View orderAll = View.inflate(getActivity(), R.layout.fragment_goods_all, null);
        expand_listview = (ExpandableListView)orderAll.findViewById(R.id.expand_listview_all);
        initdata();
        return orderAll;
    }
    /**
     * 获取数据
     */
    private void initdata() {



    }
}
