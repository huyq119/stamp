package cn.com.chinau.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.chinau.R;
import cn.com.chinau.bean.CollectionBean;

/**
 * 收藏夹的适配器
 * Created by Administrator on 2016/8/21.
 */
public class CollectionListViewAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private Context context;
    private BitmapUtils bitmapUtils;
    private List<CollectionBean.Collection> mList;
    public boolean flage = false;

    public Map<Integer, String> selected;
    private final ArrayList<String> dataIds;

    public CollectionListViewAdapter(Context context, BitmapUtils bitmapUtils, List<CollectionBean.Collection> mList) {
        this.context = context;
        this.bitmapUtils = bitmapUtils;
        this.mList = mList;

        mInflater = LayoutInflater.from(this.context);

        selected = new HashMap<>();
        dataIds = new ArrayList<>();
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
            view = View.inflate(context, R.layout.collection_listview_item, null);
            holder.goodsImg = (ImageView) view.findViewById(R.id.collection_icon);
            holder.goodsName = (TextView) view.findViewById(R.id.collection_Name);
            holder.goodsType = (TextView) view.findViewById(R.id.collection_flag);
            holder.goodsPrice = (TextView) view.findViewById(R.id.collection_price);
            holder.goodsStatus = (TextView) view.findViewById(R.id.collection_GoodsStatus);
            holder.mChexkbox= (CheckBox)view.findViewById(R.id.checkbox_operate_data);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final CollectionBean.Collection collection = mList.get(i);

        holder.goodsName.setText(collection.getGoods_name());
        holder.goodsPrice.setText("￥"+collection.getGoods_price());

        //SC商城，YS邮市，JP竞拍
        String goods_type = collection.getGoods_type();//商品类型
        if (goods_type.equals("SC")) {
            holder.goodsType.setText("商城");
        } else if (goods_type.equals("YS")) {
            holder.goodsType.setText("邮市");
        } else if (goods_type.equals("JP")) {
            holder.goodsType.setText("竞拍");
        }
        //商品状态：WH无货；YH有货；JPZ竞拍中；WKS未开始；YJS已结束
        String goods_status = collection.getGoods_status();
        if(goods_status.equals("WH")){
            holder.goodsStatus.setText("无货");
            holder.goodsStatus.setTextColor(Color.parseColor("#666666"));
            holder.goodsStatus.setVisibility(View.VISIBLE);
        }else if(goods_status.equals("YH")){
            holder.goodsStatus.setText("有货");
            holder.goodsStatus.setVisibility(View.VISIBLE);
        }else if(goods_status.equals("JPZ")){
            holder.goodsStatus.setText("竞拍中");
            holder.goodsStatus.setTextColor(Color.parseColor("#ff9900"));
            holder.goodsStatus.setVisibility(View.VISIBLE);
        }else if(goods_status.equals("WKS")){
            holder.goodsStatus.setText("未开始");
            holder.goodsStatus.setTextColor(Color.parseColor("#666666"));
            holder.goodsStatus.setVisibility(View.VISIBLE);
        }else if(goods_status.equals("YJS")){
            holder.goodsStatus.setText("已结束");
            holder.goodsStatus.setTextColor(Color.parseColor("#666666"));
            holder.goodsStatus.setVisibility(View.VISIBLE);
        }
        bitmapUtils.display(holder.goodsImg, collection.getGoods_img());

        // 根据isSelected来设置checkbox的显示状况
        if (flage) {
            holder.mChexkbox.setVisibility(View.VISIBLE);
        } else {
            holder.mChexkbox.setVisibility(View.GONE);
        }
        if (selected.containsKey(i)) {

            holder.mChexkbox.setChecked(true);

        }else {
            holder.mChexkbox.setChecked(false);

            holder.mChexkbox.setChecked(collection.isChoosed);
        }
        //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
        holder.mChexkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collection.isChoosed) {
                    collection.isChoosed = false;
                } else {
                    collection.isChoosed = true;
                }
            }
        });


        return view;
    }

    public class ViewHolder {
        public ImageView goodsImg;
        public TextView goodsName;
        public TextView goodsType;//商品类型
        public TextView goodsPrice;//商品价格
        public TextView goodsStatus;//商品状态
        public CheckBox mChexkbox;//是否选中图片
    }
}
