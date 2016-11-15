package cn.com.chinau.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.dialog.CountRegisterDialog;
import cn.com.chinau.dialog.CountSureDialog;
import cn.com.chinau.dialog.SendProgressDialog;
import cn.com.chinau.dialog.SussessDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Des3Encode;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MoneyTest;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.utils.ThreeCount;

/**
 * 申请提现页面
 */
public class ApplyWithdrawActivity extends BaseActivity implements View.OnClickListener, TextWatcher ,CountSureDialog.Count, CountRegisterDialog.Register {
    protected static final int SUCCESS = 0;// 获取验证码成功
    protected static final int CLOSEDIALOG = 1;// 关闭获取验证码对话框
    protected static final int COUNTDOWN = 2;// 倒计时功能
    protected static final int FAIL = 7;// 服务器解析失败
    protected static final int DElPRESSAGE = 3;// 关闭进度条
    protected static final int NOSUCCESS = 4;// 没有成功


    private View mApplyWithdrawContent;
    private View mApplyWithdrawTitle;
    private ImageView mBack;
    private SharedPreferences sp;
    private String mToken;
    private String mUser_id;
    private String phone;
    private Button btn_get_code, btn_cimmit;// 获取验证码，确定
    private EditText et_cash_num, et_cash_pw, et_code;// 提现金额，提现密码,手机验证码
    private TextView tv_aplay_able, mPhone;// 可提现现金,电话号码
    private int Time = 60;// 倒计时的总时间
    private static String APLAY = "aplay";
    private static String APLAYTIME = "aplaytime";
    private double balance;// 可体现余额
    private SendProgressDialog pd;
    private static final String APLAYCIMMIT = "aplaycimmit";
    private static final String APLAYCIMMITTIME = "aplaycimmittime";

    @Override
    public View CreateTitle() {
        mApplyWithdrawTitle = View.inflate(this, R.layout.base_back_title, null);
        return mApplyWithdrawTitle;
    }

    @Override
    public View CreateSuccess() {
        mApplyWithdrawContent = View.inflate(this, R.layout.activity_applywithdraw, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_APPEND);
        initView();
        return mApplyWithdrawContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        phone = sp.getString("phone", "");

        TextView mTitle = (TextView) mApplyWithdrawTitle.findViewById(R.id.base_title);
        mTitle.setText("可提现余额");
        mBack = (ImageView) mApplyWithdrawTitle.findViewById(R.id.base_title_back);

        btn_cimmit = (Button) mApplyWithdrawContent.findViewById(R.id.btn_cimmit);// 提交
        btn_get_code = (Button) mApplyWithdrawContent.findViewById(R.id.btn_get_code);
        et_cash_num = (EditText) mApplyWithdrawContent.findViewById(R.id.et_cash_num);
        et_cash_pw = (EditText) mApplyWithdrawContent.findViewById(R.id.et_cash_pw);
        et_code = (EditText) mApplyWithdrawContent.findViewById(R.id.et_code);
        tv_aplay_able = (TextView) mApplyWithdrawContent.findViewById(R.id.tv_aplay_able);
        mPhone = (TextView) mApplyWithdrawContent.findViewById(R.id.tv_info_back);
        mPhone.setText("接收验证码手机    " + new StringBuffer(phone).replace(3, 7, "****").toString());
        getIntentData(); // 获取串过来的数据
        initListener();
    }

