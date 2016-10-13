package cn.com.chinau.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.activity.EditReceiptAddressActivity;
import cn.com.chinau.base.AddressBean;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.dialog.ProgressDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 管理收货地址listView的适配器
 * Created by Administrator on 2016/8/19.
 */
public class ConfirmOrderListViewAdapter extends BaseAdapter {

    private Context context;
    private List<AddressBean.Address> mList;
    private ProgressDialog prodialog;
    private TextView Title;
    private Button dialog_button_cancel;
    private Button dialog_button_ok;
    private String mAddressId, mToken, mUser_id, mAddName, mAddMobile, mAddressDetail;
    private String is_default, name, mobile, Is_default, address_id, detail, prov, city, area, xgid;
    private boolean SelectFlag; // 点击是默认按钮还是删除按钮

    public ConfirmOrderListViewAdapter(Context context, List<AddressBean.Address> mList)  {
        this.context = context;
        this.mList = mList;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.address_listview_item, null);
            holder.Name = (TextView) view.findViewById(R.id.Address_Name);
            holder.Phone = (TextView) view.findViewById(R.id.Address_Phone);
            holder.Address = (TextView) view.findViewById(R.id.Address_address);
            holder.Status = (TextView) view.findViewById(R.id.Address_status);
            holder.Edit = (TextView) view.findViewById(R.id.Address_edit);
            holder.Delete = (TextView) view.findViewById(R.id.Address_delete);
//            holder.Select = (ImageView) view.findViewById(R.id.Address_select);
            holder.Select = (ToggleButton) view.findViewById(R.id.Address_select);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AddressBean.Address address = mList.get(i);
        mAddressId = address.getAddress_id();
        mAddName = address.getName();
        holder.Name.setText(mAddName);
        mAddMobile = address.getMobile();
        holder.Phone.setText(mAddMobile);
        mAddressDetail = address.getDetail();
        holder.Address.setText(mAddressDetail);

        holder.Select.setTag(i);
        holder.Edit.setTag(i);
        holder.Delete.setTag(i);

        is_default = address.getIs_default();
        //进来的时候判断默认地址的
        if (is_default.equals("0")) { //0:非默认；1默认
            holder.Status.setText("设为默认");
//            holder.Select.setImageResource(R.mipmap.circle_select);
            holder.Select.setBackgroundResource(R.mipmap.circle_select);
        } else {
            holder.Status.setText("默认地址");
//            holder.Select.setImageResource(R.mipmap.circle_select_click);
            holder.Select.setBackgroundResource(R.mipmap.circle_select_click);
        }

        // 核心方法，判断单选按钮被按下的位置与之前的位置是否相等，然后做相应的操作。
        if (selectID == i) {
            holder.Select.setChecked(true);
        } else {
            holder.Select.setChecked(false);
        }

        // 默认按钮
        holder.Select.setOnClickListener(new View.OnClickListener() {
            private int i;

            @Override
            public void onClick(View view) {
//                SelectFlag = true;

//                int tag = (int) view.getTag();
//                xgid = mList.get(tag).getAddress_id();
//                MyLog.LogShitou("点击默认获取的ID", xgid);
//                GetInitNet(StaticField.XQ); // 地址详情网络请求
                selectID =  i;
                if (mCheckChange != null)
                    mCheckChange.setSelectID(selectID);
            }
        });

