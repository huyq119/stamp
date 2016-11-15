package cn.com.chinau.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.dialog.PhoneDialog;

/**
 * Created by Administrator on 2016/9/21.
 * （申请成功）退款/退货页面
 */
public class SuccessApplyForRefuseActivity extends BaseActivity implements View.OnClickListener {


    private View mRefuseTitle,mRefuseContent;
    private ImageView mBack;
    private TextView mTitle,mLookOrder,mServiceTel;
    private String mOrder_sn,mGoods_sn,mOrderStatus;

    @Override
    public View CreateTitle() {
        mRefuseTitle = View.inflate(this, R.layout.base_back_title, null);
        return mRefuseTitle;
    }

    @Override
    public View CreateSuccess() {
        mRefuseContent = View.inflate(this, R.layout.activity_success_refuse_intervene, null);
        initView();
        initListener();
        return mRefuseContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        mOrder_sn = bundle.getString(StaticField.ORDER_SN); // 交易订单号
        mGoods_sn = bundle.getString(StaticField.GOODS_SN); // 商品编号
        mOrderStatus = bundle.getString(StaticField.ORDERSTATUS); //订单状态

        mBack =(ImageView) mRefuseTitle.findViewById(R.id.base_title_back);
        mTitle =(TextView) mRefuseTitle.findViewById(R.id.base_title);
        mTitle.setText("退款/退货");
        mLookOrder =(TextView) mRefuseContent.findViewById(R.id.look_order);
        mServiceTel =(TextView) mRefuseContent.findViewById(R.id.Service_tel);

    }
    private void initListener() {
        mBack.setOnClickListener(this);
        mLookOrder.setOnClickListener(this);
        mServiceTel.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back:// 返回
                finishWitchAnimation();

            break;
            case R.id.look_order:// 查看订单
                // 跳转至查看订单详情页面
                Bundle bundle = new Bundle();
                bundle.putString(StaticField.ORDER_SN,mOrder_sn);
                bundle.putString(StaticField.GOODS_SN,mGoods_sn);
                bundle.putString(StaticField.ORDERSTATUS,"TK");
                openActivityWitchAnimation(LookOrderDetailRefuseActivity.class,bundle);
            break;
            case R.id.Service_tel:// 客服电话

                PhoneDialog phoneDialog = new PhoneDialog(this,mServiceTel.getText().toString());
                phoneDialog.show();
            break;
        }
    }
}
