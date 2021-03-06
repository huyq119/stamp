package cn.com.chinau.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.SelfMallDetailActivity;
import cn.com.chinau.activity.StampDetailActivity;
import cn.com.chinau.adapter.ExpandableAdapter;
import cn.com.chinau.base.BaseFragment;
import cn.com.chinau.bean.AddShopCartBean;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.listener.ShopListenerFace;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SPUtils;
import cn.com.chinau.utils.ShoppingCartBiz;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 购物车页面
 */

public class ShopFragment extends BaseFragment implements ExpandableAdapter.SellerList {
    private View mShopContent;
    private View mShopTitle;
    private ExpandableListView mContentListView;//主要的ListView
    private TextView mGoToPay;//结算按钮
    //    private ArrayList<ShopNameBean.SellerBean> mSellerBean;
    private ArrayList<ShopNameBean.SellerBean> mSellerList;
    private ShopNameBean shopNameBean;
    private ImageView mAll;
    private ExpandableAdapter expandableAdapter;
    private TextView mEdit, mPrice, mShopTv, mShopDelete;//编辑按钮,总价格,总数量
    private String mToken, mUser_id;
    //    private boolean mEditFlag = true;// 点击编辑状态的标志
    private SharedPreferences sp;
    private LinearLayout mShopLayout, mShopLinear;
    private String mTotalAmount;

    private ShopNameBean mShopNameBean;

    @Override
    public View CreateTitle() {
        mShopTitle = View.inflate(getActivity(), R.layout.fragment_shop_title, null);
        return mShopTitle;
    }

    @Override
    public View CreateSuccess() {
        mShopContent = View.inflate(getActivity(), R.layout.fragment_shop_content, null);
        sp = getActivity().getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        initData();
        return mShopContent;
    }

    private void initView() {

        mGoToPay = (TextView) mShopContent.findViewById(R.id.Shop_pay);// 去结算
        mContentListView = (ExpandableListView) mShopContent.findViewById(R.id.shop_ELV);
        mAll = (ImageView) mShopContent.findViewById(R.id.shop_all);// 全选
        mEdit = (TextView) mShopTitle.findViewById(R.id.base_search); // 编辑
        mPrice = (TextView) mShopContent.findViewById(R.id.tv_total_price); // 价格
        mShopLayout = (LinearLayout) mShopContent.findViewById(R.id.linear_shop_layout);// 结算行
        mShopTv = (TextView) mShopContent.findViewById(R.id.shop_tv);
        mShopLinear = (LinearLayout) mShopContent.findViewById(R.id.shop_linear);// 合计价格ll
        mShopDelete = (TextView) mShopContent.findViewById(R.id.Shop_delete);// 删除
    }

