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

import java.util.HashMap;

import cn.com.chinau.MainActivity;
import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.LoginRegisterBean;
import cn.com.chinau.dialog.CountSureDialog;
import cn.com.chinau.dialog.SendProgressDialog;
import cn.com.chinau.dialog.SussessDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Des3Encode;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyNumberKeyListener;
import cn.com.chinau.utils.PhoneUtile;
import cn.com.chinau.utils.ShowPassWord;
import cn.com.chinau.utils.ShowToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.utils.ThreeCount;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher, CountSureDialog.Count {
    protected static final int SUCCESS = 0;// 成功
    protected static final int DELSUCCESS = 1;// 删除成功弹框
    protected static final int FIAL = 2;// 登陆失败
    protected static final int DELFIAL = 3;// 删除登陆失败对话框
    protected static final int LOGIN = 4;// 让按钮可以点击
    protected static final int NONET = 5;// 没有网络
    private static String CURRENT = "logincurrent";
    private static String CURRENTTIME = "logincurrentTime";
    private SharedPreferences sp;
    public static final String name = "stamp";
    public static final long GIVENTIME = 1000 * 60 * 10;// 设定时间
    private int flag = 0;// 判断是否从哪个页面来的数据

    private View mLoginTitle, mLoginContent;//内容页面
    private ImageView mBack, mShowIcon;//返回按钮
    private TextView mFP, mRegister;//忘记密码,注册
    private EditText mEtName, mEtPrassword;
    private Button mLoginBtn;
    private boolean isShowPassWord = true;// 是否显示密码
    private SendProgressDialog pd;
    private String mpw, mAccount,phone;
    private SussessDialog dialog;


    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS :// 登陆成功
                    dialog = new SussessDialog(LoginActivity.this);
                    dialog.setText("登录成功");
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELSUCCESS, 1000);
                    break;
                case DELSUCCESS :// 删除成功对话框
                    if (dialog != null)
                        dialog.dismiss();
                    if (flag == 1) {// 从提现页面来的
//                        startActivity(new Intent(LogInActivity.this, WithdrawPassWordActivity.class));
                        finish();
                    } else if (flag == 2) {// 从重置登陆密码页面
//                        startActivity(new Intent(LogInActivity.this, ResettingPassWordActivity.class));
                        finish();
                    } else if (flag == 5) {// 从订单页面来的
//                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
//                        intent.putExtra("Login", "Order");
//                        startActivity(intent);
                        finish();
                    } else if (flag == 6) {// 从密码管理页面来的
//                        startActivity(new Intent(LogInActivity.this, PressWordManagerActivity.class));
                        finish();
                    } else if (flag == 7) {// 回购页面来的
                        String result = sp.getString("ScanBack", "");
                        if (!TextUtils.isEmpty(result)) {
//                            Intent intent = new Intent(LogInActivity.this, Activity_ensure_back_buy.class);
//                            intent.putExtra("Result", result);
//                            startActivity(intent);
                            finish();
                        }
                    } else if (flag == 9) {// 回购页面来的
                        finish();
                    } else if (flag == 10) {// 回购页面来的
//                        startActivity(new Intent(LogInActivity.this, OrderActivity.class));
                        finish();
                    } else if (flag == 8) {// 回购页面来的
//                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                        finish();
                    } else {// 不是从提现页面来的
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("Login", "login");
                        startActivity(intent);
//                        finish();
                        finishWitchAnimation();
                    }
                    break;
                case FIAL :// 删除成功对话框
                    handler.sendEmptyMessageDelayed(DELFIAL, 1000);
                    String rsp_msg = (String) msg.obj;
                    dialog = new SussessDialog(LoginActivity.this);
                    dialog.setText(rsp_msg);
                    dialog.show();
                    break;
                case DELFIAL :// 删除失败对话框
                    if (dialog != null) {
                        dialog.dismiss();
                    }

                    break;
                case LOGIN :
                    if (pd != null) {
                        pd.dismiss();
                    }
                    mLoginBtn.setText("登录");
                    mLoginBtn.setEnabled(true);
                    break;
                case NONET :// 没有网络
                    dialog = new SussessDialog(LoginActivity.this);
                    dialog.setText("请连接网络");
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELFIAL, 2000);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View CreateTitle() {
        mLoginTitle = View.inflate(this, R.layout.base_back_title, null);
        return mLoginTitle;
    }

    @Override
    public View CreateSuccess() {
        mLoginContent = View.inflate(this, R.layout.activity_login_content, null);
        sp = getSharedPreferences(name, MODE_PRIVATE);
        getLogInIntent();
        initView();
        initListener();
        return mLoginContent;
    }

    /**
     * 获取那个页面启动的
     */
    private void getLogInIntent() {
        Intent intent = getIntent();
        String WithDraw = intent.getStringExtra("WithDraw");// 判断是从哪个页面来的
        if (WithDraw != null && WithDraw.equals("withdraw")) {// 说明是从提现页面来的
            flag = 1;// 标记是否是从提现页面来的
        } else if (WithDraw != null && WithDraw.equals("resetting")) {
            flag = 2;// 标记是否是从提现页面来的
        } else if (WithDraw != null && WithDraw.equals("myFragment")) {
            flag = 3;// 标记是否是从我的页面来的
        } else if (WithDraw != null && WithDraw.equals("upDate")) {
            flag = 4;// 标记是否是从绑定银行卡页面来的
        } else if (WithDraw != null && WithDraw.equals("Order")) {
            flag = 5;// 订单详情页面来的
        } else if (WithDraw != null && WithDraw.equals("pwManager")) {
            flag = 6;// 密码管理页面来的
        } else if (WithDraw != null && WithDraw.equals("buyback")) {
            flag = 7;// 回购页面
        } else if (WithDraw != null && WithDraw.equals("myFragmentLogin")) {
            flag = 8;// 我的页面去登陆页面
        } else if (WithDraw != null && WithDraw.equals("buy_back")) {
            flag = 9;
        } else if (WithDraw != null && WithDraw.equals("OrderActivity")) {
            flag = 10;
        } else if (WithDraw != null && WithDraw.equals("register")) {// 注册页面是否显示电话号码
            phone = intent.getStringExtra("phone");
            flag = 11;
        }

    }

    private void initView() {
        TextView mTitle = (TextView) mLoginTitle.findViewById(R.id.base_title);
        mTitle.setText("登录");
        mBack = (ImageView) mLoginTitle.findViewById(R.id.base_title_back);
        mEtName = (EditText) mLoginContent.findViewById(R.id.login_name);// 用户名、手机号
        mEtPrassword = (EditText) mLoginContent.findViewById(R.id.login_prassword);// 密码
        mShowIcon = (ImageView) mLoginContent.findViewById(R.id.login_showIcon);// 是否显示密码按钮
        mLoginBtn = (Button) mLoginContent.findViewById(R.id.login_btn);// 登录
        mFP = (TextView) mLoginContent.findViewById(R.id.login_ForgetPassword);//  忘记密码
        mRegister = (TextView) mLoginContent.findViewById(R.id.login_register);// 注册
        if (flag == 11) {
            mEtName.setText(phone);
        }
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mShowIcon.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mFP.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mEtPrassword.addTextChangedListener(this);
        mEtName.addTextChangedListener(this);
        // 为输入密码框添加限制
        MyNumberKeyListener listener = new MyNumberKeyListener();
        mEtPrassword.setKeyListener(listener);
    }

    /**
     * 获取账号和密码
     */
    private void GetEditTenxtString() {
        // 密码
        mAccount = mEtName.getText().toString();// 账号
        mpw = mEtPrassword.getText().toString();
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.login_showIcon://是否显示密码按钮
                if (isShowPassWord) {
                    ShowPassWord.isshowPassWord(isShowPassWord, mShowIcon, mEtPrassword);
                    isShowPassWord = false;
                } else {
                    ShowPassWord.isshowPassWord(isShowPassWord, mShowIcon, mEtPrassword);
                    isShowPassWord = true;
                }
                break;
            case R.id.login_btn://登录
                GetEditTenxtString();
                JudgePhoneorName(mpw, mAccount);// 判断电话号码和账号
                break;
            case R.id.login_ForgetPassword://忘记密码
                openActivityWitchAnimation(ResettingPassWordActivity.class);
                break;
            case R.id.login_register://注册
//                Bundle bundle = new Bundle();
//                bundle.putString("Login", "login");
//                openActivityWitchAnimation(RegisterActivity.class, bundle);
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("Login", "login");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
        }
    }

    /**
     * 判断电话号码和账号
     *
     * @param mAccount 账号
     * @param mpw      密码
     */
    private void JudgePhoneorName(String mpw, String mAccount) {
        if (PhoneUtile.judgePhoneNums(mAccount)) {
            if (HttpUtils.isConn(LoginActivity.this)) {
                pd = new SendProgressDialog(LoginActivity.this);
                pd.show();
            }
            getNetData();
        } else {// 手机格式不正确
            ShowToast.showshortToast("手机号格式不正确");
        }
    }

    /**
     * 判断次数请求网络
     */
    private void getNetData() {
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
            showSureDialog(LoginActivity.this);// 展示对话框
            return;
        }
        LoginRequestNet();
    }

    /**
     * 展示对话框
     */
    private void showSureDialog(Context context) {
        CountSureDialog countSure = new CountSureDialog(context, CURRENT, pd);
        countSure.setmCount(this);
        countSure.show();
    }

    /**
     * 登录网络请求
     */
    private void LoginRequestNet() {
        mLoginBtn.setEnabled(false);
        mLoginBtn.setText("登录中...");

        GetEditTenxtString();// 获取手机号和密码
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.LOGIN_REGISTER);//接口名称
                params.put(StaticField.MOBILE, mAccount);// 手机号
                // 加密后的字节流
                byte[] PWByte = Des3Encode.encryptMode(mpw.getBytes());
                String PWBase64 = new String(Base64.encode(PWByte, Base64.DEFAULT));
                params.put(StaticField.PASSWORD, PWBase64);// 密码
                params.put(StaticField.OP_TYPE, StaticField.DL);// 操作类型(登录)
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                handler.sendEmptyMessage(LOGIN);
                Log.e("登录--->", result);
                if (result.equals("-1")) {
                    handler.sendEmptyMessage(NONET);
                    return;
                }
                Gson gson = new Gson();
                LoginRegisterBean mLogRegBean = gson.fromJson(result, LoginRegisterBean.class);
                if (mLogRegBean.getRsp_msg().equals("成功")) {
                    // 保存手机号，token和用户ID
                    sp.edit().putString("phone", mAccount).commit();
                    sp.edit().putString("token", mLogRegBean.getToken()).commit();
                    sp.edit().putString("userId", mLogRegBean.getUserId()).commit();
                    Log.e("登录--->", mAccount+"-->"+mLogRegBean.getToken()+"-->"+mLogRegBean.getUserId());
                    handler.sendEmptyMessage(SUCCESS);
                } else {
                    Log.e("失败--->", "哈哈哈哈哈哈----》");
                    Message msg = handler.obtainMessage();
                    msg.what = FIAL;
                    msg.obj = mLogRegBean.getRsp_msg();
                    handler.sendMessage(msg);
                }
            }

        });

    }


    @Override
    public void SendCode() {
        LoginRequestNet();//登录网络请求
    }


    /*
	 * 下面是对EditText的监听
	 */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    // 输入账号和密码的监听方法
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mEtPrassword.getText().length() >= 6 && mEtName.getText().length() == 11) {
            mLoginBtn.setBackgroundResource(R.drawable.btn_register_login_bg);
            mLoginBtn.setEnabled(true);
        } else {
            mLoginBtn.setEnabled(false);
            mLoginBtn.setBackgroundResource(R.mipmap.login_normal);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


}
