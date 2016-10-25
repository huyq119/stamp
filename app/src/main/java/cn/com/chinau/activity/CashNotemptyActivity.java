package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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
import cn.com.chinau.dialog.SendProgressDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * Date: 2016/10/25 11:21
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 总的解绑或修改银行
 */

public class CashNotemptyActivity extends BaseActivity implements View.OnClickListener {
    protected static final int DELPRESSAGE = 0;// 关闭对话框
    protected static final int SUCCESS = 1;// 成功
    protected static final int CARSUCCESS = 2;// 获取银行卡名称成功
    private TextView tv_jiebang, tv_update, tv_name, tv_carNum;// 解绑，修改,名称,银行卡后四位
    private ImageView mIcon;// 银行卡图标
    private int flag = 0;
    private SendProgressDialog pd;
    private String mToken;
    private String mUser_id;
    private String bank_abbr;// 银行卡简称
    private String card_no;// 银行卡号
    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELPRESSAGE :
                    if (pd != null) {
                        pd.dismiss();
                    }
                    break;
                case SUCCESS :// 成功
                    String result = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(result);
                        bank_abbr = json.getString("bank_abbr");
                        Log.e("bank_abbr", bank_abbr);
                        sp.edit().putString("bankADD", bank_abbr).commit();
                        card_no = json.getString("card_no");
                        sp.edit().putString("bankNum", card_no).commit();
                        sp.edit().putString("bankPerson", json.getString("name")).commit();

                        getCarBinData(card_no);// 获取银行卡名称

                    } catch (JSONException e) {
//						String bank_abbr = sp.getString("bankADD", "");
//						Log.e("bank_abbr", bank_abbr);
//						String card_no = sp.getString("bankNum", "");
//						String bank_name = sp.getString("bankName", "");
//						if (!TextUtils.isEmpty(bank_name) && !TextUtils.isEmpty(bank_abbr) && !TextUtils.isEmpty(card_no)) {
//							setCarIcon(bank_abbr, bank_name);
//							tv_carNum.setText("尾号  " + card_no.substring(card_no.length() - 4, card_no.length()));
//						}
                        e.printStackTrace();
                    }
                    break;

                case CARSUCCESS :
                    String resultNum = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(resultNum);
                        String bank_name = json.getString("bank_name");
                        sp.edit().putString("bankName", bank_name).commit();
                        if (!TextUtils.isEmpty(bank_name) && !TextUtils.isEmpty(bank_abbr) && !TextUtils.isEmpty(card_no)) {
                            setCarIcon(bank_abbr, bank_name);
                            tv_carNum.setText("尾号  " + card_no.substring(card_no.length() - 4, card_no.length()));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default :
                    break;
            }

        }
    };
    private View mCashNotemptyTitle;
    private ImageView mBack;
    private SharedPreferences sp;
    private View cashNotempty;
    private BitmapUtils bitmapUtils;

    @Override
    public View CreateTitle() {
        mCashNotemptyTitle = View.inflate(this, R.layout.base_back_title, null);
        return mCashNotemptyTitle;
    }

    @Override
    public View CreateSuccess() {
        cashNotempty = View.inflate(this, R.layout.activity_cash_card_notempty, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_APPEND);
        bitmapUtils = BitmapHelper.getBitmapUtils();
        initView();
        initData();
        initListener();
        return cashNotempty;
    }

    @Override
    public void AgainRequest() {
//        if (pd != null)
//            pd.dismiss();
        getNetData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_back :// 返回
                if (flag == 1) {
                    startActivity(new Intent(CashNotemptyActivity.this, WithdrawActivity.class));
                }
                finish();

                break;
            case R.id.tv_update :// 修改
                Bundle bundle = new Bundle();
                bundle.putString("CashEmpty","CashNotempty");
                openActivityWitchAnimation(ReviseCardActivity.class,bundle);
                break;
            case R.id.tv_jiebang :// 解除绑定
                openActivityWitchAnimation(UnBundlingActivity.class);
                break;
            default :
                break;
        }
    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");
        TextView mTitle = (TextView) mCashNotemptyTitle.findViewById(R.id.base_title);
        mTitle.setText("银行卡");
        mBack = (ImageView) mCashNotemptyTitle.findViewById(R.id.base_title_back);

        tv_jiebang = (TextView) cashNotempty.findViewById(R.id.tv_jiebang);
        tv_update = (TextView) cashNotempty.findViewById(R.id.tv_update);
        mIcon = (ImageView) cashNotempty.findViewById(R.id.ib_cash);
        tv_name = (TextView) cashNotempty.findViewById(R.id.tv_name);
        tv_carNum = (TextView) cashNotempty.findViewById(R.id.tv_cadnum);

    }
    private void initListener() {
        mBack.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        tv_jiebang.setOnClickListener(this);
    }

    /**
     * 添加数据
     */
    private void initData() {
        getIntentData();
        getNetData();// 请求网络
    }

    /**
     * 绑定银行卡请求网络的接口
     */
    private void getNetData() {
//        pd = new ProgressDialog(this);
//        pd.show();
        mUser_id = sp.getString("userId", null);
        mToken = sp.getString("token", null);
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {

            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.BIND);
                params.put(StaticField.OP_TYPE, StaticField.CX);
                params.put(StaticField.USER_ID, mUser_id);
                params.put(StaticField.TOKEN, mToken);

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.NAME, params);
                Log.e("result", result);

                if (result.equals("-1")|result.equals("-1")) {
                    return;
                }
                Gson gson = new Gson();
                BaseBean codeBean = gson.fromJson(result, BaseBean.class);
                String rsp_code = codeBean.getRsp_code();// 服务器返回的结果
                if (rsp_code.equals("0000")) {
                    Message msg = handler.obtainMessage();
                    msg.obj = result;
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            }
        });
    }
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String upDate = intent.getStringExtra("upDate");
            if (upDate != null) {
                if (upDate.equals("updata")) {
                    flag = 1;// 说明是从设置页面来的
                }
            }
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
                String result = HttpUtils.submitPostData(StaticField.NAME, params);
                handler.sendEmptyMessage(DELPRESSAGE);
                if(result.equals("-1")){
                    return;
                }
                MyLog.LogShitou("银行卡bin接口", result);

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
        String SystemData = sp.getString("System", "");// 获取系统信息
        if (!TextUtils.isEmpty(SystemData)) {
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
                tv_name.setText(bank_name);
                Log.e("weishenme--------->", tv_name.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
