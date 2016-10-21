package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.CollectionListViewAdapter;
import cn.com.chinau.adapter.MyCollectionListViewEditerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.BaseBean;
import cn.com.chinau.bean.CollectionBean;
import cn.com.chinau.dialog.ProgressDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.MyToast;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 我的收藏页面
 */
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {
    private View mCollectionTitle;
    private View mCollectionContent;
    private ListView mCollection_lv;
    private List<CollectionBean.Collection> mList;
    private ImageView Back;
    private TextView mEdit;
    private int checkNum; // 记录选中的条目数量
    private TextView mDelete;
    private CollectionListViewAdapter adapter;
    private LinearLayout CollEditLl, mChooseAllLl,mCollectionTabLl;
    private MyCollectionListViewEditerAdapter mListAdapter;
    private ProgressDialog prodialog;
    private TextView Title;
    private Button dialog_button_cancel;
    private Button dialog_button_ok;
    private boolean ChooseFlage = true;// 全选与全不选的标记
    private boolean EditFlage = true;// 点击编辑的标记
    private boolean initDataFlage = true;// 网络请求的次数标记
    private ImageView mChooseImg;
    private String mToken, mUser_id, mGoods_sn;
    private SharedPreferences sp;
    private int num = 0;//初始索引
    private RadioButton mAllBtn, mStampBtn, mAuctionBtn, mMallBtn;
    private RadioGroup mRadioGroup;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 收藏列表
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    CollectionBean mCollectionBean = gson.fromJson(msge, CollectionBean.class);
                    String mRsp_code = mCollectionBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mCollectionBean.getGoods_list();
//                        if (mList != null && mList.size() != 0) {
                            initAdapter();
//                        }
                    }
                    break;
                case StaticField.DETAILSUCCESS:// 删除后再次请求的数据
                    Gson gsons = new Gson();
                    CollectionBean mCollectionBean1 = gsons.fromJson((String) msg.obj, CollectionBean.class);
                    String codes = mCollectionBean1.getRsp_code();
                    if (codes.equals("0000")) {
                        mList = mCollectionBean1.getGoods_list();
                            initAdapter();
                            adapter.flage = !adapter.flage;
                            adapter.notifyDataSetChanged();
                    }
                    break;
                case StaticField.DeleteSUCCESS:// 删除
                    Gson gsonss = new Gson();
                    BaseBean mBaseBeans = gsonss.fromJson((String) msg.obj, BaseBean.class);
                    String mRsp_codes = mBaseBeans.getRsp_code();
                    String mRsp_msg = mBaseBeans.getRsp_msg();
                    if (mRsp_codes.equals("0000")) {
                        MyLog.LogShitou("删除后现在的值是啥", initDataFlage + "");
                        if (!initDataFlage) {
                            initData();// 请求网络刷新列表
                            MyLog.LogShitou("----->", "------>走了吗？？");
                        }

                        MyLog.LogShitou("删除成功", "--->删除成功");
                    }else{
                        MyToast.showShort(MyCollectionActivity.this,mRsp_msg);
                    }
                    break;

            }
        }
    };

    @Override
    public View CreateTitle() {
        mCollectionTitle = View.inflate(this, R.layout.base_font_title, null);
        return mCollectionTitle;
    }

    @Override
    public View CreateSuccess() {
        mCollectionContent = View.inflate(this, R.layout.activity_mycollection_content, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        if (initDataFlage) {
            initData();
        }
        initListener();
        return mCollectionContent;
    }

    private void initView() {

        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        Back = (ImageView) mCollectionTitle.findViewById(R.id.base_title_back);
        TextView Title = (TextView) mCollectionTitle.findViewById(R.id.base_title);
        Title.setText("收藏夹");
        mEdit = (TextView) mCollectionTitle.findViewById(R.id.base_edit);
        mEdit.setText("编辑");
        mCollectionTabLl = (LinearLayout) mCollectionContent.findViewById(R.id.collection_tab_ll);// 标签
        mCollection_lv = (ListView) mCollectionContent.findViewById(R.id.collection_listView);
        mRadioGroup = (RadioGroup) mCollectionContent.findViewById(R.id.record_radioGroup);
        mAllBtn = (RadioButton) mCollectionContent.findViewById(R.id.collection_all_btn);// 全部
        mStampBtn = (RadioButton) mCollectionContent.findViewById(R.id.collection_stamps_btn);// 邮市
        mAuctionBtn = (RadioButton) mCollectionContent.findViewById(R.id.collection_auction_btn);// 竞拍
        mMallBtn = (RadioButton) mCollectionContent.findViewById(R.id.collection_mall_btn);// 商城
        mRadioGroup.check(R.id.collection_all_btn);// 默认选中
        // 全选，删除行
        CollEditLl = (LinearLayout) mCollectionContent.findViewById(R.id.collection_edit_ll);
        mDelete = (TextView) mCollectionContent.findViewById(R.id.delete);// 删除
        mChooseAllLl = (LinearLayout) mCollectionContent.findViewById(R.id.choose_all_ll);// 全选
        mChooseImg = (ImageView) mCollectionContent.findViewById(R.id.choose_img);// 选中图片

    }

    private void initData() {
        GetInitNet(num, StaticField.QB);// 收藏夹网络请求
    }

    private void initAdapter() {
        adapter = new CollectionListViewAdapter(this, mBitmap, mList);
        mCollection_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initListener() {
        Back.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mAllBtn.setOnClickListener(this);
        mStampBtn.setOnClickListener(this);
        mAuctionBtn.setOnClickListener(this);
        mMallBtn.setOnClickListener(this);
        mChooseAllLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.base_edit:// 编辑
                // 点击编辑的标志
                if (EditFlage) {
                    adapter.flage = !adapter.flage; // flage设为True
                    if (adapter.flage) {
                        mEdit.setText("完成");
                        CollEditLl.setVisibility(View.VISIBLE); // 显示删除栏
                        mCollectionTabLl.setVisibility(View.GONE); // 隐藏标签栏
                        MyLog.LogShitou("记录1", "点击了编辑");
                    } else {
                        mEdit.setText("编辑");
                        CollEditLl.setVisibility(View.GONE); //隐藏删除栏
                        mCollectionTabLl.setVisibility(View.VISIBLE); // 显示标签栏
                    }
                    adapter.notifyDataSetChanged();
                    EditFlage = false; // 点击编辑EditFlage设为false

                } else {
//                    adapter.flage = !adapter.flage;
                    if (!adapter.flage) { // flage不为true
                        mEdit.setText("完成");
                        CollEditLl.setVisibility(View.VISIBLE); // 显示删除栏
                        mCollectionTabLl.setVisibility(View.GONE);  // 隐藏标签栏
                    } else {
                        mEdit.setText("编辑");
                        CollEditLl.setVisibility(View.GONE);//隐藏删除栏
                        mCollectionTabLl.setVisibility(View.VISIBLE); // 显示标签栏
                        adapter.flage = false;// 隐藏列表中的选择框
                    }
                    adapter.notifyDataSetChanged();
                    EditFlage = true; // 点击编辑EditFlage设为Ture
                }
                break;
            case R.id.choose_all_ll:// 全选
                if (ChooseFlage) {
                    if (adapter.flage) { // 全选
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).isChoosed = true;
                            mChooseImg.setImageResource(R.mipmap.select_red);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    ChooseFlage = false; // 全选后设为false
                } else {
                    if (adapter.flage) { // 全不选
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).isChoosed = false;
                            mChooseImg.setImageResource(R.mipmap.select_white);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    ChooseFlage = true;// 全不选后设为true
                }

                break;
            case R.id.delete:// 删除按钮
                // 获取选中删除条目的Goods_sn商品编号
                List<String> list_sn = new ArrayList<>();
                // 循环拿到商品编号
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isChoosed) {
                        list_sn.add(mList.get(i).getGoods_sn());
                    }
                }
                // 去掉（[,],空格）符号
                mGoods_sn = list_sn.toString().replace("[", "").replace("]", "").replace(" ", "");
                MyLog.LogShitou("选择了啥", mGoods_sn);
                if (!mGoods_sn.equals("")) {
                    DeleteDialog();//删除弹出框
                } else {
                    MyToast.showShort(this, "请选择要删除的商品");
                }
                break;
            case R.id.collection_all_btn:// 全部
                GetInitNet(num, StaticField.QB);// 收藏夹网络请求
                break;
            case R.id.collection_stamps_btn:// 邮市
                GetInitNet(num, StaticField.YS);
                break;
            case R.id.collection_auction_btn:// 竞拍
                GetInitNet(num, StaticField.JP);
                break;
            case R.id.collection_mall_btn:// 商城
                GetInitNet(num, StaticField.SC);
                break;
            default:
                break;
        }
    }


    /**
     * 删除弹出框
     */

    private void DeleteDialog() {
        prodialog = new ProgressDialog(this);
        prodialog.show();
        Title = (TextView) prodialog.findViewById(R.id.title_tv);
        Title.setText("确定要删除吗？");
        // 取消
        dialog_button_cancel = (Button) prodialog
                .findViewById(R.id.dialog_button_cancel);
        // 确定
        dialog_button_ok = (Button) prodialog
                .findViewById(R.id.dialog_button_ok);// 取消
        // 取消
        dialog_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodialog.dismiss();
            }
        });
        dialog_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteGetInitNet(StaticField.SC, mGoods_sn); // 删除网络请求
                prodialog.dismiss();
            }
        });
    }


    @Override
    public void AgainRequest() {
    }

    /**
     * 收藏夹网络请求
     *
     * @param num        索引
     * @param goods_type 商品类型
     */
    private void GetInitNet(final int num, final String goods_type) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.FAVORITELIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                params.put(StaticField.GOODS_TYPE, goods_type);// 商品类型：SC商城，YS邮市，JP竞拍，QB全部

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou(goods_type + "收藏夹List", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                if (goods_type.equals("QB")){// 全部
//                    initDataFlage = true;
                    if (initDataFlage) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = StaticField.SUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                        MyLog.LogShitou("第一次请求这是多少01", initDataFlage + "");
                        initDataFlage = false;
                    } else {
                        Message msg = mHandler.obtainMessage();
                        msg.what = StaticField.DETAILSUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                        MyLog.LogShitou("再次请求这是多少02", initDataFlage + "");

                    }
                }else if(goods_type.equals("YS")){ // 邮市
                    initDataFlage = true;
                    if (initDataFlage) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = StaticField.SUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                        MyLog.LogShitou("第一次请求这是多少01", initDataFlage + "");
                        initDataFlage = false;
                    } else {
                        Message msg = mHandler.obtainMessage();
                        msg.what = StaticField.DETAILSUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                        MyLog.LogShitou("再次请求这是多少02", initDataFlage + "");

                    }
                }else if(goods_type.equals("JP")){ // 竞拍
                    initDataFlage = true;
                    if (initDataFlage) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = StaticField.SUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                        MyLog.LogShitou("第一次请求这是多少01", initDataFlage + "");
                        initDataFlage = false;
                    } else {
                        Message msg = mHandler.obtainMessage();
                        msg.what = StaticField.DETAILSUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                        MyLog.LogShitou("再次请求这是多少02", initDataFlage + "");

                    }
                }else if(goods_type.equals("SC")){ // 商城
                    initDataFlage = true;
                    if (initDataFlage) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = StaticField.SUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                        MyLog.LogShitou("第一次请求这是多少01", initDataFlage + "");
                        initDataFlage = false;
                    } else {
                        Message msg = mHandler.obtainMessage();
                        msg.what = StaticField.DETAILSUCCESS;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                        MyLog.LogShitou("再次请求这是多少02", initDataFlage + "");

                    }
                }



            }
        });
    }

    /**
     * 删除收藏网络请求
     *
     * @param op_type
     * @param goods_sn
     */
    private void DeleteGetInitNet(final String op_type, final String goods_sn) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.FAVORITEMODIFY);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.GOODS_SN, goods_sn);//  邮票编号
                params.put(StaticField.OP_TYPE, op_type);// 操作类型

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);
                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou(op_type + "-" + "收藏删除", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.what = StaticField.DeleteSUCCESS;
                msg.obj = result;
                mHandler.sendMessage(msg);

            }
        });
    }

}
