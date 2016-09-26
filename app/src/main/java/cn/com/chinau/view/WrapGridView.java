package  cn.com.chinau.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * 包裹内容的GridView
 * Created by Angle on 2016/8/25.
 */
public class WrapGridView extends GridView {
    public WrapGridView(Context context) {
        this(context, null);
    }

    public WrapGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
