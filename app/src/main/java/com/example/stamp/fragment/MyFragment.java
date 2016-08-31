package com.example.stamp.fragment;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.activity.AuctionRecordActivity;
import com.example.stamp.activity.LoginActivity;
import com.example.stamp.activity.ManagerAddressActivity;
import com.example.stamp.activity.MyCollectionActivity;
import com.example.stamp.activity.MyStampActivity;
import com.example.stamp.activity.OrderActivity;
import com.example.stamp.activity.OrdersForGoodsDetialsActivity;
import com.example.stamp.activity.PressWordManagerActivity;
import com.example.stamp.activity.UsMeActivity;
import com.example.stamp.activity.WithdrawActivity;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.utils.SPUtils;


/**
 * 我的页面
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {


    private View myContentView;//内容页面
    private LinearLayout mLogin, mNoLogin, mQuite, mPressWord, mUsMe, mStamp, mRecord, mCollection, mAddress,mOrderBack;//登录成功页面,没有登录页面,退出登录,密码管理,关于我们,我的邮集,竞拍记录,我的收藏.收获地址,回购订单
    private TextView mWithdraw,mPayment,mReceive,mComplete,mRefused;//提现
    private LinearLayout mOrderGoods;

    @Override
    public View CreateTitle() {
        return View.inflate(getActivity(), R.layout.fragment_my_title, null);
    }

    @Override
    public View CreateSuccess() {
        myContentView = View.inflate(getActivity(), R.layout.fragment_my_content, null);
        initView();
        initData();
        initListener();
        return myContentView;
    }


    /**
     * 初始化布局
     */
    private void initView() {
        mLogin = (LinearLayout) myContentView.findViewById(R.id.my_login);
        mNoLogin = (LinearLayout) myContentView.findViewById(R.id.my_noLogin);
        mQuite = (LinearLayout) myContentView.findViewById(R.id.my_quit_login);
        mUsMe = (LinearLayout) myContentView.findViewById(R.id.my_usMe);
        mPressWord = (LinearLayout) myContentView.findViewById(R.id.my_PressWordManager);
        mWithdraw = (TextView) myContentView.findViewById(R.id.my_withdraw);
        mStamp = (LinearLayout) myContentView.findViewById(R.id.my_Stamp);
        mRecord = (LinearLayout) myContentView.findViewById(R.id.my_auctionRecord);
        mCollection = (LinearLayout) myContentView.findViewById(R.id.my_collection);
        mAddress = (LinearLayout) myContentView.findViewById(R.id.my_Address);
        mOrderBack = (LinearLayout) myContentView.findViewById(R.id.my_order_buy_back);
        mOrderGoods = (LinearLayout) myContentView.findViewById(R.id.my_orders_goods);
        mPayment = (TextView)myContentView.findViewById(R.id.text_payment);
        mReceive =(TextView)myContentView.findViewById(R.id.receive);
        mComplete =(TextView)myContentView.findViewById(R.id.complete);
        mRefused = (TextView)myContentView.findViewById(R.id.refuse);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //判断是否登录,看显示的内容
        if (!isLogin()) {//登录
            mLogin.setVisibility(View.VISIBLE);
            mNoLogin.setVisibility(View.GONE);
            mQuite.setVisibility(View.VISIBLE);
        } else {//没有登录
            mLogin.setVisibility(View.GONE);
            mNoLogin.setVisibility(View.VISIBLE);
            mQuite.setVisibility(View.INVISIBLE);
        }
    }

    private void initListener() {
        mNoLogin.setOnClickListener(this);
        mUsMe.setOnClickListener(this);
        mWithdraw.setOnClickListener(this);
        mPressWord.setOnClickListener(this);
        mStamp.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mOrderBack.setOnClickListener(this);
        mOrderGoods.setOnClickListener(this);
        mPayment.setOnClickListener(this);
        mReceive.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        mRefused.setOnClickListener(this);

    }

    @Override
    public void AgainRequest() {

    }

    /**
     * 是否登录
     *
     * @return 是否登录 登录返回true,否则返回false
     */
    public boolean isLogin() {
        return !TextUtils.isEmpty((String) SPUtils.get(getActivity(), StaticField.ISPHONE, ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_noLogin://没有登录
                openActivityWitchAnimation(LoginActivity.class);
                break;
            case R.id.my_usMe://关于我们
                openActivityWitchAnimation(UsMeActivity.class);
                break;
            case R.id.my_withdraw://提现
                openActivityWitchAnimation(WithdrawActivity.class);
                break;
            case R.id.my_PressWordManager://密码管理
                openActivityWitchAnimation(PressWordManagerActivity.class);
                break;
            case R.id.my_Stamp://我的邮集
                openActivityWitchAnimation(MyStampActivity.class);
                break;
            case R.id.my_auctionRecord://竞拍记录
                openActivityWitchAnimation(AuctionRecordActivity.class);
                break;
            case R.id.my_collection://我的收藏
                openActivityWitchAnimation(MyCollectionActivity.class);
                break;
            case R.id.my_Address://收获地址管理
                openActivityWitchAnimation(ManagerAddressActivity.class);
                break;
            case R.id.my_order_buy_back://回购订单
                openActivityWitchAnimation(OrderActivity.class);
                break;
            case R.id.my_orders_goods:
                openActivityWitchAnimation(OrdersForGoodsDetialsActivity.class);
                break;
            case R.id.text_payment:
                Intent intent = new Intent(getActivity(),
                        OrdersForGoodsDetialsActivity.class);
                intent.putExtra("tag",1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.receive:
                Intent receive = new Intent(getActivity(),
                        OrdersForGoodsDetialsActivity.class);
                receive.putExtra("tag", 2);
                startActivity(receive);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.complete:
                Intent complete = new Intent(getActivity(),
                        OrdersForGoodsDetialsActivity.class);
                complete.putExtra("tag", 3);
                startActivity(complete);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.refuse:
                Intent refuse = new Intent(getActivity(),
                        OrdersForGoodsDetialsActivity.class);
                refuse.putExtra("tag", 4);
                startActivity(refuse);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            default:
                break;
        }
    }
}
