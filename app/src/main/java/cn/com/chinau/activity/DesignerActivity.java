package cn.com.chinau.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.DesignerListViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.DesignerBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.IndexListView;

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
    private PullToRefreshScrollView mDesignerSV;
    private ScrollView scrollView;

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
//        mDesignerSV = (PullToRefreshScrollView) mDesignerContent.findViewById(R.id.designer_list_sv);
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

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                MyLog.e("设计家List~~>" + result);
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
//        scrollView =mDesignerSV.getRefreshableView();

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
                String Designer_sn = mList.get(i).getDesigner_sn();// 设计家编号
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.DESIGNERSN, Designer_sn);
                openActivityWitchAnimation(DesignerDetailActivity.class, bundle);
            }
        });

//        // 上拉加载
//        mDesignerSV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                num++;
//                getHomeInfo(num);
//            }
//        });
    }

    private IndexListView.AlphabetPositionListener positionListener = new IndexListView.AlphabetPositionListener() {
        @Override
        public int getPosition(String letter) {
//            mList.get(i).getEnglish_name().charAt(0);
            // 先注释
            for (int i = 0, count = mList.size(); i < count; i++) {
//                Character firstLetter = mList.get(i).getEnglish_name().charAt(0);
//                if (firstLetter != null){
//                    if (firstLetter.toString().equals(letter)) {
//                        return i;
//                    }
//                }
            }
            return UNKNOW;
        }
    };

    @Override
    public void AgainRequest() {
        initData();
    }
}
