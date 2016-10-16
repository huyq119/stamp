package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.ExpressListViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.Logistics;
import cn.com.chinau.dialog.SussessDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 快递信息页面
 */
public class FastMailInfoActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    protected static final int SUCCESS = 0;// 成功
    protected static final int FAIL = 1;// 失败
    protected static final int DELPROGRESS = 2;// 关闭对话框
    protected static final int DELSUSSESS = 3;// 关闭成功对话框
    protected static final int NOFAIL = 4;// 没有网络
    protected static final int DELPRESSAGE = 5;// 关闭进度对话框

    private View mFastMailInfoTitle;
    private View mFastMailInfoContent;
    private Button mBtnSubmit;//提交按钮
    private ImageView mBack;
    private TextView mTitle;
    private ArrayList<Logistics> mList;// 快递信息数组
    private View mfast_mailView;// 快递的弹出框
    private ListView mPList;// popup里面的listview
    private TextView tv_send_address, tv_person, tv_send_phone, et_back_buy_phone, mKuaidi;
    private SharedPreferences sp;
    private int index = 0;// 快递的角标
    private String mFast_mail = "";// 用于接收快递信息的字符串
    private PopupWindow mPopup;
    int colors=00000;
    private ColorDrawable dw = new ColorDrawable(colors);// popup的背景
//    private ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.express_pop));// popup的背景
    private String phone, order_sn;
    private SussessDialog dialog;

    private MyHandler handler = new MyHandler(this) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:// 成功
                    // 跳转
//                    startActivity(new Intent(FastMailInfoActivity.this, FastMailSubmitActivity.class));

                    String Result = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(Result);
                        String order_sn = json.getString("order_sn");
                        sp.edit().putString("order_sn",order_sn).commit();
                        openActivityWitchAnimation(FastMailSubmitActivity.class);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case FAIL:// 失败
                    dialog = new SussessDialog(FastMailInfoActivity.this);
                  String str = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(str);
                        String rsg_msg = json.getString("rsg_msg");
