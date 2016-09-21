package com.example.stamp.activity;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.base.BaseActivity;
import com.example.stamp.bean.AddressBean;
import com.example.stamp.dialog.AddressPopupWindow;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.MyToast;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * 编辑收获地址
 */
public class EditReceiptAddressActivity extends BaseActivity implements View.OnClickListener {


    private View mEditReceiptAddressTitle;
    private View mEditReceiptAddressContent;
    private LinearLayout mEditReceiptAddress;//地址选择按钮
    private AddressPopupWindow mAddressPopupWindow;//地址选择弹出的PopupWindow
    private TextView mProvince,mCity,mArea;//省份,城市,区
    private TextView mEdit;//右上角的编辑
    private ImageView mBack;//返回按钮
    private AddressBean mAddressBean;//地址的实体类

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SUCCESS:
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    mAddressBean = gson.fromJson(result, AddressBean.class);
                    initListener();
                    break;
            }
        }
    };



    @Override
    public View CreateTitle() {
        mEditReceiptAddressTitle = View.inflate(this, R.layout.base_font_title, null);
        return mEditReceiptAddressTitle;
    }

    @Override
    public View CreateSuccess() {
        mEditReceiptAddressContent = View.inflate(this, R.layout.activity_editreceiptaddress, null);
        initView();
        initData();
        return mEditReceiptAddressContent;
    }

    private void initView() {
        TextView Title =  (TextView) mEditReceiptAddressTitle.findViewById(R.id.base_title);
        Title.setText("编辑收货地址");
        mEdit = (TextView) mEditReceiptAddressTitle.findViewById(R.id.base_edit);
        mEdit.setText("保存");
        mBack = (ImageView) mEditReceiptAddressTitle.findViewById(R.id.base_title_back);
        mEditReceiptAddress = (LinearLayout) mEditReceiptAddressContent.findViewById(R.id.EditReceiptAddress_info);
        mProvince = (TextView) mEditReceiptAddressContent.findViewById(R.id.edit_Province);
        mCity = (TextView) mEditReceiptAddressContent.findViewById(R.id.edit_City);
        mArea = (TextView) mEditReceiptAddressContent.findViewById(R.id.edit_Area);
    }


    private void initData() {
        //请求省市县三级数据
        getNetAddress();
    }

    @Override
    public void AgainRequest() {

    }

    /**
     * 获取网络地址信息数据
     */
    public void getNetAddress() {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.NATION);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                if (result.equals("-1")) {
                    return;
                }

                MyLog.e(result);

                //发送请求
                Message msg = handler.obtainMessage();
                msg.obj = result;
                msg.what = StaticField.SUCCESS;
                handler.sendMessage(msg);
            }
        });
    }


    private void initListener() {
        mEditReceiptAddress.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mBack.setOnClickListener(this);
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
                MyToast.showShort(this,"保存成功");
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
                    mProvince.setText(mAddressPopupWindow.mCurrentProvince);
                    mCity.setText(mAddressPopupWindow.mCurrentCity);
                    mArea.setText(mAddressPopupWindow.mCurrentArea);
                    mAddressPopupWindow.dismiss();
                    break;
            }
        }
    };
}
