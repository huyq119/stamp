package cn.com.chinau.fragment.scandetails;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.adapter.BackMarketAdapter;
import cn.com.chinau.bean.ScanBean;
import cn.com.chinau.dialog.SendProgressDialog;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.StringTimeUtils;
import cn.com.chinau.view.ChartView;

/**
 * 扫码回购详情页展示的第一个View
 * Created by Administrator on 2016/8/17.
 */
public class ScanDetailFirstFragment extends Fragment{

    private View mScanDetailView;
    public BitmapUtils bitmapUtils;
    public String result;// 传过来的结果
    protected static final int DELPRESSAGE = 3;// 删除失败对话框
    private static final int AUTO = 5;// 自动轮播
    private SendProgressDialog pd;
    private ViewPager mViewpagerPager;
    private RelativeLayout mChart;

    private TextView mBuyback_price,mService_fee,mCurrent_price,mIncome,mPublishcount,mBuytime,
            mName,mCurrentPrice,mIncrease,mService_fee_rate;

    public ScanDetailFirstFragment(String result, BitmapUtils bitmapUtils) {
        this.result = result;
        this.bitmapUtils = bitmapUtils;
        this.pd = pd;
    }

    private MyHandler handler = new MyHandler(getActivity()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case DELPRESSAGE :
//                    if (pd != null) {
//                        pd.dismiss();
//                    }
//                    break;
                case AUTO :
                    int currentItem = mViewpagerPager.getCurrentItem();
                    mViewpagerPager.setCurrentItem(currentItem + 1, false);
                    handler.sendEmptyMessageDelayed(AUTO, 5000);
                    break;
                default :
                    break;
            }
        }

    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mScanDetailView = View.inflate(getActivity(), R.layout.fragment_scandetail_first, null);
        initView();
        setData();// 解析传过来的数据
        return mScanDetailView;
    }


    private void initView(){
        mCurrent_price = (TextView) mScanDetailView.findViewById(R.id.market_current_price);// 当前价
        mIncome = (TextView) mScanDetailView.findViewById(R.id.market_income);//近半年涨幅
        mPublishcount = (TextView) mScanDetailView.findViewById(R.id.market_publishcount);// 已发行
        mBuytime = (TextView) mScanDetailView.findViewById(R.id.market_buytime);// 购买时间
        mName = (TextView) mScanDetailView.findViewById(R.id.market_name);// 用户
        mCurrentPrice = (TextView) mScanDetailView.findViewById(R.id.market_currentprice);// 当前价格
        mIncrease = (TextView) mScanDetailView.findViewById(R.id.market_increase);// 预计收益
        mService_fee_rate = (TextView) mScanDetailView.findViewById(R.id.market_service_fee_rate);// 服务费率
        mService_fee = (TextView) mScanDetailView.findViewById(R.id.market_service_fee);//服务费
        mBuyback_price = (TextView) mScanDetailView.findViewById(R.id.market_buyback_price);// 回购价格
        mChart = (RelativeLayout) mScanDetailView.findViewById(R.id.market_chart);//显示曲线图的布局
        mViewpagerPager = (ViewPager) mScanDetailView.findViewById(R.id.market_viewpager);// 头布局的ViewPager

    }

    /**
     * 解析传过来的数据
     *
     */
    private void setData() {
        Gson gson = new Gson();
        ScanBean scanBean = gson.fromJson(result, ScanBean.class);
        mCurrent_price.setText("￥" + scanBean.getCurrent_price());
        mIncome.setText("+" + scanBean.getIncrease());
        mPublishcount.setText(scanBean.getPublish_count());
        mBuytime.setText(StringTimeUtils.getShortTime(scanBean.getBuy_time()));
        mName.setText(scanBean.getBuyer());
        mCurrentPrice.setText("￥" + scanBean.getCurrent_price());
        mIncrease.setText(scanBean.getIncome() + "%");
        mService_fee_rate.setText("服务费:(" + scanBean.getService_fee_rate() + ")");
        mService_fee.setText("￥" + scanBean.getService_fee());
        mBuyback_price.setText("￥" + scanBean.getBuyback_price());
        String[] goods_images = scanBean.getGoods_images();
        // bitmapUtils.display(mIcon, scanBean.getGoods_images());
        // Log.e("测试", scanBean.getGoods_images()[0]);

        setDataInAdapter(scanBean);

        ArrayList<ScanBean.PriceHistory> price_history_list = scanBean.getPrice_history_list();
        String[] xLabels = new String[price_history_list.size()];
        float[] yLabels = new float[price_history_list.size()];
        String[] arr = new String[6];
        for (int i = 0; i < price_history_list.size(); i++) {
            xLabels[i] = price_history_list.get(i).getTime();
            yLabels[i] = Float.valueOf(price_history_list.get(i).getPrice());
        }

        Float max = Float.valueOf((yLabels[0]));// y轴的最大值
        for (int i = 0; i < yLabels.length; i++) {
            if (max < Float.valueOf((yLabels[i]))) {
                max = Float.valueOf((yLabels[i]));
            }
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = String.valueOf((max * (i)) / 5);
        }
        String[] ylab = new String[price_history_list.size()];
        for (int i = 0; i < ylab.length; i++) {
            ylab[i] = String.valueOf(yLabels[i]);
        }

        ChartView view = new ChartView(getActivity());
        view.SetInfo(xLabels, arr, ylab, "价格曲线图");
        mChart.addView(view);
//        handler.sendEmptyMessage(DELPRESSAGE);
    }
    /**
     * 整理数据,并设置适配器
     *
     * @param scanBean
     *            数据源
     */
    private void setDataInAdapter(ScanBean scanBean) {
        if (scanBean.getGoods_images() != null) {
            String[] goods_images = scanBean.getGoods_images();
            MyLog.LogShitou("goods_images商品图片001-->:", goods_images.length + "");
            String[] small_images = new String[goods_images.length];
            String[] big_images = new String[goods_images.length];
            for (int i = 0; i < goods_images.length; i++) {
                String[] image = goods_images[i].split(",");
                small_images[i] = image[0];
                big_images[i] = image[1];
            }
            MyLog.LogShitou("small_images-->:", small_images.length + "");
            MyLog.LogShitou("big_images-->:", big_images.length + "");

            if (small_images != null) {

                // 截取图片返回的url
//                for (int i = 0; i < small_images.length; i++) {
//                    int first = small_images[i].lastIndexOf("_");
//                    int last = small_images[i].lastIndexOf(".");
//                    StringBuffer sb = new StringBuffer(small_images[i]);
//                    StringBuffer banner = sb.delete(first, last);
//                    Log.e("url-------------->", banner.toString());
//                    small_images[i] = banner.toString();
//                }

                BackMarketAdapter mVAdapter = new BackMarketAdapter(bitmapUtils, small_images, getActivity());
                mViewpagerPager.setAdapter(mVAdapter);
                mViewpagerPager.setCurrentItem(small_images.length * 1000);
                // 轮播图片
                handler.sendEmptyMessageDelayed(AUTO, 5000);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(AUTO);
    }

}
