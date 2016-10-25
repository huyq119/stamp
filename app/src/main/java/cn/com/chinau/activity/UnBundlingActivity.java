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
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.utils.ThreeCount;

/**
 * 解绑银行卡页面
 */
public class UnBundlingActivity extends BaseActivity implements View.OnClickListener,TextWatcher,CountSureDialog.Count {


    private View mUnBundlingTitle;
    private View mUnBundlingContent;

    private EditText et_cash_pw, et_code;// 提现密码,验证码,手机号码
    private TextView tv_carNum, tv_carName, tv_phone,mTitle;// 卡号,银行名称
    private Button btn_forget_pw, btn_get_code, btn_cimmit;// 忘记密码,获取验证码,确定
    private ImageView mIcon,mBack;// 银行卡图标
    private static String UNBUNDLING = "Unbundling";
    private static String UNBUNDLINGTIME = "Unbundlingtime";
    protected static final int SUCCESS = 0;// 获取验证码成功
    protected static final int CLOSEDIALOG = 1;// 关闭获取验证码对话框
    protected static final int COUNTDOWN = 2;// 倒计时功能
    protected static final int REGISTERSUCCESS = 3;// 注册成功
    protected static final int SHOWTEXTDIALOG = 4;// 展示内容对话框
    protected static final int FAILTEXTDIALOG = 5;// 失败对话框
    protected static final int REGISTERFAIL = 6;// 修改失败
    protected static final int FAIL = 7;// 服务器解析失败
    protected static final int SHOWAGAINDIALOG = 8;// 展示重新登录对话框
    protected static final int SUCCESSAGAIN = 9;// 需要再次登录
    protected static final int DELPRESSAGE = 10;// 关闭进度条对话框
    private SendProgressDialog pd;
    private int Time = 60;// 倒计时的总时间
    private String mToken;
    private String mUser_id;

    private MyHandler handler = new MyHandler(this) {

        private SussessDialog dialog;// 获取验证码成功,展示的对话框

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS :// 获取验证码成功,展示对话框
                    dialog = new SussessDialog(UnBundlingActivity.this);
                    dialog.show();
                    break;
                case CLOSEDIALOG :// 2秒钟之后关闭对话框
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
                case COUNTDOWN :
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
                    dialog = new SussessDialog(UnBundlingActivity.this);
                    dialog.setText("请连接网络");
                    dialog.show();
                    handler.sendEmptyMessageDelayed(REGISTERFAIL, 2000);
                    break;
                case REGISTERFAIL :
                    if (dialog != null)
                        dialog.dismiss();
                    break;
                case DELPRESSAGE :// 关闭进度条对话框
                    if (pd != null)
                        pd.dismiss();
                    break;
                case SHOWTEXTDIALOG :// 展示内容对话框
                    sp.edit().putInt("isBeanCard", 0).commit();
                    dialog = new SussessDialog(UnBundlingActivity.this);
                    dialog.setText("银行卡解绑成功");
                    dialog.show();
                    break;
                case REGISTERSUCCESS :// 设置成功
                    if (dialog != null)
                        dialog.dismiss();
                    startActivity(new Intent(UnBundlingActivity.this, CashEmptyActivity.class));
                    finish();// 回到银行卡页面
                    break;
                case SHOWAGAINDIALOG :// 展示重新登录对话框
                    dialog = new SussessDialog(UnBundlingActivity.this);
                    dialog.setText("需要重新登录");
                    dialog.show();
                    break;
                case SUCCESSAGAIN :// 需要再次登录
                    Intent intent = new Intent(UnBundlingActivity.this, LoginActivity.class);
                    intent.putExtra("WithDraw", "withdraw");
                    startActivity(intent);
                    finish();
                    break;
                case FAILTEXTDIALOG :// 注册失败
                    String rspMsg = (String) msg.obj;
                    dialog = new SussessDialog(UnBundlingActivity.this);
                    dialog.setText(rspMsg);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(REGISTERFAIL, 2000);
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
        mUnBundlingTitle = View.inflate(this, R.layout.base_back_title, null);
        return mUnBundlingTitle;
    }

