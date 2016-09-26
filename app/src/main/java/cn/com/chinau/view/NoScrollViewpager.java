package  cn.com.chinau.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能滑动的viewpager
 * 
 * @author Administrator
 *
 */
public class NoScrollViewpager extends LazyViewPager {
	public NoScrollViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewpager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}

}
