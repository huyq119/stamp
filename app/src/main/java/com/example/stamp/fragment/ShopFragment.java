package com.example.stamp.fragment;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.activity.FirmOrderActivity;
import com.example.stamp.adapter.ExpandableAdapter;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.bean.ShopNameBean;
import com.example.stamp.listener.ShopListenerFace;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.ShoppingCartBiz;

import java.util.ArrayList;

/**
 * 购物车页面
 */
public class ShopFragment extends BaseFragment implements View.OnClickListener {
    private View mShopContent;
    private View mShopTitle;
    private ExpandableListView mContentListView;//主要的ListView
    private TextView mGoToPay;//结算按钮
    private ArrayList<ShopNameBean.SellerBean> mSellerBean;
    private ShopNameBean shopNameBean;
    private ImageView mAll;
    private ExpandableAdapter expandableAdapter;
    private TextView mEdit, mPrice;//编辑按钮,总价格,总数量

    private boolean mEditFlag = true;

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
        mAll = (ImageView) mShopContent.findViewById(R.id.shop_all);
        mEdit = (TextView) mShopTitle.findViewById(R.id.base_search);
        mPrice = (TextView) mShopContent.findViewById(R.id.tv_total_price);
    }

    private void initData() {
        setFalseData();
    }

    private void initListener() {
        mGoToPay.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mAll.setOnClickListener(expandableAdapter.getAdapterListener());
        mContentListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    /**
     * 设置假数据
     */
    private void setFalseData() {
        mSellerBean = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ArrayList<ShopNameBean.GoodsBean> mGoodsBean = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ShopNameBean.GoodsBean goodsBean = new ShopNameBean.GoodsBean("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg", "38596", "庚申年" + i, "1500", "1");
                mGoodsBean.add(goodsBean);
            }
            ShopNameBean.SellerBean sellerBean = new ShopNameBean.SellerBean("淘宝自营商城", "338955", "自营", mGoodsBean);
            mSellerBean.add(sellerBean);
        }
        shopNameBean = new ShopNameBean(mSellerBean, "35000");
        expandableAdapter = new ExpandableAdapter(getActivity(), mBitmap, shopNameBean, mEditFlag);
        mContentListView.setAdapter(expandableAdapter);
        //让子控件全部展开
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            mContentListView.expandGroup(i);
        }
        //去掉自带按钮
        mContentListView.setGroupIndicator(null);
        /**
         * 数据改变的回调
         */
        expandableAdapter.setChangeListener(new ShopListenerFace() {
            @Override
            public void onSelectItem(boolean isSelectedAll) {
                ShoppingCartBiz.checkItem(isSelectedAll, mAll);
            }

            @Override
            public void onDataChange(String selectCount, String selectMoney) {
                mPrice.setText(selectMoney);
                mGoToPay.setText("结算(" + selectCount + ")");
            }
        });
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
            case R.id.base_search://编辑按钮
                if (mEditFlag) {
                    mEdit.setText("完成");
                    mEditFlag = false;
                } else {
                    mEdit.setText("编辑");
                    mEditFlag = true;
                }
                MyLog.e(mEditFlag + "");
                expandableAdapter = new ExpandableAdapter(getActivity(), mBitmap, shopNameBean, mEditFlag);
                mContentListView.setAdapter(expandableAdapter);
                //让子控件全部展开
                for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
                    mContentListView.expandGroup(i);
                }
                //去掉自带按钮
                mContentListView.setGroupIndicator(null);
                break;
            default:
                break;
        }
    }
}


