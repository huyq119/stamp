package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.AddressBean;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.dialog.AddressPopupWindow;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 编辑收获地址
 */
public class EditReceiptAddressActivity extends BaseActivity implements View.OnClickListener {


    private View mEditReceiptAddressTitle;
    private View mEditReceiptAddressContent;
    private LinearLayout mEditReceiptAddress;//地址选择按钮
    private AddressPopupWindow mAddressPopupWindow;//地址选择弹出的PopupWindow
    private TextView mProvince, mCity, mArea; //省份,城市,区

    private TextView mEdit;//右上角的编辑
    private EditText mEtName, mEtMobile, mEtDetailAddress;
    private ImageView mBack;//返回按钮
    private int is_default = 0; //新增或修改时必传，0为非默认，1为默认
    private ToggleButton mToggleBtn; // 是否设置为默认地址
    private String mToken, mUser_id, mPro, mCitys, mAre, mName, mMobile, mDetailAddress;
    private String mProvinceCode, mCityCode, mAreaCode, mAddressId, mAddress, mIsDefault,
            prov, city, area;
    private SharedPreferences sp;
    private ArrayList<cn.com.chinau.base.AddressBean.Address> mList;
    private AddressBean mAddressBean;
    private String mAddress_id; // 详情请求下来的mAddress_id

    @Override
    public View CreateTitle() {
        mEditReceiptAddressTitle = View.inflate(this, R.layout.base_font_title, null);
        return mEditReceiptAddressTitle;
    }

