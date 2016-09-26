package com.example.stamp.fragment.popfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stamp.R;
import com.example.stamp.adapter.SelfMallPanStampGridViewAdapter;
import com.example.stamp.base.BaseDialogFragment;
import com.example.stamp.dialog.SelfMallPanStampFilterDialog;
import com.example.stamp.listener.SellMallPanStampGridViewOnItemClickListener;
import com.example.stamp.utils.MyLog;
import com.example.stamp.view.NoScrollGridView;

/**
 * 淘邮票筛选（竞拍）Fragment
 */
public class AuctionFilterFragment extends BaseDialogFragment implements SellMallPanStampGridViewOnItemClickListener.SelfMallItemClick{

    private String[] arrGoods = {"年册", "年册", "年册", "年册"};
    private String[] arrMetal = {"金砖", "金砖", "金砖"};
    private String[] arrItems= {"邮封", "邮封", "邮封"};
    private String[] arrBrand= {"中国集邮总公司", "邮来邮网"};
    private View mFilterView;
    private int Current = -1;//当前选择的年代角标
    private SellMallPanStampGridViewOnItemClickListener mGoodsListener, mMetalListener,mItemsListener,mBrandListener;
    private View mSelfMall;
    private String mData;


    public AuctionFilterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSelfMall = View.inflate(getActivity(), R.layout.fragment_selfmall_auction_filter_dialog, null);
        //设置GridView的数据
        setPopupWindowData();
//        RequestNet();
        return mSelfMall;
    }

    /**
     * 设置PopupWindow页面的数据
     */
    private void setPopupWindowData() {
        //竞品邮集的GridView
        NoScrollGridView mGoodsGV = (NoScrollGridView) mSelfMall.findViewById(R.id.auction_ompeting_goods_GV);
        //贵金属邮票的GridView
        NoScrollGridView mMetalGV = (NoScrollGridView) mSelfMall.findViewById(R.id.auction_precious_metal_GV);
        //邮品的GridView
        NoScrollGridView mItemsGV = (NoScrollGridView) mSelfMall.findViewById(R.id.auction_philatelic_items_GV);
        //品牌的GridView
        NoScrollGridView mBrandGV = (NoScrollGridView) mSelfMall.findViewById(R.id.auction_brand_GV);

        //创建适配器
        SelfMallPanStampGridViewAdapter mGoodsAdapter =  new SelfMallPanStampGridViewAdapter(getActivity(), arrGoods);
        SelfMallPanStampGridViewAdapter mCategoryAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), arrMetal);
        SelfMallPanStampGridViewAdapter mItemsAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), arrItems);
        SelfMallPanStampGridViewAdapter mBrandAdapter = new SelfMallPanStampGridViewAdapter(getActivity(), arrBrand);

        //设置适配器
        mGoodsGV.setAdapter(mGoodsAdapter);
        mMetalGV.setAdapter(mCategoryAdapter);
        mItemsGV.setAdapter(mItemsAdapter);
        mBrandGV.setAdapter(mBrandAdapter);

        //设置监听
        //竞品邮集的监听
        mGoodsListener = new SellMallPanStampGridViewOnItemClickListener(Current, mGoodsAdapter);
        mGoodsListener.setSelfMallItemClick(this);
        mGoodsGV.setOnItemClickListener(mGoodsListener);
        //贵金属邮票的监听
        mMetalListener = new SellMallPanStampGridViewOnItemClickListener(Current, mCategoryAdapter);
        mMetalListener.setSelfMallItemClick(this);
        mMetalGV.setOnItemClickListener(mMetalListener);
        //邮品的监听
        mItemsListener = new SellMallPanStampGridViewOnItemClickListener(Current, mItemsAdapter);
        mItemsListener.setSelfMallItemClick(this);
        mItemsGV.setOnItemClickListener(mItemsListener);
        //品牌的监听
        mBrandListener = new SellMallPanStampGridViewOnItemClickListener(Current, mBrandAdapter);
        mBrandListener.setSelfMallItemClick(this);
        mBrandGV.setOnItemClickListener(mBrandListener);

        SelfMallPanStampFilterDialog selfMallPanStampFilterDialog = (SelfMallPanStampFilterDialog) getParentFragment();
        selfMallPanStampFilterDialog.setClickReset(new SelfMallPanStampFilterDialog.ClickReset() {
            @Override
            public void setReset() {
                MyLog.e("ThreeMallFilterFragment-->");
                mGoodsListener.setPosition();
                mMetalListener.setPosition();
                mItemsListener.setPosition();
                mBrandListener.setPosition();
            }
        });
    }

    @Override
    public void GetClickItem() {
        int GoodsNum = mGoodsListener.getPosition();
        int MetalNum = mMetalListener.getPosition();
        int ItemsNum = mItemsListener.getPosition();
        int BrandNum = mBrandListener.getPosition();
        MyLog.e(GoodsNum + "-" + MetalNum + "-" + ItemsNum+"-"+BrandNum);


        String Goods = (GoodsNum == -1) ? "" : arrGoods[GoodsNum];
        String Metal = (MetalNum == -1) ? "" : arrMetal[MetalNum];
        String Items = (ItemsNum == -1) ? "" : arrItems[ItemsNum];
        String Brand = (BrandNum == -1) ? "" : arrBrand[BrandNum];

        mData = Goods + "," + Metal + "," + Items+","+Brand;
        setData(mData);
        MyLog.e("点击了啥002--->"+mData);

    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

}