    /**
     * 获取串过来的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            balance = Double.valueOf(intent.getStringExtra("balance"));
            DecimalFormat df = new DecimalFormat("#0.00");
            tv_aplay_able.setText("￥" + df.format(balance));
            if (TextUtils.isEmpty(phone)) {
                StringBuffer sb = new StringBuffer(phone);
                phone = sb.replace(3, 7, "****").toString();
                mPhone.setText("接收验证码手机    " + phone);
            }
        }
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
        btn_cimmit.setOnClickListener(this);
        et_cash_num.addTextChangedListener(this);
        et_cash_pw.addTextChangedListener(this);
        et_code.addTextChangedListener(this);
        MoneyTest.setPricePoint(et_cash_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_back:// 返回
              finishWitchAnimation();
                break;
            case R.id.btn_get_code:// 获取验证码
                phone = sp.getString("phone", "");
                getNetData(phone);
                break;
            case R.id.btn_cimmit: // 提交,这里加了三次判断
                pd = new SendProgressDialog(ApplyWithdrawActivity.this);
                pd.show();
                String pw = et_cash_pw.getText().toString();
                String money = et_cash_num.getText().toString();
                String code = et_code.getText().toString();
                getRegisterCurrent(pw, money, code);
                break;

            default:
                break;
        }
    }

    /**
     * 提交
     *
     * @param code
     * @param money
     * @param pw
     */
    private void getCimmitData(final String pw, final String money, final String code) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.REMIT);
                params.put(StaticField.REMIT_AMOUNT, money);
                params.put(StaticField.SMS_CODE, code);
                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUser_id);
                // 加密后的字节流
                byte[] PWByte = Des3Encode.encryptMode(pw.getBytes());
                String PWBase64 = new String(Base64.encode(PWByte, Base64.DEFAULT));
                params.put(StaticField.REMIT_PWD, PWBase64);
                params.put(StaticField.OP_TYPE, StaticField.DL);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                Log.e("测试提现", result);
                Gson gson = new Gson();
                BaseBean codeBean = gson.fromJson(result, BaseBean.class);
                String rsp_msg = codeBean.getRsp_msg();
                handler.sendEmptyMessage(DElPRESSAGE);
                if (rsp_msg.equals("成功")) {
                    openActivityWitchAnimation(ActivityAplayCashSuccessActivity.class);
                    finish();
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = NOSUCCESS;
                    msg.obj = rsp_msg;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private MyHandler handler = new MyHandler(this) {
        private SussessDialog dialog;// 获取验证码成功,展示的对话框

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNTDOWN:// 倒计时功能
                    btn_get_code.setEnabled(false);// 让验证码按钮不能点击
                    btn_get_code.setBackgroundResource(R.mipmap.code_normal);// 设置背景颜色
                    Time--;
                    if (Time > 0) {
                        handler.sendEmptyMessageDelayed(COUNTDOWN, 1000);
                        btn_get_code.setText("(已发送" + Time + "s)");
                    } else if (Time == 0) {
                        btn_get_code.setEnabled(true);
                        btn_get_code.setText("获取验证码");
                        btn_get_code.setBackgroundResource(R.mipmap.code);// 设置背景颜色
                        handler.removeMessages(COUNTDOWN);
                        Time = 60;
                    }
                    break;
                case FAIL:// 失败
                    break;
                case SUCCESS:// 获取验证码成功,展示对话框
                    dialog = new SussessDialog(ApplyWithdrawActivity.this);
                    dialog.show();
                    break;
                case CLOSEDIALOG:// 2秒钟之后关闭对话框
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
                case DElPRESSAGE:// 关闭进度条
                    if (pd != null) {
                        pd.dismiss();
                    }
                    break;
                case NOSUCCESS:// 没有成功
                    dialog = new SussessDialog(ApplyWithdrawActivity.this);
                    dialog.setText((String) msg.obj);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(CLOSEDIALOG, 2000);
                    break;
                default:
                    break;
            }

        }
    };


    /**
     * 输入金额变化
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        DecimalFormat df = new DecimalFormat("#0.00");
        if (!et_cash_num.getText().toString().trim().equals("")) {
            if (et_cash_pw.getText().length() >= 6 && et_code.getText().length() == 6 && Double.valueOf(et_cash_num.getText().toString().trim()) > 0 && balance * 100 >= Double.valueOf(et_cash_num.getText().toString().trim()) * 100) {
                btn_cimmit.setBackgroundResource(R.mipmap.login_press);
                btn_cimmit.setEnabled(true);
            } else {
                btn_cimmit.setEnabled(false);
                btn_cimmit.setBackgroundResource(R.mipmap.login_normal);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 判断次数访问网络
     *
     * @param phone
     */
    private void getNetData(final String phone) {
        // 判断
        ThreeCount.setThreeCount(this, APLAY, APLAYTIME);
        int current = sp.getInt(APLAY, 0);
        Log.e("counts", "&" + current);
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 获取保存的时间
        long holdTime = sp.getLong(APLAYTIME, 0);
        Log.e("测试", currentTimeMillis + "-" + holdTime);
        // 如果时间大于10分钟重新把次数归零
        if (currentTimeMillis - holdTime > StaticField.GIVENTIME) {
            sp.edit().putInt(APLAY, 0).commit();
        }
        if (current >= 4 && currentTimeMillis - holdTime < StaticField.GIVENTIME) {
            // 弹出对话框,并且跳出循环,重置次数
            showSureDialog(ApplyWithdrawActivity.this);// 展示对话框
            return;
        }
        sendMessage(phone);
    }
    /**
     * 创建对话框
     *
     */
    private void showRegisterDialog(Context context) {
        CountRegisterDialog countSure = new CountRegisterDialog(context, APLAYCIMMIT, pd);
        countSure.setmRegister(this);
        countSure.show();
    }

    /**
     * 展示对话框
     */
    private void showSureDialog(Context context) {
        CountSureDialog countSure = new CountSureDialog(context, APLAY, pd);
        countSure.setmCount(this);
        countSure.show();
    }

    /**
     * 判断注册点击次数
     */
    private void getRegisterCurrent(String pw, String money, String code) {
        // 判断
        ThreeCount.setThreeCount(this, APLAYCIMMIT, APLAYCIMMITTIME);
        int current = sp.getInt(APLAYCIMMIT, 0);
        Log.e("counts", "&" + current);
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 获取保存的时间
        long holdTime = sp.getLong(APLAYCIMMITTIME, 0);
        Log.e("测试", currentTimeMillis + "-" + holdTime);
        // 如果时间大于10分钟重新把次数归零
        if (currentTimeMillis - holdTime > StaticField.GIVENTIME) {
            sp.edit().putInt(APLAYCIMMIT, 0).commit();
        }
        if (current >= 4 && currentTimeMillis - holdTime <StaticField.GIVENTIME) {
            // 弹出对话框,并且跳出循环,重置次数
            showRegisterDialog(ApplyWithdrawActivity.this);// 展示对话框
            return;
        }
        getCimmitData(pw, money, code);
    }

    private void sendMessage(final String phone) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                // 发送倒计时
                handler.sendEmptyMessage(COUNTDOWN);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SMS_SEND);
                params.put(StaticField.MOBILE, phone);
                params.put(StaticField.SMS_TYPE, StaticField.TX);
                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUser_id);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                Log.e("提现短信", result);
                if (result.equals("-1")) {// 服务器解析异常
                    handler.sendEmptyMessage(FAIL);
                    return;
                }
                // 解析返回的结果
                Gson gson = new Gson();
                BaseBean codeBean = gson.fromJson(result, BaseBean.class);
                if (codeBean.getRsp_msg().equals("成功")) {// 获取验证码成功
                    handler.sendEmptyMessage(SUCCESS);
                    handler.sendEmptyMessageDelayed(CLOSEDIALOG, 2000);
                }
            }

        });
    }


    /**
     * 获取注册回调回来的值
     */
    @Override
    public void RegisterCode() {
        String pw = et_cash_pw.getText().toString();
        String money = et_cash_num.getText().toString();
        String code = et_code.getText().toString();
        getRegisterCurrent(pw, money, code);
    }


    @Override
    public void SendCode() {
        String phone = mPhone.getText().toString();
        getNetData(phone);
    }
}