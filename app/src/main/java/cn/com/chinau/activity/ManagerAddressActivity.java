package cn.com.chinau.activity;

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
import cn.com.chinau.utils.MyToast;
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
                        MyLog.LogShitou("管理收货地址有几条-->:", mList.size() + "");
                        if (mList != null && mList.size() != 0) {
                            //竖向ListView设置适配器
                            initAdapter();
                        } else {
                            MyToast.showShort(ManagerAddressActivity.this, "还未有管理收货地址。。。");
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
        initAdapter();
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
                openActivityWitchAnimation(EditReceiptAddressActivity.class);
                break;
        }
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
        mList = new ArrayList<>();
        AddressBean.Address address;
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                address = new AddressBean.Address("13803641265", "大橘子", "1", "北京市海淀区");
            } else {
                address = new AddressBean.Address("13803641265", "大橙子", "0", "北京市海淀区");
            }
            mList.add(address);

        }

        GetInitNet(StaticField.LB, null);// 管理收货地址列表网络请求
    }

    private void initAdapter() {
        ConfirmOrderListViewAdapter adapter = new ConfirmOrderListViewAdapter(this, mList, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.Address_select://选择
                        MyToast.showShort(ManagerAddressActivity.this, "点击了选择按钮");
                        break;
                    case R.id.Address_edit://编辑
                        openActivityWitchAnimation(EditReceiptAddressActivity.class);
                        break;
                    case R.id.Address_delete://删除
                        DeleteDialog(); // 删除弹出框
                        break;

                }
            }
        });

        mAddressLV.setAdapter(adapter);

    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mAddNewAddress.setOnClickListener(this);
    }


    /**
     * 删除弹出框
     */
    private void DeleteDialog() {
        prodialog = new ProgressDialog(this);
        prodialog.show();
        Title = (TextView) prodialog.findViewById(R.id.title_tv);
        Title.setText("确定要删除吗？");
        // 取消
        dialog_button_cancel = (Button) prodialog
                .findViewById(R.id.dialog_button_cancel);
        // 确定
        dialog_button_ok = (Button) prodialog
                .findViewById(R.id.dialog_button_ok);// 取消
        // 取消
        dialog_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodialog.dismiss();
            }
        });
        dialog_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<CollectionBean.Collection> deleteList = new ArrayList<CollectionBean.Collection>();
//                for (int i = 0; i < mList.size(); i++){
//                    AddressBean.Address group = mList.get(i);
//                    if (group.isChoosed()){
//                        deleteList.add(group);
//                    }
//                    mList.removeAll(deleteList);
//                    mListAdapter.notifyDataSetChanged();
//                }
//                MyToast.showShort(ManagerAddressActivity.this,"删除成功");
                prodialog.dismiss();// 关闭Dialog
            }
        });
    }

    /**
     * '
     * 管理收货地址列表网络请求
     *
     * @param op_type    操作类型：LB-地址列表：XQ-查询地址详情
     * @param address_id 地址ID
     */
    private void GetInitNet(final String op_type, final String address_id) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ADDRESSLIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.OP_TYPE, op_type);// 操作类型：LB-地址列表：XQ-查询地址详情
                if (op_type.equals("XQ")) {
                    params.put(StaticField.ADDRESS_ID, address_id);// 地址ID，op_type为 XQ时，必传
                }

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

               String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("管理收货地址List-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
//                Message msg = mHandler.obtainMessage();
//                msg.what = StaticField.SUCCESS;
//                msg.obj = result;
//                mHandler.sendMessage(msg);

            }
        });
    }

    /**
     * 修改地址网络请求
     * @param address_id 地址ID
     * @param is_default 是否默认
     * @param op_type 操作类型
     */
    private void UpDateGetInitNet( final String address_id,final int is_default,final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ADDRESSLIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.ADDRESS_ID, address_id);// 地址ID
                // 是否设置为默认地址：新增或修改时必传，0为非默认，1为默认
                params.put(StaticField.IS_DEFAULT, String.valueOf(is_default));
                params.put(StaticField.OP_TYPE, op_type);// 操作类型：SC：删除；XG：修改；XZ：新增
                params.put(StaticField.NAMES, "");// 收货人名称：删除时不必传
                params.put(StaticField.MOBILE, "");// 收货人联系方式：删除时不必传
                params.put(StaticField.ADDRESS_DETAIL, "");// 详细地址：删除时不必传
                params.put(StaticField.PROV, "");// 所属省份：删除时不必传
                params.put(StaticField.CITY, "");// 所属市：删除时不必传
                params.put(StaticField.AREA, "");// 所属区县：删除时不必传


                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result修改地址-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
//                Message msg = mHandler.obtainMessage();
//                msg.what = StaticField.SUCCESS;
//                msg.obj = result;
//                mHandler.sendMessage(msg);

            }
        });
    }


}
