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
    private boolean flag = true;

    public GestureListener(LinearLayout heartll, int height) {
        ll = heartll;
        mHeight = height;
    }

    /**
     * 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
     */

    @Override
    public boolean onDown(MotionEvent e) {
        MyLog.e("down~~001" + e);
        return false;
    }

    /*
    * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
    * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
    */
    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**
     * 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        MyLog.e("down~~002" + e);
        return false;
    }

    /**
     * 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float startY = e1.getY();
        float endY = e2.getY();
        ValueAnimator animator;

        MyLog.e(startY + "-" + endY);

        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
        if ((Math.abs(startY) - Math.abs(endY)) > 100) {//说明向上滑动
            animator = ValueAnimator.ofInt(ll.getHeight(), 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    layoutParams.height = value;
                    ll.setLayoutParams(layoutParams);
//                Log.e("视图高度layoutParams", layoutParams.height + "_" + value);
//                    mScrollView.scrollTo(0, 0);// 让scrollView 移动到最下面
                }
            });
            animator.setDuration(200);//设置动画持续时间
            animator.start();

            flag = true;
        } else if ((Math.abs(endY) - Math.abs(startY)) > 100 && flag) {
            animator = ValueAnimator.ofInt(0, mHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    layoutParams.height = value;
                    ll.setLayoutParams(layoutParams);
//                Log.e("视图高度layoutParams", layoutParams.height + "_" + value);
//                    mScrollView.scrollTo(0, 0);// 让scrollView 移动到最下面
                }
            });
            animator.setDuration(200);//设置动画持续时间
            animator.start();

            flag = false;
        }


        return true;
    }

    /**
     * // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
     *
     * @param e
     */
    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // &#x53c2;&#x6570;&#x89e3;&#x91ca;&#xff1a;
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度，像素/秒
        // velocityY：Y轴上的移动速度，像素/秒

        // 触发条件 ：
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒

        final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling left
            Log.i("MyGesture", "Fling left");
//            Toast.makeText(this, "Fling Left", Toast.LENGTH_SHORT).show();
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling right
            Log.i("MyGesture", "Fling right");
//            Toast.makeText(this, "Fling Right", Toast.LENGTH_SHORT).show();
        }


        return false;
    }
}
