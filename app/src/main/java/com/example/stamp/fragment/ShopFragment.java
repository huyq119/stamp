package com.example.stamp.fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.activity.FirmOrderActivity;
import com.example.stamp.adapter.ExpandableAdapter;
import com.example.stamp.adapter.ShopcartExpandableListViewAdapter;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.bean.ShopNameBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车页面
 */
public class ShopFragment extends BaseFragment implements ShopcartExpandableListViewAdapter.ModifyCountInterface, View.OnClickListener {
    private View mShopContent;
    private View mShopTitle;
    private ExpandableListView mContentListView;//主要的ListView
    private TextView mGoToPay;//结算按钮
    private TextView mShopEditer;
    private ShopcartExpandableListViewAdapter adapter;
    private LinearLayout mLinearlayout;
    private ArrayList<ShopNameBean.SellerBean> mSellerBean;
    private ShopNameBean shopNameBean;
    private CheckBox mCheckBox;
    private ExpandableAdapter expandableAdapter;
    private TextView mTotalPrice;
    private boolean isDel = true;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private int checkNum; // 记录选中的条目数量
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
        mShopEditer = (TextView) mShopTitle.findViewById(R.id.base_search);
        mCheckBox = (CheckBox) mShopContent.findViewById(R.id.all_checkbox);
        mTotalPrice = (TextView) mShopContent.findViewById(R.id.tv_total_price);
        mLinearlayout = (LinearLayout) mShopContent.findViewById(R.id.shop_linear);
    }
    private void initData() {
        setFalseData();
    }
    private void initListener() {
        mGoToPay.setOnClickListener(this);
        mShopEditer.setOnClickListener(this);
        mCheckBox.setOnClickListener(this);
    }
    /**
     * 设置假数据
     */
    private void setFalseData() {
        ArrayList<ShopNameBean.GoodsBean> mGoodsBean = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShopNameBean.GoodsBean goodsBean = new ShopNameBean.GoodsBean("http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg", "38596", "庚申年" + i, 1500, 1);
            mGoodsBean.add(goodsBean);
        }
        mSellerBean = new ArrayList<ShopNameBean.SellerBean>();
        for (int i = 0; i < 5; i++) {
            ShopNameBean.SellerBean sellerBean = new ShopNameBean.SellerBean("淘宝自营商城", "338955", "自营", mGoodsBean);
            mSellerBean.add(sellerBean);
        }
        shopNameBean = new ShopNameBean(mSellerBean, "35000");
        expandableAdapter = new ExpandableAdapter(getActivity(), mBitmap, shopNameBean);
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
            case R.id.base_search:
                if (mShopEditer != null && isDel) {
                    isDel = false;
                    mShopEditer.setText("完成");
                    adapter = new ShopcartExpandableListViewAdapter(getActivity(), mBitmap, shopNameBean);
                    adapter.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
                    // adapter.setCheckInterface(this);// 关键步骤1,设置复选框接口
                    adapter.notifyDataSetChanged();
                    mContentListView.setAdapter(adapter);
                    mGoToPay.setText("删除");
                    mLinearlayout.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        mContentListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                    }
                } else if (!isDel && mShopEditer != null) {
                    isDel = true;
                    mShopEditer.setText("编辑");
                    expandableAdapter = new ExpandableAdapter(getActivity(), mBitmap, shopNameBean);
                    mContentListView.setAdapter(expandableAdapter);
                    expandableAdapter.notifyDataSetChanged();
                    mLinearlayout.setVisibility(View.VISIBLE);
                    mGoToPay.setText("结算(0)");
                    for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
                        mContentListView.expandGroup(i);
                    }
                }
                break;
            case R.id.all_checkbox:
                if(mCheckBox.isClickable()==true){
                    // 遍历list的长度，将已选的设为未选，未选的设为已选
                    for (int i = 0; i < shopNameBean.getSeller_list().size(); i++) {
                        if (ExpandableAdapter.getIsSelected().get(i)) {
                            ExpandableAdapter.getIsSelected().put(i, false);
                            checkNum--;
                        } else {
                            ExpandableAdapter.getIsSelected().put(i, true);
                            checkNum++;
                        }
                    }
                    // 通知listView刷新
                    expandableAdapter.notifyDataSetChanged();
                    calculate();

                }
                break;
            default:
                break;
        }
    }



    /***
     * 增加操作
     *
     * @param i
     * @param i1
     * @param showCountView
     * @param isChecked
     */
    @Override
    public void doIncrease(int i, int i1, View showCountView, boolean isChecked,ArrayList<ShopNameBean.GoodsBean> goods_list) {
        ShopNameBean.GoodsBean product = (ShopNameBean.GoodsBean) adapter.getChild(i, i1);
        int currentCount = product.goods_count;
        currentCount++;
        product.setGoods_count(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        adapter.notifyDataSetChanged();
    }

    /***
     * 减少操作
     * @param i
     * @param i1
     * @param showCountView
     * @param isChecked
     */

    @Override
    public void doDecrease(int i, int i1, View showCountView, boolean isChecked) {
        ShopNameBean.GoodsBean product = (ShopNameBean.GoodsBean) adapter.getChild(i, i1);
        int currentCount = product.goods_count;
        if (currentCount == 1)
            return;
        currentCount--;
        product.setGoods_count(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        adapter.notifyDataSetChanged();
    }

    /*@Override
    public void checkGroup(int position, boolean isChecked) {
        ShopNameBean.SellerBean group = shopNameBean.seller_list.get(position);
        List<ShopNameBean.GoodsBean> childs = shopNameBean.seller_list.get(position).getGoods_list();
        for (int i = 0; i < childs.size(); i++)
        {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck()) {
            mCheckBox.setChecked(true);
        }else {
            mCheckBox.setChecked(false);
            adapter.notifyDataSetChanged();
        }
    }*/
   /* @Override
    public void checkChild(int position, int i1, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        ShopNameBean.SellerBean group = shopNameBean.getSeller_list().get(i1);
        List<ShopNameBean.GoodsBean> childs = shopNameBean.seller_list.get(i1).getGoods_list();
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).isChoosed() != isChecked){
                allChildSameState = false;
                break;
            }
        }
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }
        if (isAllCheck()){
            mCheckBox.setChecked(true);
        }else {
            mCheckBox.setChecked(false);
            adapter.notifyDataSetChanged();
            calculate();
        }
    }
    private boolean isAllCheck() {
        for (ShopNameBean.SellerBean group : shopNameBean.getSeller_list()) {
            if (!group.isChoosed()) {
                return false;
            }
        }
        return true;
    }*/
    /** 全选与反选 */
    private void doCheckAll() {
        if (mCheckBox.isClickable() == true) {
            // 遍历list的长度，将已选的设为未选，未选的设为已选
            for (int i = 0; i < shopNameBean.getSeller_list().size(); i++) {
                if (ShopcartExpandableListViewAdapter.getIsSelected().get(i)) {
                    ShopcartExpandableListViewAdapter.getIsSelected().put(i, false);
                    checkNum--;
                } else {
                    ShopcartExpandableListViewAdapter.getIsSelected().put(i, true);
                    checkNum++;
                }
            }
            // 刷新listview和TextView的显示
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shopNameBean.getSeller_list().size(); i++){
            ShopNameBean.SellerBean group = shopNameBean.getSeller_list().get(i);
            List<ShopNameBean.GoodsBean> childs = shopNameBean.seller_list.get(i).getGoods_list();
            for (int j = 0; j < childs.size(); j++) {
                ShopNameBean.GoodsBean  product  = childs.get(j);
                if (product.isChoosed()) {
                    totalCount++;
                    totalPrice += product.getGoods_price() * product.getGoods_count();
                }
            }
            mTotalPrice.setText("￥" + totalPrice);
        }
    }
}


