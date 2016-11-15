package cn.com.chinau.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.dialog.ChooseExpressCompanyPopupWindow;
import cn.com.chinau.dialog.PhoneDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * Created by Administrator on 2016/9/21.
 * 点击退款/退货(提交申请)页面
 */
public class ApplyForRefuseActivity extends BaseActivity implements View.OnClickListener {


    private View mRefuseTitle, mRefuseContent;
    private ImageView mBack, mNoRefuseImg, mYesRefuseImg;
    private TextView mTitle, mSumbit, mNoRefuse, mYesRefuse, mServiceTel;
    private LinearLayout mNoRefuseLl, mYesRefuseLl, mCompanyLl;
    private EditText mEtCourierNo, mEtRefuseExplain;// 快递单号，退款说明
    private ChooseExpressCompanyPopupWindow mChooseExpressCompanyPopup;// 选择快递公司弹出框
    private String mToken, mUser_id;// 标识，用户ID
    private SharedPreferences sp;
    private String mRefundType;
    private String mExpressComp;
    private String mCourieNO;
    private String mRefuseExplain;
    private String mOrder_sn, mGoods_sn;

    @Override
    public View CreateTitle() {
        mRefuseTitle = View.inflate(this, R.layout.base_back_title, null);
        return mRefuseTitle;
    }

    @Override
    public View CreateSuccess() {
        mRefuseContent = View.inflate(this, R.layout.activity_refuse_intervene, null);
        sp = getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
        initView();
        initListener();
        return mRefuseContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {

        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        // 获取退款适配器传过来的值
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        bundle.getString(StaticField.GOODS_SN, "");
        mOrder_sn = bundle.getString(StaticField.ORDER_SN, "");
        mGoods_sn = bundle.getString(StaticField.GOODS_SN, "");

        mBack = (ImageView) mRefuseTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mRefuseTitle.findViewById(R.id.base_title);
        mTitle.setText("退款/退货");
        mNoRefuseLl = (LinearLayout) mRefuseContent.findViewById(R.id.no_refuse_ll);
        mYesRefuseLl = (LinearLayout) mRefuseContent.findViewById(R.id.yes_refuse_ll);
        mCompanyLl = (LinearLayout) mRefuseContent.findViewById(R.id.express_company_ll);
        mEtCourierNo = (EditText) mRefuseContent.findViewById(R.id.et_courier_no);
        mEtRefuseExplain = (EditText) mRefuseContent.findViewById(R.id.ed_refuse_explain);
        mNoRefuseImg = (ImageView) mRefuseContent.findViewById(R.id.no_refuse_img);
        mYesRefuseImg = (ImageView) mRefuseContent.findViewById(R.id.yes_refuse_img);
        mSumbit = (TextView) mRefuseContent.findViewById(R.id.sumbit_apply_for);
        mNoRefuse = (TextView) mRefuseContent.findViewById(R.id.no_refuse);
        mYesRefuse = (TextView) mRefuseContent.findViewById(R.id.yes_refuse);
        mServiceTel = (TextView) mRefuseContent.findViewById(R.id.Service_tel);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mNoRefuseLl.setOnClickListener(this);
        mYesRefuseLl.setOnClickListener(this);
        mCompanyLl.setOnClickListener(this);
        mSumbit.setOnClickListener(this);
        mServiceTel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back:// 返回
                finishWitchAnimation();
                break;
            case R.id.no_refuse_ll:// 未收到货
                mNoRefuse.setTextColor(getResources().getColor(R.color.red_font));
                mYesRefuse.setTextColor(getResources().getColor(R.color.black));
                mNoRefuseImg.setVisibility(View.VISIBLE);
                mYesRefuseImg.setVisibility(View.GONE);
                mRefundType = StaticField.TK; // 退款类型
                break;
            case R.id.yes_refuse_ll:// 已收到货
                mNoRefuse.setTextColor(getResources().getColor(R.color.black));
                mYesRefuse.setTextColor(getResources().getColor(R.color.red_font));
                mNoRefuseImg.setVisibility(View.GONE);
                mYesRefuseImg.setVisibility(View.VISIBLE);
                mRefundType = StaticField.TH;// 退款类型
                break;
            case R.id.express_company_ll:// 选择快递公司
                mChooseExpressCompanyPopup = new ChooseExpressCompanyPopupWindow(this, mDistributionWindow);
                mChooseExpressCompanyPopup.showAtLocation(this.findViewById(R.id.refuse_intervene), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.sumbit_apply_for:// 提交申请
                mCourieNO = mEtCourierNo.getText().toString().trim();// 快递单号
                mRefuseExplain = mEtRefuseExplain.getText().toString().trim();// 退款说明
                if (TextUtils.isEmpty(mRefundType)) {
                    MyToast.showShort(this, "请输入退款类型");
                } else if (TextUtils.isEmpty(mRefuseExplain)) {
                    MyToast.showShort(this, "请选择退款说明");
                } else {
                    if (mRefundType.equals(StaticField.TK)) {
                        ApplyForRefuseIntent(StaticField.TK); //提交申请退款网络请求
                    } else {
                        if (TextUtils.isEmpty(mExpressComp)) {
                            MyToast.showShort(this, "请选择快递公司");
                        } else if (TextUtils.isEmpty(mCourieNO)) {
                            MyToast.showShort(this, "请填写快递单号");
                        } else {
                            ApplyForRefuseIntent(StaticField.TH); //提交申请退款网络请求
                        }
                    }
                }
                break;
            case R.id.Service_tel:// 客服电话
                PhoneDialog phoneDialog = new PhoneDialog(this, mServiceTel.getText().toString());
                phoneDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 配送方式的内部监听
     */
    private View.OnClickListener mDistributionWindow = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mChooseExpressCompanyPopup.dismiss();
            switch (view.getId()) {
                case R.id.EMS_click://Ems的点击按钮
                    mExpressComp = "ems";
                    break;
                case R.id.Wind_click://顺丰的点击按钮
                    mExpressComp = "sf";
                    break;
            }
        }
    };

    // 提交申请退款网络请求
    private void ApplyForRefuseIntent(final String refund_type) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ORDERREFUND);// 申请退款接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id); // 用户ID
                params.put(StaticField.REFUNDTYPE, refund_type); // 退款类型
                if (refund_type.equals(StaticField.TH)) {
                    params.put(StaticField.EXPRESS_COMP, mExpressComp); // 快递公司
                    params.put(StaticField.EXPRESS_NO, mCourieNO); // 快递单号
                }
                params.put(StaticField.ORDER_SN, mOrder_sn); // 订单编号
                params.put(StaticField.GOODS_SN, mGoods_sn); // 商品编号

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                params.put(StaticField.DETAIL, mRefuseExplain); // 退款说明
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("====提交申请", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }

        });

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 提交退款申请
                    Gson gson = new Gson();
                    BaseBean mBaseBean = gson.fromJson((String) msg.obj, BaseBean.class);
                    if (mBaseBean.getRsp_code().equals("0000")) {
                        Bundle bundle = new Bundle();
                        bundle.putString(StaticField.ORDER_SN,mOrder_sn);
                        bundle.putString(StaticField.GOODS_SN,mGoods_sn);
                        bundle.putString(StaticField.ORDERSTATUS,"TK");
                        //跳转至提交申请成页面（退款/退货）
                        openActivityWitchAnimation(SuccessApplyForRefuseActivity.class,bundle);
                    } else {
                        MyToast.showShort(ApplyForRefuseActivity.this, mBaseBean.getRsp_msg());
                    }
                    break;

            }

        }
    };
}
