package  cn.com.chinau.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/9/6.
 * 商品订单ViewPager
 */
public class OrderGoodsViewPager extends ViewPager {

    private float xLast, yLast;

    public OrderGoodsViewPager(Context context) {
        super(context);
    }

    public OrderGoodsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.d("zhy", "zhy---111111111---" + super.onInterceptTouchEvent(ev));
        return isTemp();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        // System.out.println("viewpager_onTouchEvent"+super.onTouchEvent(ev));
//        Log.d("zhy", "zhy---66666666---" + isTemp());
        return super.onTouchEvent(ev);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean isTouch = false;
        float xDistance, yDistance;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = ev.getX();
                float curY = ev.getY();
                xDistance = Math.abs(curX - xLast);
                yDistance = Math.abs(curY - yLast);
                float t = xDistance - yDistance;
                if (xDistance > 60.00) {
                    isTouch = true;
                } else {
                    isTouch = false;
                }
                break;
        }
        setTemp(isTouch);
//        Log.d("zhy", "zhy---4444444444---" + isTemp());
        return super.dispatchTouchEvent(ev);
    }

    boolean temp = false;

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }


}