    @Override
    public View CreateSuccess() {
        mUnBundlingContent = View.inflate(this, R.layout.activity_cash_card_jiebang, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_APPEND);
        bitmapUtils = BitmapHelper.getBitmapUtils();
        return mUnBundlingContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView(){
      mBack = (ImageView)mUnBundlingTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mUnBundlingTitle.findViewById(R.id.base_title);
        mTitle.setText("解绑银行卡");
        mToken = sp.getString("token", null);
        mUser_id = sp.getString("userId", null);
        et_code = (EditText) mUnBundlingContent.findViewById(R.id.et_code);
        btn_forget_pw = (Button) mUnBundlingContent.findViewById(R.id.btn_forget_pw);
        et_cash_pw = (EditText) mUnBundlingContent.findViewById(R.id.et_cash_pw);
        tv_phone = (TextView) mUnBundlingContent.findViewById(R.id.tv_phone);
        btn_get_code = (Button) mUnBundlingContent.findViewById(R.id.btn_get_code);
        btn_cimmit = (Button) mUnBundlingContent.findViewById(R.id.btn_cimmit);
        tv_carName = (TextView) mUnBundlingContent.findViewById(R.id.tv_name);
        mIcon = (ImageView) mUnBundlingContent.findViewById(R.id.ib_cash);
        tv_carNum = (TextView) mUnBundlingContent.findViewById(R.id.tv_cadnum);
        initData();
        initListener();
    }
    /**
     * 添加数据
     */
    private void initData() {
        String bank_name = sp.getString("bankName", "");
        String bank_abbr = sp.getString("bankADD", "");
        String bankNum = sp.getString("bankNum", "");

        StringBuffer sb = new StringBuffer(sp.getString("phone", ""));
        tv_phone.setText(sb.replace(3, 7, "****"));
        if (!TextUtils.isEmpty(bank_name) && !TextUtils.isEmpty(bank_abbr) && !TextUtils.isEmpty(bankNum)) {
            setCarIcon(bank_abbr, bank_name);
            tv_carNum.setText("尾号  " + bankNum.substring(bankNum.length() - 4, bankNum.length()));
        }

    }
    private void initListener(){
        btn_cimmit.setOnClickListener(this);
        btn_get_code.setOnClickListener(this);
        btn_forget_pw.setOnClickListener(this);
        et_cash_pw.addTextChangedListener(this);
        et_code.addTextChangedListener(this);

        // 为输入密码框添加限制
        MyNumberKeyListener listener = new MyNumberKeyListener();
        et_cash_pw.setKeyListener(listener);
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
                bitmapUtils.display(mIcon, Icon[0]);
            } else if (num > 480 * 800 && num < 960 * 720) {// 用2倍图
                bitmapUtils.display(mIcon, Icon[1]);
            } else {// 用3倍图
                bitmapUtils.display(mIcon, Icon[2]);
            }
            tv_carName.setText(bank_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_back :// 返回
                finishWitchAnimation();
                break;
            case R.id.btn_get_code :// 获取验证码
                // 判断次数访问网络
                getNetData();
                break;
            case R.id.btn_forget_pw :// 忘记密码
                Intent intent = new Intent(UnBundlingActivity.this, WithdrawPressWordActivity.class);
                intent.putExtra("Unbundling", "Unbundling");
                startActivity(intent);
                break;
            default :
                break;
        }
    }

