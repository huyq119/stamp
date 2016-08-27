package com.example.stamp.fragment;


import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.activity.FirmOrderActivity;
import com.example.stamp.adapter.ExpandableAdapter;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.bean.ShopNameBean;

import java.util.ArrayList;


/**
 * 购物车页面
 */
public class ShopFragment extends BaseFragment implements View.OnClickListener {


    private View mShopContent;
    private View mShopTitle;
    private ExpandableListView mContentListView;//主要的ListView
    private TextView mGoToPay;//结算按钮

    @Override
    public View CreateTitle() {
        mShopTitle = View.inflate(getActivity(), R.layout.fragment_shop_title, null);
        return mShopTitle;
    }

    @Override
    public View CreateSuccess() {
        mShopContent = View.inflate(getActivity(), R.layout.fragment_shop_content, null);
        initView();
        initData();
        initListener();
        return mShopContent;
    }


    private void initView() {
        mGoToPay = (TextView) mShopContent.findViewById(R.id.Shop_pay);
        mContentListView = (ExpandableListView) mShopContent.findViewById(R.id.shop_ELV);

    }

    private void initData() {
        setFalseData();
    }

    private void initListener() {
        mGoToPay.setOnClickListener(this);
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


        ExpandableAdapter expandableAdapter = new ExpandableAdapter(getActivity(), mBitmap, shopNameBean);
        mContentListView.setAdapter(expandableAdapter);
        //让子控件全部展开
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            mContentListView.expandGroup(i);
        }
        //去掉自带按钮
        mContentListView.setGroupIndicator(null);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Shop_pay://结算按钮
                openActivityWitchAnimation(FirmOrderActivity.class);
                break;
        }
    }
}
