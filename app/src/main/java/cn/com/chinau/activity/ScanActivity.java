package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.SysParamQueryBean;
import cn.com.chinau.zxing.activity.CaptureActivity;

/**
 * 扫码页面
 */
public class ScanActivity extends BaseActivity implements View.OnClickListener {

    private View mScanContent,mScanTitle;
    private Button mScan;//扫码回购
    private ImageView mBack;
    private TextView mTitle,mSummaryTv,mScan_Process1,mScan_Process2,mScan_Process3,mScan_Process4;
    private String mImage, mSummary;
    private ArrayList<String> mProcess;
    private ImageView mIcon;
    private SharedPreferences sp;


    @Override
    public View CreateTitle() {
        mScanTitle = View.inflate(this, R.layout.activity_scan_title, null);
        return mScanTitle;
    }

    @Override
    public View CreateSuccess() {
        mScanContent = View.inflate(this, R.layout.activity_scan_content, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_PRIVATE);
        initView();
        initListener();
        return mScanContent;
    }


    private void initView() {
        // 获取首页传过来的数据
//        Bundle bundle = getIntent().getExtras();
//        mImage = bundle.getString("Image");// 头部图片
//        mSummary= bundle.getString("Summary");// 业务介绍
//        mProcess = bundle.getStringArrayList("Process");// 业务流程

        String mData = sp.getString("System","");
        Gson gson = new Gson();
        SysParamQueryBean paramQueryBean = gson.fromJson(mData, SysParamQueryBean.class);
        SysParamQueryBean.Sys_param_value sys_param_value = paramQueryBean.getSys_param_value();

        // 扫码回购页面的数据
        SysParamQueryBean.Sys_param_value.Buyback_scan_summary mBuyback_scan_summary = sys_param_value.getBuyback_scan_summary();
        mImage = mBuyback_scan_summary.getImage();// 扫码回购头部显示的图片
        mSummary = mBuyback_scan_summary.getSummary();// 业务介绍
        mProcess = mBuyback_scan_summary.getProcess();// 业务流程

        mBack = (ImageView) mScanTitle.findViewById(R.id.search_title_back);
        mTitle = (TextView) mScanTitle.findViewById(R.id.search_title);
        mTitle.setText("扫码回购");
        mIcon = (ImageView) mScanContent.findViewById(R.id.scan_icon);

        mSummaryTv = (TextView) mScanContent.findViewById(R.id.scan_summary);

        mScan_Process1 = (TextView) mScanContent.findViewById(R.id.scan_process1);
        mScan_Process2 = (TextView) mScanContent.findViewById(R.id.scan_process2);
        mScan_Process3 = (TextView) mScanContent.findViewById(R.id.scan_process3);
        mScan_Process4 = (TextView) mScanContent.findViewById(R.id.scan_process4);

        if (mImage != null&&mSummary != null &&mProcess != null){
            mBitmap.display(mIcon,mImage);
            mSummaryTv.setText(mSummary);
            if (mProcess.get(0) != null){
                mScan_Process1.setText(mProcess.get(0));
            }else{
                mScan_Process1.setVisibility(View.GONE);
            }
            if (mProcess.get(1) != null){
                mScan_Process2.setText(mProcess.get(1));
            }else{
                mScan_Process2.setVisibility(View.GONE);
            }
            if (mProcess.get(2) != null){
                mScan_Process3.setText(mProcess.get(2));
            }else{
                mScan_Process3.setVisibility(View.GONE);
            }
            if (mProcess.get(3) != null){
                mScan_Process4.setText(mProcess.get(3));
            }else{
                mScan_Process4.setVisibility(View.GONE);
            }
        }else{
            return;
        }

        mScan = (Button) mScanContent.findViewById(R.id.scan_back_buy_now);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mScan.setOnClickListener(this);
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.scan_back_buy_now://立即扫码回购
                openActivityWitchAnimation(CaptureActivity.class);
                finish();
                break;
        }
    }
}
