package cn.com.chinau.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import cn.com.chinau.utils.pinyin.HintSideBar;
import cn.com.chinau.utils.pinyin.SideBar;

/**
 * 设计家页面
 */
public class DesignerActivity extends BaseActivity implements SideBar.OnChooseLetterChangedListener {
    private View mDesignerContent;
    private View mDesignerTitle;
    private RecyclerView mDesignerLV;
    private List<DesignerBean.Designer> mList;
    private ImageView mBack;//返回按钮
    private int num = 0; // 初始步长
    private static final int OFFSETNUM = 1000; // 首次请求条目数
    private PullToRefreshScrollView mDesignerSV;
    private ScrollView scrollView;
    private Button mTopBtn;


    private HintSideBar mSideBar;
    private DesignerListViewAdapter mMLVAdapter;
    private LinearLayoutManager manager;


    @Override
    public View CreateTitle() {
        mDesignerTitle = View.inflate(this, R.layout.base_back_title, null);
        return mDesignerTitle;
    }

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
    public View CreateSuccess() {
        mDesignerContent = View.inflate(this, R.layout.activity_designer_content, null);

        mList = new ArrayList<>();

        initView();
        initData();
        initListener();
        return mDesignerContent;
    }

    private void initView() {
        mBack = (ImageView) mDesignerTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mDesignerTitle.findViewById(R.id.base_title);
        mTitle.setText("设计家");
        mDesignerLV = (RecyclerView) mDesignerContent.findViewById(R.id.designer_lv);
        mSideBar = (HintSideBar) mDesignerContent.findViewById(R.id.designer_SideBar);

    }

    private void initAdapter(DesignerBean designerBean) {
        manager = new LinearLayoutManager(this, LinearLayoutCompat.VERTICAL, false);
        mMLVAdapter = new DesignerListViewAdapter(this, mBitmap, mList);
        mDesignerLV.setLayoutManager(manager);
        mMLVAdapter.setData(designerBean.getDesigner_list());
        mDesignerLV.setAdapter(mMLVAdapter);
    }

    private void initListener() {
        mSideBar.setOnChooseLetterChangedListener(this);
    }


    @Override
    public void AgainRequest() {
        initData();
    }

    /**
     * 设计家list网络请求
     */
    private void initData() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.DESIGNERLIST);
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num));
                params.put(StaticField.OFFSET, String.valueOf(OFFSETNUM));
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                MyLog.LogShitou("设计家List", "===" + result);
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }


    @Override
    public void onChooseLetter(String s) {
        int i = mMLVAdapter.getFirstPositionByChar(s.charAt(0));
        if (i == -1) {
            return;
        }
        manager.scrollToPositionWithOffset(i, 0);
    }

    @Override
    public void onNoChooseLetter() {

    }
}