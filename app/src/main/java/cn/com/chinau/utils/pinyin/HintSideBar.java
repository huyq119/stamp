package cn.com.chinau.utils.pinyin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import cn.com.chinau.R;

public class HintSideBar extends RelativeLayout implements SideBar.OnChooseLetterChangedListener {


    private SideBar.OnChooseLetterChangedListener onChooseLetterChangedListener;

    public HintSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_hint_side_bar, this);
        initView();
    }

    private void initView() {
        SideBar sideBar = (SideBar) findViewById(R.id.sideBar);
        sideBar.setOnTouchingLetterChangedListener(this);
    }

    @Override
    public void onChooseLetter(String s) {
        if (onChooseLetterChangedListener != null) {
            onChooseLetterChangedListener.onChooseLetter(s);
        }
    }

    @Override
    public void onNoChooseLetter() {
        if (onChooseLetterChangedListener != null) {
            onChooseLetterChangedListener.onNoChooseLetter();
        }
    }

    public void setOnChooseLetterChangedListener(SideBar.OnChooseLetterChangedListener onChooseLetterChangedListener) {
        this.onChooseLetterChangedListener = onChooseLetterChangedListener;
    }
}
