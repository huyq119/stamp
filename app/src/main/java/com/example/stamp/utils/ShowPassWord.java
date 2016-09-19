package com.example.stamp.utils;

import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.stamp.R;

/**
 * Created by Administrator on 2016/9/19.
 * 展示密码
 */
public class ShowPassWord {
    /**
     * 是否显示密码
     *
     * @param mPrassWord
     *            密码
     * @param mShowPassWord
     *            密码图标
     * @param isShowPassWord
     *            判断的标记
     */
    public static void isshowPassWord(boolean isShowPassWord, ImageView mShowPassWord, EditText mPrassWord) {
        if (isShowPassWord) {
            mShowPassWord.setImageResource(R.mipmap.eye_press);
            mPrassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); // 显示密码
            String pressword = mPrassWord.getText().toString();
            mPrassWord.setSelection(pressword.length());
        } else {
            mShowPassWord.setImageResource(R.mipmap.eye_normal);
            mPrassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // 隐藏密码
            String pressword = mPrassWord.getText().toString();
            mPrassWord.setSelection(pressword.length());
        }
    }

}
