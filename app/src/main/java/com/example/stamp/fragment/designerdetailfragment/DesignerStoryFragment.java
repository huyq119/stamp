package com.example.stamp.fragment.designerdetailfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.stamp.R;
import com.example.stamp.activity.DesignerStoryDetailActivity;
import com.example.stamp.adapter.DesignerDetailsStoryAdapter;
import com.example.stamp.bean.DesignerDetailsBean;
import com.example.stamp.bean.DesignerDetailsBean.DesignerStory;
import com.example.stamp.bitmap.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 * 设计故事页面
 */
public class DesignerStoryFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView mStoryList;
    public ArrayList<DesignerDetailsBean.DesignerStory> mList = new ArrayList<>();//存放数据的集合
    public BitmapUtils mBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mDesignerStory = View.inflate(getActivity(), R.layout.fragment_designer_story,null);
        mStoryList = (ListView) mDesignerStory.findViewById(R.id.designer_story_lv);
        mBitmap = BitmapHelper.getBitmapUtils();
        initAdapter();

        return mDesignerStory;
    }

    /**
     * mStoryList条目监听事件
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
      // 跳转到设计故事详情界面
        openActivityWitchAnimation(DesignerStoryDetailActivity.class);
    }

    private void initAdapter() {
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add(new DesignerStory("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg","陈绍华:《葵已年，未采用稿》","陈绍华"+i));
        }

        DesignerDetailsStoryAdapter mPanStampAdapter = new DesignerDetailsStoryAdapter(getActivity(), mList, mBitmap);
        mStoryList.setAdapter(mPanStampAdapter);
    }

    /**
     * 通过类名启动Activity,带动画
     *
     * @param pClass
     */
    public void openActivityWitchAnimation(Class<?> pClass) {
        Intent intent = new Intent(getActivity(), pClass);
        startActivity(intent);
        //跳转动画
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }


}
