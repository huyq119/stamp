package cn.com.chinau.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.bean.GoodsStampBean;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.StringTimeUtils;

/**
 * 竞拍ListView适配器
 * Created by Administrator on 2016/8/4.
 */
public class AuctionListViewAdapter extends BaseAdapter {

    private List<GoodsStampBean.GoodsList> mList;
    private Context context;
    private BitmapUtils bitmapUtils;
    public int mHour, mMin, mSecond;// 时，分，秒
    private boolean isRun = true;
    private Handler mHandler;

    public AuctionListViewAdapter(Context context, BitmapUtils bitmapUtils, List<GoodsStampBean.GoodsList> mList) {
        this.mList = mList;
        this.context = context;
        this.bitmapUtils = bitmapUtils;
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
            view = View.inflate(context, R.layout.stamp_listview_item, null);
            holder.mIcon = (ImageView) view.findViewById(R.id.stamp_icon);
            holder.mTitle = (TextView) view.findViewById(R.id.stamp_title);
            holder.mTimeTv = (TextView) view.findViewById(R.id.stamp_time_tv);
            holder.mTimeLl = (LinearLayout) view.findViewById(R.id.stamp_time_ll);
            holder.mTime = (TextView) view.findViewById(R.id.stamp_time);
            holder.mStatue = (TextView) view.findViewById(R.id.stamp_status);
            holder.mStampPrice = (TextView) view.findViewById(R.id.stamp_price);
            holder.mPrice = (TextView) view.findViewById(R.id.stamp_starting_price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final GoodsStampBean.GoodsList mGoodsList = mList.get(i);
        holder.mTitle.setText(mGoodsList.getGoods_name());

        String mLeft_time = mGoodsList.getLeft_time();

        if (!mLeft_time.equals("")) {
            startRun(); //开启倒计时
            // 将时间转换成时分秒格式
            int times = Integer.valueOf(mLeft_time).intValue(); // 转int类型
            String time = StringTimeUtils.calculatTime(times); //  将毫秒转换成时分秒格式
            String[] mTimes = time.split(",");
            String time2 = mTimes[0];// 获取小时
            mHour = Integer.valueOf(time2).intValue(); // 转int类型
            String time3 = mTimes[1]; // 分钟
            mMin = Integer.valueOf(time3).intValue();
            String time4 = mTimes[2]; // 秒
            mSecond = Integer.valueOf(time4).intValue();

            MyLog.LogShitou("=====剩余时间", "=======/" + mHour + "时" + mMin + "分" + mSecond + "秒");
        }

//        holder.mTime.setText(mGoodsList.getLeft_time());

        String mStatues = mGoodsList.getAuction_status();

        holder.mPrice.setText("￥" +mGoodsList.getCurrent_price());

        if (mStatues.equals("DP")) {
            holder.mStatue.setText("未开始");
            holder.mTimeTv.setText("距离开拍：");
            holder.mStampPrice.setText("起拍价：");
            holder.mStatue.setTextColor(context.getResources().getColor(R.color.red_font));
        } else if (mStatues.equals("JP")) {
            holder.mStatue.setText("竞拍中");
            holder.mStatue.setTextColor(context.getResources().getColor(R.color.refuse));
            holder.mTimeTv.setText("剩余时间：");
            holder.mStampPrice.setText("当前价：");
        } else if (mStatues.equals("JS")) {
            holder.mStatue.setText("已结束");
            holder.mTimeTv.setText("距离开拍：");
            holder.mStampPrice.setText("成交价：");
            holder.mTimeLl.setVisibility(View.INVISIBLE);
            holder.mStatue.setTextColor(context.getResources().getColor(R.color.font_color));
        }

    mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10:
                        computeTime(); // 倒计时计算
                        holder.mTime.setText(mHour + "小时" + mMin + "分" + mSecond + "秒");
                        if (mHour == 0 && mMin == 0 && mSecond == 0) {
                            holder.mStatue.setText("已结束");
                            holder.mTimeTv.setText("距离开拍：");
                            holder.mStampPrice.setText("成交价：");
                            holder.mTimeLl.setVisibility(View.INVISIBLE);
                            holder.mStatue.setTextColor(context.getResources().getColor(R.color.font_color));
                        }

                        break;
                }

            }
        };


//        int[] time = {01, 01, 05};
//        holder.mTime.setTimes(time);
//        if (!holder.mTime.isRun()) {
//            holder.mTime.run();
//        }
        bitmapUtils.display(holder.mIcon,mGoodsList.getGoods_img());
        return view;
    }

    public class ViewHolder {
        public ImageView mIcon;
        public LinearLayout mTimeLl;
//        public TimeTextView mTime;
        public TextView mTitle, mStatue, mPrice, mTimeTv,mStampPrice,mTime;
    }

    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000); // 停止1000ms
                        Message message = Message.obtain();
                        message.what = 10;
                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
//                    mDay--;
                }
            }
        }
    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//
//                computeTime(); // 倒计时计算
//                mTime.setText(mHour + "时" + mMin + "分" + mSecond + "秒");
//                if (mHour == 0 && mMin == 0 && mSecond == 0) {
//                        mTime.setVisibility(View.GONE);
//                    mTimeLl.setVisibility(View.GONE);
//                }
//
//            }
//        }
//    };

}
