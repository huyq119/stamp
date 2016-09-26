package  cn.com.chinau.view;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 可以斜着显示文字的TextView
 * 45度角的旋转
 */
public class RotateTextView extends TextView {


    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(-45, getMeasuredWidth() / 3, getMeasuredHeight() / 3);
        super.onDraw(canvas);
    }

}