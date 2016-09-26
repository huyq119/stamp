package cn.com.chinau.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.StampTapBean;
import cn.com.chinau.dialog.AddStampDialog;


/**
 * 邮票目录的GridView的适配器
 * Created by Administrator on 2016/7/28.
 */
public class StampTapGridViewAdapter extends BaseAdapter {

    private static final int DELDIALOG = 0;
    private ArrayList<StampTapBean.StampList> mList;//填入的集合
    private Context context;
    private BitmapUtils bitmap;
    private AddStampDialog mAdd;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdd.dismiss();
        }
    };


    public StampTapGridViewAdapter(Context context, ArrayList<StampTapBean.StampList> mList, BitmapUtils bitmap) {
        this.mList = mList;
        this.context = context;
        this.bitmap = bitmap;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.stamptap_gridview_item, null);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.StampTap_item_icon);
            viewHolder.mMoney = (TextView) view.findViewById(R.id.StampTap_item_money);
            viewHolder.mTitle = (TextView) view.findViewById(R.id.StampTap_item_title);
            viewHolder.mAdd = (ImageView) view.findViewById(R.id.StampTap_item_add);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        StampTapBean.StampList stampList = mList.get(i);
        viewHolder.mTitle.setText(stampList.getStamp_name());
        viewHolder.mMoney.setText(stampList.getCurrent_price());
        //设置图片
        bitmap.display(viewHolder.mIcon, stampList.getStamp_img());
        //添加收藏的点击按钮
        viewHolder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdd = new AddStampDialog(context);
                mAdd.show();
                handler.sendEmptyMessageDelayed(DELDIALOG,1000);
                
            }
        });

        return view;
    }

    public class ViewHolder {
        public ImageView mIcon, mAdd;//图片,添加按钮
        public TextView mTitle, mMoney;//名称,钱数
    }
}