    private void initData() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        if (TextUtils.isEmpty(mToken) && TextUtils.isEmpty(mUser_id)) {
//            mEdit.setVisibility(View.GONE);
//            mContentListView.setVisibility(View.GONE);// list隐藏
//            mShopLayout.setVisibility(View.GONE); // 结算ll
//            mShopTv.setVisibility(View.VISIBLE); // 购物车为空是显示的布局
            String sg = (String) SPUtils.get(getActivity(), StaticField.SHOPJSON, "");
            if(!TextUtils.isEmpty(sg)) {
                shopNameBean = new Gson().fromJson(sg, ShopNameBean.class);
                initAdapter();
                initListener();
            }
        } else {

            String sg = (String) SPUtils.get(getActivity(), StaticField.SHOPJSON, "");
            if(!TextUtils.isEmpty(sg)) {
                shopNameBean = new Gson().fromJson(sg, ShopNameBean.class);
                MyLog.LogShitou("====sg","====/"+sg+"/==/"+shopNameBean);
                if (shopNameBean != null){

                    ArrayList<ShopNameBean.SellerBean> seller_list = shopNameBean.getSeller_list();
                    if (seller_list != null && seller_list.size() != 0) {
                        commitData();
                    } else {
                        ShopRequestNet(); //  购物车网络请求
                    }
                }
            }
        }
    }

    /**
     * 提交本地数据的网络请求
     */
    private void commitData() {
        //保存提交服务器的集合
        final ArrayList<AddShopCartBean> list = new ArrayList<>();
        //保存的实体类
        ArrayList<ShopNameBean.SellerBean> seller_list = shopNameBean.getSeller_list();
        for (int i = 0; i < seller_list.size(); i++) {
            ArrayList<ShopNameBean.SellerBean.GoodsBean> goods_list = seller_list.get(i).getGoods_list();
            for (int j = 0; j < goods_list.size(); j++) {
                ShopNameBean.SellerBean.GoodsBean goodsBean = goods_list.get(j);
                String goods_sn = goodsBean.getGoods_sn();
                String goods_count = goodsBean.getGoods_count();
                AddShopCartBean addShopCartBean = new AddShopCartBean(goods_sn, goods_count);
                list.add(addShopCartBean);
            }
        }
        MyLog.e("提交数据的信息" + seller_list.toString());

        //开始请求网络
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SHOPCARTMODIFY);// 接口名称

                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODESINFO, list.toString());//  商品信息：所有商品的json字符串
                params.put(StaticField.OP_TYPE, "HB");// 操作类型：

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("提交数据接口已经执行" + mToken + mUser_id, result);

                mHandler.sendEmptyMessage(1);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                BaseBean mBasebean = new Gson().fromJson(result, BaseBean.class);
                if (mBasebean.getRsp_code().equals("0000")) {
                    MyLog.LogShitou("提交数据接口已经成功清除数据", result);
                    SPUtils.put(getContext(), StaticField.SHOPJSON, new Gson().toJson(new ShopNameBean()));
                }
            }
        });
    }

    private void initListener() {

        mGoToPay.setOnClickListener(expandableAdapter.getAdapterListener());// 去結算

        mShopDelete.setOnClickListener(expandableAdapter.getAdapterListener()); // 刪除
        mEdit.setOnClickListener(expandableAdapter.getAdapterListener()); // 编辑
        mAll.setOnClickListener(expandableAdapter.getAdapterListener()); //  全选
        mContentListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Bundle bundle = new Bundle();
                // 获取父控件卖家类型get(i)
                String mType = shopNameBean.getSeller_list().get(i).getSeller_type();
                // 获取父控件里的子控件商品编号get(i).get(i1)
                String mGoods_sn = shopNameBean.getSeller_list().get(i).getGoods_list().get(i1).getGoods_sn();
                MyLog.LogShitou("商家--编号", mType + "--" + mGoods_sn);
                if (mType.equals("SC_ZY")) {
                    bundle.putString(StaticField.GOODS_SN, mGoods_sn);
                    openActivityWitchAnimation(SelfMallDetailActivity.class, bundle);
                } else if (mType.equals("YS")) {
                    bundle.putString(StaticField.GOODS_SN, mGoods_sn);
                    openActivityWitchAnimation(StampDetailActivity.class, bundle);
                } else if (mType.equals("SC_DSF")) {
                    bundle.putString(StaticField.GOODS_SN, mGoods_sn);
                    openActivityWitchAnimation(SelfMallDetailActivity.class, bundle);
                }
                return false;
            }
        });

        // 点击不可回缩
        mContentListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    /**
     * 添加数据
     */
    private void initAdapter() {
        expandableAdapter = new ExpandableAdapter(getActivity(), mBitmap, shopNameBean, mShopLinear, mShopDelete, mGoToPay);
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

                mPrice.setText("￥" + selectMoney);
                mGoToPay.setText("结算(" + selectCount + ")");
//                initAdapter(); // 添加数据
//                initListener();

            }
        });
        expandableAdapter.setSellerList(this);// 回调刷新购物车数据


    }



   /* @Override
    public void o() {
        ShopRequestNet(); //  购物车网络请求
        MyLog.LogShitou("====1111=======再次回到此Fragment","再次回到此Fragment的操作");
        super.onResume();
    }*/


    @Override
    public void AgainRequest() {

    }


    /**
     * 购物车List网络请求
     */
    private void ShopRequestNet() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SHOPCARTQUERY);// 接口名称
                params.put(StaticField.TOKEN, mToken); // 登录标识
                params.put(StaticField.USER_ID, mUser_id); // 用户ID

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("result购物车list", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:
                    Gson gson = new Gson();
                    mShopNameBean = gson.fromJson((String) msg.obj, ShopNameBean.class);
                    String code = mShopNameBean.getRsp_code();
                    if (code.equals("0000")) {
//                        // 总价
                        mTotalAmount = mShopNameBean.getGoods_total_amount();
                        mSellerList = mShopNameBean.getSeller_list(); // 商品List
                        MyLog.LogShitou("mSellerList=================", "" + mSellerList);
                        shopNameBean = new ShopNameBean(mSellerList, mTotalAmount);
                        initAdapter(); // 添加数据
                        initListener();
                    }
                    break;
                case 1://购物车提交网路的请求
                    ShopRequestNet(); //  购物车网络请求
                    break;
            }
        }
    };


    // 回调删除后刷新的List
    @Override
    public void GetSellerList(ShopNameBean shopNameBeans) {
        shopNameBean = shopNameBeans;
        initAdapter(); // 添加数据
        initListener();
    }
}