        // 编辑
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                Intent intent = new Intent(context, EditReceiptAddressActivity.class);
                String id = mList.get(tag).getAddress_id();
                intent.putExtra("AddressId", id);
                MyLog.LogShitou("点击编辑传过去的值", id);
                intent.putExtra("Address", "mAddressAdapter");
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });

        // 删除
        holder.Delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SelectFlag = false;
                int tag = (int) view.getTag();
                xgid = mList.get(tag).getAddress_id();
                MyLog.LogShitou("点击删除获取的ID", xgid);
                DeleteDialog(); // 删除弹出框
            }
        });



        return view;
    }

    private int selectID;
    private OnMyCheckChangedListener mCheckChange;
    // 回调函数，很类似OnClickListener吧
    public void setOncheckChanged(OnMyCheckChangedListener l) {
        mCheckChange = l;
    }
    // 自定义的选中方法
    public void setSelectID(int i) {
        selectID = i;
    }
    // 回調接口
    public interface OnMyCheckChangedListener {
        void setSelectID(int selectID);
    }



    private class ViewHolder {
        public TextView Name, Phone, Address, Status, Edit, Delete;//名字,电话,地址,状态,编辑,删除
//        public ImageView Select;
        public ToggleButton Select;
    }

    /**
     * 删除弹出框
     */
    private void DeleteDialog() {
        prodialog = new ProgressDialog(context);
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

        // 确定
        dialog_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetInitNet(StaticField.XQ); // 地址详情网络请求
            }
        });
    }

    /**
     * 修改地址网络请求
     *
     * @param is_default
     * @param op_type
     */
    public void UpDatAddress(final int is_default, final String op_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                // 获取本地保存的标识，用户ID
                SharedPreferences sp = context.getSharedPreferences(StaticField.NAME, Context.MODE_PRIVATE);
                mToken = sp.getString("token", "");
                mUser_id = sp.getString("userId", "");
                params.put(StaticField.SERVICE_TYPE, StaticField.UPDATERESSMODIFY);// 接口名称
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.OP_TYPE, op_type);// 操作类型：SC：删除；XG：修改；XZ：新增
                params.put(StaticField.IS_DEFAULT, is_default + "");// 是否设置为默认地址：新增或修改时必传，0为非默认，1为默认
                params.put(StaticField.ADDRESS_ID, xgid);// 地址ID

                if (op_type.equals(StaticField.XG)) {
                    params.put(StaticField.MOBILE, mobile);// 收货人联系方式
                    params.put(StaticField.PROV, prov);// 省份
                    params.put(StaticField.CITY, city);// 市
                    params.put(StaticField.AREA, area);// 区/县
                }
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                if (op_type.equals(StaticField.XG)) {
                    params.put(StaticField.NAMES, name);// 收货人名称
                    params.put(StaticField.ADDRESS_DETAIL, detail);// 详细地址
                }

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result修改地址", result);
                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                if (op_type.equals(StaticField.XG)) {
                    Message msg = handler.obtainMessage();
                    msg.obj = result;
                    msg.what = StaticField.SUCCESS;//修改默认按钮
                    handler.sendMessage(msg);
                } else {
                    Message msg = handler.obtainMessage();
                    msg.obj = result;
                    msg.what = StaticField.DeleteSUCCESS; // 删除
                    handler.sendMessage(msg);
                }

            }
        });
    }


    /**
     * 地址详情网络请求
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
                params.put(StaticField.ADDRESS_ID, xgid);// 地址ID op_type为XQ时必传
                MyLog.LogShitou("详情请求的字段", op_type + "--" + xgid);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou("地址详情:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }

                if(SelectFlag){
                    Message msg = handler.obtainMessage();
                    msg.what = StaticField.SelectSUCCESS; //点击默认获取地址详情
                    msg.obj = result;
                    handler.sendMessage(msg);
                }else {
                    Message msg = handler.obtainMessage();
                    msg.what = StaticField.QUERYSUCCESS;  // 点击删除获取地址详情
                    msg.obj = result;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticField.SelectSUCCESS:// 点击默认获取地址详情
                    Gson gson = new Gson();
                    AddressBean mAuctionBean = gson.fromJson((String) msg.obj, AddressBean.class);
                    String mRsp_code = mAuctionBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mAuctionBean.getAddress_list();
                        name = mList.get(0).getName();
                        mobile = mList.get(0).getMobile();
                        address_id = mList.get(0).getAddress_id();
                        detail = mList.get(0).getDetail();
                        Is_default = mList.get(0).getIs_default();
                        prov = mList.get(0).getProv();
                        city = mList.get(0).getCity();
                        area = mList.get(0).getArea();

                        if (Is_default.equals("0")) { //0:非默认；1默认
                            UpDatAddress(1, StaticField.XG); //修改地址网络请求
                        } else {
                            UpDatAddress(0, StaticField.XG);//修改地址网络请求
                        }
                    }
                    break;
                case StaticField.QUERYSUCCESS:// 点击删除获取地址详情
                    Gson gsons = new Gson();
                    AddressBean mAuctionBeans = gsons.fromJson((String) msg.obj, AddressBean.class);
                    String mRsp_codes = mAuctionBeans.getRsp_code();
                    if (mRsp_codes.equals("0000")) {
                        mList = mAuctionBeans.getAddress_list();
                        name = mList.get(0).getName();
                        mobile = mList.get(0).getMobile();
                        address_id = mList.get(0).getAddress_id();
                        detail = mList.get(0).getDetail();
                        Is_default = mList.get(0).getIs_default();
                        prov = mList.get(0).getProv();
                        city = mList.get(0).getCity();
                        area = mList.get(0).getArea();

                        UpDatAddress(0, StaticField.SC);// 修改(删除)地址网络请求
                    }
                    break;
                case StaticField.SUCCESS:// 修改默认
                    Gson gsones = new Gson();
                    BaseBean mBaseBeans = gsones.fromJson((String) msg.obj, BaseBean.class);
                    String mCodes = mBaseBeans.getRsp_code();
                    if (mCodes.equals("0000")) {
                        if (Is_default.equals("0")) { //0:非默认；1默认
                            MyLog.LogShitou("默认按钮----->", "已设为默认");
//                            holder.Status.setText("设为默认");
//                            holder.Select.setImageResource(R.mipmap.circle_select);
                        } else {
                            MyLog.LogShitou("默认按钮----->", "已设为非默认");
//                            holder.Status.setText("默认地址");
//                            holder.Select.setImageResource(R.mipmap.circle_select_click);
                        }
                    }
                    break;
                case StaticField.DeleteSUCCESS:// 删除
                    Gson mGsons = new Gson();
                    BaseBean mBaseBean = mGsons.fromJson((String) msg.obj, BaseBean.class);
                    String mCode = mBaseBean.getRsp_code();
                    if (mCode.equals("0000")) {
                        prodialog.dismiss();// 删除成功关闭Dialog
                    }
                    break;
                default:
                    break;
            }

        }
    };


}
