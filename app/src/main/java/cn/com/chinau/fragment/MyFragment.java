package cn.com.chinau.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.AuctionRecordActivity;
import cn.com.chinau.activity.LoginActivity;
import cn.com.chinau.activity.ManagerAddressActivity;
import cn.com.chinau.activity.MyCollectionActivity;
import cn.com.chinau.activity.MyStampActivity;
import cn.com.chinau.activity.OrderBuyBackActivity;
import cn.com.chinau.activity.OrdersGoodsActivity;
import cn.com.chinau.activity.PressWordManagerActivity;
import cn.com.chinau.activity.UsMeActivity;
import cn.com.chinau.activity.WithdrawActivity;
import cn.com.chinau.base.BaseFragment;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.PersonBean;
import cn.com.chinau.dialog.ProgressDialog;
import cn.com.chinau.dialog.QuiteDialog;
import cn.com.chinau.dialog.SendProgressDialog;
import cn.com.chinau.dialog.SussessDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SPUtils;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;


/**
 * 我的页面
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    protected static final int SHOWPROGRESS = 0;// 展示进度条对话框
    protected static final int DELFAIL = 2;// 关闭对话框
    protected static final int AGAINLOGIN = 3;// 重新登录
    protected static final int DELAGAINLOGIN = 4;// 关闭提示对话框
    protected static final int SUCCESS = 5;// 成功
    protected static final int DATAFAIL = 6;// 返回其他内容
    protected static final int DELSHOWPROGRESS = 7;// 关闭进度条对话框
    protected static final int NONET = 8;// 没有网络
    protected static final int VERSIONSUCCESS = 9;// 获取版本内容成功

    private View myContentView;//内容页面
    private LinearLayout mLogin, mNoLogin, mQuite, mPressWord, mUsMe, mStamp, mRecord, mCollection, mAddress, mOrderBack;//登录成功页面,没有登录页面,退出登录,密码管理,关于我们,我的邮集,竞拍记录,我的收藏.收获地址,回购订单
    private TextView mWithdraw, mPayment, mReceive, mComplete, mRefused, mPhone, mBalance, mNotBalance, Title;//提现
    private LinearLayout mOrderGoods;
    private String phone, mToken, mUser_id;// 手机号，标识，用户ID
    public SharedPreferences sp;
    private SendProgressDialog pd;
    private String result;
    private SussessDialog dialog;
    private PersonBean personBean;

//    private VersionBean vBean;

    private MyHandler handler = new MyHandler(getActivity()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELFAIL:// 关闭对话框
                    if (dialog != null)
                        dialog.dismiss();
                    break;
                case AGAINLOGIN:// 需要重新登录
                    dialog = new SussessDialog(getActivity());
                    dialog.setText("需要重新登录");
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELAGAINLOGIN, 2000);
                    break;
                case DELAGAINLOGIN:// 关闭提示对话框
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragment");
                    startActivity(intent);
                    break;
                case SUCCESS:// 返回成功
                    result = (String) msg.obj;
                    setData(result);
                    break;
                case DATAFAIL:// 返回其他内容
                    String Data = (String) msg.obj;
                    dialog = new SussessDialog(getActivity());
                    dialog.setText(Data);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELFAIL, 2000);
                    break;
                case NONET:// 没有网络
                    dialog = new SussessDialog(getActivity());
                    dialog.setText("请连接网络");
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELAGAINLOGIN, 2000);
                    break;
                case VERSIONSUCCESS:// 获取版本成功
//                    String result = (String) msg.obj;
//                    setVersion(result);

                    break;
                default:
                    break;
            }
        }

    };
    private LinearLayout mUpdate;
    private ProgressDialog prodialog;
    private Button dialog_button_cancel, dialog_button_ok;

    @Override
    public View CreateTitle() {
        return View.inflate(getActivity(), R.layout.fragment_my_title, null);
    }

    @Override
    public View CreateSuccess() {
        myContentView = View.inflate(getActivity(), R.layout.fragment_my_content, null);
        sp = getActivity().getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
       MyLog.LogShitou("CreateSuccess跳转后的", mToken + "-->" + mUser_id + "-->" + phone);
        initView();
        judgeView();// 判断view的方法(判断页面是否显示)
        initListener();
        return myContentView;
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        phone = sp.getString("phone", "");

        Log.e("=======initView跳转后的", mToken + "-->" + mUser_id + "-->" + phone);
        mLogin = (LinearLayout) myContentView.findViewById(R.id.my_login);
        mPhone = (TextView) myContentView.findViewById(R.id.my_phone);
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
        mPayment = (TextView) myContentView.findViewById(R.id.text_payment);
        mReceive = (TextView) myContentView.findViewById(R.id.receive);
        mComplete = (TextView) myContentView.findViewById(R.id.complete);
        mRefused = (TextView) myContentView.findViewById(R.id.refuse);
        mBalance = (TextView) myContentView.findViewById(R.id.my_balance);// 余额
        mNotBalance = (TextView) myContentView.findViewById(R.id.my_notbalance);// 不可用余额

        mUpdate = (LinearLayout) myContentView.findViewById(R.id.my_update);// 版本更新


    }

    /**
     * 判断view的方法(判断页面是否显示)
     */
    private void judgeView() {
        if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
            StringBuffer sb = new StringBuffer(phone);
            sb.replace(3, 7, "****");
            mPhone.setText(sb);
            mLogin.setVisibility(View.VISIBLE);
            mNoLogin.setVisibility(View.GONE);
            mQuite.setVisibility(View.VISIBLE);
//            pd = new SendProgressDialog(getActivity());
//            pd.show();
            getNetData();
        } else {
            mNoLogin.setVisibility(View.VISIBLE);
            mQuite.setVisibility(View.GONE);
            mLogin.setVisibility(View.GONE);
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
        mQuite.setOnClickListener(this);
        mUpdate.setOnClickListener(this);

    }

    @Override
    public void AgainRequest() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_noLogin://没有登录
                Intent intents = new Intent(getActivity(), LoginActivity.class);
                intents.putExtra("WithDraw", "myFragmentLogin");
                startActivity(intents);
                //跳转动画
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);

                break;
            case R.id.my_usMe://关于我们
                openActivityWitchAnimation(UsMeActivity.class);
                break;
            case R.id.my_update://版本更新
                DeleteDialog(); // 非强制更新弹出框
                break;
            case R.id.my_withdraw://提现
                if (personBean != null) {
                    Intent WithdrawIntent = new Intent(getActivity(), WithdrawActivity.class);
                    WithdrawIntent.putExtra("balance", personBean.getBalance());// 余额
                    WithdrawIntent.putExtra("result", result);// 传递整个数据
                    WithdrawIntent.putExtra("Unbalance", personBean.getUnusable_balance());// 不可用余额
                    startActivity(WithdrawIntent);
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//                    getActivity().finish();
                }
