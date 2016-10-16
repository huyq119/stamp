package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private Button mRecordAll, mRecording, mRecordOut, mRecordSuccess;//全部 竞拍中 已出局 竞拍成功
    private AuctionRecordListViewAdapter mAdapter;
    private List<AuctionRecordBean.Auction> mList;
    private SharedPreferences sp;
    private String mToken, mUser_id,result;
    private int num = 0;//初始索引

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    AuctionRecordBean mAuctionBean = gson.fromJson(msge, AuctionRecordBean.class);
                    String mRsp_code = mAuctionBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mAuctionBean.getAuction_rec_list();
                        MyLog.LogShitou("竞拍列表有几条-->:", mList.size() + "");
                        if (mList != null && mList.size() != 0) {
                            //竖向ListView设置适配器
                            initAdapter();
                        }else {
                            mRecordGroup_ll.setVisibility(View.GONE);// 标签栏
                            mRecordListView.setVisibility(View.GONE);
                            mNoOrderTv.setVisibility(View.VISIBLE); // 无信息控件显示
                            mNoOrderTv.setText("暂无订单信息");
                        }
                    }
                    break;
            }
        }
    };
    private LinearLayout mRecordGroup_ll;
    private TextView mNoOrderTv;

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
                if (!(result.equals("-1") | result.equals("-2"))){
                    GetIntenerNet(num, StaticField.QB);
                }
                break;
            case R.id.btn_recording://竞拍中
                if (!(result.equals("-1") | result.equals("-2"))){
                    GetIntenerNet(num, StaticField.JPZ);
                }
                break;
            case R.id.btn_recordout://已出局
                if (!(result.equals("-1") | result.equals("-2"))){
                    GetIntenerNet(num, StaticField.CJ);
                }
                break;
            case R.id.record_success:///竞拍成功
                if (!(result.equals("-1") | result.equals("-2"))){
                    GetIntenerNet(num, StaticField.CG);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        openActivityWitchAnimation(SelfMallDetailActivity.class);
    }

    private void initView() {
        // 获取本地保存的标识，用户ID
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        mRecordBack = (ImageView) mAuctionRecordTitle.findViewById(R.id.base_title_back);
        TextView mTitle = (TextView) mAuctionRecordTitle.findViewById(R.id.base_title);
        mTitle.setText("竞拍记录");
        mRecordListView = (ListView) mAuctionRecordContent.findViewById(R.id.record_auction_LV);
        mRecordAll = (Button) mAuctionRecordContent.findViewById(R.id.record_all);
        mRecording = (Button) mAuctionRecordContent.findViewById(R.id.btn_recording);
        mRecordOut = (Button) mAuctionRecordContent.findViewById(R.id.btn_recordout);
        mRecordSuccess = (Button) mAuctionRecordContent.findViewById(R.id.record_success);
        mRecordGroup_ll = (LinearLayout) mAuctionRecordContent.findViewById(R.id.record_radioGroup_ll);
        mNoOrderTv = (TextView) mAuctionRecordContent.findViewById(R.id.no_order_tv);
    }

    private void initData() {

        GetIntenerNet(num, StaticField.QB);
    }

    private void initAdapter(){
        mAdapter = new AuctionRecordListViewAdapter(this, mBitmap, mList);
        mRecordListView.setAdapter(mAdapter);//竞拍记录竞拍中适配器
        mAdapter.notifyDataSetChanged();
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
                MyLog.LogShitou("竞拍记录List-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.SUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }
}