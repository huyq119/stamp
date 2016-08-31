package com.example.stamp.fragment;

import android.view.View;
import android.widget.ListView;

import com.example.stamp.R;
import com.example.stamp.base.BaseFragment;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/8/30.
 */
public class OrderFragment extends BaseFragment {

    private View OrderContent;
    private ListView mlistview;

    @Override
    public View CreateTitle() {
        return null;
    }
    @Override
    public View CreateSuccess() {
       OrderContent =  View.inflate(getActivity(), R.layout.activity_order_sweep, null);
        initView();
        initdata();
        return OrderContent;
    }

    private void initView() {
        mlistview = (ListView) OrderContent.findViewById(R.id.order_listView);


    }
    private void initdata() {


    }
    @Override
    public void AgainRequest() {

    }
}
