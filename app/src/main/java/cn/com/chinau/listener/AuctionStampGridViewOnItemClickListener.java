package cn.com.chinau.listener;

import android.view.View;
import android.widget.AdapterView;

import cn.com.chinau.adapter.SelfMallPanStampGridViewAdapter;

/**
 * Date: 2016/11/19 11:36
 * Autor：ChenXR
 * Mail：410529656@qq.com
 * 淘邮票筛选（邮市，竞拍）页面GridView监听事件
 */

public class AuctionStampGridViewOnItemClickListener implements AdapterView.OnItemClickListener {
    private int position;
    private SelfMallPanStampGridViewAdapter adapter;
    private AuctionStampGridViewOnItemClickListener.SelfMallItemClick mSelfMallItemClick;


    public AuctionStampGridViewOnItemClickListener(int position, SelfMallPanStampGridViewAdapter adapter) {
        this.position = position;
        this.adapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


//        if (position != i) {
//            position = i;
//            adapter.setSeclection(i);
//            adapter.notifyDataSetChanged();
//        } else {
//            position = -1;
//            adapter.setSeclection(adapter.getCount() + 1);
//            adapter.notifyDataSetChanged();
//        }

        if (position != i) {
            position = i;
            adapter.setSeclection(i);

            adapter.notifyDataSetChanged();
        } else {
            position = -1;
            adapter.setSeclection(adapter.getCount() + 1);
            adapter.notifyDataSetChanged();
        }


        //TODO 这里写个回调,这里是改变的数据


        mSelfMallItemClick.GetClickItem();
    }

    /**
     * 这个是接口的点击回调
     */
    public interface SelfMallItemClick {
        void GetClickItem();
    }

    public void setSelfMallItemClick(AuctionStampGridViewOnItemClickListener.SelfMallItemClick selfMallItemClick) {
        mSelfMallItemClick = selfMallItemClick;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition() {
        position = -1;
        adapter.setSeclection(adapter.getCount() + 1);
        adapter.notifyDataSetChanged();
    }

}
