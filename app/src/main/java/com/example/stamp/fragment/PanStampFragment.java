package com.example.stamp.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.activity.SearchActivity;
import com.example.stamp.adapter.PanStampGridViewAdapter;
import com.example.stamp.adapter.StampMarketGridViewAdapter;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.dialog.PanStampFilterDialog;
import com.example.stamp.fragment.popfragment.SelfMallFragment;
import com.example.stamp.utils.ScreenUtils;
import com.example.stamp.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 淘邮票页面
 */
public class PanStampFragment extends BaseFragment implements View.OnClickListener,AbsListView.OnScrollListener {


    private View mPanStampContent;//内容View
    private View mPanStampTitle;//标题View
    private Button mFilter,mTopBtn;//筛选,置顶
    private List<Fragment> mPopupList;//展示PopupWindow页面的Fragment的集合
    private String[] arr = {"自营商城", "第三方商家", "邮市", "竞拍"};
    private GridView mPanStampGV;//本页的GridView

    private ArrayList<StampTapBean.StampList> mList;//存放数据的集合
    private ImageView mScan,mSearch;// 扫码，搜索
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private int mCount;
    private boolean scrollFlag;
    @Override
    public View CreateTitle() {
        mPanStampTitle = View.inflate(getActivity(), R.layout.fragment_panstamp_title, null);
        return mPanStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mPanStampContent = View.inflate(getActivity(), R.layout.fragment_panstamp_content, null);
        initView();
        initAdapter();
        initListener();
        return mPanStampContent;
    }


    private void initView() {
        mScan = (ImageView) mPanStampTitle.findViewById(R.id.panstamp_title_scan);
        mSearch = (ImageView) mPanStampTitle.findViewById(R.id.panstamp_search);

        mFilter = (Button) mPanStampContent.findViewById(R.id.panStamp_filter);
        mTopBtn = (Button) mPanStampContent.findViewById(R.id.base_top_btn);
        mPanStampGV = (GridView) mPanStampContent.findViewById(R.id.panstamp_gl);
    }


    private void initAdapter() {
        mList = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            mList.add(new StampTapBean.StampList( "庚申年" ,"10000.0"+ i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        }

        PanStampGridViewAdapter mPanStampAdapter = new PanStampGridViewAdapter(getActivity(), mList, mBitmap);
        mPanStampGV.setAdapter(mPanStampAdapter);
    }

    private void initListener() {
        mScan.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mFilter.setOnClickListener(this);
        mTopBtn.setOnClickListener(this);
        mPanStampGV.setOnScrollListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.panstamp_title_scan://扫码
                openActivityWitchAnimation(CaptureActivity.class);
                break;
            case R.id.panstamp_search://搜索
                openActivityWitchAnimation(SearchActivity.class);
                break;
            case R.id.panStamp_filter://筛选
                setPopupWindowListData();
                PanStampFilterDialog filterDialogFragment = new PanStampFilterDialog(mPopupList, arr);
                filterDialogFragment.show(getChildFragmentManager(), StaticField.PANSTAMPFILTERDIALOG);
                break;
            case R.id.base_top_btn://筛选
                setListViewPos(0);
                mTopBtn.setVisibility(View.GONE);// 置顶后隐藏按钮
                break;
            default:
                break;

        }
    }

    /**
     * 滚动GridView到指定位置
     *
     * @param pos
     */
    private void setListViewPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= mCount) {
            mPanStampGV.smoothScrollToPosition(pos);
        } else {
            mPanStampGV.setSelection(pos);
        }
    }
    /**
     * GridView滑动状态改变监听的方法
     */
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        switch (i) {
            // 当不滚动时
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                scrollFlag = false;
                // 判断滚动到底部
                if (mPanStampGV.getLastVisiblePosition() == (mPanStampGV
                        .getCount() - 1)) {
                    mTopBtn.setVisibility(View.VISIBLE);
                }
                // 判断滚动到顶部
                if (mPanStampGV.getFirstVisiblePosition() == 0) {
                    mTopBtn.setVisibility(View.GONE);
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                scrollFlag = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                scrollFlag = false;
                break;
        }
    }
    /**
     * 置顶按钮显示的方法
     * @param absListView
     * @param firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
     * @param visibleItemCount 当前能看见的列表项个数（小半个也算）
     * @param totalItemCount totalItemCount：列表项共数
     */
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 当开始滑动且GridView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
        mCount = totalItemCount;
        if (scrollFlag
                && ScreenUtils.getScreenViewBottomHeight(mPanStampGV) >= ScreenUtils
                .getScreenHeight(getActivity())) {
            if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                mTopBtn.setVisibility(View.VISIBLE);
            } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                mTopBtn.setVisibility(View.GONE);
            } else {
                return;
            }
            lastVisibleItemPosition = firstVisibleItem;


        }
    }

    /**
     * 这个最后看在哪里写
     */
    private void setPopupWindowListData() {
        //初始化集合
        mPopupList = new ArrayList<>();

        SelfMallFragment mallFragment = new SelfMallFragment();

        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
        mPopupList.add(mallFragment);
    }
}
