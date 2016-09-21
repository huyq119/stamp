package com.example.stamp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stamp.R;
import com.example.stamp.adapter.ChooseAddressListViewAdapter;
import com.example.stamp.base.AddressBean;
import com.example.stamp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 * 选择收货地址
 */
public class ChooseReceiverAddress extends BaseActivity implements View.OnClickListener {
    private View mAddressTitle, mAddressContent;
    private ImageView mBack;
    private TextView mTitle, mEdit;
    private ListView mAddressLV;

    @Override
    public View CreateTitle() {
        mAddressTitle = View.inflate(this, R.layout.base_font_title, null);
        return mAddressTitle;
    }

    @Override
    public View CreateSuccess() {
        mAddressContent = View.inflate(this, R.layout.activity_choose_address_listview, null);
        initView();
        initData();
        initLintener();
        return mAddressContent;
    }

    @Override
    public void AgainRequest() {

    }

    private void initView() {
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
        List<AddressBean.Address> mList = new ArrayList<>();
        AddressBean.Address address;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                address = new AddressBean.Address("13803464265", "大橘子", "1", "北京市上地三街嘉园小区十单元202");
            } else {
                address = new AddressBean.Address("13333641265", "华莱士", "0", "北京市海淀区上地三街十大小区十单元201");
            }
            mList.add(address);
        }

        ChooseAddressListViewAdapter mChooseAddressAdapter = new ChooseAddressListViewAdapter(this, mList);
        mAddressLV.setAdapter(mChooseAddressAdapter);
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
}
