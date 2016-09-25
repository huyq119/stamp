package com.example.stamp.activity;

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

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.BaseBean;
import com.example.stamp.dialog.CountSureDialog;
import com.example.stamp.dialog.SendProgressDialog;
import com.example.stamp.dialog.SussessDialog;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Des3Encode;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyHandler;
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
 * 重置登录密码页面
 */
public class LoginPressWordActivity extends BaseActivity implements View.OnClickListener, TextWatcher, CountSureDialog.Count{

    private View mLoginPressWordTitle,mLoginPressWordContent;
    private ImageView mBack, mShowIcon;// 返回，是否显示密码
    private EditText mEtPhone, mEtCode, mEtPrassword;// 手机号，验证码，密码
    private Button mBtnCode, mBtnLogin;// 获取验证码，确定

    private static String RESET = "reset";// 次数标记
    private static String RESETTIME = "resettime";// 时间标记
    public SharedPreferences sp;
    private String mPhone, mCode, mPrassword;
    private SendProgressDialog pd;
    private boolean isShowPassWord = true;// 是否显示密码
    protected static final int SUCCESS = 0;// 获取验证码成功
    protected static final int CLOSEDIALOG = 1;// 关闭获取验证码对话框
    protected static final int COUNTDOWN = 2;// 倒计时功能
    protected static final int SHOWTEXTDIALOG = 3;// 展示内容对话框
    protected static final int LOGINSUCCESS = 4;// 登陆成功
    protected static final int FAILTEXTDIALOG = 5;// 登陆失败
    protected static final int REGISTERFAIL = 6;// 关闭失败对话框
    protected static final int FAIL = 7;// 服务器解析失败
    protected static final int SHOWAGAINDIALOG = 8;// 展示重新登录对话框
    protected static final int SUCCESSAGAIN = 9;// 需要再次登录
    protected static final int DELPROGRESS = 10;// 关闭对话框
    protected static final int FAILMSG = 11;// 短信失败信息
    private int Time = 60;// 倒计时的总时间
    private SussessDialog dialog;// 获取验证码成功,展示的对话框
    private String mToken;
    private String mUser_id;
    private MyHandler handler = new MyHandler(this) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:// 获取验证码成功,展示对话框
                    dialog = new SussessDialog(LoginPressWordActivity.this);
                    dialog.show();
                    break;
                case CLOSEDIALOG:// 2秒钟之后关闭对话框
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
                case COUNTDOWN:// 倒计时功能
                    mBtnCode.setEnabled(false);// 让验证码按钮不能点击
                    mBtnCode.setBackgroundResource(R.mipmap.code_normal);// 设置背景颜色
                    Time--;
                    if (Time > 0) {
                        handler.sendEmptyMessageDelayed(COUNTDOWN, 1000);
                        mBtnCode.setText("(已发送" + Time + "s)");
                    } else if (Time == 0) {
                        mBtnCode.setEnabled(true);
                        mBtnCode.setText("获取验证码");
                        mBtnCode.setBackgroundResource(R.mipmap.code);// 设置背景颜色
                        handler.removeMessages(COUNTDOWN);
                        Time = 60;
                    }
                    break;
                case LOGINSUCCESS:// 登陆成功
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    finish();
                    break;
                case SHOWTEXTDIALOG:// 展示内容对话框
                    dialog = new SussessDialog(LoginPressWordActivity.this);
                    dialog.setText("修改成功");
                    dialog.show();
                    break;
                case FAILTEXTDIALOG:// 登陆失败
                    String rspMsg = (String) msg.obj;
                    dialog = new SussessDialog(LoginPressWordActivity.this);
                    dialog.setText(rspMsg);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(REGISTERFAIL, 2000);
                    break;
                case FAIL:// 服务器解析异常
                    dialog = new SussessDialog(LoginPressWordActivity.this);
                    dialog.setText("请连接网络");
                    dialog.show();
                    handler.sendEmptyMessageDelayed(REGISTERFAIL, 2000);
                    break;
                case REGISTERFAIL:
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
                case SHOWAGAINDIALOG:// 展示重新登录对话框
                    dialog = new SussessDialog(LoginPressWordActivity.this);
                    dialog.setText("需要重新登录");
                    dialog.show();
                    break;
                case SUCCESSAGAIN:// 需要再次登录
                    Intent intent = new Intent(LoginPressWordActivity.this, LoginActivity.class);
                    intent.putExtra("WithDraw", "resetting");
                    startActivity(intent);
                    finish();
                    break;
                case DELPROGRESS:
                    if (pd != null)
                        pd.dismiss();
                    break;
                case FAILMSG:// 短信失败信息
                    String failMsg = (String) msg.obj;
                    dialog = new SussessDialog(LoginPressWordActivity.this);
                    dialog.setText(failMsg);
                    dialog.show();

                    mBtnCode.setEnabled(true);
                    mBtnCode.setText("获取验证码");
                    mBtnCode.setBackgroundResource(R.mipmap.code);// 设置背景颜色
                    handler.removeMessages(COUNTDOWN);
                    Time = 60;

