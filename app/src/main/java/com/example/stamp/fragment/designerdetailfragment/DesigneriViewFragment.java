package com.example.stamp.fragment.designerdetailfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.stamp.R;
import com.example.stamp.adapter.DesignerDetailViewAdapter;
import com.example.stamp.bean.DesignerDetailsBean;
import com.example.stamp.bitmap.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 * 名家访谈页面
 */
public class DesigneriViewFragment extends Fragment{

    private ListView mDesignerList;
    private BitmapUtils mBitmap;
    private ArrayList<DesignerDetailsBean.DesignerView> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View mDesignerView = View.inflate(getActivity(), R.layout.fragment_designer_view,null);

        mDesignerList = (ListView) mDesignerView.findViewById(R.id.designer_view_lv);
        mBitmap = BitmapHelper.getBitmapUtils();
        initAdapter();
        return mDesignerView;
    }

    /**
     * 设置适配器
     */
    private void initAdapter() {
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add(new DesignerDetailsBean.DesignerView("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg","邮来邮网专访《豫园》特种邮票设计师张安朴"+i,"1980-02-1"+i));
        }

        DesignerDetailViewAdapter mViewAdapter = new DesignerDetailViewAdapter(getActivity(), mList, mBitmap);
        mDesignerList.setAdapter(mViewAdapter);


    }
}
