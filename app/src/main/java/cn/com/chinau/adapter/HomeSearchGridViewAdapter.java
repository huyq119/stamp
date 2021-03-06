package cn.com.chinau.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.bean.SearchFristListBean;
import cn.com.chinau.view.RotateTextView;

/**
 * Created by Administrator on 2016/10/12.
 * 首页搜索list适配器
 */
public class HomeSearchGridViewAdapter extends BaseAdapter {

    private ArrayList<SearchFristListBean.ResultList> list;//填入的集合
    private Context context;
    private BitmapUtils bitmap;

    public HomeSearchGridViewAdapter(Context context, ArrayList<SearchFristListBean.ResultList> list, BitmapUtils bitmap) {
        this.list = list;
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            view = View.inflate(context, R.layout.panstamp_gridview_item, null);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.PanStamp_item_icon);
            viewHolder.mName = (TextView) view.findViewById(R.id.PanStamp_item_title);
            viewHolder.mMoney = (TextView) view.findViewById(R.id.PanStamp_item_money);
            viewHolder.mSource = (RotateTextView) view.findViewById(R.id.PanStamp_item_goodsSource);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //设置数据
        SearchFristListBean.ResultList mGoodsList = list.get(i);
        //设置图片
        viewHolder.mName.setText(mGoodsList.getName());
        viewHolder.mMoney.setText("￥" + mGoodsList.getPrice());
        bitmap.display(viewHolder.mIcon, mGoodsList.getImage());
        String mGoods_source = mGoodsList.getType();

        if (!mGoods_source.equals("")) {
            if (mGoods_source.equals("SC")) {
                viewHolder.mSource.setText("商城");
            }else if(mGoods_source.equals("YS")){
                viewHolder.mSource.setText("邮市");
            }else if(mGoods_source.equals("ML")){
                viewHolder.mSource.setText("邮票目录");
            }else if(mGoods_source.equals("JP")){
                viewHolder.mSource.setText("竞拍");
            }
        }

        return view;
    }


    public class ViewHolder {
        public ImageView mIcon;//图片
        public TextView mName, mMoney; //名称,钱数,
        public RotateTextView mSource;// 商品类型
    }
}
