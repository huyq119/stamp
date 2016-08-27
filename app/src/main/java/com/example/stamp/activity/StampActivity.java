package com.example.stamp.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.PanStampGridViewAdapter;
import com.example.stamp.adapter.StampHorizontalListViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.view.HorizontalListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 邮市页面
 */
public class StampActivity extends BaseActivity implements View.OnClickListener {

    private View mStampTitle;
    private View mStampContent;
    private HorizontalListView hListView;//横向滑动的listView
    private StampHorizontalListViewAdapter hListViewAdapter;//横向花的listView的适配器
    private ArrayList<StampTapBean> mList;
    private GridView mGridView;
    private ImageView mBack, mSearch;//返回按钮,搜索


    private String[] mArr = {"红豆", "大红豆", "绿豆", "红豆", "大红豆", "绿豆", "大红豆", "红豆", "红豆", "大红豆", "绿豆", "大红豆", "红豆"};


    @Override
    public View CreateTitle() {
        mStampTitle = View.inflate(this, R.layout.base_search_title, null);
        return mStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mStampContent = View.inflate(this, R.layout.activity_stamp_content, null);
        initView();
        initData();
        initAdapter();
        initListener();
        return mStampContent;
    }


    private void initView() {
        mList = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            mList.add(new StampBean("10000.00", "庚申年" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
//        }


        mBack = (ImageView) mStampTitle.findViewById(R.id.base_title_back);
        mSearch = (ImageView) mStampTitle.findViewById(R.id.base_search);
        hListView = (HorizontalListView) mStampContent.findViewById(R.id.stamp_hl);
        mGridView = (GridView) mStampContent.findViewById(R.id.stamp_gl);
    }

    private void initData() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPCATEGORY);
                params.put(StaticField.OP_TYPE, "YS");
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.e(result);
            }
        });
    }


    private void initAdapter() {
        //内容GridView设置适配器
        PanStampGridViewAdapter mPanStampAdapter = new PanStampGridViewAdapter(this, mList, mBitmap);
        mGridView.setAdapter(mPanStampAdapter);

        //横向的listView设置适配器
        hListViewAdapter = new StampHorizontalListViewAdapter(this, mArr);
        hListView.setAdapter(hListViewAdapter);

    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        //横向ListView的点击事件
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //设置点击背景的方法
                hListViewAdapter.setSelection(i);
                hListViewAdapter.notifyDataSetChanged();
            }
        });

        //GridView的条目点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //去往邮市详情页
                openActivityWitchAnimation(StampDetailActivity.class);
            }
        });

    }

    @Override
    public void AgainRequest() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_search://搜索按钮
                openActivityWitchAnimation(SearchActivity.class);
                break;

        }
    }
}
