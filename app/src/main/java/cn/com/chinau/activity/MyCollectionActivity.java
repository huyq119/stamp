package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private LinearLayout cocollection_edit;
    private MyCollectionListViewEditerAdapter mListAdapter;
    private ProgressDialog prodialog;
    private TextView Title;
    private Button dialog_button_cancel;
    private Button dialog_button_ok;
    private boolean isDel = true;
    private ImageView mALLImg;
    private String mToken,mUser_id;
    private SharedPreferences sp;
    private int num = 0;//初始索引
    private Button mAllBtn,mStampBtn,mAuctionBtn,mMallBtn;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case StaticField.SUCCESS:
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    CollectionBean mCollectionBean = gson.fromJson(msge, CollectionBean.class);
                    String mRsp_code = mCollectionBean.getRsp_code();
                    if (mRsp_code.equals("0000")) {
                        mList = mCollectionBean.getGoods_list();
                        MyLog.LogShitou("收藏夹有几条-->:", mList.size() + "");
                        if (mList != null && mList.size() != 0) {
                            //竖向ListView设置适配器
                            initAdapter();
                        }else {
                            MyToast.showShort(MyCollectionActivity.this,"还未有竞拍记录。。。");
                        }
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
        sp = getSharedPreferences(StaticField.NAME,MODE_PRIVATE);
        initView();
        initData();
//        initAdapter();
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
        mCollection_lv = (ListView) mCollectionContent.findViewById(R.id.collection_listView);
        mALLImg = (ImageView) mCollectionContent.findViewById(R.id.choose_all_img);

        mDelete = (TextView)mCollectionContent.findViewById(R.id.delete);
        cocollection_edit = (LinearLayout)mCollectionContent.findViewById(R.id.collection_edit);

        mAllBtn = (Button)mCollectionContent.findViewById(R.id.collection_all_btn);// 全部
        mStampBtn = (Button)mCollectionContent.findViewById(R.id.collection_stamps_btn);// 邮市
        mAuctionBtn = (Button)mCollectionContent.findViewById(R.id.collection_auction_btn);// 竞拍
        mMallBtn = (Button)mCollectionContent.findViewById(R.id.collection_mall_btn);// 商城



    }
    private void initData(){
//        mList = new ArrayList<>();
//        CollectionBean.Collection collection;
//        collection = new CollectionBean.Collection("庚申年", "YH", "YS", "100000.00", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
//        mList.add(collection);
//        collection = new CollectionBean.Collection("庚申年", "WH", "YS", "100000.00", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
//        mList.add(collection);
//        collection = new CollectionBean.Collection("庚申年", "JPZ", "JP", "100000.00", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
//        mList.add(collection);
//        collection = new CollectionBean.Collection("庚申年", "WKS", "JP", "100000.00", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
//        mList.add(collection);
//        collection = new CollectionBean.Collection("庚申年", "YJS", "JP", "100000.00", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
//        mList.add(collection);
//        collection = new CollectionBean.Collection("庚申年", "YH", "SC", "100000.00", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
//        mList.add(collection);
//        collection = new CollectionBean.Collection("庚申年1", "WH", "SC", "100000.00", "7983247", "http://img1.imgtn.bdimg.com/it/u=3024095604,405628783&fm=21&gp=0.jpg");
//        mList.add(collection);

        GetInitNet(num,StaticField.QB);// 收藏夹网络请求
    }
    private void initAdapter() {
        adapter = new CollectionListViewAdapter(this, mBitmap, mList);
        mCollection_lv.setAdapter(adapter);

    }
    private void initListener() {
        Back.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mALLImg.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mAllBtn.setOnClickListener(this);
        mStampBtn.setOnClickListener(this);
        mAuctionBtn.setOnClickListener(this);
        mMallBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_title_back://返回
                finishWitchAnimation();
                break;
            case R.id.base_edit:
                if (mEdit != null && isDel) {
                    isDel = false;
                    mEdit.setText("完成");
                    mListAdapter = new MyCollectionListViewEditerAdapter(this, mBitmap, mList);
                    adapter.notifyDataSetChanged();
                    mCollection_lv.setAdapter(mListAdapter);
                    mListAdapter.notifyDataSetChanged();
                    cocollection_edit.setVisibility(View.VISIBLE);


                } else if (!isDel && mEdit != null) {
                    isDel = true;
                    mEdit.setText("编辑");
                    adapter = new CollectionListViewAdapter(this, mBitmap, mList);
                    mCollection_lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    cocollection_edit.setVisibility(View.GONE);
                }
                break;
            case R.id.choose_all_img:// 反选按钮的回调接口
//                if(mSelect.isClickable()==true){
//                    // 遍历list的长度，将已选的设为未选，未选的设为已选
//                    for (int i = 0; i < mList.size(); i++) {
//                        if (MyCollectionListViewEditerAdapter.getIsSelected().get(i)) {
//                            MyCollectionListViewEditerAdapter.getIsSelected().put(i, false);
//                            checkNum--;
//                        } else {
//                            MyCollectionListViewEditerAdapter.getIsSelected().put(i, true);
//                            checkNum++;
//                        }
//                    }
//                    // 刷新listview和TextView的显示
//                    dataChanged();
//                }
                mALLImg.setImageResource(R.mipmap.select_red);
                break;
            case R.id.delete:
                DeleteDialog();//删除弹出框
                break;
            case R.id.collection_all_btn:// 全部
                GetInitNet(num,StaticField.QB);// 收藏夹网络请求
                break;
            case R.id.collection_stamps_btn:// 邮市
                GetInitNet(num,StaticField.YS);// 收藏夹网络请求
                break;
            case R.id.collection_auction_btn:// 竞拍
                GetInitNet(num,StaticField.JP);// 收藏夹网络请求
                break;
            case R.id.collection_mall_btn:// 商城
                GetInitNet(num,StaticField.SC);// 收藏夹网络请求
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
                ArrayList<CollectionBean.Collection> deleteList = new ArrayList<CollectionBean.Collection>();
                for (int i = 0; i < mList.size(); i++){
                    CollectionBean.Collection group = mList.get(i);
                    if (group.isChoosed()){
                        deleteList.add(group);
                    }
                    mList.removeAll(deleteList);
                    mListAdapter.notifyDataSetChanged();
                }
                prodialog.dismiss();// 关闭Dialog
            }
        });
    }
    // 刷新listview和TextView的显示
    private void dataChanged() {
        // 通知listView刷新
        mListAdapter.notifyDataSetChanged();
    }
    @Override
    public void AgainRequest(){
    }

    /**
     * 收藏夹网络请求
     * @param num 索引
     * @param goods_type 商品类型
     */
    private void GetInitNet(final int num,final String goods_type){
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
                MyLog.LogShitou("收藏夹List-->:", result);

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

    /**
     *  修改收藏夹网络请求
     * @param num 索引
     * @param op_type 操作类型
     */
    private void DeleteGetInitNet(final int num,final String op_type){
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.FAVORITELIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(StaticField.OFFSETNUM)); // 步长(item条目数)
                params.put(StaticField.GOODS_SN, "");//  邮票编号 （暂时为空）
                params.put(StaticField.OP_TYPE, op_type);// 操作类型：SC删除；JR加入

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                String result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("result修改-->:", result);

                if (result.equals("-1") | result.equals("-2")) {
                    return;
                }
//                Message msg = mHandler.obtainMessage();
//                msg.what = StaticField.SUCCESS;
//                msg.obj = result;
//                mHandler.sendMessage(msg);

            }
        });
    }

}
