package cn.com.chinau.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import cn.com.chinau.activity.FirmOrderActivity;
import cn.com.chinau.adapter.ExpandableAdapter;
import cn.com.chinau.base.BaseFragment;
import cn.com.chinau.bean.ShopNameBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.listener.ShopListenerFace;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.ShoppingCartBiz;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 购物车页面
 */
public class ShopFragment extends BaseFragment implements View.OnClickListener {
    private View mShopContent;
    private View mShopTitle;
    private ExpandableListView mContentListView;//主要的ListView
    private TextView mGoToPay;//结算按钮
    private ArrayList<ShopNameBean.SellerBean> mSellerBean;
    private ArrayList<ShopNameBean.SellerBean> mSellerList;
    private ShopNameBean shopNameBean;
    private ImageView mAll;
    private ExpandableAdapter expandableAdapter;
    private TextView mEdit, mPrice,mShopTv;//编辑按钮,总价格,总数量
    private String  mToken, mUser_id;
    private boolean mEditFlag = true;
    private SharedPreferences sp;
    private LinearLayout mShopLayout;
    private String mTotalAmount;
    @Override
    public View CreateTitle() {
        mShopTitle = View.inflate(getActivity(), R.layout.fragment_shop_title, null);
        return mShopTitle;
    }

    @Override
    public View CreateSuccess() {
        mShopContent = View.inflate(getActivity(), R.layout.fragment_shop_content, null);
        sp =getActivity().getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        initData();
        return mShopContent;
    }

    private void initView() {

        mGoToPay = (TextView) mShopContent.findViewById(R.id.Shop_pay);
        mContentListView = (ExpandableListView) mShopContent.findViewById(R.id.shop_ELV);
        mAll = (ImageView) mShopContent.findViewById(R.id.shop_all);
        mEdit = (TextView) mShopTitle.findViewById(R.id.base_search);
        mPrice = (TextView) mShopContent.findViewById(R.id.tv_total_price);
        mShopLayout = (LinearLayout) mShopContent.findViewById(R.id.linear_shop_layout);
        mShopTv = (TextView) mShopContent.findViewById(R.id.shop_tv);
    }


    // shif
    private void initData() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        if (TextUtils.isEmpty(mToken) && TextUtils.isEmpty(mUser_id)){
            mEdit.setVisibility(View.GONE);
            mContentListView.setVisibility(View.GONE);// list隐藏
            mShopLayout.setVisibility(View.GONE); // 结算ll
            mShopTv.setVisibility(View.VISIBLE); // 购物车为空是显示的布局
        }else{
            ShopRequestNet(); //  购物车网络请求
        }
    }

    private void initListener() {
        mGoToPay.setOnClickListener(this);
        mEdit.setOnClickListener(expandableAdapter.getAdapterListener());
        mAll.setOnClickListener(expandableAdapter.getAdapterListener());
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
        shopNameBean = new ShopNameBean(mSellerList, mTotalAmount);
        expandableAdapter = new ExpandableAdapter(getActivity(), mBitmap, shopNameBean);
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
                mPrice.setText("￥"+selectMoney);
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
//            case R.id.base_search://编辑按钮
//                if (mEditFlag) {
//                    mEdit.setText("完成");
//                    mEditFlag = false;
//                } else {
//                    mEdit.setText("编辑");
//                    mEditFlag = true;
//                }
//                MyLog.e(mEditFlag + "");
//                expandableAdapter = new ExpandableAdapter(getActivity(), mBitmap, shopNameBean, mEditFlag);
//                mContentListView.setAdapter(expandableAdapter);
//                //让子控件全部展开
//                for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
//                    mContentListView.expandGroup(i);
//                }
//                //去掉自带按钮
//                mContentListView.setGroupIndicator(null);
//                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StaticField.SUCCESS:
                    Gson gson = new Gson();
                    ShopNameBean shopNameBean = gson.fromJson((String)msg.obj,ShopNameBean.class);
                    String code = shopNameBean.getRsp_code();
                    if(code.equals("0000")){
                        // 总价
                        mTotalAmount = shopNameBean.getGoods_total_amount();
                        mSellerList =  shopNameBean.getSeller_list(); // 商品List
                        initAdapter(); // 添加数据
                        initListener();
                    }
                break;
            }
        }
    };

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

                MyLog.LogShitou("result购物车list",result);

                if (result.equals("-1") |result.equals("-2")  ) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

}


