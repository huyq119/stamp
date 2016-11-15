package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.WithdrawAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.PersonBean;
import cn.com.chinau.dialog.SussessDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 提现页面
 */
public class WithdrawActivity extends BaseActivity implements View.OnClickListener {
    protected static final int DELFAIL = 2;// 关闭对话框
    protected static final int SUCCESS = 5;// 成功
    protected static final int DATAFAIL = 6;// 返回其他内容

    private View mWithdrawTitle;
    private View mWithdrawContent;
    private ImageView mBack;
    private TextView mCard;//银行卡按钮
    private TextView mCardCash;//申请提现
    private TextView mNoOrder;// 暂无明细
    private TextView mBalance;//可提现余额
    private double balance;// 可提现金额
    private boolean flag = false;//只作为一个标记,之后要删除的
    private ListView mHistoryLV;
    private ArrayList<PersonBean.History> history_list = new ArrayList<PersonBean.History>();
    private String mToken;
    private String mUser_id;
    private SharedPreferences sp;

    @Override
    public View CreateTitle() {
        mWithdrawTitle = View.inflate(this, R.layout.base_back_title, null);
        return mWithdrawTitle;
    }

    @Override
    public View CreateSuccess() {
        mWithdrawContent = View.inflate(this, R.layout.activity_withdraw, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_APPEND);
        initView();
        getIntentData();
        initListener();
        return mWithdrawContent;
    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        TextView mTitle = (TextView) mWithdrawTitle.findViewById(R.id.base_title);
        mTitle.setText("可提现余额");
        mBack = (ImageView) mWithdrawTitle.findViewById(R.id.base_title_back);

        mCard = (TextView) mWithdrawContent.findViewById(R.id.withdraw_card);
        mBalance = (TextView) mWithdrawContent.findViewById(R.id.withdraw_balance);
        mCardCash = (TextView) mWithdrawContent.findViewById(R.id.withdraw_card_cash);
        mNoOrder = (TextView) mWithdrawContent.findViewById(R.id.withdraw_noData);
        mHistoryLV = (ListView) mWithdrawContent.findViewById(R.id.withdraw_lv);
    }

    /**
     * 获取Intent内容
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("balance") != null) {
                balance = Double.valueOf(intent.getStringExtra("balance")) - Double.valueOf(intent.getStringExtra("Unbalance"));
                DecimalFormat df = new DecimalFormat("#0.00");
                if (balance > 0.00) {
                    // 这里是判断申请提现是否可以点击,并且可以置灰
                    mCardCash.setEnabled(true);
                    mCardCash.setBackgroundResource(R.mipmap.green_bg);
                } else {
                    balance = 0.00;
                    mCardCash.setEnabled(false);
                    mCardCash.setBackgroundResource(R.mipmap.code_normal);
                }
                mBalance.setText("￥" + df.format(balance));
                // 返回的历史记录
                String result = intent.getStringExtra("result");// 传递过来的数据
                setData(result); //解析传过来的数据
            } else {
                getNetData(); // 获取个人信息网络请求
            }
        }
    }

    /**
     * 解析传过来的数据
     * @param result
     */
    private void setData(String result) {
        Gson gson = new Gson();
        PersonBean personBean = gson.fromJson(result, PersonBean.class);
        history_list = personBean.getAct_history_list();
        if (history_list.size() == 0) {
            mNoOrder.setVisibility(View.VISIBLE);
            mHistoryLV.setVisibility(View.GONE);
        } else {
            mNoOrder.setVisibility(View.GONE);
            mHistoryLV.setVisibility(View.VISIBLE);
            // 设置适配器
            setmHistoryLV(history_list);
        }

    }

    /**
     * 设置历史数据
     *
     * @param history_list
     *            获取的数据
     */
    private void setmHistoryLV(ArrayList<PersonBean.History> history_list) {
        WithdrawAdapter adapter = new WithdrawAdapter(history_list, this);
        mHistoryLV.setAdapter(adapter);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mCard.setOnClickListener(this);
        mCardCash.setOnClickListener(this);
    }
    @Override
    public void AgainRequest() {
        getNetData(); // 获取个人信息网络请求
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.withdraw_card://银行卡页面
                //这里要判断一下是否绑定银行卡
                int isBeanCard = sp.getInt("isBeanCard", 0);
               MyLog.LogShitou("isBeanCard是否添加银行卡的值", isBeanCard + "");
                if (isBeanCard == 1) {// 绑定银行卡
                    // 银行卡为空跳转添加银行卡
                    startActivity(new Intent(WithdrawActivity.this, CashNotemptyActivity.class));
                } else {
                    startActivity(new Intent(WithdrawActivity.this, CashEmptyActivity.class));
                }

                break;
            case R.id.withdraw_card_cash://申请提现
                Intent intent = new Intent(WithdrawActivity.this, ApplyWithdrawActivity.class);
                intent.putExtra("balance", String.valueOf(balance));// 存入余额
                startActivity(intent);
//                openActivityWitchAnimation(ApplyWithdrawActivity.class);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//                finish();
                break;
        }
    }

    /**
     * 获取个人信息网络请求
     */
    private void getNetData() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.PERSONQUERY);
                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUser_id);

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                if (result.equals("-1")) {
                    return;
                }
                Log.e("提现列表的测试", result);
//                handler.sendEmptyMessage(DELSHOWPROGRESS);
                Gson gson = new Gson();
                BaseBean json = gson.fromJson(result, BaseBean.class);
                String mCode = json.getRsp_code();// 返回的内容
                String mMsg = json.getRsp_msg();// 返回的内容
                if (mCode.equals("0000")) {
                    Message msgSuccess = handler.obtainMessage();
                    msgSuccess.what = SUCCESS;
                    msgSuccess.obj = result;
                    handler.sendMessage(msgSuccess);
                } else {
                    Message msgFail = handler.obtainMessage();
                    msgFail.what = DATAFAIL;
                    msgFail.obj = mMsg;
                    handler.sendMessage(msgFail);
                }

            }
        });

    }

    private MyHandler handler = new MyHandler(WithdrawActivity.this) {
        private String result;
        private SussessDialog dialog;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELFAIL :// 关闭对话框
                    if (dialog != null)
                        dialog.dismiss();
                    break;
                case SUCCESS :// 返回成功
                    result = (String) msg.obj;
                    Gson gson = new Gson();
                    PersonBean personBean = gson.fromJson(result, PersonBean.class);
                    String Balance = personBean.getBalance();// 余额
                    String unBalance = personBean.getUnusable_balance();
                    balance = Double.valueOf(Balance) - Double.valueOf(unBalance);
                    DecimalFormat df = new DecimalFormat("#0.00");
                    if (balance > 0.00) {
                        // 这里是判断申请提现是否可以点击,并且可以置灰
                        mCardCash.setEnabled(true);
                        mCardCash.setBackgroundResource(R.mipmap.green_bg);
                    } else {
                        balance = 0.00;
                        mCardCash.setEnabled(false);
                        mCardCash.setBackgroundResource(R.mipmap.code_normal);
                    }
                    mBalance.setText("￥" + df.format(balance));
                    setData(result); // 解析传过来的数据
                    break;
                case DATAFAIL :// 返回其他内容
                    String Data = (String) msg.obj;
                    dialog = new SussessDialog(WithdrawActivity.this);
                    dialog.setText(Data);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELFAIL, 2000);
                    break;
                default :
                    break;
            }
        }
    };
}
