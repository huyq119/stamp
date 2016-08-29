package com.example.stamp.activity;

import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.MyStampGridViewAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.MyStampGridViewBean;
import com.example.stamp.utils.MyToast;

import java.util.ArrayList;

/**
 * 我的邮集页面
 */
public class MyStampActivity extends BaseActivity implements View.OnClickListener{

    private View mMyStampTitle,mMyStampContent;
    private ImageView mBack,mMore;
    private GridView mStampGV;
    private ArrayList<MyStampGridViewBean.StampList> mList;
    private View MorePopView;
    private PopupWindow mMorePop;
    private TextView mEdit,mShared;

    // popup的背景
    private ColorDrawable mColorBg = new ColorDrawable(00000000);
    @Override
    public View CreateTitle() {
        mMyStampTitle = View.inflate(this, R.layout.activity_mystamp_title, null);
        return mMyStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mMyStampContent = View.inflate(this, R.layout.activity_mystamp_content, null);
        initView();
        initAdapter();
        initListener();
        return mMyStampContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mList.add(new MyStampGridViewBean.StampList("庚申年", ""+i, "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg"));
        }
        mBack = (ImageView) mMyStampTitle.findViewById(R.id.base_title_back);
        mMore= (ImageView) mMyStampTitle.findViewById(R.id.base_more);
        mStampGV = (GridView) mMyStampContent.findViewById(R.id.myStamp_gv);
    }

    private void initAdapter(){
        MyStampGridViewAdapter mMyStampAdapter = new MyStampGridViewAdapter (this, mList, mBitmap);
        mStampGV.setAdapter(mMyStampAdapter);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mMore.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_more://更多按钮
                MorePopupWindow();
                break;
        }
    }

    /**
     * 点击更多
     */
    private void MorePopupWindow() {
        // 判断选择性别弹出的View是否为空
        if (MorePopView == null) {
            MorePopView = View.inflate(this, R.layout.mystamp_dialog,
                    null);
        }
        if (mMorePop == null) {
            mMorePop = new PopupWindow(MorePopView,
                    180, 154);
        }
        // 为了响应返回键和界面外的其他界面
        mMorePop.setBackgroundDrawable(mColorBg);
        mMorePop.showAsDropDown(mMore, 0, -15);
        mMorePop.setOutsideTouchable(true);

        mEdit = (TextView) MorePopView.findViewById(R.id.mystamp_edit);
        mShared = (TextView) MorePopView.findViewById(R.id.mystamp_shared);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyToast.showShort(MyStampActivity.this,"点击了编辑");
                mMorePop.dismiss();
            }
        });
        mShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(SharedActivity.class);
                mMorePop.dismiss();
            }
        });
    }

    /**
     * 点击mMorePop外部关闭PopupWindow
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMorePop != null){
            mMorePop.dismiss();
        }
        return super.onTouchEvent(event);
    }
}
