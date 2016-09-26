package cn.com.chinau.utils;

import android.text.InputType;
import android.text.method.NumberKeyListener;

/**
 * Created by Administrator on 2016/9/19.
 * 密码页面的输入限制
 */
public class MyNumberKeyListener extends NumberKeyListener {

    @Override
    public int getInputType() {
        return InputType.TYPE_TEXT_VARIATION_PASSWORD;
    }

    @Override
    protected char[] getAcceptedChars() {
        char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        return numberChars;
    }




}
