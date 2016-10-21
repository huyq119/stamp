package cn.com.chinau.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.SearchListViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.CategoryRMBean;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SPUtils;
import cn.com.chinau.view.FlowLayout;


/**
 * 搜索页面
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private View mSearchContent;//内容页面
    private View mSearchTitle;//标题页面
    private FlowLayout mFlowLayout;//流式布局View
    private ListView mHistoricalSearch;//历史搜索的ListView
    private ImageView mBack, mSearch;//返回,搜索
    private EditText mSearchText;//搜索文字内容
    private TextView mClean;//清除搜索历史
    private LinearLayout mHistoricalView;//搜索历史整块布局
    private String[] mArrName;
    private List<String> mHList;
    private SharedPreferences sp;
    private String mCategory;

    @Override
    public View CreateTitle() {
        mSearchTitle = View.inflate(this, R.layout.activity_search_title, null);
        return mSearchTitle;
    }

    @Override
    public View CreateSuccess() {
        mSearchContent = View.inflate(this, R.layout.activity_search_content, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initSuccessView();
        initData();
        initListener();
        return mSearchContent;
    }

    /**
     * 初始化控件
     */
    private void initSuccessView() {

        //标题的控件
        mBack = (ImageView) mSearchTitle.findViewById(R.id.search_title_back);
        mSearch = (ImageView) mSearchTitle.findViewById(R.id.search_title_search);
        mSearchText = (EditText) mSearchTitle.findViewById(R.id.search_title_scanContent);
        // 显示Android输入法窗口
        InputMethodManager imm = ( InputMethodManager ) mSearchText.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.showSoftInput(mSearchText,InputMethodManager.SHOW_FORCED);
        //内容的控件
        mClean = (TextView) mSearchContent.findViewById(R.id.search_clean);
        mFlowLayout = (FlowLayout) mSearchContent.findViewById(R.id.search_fl);
        mHistoricalSearch = (ListView) mSearchContent.findViewById(R.id.search_lv);
        mHistoricalView = (LinearLayout) mSearchContent.findViewById(R.id.search_Historical_LV);
    }

    /**
     * 初始化数据(流式布局和搜索历史)
     */
    private void initData() {
        mCategory = sp.getString("Category1", "");// 保存在本地热搜数据
//        MyLog.LogShitou("一级类别0001----->:", mCategory);
        if (mCategory != null) {
            Gson gson = new Gson();
            CategoryRMBean mCategoryRMBean = gson.fromJson(mCategory, CategoryRMBean.class);
            ArrayList<CategoryRMBean.Category> list = mCategoryRMBean.getCategory();
            CategoryRMBean.Category SubCategory = list.get(0);
            ArrayList<CategoryRMBean.Category.SubCategory> subCategory1 = SubCategory.getSubCategory();

            int sub = subCategory1.size();// 获取subCategory1的个数
            mArrName = new String[sub];// 一级分类
            for (int i = 0; i < subCategory1.size(); i++) {
                mArrName[i] = subCategory1.get(i).getName();
//                MyLog.LogShitou("一级类别0001----->:", mArrName[i]);
            }
        }

        setFlowData(mArrName, mFlowLayout);  // 设置热门搜索的标签
        setListData(); // 设置搜索历史ListView
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mClean.setOnClickListener(this);

        mHistoricalSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 点击搜索历史记录进行搜索
                String data = mHList.get(i);
                if (!TextUtils.isEmpty(data)) {
                    //传递的搜索内容
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticField.SEARCHBUNDLE, data);
                    openActivityWitchAnimation(SearchDetailActivity.class, bundle);
                }

            }
        });
    }


    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.search_clean://清除搜索记录
                //隐藏搜索历史,这里要把数据清空
                mHistoricalView.setVisibility(View.GONE);
                //清除搜索历史
                SPUtils.remove(this, StaticField.SEARCHHISTORICAL);
                break;
            case R.id.search_title_search://搜索按钮
                //这里要保存历史记录的
                String SearchContent = mSearchText.getText().toString().trim();
                if (!TextUtils.isEmpty(SearchContent)) {
                    //传递的搜索内容
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticField.SEARCHBUNDLE, SearchContent);
                    openActivityWitchAnimation(SearchDetailActivity.class, bundle);
                    //设置历史数据
                    setHistoryData(SearchContent);
                }
                break;
        }
    }


    /**
     * 设置热门搜索的标签
     *
     * @param data   数据集
     * @param layout 流式布局空间
     */
    private void setFlowData(String[] data, FlowLayout layout) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < data.length; i++) {
          final TextView  tv = (TextView) inflater.inflate(R.layout.flowlayout_tv_item, layout, false);
            tv.setText(data[i]);
            tv.setTag(data[i]);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = (String) tv.getTag();
                    MyLog.LogShitou("点击了啥", tag);
                    //传递的搜索内容
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticField.SEARCHBUNDLE, tag);
                    openActivityWitchAnimation(SearchDetailActivity.class, bundle);
                }
            });
            layout.addView(tv);
        }


    }

    /**
     * 设置搜索历史ListView
     */
    private void setListData() {

        //首先取到本地保存的数据,如果有显示,没有不显示(还要把我的搜索历史记录隐藏)
        //获取搜索保存的所有内容
        String Search = (String) SPUtils.get(this, StaticField.SEARCHHISTORICAL, "");
        if (Search != null && !TextUtils.isEmpty(Search)) {
            String[] mHSearch = Search.split(",");//搜索历史数组
            mHList = Arrays.asList(mHSearch);
            SearchListViewAdapter mSearchAdapter = new SearchListViewAdapter(this, mHList);
            mHistoricalSearch.setAdapter(mSearchAdapter);
        } else {
            //隐藏搜索历史
            mHistoricalView.setVisibility(View.GONE);
        }

    }

    /**
     * 设置历史显示的数据
     *
     * @param searchContent 搜索的内容
     */
    private void setHistoryData(String searchContent) {
        //之前的历史数据
        String search = (String) SPUtils.get(this, StaticField.SEARCHHISTORICAL, "");
        if (TextUtils.isEmpty(search)) {//如果为空,直接添加
            SPUtils.put(this, StaticField.SEARCHHISTORICAL, searchContent);
        } else {//直接加在前面
            //这里还要判断一种情况就是之前是不是有5条数据了
            //说明一共是5条数据了
            if (search.split(",").length > 4) {
                //最后一次出现","的位置
                int last = search.lastIndexOf(",");
                //截取字符串
                search = search.substring(0, last);
            }

            StringBuffer sb = new StringBuffer(search);
            sb.insert(0, searchContent + ",");
            SPUtils.put(this, StaticField.SEARCHHISTORICAL, sb.toString());
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }
}
