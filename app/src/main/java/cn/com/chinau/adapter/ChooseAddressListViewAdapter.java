package cn.com.chinau.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.base.AddressBean;

/**
 * Created by Administrator on 2016/9/21.
 * 选择收货地址ListView适配器
 */
public class ChooseAddressListViewAdapter extends BaseAdapter {
    private Context context;
    private List<AddressBean.Address> mList;

    public ChooseAddressListViewAdapter(Context context, List<AddressBean.Address> mList) {
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
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.activity_choose_address_listview_item, null);
            holder.ChooseLl = (LinearLayout) view.findViewById(R.id.item_choose_ll);
            holder.Name = (TextView) view.findViewById(R.id.item_choose_name);
            holder.Phone = (TextView) view.findViewById(R.id.item_choose_mobile);
            holder.Default = (TextView) view.findViewById(R.id.item_choose_is_default);
            holder.Address = (TextView) view.findViewById(R.id.item_choose_address);
            holder.img = (ImageView) view.findViewById(R.id.item_choose_img);
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
        Log.i("is_default状态--->",is_default);
        if (i==0) {
            holder.ChooseLl.setVisibility(View.GONE);
            holder.Default.setText("【默认地址】");
        }else{
            holder.ChooseLl.setVisibility(View.VISIBLE);
            holder.Default.setVisibility(View.GONE);
        }
        if (is_default.equals("1")){
            holder.img.setImageResource(R.mipmap.choose_address);
        }else{
            holder.img.setVisibility(View.GONE);
        }



        return view;
    }

    private class ViewHolder {
        public TextView Name, Phone, Default, Address;//名字,电话,状态,地址
        public ImageView img;
        public LinearLayout ChooseLl;
    }

}
