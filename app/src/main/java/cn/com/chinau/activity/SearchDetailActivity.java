package cn.com.chinau.activity;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.ClassifyPopupWindowAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.StampTapBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.ScreenUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;


/**
 * 搜索详情页
 */
public class SearchDetailActivity extends BaseActivity implements View.OnClickListener {

    private View mSearchDetail_content;
    private View mSearchDetail_title;
    private GridView mGridView;
    private ImageView mBack;
    private Button mClassify;//分类
    private View mTopItem;
    private View mClassify_View;// 快递的弹出框
    private ListView mClassifyLV;
    private PopupWindow mClassifyPop;//分类的PopupWindow对象
    private ColorDrawable dw = new ColorDrawable(0x0e000000);// popup的背景
    private int num = 0 ;

    private static final String[] mArr = {"全部", "商城", "竞拍", "邮市", "邮票目录"};
    private ArrayList<StampTapBean> mList;//存放数据的集合

    @Override
    public View CreateTitle() {
        mSearchDetail_title = View.inflate(this, R.layout.activity_searchdetail_title, null);
        return mSearchDetail_title;
    }

    @Override
    public View CreateSuccess() {
        mSearchDetail_content = View.inflate(this, R.layout.activity_searchdetail_content, null);
        initView();
        initData();
        initAdapter();
        initListener();
        return mSearchDetail_content;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mGridView = (GridView) mSearchDetail_content.findViewById(R.id.searchDetail_gl);
        mBack = (ImageView) mSearchDetail_title.findViewById(R.id.searchdetail_title_back);
        mClassify = (Button) mSearchDetail_content.findViewById(R.id.searchDetail_value);
        mTopItem = mSearchDetail_content.findViewById(R.id.searchDetail_tab);
    }

    private void initData() {
        String previousData = getIntentData();//获取上一个页面传过来的数据
        RequestNet(StaticField.ZH, num, StaticField.A,previousData,StaticField.QB);
    }

    private void initAdapter() {
//        mList = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            mList.add(new StampTapBean("10000.00", "庚申年" + i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
//        }

//        PanStampGridViewAdapter mPanStampAdapter = new PanStampGridViewAdapter(this, mList, mBitmap);
//        mGridView.setAdapter(mPanStampAdapter);
    }

    private void initListener() {
        mClassify.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchDetail_value://分类
                MyLog.e("执行分类按钮");
                showPopupWindow();
                break;
            case R.id.searchdetail_title_back://返回
                finishWitchAnimation();
                break;
        }
    }


    /**
     * 展示分类弹出的PopupWindow
     */
    private void showPopupWindow() {
        if (mClassify_View == null) {
            mClassify_View = View.inflate(this, R.layout.popupwindow_classify, null);
        }
        mClassifyLV = (ListView) mClassify_View.findViewById(R.id.classify_lv);

        ClassifyPopupWindowAdapter adapter = new ClassifyPopupWindowAdapter(mArr, this);
        mClassifyLV.setAdapter(adapter);

        if (mClassifyPop == null) {
            mClassifyPop = new PopupWindow(mClassify_View, ScreenUtils.getScreenWidth(this), ViewGroup.LayoutParams.MATCH_PARENT, true);
        }
        // 为了响应返回键和界面外的其他界面
        mClassifyPop.setBackgroundDrawable(dw);
        mClassifyPop.showAsDropDown(mTopItem, 0, 0);
    }

    /**
     * 搜索结果的请求方法
     * @param Order_By 排序条件
     * @param index 索引
     * @param Sort 顺序
     * @param search 搜索条件
     * @param scope 查询范围
     */
    private void RequestNet(final String Order_By, final int index, final String Sort, final String search, final String scope) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SEARCH);
                params.put(StaticField.CONDITION, search);//搜索条件
                params.put(StaticField.SCOPE, scope);//查询范围：QB全部；SC商城；YS邮市；JP竞拍；ML邮票目录；
                params.put(StaticField.ORDER_BY, Order_By);
                params.put(StaticField.SORT_BY, Sort);
                params.put(StaticField.CURRENT_INDEX, String.valueOf(index));
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM));
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.e(result);

                if (result.equals("-1")) {
                    return;
                }
            }
        });
    }

    /**
     * 获取上一个页面传过来的数据
     * @return
     */
    public String getIntentData() {
        return getIntent().getStringExtra(StaticField.SEARCHBUNDLE);
    }
}
