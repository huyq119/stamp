package com.example.stamp.fragment.designerdetailfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.stamp.R;
import com.example.stamp.adapter.DesignerDetailWorksAdapter;
import com.example.stamp.bean.DesignerDetailsBean;
import com.example.stamp.bitmap.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/19.
 * 艺术作品页面
 */
public class DesignerWorksFragment extends Fragment {


    public ArrayList<DesignerDetailsBean.DesignerWorks> mList = new ArrayList<>();//存放数据的集合
    public BitmapUtils mBitmap;
    private ListView mDesignerList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mDesignerWorks = View.inflate(getActivity(), R.layout.fragment_designer_works,null);

        mDesignerList = (ListView) mDesignerWorks.findViewById(R.id.designer_works_lv);
        mBitmap = BitmapHelper.getBitmapUtils();
        initAdapter();
        return mDesignerWorks;
    }

    /**
     * 设置适配器
     */
    private void initAdapter() {
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add(new DesignerDetailsBean.DesignerWorks("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg","庚申年（金猴）"+i,"T.4"+i,"编年邮票","1980-02-15","1枚/套","￥1000000.00"));
        }

        DesignerDetailWorksAdapter mWorksAdapter = new DesignerDetailWorksAdapter(getActivity(), mList, mBitmap);
        mDesignerList.setAdapter(mWorksAdapter);

    }



}
