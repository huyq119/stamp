package com.example.stamp.listener;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.example.stamp.utils.MyLog;

/**
 * Created by Angle on 2016/9/8.
 */
public class GestureListener implements GestureDetector.OnGestureListener {

    private LinearLayout ll;
    private int mHeight;

    public GestureListener(LinearLayout heartll, int height) {
        ll = heartll;
        mHeight = height;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
        MyLog.e(mHeight + "");
        int startHeight = 0;
        int targetHeight = 0;
        if (distanceY >= 0) {//说明向上滑动
            startHeight = ll.getHeight();
            targetHeight = 0;
        } else if (distanceY < 0) {
            startHeight = 0;
            targetHeight = mHeight;
            MyLog.e(targetHeight + "");
        }

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, targetHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                layoutParams.height = value;
                ll.setLayoutParams(layoutParams);
                Log.e("视图高度layoutParams", layoutParams.height + "_" + value);
//                    mScrollView.scrollTo(0, 0);// 让scrollView 移动到最下面
            }
        });
        animator.setDuration(500);//设置动画持续时间
        animator.start();

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
