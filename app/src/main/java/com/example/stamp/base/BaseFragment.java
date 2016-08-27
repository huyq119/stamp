package com.example.stamp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.stamp.R;
import com.example.stamp.bitmap.BitmapHelper;
import com.example.stamp.http.HttpUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    private View Error;// 失败的界面
    private View Success;// 成功界面

    private FrameLayout mContent;// 内容
    private FrameLayout mTitle;// 标题
    public BitmapUtils mBitmap;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBitmap = BitmapHelper.getBitmapUtils();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View BaseView = inflater.inflate(R.layout.fragment_base, null);
        mContent = (FrameLayout) BaseView.findViewById(R.id.fragment_fl);
        mTitle = (FrameLayout) BaseView.findViewById(R.id.base_title);
        showView();
        return BaseView;
    }

    /**
     * 初始化布局
     */
    private void showView() {
        mTitle.addView(CreateTitle());
        Error = CreateError();
        if (Error != null) {
            mContent.addView(Error, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        Success = CreateSuccess();
        if (Success != null) {
            mContent.addView(Success, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        show();
    }


    /**
     * 用于展示view的判断
     */
    private void show() {
        //判断有误网络进行显示页面
        Error.setVisibility(HttpUtils.isConn(getActivity()) ? View.INVISIBLE : View.VISIBLE);
        Success.setVisibility(HttpUtils.isConn(getActivity()) ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * 创建一个错误界面
     *
     * @return
     */
    private View CreateError() {
        View view = View.inflate(getActivity(), R.layout.view_error, null);
        LinearLayout page_ll = (LinearLayout) view.findViewById(R.id.base_errorLL);
        page_ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (HttpUtils.isConn(getActivity())) {
                    show();
                    //如果有网络连接重新请求服务器,否则不请求
                    AgainRequest();// 再次请求服务器,让子类自己去实现
                }
            }
        });
        return view;
    }

    /**
     * 创建标题
     *
     * @return 标题布局
     */
    public abstract View CreateTitle();

    /**
     * 创建成功界面
     *
     * @return 成功的布局
     */
    public abstract View CreateSuccess();

    /**
     * 再次请求服务器的方法
     */
    public abstract void AgainRequest();


    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }


    /**
     * 通过类名启动Activity带动画
     *
     * @param pClass
     */
    public void openActivityWitchAnimation(Class<?> pClass) {
        openActivityWitchAnimation(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据,带动画
     *
     * @param pClass
     * @param pBundle
     */
    public void openActivityWitchAnimation(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        //跳转动画
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 带动画的退出
     */
    public void finishwitchAnimation() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }


}
