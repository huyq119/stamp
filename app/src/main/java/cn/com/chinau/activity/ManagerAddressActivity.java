package cn.com.chinau.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.adapter.ConfirmOrderListViewAdapter;
import cn.com.chinau.base.AddressBean;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.dialog.ProgressDialog;
import cn.com.chinau.utils.MyToast;

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

    @Override
    public View CreateTitle() {
        mManagerAddressTitle = View.inflate(this, R.layout.base_back_title, null);
        return mManagerAddressTitle;
    }

    @Override
    public View CreateSuccess() {
        mManagerAddressContent = View.inflate(this, R.layout.activity_manageraddress, null);
        initView();
        initData();
        initListener();
        return mManagerAddressContent;
    }


    private void initView() {

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

        ConfirmOrderListViewAdapter adapter = new ConfirmOrderListViewAdapter(this, mList, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.Address_select://选择
                        MyToast.showShort(ManagerAddressActivity.this, "点击了选择按钮");
                        break;
                    case R.id.Address_edit://编辑
                        openActivityWitchAnimation(EditReceiptAddressActivity.class);
//                        MyToast.showShort(ManagerAddressActivity.this, "点击了编辑按钮");
                        break;
                    case R.id.Address_delete://删除

                        DeleteDialog(); // 删除弹出框
//                        MyToast.showShort(ManagerAddressActivity.this, "点击了删除按钮");
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

    @Override
    public void AgainRequest() {

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
}