//                openActivityWitchAnimation(WithdrawActivity.class);
                break;
            case R.id.my_PressWordManager://密码管理
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    openActivityWitchAnimation(PressWordManagerActivity.class);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }

                break;
            case R.id.my_Stamp://我的邮集
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    openActivityWitchAnimation(MyStampActivity.class);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.my_auctionRecord://竞拍记录
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    openActivityWitchAnimation(AuctionRecordActivity.class);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.my_collection://收藏夹
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    openActivityWitchAnimation(MyCollectionActivity.class);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.my_Address://收获地址管理
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    openActivityWitchAnimation(ManagerAddressActivity.class);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.my_order_buy_back://回购订单
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    openActivityWitchAnimation(OrderBuyBackActivity.class);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.my_orders_goods:// 商品订单
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticField.ORDERS, "all");
                    openActivityWitchAnimation(OrdersGoodsActivity.class, bundle);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.text_payment: // 待付款
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(StaticField.ORDERS, "payment");
                    openActivityWitchAnimation(OrdersGoodsActivity.class, bundle1);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.receive:  // 待收货
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(StaticField.ORDERS, "receiving");
                    openActivityWitchAnimation(OrdersGoodsActivity.class, bundle2);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.complete: // 已完成
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString(StaticField.ORDERS, "completed");
                    openActivityWitchAnimation(OrdersGoodsActivity.class, bundle3);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.refuse: // 退款/退货
                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    Bundle bundle4 = new Bundle();
                    bundle4.putString(StaticField.ORDERS, "refused");
                    openActivityWitchAnimation(OrdersGoodsActivity.class, bundle4);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("WithDraw", "myFragmentLogin");
                    startActivity(intent);
                    //跳转动画
                    getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
                break;
            case R.id.my_quit_login: // 退出登录
                showDialog(getActivity());
                SPUtils.clear(getContext());
                break;
            default:
                break;
        }
    }

    /**
     * 展示退出对话框
     *
     * @param context
     */
    private void showDialog(Context context) {
        final QuiteDialog builder = new QuiteDialog(context, mQuite, mLogin, mNoLogin);
        builder.show();
    }

    /**
     * 个人信息
     * 访问网络获取数据
     */
    private void getNetData() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.PERSONQUERY);// 接口名称
                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUser_id);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Log.e("登陆后的测试-->", result);