                    handler.sendEmptyMessageDelayed(REGISTERFAIL, 2000);
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    public View CreateTitle() {
        mLoginPressWordTitle = View.inflate(this, R.layout.base_back_title, null);
        return mLoginPressWordTitle;
    }

    @Override
    public View CreateSuccess() {
        mLoginPressWordContent = View.inflate(this, R.layout.activity_loginpressword, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initListener();
        return mLoginPressWordContent;
    }
    @Override
    public void AgainRequest() {

    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

       mBack = (ImageView) mLoginPressWordTitle.findViewById(R.id.base_title_back);
        TextView  mTitle = (TextView) mLoginPressWordTitle.findViewById(R.id.base_title);
        mTitle.setText("重置登录密码");

        mEtPhone = (EditText) mLoginPressWordContent.findViewById(R.id.reset_phone);
        mEtCode = (EditText) mLoginPressWordContent.findViewById(R.id.reset_code);
        mEtPrassword = (EditText) mLoginPressWordContent.findViewById(R.id.reset_prassword);
        mBtnCode = (Button) mLoginPressWordContent.findViewById(R.id.resettingpw_code);
        mShowIcon = (ImageView) mLoginPressWordContent.findViewById(R.id.reset_showIcon);
        mBtnLogin = (Button) mLoginPressWordContent.findViewById(R.id.reset_login);

        if (!TextUtils.isEmpty(sp.getString("phone", ""))) {
            StringBuffer sb = new StringBuffer(sp.getString("phone", ""));
//            mEtPhone.setText(sb.replace(3, 7, "****"));
            mEtPhone.setText(sb);
            mEtPhone.setEnabled(false);
        } else {
            mEtPhone.setHint("请输入手机号");
            mEtPhone.setEnabled(true);
        }
    }

    private void initGetTexyString() {
        mPhone = mEtPhone.getText().toString().trim();
        mCode = mEtCode.getText().toString().trim();
        mPrassword = mEtPrassword.getText().toString().trim();
    }
    private void initListener(){
        mBack.setOnClickListener(this);
        mBtnCode.setOnClickListener(this);
        mShowIcon.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mEtCode.addTextChangedListener(this);
        mEtPrassword.addTextChangedListener(this);
        mEtPhone.addTextChangedListener(this);
        // 为输入密码框添加限制
        MyNumberKeyListener listener = new MyNumberKeyListener();
        mEtPrassword.setKeyListener(listener);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back:// 返回
                finishWitchAnimation();
            break;
            case R.id.resettingpw_code://获取验证码
                initGetTexyString();
                if (!TextUtils.isEmpty(mPhone)) {
                    if (PhoneUtile.judgePhoneNums(mPhone)) {
                        // 判断次数访问网络
                        getNetData(mPhone);
                    } else {
                        ShowToast.showshortToast("手机格式不正确");
                    }
                } else {
                    ShowToast.showshortToast("手机号码不能为空");
                }
                break;
            case R.id.reset_showIcon://是否显示密码按钮
                if (isShowPassWord) {
                    ShowPassWord.isshowPassWord(isShowPassWord, mShowIcon, mEtPrassword);
                    isShowPassWord = false;
                } else {
                    ShowPassWord.isshowPassWord(isShowPassWord, mShowIcon, mEtPrassword);
                    isShowPassWord = true;
                }
                break;
            case R.id.reset_login :// 确定按钮
                initGetTexyString();
                pd = new SendProgressDialog(LoginPressWordActivity.this);
                pd.show();
                getLoginContext(mPhone, mPrassword, mCode);// 获取注册访问网络内容
                break;
            default :
                break;
        }
    }


    /**
     * 判断获取验证码访问网络次数
     *
     * @param phone
     */
    private void getNetData(String phone) {
        // 判断
        ThreeCount.setThreeCount(this, RESET, RESETTIME);
        int current = sp.getInt(RESET, 0);
        Log.e("counts", "&" + current);
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 获取保存的时间
        long holdTime = sp.getLong(RESETTIME, 0);
        Log.e("测试", currentTimeMillis + "-" + holdTime);
        // 如果时间大于10分钟重新把次数归零
        if (currentTimeMillis - holdTime > StaticField.GIVENTIME) {
            sp.edit().putInt(RESET, 0).commit();
        }
        if (current > 4 && currentTimeMillis - holdTime < StaticField.GIVENTIME) {
            // 弹出对话框,并且跳出循环,重置次数
            showSureDialog(LoginPressWordActivity.this);// 展示对话框
            return;
        }

        sendMessage(phone);// 忘记密码获取验证码网络请求
    }

    /**
     * 展示对话框
     */
    private void showSureDialog(Context context) {
        CountSureDialog countSure = new CountSureDialog(context, RESET, pd);
        countSure.setmCount(this);
        countSure.show();
    }

