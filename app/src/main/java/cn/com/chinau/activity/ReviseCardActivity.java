package cn.com.chinau.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bitmap.BitmapHelper;
import cn.com.chinau.dialog.CountSureDialog;
import cn.com.chinau.dialog.SendProgressDialog;
import cn.com.chinau.dialog.SussessDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Des3Encode;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyNumberKeyListener;
import cn.com.chinau.utils.PhoneUtile;
import cn.com.chinau.utils.ShowToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.utils.ThreeCount;

/**
 * 修改银行卡页面
 */
public class ReviseCardActivity extends BaseActivity implements View.OnClickListener,TextWatcher,CountSureDialog.Count {

    private View mReviseCardTitle;
    private View mReviseCardContent;
    private String mCashEmpty;

    protected static final int SUCCESS = 0;// 获取验证码成功
    protected static final int CLOSEDIALOG = 1;// 关闭获取验证码对话框
    protected static final int COUNTDOWN = 2;// 倒计时功能
    protected static final int SHOWTEXTDIALOG = 3;// 展示内容对话框
    protected static final int LOGINSUCCESS = 4;// 登陆成功
    protected static final int FAILTEXTDIALOG = 5;// 登陆失败
    protected static final int REGISTERFAIL = 6;// 获取失败
    protected static final int FAIL = 7;// 服务器解析失败
    protected static final int SHOWAGAINDIALOG = 8;// 展示重新登录对话框
    protected static final int SUCCESSAGAIN = 9;// 需要再次登录
    private static final int CARSUCCESS = 10;// 获取银行卡信息成功
    protected static final int REPLY = 11;
    private ImageButton ib_cash, ib_name;// 清除卡号,清除持卡人
    private ImageView mIcon,mBack;// 银行图标
    private TextView mCarName, et_phone,mTitle;// 银行名称
    private EditText et_cash_pw, et_code, et_name, et_cash_num;// 提现密码，验证码,持卡人,卡号,输入电话号码
    private Button btn_forget_pw, btn_get_code, btn_cimmit;// 忘记密码按钮,获取验证码按钮,提交按钮
    private int Time = 60;// 倒计时的总时间
    private static String BANKCODE = "bankcode";
    private static String BANKCODETIME = "bankcodetime";
    private String mToken;
    private int isRemitPwd;// 是否设置银行卡标识
    private String mUser_id;

