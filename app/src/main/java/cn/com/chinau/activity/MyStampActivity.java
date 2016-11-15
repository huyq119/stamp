package cn.com.chinau.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.chinau.R;
import cn.com.chinau.StaticField;
import cn.com.chinau.adapter.MyStampViewPagerAdapter;
import cn.com.chinau.base.BaseActivity;
import cn.com.chinau.bean.MyStampGridViewBean;
import cn.com.chinau.dialog.SharedDialog;
import cn.com.chinau.http.HttpUtils;
import cn.com.chinau.utils.Encrypt;
import cn.com.chinau.utils.MyLog;
import cn.com.chinau.utils.SortUtils;
import cn.com.chinau.utils.ThreadManager;

/**
 * 我的邮集页面
 */
public class MyStampActivity extends BaseActivity implements View.OnClickListener, UMShareListener, GridViewAdapter.DataList {

    private View mMyStampTitle, mMyStampContent;
    private ImageView mBack, mMore, mWeiXin, mPengYouQuan;
    private GridView mStampGV;
    private ArrayList<MyStampGridViewBean.StampList> mList;
    private View MorePopView;
    private PopupWindow mMorePop;
    private TextView mEdit, mShared, tv_cancel;// 编辑，分享
    //这个标记是编辑状态下的标记
    private boolean flag;

