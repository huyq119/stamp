package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.ConfirmOrderListViewAdapter;
import cn.com.chinau.base.AddressBean;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.dialog.ProgressDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 管理收获地址页面
 */
public class ManagerAddressActivity extends BaseActivity implements View.OnClickListener {


    private View mManagerAddressContent;
    private View mManagerAddressTitle;
    private ListView mAddressLV;//收货地址的ListView
    private TextView mAddNewAddress;//添加新地址
    private ImageView mBack;
    private TextView mTitle;
    private String mAddress;
    private ProgressDialog prodialog;
    private TextView Title;
    private Button dialog_button_cancel;
    private Button dialog_button_ok;
    private ArrayList<AddressBean.Address> mList;
    private String mToken, mUser_id;
    private SharedPreferences sp;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case StaticField.SUCCESS:
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    AddressBean mAuctionBean = gson.fromJson(msge, AddressBean.class);
                    String mRsp_code = mAuctionBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mAuctionBean.getAddress_list();
                        if (mList != null && mList.size() != 0) {
                            //竖向ListView设置适配器
                            initAdapter();
                        }
                    }
                    break;

            }
        }
    };


    @Override
    public View CreateTitle() {
        mManagerAddressTitle = View.inflate(this, R.layout.base_back_title, null);
        return mManagerAddressTitle;
    }

    @Override
    public View CreateSuccess() {
        mManagerAddressContent = View.inflate(this, R.layout.activity_manageraddress, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mManagerAddressContent;
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.managerAddress_TV://添加收获地址
                Intent intent = new Intent(this, EditReceiptAddressActivity.class);
                intent.putExtra("Address", "mManagerAddress");
                startActivity(intent);
                //跳转动画
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        initData(); // 再出获取焦点重新请求
    }

    private void initView() {

        // 获取本地保存的标识，用户ID
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        mBack = (ImageView) mManagerAddressTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mManagerAddressTitle.findViewById(R.id.base_title);
        mTitle.setText("管理收货地址");

        mAddressLV = (ListView) mManagerAddressContent.findViewById(R.id.managerAddress_LV);
        mAddNewAddress = (TextView) mManagerAddressContent.findViewById(R.id.managerAddress_TV);
    }


    private void initData() {
        GetInitNet(StaticField.LB);// 管理收货地址列表网络请求

    }

    private void initAdapter() {
        ConfirmOrderListViewAdapter adapter = new ConfirmOrderListViewAdapter(this, mList);
        mAddressLV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mAddNewAddress.setOnClickListener(this);
    }

    /**
     * '
     * 管理收货地址列表网络请求
     *
     * @param op_type    操作类型：LB-地址列表：XQ-查询地址详情
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

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

               String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("管理收货地址List-->:", result);

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




}
