package cn.com.chinau.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import cn.com.chinau.R;
import cn.com.chinau.utils.ScreenUtils;

/**
 * 筛选右侧弹出框的基类
 * Created by Administrator on 2016/8/3.
 */
public class BaseDialogFragment extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 创建基类的View
     *
     * @param inflater
     * @param ID       创建布局的ID
     * @return
     */
    public View CreateBaseView(LayoutInflater inflater, int ID) {
        //去出标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置动画
        getDialog().getWindow().getAttributes().windowAnimations = R.style.popwin_anim_style;
        //设置主题
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), com.viewpagerindicator.R.style.Theme_PageIndicatorDefaults);


        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        //填充布局
        //需要用android.R.id.content这个view
        View mFilterView = localInflater.inflate(ID, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(ScreenUtils.getScreenWidth(getActivity()) - 80, ScreenUtils.getScreenHeight(getActivity()) - ScreenUtils.getStatusHeight(getActivity()));
        return mFilterView;
    }


}
