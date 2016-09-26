package com.example.stamp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stamp.MainActivity;
import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.BaseBean;
import com.example.stamp.bean.LoginRegisterBean;
import com.example.stamp.dialog.CountRegisterDialog;
import com.example.stamp.dialog.CountSureDialog;
import com.example.stamp.dialog.RegisterDialog;
import com.example.stamp.dialog.SendProgressDialog;
import com.example.stamp.dialog.SussessDialog;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Des3Encode;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyNumberKeyListener;
import com.example.stamp.utils.PhoneUtile;
import com.example.stamp.utils.ShowPassWord;
import com.example.stamp.utils.ShowToast;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.utils.ThreeCount;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, CountSureDialog.Count, CompoundButton.OnCheckedChangeListener, TextWatcher, CountRegisterDialog.Register {
    private static final long GIVENTIME = 1000 * 60 * 10;// 设定时间
    protected static final int SUCCESS = 0;// 获取验证码成功
    protected static final int CLOSEDIALOG = 1;// 关闭获取验证码对话框
    protected static final int COUNTDOWN = 2;// 倒计时功能
    protected static final int REGISTERSUCCESS = 3;// 注册成功
    protected static final int SHOWTEXTDIALOG = 4;// 展示内容对话框
    protected static final int FAILTEXTDIALOG = 5;// 失败对话框
    protected static final int REGISTERFAIL = 6;// 注册失败
    protected static final int FAIL = 7;// 服务器解析失败
    protected static final int DELPRESSAGE = 8;// 关闭进度条
    protected static final int SHOWLOGIN = 9;// 展示有账号登录的对话框
    protected static final int FAILPHONE = 11;// 手机号能否修改
    private int Time = 60;// 倒计时的总时间
    private CheckBox mCB;// 用户协议的选择
    private boolean mCBFlag = true;// 用户协议的标识
    private View mRegisterContent;
    private View mRegisterTitle;
    private ImageView mBack, mShowIcon;
    private TextView mService, medit,mHint;
    private EditText mPhone, mEtPrassword, mEtCode;
    private Button mCodeBtn, mRegisterBtn;
    private CheckBox mCheckBox;
    private String mMobile, mCode, mPrassword;
    private static String CURRENT = "current";
    private static String CURRENTTIME = "currentTime";
    private static final String REGISTER = "register";
    private static final String REGISTERTIME = "registertime";
    private SendProgressDialog pd;
    public SharedPreferences sp;
    private SussessDialog dialog;
    private boolean isShowPassWord = false;// 是否显示密码
    private RegisterDialog rDialog;

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.CODE_SUCCESS:// 获取验证码成功,展示对话框
                    dialog = new SussessDialog(RegisterActivity.this);
                    dialog.show();
                    // 获取验证码成功,手机号不能修改
                    mPhone.setEnabled(false);
                    // 保存注册的手机号码
                    sp.edit().putString("registerPhone", mPhone.getText().toString().trim()).commit();
                    break;
                case CLOSEDIALOG:// 2秒钟之后关闭对话框
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
                case COUNTDOWN:// 倒计时功能
                    mCodeBtn.setEnabled(false);// 让验证码按钮不能点击
                    mCodeBtn.setBackgroundResource(R.mipmap.code_normal);// 设置背景颜色
                    Time--;
                    if (Time > 0) {
                        mHandler.sendEmptyMessageDelayed(COUNTDOWN, 1000);
                        mCodeBtn.setText("(已发送" + Time + "s)");
                    } else if (Time == 0) {
                        mCodeBtn.setEnabled(true);
                        mCodeBtn.setText("获取验证码");
                        mCodeBtn.setBackgroundResource(R.mipmap.code);// 设置背景颜色
                        mHandler.removeMessages(COUNTDOWN);
                        Time = 60;
                    }
                    break;
                case REGISTERSUCCESS:// 注册成功
                    if (dialog != null) {
                        dialog.dismiss();
                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Login","login");
//                    openActivityWitchAnimation(MainActivity.class,bundle);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("Login", "login");
                    startActivity(intent);
                    finish();
                    break;
                case SHOWTEXTDIALOG:// 展示内容对话框
                    dialog = new SussessDialog(RegisterActivity.this);
                    dialog.setText("注册成功");
                    dialog.show();
                    break;
                case FAILTEXTDIALOG:// 注册失败
                    String rspMsg = (String) msg.obj;
                    dialog = new SussessDialog(RegisterActivity.this);
                    dialog.setText(rspMsg);
                    dialog.show();
                    mHandler.sendEmptyMessageDelayed(CLOSEDIALOG, 2000);
                    break;
//                case REGISTERFAIL :
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                    break;
                case DELPRESSAGE:// 关闭进度条
                    if (pd != null) {
                        pd.dismiss();
                    }
                    break;
                case SHOWLOGIN:
                    String phone = (String) msg.obj;
                    rDialog = new RegisterDialog(RegisterActivity.this, phone);
                    rDialog.show();
                    break;
                case FAILPHONE:// 手机号码可以变更
                    mPhone.setEnabled(true);
                    break;

            }
        }
    };

    @Override
    public View CreateTitle() {
        mRegisterTitle = View.inflate(this, R.layout.base_font_title, null);
        return mRegisterTitle;
    }

    @Override
    public View CreateSuccess() {
        mRegisterContent = View.inflate(this, R.layout.activity_register, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initListener();
        return mRegisterContent;
    }

    @Override
    public void AgainRequest() {

    }


    private void initView() {
        mBack = (ImageView) mRegisterTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mRegisterTitle.findViewById(R.id.base_title);
        mTitle.setText("注册");
        medit = (TextView) mRegisterTitle.findViewById(R.id.base_edit);
        medit.setText("登录");
        mCodeBtn = (Button) mRegisterContent.findViewById(R.id.register_code);
        mPhone = (EditText) mRegisterContent.findViewById(R.id.register_phone);
        mEtCode = (EditText) mRegisterContent.findViewById(R.id.register_et_code);
        mEtPrassword = (EditText) mRegisterContent.findViewById(R.id.register_prassword);
        mShowIcon = (ImageView) mRegisterContent.findViewById(R.id.register_showIcon);
        mRegisterBtn = (Button) mRegisterContent.findViewById(R.id.register_login);
        mCheckBox = (CheckBox) mRegisterContent.findViewById(R.id.register_checked);
        mService = (TextView) mRegisterContent.findViewById(R.id.register_service);
        mHint = (TextView) mRegisterContent.findViewById(R.id.register_hint);

        // 设置保存的注册手机号码
        String registerPhone = sp.getString("registerPhone", "");
        mPhone.setText(registerPhone);
    }

    // 获取组件的值
    private void GetTextString() {
        mMobile = mPhone.getText().toString().trim();
        mCode = mEtCode.getText().toString().trim();
        mPrassword = mEtPrassword.getText().toString().trim();
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        medit.setOnClickListener(this);
        mCodeBtn.setOnClickListener(this);
        mShowIcon.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mService.setOnClickListener(this);
        mCheckBox.setOnCheckedChangeListener(this);
        mPhone.addTextChangedListener(this);
        mEtCode.addTextChangedListener(this);
        mEtPrassword.addTextChangedListener(this);
        // 为输入密码框添加限制
        MyNumberKeyListener listener = new MyNumberKeyListener();
        mEtPrassword.setKeyListener(listener);
    }

    @Override
    public void onClick(View view) {
//        GetTextString();// 获取组件的值
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_edit://登录
                openActivityWitchAnimation(LoginActivity.class);
                break;
            case R.id.register_code://获取验证码
                GetTextString();// 获取组件的值
                if (!TextUtils.isEmpty(mMobile)) {
                    if (PhoneUtile.judgePhoneNums(mMobile)) {
                        // 判断次数访问网络
                        getNetData(mMobile);
                    } else {
                        ShowToast.showshortToast("手机格式不正确");
                    }
                } else {
                    ShowToast.showshortToast("手机号码不能为空");
                }
                break;
            case R.id.register_showIcon://是否显示密码按钮
                if (isShowPassWord) {
                    mEtPrassword.setTextSize(16);
                    ShowPassWord.isshowPassWord(isShowPassWord, mShowIcon, mEtPrassword);
                    isShowPassWord = false;
                } else {
                    mEtPrassword.setTextSize(14);
                    ShowPassWord.isshowPassWord(isShowPassWord, mShowIcon, mEtPrassword);
                    isShowPassWord = true;
                }
                break;
            case R.id.register_login://注册
                GetTextString();// 获取组件的值
                pd = new SendProgressDialog(RegisterActivity.this);
                pd.show();
                getRegisterCurrent(mMobile, mPrassword, mCode);
                break;
            case R.id.register_service://查看邮来邮网协议
                openActivityWitchAnimation(ServiceProtocolActivity.class);
                break;
            default:
                break;
        }
    }


    /**
     * 注册获取验证码网络请求
     */
    private void SendCodeRequestNet(final String mMobile) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                // 发送倒计时
                mHandler.sendEmptyMessage(COUNTDOWN);
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SMS_SEND);// 接口名称
                params.put(StaticField.MOBILE, mMobile);// 手机号
                params.put(StaticField.SMS_TYPE, "ZC"); // 短信类型(注册)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                Log.e("获取短信~~~~>", result);
                if (result.equals("-1")) {
                    mHandler.sendEmptyMessage(FAIL);
                    return;
                }

                Gson gson = new Gson();
                BaseBean mBaseBean = gson.fromJson(result, BaseBean.class);
                String mMsg = mBaseBean.getRsp_msg();
                Log.e("mMsg获取验证码--->", mMsg);
                if (mMsg.equals("成功")) {
                    mHandler.sendEmptyMessage(StaticField.CODE_SUCCESS);
                    mHandler.sendEmptyMessageDelayed(CLOSEDIALOG, 2000);
                } else {// 判断手机号码是否需要不能更改,失败的话可以更改
                    mHandler.sendEmptyMessage(FAILPHONE);
                }
            }
        });
    }

    /**
     * 注册网络请求
     *
     * @param phone             电话号码
     * @param mPassWordRegister 密码
     * @param mCodeRegister     验证码
     */
    private void getRegisterRequestNet(final String phone, final String mPassWordRegister, final String mCodeRegister) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.LOGIN_REGISTER);// 接口名称
                params.put(StaticField.MOBILE, phone);// 手机号
                // 加密后的字节流
                byte[] PWByte = Des3Encode.encryptMode(mPassWordRegister.getBytes());
                String PWBase64 = new String(Base64.encode(PWByte, Base64.DEFAULT));
                params.put(StaticField.PASSWORD, PWBase64);// 密码
                params.put(StaticField.SMS_CODE, mCodeRegister);// 验证码
                params.put(StaticField.OP_TYPE, StaticField.ZC); // 操作类型(注册)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                mHandler.sendEmptyMessage(DELPRESSAGE);
                if (result.equals("-1")) {
                    mHandler.sendEmptyMessage(FAIL);
                    return;
                }
                Log.e("测试注册--->", result);
                Gson gson = new Gson();
                BaseBean mBaseBean = gson.fromJson(result, BaseBean.class);
                // 返回的获取信息
                String rspMsgContext = mBaseBean.getRsp_msg();
                if (rspMsgContext.equals("成功")) {
                    mHandler.sendEmptyMessage(SHOWTEXTDIALOG);
                    mHandler.sendEmptyMessageDelayed(REGISTERSUCCESS, 2000);
                    LoginRegisterBean mLogRegBean = gson.fromJson(result, LoginRegisterBean.class);
                    sp.edit().putString("phone", phone).commit();
                    sp.edit().putString("token", mLogRegBean.getToken()).commit();
                    sp.edit().putString("userId", mLogRegBean.getUserId()).commit();
                } else if (rspMsgContext.equals("该手机号已注册邮来邮往请直接登录")) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = phone;
                    msg.what = SHOWLOGIN;
                    mHandler.sendMessage(msg);
                } else {
                    String rspMsg = mBaseBean.getRsp_msg();
                    Message msg = mHandler.obtainMessage();
                    msg.what = FAILTEXTDIALOG;
                    msg.obj = rspMsg;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }


    /**
     * 判断点击获取验证码访问网络的次数
     */
    private void getNetData(final String mMobile) {
        // 判断
        ThreeCount.setThreeCount(this, CURRENT, CURRENTTIME);
        int current = sp.getInt(CURRENT, 0);
        Log.e("counts", "&" + current);
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 获取保存的时间
        long holdTime = sp.getLong(CURRENTTIME, 0);
        Log.e("测试", currentTimeMillis + "-" + holdTime);
        // 如果时间大于10分钟重新把次数归零
        if (currentTimeMillis - holdTime > GIVENTIME) {
            sp.edit().putInt(CURRENT, 0).commit();
        }
        if (current >= 4 && currentTimeMillis - holdTime < GIVENTIME) {
            // 弹出对话框,并且跳出循环,重置次数
            showSureDialog(RegisterActivity.this);// 展示对话框
            return;
        }
        SendCodeRequestNet(mMobile); //注册获取验证码网络请求
    }

    /**
     * 获取验证码展示对话框
     */
    private void showSureDialog(Context context) {
        CountSureDialog countSure = new CountSureDialog(context, CURRENT, pd);
        countSure.setmCount(this);
        countSure.show();
    }


    /**
     * 判断点击注册访问网络的次数
     *
     * @param mCodeLogin
     * @param mPassWordLogin
     * @param mPhoneLogin
     */
    private void getRegisterCurrent(String mPhoneLogin, String mPassWordLogin, String mCodeLogin) {
        // 判断
        ThreeCount.setThreeCount(this, REGISTER, REGISTERTIME);
        int current = sp.getInt(REGISTER, 0);
        Log.e("counts", "&" + current);
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 获取保存的时间
        long holdTime = sp.getLong(REGISTERTIME, 0);
        Log.e("测试", currentTimeMillis + "-" + holdTime);
        // 如果时间大于10分钟重新把次数归零
        if (currentTimeMillis - holdTime > GIVENTIME) {
            sp.edit().putInt(REGISTER, 0).commit();
        }
        if (current >= 4 && currentTimeMillis - holdTime < GIVENTIME) {
            // 弹出对话框,并且跳出循环,重置次数
            showRegisterDialog(RegisterActivity.this);// 展示对话框
            return;
        }
        getRegisterRequestNet(mPhoneLogin, mPassWordLogin, mCodeLogin);// 注册网络请求
    }

    /**
     * 注册创建对话框
     */
    private void showRegisterDialog(Context context) {
        CountRegisterDialog countSure = new CountRegisterDialog(context, REGISTER, pd);
        countSure.setmRegister(this);
        countSure.show();
    }

    @Override
    public void SendCode() {
        GetTextString();
        getNetData(mMobile);
    }

    @Override
    public void RegisterCode() {
        GetTextString();
        getRegisterCurrent(mMobile, mPrassword, mCode);
    }

    /**
     * 为输入密码框添加限制的监听事件
     *
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCBFlag = b;
        if (mCBFlag && mEtPrassword.getText().toString().length() >= 6 && mEtCode.getText().toString().length() == 6 && mPhone.getText().toString().length() == 11 && mCBFlag) {
            mRegisterBtn.setBackgroundResource(R.drawable.btn_register_login_bg);
            mRegisterBtn.setEnabled(true);
        } else {
            mRegisterBtn.setBackgroundResource(R.mipmap.login_normal);
            mRegisterBtn.setEnabled(false);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    /**
     * 判断电话号码,验证码,密码的长度从而让注册按钮可以点击
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mEtPrassword.getText().toString().length() >= 6 || mEtPrassword.getText().toString().length() < 1) {
            mHint.setVisibility(View.GONE);
        } else {
            mHint.setVisibility(View.VISIBLE);
        }
        // 判断电话号码,验证码,密码的长度从而让注册按钮可以点击
        if (mEtPrassword.getText().toString().length() >= 6 && mEtCode.getText().toString().length() == 6 && mPhone.getText().toString().length() == 11 && mCBFlag) {
            mRegisterBtn.setBackgroundResource(R.drawable.btn_register_login_bg);
            mRegisterBtn.setEnabled(true);
        } else {
            mRegisterBtn.setBackgroundResource(R.mipmap.login_normal);
            mRegisterBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
