package com.example.stamp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.stamp.R;

/**
 * Created by Administrator on 2016/8/30.
 */
public class TimeTextView extends TextView implements Runnable{

    Paint mPaint; // 画笔,包含了画几何图形、文本等的样式和颜色信息
    private int[] times;
    private long  mhour, mmin, msecond;// 天，小时，分钟，秒
    private boolean run = false; // 是否启动了

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);
        array.recycle();// 一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);
        array.recycle();// 一定要调用，否则这次的设定会对下次的使用造成影响
    }



    public int[] getTimes() {
        return times;
    }

    public void setTimes(int[] times) {
        this.times = times;
        mhour = times[0];
        mmin = times[1];
        msecond = times[2];
    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mhour = 0;
                }
            }
        }
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        // 标示已经启动
        run = true;

        ComputeTime();
        String strTime = mhour + "小时" + mmin + "分"
                + msecond + "秒";
        this.setText(strTime);
        if (mhour == 0 && mmin == 0 && msecond == 0) {
            return;
        }
        postDelayed(this, 1000);
    }
}
