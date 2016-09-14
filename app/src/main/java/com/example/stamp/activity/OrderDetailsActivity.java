package com.example.stamp.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.stamp.R;
import com.example.stamp.adapter.FirmOrderExpandableAdapter;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.ShopNameBean;
import com.example.stamp.dialog.SelectDistributionPopupWindow;
import com.example.stamp.dialog.SelectPayPopupWindow;
import com.example.stamp.utils.MyToast;
import com.example.stamp.view.WrapExpandableListView;

import java.util.ArrayList;

/**
 * 订单详情页面
 * 所有的订单页面都在这一个类里面,到时候看看区分显示
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {


    private View mOrderDetailsTitle;
    private View mOrderDetailsContent;
    private LinearLayout mLogistics;//物流信息页面
    private WrapExpandableListView mListView;//数据展示
    private ScrollView mScrollView;
    private LinearLayout mPay;//付款
    private LinearLayout mDistribution;//快递
    private SelectPayPopupWindow mPayPopupWindow;//付款的PopupWindow
    private SelectDistributionPopupWindow mDistributionPopupWindow;//快递弹出的PopupWindow
    private ImageView mBack;

    @Override
    public View CreateTitle() {
        mOrderDetailsTitle = View.inflate(this, R.layout.base_back_title, null);
        return mOrderDetailsTitle;
    }

    @Override
    public View CreateSuccess() {
        mOrderDetailsContent = View.inflate(this, R.layout.activity_order_details, null);
        initView();
        initAdapter();
        initListener();
        return mOrderDetailsContent;
    }


    private void initView() {
        mBack = (ImageView)mOrderDetailsTitle.findViewById(R.id.base_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWitchAnimation();
            }
        });

        mLogistics = (LinearLayout) mOrderDetailsContent.findViewById(R.id.orderDetails_Address);
        mPay = (LinearLayout) mOrderDetailsContent.findViewById(R.id.orderDetails_pay);
        mDistribution = (LinearLayout) mOrderDetailsContent.findViewById(R.id.orderDetails_distribution);
        mListView = (WrapExpandableListView) mOrderDetailsContent.findViewById(R.id.order_details_lv);
        mScrollView = (ScrollView) mOrderDetailsContent.findViewById(R.id.order_details_sv);
        //隐藏地址上面的箭头
        ImageView Arrow = (ImageView) mOrderDetailsContent.findViewById(R.id.firmOrder_back);
        Arrow.setVisibility(View.GONE);
    }


    private void initAdapter() {
        ArrayList<ShopNameBean.GoodsBean> mGoodsBean = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShopNameBean.GoodsBean goodsBean = new ShopNameBean.GoodsBean("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg", "38596", "庚申年" + i,"1500", "1");
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

        mScrollView.smoothScrollTo(0, 0);

    }

    private void initListener() {
        mLogistics.setOnClickListener(this);
        mPay.setOnClickListener(this);
        mDistribution.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.orderDetails_Address://物流信息页面
                openActivityWitchAnimation(LogisticsDetailsActivity.class);
                break;
            case R.id.orderDetails_pay://支付方式
                mPayPopupWindow = new SelectPayPopupWindow(this, mPayWindow);
                //设置layout在PopupWindow中显示的位置,因为这里写了全屏的所以就没有居中
                mPayPopupWindow.showAtLocation(this.findViewById(R.id.order_details), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.orderDetails_distribution://快递方式
                mDistributionPopupWindow = new SelectDistributionPopupWindow(this, mDistributionWindow);
                mDistributionPopupWindow.showAtLocation(this.findViewById(R.id.order_details), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    MyToast.showShort(OrderDetailsActivity.this, "点击了微信按钮");
                    break;
                case R.id.Alipay_click://点击了支付宝按钮
                    MyToast.showShort(OrderDetailsActivity.this, "点击了支付宝按钮");
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
                    MyToast.showShort(OrderDetailsActivity.this, "点击了EMS按钮");
                    break;
                case R.id.Wind_click://顺丰的点击按钮
                    MyToast.showShort(OrderDetailsActivity.this, "点击了顺丰按钮");
                    break;
            }
        }
    };
}