    /**
     * @param phone
     * 重置密码获取验证码网络请求
     */
    private void sendMessage(final String phone) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                // 发送倒计时
                handler.sendEmptyMessage(COUNTDOWN);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.SMS_SEND);// 接口名称

                if (!TextUtils.isEmpty(sp.getString("phone", ""))) {
                    Log.e("本地获取的手机号-->", sp.getString("phone", ""));
                    params.put(StaticField.MOBILE, sp.getString("phone", ""));// 手机号
                } else {
                    params.put(StaticField.MOBILE, phone);// 手机号
                    Log.e("输入的的手机号-->", phone);
                }
                params.put(StaticField.SMS_TYPE, StaticField.XGDL);// 操作类型

                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    // 加入token和user_id
                    params.put(StaticField.TOKEN, mToken);// 登录标识
                    params.put(StaticField.USER_ID, mUser_id);// 用户ID
                }

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code); // 签名
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                Log.e("重置密码测试-->", result);
                if (result.equals("-1")) {
                    handler.sendEmptyMessage(FAIL);
                    return;
                }
                // 解析返回的结果
                Gson gson = new Gson();
                BaseBean mBaseBean = gson.fromJson(result, BaseBean.class);
                String mMsg = mBaseBean.getRsp_msg();
                Log.e("mMsg重置密码获取验证码--->", mMsg);
                if (mMsg.equals("成功")) {
                    handler.sendEmptyMessage(SUCCESS);
                    handler.sendEmptyMessageDelayed(CLOSEDIALOG, 2000);
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = FAILMSG;
                    msg.obj = mBaseBean.rsp_msg;
                    handler.sendMessage(msg);
                }
            }

        });
    }

    @Override
    public void SendCode() {
        initGetTexyString();
        getNetData(mPhone);
    }

    /**
     * 输入手机号，验证码，密码变化监听事件
     *
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // 判断电话号码,验证码,密码的长度从而让注册按钮可以点击
        if (mEtPrassword.getText().toString().length() >= 6 && mEtCode.getText().toString().length() == 6 && mEtPhone.getText().toString().length() == 11 && mEtPhone.getText().toString().length() == 11) {
            mBtnLogin.setBackgroundResource(R.drawable.btn_register_login_bg);
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setBackgroundResource(R.mipmap.login_normal);
            mBtnLogin.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    /**
     * 点击确定访问网络内容
     *
     * @param phone
     *            电话号码
     * @param mPassWordRegister
     *            密码
     * @param mCodeRegister
     *            验证码
     */
    private void getLoginContext(final String phone, final String mPassWordRegister, final String mCodeRegister) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.PWD_MODIFY);// 接口名称
                if (phone.contains("*")) {
                    params.put(StaticField.MOBILE, sp.getString("phone", ""));
                    Log.e("重置获取本地手机号-->：-->", phone);
                } else {
                    params.put(StaticField.MOBILE, phone);
                    Log.e("重置输入手机号-->：", phone);
                }
                params.put(StaticField.SMS_CODE, mCodeRegister);// 验证码
                Log.e("确定--重置输入的手机号——验证码：-->", phone+"--"+mCodeRegister);
                // 加密后的字节流
                byte[] PWByte = Des3Encode.encryptMode(mPassWordRegister.getBytes());
                String PWBase64 = new String(Base64.encode(PWByte, Base64.DEFAULT));
                params.put(StaticField.NEW_PWD, PWBase64); //密码
                params.put(StaticField.OP_TYPE, StaticField.CZDL);// 操作类型

                if (!TextUtils.isEmpty(mToken) && !TextUtils.isEmpty(mUser_id)) {
                    // 加入token和user_id
                    params.put(StaticField.TOKEN, mToken);// 登录标识
                    params.put(StaticField.USER_ID, mUser_id);// 用户ID
                    Log.e("重置密码获取，标识--用户ID：-->", mToken+"--"+mUser_id);
                }

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                handler.sendEmptyMessage(DELPROGRESS);
                if (result.equals("-1")) {// 服务器解析异常
                    handler.sendEmptyMessage(FAIL);
                    return;
                }
                Log.e("重置密码，确定-->", result);
                Gson gson = new Gson();
                BaseBean mBaseBean = gson.fromJson(result, BaseBean.class);
                // 返回的获取信息
                String rspMsgContext = mBaseBean.getRsp_msg();
                if (rspMsgContext.equals("成功")) {
                    handler.sendEmptyMessage(SHOWTEXTDIALOG);
                    handler.sendEmptyMessageDelayed(LOGINSUCCESS, 2000);
                } else if (rspMsgContext.equals("用户需要重新登录")) {
                    handler.sendEmptyMessage(SHOWAGAINDIALOG);
                    handler.sendEmptyMessageDelayed(SUCCESSAGAIN, 2000);
                } else {
                    Log.e("ceshi", "是否执行");
                    String rspMsg = mBaseBean.getRsp_msg();
                    Message msg = handler.obtainMessage();
                    msg.what = FAILTEXTDIALOG;
                    msg.obj = rspMsg;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
