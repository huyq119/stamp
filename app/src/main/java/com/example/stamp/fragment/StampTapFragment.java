package com.example.stamp.fragment;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.stamp.R;
import com.example.stamp.StaticField;
import com.example.stamp.activity.SearchActivity;
import com.example.stamp.activity.StampTapDetailActivity;
import com.example.stamp.adapter.StampTapGridViewAdapter;
import com.example.stamp.base.BaseFragment;
import com.example.stamp.bean.StampTapBean;
import com.example.stamp.dialog.StampTapFilterDialog;
import com.example.stamp.http.HttpUtils;
import com.example.stamp.utils.Encrypt;
import com.example.stamp.utils.MyLog;
import com.example.stamp.utils.SortUtils;
import com.example.stamp.utils.ThreadManager;
import com.example.stamp.zxing.activity.CaptureActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 邮票目录页面
 */
public class StampTapFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    private static final int STAMPCONTENT = 0;

    private View mStampTitle;//标题
    private ImageView mScan;//扫码按钮
    private ImageView mSearch;//搜索按钮
    private GridView mGrid;
    private View mStampTapContent;
    private Button mSynthesize, mRelease, mPrice, mFilter;//筛选按钮

    private StampTapGridViewAdapter gvAdapter;

    private boolean Synthesizeflag;// 综合的标记->升序还是降序false升序,true是降序
    private boolean Salesflag;// 发行时间的标记->升序还是降序false升序,true是降序
    private boolean Priceflag;// 价格的标记标记->升序还是降序false升序,true是降序

    private ArrayList<StampTapBean.StampList> mList;
    private int num = 0;//初始索引

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case STAMPCONTENT://邮票信息内容
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    StampTapBean stampTapBean = gson.fromJson(result, StampTapBean.class);
                    mList = stampTapBean.getStamp_list();
                    initAdapter();
                    break;
            }
        }
    };





    @Override
    public View CreateTitle() {
        mStampTitle = View.inflate(getActivity(), R.layout.fragment_stamptap_title, null);
        return mStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mStampTapContent = View.inflate(getActivity(), R.layout.fragment_stamptap_content, null);
        initView();
        initData();
        initListener();
        return mStampTapContent;
    }


    private void initView() {
        mScan = (ImageView) mStampTitle.findViewById(R.id.stamptap_title_scan);
        mSearch = (ImageView) mStampTitle.findViewById(R.id.stamptap_search);
        mGrid = (GridView) mStampTapContent.findViewById(R.id.stamptap_gl);
        mFilter = (Button) mStampTapContent.findViewById(R.id.stamptap_filter);
        mSynthesize = (Button) mStampTapContent.findViewById(R.id.stampTap_synthesize);
        mRelease = (Button) mStampTapContent.findViewById(R.id.stampTap_sales);
        mPrice = (Button) mStampTapContent.findViewById(R.id.stampTap_price);
    }

    private void initData() {
        if (mList != null) {
            mList = new ArrayList<>();
        }
        // 初始化第一个按钮
        setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
        RequestNet(StaticField.ZH, num, StaticField.A);
    }

    /**
     * 请求网络的方法
     *
     * @param Order_By 类别
     * @param index    角标
     * @param Sort     排序
     */
    private void RequestNet(final String Order_By, final int index, final String Sort) {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put(StaticField.SERVICE_TYPE, StaticField.STAMPTAP);
                params.put(StaticField.CURRENT_INDEX, String.valueOf(index));
                params.put(StaticField.ORDER_BY, Order_By);
                params.put(StaticField.SORT_TYPE, Sort);
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM));
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                MyLog.e(md5code);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.e(result);

                if (result.equals("-1")) {
                    return;
                }

                Message msg = mHandler.obtainMessage();
                msg.obj = result;
                msg.what = STAMPCONTENT;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void initAdapter() {
//        if (gvAdapter == null) {
        //为GridView设置适配器
        gvAdapter = new StampTapGridViewAdapter(getActivity(), mList, mBitmap);
//        }
        mGrid.setAdapter(gvAdapter);
        gvAdapter.notifyDataSetChanged();

    }

    private void initListener() {
        mGrid.setOnItemClickListener(this);
        mFilter.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mSynthesize.setOnClickListener(this);
        mRelease.setOnClickListener(this);
        mPrice.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stamptap_filter://筛选按钮
                StampTapFilterDialog stampTapFilterDialog = new StampTapFilterDialog();
                stampTapFilterDialog.show(getFragmentManager(), StaticField.STAMPTAPFILTERDIALOG);
                break;
            case R.id.stamptap_title_scan://扫码按钮
                openActivityWitchAnimation(CaptureActivity.class);
                break;
            case R.id.stamptap_search://搜索按钮
                openActivityWitchAnimation(SearchActivity.class);
                break;
            case R.id.stampTap_synthesize://综合
                setOtherButton(mRelease, mPrice);
                Salesflag = true;
                Priceflag = true;
                // true代表降序,false代表升序
                if (Synthesizeflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.D);
                    Synthesizeflag = false;
                } else {
                    setDrawable(R.mipmap.top_arrow_top, mSynthesize, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.ZH, num, StaticField.D);
                    Synthesizeflag = true;
                }
                break;
            case R.id.stampTap_sales://发行时间
                setOtherButton(mSynthesize, mPrice);
                Synthesizeflag = true;
                Priceflag = true;

                if (Salesflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mRelease, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.SJ, num, StaticField.D);
                    Salesflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mRelease, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.SJ, num, StaticField.A);
                    Salesflag = true;
                }
                break;
            case R.id.stampTap_price://价格
                setOtherButton(mSynthesize, mRelease);
                Synthesizeflag = true;
                Salesflag = true;

                if (Priceflag) {
                    setDrawable(R.mipmap.top_arrow_bottom, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.D);
                    Priceflag = false;
                } else {
                    Log.e("flag", "false");
                    setDrawable(R.mipmap.top_arrow_top, mPrice, Color.parseColor("#ff0000"));
                    RequestNet(StaticField.JG, num, StaticField.A);
                    Priceflag = true;
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String stamp_sn = mList.get(i).getStamp_sn();
        Bundle bundle = new Bundle();
        bundle.putString(StaticField.STAMPDETAIL, stamp_sn);
        openActivityWitchAnimation(StampTapDetailActivity.class, bundle);
    }

    /**
     * 设置其他button的方法
     *
     * @param btn1
     * @param btn2
     */
    private void setOtherButton(Button btn1, Button btn2) {
        setDrawable(R.mipmap.top_arrow_normal, btn1, Color.parseColor("#666666"));
        setDrawable(R.mipmap.top_arrow_normal, btn2, Color.parseColor("#666666"));
    }

    /**
     * 设置按钮
     */
    private void setDrawable(int ID, Button button, int color) {
        Drawable drawable = getActivity().getResources().getDrawable(ID);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        button.setCompoundDrawables(null, null, drawable, null);
        button.setTextColor(color);
    }
}
