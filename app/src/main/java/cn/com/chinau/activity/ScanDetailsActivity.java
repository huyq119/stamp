package cn.com.chinau.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.dialog.SendProgressDialog;
import cn.com.chinau.dialog.SussessDialog;
import cn.com.chinau.fragment.scandetails.ScanDetailFirstFragment;
import cn.com.chinau.fragment.scandetails.ScanDetailSecondFragment;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyHandler;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;
import cn.com.chinau.view.DragLayout;
import cn.com.chinau.zxing.activity.CaptureActivity;

/**
 * 扫码详情页
 */
public class ScanDetailsActivity extends BaseActivity implements View.OnClickListener {
    protected static final int DELPRESSAGE = 0;// 关闭进度条
    protected static final int SUCCESS = 1;// 请求成功
    protected static final int FAIL = 2;// 扫码失败
    protected static final int DELTEXTDIALOG = 3;// 删除失败对话框
    protected static final int AGAIN = 4;// 再次加载

    private View mScanDetailsTitle;
    private View mScanDetailsContent;
    private DragLayout mDragLayout;
    private TextView mBuyBack, mTitle;//申请回购按钮
    private ImageView mBack;
    private SendProgressDialog pd;
    private String introduction,result,token,userId;
    private SharedPreferences sp;
    private SussessDialog dialog ;
    private MyHandler handler = new MyHandler(this) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELPRESSAGE :
                    if (pd != null) {
                        pd.dismiss();
                    }
                    break;
                case SUCCESS :
                    result = (String) msg.obj;
                    sp.edit().putString("ScanBack", result).commit();
                    initData(result);// 设置数据
                    break;
                case FAIL :// 扫码失败
                    String FailResult = (String) msg.obj;
                    dialog = new SussessDialog(ScanDetailsActivity.this);
                    dialog.setText(FailResult);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(DELTEXTDIALOG, 2000);
                    break;
                case DELTEXTDIALOG :
                    if (dialog != null) {
                        dialog.show();
                    }
                    startActivity(new Intent(ScanDetailsActivity.this, CaptureActivity.class));
                    finish();
                    break;
                case AGAIN :
                    if (introduction != null) {
                        getNetData(introduction);
                    }
                    break;
                default :
                    break;
            }
        }

    };


    @Override
    public View CreateTitle() {
        mScanDetailsTitle = View.inflate(this, R.layout.base_back_title, null);
        return mScanDetailsTitle;
    }

    @Override
    public View CreateSuccess() {
        mScanDetailsContent = View.inflate(this, R.layout.activity_scandetails, null);
        sp = getSharedPreferences(StaticField.NAME,MODE_PRIVATE);
        initView();
        getIntentData();
        initListener();
        return mScanDetailsContent;
    }


    @Override
    public void AgainRequest() {
//        if (pd != null)
//            pd.dismiss();
        getIntentData();
    }

    private void initView() {
        mBack = (ImageView) mScanDetailsTitle.findViewById(R.id.base_title_back);
        mTitle = (TextView) mScanDetailsTitle.findViewById(R.id.base_title);
        mTitle.setText("商品详情");
        mDragLayout = (DragLayout) mScanDetailsContent.findViewById(R.id.scanDetail_dragLayout);
        mBuyBack = (TextView) mScanDetailsContent.findViewById(R.id.scan_buyBack);
    }

    /**
     * 获取上个页面传过来的数据
     */
    private void getIntentData() {
//        introduction = getIntent().getExtras().getString("result");
         introduction = sp.getString("result","");

        MyLog.e("获取在本地的url-->", introduction);
        // 判断返回结果的
        if (introduction != null) {
//         pd = new SendProgressDialog(this);
//            pd.show();
            Log.e("扫描结果-->", introduction);
            getNetData(introduction);
        }
    }

    private void initData(String result) {
        // 上面显示的view,下面显示的view
        ScanDetailFirstFragment first = new ScanDetailFirstFragment(result, mBitmap);
        final ScanDetailSecondFragment second = new ScanDetailSecondFragment(result);

        getSupportFragmentManager().beginTransaction().add(R.id.first, first).add(R.id.second, second).commit();

        DragLayout.ShowNextPageNotifier nextInfo = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                second.initView();
            }
        };
        mDragLayout.setNextPageListener(nextInfo);
    }


    private void initListener() {
        mBack.setOnClickListener(this);
        mBuyBack.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                openActivityWitchAnimation(ScanActivity.class);
                finish();
//                finishWitchAnimation();
                break;
            case R.id.scan_buyBack://申请回购
                token = sp.getString("token", "");
                userId = sp.getString("userId", "");
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(userId)) {// 已经登录进入下一页面
//                    Bundle mBundle = new Bundle();
//                    mBundle.putString("RESULT",result);
//                    openActivityWitchAnimation(AffirmBuyBackActivity.class,mBundle);// 确认回购
                    openActivityWitchAnimation(AffirmBuyBackActivity.class);// 确认回购
                    finish();

                } else {// 进入登录界面
                    Intent intent = new Intent(ScanDetailsActivity.this, LoginActivity.class);
                    intent.putExtra("WithDraw", "buyback");
                    startActivity(intent);
                }
                break;
        }
    }
    /**
     * 扫码后商品详情页面获取网络数据
     *
     * @param result
     *            扫码的结果
     */
    private void getNetData(final String result) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {


            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.QUERY);
                params.put(StaticField.SCAN_CODE, result);
                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String mResult = HttpUtils.submitPostData(StaticField.ROOT, params);
                if (mResult.equals("-1")) {
                    handler.sendEmptyMessage(AGAIN);
                    return;
                }
                  MyLog.LogShitou ("扫码商品详情-->", mResult);
                Gson gson = new Gson();
                BaseBean mBaseBean = gson.fromJson(mResult, BaseBean.class);
                String rsp_coode = mBaseBean.getRsp_code();
                if (rsp_coode.equals("0000")) {
                    Message msg = handler.obtainMessage();
                    msg.what = SUCCESS;
                    msg.obj = mResult;
                    handler.sendMessage(msg);
                } else {
                    Message msgFail = handler.obtainMessage();
                    msgFail.what = FAIL;
                    String rsp_msg = mBaseBean.getRsp_msg();
                    msgFail.obj = rsp_msg;
                    handler.sendMessage(msgFail);
                }

            }
        });
    }

}
