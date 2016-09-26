package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.base.AddressBean;

/**
 * 确认订单的listView的适配器
 * Created by Administrator on 2016/8/19.
 */
public class ConfirmOrderListViewAdapter extends BaseAdapter {

    private Context context;
    private List<AddressBean.Address> mList;
    private View.OnClickListener mOnClickListener;

    public ConfirmOrderListViewAdapter(Context context, List<AddressBean.Address> mList, View.OnClickListener onClickListener) {
        this.context = context;
        this.mList = mList;
        mOnClickListener = onClickListener;
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
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.address_listview_item, null);
            holder.Name = (TextView) view.findViewById(R.id.Address_Name);
            holder.Phone = (TextView) view.findViewById(R.id.Address_Phone);
            holder.Address = (TextView) view.findViewById(R.id.Address_address);
            holder.Status = (TextView) view.findViewById(R.id.Address_status);
            holder.Edit = (TextView) view.findViewById(R.id.Address_edit);
            holder.Delete = (TextView) view.findViewById(R.id.Address_delete);
            holder.Select = (ImageView) view.findViewById(R.id.Address_select);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AddressBean.Address address = mList.get(i);

        holder.Name.setText(address.getName());
        holder.Phone.setText(address.getMobile());
        holder.Address.setText(address.getDetail());


        //进来的时候判断默认地址的
        String is_default = address.getIs_default();
        if (is_default.equals("0")) { //0:非默认；1默认
            holder.Status.setText("设为默认");
            holder.Select.setImageResource(R.mipmap.circle_select);
        } else {
            holder.Status.setText("默认地址");
            holder.Select.setImageResource(R.mipmap.circle_select_click);
        }

        //TODO 这里之后在判断一下
        holder.Select.setOnClickListener(mOnClickListener);
        holder.Edit.setOnClickListener(mOnClickListener);
        holder.Delete.setOnClickListener(mOnClickListener);

        return view;
    }

    private class ViewHolder {
        public TextView Name, Phone, Address, Status, Edit, Delete;//名字,电话,地址,状态,编辑,删除
        public ImageView Select;
    }
}
