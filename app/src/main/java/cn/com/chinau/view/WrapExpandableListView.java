package  cn.com.chinau.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * 包裹内容的ExpandableListView
 * Created by Angle on 2016/8/26.
 */
public class WrapExpandableListView extends ExpandableListView {
    public WrapExpandableListView(Context context) {
        super(context);
    }

    public WrapExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
