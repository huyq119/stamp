package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.ScanBean;
import cn.com.chinau.dialog.SussessDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.StringTimeUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 确认回购页面
 */
public class AffirmBuyBackActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    protected static final int SUCCESS = 0;// 成功
    protected static final int FAIL = 1;// 失败
    protected static final int DELPROGRESS = 2;// 关闭对话框
    protected static final int AGAINLOGIN = 3;// 再次登录
    protected static final int AGAINDIALOG = 4;// 弹出对话框
    protected static final int DELSURESS = 5;// 关闭对话框

    private View mAffirmBuyBackTitle;
    private View mAffirmBuyBackContent;
    private Button mBuyBack;//确认回购
    private ImageView mBack;
    private TextView mTitle, mGoodsName, mTimeBuy, mOrderName, mCurrentPrice, mPreEarn, mOrderServer,
            mBackPrice;
    private EditText mBuyPhone;
    private SharedPreferences sp;
    private String mGoodsSn,mDetailSn,mToken,mUserId,mPhone;
    private SussessDialog dialog;
    private String result;
    private MyHandler handler = new MyHandler(this) {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS :// 成功
                    String Result = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(Result);
                        String order_sn = json.getString("order_sn");
                        sp.edit().putString("order_sn",order_sn).commit();
                        openActivityWitchAnimation(FastMailInfoActivity.class);// 跳转快递信息页面
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAIL :// 失败
                    dialog = new SussessDialog(AffirmBuyBackActivity.this);
                    dialog.setText((String) msg.obj);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELSURESS, 1000);
                    break;
                case DELSURESS :
                    if (dialog != null)
                        dialog.dismiss();
                    break;
                case AGAINLOGIN :// 再次登录
                    if (dialog != null){
                        dialog.dismiss();
                    }
                    Intent intent = new Intent(AffirmBuyBackActivity.this, LoginActivity.class);
                    intent.putExtra("WithDraw", "buy_back");
                    startActivity(intent);
                    finish();
                    break;
                case AGAINDIALOG :
                    dialog = new SussessDialog(AffirmBuyBackActivity.this);
                    dialog.setText("用户需要重新登录");
                    dialog.show();
                    break;
                default :
                    break;
            }
        }

    };


    @Override
    public View CreateTitle() {
        mAffirmBuyBackTitle = View.inflate(this, R.layout.base_back_title, null);
        return mAffirmBuyBackTitle;
    }

    @Override
    public View CreateSuccess() {
        mAffirmBuyBackContent = View.inflate(this, R.layout.activity_affirmbuyback, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mAffirmBuyBackContent;
    }


    @Override
    public void AgainRequest() {

    }

    private void initView() {

        mBack = (ImageView) mAffirmBuyBackTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mAffirmBuyBackTitle.findViewById(R.id.base_title);
        mTitle.setText("确认回购");
        mGoodsName = (TextView) mAffirmBuyBackContent.findViewById(R.id.tv_goods_name);
        mTimeBuy = (TextView) mAffirmBuyBackContent.findViewById(R.id.tv_order_time_buy);
        mOrderName = (TextView) mAffirmBuyBackContent.findViewById(R.id.tv_order_name);
        mCurrentPrice = (TextView) mAffirmBuyBackContent.findViewById(R.id.tv_order_current_price);
        mPreEarn = (TextView) mAffirmBuyBackContent.findViewById(R.id.tv_order_pre_earn);
        mOrderServer = (TextView) mAffirmBuyBackContent.findViewById(R.id.tv_order_server);
        mBackPrice = (TextView) mAffirmBuyBackContent.findViewById(R.id.tv_order_back_price);
        mBuyPhone = (EditText) mAffirmBuyBackContent.findViewById(R.id.et_back_buy_phone);
        mBuyPhone.setText(sp.getString("phone", ""));

        mBuyBack = (Button) mAffirmBuyBackContent.findViewById(R.id.Affirm_buyBack);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mBuyBack.setOnClickListener(this);
        mBuyPhone.addTextChangedListener(this);
    }

    /**
     * 给组件赋值
     */
    private void initData() {
//        Bundle bundle = getIntent().getExtras();
//        result = bundle.getString("RESULT");

        result = sp.getString("ScanBack","");
        MyLog.LogShitou("mResult+获取本地商品详情", result);
        if (result != null) {
            Gson gson = new Gson();
            ScanBean scanBean = gson.fromJson(result, ScanBean.class);
            String mGoods_names = scanBean.getGoods_name();// 商品名称
            mGoodsName.setText(mGoods_names);
            String mBuy_time = scanBean.getBuy_time(); // 购买时间
            mTimeBuy.setText(StringTimeUtils.getShortTime(mBuy_time));
            String mBuyer = scanBean.getBuyer();// 用户
            mOrderName.setText(mBuyer);
            String mCurrent_price = scanBean.getCurrent_price();// 当前价格
            mCurrentPrice.setText(mCurrent_price);
            String mIncome = scanBean.getIncome();// 预计收益
            mPreEarn.setText(mIncome);
            String mFee_rate = scanBean.getService_fee_rate();// 服务费率
            mOrderServer.setText(mFee_rate);
            String mBuyback_price = scanBean.getBuyback_price();// 回购价格
            mBackPrice.setText(mBuyback_price);

            mGoodsSn = scanBean.getGoods_sn();// 商品编号
            mDetailSn= scanBean.getOrder_detail_sn(); // 订单明细编号(原订单) ，确认回购（HG）时传，物流（WL）时不传
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                openActivityWitchAnimation(ScanDetailsActivity.class);
                finish();
                break;
            case R.id.Affirm_buyBack://确认回购按钮
                mToken = sp.getString("token", "");
                mUserId = sp.getString("userId", "");
                mPhone = mBuyPhone.getText().toString();
                sp.edit().putString("Phone",mPhone).commit();
                GetNetBackBuy(mPhone);
                break;
            default:
                break;
        }
    }

    /**
     * 确认回购网络请求
     */
    private void GetNetBackBuy(final String phone){
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.CONFIRM);// 接口类型
                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUserId);
                params.put(StaticField.ORDER_DETAIL_SN, mDetailSn);// 订单明细编号(原订单) ，确认回购（HG）时传，物流（WL）时不传
                params.put(StaticField.GOODS_SN, mGoodsSn); // 商品编号，确认回购（HG）时传，物流（WL）时不传
                params.put(StaticField.MOBILE, phone);// 手机号
                params.put(StaticField.OP_TYPE, StaticField.HG);// 操作类型：HG确认回购；WL物流
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String mResult = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("确认回购页面--->", mResult);
//                handler.sendEmptyMessage(DELPROGRESS);
                if (mResult.equals("-1")) {
                    return;
                }
                Gson gson = new Gson();
                BaseBean codeBean = gson.fromJson(mResult, BaseBean.class);
                String rsp_coode = codeBean.getRsp_code();
                if (rsp_coode.equals("0000")) {
                    Message msg = handler.obtainMessage();
                    msg.what = SUCCESS;
                    msg.obj = mResult;
                    handler.sendMessage(msg);

                } else if (rsp_coode.equals("1002")) {
                    handler.sendEmptyMessage(AGAINDIALOG);
                    handler.sendEmptyMessageDelayed(AGAINLOGIN, 2000);
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = FAIL;
                    String rsp_msg = codeBean.getRsp_msg();
                    msg.obj = rsp_msg;
                    handler.sendMessage(msg);
                }
            }
        });



    }




    /**
     * 手机号填写的监听方法
     *
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    // 手机号发生变化的监听事件方法
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int phone = mBuyPhone.getText().length();
        MyLog.LogShitou("手机号有几位-->:" + phone);
        if (mBuyPhone.getText().length() == 11) {
            mBuyBack.setBackgroundResource(R.drawable.btn_backbuy_bg);
            mBuyBack.setEnabled(true);
        } else {
            mBuyBack.setEnabled(false);
            mBuyBack.setBackgroundResource(R.mipmap.btn_normal);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