    /**
     * 判断次数访问网络
     */
    private void getNetData() {
        // 判断
        ThreeCount.setThreeCount(this, UNBUNDLING, UNBUNDLINGTIME);
        int current = sp.getInt(UNBUNDLING, 0);
        Log.e("counts", "&" + current);
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        // 获取保存的时间
        long holdTime = sp.getLong(UNBUNDLINGTIME, 0);
        Log.e("测试", currentTimeMillis + "-" + holdTime);
        // 如果时间大于10分钟重新把次数归零
        if (currentTimeMillis - holdTime > StaticField.GIVENTIME) {
            Log.e("设置时间", "是否执行");
            sp.edit().putInt(UNBUNDLING, 0).commit();
        }
        if (current >= 4 && currentTimeMillis - holdTime < StaticField.GIVENTIME) {
            // 弹出对话框,并且跳出循环,重置次数
            showSureDialog(UnBundlingActivity.this);// 展示对话框
            return;
        }
        sendMessage();
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
                params.put(StaticField.SMS_TYPE, StaticField.JB);
                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUser_id);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                Log.e("测试解绑短信", result);
                if (result.equals("-1")|result.equals("-2")) {// 服务器解析异常
                    handler.sendEmptyMessage(FAIL);
                    return;
                }
                // 解析返回的结果
                Gson gson = new Gson();
                BaseBean codeBean = gson.fromJson(result, BaseBean.class);
                if (codeBean.getRsp_code().equals("成功")) {// 获取验证码成功
                    handler.sendEmptyMessage(SUCCESS);
                    handler.sendEmptyMessageDelayed(CLOSEDIALOG, 2000);
                }
            }

        });
    }

    /**
     * 获取注册访问网络内容
     * @param PassWordLogin
     * @param CodeLogin
     */

    private void getLoginContext(final String PassWordLogin, final String CodeLogin) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.BIND);
                // 加密后的字节流
                byte[] PWByte = Des3Encode.encryptMode(PassWordLogin.getBytes());
                String PWBase64 = new String(Base64.encode(PWByte, Base64.DEFAULT));
                params.put(StaticField.REMIT_PWD, PWBase64);
                params.put(StaticField.OP_TYPE, StaticField.JB);
                params.put(StaticField.SMS_CODE, CodeLogin);
                params.put(StaticField.TOKEN, mToken);
                params.put(StaticField.USER_ID, mUser_id);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                Log.e("测试解绑", result);
                handler.sendEmptyMessage(DELPRESSAGE);
                if (result.equals("-1")|result.equals("-2")) {
                    handler.sendEmptyMessage(FAIL);
                    return;
                }
                Gson gson = new Gson();
                BaseBean registerBean = gson.fromJson(result, BaseBean.class);
                // 返回的获取信息
                String rspcode = registerBean.getRsp_code();
                String rspMsgContext = registerBean.getRsp_msg();
                if (rspcode.equals("0000")){
                    if (rspMsgContext.equals("成功")) {
                        handler.sendEmptyMessage(SHOWTEXTDIALOG);
                        handler.sendEmptyMessageDelayed(REGISTERSUCCESS, 2000);
                    } else if (rspMsgContext.equals("用户需要重新登录")) {
                        handler.sendEmptyMessage(SHOWAGAINDIALOG);
                        handler.sendEmptyMessageDelayed(SUCCESSAGAIN, 2000);
                    } else {
                        Log.e("ceshi", "是否执行");
                        String rspMsg = registerBean.getRsp_msg();
                        Message msg = handler.obtainMessage();
                        msg.what = FAILTEXTDIALOG;
                        msg.obj = rspMsg;
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }

    /**
     * 展示对话框
     */
    private void showSureDialog(Context context) {
        CountSureDialog countSure = new CountSureDialog(context, UNBUNDLING, pd);
        countSure.setmCount(this);
        countSure.show();
    }

    /**
     * 控件输入的变化监听事件
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
// 判断电话号码,验证码,密码的长度从而让注册按钮可以点击
        if (et_cash_pw.getText().toString().length() >= 6 && et_code.getText().toString().length() == 6) {
            btn_cimmit.setBackgroundResource(R.mipmap.login_press);
            btn_cimmit.setEnabled(true);
        } else {
            btn_cimmit.setBackgroundResource(R.mipmap.login_normal);
            btn_cimmit.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    @Override
    public void SendCode() {
        getNetData();
    }
}
