package com.example.stamp.fragment.designerdetailfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;

/**
 * Created by Administrator on 2016/8/17.
 * 设计家详情-个人简历页面
 *
 */
public class DesignerResumeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View  mDesignerResume = View.inflate(getActivity(), R.layout.fragment_designer_resume, null);
        return mDesignerResume;
    }
}