//                handler.sendEmptyMessage(DELSHOWPROGRESS);
                Gson gson = new Gson();
                BaseBean json = gson.fromJson(result, BaseBean.class);
                String msg_Code= json.getRsp_code();// 返回的内容
                String msg_Data = json.getRsp_msg();// 返回的内容
                if (msg_Data.equals("用户需要重新登录")) {
                    handler.sendEmptyMessage(AGAINLOGIN);
                } else if (msg_Code.equals("0000")) {
                    Message msgSuccess = handler.obtainMessage();
                    msgSuccess.what = SUCCESS;
                    msgSuccess.obj = result;
                    handler.sendMessage(msgSuccess);
                } else {
                    Message msgFail = handler.obtainMessage();
                    msgFail.what = DATAFAIL;
                    msgFail.obj = msg_Data;
                    handler.sendMessage(msgFail);
                }

            }
        });

    }

    /**
     * 设置版本的方法
     *
     * @param result
     */
//    private void setVersion(String result) {
//        Gson gson = new Gson();
//        vBean = gson.fromJson(result, VersionBean.class);
//
//        String version = Version.getVersion();
//        // 当前版本
//        Double currentVersion = Double.valueOf(version);
//        String netVersion = vBean.getSys_version();
//        Double valueOf = Double.valueOf(netVersion);
//        if (valueOf > currentVersion) {
//            Log.e("版本是否更新", "该更新了");
//            VersionDialog dialog = new VersionDialog(getActivity(), vBean.getDownload_address(), vBean.getIs_force_update(), vBean.getSys_version(), false);
//            dialog.setVersion(this);
//            dialog.show();
//        } else {
//            ShowToast.showshortToast("已经是最新版本了");
//        }
//    }


    /**
     * 设置数据的方法
     *
     * @param result 返回成功的结果
     */
    private void setData(String result) {
        Gson gson = new Gson();
        personBean = gson.fromJson(result, PersonBean.class);
        sp.edit().putInt("isBeanCard", Integer.valueOf(personBean.getHas_bind_card())).commit();// 设置银行卡
        sp.edit().putInt("isRemitPwd", Integer.valueOf(personBean.getHas_remit_pwd())).commit();// 设置提现密码
        mBalance.setText("￥" + personBean.getBalance());
        mNotBalance.setText("￥" + personBean.getUnusable_balance());
    }

    /**
     * 非强制更新弹出框
     */
    private void DeleteDialog() {
        prodialog = new ProgressDialog(getActivity());
        prodialog.show();
        Title = (TextView) prodialog.findViewById(R.id.title_tv);
        Title.setText("发现新版本V1.0");
        // 下次再说
        dialog_button_cancel = (Button) prodialog
                .findViewById(R.id.dialog_button_cancel);
        dialog_button_cancel.setText("下次再说");
        // 确定
        dialog_button_ok = (Button) prodialog
                .findViewById(R.id.dialog_button_ok);// 取消
        dialog_button_ok.setText("立即更新");

        // 取消
        dialog_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodialog.dismiss();
            }
        });
        dialog_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<CollectionBean.Collection> deleteList = new ArrayList<CollectionBean.Collection>();
//                for (int i = 0; i < mList.size(); i++){
//                    AddressBean.Address group = mList.get(i);
//                    if (group.isChoosed()){
//                        deleteList.add(group);
//                    }
//                    mList.removeAll(deleteList);
//                    mListAdapter.notifyDataSetChanged();
//                }
//                MyToast.showShort(ManagerAddressActivity.this,"删除成功");
                prodialog.dismiss();// 关闭Dialog
                MyToast.showShort(getActivity(), "更新中...");
            }
        });
    }
}
