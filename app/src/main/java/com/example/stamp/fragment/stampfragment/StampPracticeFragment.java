package com.example.stamp.fragment.stampfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;

/**
 * 邮票故事
 */
public class StampPracticeFragment extends Fragment {


    public StampPracticeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mStampPractice = View.inflate(getActivity(), R.layout.fragment_stamppractice, null);
        return mStampPractice;
    }

}
