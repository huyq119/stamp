package com.example.stamp.base;

/**
 * 作者：jinlonghe on 2016/6/24 11:36
 * <p/>
 * 邮箱：753355530@qq.com
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.stamp.R;
import com.example.stamp.bitmap.BitmapHelper;
import com.example.stamp.http.HttpUtils;
import com.lidroid.xutils.BitmapUtils;


/**
 * 所有activity的基类
 *
 * @author Administrator
 */
public abstract class BaseActivity extends FragmentActivity {


    private View Error;// 失败的界面
    private View Success;// 成功界面

    private FrameLayout mContent;// 内容
    private FrameLayout mTitle;// 标题
    public BitmapUtils mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        mBitmap = BitmapHelper.getBitmapUtils();
        setContentView(R.layout.activity_base);
        mContent = (FrameLayout) findViewById(R.id.activity_base_content);
        mTitle = (FrameLayout) findViewById(R.id.activity_base_title);
        initContentView();
    }

    private void initContentView() {
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
        Error.setVisibility(HttpUtils.isConn(this) ? View.INVISIBLE : View.VISIBLE);
        Success.setVisibility(HttpUtils.isConn(this) ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * 创建一个错误界面
     *
     * @return 创建一个错误页面
     */
    private View CreateError() {
        View view = View.inflate(this, R.layout.view_error, null);
        LinearLayout page_ll = (LinearLayout) view.findViewById(R.id.base_errorLL);
        page_ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (HttpUtils.isConn(BaseActivity.this)) {
                    //判断view的显示
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
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        //跳转动画
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 通过类名启动Activity 不带动画
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据 不包含动画
     *
     * @param pClass
     * @param pBundle
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 带动画的退出
     */
    public void finishWitchAnimation() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    /**
     * 托付OnKeyDown事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishWitchAnimation();
        }
        return super.onKeyDown(keyCode, event);
    }
}