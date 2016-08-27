package com.example.stamp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.adapter.DesignerListViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.DesignerBean;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.view.IndexListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 设计家页面
 */
public class DesignerActivity extends BaseActivity {

    private View mDesignerContent;
    private View mDesignerTitle;
    private IndexListView mDesignerLV;
    private List<DesignerBean.Designer> mList;
    private ImageView mBack;//返回按钮

    private int num = 0;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS:
                    Gson gson = new Gson();
                    DesignerBean designerBean = gson.fromJson((String) msg.obj, DesignerBean.class);

                    initAdapter(designerBean);
                    break;
            }
        }
    };


    @Override
    public View CreateTitle() {
        mDesignerTitle = View.inflate(this, R.layout.base_back_title, null);
        return mDesignerTitle;
    }

    @Override
    public View CreateSuccess() {
        mDesignerContent = View.inflate(this, R.layout.activity_designer_content, null);
        initView();
        initData();
        initListener();
        return mDesignerContent;
    }


    private void initView() {
        mBack = (ImageView) mDesignerTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mDesignerTitle.findViewById(R.id.base_title);
        mTitle.setText("设计家");
        mDesignerLV = (IndexListView) mDesignerContent.findViewById(R.id.designer_lv);
    }

    private void initData() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.DESIGNERLIST);
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num));
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM));
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                if (result.equals("-1")) {
                    return;
                }

                MyLog.e("设计家内容" + result);

                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void initAdapter(DesignerBean designerBean) {
        mList = new ArrayList<>();
        mList = designerBean.getDesigner_list();

        DesignerListViewAdapter mLVAdapter = new DesignerListViewAdapter(this, mBitmap, mList);
        mDesignerLV.setAdapter(mLVAdapter, positionListener);
    }

    private void initListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });

        mDesignerLV.setOnItemClickListener(new IndexListView.onItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 跳转设计家详情页面
                String Chinese_name = mList.get(i).getChinese_name();// 获取中文名
                String English_name = mList.get(i).getEnglish_name();// 获取英文名
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.DESIGNERDETAIL_CHINESE, Chinese_name);
                bundle.putString(StaticField.DESIGNERDETAIL_ENGLISH, English_name);
                openActivityWitchAnimation(DesignerDetailActivity.class, bundle);
            }
        });
    }

    private IndexListView.AlphabetPositionListener positionListener = new IndexListView.AlphabetPositionListener() {
        @Override
        public int getPosition(String letter) {
            for (int i = 0, count = mList.size(); i < count; i++) {
                Character firstLetter = mList.get(i).getEnglish_name().charAt(0);
                if (firstLetter.toString().equals(letter)) {
                    return i;
                }
            }
            return UNKNOW;
        }
    };


    @Override
    public void AgainRequest() {
        initData();
    }
}
