package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.AuctionRecordListViewAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.AuctionRecordBean;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 竞拍记录页面
 */
public class AuctionRecordActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View mAuctionRecordContent;
    private View mAuctionRecordTitle;
    private ImageView mRecordBack;//返回
    private ListView mRecordListView;
    private RadioButton mRecordAll, mRecording, mRecordOut, mRecordSuccess;//全部 竞拍中 已出局 竞拍成功
    private AuctionRecordListViewAdapter mAdapter;
    private List<AuctionRecordBean.Auction> mList;
    private SharedPreferences sp;
    private String mToken, mUser_id, result;
    private int num = 0;//初始索引
    private TextView mNoOrderTv;
    private RadioGroup mRecordGroup;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.QB_SUCCESS:// 全部
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    AuctionRecordBean mAuctionBean = gson.fromJson(msge, AuctionRecordBean.class);
                    String mRsp_code = mAuctionBean.getRsp_code();
                    String mRsp_msg = mAuctionBean.getRsp_msg();
                    if (mRsp_code.equals("0000")) {
                        mList = mAuctionBean.getAuction_rec_list();
                        if (mList != null && mList.size() != 0) {
                            initAdapter();
                        } else {
                            GoneOrVisibleView(); // ListView为空时显示的布局
                        }
                    }else if(mRsp_code.equals("1002")){
                        MyToast.showShort(AuctionRecordActivity.this,mRsp_msg);
                    }
                    break;
                case StaticField.JPZ_SUCCESS: // 竞拍中
                    String msge1 = (String) msg.obj;
                    Gson gson1 = new Gson();
                    AuctionRecordBean mAuctionBean1 = gson1.fromJson(msge1, AuctionRecordBean.class);
                    String mRsp_code1 = mAuctionBean1.getRsp_code();
                    String mRsp_msg1 = mAuctionBean1.getRsp_msg();
                    if (mRsp_code1.equals("0000")) {
                        mList = mAuctionBean1.getAuction_rec_list();
                        if (mList != null && mList.size() != 0) {
                            initAdapter();
                            mNoOrderTv.setVisibility(View.GONE); // 无信息控件显示
                        } else {
                            GoneOrVisibleView();// ListView为空时显示的布局
                        }
                    }else if(mRsp_code1.equals("1002")){
                        MyToast.showShort(AuctionRecordActivity.this,mRsp_msg1);
                    }
                    break;
                case StaticField.CJ_SUCCESS: // 已出局
                    String msge2 = (String) msg.obj;
                    Gson gson2 = new Gson();
                    AuctionRecordBean mAuctionBean2 = gson2.fromJson(msge2, AuctionRecordBean.class);
                    String mRsp_code2 = mAuctionBean2.getRsp_code();
                    String mRsp_msg2 = mAuctionBean2.getRsp_msg();
                    if (mRsp_code2.equals("0000")) {
                        mList = mAuctionBean2.getAuction_rec_list();
                        if (mList != null && mList.size() != 0) {
                            initAdapter();
                        } else {
                            GoneOrVisibleView();// ListView为空时显示的布局
                        }
                    }else if(mRsp_code2.equals("1002")){
                        MyToast.showShort(AuctionRecordActivity.this,mRsp_msg2);
                    }
                    break;
                case StaticField.CG_SUCCESS: // 竞拍成功
                    String msge3 = (String) msg.obj;
                    Gson gson3 = new Gson();
                    AuctionRecordBean mAuctionBean3 = gson3.fromJson(msge3, AuctionRecordBean.class);
                    String mRsp_code3 = mAuctionBean3.getRsp_code();
                    String mRsp_msg3 = mAuctionBean3.getRsp_msg();
                    if (mRsp_code3.equals("0000")) {
                        mList = mAuctionBean3.getAuction_rec_list();
                        if (mList != null && mList.size() != 0) {
                            initAdapter();
                        } else {
                            GoneOrVisibleView();// ListView为空时显示的布局
                        }
                    }else if(mRsp_code3.equals("1002")){
                        MyToast.showShort(AuctionRecordActivity.this,mRsp_msg3);
                    }
                    break;
            }
        }
    };


    @Override
    public View CreateTitle() {
        mAuctionRecordTitle = View.inflate(this, R.layout.base_back_title, null);
        return mAuctionRecordTitle;
    }

    @Override
    public View CreateSuccess() {
        mAuctionRecordContent = View.inflate(this, R.layout.activity_auctionrecord_content, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mAuctionRecordContent;
    }

    @Override
    public void AgainRequest() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.record_all://全部
                if (!(result.equals("-1") | result.equals("-2"))) {
                    GetIntenerNet(num, StaticField.QB);
                }
                break;
            case R.id.btn_recording://竞拍中
                if (!(result.equals("-1") | result.equals("-2"))) {
                    GetIntenerNet(num, StaticField.JPZ);
                }
                break;
            case R.id.btn_recordout://已出局
                if (!(result.equals("-1") | result.equals("-2"))) {
                    GetIntenerNet(num, StaticField.CJ);
                }
                break;
            case R.id.record_success:///竞拍成功
                if (!(result.equals("-1") | result.equals("-2"))) {
                    GetIntenerNet(num, StaticField.CG);
                }
                break;
        }
    }

    // 点击条目跳转
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Bundle bundle = new Bundle();
        bundle.putString(StaticField.GOODS_SN, mList.get(i).getGoods_sn());// 传过去的编号
        bundle.putString("AuctionRecord", "AuctionRecord");
        openActivityWitchAnimation(AuctionDetailActivity.class, bundle);

    }

    private void initView() {
        // 获取本地保存的标识，用户ID
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        mRecordBack = (ImageView) mAuctionRecordTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mAuctionRecordTitle.findViewById(R.id.base_title);
        mTitle.setText("竞拍记录");
        mRecordGroup = (RadioGroup) mAuctionRecordContent.findViewById(R.id.record_radioGroup);
        mRecordAll = (RadioButton) mAuctionRecordContent.findViewById(R.id.record_all);
        mRecording = (RadioButton) mAuctionRecordContent.findViewById(R.id.btn_recording);
        mRecordOut = (RadioButton) mAuctionRecordContent.findViewById(R.id.btn_recordout);
        mRecordSuccess = (RadioButton) mAuctionRecordContent.findViewById(R.id.record_success);

        mRecordGroup.check(R.id.record_all);// 默认选中
        mRecordListView = (ListView) mAuctionRecordContent.findViewById(R.id.record_auction_LV);
        mNoOrderTv = (TextView) mAuctionRecordContent.findViewById(R.id.no_order_tv);
    }

    private void initData() {
        GetIntenerNet(num, StaticField.QB);
    }

    private void initAdapter() {
        mAdapter = new AuctionRecordListViewAdapter(this, mBitmap, mList);
        mRecordListView.setVisibility(View.VISIBLE);
        mNoOrderTv.setVisibility(View.GONE); // 无信息控件显示
        mRecordListView.setAdapter(mAdapter);//竞拍记录竞拍中适配器
        mAdapter.notifyDataSetChanged();
    }

    // ListView为空时显示的布局
    private void GoneOrVisibleView() {
        mRecordListView.setVisibility(View.GONE);
        mNoOrderTv.setVisibility(View.VISIBLE); // 无信息控件显示
        mNoOrderTv.setText("暂无竞拍记录信息~");
    }

    private void initListener() {
        mRecordBack.setOnClickListener(this);
        mRecordAll.setOnClickListener(this);
        mRecording.setOnClickListener(this);
        mRecordOut.setOnClickListener(this);
        mRecordSuccess.setOnClickListener(this);
        mRecordListView.setOnItemClickListener(this);

    }

    /**
     * 竞拍记录List网络请求
     */
    private void GetIntenerNet(final int num, final String status) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.AUCTIONREC);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                params.put(StaticField.AUCTION_STATUS, status); // 状态

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                result = HttpUtils.submitPostData(StaticField.ROOT, params);

                MyLog.LogShitou(status + "竞拍记录List-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                // 判断是哪个状态下的请求
                if (status.equals("QB")) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.QB_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (status.equals("JPZ")) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.JPZ_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (status.equals("CJ")) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.CJ_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } else if (status.equals("CG")) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = StaticField.CG_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }
}