    private MyHandler handler = new MyHandler(this) {
        private SussessDialog dialog;// 获取验证码成功,展示的对话框

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS :// 获取验证码成功,展示对话框
                    dialog = new SussessDialog(ReviseCardActivity.this);
                    dialog.show();
                    break;
                case CLOSEDIALOG :// 2秒钟之后关闭对话框
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
                case COUNTDOWN :// 倒计时功能
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
                case FAIL :// 服务器解析异常
                    dialog = new SussessDialog(ReviseCardActivity.this);
                    dialog.setText("请检查你的网络");
                    dialog.show();
                    handler.sendEmptyMessageDelayed(REGISTERFAIL, 2000);
                    break;
                case REGISTERFAIL :
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
                case SHOWTEXTDIALOG :// 展示内容对话框
                    dialog = new SussessDialog(ReviseCardActivity.this);
                    if (isRemitPwd == 1) {
                        dialog.setText("修改成功");
                    } else {
                        dialog.setText("添加成功");
                    }
                    dialog.show();
                    break;

                case LOGINSUCCESS :// 修改成功
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    sp.edit().putInt("isBeanCard", 1).commit();
                    // 保存银行卡卡号
                    sp.edit().putString("bankNum", et_cash_num.getText().toString()).commit();
                    Intent intentUp = new Intent(ReviseCardActivity.this, CashNotemptyActivity.class);
                    intentUp.putExtra("upDate", "updata");
                    startActivity(intentUp);
                    finish();
                    break;
                case SHOWAGAINDIALOG :// 展示重新登录对话框
                    dialog = new SussessDialog(ReviseCardActivity.this);
                    dialog.setText("需要重新登录");
                    dialog.show();
                    break;
                case SUCCESSAGAIN :// 需要再次登录
                    Intent intent = new Intent(ReviseCardActivity.this, LoginActivity.class);
                    intent.putExtra("WithDraw", "upDate");
                    startActivity(intent);
                    finish();
                    break;
                case FAILTEXTDIALOG :// 登陆失败
                    String rspMsg = (String) msg.obj;
                    dialog = new SussessDialog(ReviseCardActivity.this);
                    dialog.setText(rspMsg);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(REGISTERFAIL, 2000);
                    break;
                case CARSUCCESS :// 获取银行卡信息成功
                    String result = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(result);
                        String bank_abbr = json.getString("bank_abbr");
                        String bank_name = json.getString("bank_name");
                        // sp.edit().putString("bankName", bank_name).commit();
                        sp.edit().putString("bankADD", bank_abbr).commit();
                        setCarIcon(bank_abbr, bank_name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case REPLY :// 恢复
                    btn_cimmit.setEnabled(true);
                    break;
                default :
                    break;
            }
        }
    };
    private SharedPreferences sp;
    private BitmapUtils bitmapUtils;

    @Override
    public View CreateTitle() {
        mReviseCardTitle = View.inflate(this, R.layout.base_back_title, null);
        return mReviseCardTitle;
    }

    @Override
    public View CreateSuccess() {
        mReviseCardContent = View.inflate(this, R.layout.activity_revisecard, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_APPEND);
        bitmapUtils = BitmapHelper.getBitmapUtils();
        initView();
        GetInittentString();
        return mReviseCardContent;
    }

    private void initView() {
        mToken = sp.getString("token", null);
        mUser_id = sp.getString("userId", null);
        ib_name = (ImageButton) mReviseCardContent.findViewById(R.id.ib_name);
        ib_cash = (ImageButton) mReviseCardContent.findViewById(R.id.ib_cash);
        et_code = (EditText) mReviseCardContent.findViewById(R.id.et_code);
        btn_forget_pw = (Button) mReviseCardContent.findViewById(R.id.btn_forget_pw);
        et_cash_pw = (EditText) mReviseCardContent.findViewById(R.id.et_cash_pw);
        et_cash_num = (EditText) mReviseCardContent.findViewById(R.id.et_cash_num);

        et_phone = (TextView) mReviseCardContent.findViewById(R.id.et_phone);
        btn_get_code = (Button) mReviseCardContent.findViewById(R.id.btn_get_code);
        btn_cimmit = (Button) mReviseCardContent.findViewById(R.id.btn_cimmit);
        et_name = (EditText) mReviseCardContent.findViewById(R.id.et_name);

        mIcon = (ImageView) mReviseCardContent.findViewById(R.id.cash_caricon);
        mCarName = (TextView) mReviseCardContent.findViewById(R.id.cash_carName);
        isRemitPwd = sp.getInt("isBeanCard", 0);

        mBack = (ImageView) mReviseCardTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mReviseCardTitle.findViewById(R.id.base_title);
        if (isRemitPwd == 1) {
            mTitle.setText("修改银行卡");
            et_cash_num.setText(sp.getString("bankNum", ""));// 卡号
            getCarBinData(sp.getString("bankNum", ""));
            et_name.setText(sp.getString("bankPerson", ""));// 持卡人姓名
        } else {
            mTitle.setText("添加银行卡");
        }

        StringBuffer sb = new StringBuffer(sp.getString("phone", ""));
        et_phone.setText(sb.replace(3, 7, "****"));

        initListener();
    }

    private void initListener() {
        ib_cash.setOnClickListener(this);
        ib_name.setOnClickListener(this);
        btn_cimmit.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
        btn_forget_pw.setOnClickListener(this);
        et_cash_pw.addTextChangedListener(this);

        // 为输入密码框添加限制
        MyNumberKeyListener listener = new MyNumberKeyListener();
        et_cash_pw.setKeyListener(listener);

        et_code.addTextChangedListener(this);
        et_name.addTextChangedListener(this);
        et_cash_num.addTextChangedListener(this);
        // edittext焦点改变的监听
        et_cash_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && et_cash_num.getText().length() >= 16) {
                    String carNum = et_cash_num.getText().toString();
                    getCarBinData(carNum);
                }
            }

        });
    }

    @Override
    public void AgainRequest() {

    }


    // 获取传过来的值
    private void GetInittentString() {
        Bundle bundle = getIntent().getExtras();
        mCashEmpty = bundle.getString("CashEmpty","");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_back :// 返回
                finishWitchAnimation();
                break;
            case R.id.btn_forget_pw :// 忘记密码
                Intent intent = new Intent(ReviseCardActivity.this, WithdrawPressWordActivity.class);
                intent.putExtra("Unbundling", "Unbundling");
                startActivity(intent);
                break;
            case R.id.ib_cash :// 清除卡号
                et_cash_num.setText("");// 重新设置卡号
                break;
            case R.id.ib_name :// 重新设置姓名
                et_name.setText("");
                break;
            case R.id.btn_get_code :// 获取验证码
                getNetData();
                break;
            case R.id.btn_cimmit :// 提交按钮
                btn_cimmit.setEnabled(false);
                JudgeCimmit();// 判断提交的方法
                break;
            default :
                break;
        }
    }

    /**
     * 获取卡bin接口的信息
     *
     * @param carNum
     *            银行卡号码
     */
    private void getCarBinData(final String carNum) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.CARDBIN);
                params.put(StaticField.CARD_NO, carNum);
                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUser_id);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                Log.e("银行卡bin接口", result);

                try {
                    JSONObject json = new JSONObject(result);
                    String rsp_msg = json.getString("rsp_msg");

                    if (rsp_msg.equals("成功")) {
                        Message msg = handler.obtainMessage();
                        msg.what = CARSUCCESS;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    /**
     * 设置银行图标
     *
     * @param bank_abbr
     *            银行卡简称
     * @param bank_name
     *            银行卡名称
     */
    private void setCarIcon(String bank_abbr, String bank_name) {
        String SystemData = sp.getString("System", null);// 获取系统信息
        try {
            JSONObject json = new JSONObject(SystemData);
            String ParamValue = json.getString("sys_param_value");
            JSONObject jsonParam = new JSONObject(ParamValue);
            String Bank = jsonParam.getString("bank_icon");
            JSONObject jsonBank_icon = new JSONObject(Bank);
            String cadIcon = jsonBank_icon.getString(bank_abbr);
            Log.e("银行卡的Url", cadIcon);
            // 获取银行卡图片的数组
            String[] Icon = cadIcon.split(",");
            // 获取屏幕的分辨率
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight = display.getHeight();

            // 判断屏幕尺寸选择图片
            int num = screenWidth * screenHeight;
            if (num <= 480 * 800) {// 用1倍图
                mIcon.setVisibility(View.VISIBLE);
                bitmapUtils.display(mIcon, Icon[0]);
            } else if (num > 480 * 800 && num < 960 * 720) {// 用2倍图
                mIcon.setVisibility(View.VISIBLE);
                bitmapUtils.display(mIcon, Icon[1]);
            } else {// 用3倍图
                mIcon.setVisibility(View.VISIBLE);
                bitmapUtils.display(mIcon, Icon[2]);
            }
            mCarName.setText(bank_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 提交按钮的判断与提交
     */
    private void JudgeCimmit() {
        String mWithdraw = et_cash_pw.getText().toString();
        String mCode = et_code.getText().toString();
        String mName = et_name.getText().toString();
        String mBankCode = et_cash_num.getText().toString();
        if (PhoneUtile.verifyNickname(mName)) {// 判断姓名
            if (mBankCode.length() >= 16) {// 判断卡号
                if (mWithdraw.length() >= 6) {// 判断密码长短
                    getCimmit(mWithdraw, mCode, mName, mBankCode);// 提交的方法
                } else {
                    ShowToast.showshortToast("提现密码不能少于6位");
                }
            } else {
                ShowToast.showshortToast("请正确填写您的卡号");
            }
        } else {
            ShowToast.showshortToast("请正确填写您的行密码");
        }
    }
    /**
     * 提交的方法
     *
     * @param mBankCode
     *            银行卡号
     * @param mName
     *            用户名
     * @param mCode
     *            验证码
     * @param mWithdraw
     *            提现密码
     */
    private void getCimmit(final String mWithdraw, final String mCode, final String mName, final String mBankCode) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.BIND);
                params.put(StaticField.MOBILE, sp.getString("phone", ""));
                params.put(StaticField.CARD_NO, mBankCode);// 卡号
                // 加密后的字节流
                byte[] PWByte = Des3Encode.encryptMode(mWithdraw.getBytes());
                String PWBase64 = new String(Base64.encode(PWByte, Base64.DEFAULT));
                params.put(StaticField.REMIT_PWD, PWBase64);
                if (isRemitPwd == 1) {
                    params.put(StaticField.OP_TYPE, StaticField.XG);
                } else {
                    params.put(StaticField.OP_TYPE, StaticField.BD);
                }
                params.put(StaticField.SMS_CODE, mCode);
                params.put(StaticField.USER_ID, mUser_id);
                params.put(StaticField.TOKEN, mToken);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.NAME, mName);// 姓名
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.NAME, params);
                handler.sendEmptyMessage(REPLY);
                if (result.equals("-1")) {
                    Log.e("弹出对话框", "是否执行");
                    handler.sendEmptyMessage(FAIL);
                    return;
                }
                Gson gson = new Gson();
                BaseBean codeBean = gson.fromJson(result, BaseBean.class);
                String rsp_code = codeBean.getRsp_code();// 服务器返回的结果
                String rsp_msg = codeBean.getRsp_msg();// 服务器返回的结果
                if(rsp_code.equals("0000")){
                    if (rsp_msg.equals("成功")) {
                        handler.sendEmptyMessage(SHOWTEXTDIALOG);
                        handler.sendEmptyMessageDelayed(LOGINSUCCESS, 2000);
                    } else if (rsp_msg.equals("用户需要重新登录")) {
                        handler.sendEmptyMessage(SHOWAGAINDIALOG);
                        handler.sendEmptyMessageDelayed(SUCCESSAGAIN, 2000);
                    } else {
                        Log.e("ceshi", "是否执行");
                        String rspMsg = codeBean.getRsp_msg();
                        Message msg = handler.obtainMessage();
                        msg.what = FAILTEXTDIALOG;
                        msg.obj = rspMsg;
                        handler.sendMessage(msg);
                    }
                }

            }
        });

    }

    private void getNetData() {
        // 判断
        ThreeCount.setThreeCount(this, BANKCODE, BANKCODETIME);
        int current = sp.getInt(BANKCODE, 0);
        Log.e("counts", "&" + current);
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 获取保存的时间
        long holdTime = sp.getLong(BANKCODETIME, 0);
        Log.e("测试", currentTimeMillis + "-" + holdTime);
        // 如果时间大于10分钟重新把次数归零
        if (currentTimeMillis - holdTime > StaticField.GIVENTIME) {
            sp.edit().putInt(BANKCODE, 0).commit();
        }
        if (current >= 4 && currentTimeMillis - holdTime < StaticField.GIVENTIME) {
            // 弹出对话框,并且跳出循环,重置次数
            showSureDialog(ReviseCardActivity.this);// 展示对话框
            return;
        }
        sendMessage();

    }
    /**
     * 展示对话框
     */
    private void showSureDialog(Context context) {
        SendProgressDialog pd = null;
        CountSureDialog countSure = new CountSureDialog(context, BANKCODE, pd);
        countSure.setmCount(this);
        countSure.show();
    }

    @Override
    public void SendCode() {
        getNetData();
    }
    private void sendMessage() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                // 发送倒计时
                handler.sendEmptyMessage(COUNTDOWN);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SMS_SEND);
                params.put(StaticField.MOBILE, sp.getString("phone", ""));

                if (isRemitPwd == 1) {
                    params.put(StaticField.SMS_TYPE, StaticField.XG);
                } else {
                    params.put(StaticField.SMS_TYPE, StaticField.BK);
                }

                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUser_id);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.NAME, params);
                Log.e("测试短信", result);
                if (result.equals("-1")) {// 服务器解析异常
                    handler.sendEmptyMessage(FAIL);
                    return;
                }
                // 解析返回的结果
                Gson gson = new Gson();
                BaseBean codeBean = gson.fromJson(result, BaseBean.class);
                if (codeBean.getRsp_code().equals("0000")) {// 获取验证码成功
                    handler.sendEmptyMessage(SUCCESS);
                    handler.sendEmptyMessageDelayed(CLOSEDIALOG, 2000);
                }
            }

        });
    }

    /**
     *   控件输入的变化
     * @param s
     * @param start
     * @param count
     * @param after
     */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 银行卡最少16位到19位
        if (et_cash_pw.getText().length() >= 6 && et_code.getText().length() == 6 && PhoneUtile.verifyNickname(et_name.getText().toString()) && et_cash_num.getText().length() >= 16) {
            btn_cimmit.setEnabled(true);
            btn_cimmit.setBackgroundResource(R.mipmap.login_press);
        } else {
            btn_cimmit.setEnabled(false);
            btn_cimmit.setBackgroundResource(R.mipmap.login_normal);
        }

        if (!TextUtils.isEmpty(et_name.getText().toString())) {
            ib_name.setVisibility(View.VISIBLE);
        } else {
            ib_name.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(et_cash_num.getText().toString())) {
            ib_cash.setVisibility(View.VISIBLE);
        } else {
            ib_cash.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
