package com.example.stamp.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.FirmOrderExpandableAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.ShopNameBean;
import com.example.stamp.dialog.SelectDistributionPopupWindow;
import com.example.stamp.dialog.SelectPayPopupWindow;
import com.example.stamp.utils.MyToast;

import java.util.ArrayList;

/**
 * 确认订单页面
 */
public class FirmOrderActivity extends BaseActivity implements View.OnClickListener {

    private View mFirmOrderTitle;
    private View mFirmOrderContent;
    private LinearLayout mAddress, mNoAddress;//有收货地址,没有收货地址
    private LinearLayout mPay, mDistribution;//支付方式,配送方式
    private SelectPayPopupWindow mPayPopupWindow;//支付的弹出框
    private SelectDistributionPopupWindow mDistributionPopupWindow;//配送方式的弹出框
    private ExpandableListView mListView;//底部列表展示
    private ImageView mBack;
    private TextView mTitle,mOkPay;

    @Override
    public View CreateTitle() {
        mFirmOrderTitle = View.inflate(this, R.layout.base_back_title, null);
        return mFirmOrderTitle;
    }

    @Override
    public View CreateSuccess() {
        mFirmOrderContent = View.inflate(this, R.layout.activity_firmorder, null);
        initView();
        initData();
//        initAdapter();
        initListener();
        return mFirmOrderContent;
    }




    private void initView() {
        mBack =(ImageView) mFirmOrderTitle.findViewById(R.id.base_title_back);
        mTitle =(TextView) mFirmOrderTitle.findViewById(R.id.base_title);
        mTitle.setText("确认订单");
        mAddress = (LinearLayout) mFirmOrderContent.findViewById(R.id.FirmOrder_Address);
        mNoAddress = (LinearLayout) mFirmOrderContent.findViewById(R.id.FirmOrder_NoAddress);
        mPay = (LinearLayout) mFirmOrderContent.findViewById(R.id.FirmOrder_pay);
        mDistribution = (LinearLayout) mFirmOrderContent.findViewById(R.id.FirmOrder_distribution);
        mListView = (ExpandableListView) mFirmOrderContent.findViewById(R.id.firmOrder_expandableLV);
        mOkPay = (TextView) mFirmOrderContent.findViewById(R.id.firmOrder_ok_pay);
        //TODO	服务费，邮市、竞拍商品包含服务费，商城商品没有。

    }

    private void initData() {
        //收货地址的显示
//        mAddress.setVisibility(isAddress() ? View.VISIBLE : View.GONE);
//        mNoAddress.setVisibility(isAddress() ? View.GONE : View.VISIBLE);
        setFalseData();
    }



//    private void initAdapter() {
//
//        mListView.setAdapter();
//    }

    private void initListener() {
        mPay.setOnClickListener(this);
        mDistribution.setOnClickListener(this);
        mNoAddress.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mOkPay.setOnClickListener(this);
        mAddress.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }
    /**
     * 设置假数据
     */
    private void setFalseData() {
        ArrayList<ShopNameBean.GoodsBean> mGoodsBean = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShopNameBean.GoodsBean goodsBean = new ShopNameBean.GoodsBean("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg", "38596", "庚申年" + i, "1500", "1");
            mGoodsBean.add(goodsBean);
        }

        ArrayList<ShopNameBean.SellerBean> mSellerBean = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShopNameBean.SellerBean sellerBean = new ShopNameBean.SellerBean("淘宝自营商城", "338955", "自营", mGoodsBean);
            mSellerBean.add(sellerBean);
        }

        ShopNameBean shopNameBean = new ShopNameBean(mSellerBean, "35000");

        FirmOrderExpandableAdapter expandableAdapter = new FirmOrderExpandableAdapter(this, mBitmap, shopNameBean);
        mListView.setAdapter(expandableAdapter);
        //让子控件全部展开
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
        //去掉自带按钮
        mListView.setGroupIndicator(null);
    }

    /**
     * 是否有收货地址的判断
     *
     * @return true, 有收获地址 false,没有收获地址
     */
    public boolean isAddress() {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FirmOrder_pay://支付方式
                mPayPopupWindow = new SelectPayPopupWindow(this, mPayWindow);
                //设置layout在PopupWindow中显示的位置,因为这里写了全屏的所以就没有居中
                mPayPopupWindow.showAtLocation(this.findViewById(R.id.FirmOrder), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.FirmOrder_distribution://配送方式
                mDistributionPopupWindow = new SelectDistributionPopupWindow(this, mDistributionWindow);
                mDistributionPopupWindow.showAtLocation(this.findViewById(R.id.FirmOrder), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.FirmOrder_NoAddress://没有收获地址
//                openActivityWitchAnimation(ManagerAddressActivity.class);
                break;
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.firmOrder_ok_pay://确认并付款
                MyToast.showShort(this,"调取微信或者支付宝页面。。。");
                break;
            case R.id.FirmOrder_Address://收货地址选择
                MyToast.showShort(this,"跳转至选择收货地址。。。");
                break;
        }
    }

    /**
     * 支付方式的内部监听
     */
    private View.OnClickListener mPayWindow = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPayPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.Wechat_click://点击了微信按钮
                    MyToast.showShort(FirmOrderActivity.this, "点击了微信按钮");
                    break;
                case R.id.Alipay_click://点击了支付宝按钮
                    MyToast.showShort(FirmOrderActivity.this, "点击了支付宝按钮");
                    break;
            }
        }
    };
    /**
     * 配送方式的内部监听
     */
    private View.OnClickListener mDistributionWindow = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mDistributionPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.EMS_click://Ems的点击按钮
                    MyToast.showShort(FirmOrderActivity.this, "点击了EMS按钮");
                    break;
                case R.id.Wind_click://顺丰的点击按钮
                    MyToast.showShort(FirmOrderActivity.this, "点击了顺丰按钮");
                    break;
            }
        }
    };
}
