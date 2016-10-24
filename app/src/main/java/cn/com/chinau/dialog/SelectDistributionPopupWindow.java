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
import cn.com.chinau.utils.MyLog;


/**
 * 选择配送方式的PopupWindow
 * Created by Administrator on 2016/8/18.
 */
public class SelectDistributionPopupWindow extends PopupWindow implements View.OnClickListener {

    private View mExpreeView;
    private ImageView mEMSImg, mSFImg;//顺丰速递
    private TextView mName1, mName2, mValue1, mValue2, mCancel;

    private ArrayList<FirmOrderBean.ExpreeComp> express_comp;
    private ArrayList<FirmOrderBean.ExpreeFee> express_fee;
    private StringText stringText;
    private boolean clickImgFlage = true;//  选择那种配送方式标识

    /**
     *
     * @param context
     * @param express_comp 配送方式
     * @param express_fee  配送价格
     */
    public SelectDistributionPopupWindow(Context context, ArrayList<FirmOrderBean.ExpreeComp> express_comp
            , ArrayList<FirmOrderBean.ExpreeFee> express_fee) {
        super(context);
        this.express_comp = express_comp;
        this.express_fee = express_fee;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mExpreeView = inflater.inflate(R.layout.firmorder_distribution_popup, null);

        setView();
        initView();
        initListener();
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
        this.setAnimationStyle(R.style.MyDialogStyleBottom);
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
        mValue1.setText("￥" + express_fee.get(0).getValue());
        mValue2 = (TextView) mExpreeView.findViewById(R.id.expree_pop_value2);
        mValue2.setText("￥" + express_fee.get(1).getValue());

        mEMSImg = (ImageView) mExpreeView.findViewById(R.id.EMS_click_image);
        mSFImg = (ImageView) mExpreeView.findViewById(R.id.SF_click_image);
        mCancel = (TextView) mExpreeView.findViewById(R.id.popup_cancel);
        if (clickImgFlage){
            mEMSImg.setImageResource(R.mipmap.circle_select_click);
            mSFImg.setImageResource(R.mipmap.circle_select);
        }else{
            mSFImg.setImageResource(R.mipmap.circle_select_click);
            mEMSImg.setImageResource(R.mipmap.circle_select);
        }
    }


    private void initListener() {
        mEMSImg.setOnClickListener(this);
        mSFImg.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mExpreeView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mExpreeView.findViewById(R.id.firmOrder).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.EMS_click_image: // 邮政
                String name1 = mName1.getText().toString().trim();
                String valuel1 = mValue1.getText().toString().trim();
                if (clickImgFlage){
                    mEMSImg.setImageResource(R.mipmap.circle_select_click);
                    mSFImg.setImageResource(R.mipmap.circle_select);
                }else{
                    mSFImg.setImageResource(R.mipmap.circle_select_click);
                    mEMSImg.setImageResource(R.mipmap.circle_select);
                }
                MyLog.LogShitou("选择了啥11",name1+"--"+valuel1);
                stringText.GetStringText(name1,valuel1);
                dismiss();

                break;
            case R.id.SF_click_image:// 顺丰
                String name2 = mName2.getText().toString().trim();
                String valuel2 = mValue2.getText().toString().trim();
                if (clickImgFlage){
                    mSFImg.setImageResource(R.mipmap.circle_select_click);
                    mEMSImg.setImageResource(R.mipmap.circle_select);
                }else{
                    mEMSImg.setImageResource(R.mipmap.circle_select_click);
                    mSFImg.setImageResource(R.mipmap.circle_select);
                }

                MyLog.LogShitou("选择了啥22",name2+"--"+valuel2);
                stringText.GetStringText(name2,valuel2);
                dismiss();
                clickImgFlage= false;
                break;
            case R.id.popup_cancel:// 取消
                dismiss();
                break;
        }
    }

    // 定义一个接口给FirmOrderActivity实现
    public interface StringText{
        void GetStringText(String name,String valuel);
    }

    public void SetStringText(StringText stringText) {
        this.stringText = stringText;
    }
}
