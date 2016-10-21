package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.ChooseAddressListViewAdapter;
import cn.com.chinau.base.AddressBean;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * Created by Administrator on 2016/9/20.
 * 选择收货地址
 */
public class ChooseReceiverAddress extends BaseActivity implements View.OnClickListener {
    private View mAddressTitle, mAddressContent;
    private ImageView mBack;
    private TextView mTitle, mEdit;
    private ListView mAddressLV;
    private String mToken, mUser_id;
    private SharedPreferences sp;
    private ArrayList<AddressBean.Address> mList;
    @Override
    public View CreateTitle() {
        mAddressTitle = View.inflate(this, R.layout.base_font_title, null);
        return mAddressTitle;
    }

    @Override
    public View CreateSuccess() {
        mAddressContent = View.inflate(this, R.layout.activity_choose_address_listview, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initLintener();
        return mAddressContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
        // 获取本地保存的标识，用户ID
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        mBack = (ImageView) mAddressTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mAddressTitle.findViewById(R.id.base_title);
        mTitle.setText("选择收货地址");
        mEdit = (TextView) mAddressTitle.findViewById(R.id.base_edit);
        mEdit.setText("管理");
        mAddressLV = (ListView) mAddressContent.findViewById(R.id.Address_Lv);
    }

    private void initLintener() {
        mBack.setOnClickListener(this);
        mEdit.setOnClickListener(this);

    }

    private void initData() {
        GetInitNet(StaticField.LB); // 选择收货地址列表网络请求
    }

    private void initAdapter() {
        ChooseAddressListViewAdapter mChooseAddressAdapter = new ChooseAddressListViewAdapter(this, mList);
        mAddressLV.setAdapter(mChooseAddressAdapter);
        mChooseAddressAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back:
                finishWitchAnimation();
                break;
            case R.id.base_edit:
                openActivityWitchAnimation(ManagerAddressActivity.class);
                break;
            default:
                break;

        }
    }

    /**
     * 选择收货地址列表网络请求
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
                MyLog.LogShitou("管理收货地址List", result);

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
                case StaticField.SUCCESS:
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    AddressBean mAuctionBean = gson.fromJson(msge, AddressBean.class);
                    String mRsp_code = mAuctionBean.getRsp_code();

                    if (mRsp_code.equals("0000")) {
                        mList = mAuctionBean.getAddress_list();
                            //竖向ListView设置适配器
                            initAdapter();
                    }
                    break;

            }
        }
    };
}
