package cn.com.chinau.activity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.chinau.R;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.dialog.ChooseExpressCompanyPopupWindow;
import cn.com.chinau.dialog.PhoneDialog;
import cn.com.chinau.utils.MyToast;

/**
 * Created by Administrator on 2016/9/21.
 * (提交申请)退款/退货页面
 */
public class ApplyForRefuseActivity extends BaseActivity implements View.OnClickListener{


    private View mRefuseTitle, mRefuseContent;
    private ImageView mBack,mNoRefuseImg,mYesRefuseImg;
    private TextView mTitle,mSumbit,mNoRefuse,mYesRefuse,mServiceTel;
    private LinearLayout mNoRefuseLl,mYesRefuseLl,mCompanyLl;
    private EditText mEtCourierNo,mEtRefuseExplain;// 快递单号，退款说明
    private ChooseExpressCompanyPopupWindow mChooseExpressCompanyPopup;// 选择快递公司弹出框

    @Override
    public View CreateTitle() {
        mRefuseTitle = View.inflate(this, R.layout.base_back_title, null);
        return mRefuseTitle;
    }

    @Override
    public View CreateSuccess() {
        mRefuseContent = View.inflate(this, R.layout.activity_refuse_intervene, null);
        initView();
        initListener();
        return mRefuseContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
       mBack =(ImageView) mRefuseTitle.findViewById(R.id.base_title_back);
        mTitle =(TextView) mRefuseTitle.findViewById(R.id.base_title);
        mTitle.setText("退款/退货");
        mNoRefuseLl =(LinearLayout) mRefuseContent.findViewById(R.id.no_refuse_ll);
        mYesRefuseLl=(LinearLayout) mRefuseContent.findViewById(R.id.yes_refuse_ll);
        mCompanyLl=(LinearLayout) mRefuseContent.findViewById(R.id.express_company_ll);
        mEtCourierNo =(EditText) mRefuseContent.findViewById(R.id.et_courier_no);
        mEtRefuseExplain =(EditText) mRefuseContent.findViewById(R.id.ed_refuse_explain);
        mNoRefuseImg =(ImageView) mRefuseContent.findViewById(R.id.no_refuse_img);
        mYesRefuseImg =(ImageView) mRefuseContent.findViewById(R.id.yes_refuse_img);
        mSumbit =(TextView) mRefuseContent.findViewById(R.id.sumbit_apply_for);
        mNoRefuse =(TextView) mRefuseContent.findViewById(R.id.no_refuse);
        mYesRefuse =(TextView) mRefuseContent.findViewById(R.id.yes_refuse);
        mServiceTel =(TextView) mRefuseContent.findViewById(R.id.Service_tel);
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
        switch (view.getId()){
            case R.id.base_title_back:// 返回
                finishWitchAnimation();
            break;
            case R.id.no_refuse_ll:// 未收到货
                mNoRefuse.setTextColor(getResources().getColor(R.color.red_font));
                mYesRefuse.setTextColor(getResources().getColor(R.color.black));
                mNoRefuseImg.setVisibility(View.VISIBLE);
                mYesRefuseImg.setVisibility(View.GONE);
            break;
            case R.id.yes_refuse_ll:// 已收到货
                mNoRefuse.setTextColor(getResources().getColor(R.color.black));
                mYesRefuse.setTextColor(getResources().getColor(R.color.red_font));
                mNoRefuseImg.setVisibility(View.GONE);
                mYesRefuseImg.setVisibility(View.VISIBLE);
            break;
            case R.id.express_company_ll:// 选择快递公司
                mChooseExpressCompanyPopup = new ChooseExpressCompanyPopupWindow(this, mDistributionWindow);
                mChooseExpressCompanyPopup.showAtLocation(this.findViewById(R.id.refuse_intervene), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            break;
            case R.id.sumbit_apply_for:// 提交申请
                String mCourie = mEtCourierNo.getText().toString().trim();
                String mRefuseExplain= mEtRefuseExplain.getText().toString().trim();
                if (TextUtils.isEmpty(mCourie)){
                    MyToast.showShort(this,"请填写快递单号");
                }else if (TextUtils.isEmpty(mRefuseExplain)){
                    MyToast.showShort(this,"请输入退款说明");
                }else {
                    //跳转至提交申请成页面（退款/退货）
                    openActivityWitchAnimation(SuccessApplyForRefuseActivity.class);
                }
            break;
            case R.id.Service_tel:// 客服电话
                PhoneDialog phoneDialog = new PhoneDialog(this,mServiceTel.getText().toString());
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
                    MyToast.showShort(ApplyForRefuseActivity.this, "选择了EMS");


                    break;
                case R.id.Wind_click://顺丰的点击按钮
                    MyToast.showShort(ApplyForRefuseActivity.this, "选择了顺丰速递");
                    break;
            }
        }
    };
}
