package cn.com.chinau.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.FirmOrderBean;


/**
 * 选择配送方式的PopupWindow
 * Created by Administrator on 2016/8/18.
 */
public class SelectDistributionPopupWindow extends PopupWindow {

    private  View mExpreeView;
    private ImageView mEMSImg,mSFImg;//顺丰速递
    private TextView mName1,mName2,mValue1,mValue2,mCancel;

    private ArrayList<FirmOrderBean.ExpreeComp> express_comp;
    private ArrayList<FirmOrderBean.ExpreeFee> express_fee;
    /**
     * 这里的思想注意一下:这里直接把监听传入进来 这样便于外部实现
     *
     * @param context         上下文
     * @param onClickListener 监听
     */
    public SelectDistributionPopupWindow(Context context, View.OnClickListener onClickListener,
                                         ArrayList<FirmOrderBean.ExpreeComp> express_comp
            , ArrayList<FirmOrderBean.ExpreeFee> express_fee) {
        super(context);
        this.express_comp = express_comp;
        this.express_fee = express_fee;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mExpreeView = inflater.inflate(R.layout.firmorder_distribution_popup, null);

        setView();
        initView();
        initListener(onClickListener);
    }

    /**
     * 设置显示参数
     */
    private void setView() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mExpreeView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }


    private void initView() {
        mName1 = (TextView) mExpreeView.findViewById(R.id.expree_pop_name1);// 快公司
        mName1.setText(express_comp.get(0).getValue());
        mName2 = (TextView) mExpreeView.findViewById(R.id.expree_pop_name2);
        mName2.setText(express_comp.get(1).getValue());
        mValue1 = (TextView) mExpreeView.findViewById(R.id.expree_pop_value1);// 价格
        mValue1.setText("￥"+express_fee.get(0).getValue());
        mValue2 = (TextView) mExpreeView.findViewById(R.id.expree_pop_value2);
        mValue2.setText("￥"+express_fee.get(1).getValue());

        mEMSImg = (ImageView) mExpreeView.findViewById(R.id.EMS_click_image);
        mSFImg = (ImageView) mExpreeView.findViewById(R.id.SF_click_image);
        mCancel = (TextView) mExpreeView.findViewById(R.id.popup_cancel);
    }

    private void initListener(View.OnClickListener onClickListener) {
        mEMSImg.setOnClickListener(onClickListener);
        mSFImg.setOnClickListener(onClickListener);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mExpreeView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mExpreeView.findViewById(R.id.firmOrder).getTop();
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