//                        String order_sn = json.getString("order_sn");
                        dialog.setText(rsg_msg);
                        dialog.show();
                        handler.sendEmptyMessageDelayed(DELSUSSESS, 2000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case DELPROGRESS:// 关闭对话框
//                    if ( pd != null)
//                        pd.dismiss();
                    break;
                case DELSUSSESS:// 网络不好关闭成功对话框
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
//                case DELPRESSAGE :
//                    if (pd != null)
//                        pd.dismiss();
//                    break;
                case NOFAIL:// 没有网络
                    dialog = new SussessDialog(FastMailInfoActivity.this);
                    dialog.setText("请检查您的网络");
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELSUSSESS, 2000);
                    break;
                default:
                    break;
            }
        }
    };
    private String mDetail;


    @Override
    public View CreateTitle() {
        mFastMailInfoTitle = View.inflate(this, R.layout.base_back_title, null);
        return mFastMailInfoTitle;
    }

    @Override
    public View CreateSuccess() {
        mFastMailInfoContent = View.inflate(this, R.layout.activity_fastmailinfo, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mFastMailInfoContent;
    }

    @Override
    public void AgainRequest() {
        initData();
    }

    private void initView() {

        Bundle bundle = getIntent().getExtras();
        mDetail = bundle.getString("ScanOrderBuyDetail", "");

        mBack = (ImageView) mFastMailInfoTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mFastMailInfoTitle.findViewById(R.id.base_title);
        mTitle.setText("快递信息");

        tv_send_address = (TextView) mFastMailInfoContent.findViewById(R.id.tv_send_address);//寄送地址
        tv_person = (TextView) mFastMailInfoContent.findViewById(R.id.tv_person);// 收件人
        tv_send_phone = (TextView) mFastMailInfoContent.findViewById(R.id.tv_send_phone); // 电话
        et_back_buy_phone = (EditText) mFastMailInfoContent.findViewById(R.id.et_back_buy_phone);// 快递单号
        mKuaidi = (TextView) mFastMailInfoContent.findViewById(R.id.express_kuaidi);// 快递公司
        mBtnSubmit = (Button) mFastMailInfoContent.findViewById(R.id.FastMail_Submit);// 提交


    }

    private void initListener() {

        mBack.setOnClickListener(this);
        mKuaidi.setOnClickListener(this);
        et_back_buy_phone.addTextChangedListener(this);
        mBtnSubmit.setOnClickListener(this);

        mPList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                mFast_mail = mList.get(position).getName();
                mKuaidi.setText(mFast_mail);
                if (et_back_buy_phone.getText().length() >= 10 && !TextUtils.isEmpty(mKuaidi.getText()) && !mKuaidi.getText().equals("请选择")) {
                    mBtnSubmit.setBackgroundResource(R.drawable.btn_backbuy_bg);
                    mBtnSubmit.setEnabled(true);
                } else {
                    mBtnSubmit.setBackgroundResource(R.mipmap.btn_normal);
                    mBtnSubmit.setEnabled(false);
                }
                mPopup.dismiss();
            }
        });
        // 设置输入限制
        et_back_buy_phone.setKeyListener(new NumberKeyListener() {

            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return numberChars;
            }
        });
    }

    // 填充数据
    public void initData() {
        getIntentData();
        mList = getFastMailData();
        if (mfast_mailView == null) {
            mfast_mailView = View.inflate(this, R.layout.popup_view, null);
        }
        mPList = (ListView) mfast_mailView.findViewById(R.id.pop_lv);
        if (mList.size() != 0) {
            ExpressListViewAdapter adapter = new ExpressListViewAdapter(mList, this);
            mPList.setAdapter(adapter);
        }
    }


    /**
     * 上个页面传过来的值
     */
    private void getIntentData() {
//        String result = getIntent().getExtras().getString("Result");// json串
//        phone = getIntent().getExtras().getString("phone");// 手机号
//        order_sn = getIntent().getExtras().getString("order_sn"); // 回购订单编号

        phone = sp.getString("Phone","");
        order_sn = sp.getString("order_sn","");// 回购订单编号
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                if(mDetail.equals("ScanOrderBuyDetail")){
                    finishWitchAnimation();
                }else{
                    openActivityWitchAnimation(AffirmBuyBackActivity.class);
                    finish();
                }
                break;
            case R.id.express_kuaidi://选择快递公司
                if (mPopup == null) {
                    mPopup = new PopupWindow(mfast_mailView, mKuaidi.getWidth(), 220, true);
                }
                // 为了响应返回键和界面外的其他界面
                mPopup.setBackgroundDrawable(dw);
                mPopup.showAsDropDown(mKuaidi, 0, 0);

                break;
            case R.id.FastMail_Submit://提交按钮
                String express = et_back_buy_phone.getText().toString();
                Log.e("快递单号-->:", express);
//                pd = new ProgressDialog(this);
//                pd.show();
                buyback(express);
                // openActivityWitchAnimation(FastMailSubmitActivity.class);
                break;
            default:
                break;
        }
    }


    /**
     * 获取快递集合
     */
    private ArrayList<Logistics> getFastMailData() {
        String result = sp.getString("System", null);
        MyLog.LogShitou("获取保存本地的系统数据-->：", result);

        if (result != null) {
            try {
                ArrayList<Logistics> mList = new ArrayList<Logistics>();
                JSONObject jsonresult = new JSONObject(result);
                String string = jsonresult.getString("sys_param_value");
                JSONObject json = new JSONObject(string);
                String mExpress_comp = json.getString("express_comp");
                JSONObject jsonby = new JSONObject(mExpress_comp);// 获取快递公司
                MyLog.LogShitou("jsonby测试-->:", jsonby.toString());
                Iterator<String> keys = jsonby.keys();
                String key;
                Object value;
                while (keys.hasNext()) {
                    key = keys.next();
                    value = jsonby.get(key);
                    mList.add(new Logistics(key, (String) value));
                }

                return mList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 提交按钮的网络请求
     *
     * @param express 单号
     */
    private void buyback(final String express) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.CONFIRM);// 接口名称
                params.put(StaticField.EXPRESS_COMP, mList.get(index).getAbb());// 快递公司
                Log.e("选择的快递----->", mList.get(index).getAbb());
                params.put(StaticField.EXPRESS_NO, express);// 快递单号
                // params.put(Constants.GOODS_SN, goods_sn);
                params.put(StaticField.MOBILE, phone);// 手机号
                params.put(StaticField.OP_TYPE, StaticField.WL);// 操作类型
                // params.put(Constants.ORDER_DETAIL_SN, order_detail_sn);
                params.put(StaticField.ORDER_SN, order_sn);// 回购订单编号

                String token = sp.getString("token", "");
                String userId = sp.getString("userId", "");

                params.put(StaticField.TOKEN, token);
                params.put(StaticField.USER_ID, userId);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String mResult = HttpUtils.submitPostData(StaticField.ROOT, params);
//                handler.sendEmptyMessage(DELPRESSAGE);

                MyLog.LogShitou("快递信息-->", mResult);

                if (mResult.equals("-1")) {
                    handler.sendEmptyMessage(NOFAIL);
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
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = FAIL;
                    msg.obj = mResult;
                    handler.sendMessage(msg);
                }
            }
        });
    }


    /**
     * 输入
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

    }

    // 改变选定快递公司后提交按钮的状态
    @Override
    public void afterTextChanged(Editable editable) {
        if (et_back_buy_phone.getText().length() >= 10 && !TextUtils.isEmpty(mKuaidi.getText()) && !mKuaidi.getText().equals("请选择")) {
            mBtnSubmit.setBackgroundResource(R.drawable.btn_backbuy_bg);
            mBtnSubmit.setEnabled(true);
        } else {
            mBtnSubmit.setBackgroundResource(R.mipmap.btn_normal);
            mBtnSubmit.setEnabled(false);
        }
    }
}
