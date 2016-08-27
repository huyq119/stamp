package com.example.stamp.fragment.popfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;

/**
 * 自营商城
 */
public class SelfMallFragment extends Fragment {


    public SelfMallFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mSelfMall = View.inflate(getActivity(), R.layout.fragment_selfmall, null);

        return mSelfMall;
    }

}