    // popup的背景
    private ColorDrawable mColorBg = new ColorDrawable(00000000);
    //viewpager的对象
    private ViewPager myStampViewPager;
    //数据集合
    private List<View> mViewPagerList;
    private int pageSize; //每页的数量
    private int pageCount;//总共的页数
    private LayoutInflater layoutInflater;
    private SharedPreferences sp;
    private String mToken, mUser_id, result;
    private int num = 0;//初始索引
    private SharedDialog dialog;
    private String mShare_url;
    private GridView grid;
    private String mTotal;
    private TextView mContentTotal,mNoOrderTv;
    private View dialog_finsih;
    private GridViewAdapter adapter;
    private String mToalPrice;
    private int mEditFlag;
    private static int offsetnum = 1000; // 显示邮集条数 步长(item条目数)
    private ArrayList<MyStampGridViewBean.StampList> dataList;
    private LinearLayout mContentTotalLl;
    private final static int OFFSETNUM = 1000;// 邮集显示的条数
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticField.SUCCESS:// 我的邮集
                    String msge = (String) msg.obj;
                    Gson gson = new Gson();
                    MyStampGridViewBean mOrderSweepBean = gson.fromJson(msge, MyStampGridViewBean.class);
                    String mRsp_code = mOrderSweepBean.getRsp_code();
                    mShare_url = mOrderSweepBean.getShare_url();// 分享地址url
                    if (mRsp_code.equals("0000")) {
                        mList = mOrderSweepBean.getStamp_list();
                        MyLog.LogShitou("我的邮集有几条-->:", mList.size() + "");
                        if (mList != null && mList.size() != 0) {
                            mMore.setVisibility(View.VISIBLE);// 更多显示
                            myStampViewPager.setVisibility(View.VISIBLE);
                            mContentTotalLl.setVisibility(View.VISIBLE);
                            mNoOrderTv.setVisibility(View.GONE); // 无信息控件显示

                            // 总价值
                            mTotal = mOrderSweepBean.getTotal_amount();
                            mContentTotal.setText("￥" + mTotal);
                            //初始化GridView的集合
                            mViewPagerList = new ArrayList<>();
                            myStampViewPager.setVisibility(View.VISIBLE);
                            mNoOrderTv.setVisibility(View.GONE); // 无信息控件显示
                            //获取Activity的LayoutInflater
                            layoutInflater = getLayoutInflater();

                            //计算每页最大显示个数，2列4行
                            pageSize = getResources().getInteger(R.integer.PagerCount) * 2;
                            //总共的页数 总数/每页数量，并取整。
                            pageCount = (int) Math.ceil(mList.size() * 1.0 / pageSize);

                            //使用For循环创建页面,并添加到集合中去

                            for (int i = 0; i < pageCount; i++) {
                                //每个页面都是inflate出一个新实例
                                //这里面传的是GridView的布局
                                grid = (GridView) layoutInflater.inflate(R.layout.item_viewpager, myStampViewPager, false);
                                //给GridView设置Adapter，传入index
                                GridViewAdapter mAdapter = new GridViewAdapter(MyStampActivity.this, mList, i, mBitmap, flag);
                                grid.setAdapter(mAdapter);
                                //加入到ViewPager的View数据集中
                                mViewPagerList.add(grid);
                            }
                            initAdapter();
                            // 条目监听事件
                            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String mStamp_sn = mList.get(position).getStamp_sn();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(StaticField.STAMPDETAIL_SN, mStamp_sn);
                                    MyLog.LogShitou("===========mStamp_sn邮集编号", mStamp_sn);
                                    openActivityWitchAnimation(StampTapDetailActivity.class, bundle);
                                }
                            });
                        }else{
//                            MyLog.LogShitou("========走这了吗=============", "========走这了吗=============");
                            GoneOrVisibleView(); // mList为空时显示的布局
                        }
                    }
                    break;
            }
        }
    };


    @Override
    public View CreateTitle() {
        mMyStampTitle = View.inflate(this, R.layout.activity_mystamp_title, null);
        return mMyStampTitle;
    }

    @Override
    public View CreateSuccess() {
        mMyStampContent = View.inflate(this, R.layout.activity_mystamp_content, null);
        sp = getSharedPreferences(StaticField.NAME, MODE_PRIVATE);
        initView();
        initData();
        initListener();
        return mMyStampContent;
    }

    @Override
    public void AgainRequest() {

    }

    // 适配器的回调回来的通知
    @Override
    public void GetDataList(ArrayList<MyStampGridViewBean.StampList> mDataList, String str) {
        mList.clear(); // 清空之前的mLsit
        mList = mDataList; // 赋值新的list数据
        String mToalPrice = str; // 获取总资产
        mContentTotal.setText("￥" + mToalPrice);
        MyLog.LogShitou("=============回调回来的数据",str+"=="+ mList.toString());

        if (mList != null && mList.size() != 0) {
            mViewPagerList.clear(); // 清空之前的mViewPagerList
            //使用For循环创建页面,并添加到集合中去
            for (int i = 0; i < pageCount; i++) {
                //每个页面都是inflate出一个新实例
                //这里面传的是GridView的布局
                 grid = (GridView) layoutInflater.inflate(R.layout.item_viewpager, myStampViewPager, false);
                //给GridView设置Adapter，传入index
                adapter = new GridViewAdapter(this, mList, i, mBitmap, flag);
                grid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //加入到ViewPager的View数据集中
                mViewPagerList.add(grid);
            }

            adapter.SetDataList(MyStampActivity.this);// 回调刷新邮集数据
            MyStampViewPagerAdapter mMyStampViewPagerAdapter = new MyStampViewPagerAdapter(mViewPagerList);
            myStampViewPager.setAdapter(mMyStampViewPagerAdapter);

        }else{
            MyLog.LogShitou("========走这了吗=============", "========走这了吗=============");
            GoneOrVisibleView();
        }
    }

    private void initView() {
        mToken = sp.getString("token", "");
        mUser_id = sp.getString("userId", "");

        mBack = (ImageView) mMyStampTitle.findViewById(R.id.base_title_back);
        mMore = (ImageView) mMyStampTitle.findViewById(R.id.base_more);
        //初始化ViewPager
        myStampViewPager = (ViewPager) mMyStampContent.findViewById(R.id.my_content_Viewpager);
        mContentTotalLl = (LinearLayout) mMyStampContent.findViewById(R.id.my_content_total_ll);// 总资产值ll
        mContentTotal = (TextView) mMyStampContent.findViewById(R.id.my_content_total);// 总资产值
        mNoOrderTv = (TextView) mMyStampContent.findViewById(R.id.no_order_tv);// 无信息显示的布局
    }

    // mList为空时显示的布局
    private void GoneOrVisibleView() {
        mMore.setVisibility(View.GONE);// 更多隐藏
        myStampViewPager.setVisibility(View.GONE); // viewpager隐藏
        mContentTotalLl.setVisibility(View.GONE); // 总资产隐藏
        mNoOrderTv.setVisibility(View.VISIBLE); // 无信息控件显示
        mNoOrderTv.setText("暂无邮集信息~");
    }

    private void initData() {
        GetInitNet(num);// 我的邮集列表网络请求方法
    }

    private void initAdapter() {
        //设置翻页效果
        myStampViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position <= 0) {
                    page.setPivotX(page.getMeasuredWidth());
                    page.setPivotY(0);
                    page.setScaleX(1 + position);
                } else if (position <= 1) {
                    page.setPivotX(0);
                    page.setPivotY(0);
                    page.setScaleX(1 - position);
                }
            }
        });
        myStampViewPager.setAdapter(new MyStampViewPagerAdapter(mViewPagerList));
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mMore.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_title_back://返回按钮
                finishWitchAnimation();
                break;
            case R.id.base_more://更多按钮
                MorePopupWindow();
                break;
        }
    }

    /**
     * 点击更多
     */
    private void MorePopupWindow() {
        // 判断选择性别弹出的View是否为空
        if (MorePopView == null) {
            MorePopView = View.inflate(this, R.layout.mystamp_dialog, null);
        }
        if (mMorePop == null) {
            mMorePop = new PopupWindow(MorePopView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 为了响应返回键和界面外的其他界面
        mMorePop.setBackgroundDrawable(mColorBg);
        //把他改为屏幕中间了
        mMorePop.showAsDropDown(mMore, mMore.getWidth() / 2, 0);
        mMorePop.setOutsideTouchable(true);

        mEdit = (TextView) MorePopView.findViewById(R.id.mystamp_edit);
        mShared = (TextView) MorePopView.findViewById(R.id.mystamp_shared);
        // 编辑
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.LogShitou("=============mList数据", mList.toString());
                EditClickDispose(); // 编辑按钮的处理方法
            }
        });
        // 分享
        mShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedDialog(); // 分享弹出Dialog
                mMorePop.dismiss();
            }
        });
    }

    /**
     * 编辑按钮的处理方法
     */
    private void EditClickDispose() {
        MyLog.LogShitou("=========这个List是啥数据", mList.toString());
        mMorePop.dismiss();
        if (mList != null && mList.size() != 0) {
            flag = !flag;
            if (flag) {//编辑状态
                mEditFlag = 1;
                mEdit.setText("完成");

            } else {//非编辑状态
                mEditFlag = 2;
                mEdit.setText("编辑");
            }
            mViewPagerList.clear();
            //使用For循环创建页面,并添加到集合中去
            for (int i = 0; i < pageCount; i++) {
                //每个页面都是inflate出一个新实例
                //这里面传的是GridView的布局
                GridView grid = (GridView) layoutInflater.inflate(R.layout.item_viewpager, myStampViewPager, false);
                //给GridView设置Adapter，传入index
                adapter = new GridViewAdapter(this, mList, i, mBitmap, flag);
                adapter.notifyDataSetChanged();
                grid.setAdapter(adapter);
                //加入到ViewPager的View数据集中
                mViewPagerList.add(grid);
            }
            adapter.SetDataList(MyStampActivity.this);// 回调刷新邮集数据
            MyStampViewPagerAdapter mMyStampViewPagerAdapter = new MyStampViewPagerAdapter(mViewPagerList);
            myStampViewPager.setAdapter(mMyStampViewPagerAdapter);
        }

    }


    /**
     * 我的邮集列表网络请求
     *
     * @param num 初始化索引
     */
    private void GetInitNet(final int num) {
        ThreadManager.getInstance().createShortPool().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(StaticField.SERVICE_TYPE, StaticField.ALBUMLIST);// 接口名称
                params.put(StaticField.TOKEN, mToken);// 标识
                params.put(StaticField.USER_ID, mUser_id);// 用户ID
                params.put(StaticField.CURRENT_INDEX, String.valueOf(num)); // 当前记录索引
                params.put(StaticField.OFFSET, String.valueOf(offsetnum)); // 步长(item条目数)

                String mapSort = SortUtils.MapSort(params);
                String md5code = Encrypt.MD5(mapSort);
                params.put(StaticField.SIGN, md5code);

                result = HttpUtils.submitPostData(StaticField.ROOT, params);
                MyLog.LogShitou("我的邮集List-->:", result);

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
     * 分享弹出Dialog
     */

    private void SharedDialog() {
        dialog = new SharedDialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        mWeiXin = (ImageView) dialog.findViewById(R.id.weixin);
        mPengYouQuan = (ImageView) dialog.findViewById(R.id.pengyouquan);
        // 外部View 点击关闭dialog
        dialog_finsih = dialog.findViewById(R.id.shared_finish);
        dialog_finsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 取消
        tv_cancel = (TextView) dialog.findViewById(R.id.shared_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 微信
        mWeiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedDes(SHARE_MEDIA.WEIXIN);
                dialog.dismiss();
            }
        });
        // 朋友圈
        mPengYouQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedDes(SHARE_MEDIA.WEIXIN_CIRCLE);
                dialog.dismiss();
            }
        });

    }

    /**
     * 调起微信分享的方法
     *
     * @param share_media // 分享类型
     */
    private void SharedDes(SHARE_MEDIA share_media) {
//        UMImage image = new UMImage(this.getApplicationContext(), mSharedImage);
        ShareAction shareAction = new ShareAction(this);
        shareAction.withText("微信分享"); // 显示的内容
//        shareAction.withMedia(image);// 显示图片的url
        shareAction.withTitle("我的邮集");// 标题
        shareAction.withTargetUrl(mShare_url); // 分享后点击查看的地址url
        shareAction.setPlatform(share_media); // 分享类型
        shareAction.setCallback(this);// 回调监听
        shareAction.share();
    }


    /**
     * 友盟微信分享回调 (成功，失败，取消)
     *
     * @param share_media 分享类型
     */
    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Toast.makeText(MyStampActivity.this, "已分享", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享11", "" + share_media);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//        Toast.makeText(MyStampActivity.this, " 分享失败" , Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享22", share_media + "----" + throwable.getMessage());

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
//        Toast.makeText(MyStampActivity.this, " 分享取消了", Toast.LENGTH_SHORT).show();
        MyLog.LogShitou("platform分享33", share_media + "");
    }


}
