package com.example.stamp.activity;

import android.view.View;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.fragment.scandetails.ScanDetailFirstFragment;
import com.example.stamp.fragment.scandetails.ScanDetailSecondFragment;
import com.example.stamp.view.DragLayout;

/**
 * 扫码详情页
 */
public class ScanDetailsActivity extends BaseActivity implements View.OnClickListener {


    private View mScanDetailsTitle;
    private View mScanDetailsContent;
    private DragLayout mDragLayout;
    private TextView mBuyBack;//申请回购按钮

    @Override
    public View CreateTitle() {
        mScanDetailsTitle = View.inflate(this, R.layout.base_back_title, null);
        return mScanDetailsTitle;
    }

    @Override
    public View CreateSuccess() {
        mScanDetailsContent = View.inflate(this, R.layout.activity_scandetails, null);
        initView();
        initData();
        initListener();
        return mScanDetailsContent;
    }


    private void initView() {
        mDragLayout = (DragLayout) mScanDetailsContent.findViewById(R.id.scanDetail_dragLayout);
        mBuyBack = (TextView) mScanDetailsContent.findViewById(R.id.scan_buyBack);
    }

    private void initData() {
        // 上面显示的view,下面显示的view
        ScanDetailFirstFragment first = new ScanDetailFirstFragment();
        final ScanDetailSecondFragment second = new ScanDetailSecondFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.first, first).add(R.id.second, second).commit();

        DragLayout.ShowNextPageNotifier nextInfo = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                second.initView();
            }
        };

        mDragLayout.setNextPageListener(nextInfo);
    }


    private void initListener() {
        mBuyBack.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scan_buyBack://申请回购
openActivityWitchAnimation(AffirmBuyBackActivity.class);
                break;
        }
    }
}