    @Override
    public View CreateSuccess() {
        mEditReceiptAddressContent = View.inflate(this, R.layout.activity_editreceiptaddress, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        GetIntentSTring();
        initListener();
        return mEditReceiptAddressContent;
    }

    @Override
    public void AgainRequest() {

    }


    private void GetIntentSTring() {
        Intent intent = getIntent();
        mAddress = intent.getStringExtra("Address");
        if (mAddress.equals("mAddressAdapter")) {
            mAddressId = intent.getStringExtra("AddressId");
            mIsDefault = intent.getStringExtra("is_default");
            if (mIsDefault.equals("0")) { //0:非默认；1默认
                mToggleBtn.setBackgroundResource(R.mipmap.toggle_grey);
            } else {
                mToggleBtn.setBackgroundResource(R.mipmap.toggle_red);
            }

            GetInitNet(StaticField.XQ); // 查询地址详情请求
            MyLog.LogShitou("这是从编辑跳过来的", mAddressId);
        }


    }

    private void initView() {
        // 获取本地保存的标识，用户ID
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");


        TextView Title = (TextView) mEditReceiptAddressTitle.findViewById(R.id.base_title);
        Title.setText("编辑收货地址");
        mEdit = (TextView) mEditReceiptAddressTitle.findViewById(R.id.base_edit);
        mEdit.setText("保存");
        mBack = (ImageView) mEditReceiptAddressTitle.findViewById(R.id.base_title_back);
        mEditReceiptAddress = (LinearLayout) mEditReceiptAddressContent.findViewById(R.id.EditReceiptAddress_info);

        mProvince = (TextView) mEditReceiptAddressContent.findViewById(R.id.edit_Province);
        mCity = (TextView) mEditReceiptAddressContent.findViewById(R.id.edit_City);
        mArea = (TextView) mEditReceiptAddressContent.findViewById(R.id.edit_Area);

        mEtName = (EditText) mEditReceiptAddressContent.findViewById(R.id.et_address_name);// 收件人
        mEtMobile = (EditText) mEditReceiptAddressContent.findViewById(R.id.et_address_mobile);// 电话
        mEtDetailAddress = (EditText) mEditReceiptAddressContent.findViewById(R.id.et_address_detail);// 详细地址
        mToggleBtn = (ToggleButton) mEditReceiptAddressContent.findViewById(R.id.toggleBtn);// 是否默认按钮
    }

    /**
     * 获取输入的收件人名字，手机号，详细地址 ，省市区
     */
    private void GetEditTextString() {
        mPro = mProvince.getText().toString().trim();
        mCitys = mCity.getText().toString().trim();
        mAre = mArea.getText().toString().trim();
        MyLog.LogShitou("获取省市区名字-->:", mPro + "--" + mCitys + "-" + mAre);
        mName = mEtName.getText().toString().trim();
        mMobile = mEtMobile.getText().toString().trim();
        mDetailAddress = mEtDetailAddress.getText().toString().trim();
    }

    // 获取保存再本地的省市区
    private void initData() {
        String mAddress = sp.getString("Address", "");
        if (mAddress != null) {
            Gson gson = new Gson();
            mAddressBean = gson.fromJson(mAddress, AddressBean.class);
        }
    }


    private void initListener() {
        mEditReceiptAddress.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mBack.setOnClickListener(this);
        // 给是否默认设置值
        mToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    is_default = 1;
                    mToggleBtn.setBackgroundResource(R.mipmap.toggle_red);
                } else {
                    is_default = 0;
                    mToggleBtn.setBackgroundResource(R.mipmap.toggle_grey);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.EditReceiptAddress_info://编辑收获地址按钮
                if (mAddressBean != null) {
                    mAddressPopupWindow = new AddressPopupWindow(this, mAddressBean, AddressClick);
                    mAddressPopupWindow.showAtLocation(this.findViewById(R.id.EditReceiptAddress), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.base_edit://保存按钮
                GetEditTextString(); // 获取输入的收件人名字，手机号，详细地址
                if (TextUtils.isEmpty(mName)) {
                    MyToast.showShort(EditReceiptAddressActivity.this, "请填写收件人姓名");
                } else if (TextUtils.isEmpty(mMobile)) {
                    MyToast.showShort(EditReceiptAddressActivity.this, "请填写联系电话");
                } else if (TextUtils.isEmpty(mDetailAddress)) {
                    MyToast.showShort(EditReceiptAddressActivity.this, "请填写详细地址");
                }else {
                    if (mAddress.equals("mAddressAdapter")) {
                        MyLog.LogShitou("编辑地址选择了几-->：", is_default + "");
                        GetEditAddress(is_default, StaticField.XG); // 修改网络请求
                    } else {
                        MyLog.LogShitou("新增地址选择了几-->：", is_default + "");
                        GetEditAddress(is_default, StaticField.XZ); // 新增网络请求
                    }
                }
                break;
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
        }
    }

    /**
     * 地址选择的布局
     */
    private View.OnClickListener AddressClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.address_cancel://取消
                    mAddressPopupWindow.dismiss();
                    break;
                case R.id.address_finish://完成
                    String Province = mAddressPopupWindow.mCurrentProvince;
                    String City = mAddressPopupWindow.mCurrentCity;
                    String Area = mAddressPopupWindow.mCurrentArea;
                    mProvince.setText(Province);
                    mCity.setText(City);
                    mArea.setText(Area);
                    MyLog.LogShitou("省市区Name", Province + "-" + City + "--" + Area);
                    mProvinceCode = mAddressPopupWindow.mCurrentProvinceCode;// 省code
                    mCityCode = mAddressPopupWindow.mCurrentCityCode; // 市code
                    mAreaCode = mAddressPopupWindow.mCurrentAreaCode; // 区code
                    MyLog.LogShitou("省市区COde", mProvinceCode + "-" + mCityCode + "--" + mAreaCode);
                    mAddressPopupWindow.dismiss();
                    break;
            }
        }
    };


    /**
     * 新增地址网络请求
     *
     * @param is_default // 是否默认
     * @param op_type    // 操作类型
     */
    public void GetEditAddress(final int is_default, final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.UPDATERESSMODIFY);// 接口名称
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.IS_DEFAULT, is_default + "");// 是否设置为默认地址：新增或修改时必传，0为非默认，1为默认
                params.put(StaticField.OP_TYPE, op_type);// 操作类型：SC：删除；XG：修改；XZ：新增
                if(op_type.equals(StaticField.XG)){
                    params.put(StaticField.ADDRESS_ID, mAddress_id);// 地址ID
                }
                params.put(StaticField.MOBILE, mMobile);// 收货人联系方式

            // 判断是否选择了地址，如果没选择地址，就上传地址详情请求下来的地址code值，有选择地址，就上传选中地址的code值
                if (mProvinceCode !=null | mCityCode != null | mAreaCode != null){
                    params.put(StaticField.PROV, mProvinceCode);// 省份
                    params.put(StaticField.CITY, mCityCode);// 市
                    params.put(StaticField.AREA, mAreaCode);// 区县
                }else{
                    params.put(StaticField.PROV, prov);// 省份
                    params.put(StaticField.CITY, city);// 市
                    params.put(StaticField.AREA, area);// 区县
                }

                MyLog.LogShitou("上传的字段-->:", mUser_id + "--" + mToken + "--" + is_default + "--" + op_type + "--" +
                        mName + "--" + mMobile + "--" + mDetailAddress + "--" + mProvinceCode + "--" + mCityCode + "--" + mAreaCode);

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                // 收货人姓名，详细地址不需要签名
                params.put(StaticField.NAMES, mName);// 收货人名称
                params.put(StaticField.ADDRESS_DETAIL, mDetailAddress);// 详细地址
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("result修改地址-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.what = StaticField.ADDSUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);

            }
        });
    }

    /**
     * 查询地址详情请求
     *
     * @param op_type 操作类型
     */
    private void GetInitNet(final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ADDRESSLIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.OP_TYPE, op_type);// 操作类型：LB-地址列表：XQ-查询地址详情
                params.put(StaticField.ADDRESS_ID, mAddressId);// 地址ID

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("地址详情-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.what = StaticField.DETAILSUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.ADDSUCCESS:// 新增地址
                    Gson gsons = new Gson();
                    BaseBean mBaseBean = gsons.fromJson((String) msg.obj, BaseBean.class);
                    String mCoode = mBaseBean.getRsp_code();
                    if (mCoode.equals("0000")) {
                        finishWitchAnimation();
                    }
                    break;
                case StaticField.DETAILSUCCESS:// 地址详情
                    String msge = (String) msg.obj;
                    Gson gsones = new Gson();
                    cn.com.chinau.base.AddressBean mAuctionBean = gsones.fromJson(msge, cn.com.chinau.base.AddressBean.class);
                    String mRsp_code = mAuctionBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mAuctionBean.getAddress_list();
                        String mNames = mList.get(0).getName();
                        String mMobiles = mList.get(0).getMobile();
                        String mDetails = mList.get(0).getDetail();
                         mAddress_id = mList.get(0).getAddress_id();
                        String[] address = mDetails.split(" ");
                        String address1 = address[0];
                        String address2 = address[1];
                        String address3 = address[2];
                        String address4 = address[3];

                        MyLog.LogShitou("省-市-区", address1 + "-" + address2 + "-" + address3 + "-" + address4);
                        mEtName.setText(mNames);
                        mEtMobile.setText(mMobiles);
                        mProvince.setText(address1);
                        mCity.setText(address2);
                        mArea.setText(address3);
                        mEtDetailAddress.setText(address4);

                        prov = mList.get(0).getProv();
                        city = mList.get(0).getCity();
                        area = mList.get(0).getArea();

                    }
                    break;
            }
        }
    };
}
