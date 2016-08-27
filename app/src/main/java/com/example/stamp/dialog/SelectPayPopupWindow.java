package com.example.stamp.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.stamp.R;

/**
 * 选择支付方式的PopupWindow
 * Created by Administrator on 2016/8/18.
 */
public class SelectPayPopupWindow extends PopupWindow {

    private  View mPayView;
    private ImageView mWechat;
    private ImageView mAlipay;
    private TextView mCancel;

    /**
     * 这里的思想注意一下:这里直接把监听传入进来 这样便于外部实现
     *
     * @param context         上下文
     * @param onClickListener 监听
     */
    public SelectPayPopupWindow(Context context, View.OnClickListener onClickListener) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPayView = inflater.inflate(R.layout.firmorder_pay_popup, null);

        setView();
        initView();
        initListener(onClickListener);
    }

    /**
     * 设置显示参数
     */
    private void setView() {
        //设置SelectPayPopupWindow的View
        this.setContentView(mPayView);
        //设置SelectPayPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPayPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPayPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPayPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }


    private void initView() {
        mWechat = (ImageView) mPayView.findViewById(R.id.Wechat_click);
        mAlipay = (ImageView) mPayView.findViewById(R.id.Alipay_click);
        mCancel = (TextView) mPayView.findViewById(R.id.popup_cancel);
    }

    private void initListener(View.OnClickListener onClickListener) {
        mWechat.setOnClickListener(onClickListener);
        mAlipay.setOnClickListener(onClickListener);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mPayView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                //判断整个控件的高度,如果按下的位置小于这个高度就关闭对话框
                int height = mPayView.findViewById(R.id.firmOrder).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
